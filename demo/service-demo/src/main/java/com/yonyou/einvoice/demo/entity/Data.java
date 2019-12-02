package com.yonyou.einvoice.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回消息实体
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
/**
 * 请求参数的data
 */
public class Data {

  private String code;

  private String msg;

  private String content;

  private String invoiceType;

}
