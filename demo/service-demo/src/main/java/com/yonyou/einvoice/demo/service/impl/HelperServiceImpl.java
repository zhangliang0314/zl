package com.yonyou.einvoice.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.einvoice.demo.service.IHelperService;
import com.yonyou.einvoice.demo.utils.JwtInnerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: zhangwbin
 * @Date: 2019/9/18
 */
@Slf4j
@Service
public class HelperServiceImpl implements IHelperService {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String callBackMessage(String param) {

    String url="http://192.168.52.80/clientmessage/privateapi/clientapp/v2/message";

    HttpHeaders httpHeaders = new HttpHeaders();

    httpHeaders.add("sign", JwtInnerUtils.sign(null));
    httpHeaders.add("Content-Type", "application/json");
    httpHeaders.add("Accept","application/json, application/xml, text/json, text/x-json, text/javascript, text/xml");
    HttpEntity<String> httpEntity=new HttpEntity<>(param,httpHeaders);
    ResponseEntity<JSONObject> responseEntity=restTemplate.postForEntity(url,httpEntity,JSONObject.class);
    return responseEntity.getBody().toJSONString();
  }
}
