package com.yonyou.einvoice.demo.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import com.yonyou.einvoice.demo.dto.MqMessageDto;
import com.yonyou.einvoice.demo.entity.Context;
import com.yonyou.einvoice.demo.entity.Data;
import com.yonyou.einvoice.demo.dto.DataDto;
import com.yonyou.einvoice.demo.enums.ErrorCodeEnum;
import com.yonyou.einvoice.demo.enums.InvoiceTypeEnum;
import com.yonyou.einvoice.demo.enums.MessageTypeEnum;
import com.yonyou.einvoice.demo.exception.DemoException;
import com.yonyou.einvoice.demo.service.IHelperService;
import com.yonyou.einvoice.demo.service.IInvoiceService;
import com.yonyou.einvoice.demo.utils.RedisUtil;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 监听MQ队列消息 队列名称：INVOICE-纳税人识别号-税控设备
 */
@Component
@Slf4j
public class InvoiceMqListener {

  private static final String CHARSET = "UTF-8";
  /**
   * 消息最大处理次数
   */
  private static final int MAX_RETRY_TIME = 3;

  @Autowired
  private IInvoiceService eInvoiceService;
  @Autowired
  private IInvoiceService pInvoiceService;

  @Autowired
  private IHelperService helperService;

  @Autowired
  private RedisUtil redisUtil;

  /**
   * 监听队列消息并进行处理
   *
   * @param message
   * @param channel
   */
  @RabbitListener(queues = {"${invoice.queue.name1}",
      "${invoice.queue.name2}"}, containerFactory = "singleListenerContainer")
  public void consumeQueue(Message message, Channel channel) {
    boolean isAck = false;

    try {
      //判断是否要进行消费
      boolean isContinue = preProcess(message, channel);
      if (!isContinue) {
        isAck = true;
        return;
      }

      //接收消息并转换成字符串
      byte[] body = message.getBody();
      String messageStr = new String(body, "gbk");
      log.info("监听到消息： {} ", messageStr);
      //将字符串转换我包装的对象
      MqMessageDto mqMessageDto = JSON.parseObject(messageStr,
          new TypeReference<MqMessageDto>() {
          });
      log.info("MQ消息转换后的包装对象：mqMessageDto ：{} ", mqMessageDto);
      //判断消息类型是否是发票
      if (Objects
          .equals(mqMessageDto.getContext().getType(), MessageTypeEnum.InvoiceApply.name())) {
        //数据格式转换并存储
        DataDto dataDto = JSON.parseObject(mqMessageDto.getData().toString(),
            new TypeReference<DataDto>() {
            });
        log.info("MQ消息Data参数的包装对象：DataDto ：{} ", dataDto);
        IInvoiceService service = getInvoiceService(dataDto.getType());
        log.info("开始文件操作");
        String resultXml = service.invoiceOutPut(dataDto);
        //调用税控设备生成发票
        MqMessageDto requestForm = buildMqMessageDto(mqMessageDto, dataDto, resultXml);
        log.info("请求参数：requestForm：{}",requestForm.toString());
        String helperResult = helperService.callBackMessage(JSON.toJSONString(requestForm));
        log.info("调用助手消息回传，helperResult:{}", helperResult);
      }else if (Objects.equals(mqMessageDto.getContext().getType(), MessageTypeEnum.PDF_GEN_RESULT.name())){
        //如果是回传的消息不进行处理
      }
    } catch (Exception e) {
      isAck = true;
      log.error(e.getMessage(), e);
      doNack(message, channel);
      throw new DemoException(e.getMessage());
    } finally {
      if (!isAck) {
        this.doAck(message, channel);
      }
    }

  }

  /**
   * 构造调用openApi的请求参数
   *
   * @param mqMessageDto
   * @param dataDto
   * @param resultXml
   */
  private MqMessageDto buildMqMessageDto(MqMessageDto mqMessageDto, DataDto dataDto,
      String resultXml) {
    Data data = new Data();
    data.setCode(ErrorCodeEnum.OK.getCode());
    data.setMsg(ErrorCodeEnum.OK.getMsg());
    data.setContent(resultXml);
    data.setInvoiceType(dataDto.getType());
    String data2Base64 = Base64.getEncoder()
        .encodeToString(JSONObject.toJSONString(data).getBytes());
    Context context = new Context();
    BeanUtils.copyProperties(mqMessageDto.getContext(), context);
    context.setType(MessageTypeEnum.TaxEquipmentResult.name());

    mqMessageDto.setContext(context);
    mqMessageDto.setData(data2Base64);

    return mqMessageDto;
  }


  /**
   * 根据发票类型选择相应业务处理逻辑
   *
   * @param invoiceType
   * @return
   */
  public IInvoiceService getInvoiceService(String invoiceType) {
    switch (InvoiceTypeEnum.valueOf(invoiceType)) {
      case AutoEinvoice:
        return eInvoiceService;
      case AutoPaperInvoice:
        return pInvoiceService;
      default:
        throw new DemoException("暂不支持的类型");
    }
  }


  /**
   * 查询缓存是否存在key
   *
   * @param message
   * @param channel
   * @return
   * @throws ExecutionException
   */
  private boolean preProcess(Message message, Channel channel) {
    if (!StringUtils.isEmpty(redisUtil.get("MessageId:" + getKey(message)))) {
      redisUtil.remove("MessageId:"+getKey(message));
      doAck(message, channel);
      return false;
    }
    redisUtil.set("MessageId:" + getKey(message), getKey(message));
    redisUtil.expire("MessageId:" + getKey(message), 600);
    return true;
  }

  /**
   * 获取MessageId全局唯一标识,判断消费方使用同一个， 可用于解决因为网络或者客户端延迟的原因导致重复消费（幂等性问题）： 具体方式：把该 ID 存至 redis中，下次再调用时，先去
   * redis 判断是否存在该 ID 了， 如果存在表明已经消费过了则直接返回，不再消费， 否则消费，然后将记录存至 redis。
   *
   * @param message
   * @return
   */
  private String getKey(Message message) {
    return message.getMessageProperties().getMessageId();
  }


  /**
   * 消费成功应答
   *
   * @param message
   * @param channel
   */
  public void doAck(Message message, Channel channel) {
    try {
      channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (IOException e) {
      log.error("消息Ack错误", e);
      throw new DemoException(e.getMessage());
    }
  }

  /**
   * 消费失败应答 将消息重回队列末端 ——将basicNack最后一个参数设为 true
   *
   * @param message
   * @param channel
   */
  private void doNack(Message message, Channel channel) {
    try {
      channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
    } catch (IOException e) {
      log.error("消息Nack错误", e.getMessage());
      throw new DemoException(e.getMessage());
    }
  }

}
