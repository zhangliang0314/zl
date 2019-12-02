package com.yonyou.einvoice.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

@Slf4j
public class ReviseXmlUtil {


  public static String changeEncoding(String strXML) {

    Document doc = null;
    try {
      doc = DocumentHelper.parseText(strXML);
      doc.setXMLEncoding("GBK");
      return doc.asXML();
    } catch (DocumentException e) {
      log.error("XML编码格式设置异常",e.getMessage());
    }
    return null;
  }

}
