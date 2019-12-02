package com.yonyou.einvoice.demo.utils;

import java.util.Random;

/**
 * 随机生成数字工具类
 */

public class RandomCharDataUtil {

  private static String[] areaCode=new String[]{"0000","3300","1100","2200","4400"};

  /**  根据指定长度生成字母和数字的随机数
          0~9的ASCII为48~57
          A~Z的ASCII为65~90
          a~z的ASCII为97~122
   */
  public static String createRandomCharData(int length)
  {
    StringBuilder sb=new StringBuilder();
    Random rand=new Random();
    Random randdata=new Random();
    int data=0;
    for(int i=0;i<length;i++)
    {
      int index=rand.nextInt(1);
      //目的是随机选择生成数字，大小写字母
      switch(index)
      {
        case 0:
          //保证只会产生97~122之间的整数(字符)
          data=randdata.nextInt(26)+97;
          sb.append((char)data);
          break;
          default:
            //仅仅会生成0~9
            data=randdata.nextInt(10);
            sb.append(data);
            break;
      }
    }
    String result=sb.toString();
    return result;
  }

  /**
   * 根据指定长度生成纯数字的随机数
   */

  public static String createData(int length) {
    StringBuilder sb=new StringBuilder();
    Random rand=new Random();
    for(int i=0;i<length;i++)
    {
      sb.append(rand.nextInt(10));
    }
    String data=sb.toString();
    return data;
  }

  /**
   * 创建发票代码随机数
   * @return
   */
  public static String createFPDM(){
    StringBuilder sb=new StringBuilder();
    Random rand=new Random();
    for(int i=1;i<=12;i++){
      if(i==1){
        sb.append(0);
        continue;
      }
      if(i==2){
        sb.append(areaCode[rand.nextInt(5)]);
        i=5;
        continue;
      }
      sb.append(rand.nextInt(10));
    }
    String data=sb.toString();
    return data;
  }
}
