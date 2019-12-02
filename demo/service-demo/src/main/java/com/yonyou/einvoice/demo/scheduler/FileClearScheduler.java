package com.yonyou.einvoice.demo.scheduler;

import com.yonyou.einvoice.demo.service.IInvoiceFileService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FileClearScheduler {

  @Autowired
  private DateTimeFormatter dateTimeFormateyyyyMMdd;
  @Autowired
  private IInvoiceFileService iInvoiceFileService;

  /**
   * 每天0点触发一次，清除前一天的文件
   * http://cron.qqe2.com/
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void fileCleaning() {
    LocalDateTime before = LocalDateTime.now().minusDays(1);
    String beforeStr = before.format(dateTimeFormateyyyyMMdd);
    try {
      iInvoiceFileService.deleteExpire(beforeStr);
    } catch (IOException e) {
      log.error("删除过期文件失败", e);
    }
  }

}
