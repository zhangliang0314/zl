/**
 * 
 */
package com.yonyou.einvoice.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import java.util.Map;

/**
 * piaoeda内部各模块restful服务调用签名、验签工具类
 * 
 * @author wangweir
 *
 */
public class JwtInnerUtils {

  /**
   *
   */
  private static final String INNER_KEY = "com.yonyou.einvoice";

  /**
   *
   */
  private static final String PIAOEDA = "piaoeda";

  /**
   *
   */
  private static final String YONYOU_EINVOICE = "yonyou einvoice";


  /**
   * 签名
   *
   * @param datas 签名数据
   */
  public static String sign(Map<String, Object> datas) {
    Map<String, Object> claims =
        JwtParamBuilder.build().setSubject(PIAOEDA).setIssuer(YONYOU_EINVOICE).setAudience(PIAOEDA)
            .addJwtId().addIssuedAt().setExpirySeconds(300).setNotBeforeSeconds(300).getClaims();
//    if (MapUtils.isNotEmpty(datas)) {
    if (datas != null && datas.size() > 0) {
      claims.putAll(datas);
    }
    String compactJws =
        Jwts.builder().signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(INNER_KEY))
            .setClaims(claims).compressWith(CompressionCodecs.DEFLATE).compact();

    return compactJws;
  }

}
