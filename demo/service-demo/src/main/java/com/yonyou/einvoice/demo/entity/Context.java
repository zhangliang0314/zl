package com.yonyou.einvoice.demo.entity;

import lombok.AllArgsConstructor;
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
 * 请求参数的context
 */
public class Context {
  /**
   * 公司id
   */
  private String corpid;
  /**
   * 税控设备编号
   */
  private String equipmentCode;
  /**
   * 消息id,后续用来控制无限重传
   */
  private String id;
  /**
   * 纳税人识别号
   */
  private String nsrsbh;
  /**
   * 消息类型，如"InvoiceApply"
   */
  private String type;

}
