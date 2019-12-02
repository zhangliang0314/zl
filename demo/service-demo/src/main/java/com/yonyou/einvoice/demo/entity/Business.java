package com.yonyou.einvoice.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Business {

  @JSONField(name = "RESPONSE_COMMON_FPKJ")
  ResponseCommonFpkj responseCommonFpkj;
}
