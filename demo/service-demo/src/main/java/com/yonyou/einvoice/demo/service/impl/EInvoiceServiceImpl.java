package com.yonyou.einvoice.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.github.pkuliuqiang.XMLTransformFacade;
import com.yonyou.einvoice.demo.entity.Business;
import com.yonyou.einvoice.demo.dto.DataDto;
import com.yonyou.einvoice.demo.entity.ResponseCommonFpkj;
import com.yonyou.einvoice.demo.entity.ResultEInvoice;
import com.yonyou.einvoice.demo.service.IInvoiceFileService;
import com.yonyou.einvoice.demo.service.IInvoiceService;
import com.yonyou.einvoice.demo.utils.RandomCharDataUtil;
import com.yonyou.einvoice.demo.utils.ReviseXmlUtil;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service("eInvoiceService")
public class EInvoiceServiceImpl implements IInvoiceService {

  /**
   * 发票密文
   */
  private static final String FPMW = "<-<>48938<4+<14>735+<2554*8-1-<+15<*026+848686/2/3//0>+*>>>356*<757/47>90+<25<<3575**934<+15<*026+848686--57";

  @Autowired
  private IInvoiceFileService iInvoiceFileService;

  @Override
  public String invoiceOutPut(DataDto dataDto)
      throws UnsupportedEncodingException {
    log.info("生成电子发票：fpqqlsh:{}",dataDto.getFpqqlsh());
    String fileName = "电子发票_" + dataDto.getFpqqlsh() + ".xml";
    iInvoiceFileService.saveEInput(fileName,dataDto.getData().getBytes("gbk"));

    String outPutData = getXmlResult(dataDto);
    log.info("xml字符串：xmlStr：{}",outPutData);
    iInvoiceFileService.saveEOutPut("电子发票_" + dataDto.getFpqqlsh() + "_result.xml",
        outPutData.getBytes("gbk"));
    return outPutData;
}

  private String getXmlResult(DataDto dataDto) {
    ResultEInvoice resultEInvoice = buildResultEInvoice(dataDto.getFpqqlsh());
    log.info("构建xml信息的实体对象：ResultInvoice：{}",resultEInvoice);
    JSONObject jsonObject = JSONObject
        .parseObject(JSON.toJSONString(resultEInvoice), Feature.OrderedField);

    return ReviseXmlUtil.changeEncoding(XMLTransformFacade.getXMLStrFromJSONObject(jsonObject));
  }

  /**
   * 构造返回的xml格式的字段实体
   * @param fpqqqlsh
   * @return
   */
  private static ResultEInvoice buildResultEInvoice(String fpqqqlsh){

    ResponseCommonFpkj responseCommonFpkj = new ResponseCommonFpkj(
        fpqqqlsh,
        "312099511551",
        RandomCharDataUtil.createFPDM(),
        RandomCharDataUtil.createRandomCharData(8),
        new Date(),
        FPMW,
        "97462042493738665641",
        "0000",
        "发票开具成功"
    );

    Business business = new Business(responseCommonFpkj);

    ResultEInvoice resultEInvoice = new ResultEInvoice(business);

    return resultEInvoice;
  }
}
