package com.yonyou.einvoice.demo.config;

import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateFormateConfig {

  @Bean(name = "dateTimeFormateyyyyMMdd")
  public DateTimeFormatter getDateTimeFormatteryyyyMMdd(){
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
    return format;
  }
  @Bean(name = "dateTimeFormateyyyyMMddHHmmss")
  public DateTimeFormatter getDateTimeFormatteryyyyMMddHHmmss(){
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return format;
  }
}
