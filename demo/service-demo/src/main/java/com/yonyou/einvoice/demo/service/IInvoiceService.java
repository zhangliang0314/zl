package com.yonyou.einvoice.demo.service;

import com.yonyou.einvoice.demo.dto.DataDto;
import java.io.UnsupportedEncodingException;

public interface IInvoiceService {

  /**
   * 保存inputData，并生成发票data,保存 outputData
   * @param dataDto
   * @return
   * @throws UnsupportedEncodingException
   */
  String invoiceOutPut(DataDto dataDto) throws UnsupportedEncodingException;

}
