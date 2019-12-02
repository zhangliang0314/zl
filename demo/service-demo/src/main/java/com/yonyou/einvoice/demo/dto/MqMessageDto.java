package com.yonyou.einvoice.demo.dto;

import com.yonyou.einvoice.demo.entity.Context;
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
 * 接收到MQ消息转换成openapi请求参数json对象的封装类
 */
public class MqMessageDto<T> {

  private Context context;

  private T data;
}
