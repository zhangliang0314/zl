package com.yonyou.einvoice.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.github.pkuliuqiang.XMLTransformFacade;
import com.yonyou.einvoice.demo.dto.DataDto;
import com.yonyou.einvoice.demo.entity.Business;
import com.yonyou.einvoice.demo.entity.ResponseCommonFpkj;
import com.yonyou.einvoice.demo.entity.ResultEInvoice;
import com.yonyou.einvoice.demo.service.IInvoiceFileService;
import com.yonyou.einvoice.demo.service.IInvoiceService;
import com.yonyou.einvoice.demo.utils.RandomCharDataUtil;
import com.yonyou.einvoice.demo.utils.ReviseXmlUtil;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Slf4j
@Service("pInvoiceService")
public class PInvoiceServiceImpl implements IInvoiceService {


  @Autowired
  private IInvoiceFileService iInvoiceFileService;

  @Autowired
  @Qualifier(value = "dateTimeFormateyyyyMMddHHmmss")
  private DateTimeFormatter dateTimeFormateyyyyMMddHHmmss;

  @Override
  public String invoiceOutPut(DataDto dataDto)
      throws UnsupportedEncodingException {
    log.info("生成纸质发票：fpqqlsh:{}",dataDto.getFpqqlsh());
    String fileName = "纸质发票_" + dataDto.getFpqqlsh() + ".xml";
    iInvoiceFileService.savePInput(fileName,dataDto.getData().getBytes("gbk"));

    String outPutData = getPaperResult(dataDto);
    log.info("构建纸质开票结果：{}",outPutData);
    iInvoiceFileService.savePOutPut("纸质发票_" + dataDto.getFpqqlsh() + "_result.xml",
        outPutData.getBytes("gbk"));
    return outPutData;
  }

  /**
   * 构造返回的纸质开票结果
   * @param dataDto
   * @return
   */
  private String getPaperResult(DataDto dataDto) {
    StringBuilder sb = new StringBuilder("["+LocalDateTime.now().format(dateTimeFormateyyyyMMddHHmmss)+"]");
    sb.append("   ")
        .append("单据号：" + dataDto.getFpqqlsh())
        .append(",开具结果：" + 1)
        .append(",对应发票信息：增值税普通发票")
        .append(RandomCharDataUtil.createFPDM())
        .append(RandomCharDataUtil.createRandomCharData(8));

    return sb.toString();

  }

}
