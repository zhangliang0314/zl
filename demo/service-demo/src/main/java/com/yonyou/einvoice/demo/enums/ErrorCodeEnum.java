package com.yonyou.einvoice.demo.enums;

import lombok.Getter;


@Getter

public enum ErrorCodeEnum {
  /**
   * 错误码枚举
   */
  OK("0000", "操作成功"),
  UnknowError("9999", "未知错误"),
  IllParamError("1001", "参数不合法"),
  DataNotExistError("1002", "数据不存在"),
  FileTypeError("1003", "文件类型不合法"),
  FileSizeError("1004", "文件大小不合法"),
  ;

  private String code;

  private String msg;

  ErrorCodeEnum(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
