package com.tydic.lbs.util;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * DES加密和解密工具,可以对字符串进行加密和解密操作  。 
 */
public class CSSSpassword {
  
  /** 字符串默认键值     */
  private static String dic_strDefaultKey = "dic#csss^tpss*2013!&";

  /** 加密工具     */
  private Cipher encryptCipher = null;

  /** 解密工具     */
  private Cipher decryptCipher = null;

  /**  
   * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]  
   * hexStr2ByteArr(String strIn) 互为可逆的转换过程  
   *   
   * @param arrB  
   *            需要转换的byte数组  
   * @return 转换后的字符串  
   * @throws Exception  
   *             本方法不处理任何异常，所有异常全部抛出  
   */
  public static String byteArr2HexStr(byte[] arrB) throws Exception {
    int iLen = arrB.length;
    // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍   
    StringBuffer sb = new StringBuffer(iLen * 2);
    for (int i = 0; i < iLen; i++) {
      int intTmp = arrB[i];
      // 把负数转换为正数   
      while (intTmp < 0) {
        intTmp = intTmp + 256;
      }
      // 小于0F的数需要在前面补0   
      if (intTmp < 16) {
        sb.append("0");
      }
      sb.append(Integer.toString(intTmp, 16));
    }
    return sb.toString();
  }

  /**  
   * @param strIn  
   *             
   * @return   
   * @throws Exception  
   *              
   */
  public static byte[] hexStr2ByteArr(String strIn) throws Exception {
    byte[] arrB = strIn.getBytes();
    int iLen = arrB.length;

    // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2   
    byte[] arrOut = new byte[iLen / 2];
    for (int i = 0; i < iLen; i = i + 2) {
      String strTmp = new String(arrB, i, 2);
      arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
    }
    return arrOut;
  }

  /**  
   *   
   * @throws Exception  
   */
  public CSSSpassword() throws Exception {
    this(dic_strDefaultKey);
  }

  /**  
   *   
   * @param strKey  
   * @throws Exception  
   */
  public CSSSpassword(String strKey) throws Exception {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    Key key = getKey(strKey.getBytes());

    encryptCipher = Cipher.getInstance("DES");
    encryptCipher.init(Cipher.ENCRYPT_MODE, key);

    decryptCipher = Cipher.getInstance("DES");
    decryptCipher.init(Cipher.DECRYPT_MODE, key);
  }

  /**  
   *   
   * @param arrB  
   * @return  
   * @throws Exception  
   */
  public byte[] encrypt(byte[] arrB) throws Exception {
    return encryptCipher.doFinal(arrB);
  }

  /**  
   *   
   * @param strIn  
   * @return   
   * @throws Exception  
   */
  public String encrypt(String strIn) throws Exception {
    return byteArr2HexStr(encrypt(strIn.getBytes()));
  }

  /**  
   *   
   * @param arrB  
   *               
   * @return  
   * @throws Exception  
   */
  public byte[] decrypt(byte[] arrB) throws Exception {
    return decryptCipher.doFinal(arrB);
  }

  /**  
   *   
   * @param strIn  
   * @return  
   * @throws Exception  
   */
  public String decrypt(String strIn) throws Exception {
    return new String(decrypt(hexStr2ByteArr(strIn)));
  }

  /**  
   *   
   * @param arrBTmp  
   * @return 
   * @throws java.lang.Exception  
   */
  private Key getKey(byte[] arrBTmp) throws Exception {
    // 创建一个空的8位字节数组（默认值为0）   
    byte[] arrB = new byte[8];

    // 将原始字节数组转换为8位   
    for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
      arrB[i] = arrBTmp[i];
    }

    // 生成密钥   
    Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

    return key;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      //大渠道传过来的参数请使用post方式提交
    /***
     * 原始的数据：data=QueryType=01&DepartType=UNIT_LABLE_01&ObjType=chnList&ObjCode=3604041000484,3604031011596,3604091011593,3604111001449&IsComplain=0
     * 加密后的数据:data=dc06da2e38762240ed54a94aeb7a04fb307d3019845906660a646b9d8498fbcf33016f581139c566e269445bdd10e5577e9030bbea59379c0f12bc266a4828eb0b70e2cb8b79b69b8dfd30c109b0591aff9281e5bbae88664b92180ba653117292ce10792a824c648367e48448372252a9c256f3477c5082fc52f55c532b55e069a76b99bfad1d4c
     */
    	
      //String test = "QueryType=01&DepartType=UNIT_LABLE_01&ObjType=chnList&ObjCode=3604041000484,3604031011596,3604091011593,3604111001449&IsComplain=0&privarea=";
       String test = "QueryType=02&DepartType=&ObjType=staff&ObjCode=wt126200&privarea=36010102&IsComplain=0";

      CSSSpassword encodeDesc = new CSSSpassword("dic#csss^tpss*2013!&");//加密密钥   
      System.out.println("加密前的字符：" + test);
      String result=encodeDesc.encrypt(test);
      System.out.println("加密后的字符：" + result);
      CSSSpassword decodeDesc = new CSSSpassword("dic#csss^tpss*2013!&");//解密密钥   
      String decodeResult=decodeDesc.decrypt(result);
      System.out.println("解密后的字符：" + decodeResult);
      //
      System.out.println("解析后的参数-----------------------");
      //解析URL参数
      String params[]=decodeResult.split("&");
      for(String param:params){
    	  String tempArr[]=param.split("=");
    	  String key="";
    	  String value="";
    	  if(tempArr.length>1){
    		  key=param.split("=")[0];
        	  value=param.split("=")[1];
    	  }else if(tempArr.length==1){
    		  key=param.split("=")[0];
    	  }
    	  System.out.println("param:"+key+",value:"+value);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

