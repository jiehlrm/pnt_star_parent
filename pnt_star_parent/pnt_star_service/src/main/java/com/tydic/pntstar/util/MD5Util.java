package com.tydic.pntstar.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MD5Util{

	  private final static String[] hexDigits = {
	      "0", "1", "2", "3", "4", "5", "6", "7",
	      "8", "9", "a", "b", "c", "d", "e", "f"};

	  /**
	   * 转换字节数组为16进制字串
	   * @param b 字节数组
	   * @return 16进制字串
	   */
	  public static String byteArrayToString(byte[] b) {
	    StringBuffer resultSb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	     resultSb.append(byteToHexString(b[i]));//若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
	     //resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
	    }
	    return resultSb.toString();
	  }

	  private static String byteToNumString(byte b) {

	    int _b = b;
	    if (_b < 0) {
	      _b = 256 + _b;
	    }

	    return String.valueOf(_b);
	  }

	  private static String byteToHexString(byte b) {
	    int n = b;
	    if (n < 0) {
	      n = 256 + n;
	    }
	    int d1 = n / 16;
	    int d2 = n % 16;
	    return hexDigits[d1] + hexDigits[d2];
	  }

	  public static String MD5Encode(String origin) {
	    String resultString = null;

	    try {
	      resultString = new String(origin);
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      byte [] bytes= md.digest(resultString.getBytes());
	      resultString = byteArrayToString(bytes);
	    }
	    catch (Exception ex) {

	    }
	    return resultString;
	  }

	  public static void main(String[] args) {	
		  Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHH");
	    String CurrentTime= sdf.format(date);
	    String PrivateKey="1_pntstar001&2018";
		String origin=PrivateKey+CurrentTime;
		  String string=MD5Util.MD5Encode(origin);
	    System.out.println(string);
	  }
}
