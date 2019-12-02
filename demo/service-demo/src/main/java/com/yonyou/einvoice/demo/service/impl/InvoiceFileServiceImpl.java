package com.yonyou.einvoice.demo.service.impl;

import com.yonyou.einvoice.demo.service.IInvoiceFileService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Data
@ConfigurationProperties(prefix = "invoice.save.file.path")
public class InvoiceFileServiceImpl implements IInvoiceFileService {

  private String eInput;
  private String eOutput;
  private String pInput;
  private String pOutput;

  @Autowired
  private Executor executor;
  @Autowired
  private DateTimeFormatter dateTimeFormateyyyyMMdd;

  @Override
  public void saveEInput(String fileName, byte[] content) {
    executor.execute(() -> {
      try {
        FileUtils.writeByteArrayToFile(getFileByName(eInput, fileName), content);
      } catch (IOException e) {
        log.error("保存文件异常", e);
      }
    });
  }

  private File getFileByName(String path, String name) {
    LocalDateTime now = LocalDateTime.now();
    StringBuilder filePath = new StringBuilder(path);
    filePath.append(File.separator);
    filePath.append(now.format(dateTimeFormateyyyyMMdd));
    filePath.append(File.separator);
    filePath.append(name);
    return new File(filePath.toString());
  }

  @Override
  public void savePInput(String fileName, byte[] content) {
    executor.execute(() -> {
      try {
        FileUtils.writeByteArrayToFile(getFileByName(pInput, fileName), content);
      } catch (IOException e) {
        log.error("保存文件异常", e);
      }
    });
  }

  @Override
  public void saveEOutPut(String fileName, byte[] content) {
    executor.execute(() -> {
      try {
        FileUtils.writeByteArrayToFile(getFileByName(eOutput, fileName), content);
      } catch (IOException e) {
        log.error("保存文件异常", e);
      }
    });
  }

  @Override
  public void savePOutPut(String fileName, byte[] content) {
    executor.execute(() -> {
      try {
        FileUtils.writeByteArrayToFile(getFileByName(pOutput, fileName), content);
      } catch (IOException e) {
        log.error("保存文件异常", e);
      }
    });
  }

  @Override
  public void deleteExpire(String date) throws IOException {
    String[] filePaths = new String[]{eInput, eOutput, pInput, pOutput};
    for (String filePath : filePaths) {
      File director = new File(filePath + File.separator + date);
      if (!director.exists()) {
        continue;
      }
      String path = director.getPath();
      log.debug("定时任务删除前一天文件，fileName:{}", director.getName());
      FileUtils.deleteDirectory(director);
    }
  }
}
