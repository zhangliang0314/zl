package com.yonyou.einvoice.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoException extends RuntimeException{

  private String code;

  public DemoException(String code,String message){
    super(message);
    this.code = code;
  }

  public DemoException(String message){
    super(message);
  }
}
