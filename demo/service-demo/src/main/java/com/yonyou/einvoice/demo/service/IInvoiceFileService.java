package com.yonyou.einvoice.demo.service;

import java.io.IOException;

/**
 * 文件操作
 */
public interface IInvoiceFileService {

  void saveEInput(String fileName, byte[] content);

  void savePInput(String fileName, byte[] content);

  void saveEOutPut(String fileName, byte[] content);

  void savePOutPut(String fileName, byte[] content);

  void deleteExpire(String date) throws IOException;

}
