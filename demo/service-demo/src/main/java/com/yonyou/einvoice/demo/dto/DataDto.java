package com.yonyou.einvoice.demo.dto;

import com.yonyou.einvoice.demo.enums.InvoiceTypeEnum;
import com.yonyou.einvoice.demo.exception.DemoException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
/**
 * 消息队列中的data参数进行封装
 *        fpqqlsh  用于转换成xml的文件名拼接
 *        data    转换xml的字符串
 *        type    根据发票类型调用不同的接口
 */
public class DataDto {

  private String fpqqlsh;
  private String type;
  private String data;

  public String getType(){
    if(data == null){
      throw new DemoException("data不能为空，接收到的消息有问题！");
    }
    if(data.indexOf("</business>") > 0) {
      type = InvoiceTypeEnum.AutoEinvoice.name();
    } else if(data.indexOf("</Kp>") > 0) {
      type = InvoiceTypeEnum.AutoPaperInvoice.name();
    } else {
      throw new DemoException("data不能识别，接收到的消息有问题！");
    }
    return type;
  }
}
