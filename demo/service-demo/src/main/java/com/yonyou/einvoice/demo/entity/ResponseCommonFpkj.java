package com.yonyou.einvoice.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.Date;
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
 * 电子发票信息
 */
public class ResponseCommonFpkj {
  /**
   * 发票流水号
   */
  @JSONField(name="FPQQLSH",ordinal =1)
  private String fpqqlsh;

  /**
   * 机器编码
   */
  @JSONField(name="JQBH",ordinal =2)
  private String jqbh;

  /**
   * 发票代码
   */
  @JSONField(name="FP_DM",ordinal =3)
  private String fpdm;

  /**
   * 发票号码
   */
  @JSONField(name="FP_HM",ordinal =4)
  private String fphm;

  /**
   * 开票日期
   */
  @JSONField(name="KPRQ",ordinal =5, format = "yyyyMMddHHmmss")
  private Date kprq;

  /**
   * 发票密文
   */
  @JSONField(name="FP_MW",ordinal =6)
  private String fpmw;

  /**
   * 校验码
   */
  @JSONField(name="JYM",ordinal =7)
  private String jym;

  /**
   * 二维码
   */
  @JSONField(name="EWM",ordinal =8, serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
  private String ewm;

  @JSONField(name="BZ",ordinal =9, serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
  private String bz;

  /**
   * 返回代码
   */
  @JSONField(name="RETURNCODE",ordinal =10)
  private String returnCode;

  /**
   * 返回信息
   */
  @JSONField(name="RETURNMSG",ordinal =11)
  private String returnMsg;

  public ResponseCommonFpkj(String fpqqlsh, String jqbh, String fpdm, String fphm,
      Date kprq, String fpmw, String jym, String returnCode, String returnMsg) {
    this.fpqqlsh = fpqqlsh;
    this.jqbh = jqbh;
    this.fpdm = fpdm;
    this.fphm = fphm;
    this.kprq = kprq;
    this.fpmw = fpmw;
    this.jym = jym;
    this.returnCode = returnCode;
    this.returnMsg = returnMsg;
  }
}
