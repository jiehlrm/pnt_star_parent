
package com.tydic.pntstar.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author xkarvy
 *	工具类
 *	常用工具
 */
@SuppressWarnings("unchecked")
public class Tools {
	
    public final static String SUCCESS="0";
    public final static String FAILED="1";
    
    public final static int PAGE_INDEX=0;
    public final static int PAGE_SIZE=10;
	
	/**托收*/
	public static int coll_y = 1;
	/**不托收*/
	public static int coll_n = 0;

	/**
	 * 根据支付方式返回是否托收
	 * 我的E家联名卡	P
	   信用卡付款	C
	   银行代收	M
		银行托收	N
		支票	B
		普通现金	A
		201卡	E
	 * @param payMethod
	 * @return 1托收，0不托收
	 */
	public static int collByPayMethod(String payMethod){
		
		
		if(payMethod.equals("P") 
				|| payMethod.equals("C") 
				|| payMethod.equals("M") 
				|| payMethod.equals("N")
				|| payMethod.equals("B")){
			return coll_y;
		}else{
			return coll_n;
		}

	}
	
	/**
	 * @param objs
	 * @return boolean 是否为空
	 */
	public static boolean isNull(Object[] objs) {
		if (null == objs || objs.length <= 0) {
			return true;
		}
		/*String[]*/
		if(objs instanceof String[]){
			return checkDate((String[])objs);
		}
		/*Object[]*/
		if(objs instanceof Object[]){
			return checkObjects((Object[])objs);
		}
		return false;
	}
	
	/**
	 * @param obj
	 * @return boolean 是否为空
	 */
	public static boolean isNull(Object obj) {
		/*为空*/
		if(obj==null) return true;
		/*String*/
		if(obj instanceof String){
			return checkString((String)obj);
		}
		/*Integer*/
		if(obj instanceof Integer){
			return checkInteger((Integer)obj);
		}
		/*Long*/
		if(obj instanceof Long){
			return checkLong((Long)obj);
		}
		/*Double*/
		if(obj instanceof Double){
			return checkDouble((Double)obj);
		}
		/*Date*/
		if(obj instanceof Date){
			return checkDate((Date)obj);
		}
		/*List*/
		if(obj instanceof List){
			return checkList((List)obj);
		}
		/*Set*/
		if(obj instanceof Set){
			return checkSet((Set)obj);
		}
		/*Map*/
		if(obj instanceof Map){
			return checkMap((Map)obj);
		}
		if(obj instanceof BigDecimal){
			return BigDecimal((BigDecimal)obj);
		}
		
		
		return false;
	}
	
	private static boolean BigDecimal(BigDecimal bigDecimal) {
		if(null==bigDecimal)
			return true;
		return false;
	}
	

	private static boolean checkList(List list) {
		if( list.size()<=0)
			return true;
		return false;
	}
	
	private static boolean checkObjects(Object[] objs) {
		if( objs.length <= 0 )
			return true;
		return false;
	}
	
	private static boolean checkSet(Set set) {
		if(set.isEmpty() || set.size()<=0)
			return true;
		return false;
	}
	
	private static boolean checkMap(Map set) {
		if(set.isEmpty() || set.size()<=0)
			return true;
		return false;
	}
	
	private static boolean checkDate(String[] strings) {
		if(strings.length<=0)
			return true;
		return false;
	}
	private static boolean checkDate(Date date) {
		if(date == null ){
			return true;
		}
		return false;
	}

	private static boolean checkDouble(Double double1) {
		if( double1.doubleValue()==0){
			return true;
		}
		return false;
	}

	private static boolean checkLong(Long long1) {
		if( long1.longValue()==0 || long1.longValue()==-1L ){
			return true;
		}
		return false;
	}

	private static boolean checkInteger(Integer integer) {
		if( integer.intValue()==0 || integer.intValue()==-1 ){
			return true;
		}
		return false;
	}

	private static boolean checkString(String string) {
		if( string.trim().length()<=0 || "null".equalsIgnoreCase(string)){
			return true;
		}
		return false;
	}
	
	
	
	public static Class gatAttrClassType(Object obj){

		/*String*/
		if(obj instanceof String){
			return String.class;
		}
		/*Integer*/
		if(obj instanceof Integer){
			return Integer.class;
		}
		/*Long*/
		if(obj instanceof Long){
			return Long.class;
		}
		/*Double*/
		if(obj instanceof Double){
			return Double.class;
		}
		/*Date*/
		if(obj instanceof Date){
			return Date.class;
		}
		/*Date*/
		if(obj instanceof java.sql.Date){
			return java.sql.Date.class;
		}
		/*Timestamp*/
		if(obj instanceof java.sql.Timestamp){
			return java.sql.Timestamp.class;
		}
		/*List*/
		if(obj instanceof List){
			return List.class;
		}
		/*Set*/
		if(obj instanceof Set){
			return Set.class;
		}
		/*Map*/
		if(obj instanceof Map){
			return Map.class;
		}
		if(obj instanceof BigDecimal){
			return BigDecimal.class;
		}
		
		if(obj instanceof Boolean){
			return Boolean.class;
		}
		
		return Object.class;
	}
	
	
	public final static java.sql.Timestamp string2Time(String dateString,String forMat)  {
		DateFormat dateFormat;
		
		dateFormat = new SimpleDateFormat(forMat, Locale.ENGLISH);//设定格式
		dateFormat.setLenient(false);
		java.util.Date timeDate = null;
		try {
			timeDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
		
		return dateTime;
	 }
	
	
	/**
	 * 计算当天加减
	 * 获取 根据指定格式 计算指定类型
	 * @param format 格式：如"yyyy-MM-dd"
	 * @param type   类型 如:Calendar.YEAR,Calendar.MONTH,Calendar.DATE
	 * @param add    具体家的值
	 * @return String 
	 */
	public static String addDate(String format,int type,int add){
		Calendar cal = Calendar.getInstance();
		int newYear  = cal.get(Calendar.YEAR);
		int newMonth = cal.get(Calendar.MONTH);
		int newDay   = cal.get(Calendar.DATE);
		int newHour  = cal.get(Calendar.HOUR_OF_DAY);
		int newMinute= cal.get(Calendar.MINUTE);
		int newSecond= cal.get(Calendar.SECOND);
		switch(type){
			case Calendar.YEAR:
				newYear += add; 
				break;
			case Calendar.MONTH:
				newMonth += add; 
				break;
			case Calendar.DATE:
				newDay += add; 
				break;
			case Calendar.HOUR_OF_DAY:
				newHour += add; 
				break;
			case Calendar.MINUTE:
				newMinute += add; 
				break;
			case Calendar.SECOND:
				newSecond += add; 
				break;
		}	
		cal.set(newYear, newMonth, newDay,newHour,newMinute,newSecond);
		SimpleDateFormat simpDate = new SimpleDateFormat(format);
		return simpDate.format(cal.getTime());
	}
	
	
	
	/**
	 * 指定日期
	 * 指定格式 
	 * 指定类型
	 * 计算时间
	 * @param inDate 按照format格式指定的时间，如：2010-02-02
	 * @param format 格式：如"yyyy-MM-dd"
	 * @param type   类型 如:Calendar.YEAR,Calendar.MONTH,Calendar.DATE
	 * @param add    具体增加的值
	 * @return string
	 */
	public static String addDateByTime(String inDate,String format,int type,int add) {
		SimpleDateFormat simpDate = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		try
		{
			cal.setTime(simpDate.parse(inDate));
			int newYear  =  cal.get(Calendar.YEAR);
			int newMonth =  cal.get(Calendar.MONTH);
			int newDay   = cal.get(Calendar.DATE);
			int newHour  = cal.get(Calendar.HOUR_OF_DAY);
			int newMinute= cal.get(Calendar.MINUTE);
			int newSecond= cal.get(Calendar.SECOND);
			switch(type){
				case Calendar.YEAR:
					newYear += add; 
					break;
				case Calendar.MONTH:
					newMonth += add; 
					break;
				case Calendar.DATE:
					newDay += add; 
					break;
				case Calendar.HOUR_OF_DAY:
					newHour += add; 
					break;
				case Calendar.MINUTE:
					newMinute += add; 
					break;
				case Calendar.SECOND:
					newSecond += add; 
					break;
			}	
			cal.set(newYear, newMonth, newDay,newHour,newMinute,newSecond);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return simpDate.format(cal.getTime());
	}
	
	/**
	 * 时间格式化
	 * @param inDate   原始时间
	 * @param inFormat 原始时间格式
	 * @param outFormat 改变格式
	 * @return
	 */
	public static String dateFormat(String inDate,String inFormat,String outFormat) {
		
		SimpleDateFormat outDate = new SimpleDateFormat(outFormat);
		
		Calendar cal =  Calendar.getInstance();;
		try {
			SimpleDateFormat simpDate = new SimpleDateFormat(inFormat);
			cal.setTime(simpDate.parse(inDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outDate.format(cal.getTime());
	}
	
	/**
	 * date1是否大于date2
	 * @param format
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateCompare(String format,String date1,String date2){
		DateFormat df = new SimpleDateFormat(format);
		try{
		  Date d1 = df.parse(date1);
		  Date d2 = df.parse(date2);
		  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
		  // long days = diff / (1000 * 60 * 60 * 24);
		  //long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
		  //long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		  //System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
		 
		  if(diff>0){
			  return true;
		  }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/** 
	 * 将InputStream转换成某种字符编码的String 
	 * @param in 
	 * @param encoding 
	 * @return 
	 * @throws Exception 
	 */
	public static String InputStream2String(InputStream in, String encoding)
			throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int count = -1;
		while ((count = in.read(data, 0, 1024)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "ISO-8859-1");
	}  
	
	
	
	/**
	 * 根据制定类型转换业务类型
	 * @param value
	 * @param type
	 * @return
	 * @throws ParseException 
	 */
	public static Object setPropertyType(Object value , Class type) throws ParseException{

		/*Char*/
		if(type == char.class){
			value = Double.valueOf(value.toString());
		}
		/*String*/
		if(type == String.class){
			value = value.toString();
		}
		/*Short*/
		if(type == Short.class || type == short.class){
			value = Double.valueOf(value.toString());
		}
		/*Integer*/
		if(type == Integer.class || type == int.class){
			value = Integer.valueOf(value.toString());
		}
		/*Long*/
		if(type == Long.class || type == long.class){
			value = Long.valueOf(value.toString());
		}
		/*Float*/
		if(type == Float.class || type == float.class){
			value = Double.valueOf(value.toString());
		}
		/*Double*/
		if(type == Double.class || type == double.class){
			value = Double.valueOf(value.toString());
		}
		/*Boolean*/
		if(type == Boolean.class || type == boolean.class){
			value = Boolean.valueOf(value.toString());
		}
		/*Date*/
		if(type == Date.class){
			SimpleDateFormat simpDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			simpDate.setLenient(false);
			java.util.Date timeDate = simpDate.parse(value.toString());//util类型
			value = timeDate;  
		}
		if(type == BigDecimal.class){
			value = BigDecimal.valueOf(Long.valueOf(value.toString()));
		}
		
		return value;
	}
	
	
	/**
	 * 返回集合一部分
	 * @param allSet   集合
	 * @param beginIdx 开始标记位
	 * @param endCount 结束idx+1的标记位
	 * @return
	 */
	public static List getSubList(List allSet,int beginIdx,int endCount){
		List subSet = new ArrayList();
		for(int i=beginIdx ; i<endCount ; i++){
			subSet.add(allSet.get(i));
		}
		return subSet;
	}
	
	
	/**
	 * 获取输出对象
	 * @return
	 */
	public static XMLOutputter getXMLOutputter(){
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter XMLOUTPUT = new XMLOutputter(format);
		return XMLOUTPUT;
	}
	
	
	// 将手字母大写
	public static String toFirstLetterUpperCase(String strName) {
		if (strName == null || strName.length() < 2) {
			return strName;
		}
		String firstLetter = strName.substring(0, 1).toUpperCase();
		return firstLetter + strName.substring(1, strName.length());
	}
	
	/**
	 * 将map集合转换成Element对象
	 * webService接口使用
	 * @param mapSet  集合
	 * @param xmlObjName  对象名称
	 * @return
	 */
	public static List<Element> mapList2EleList(List<ExpHashMap> mapSet,String xmlObjName){
		List<Element> eleSet = new ArrayList<Element>();
		for(ExpHashMap expMap : mapSet){
			Element ele = new Element(xmlObjName);
			for(Iterator it = expMap.keySet().iterator(); it.hasNext();){
				String key = (String) it.next();
				ele.addContent((new Element(key)).addContent(expMap.getString(key)));
			}
			eleSet.add(ele);
		}
		
		return eleSet;
	}
	
	public static String getIpAddr(HttpServletRequest request) { 
	       String ip = request.getHeader("x-forwarded-for"); 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getRemoteAddr(); 
	       } 
	       return ip; 
	   } 
	
	
	/**
	 * 构建返回json字符串  
	 * @param resultCode   返回标识
	 * @param resultMsg    返回信息
	 * @returndataList     返回数据
	 */
	public static String buildResponseData(String resultCode,String msg,Object data){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", msg);
		jsonObject.put("dataList", data);
		return JSONObject.toJSONString(jsonObject);
	}
	
	/**
	 * 构建返回json字符串
	 * @param  resultCode 返回标识
	 * @param  resultMsg  返回信息
	 * @return dataList   返回数据
	 *         total      分页总数
	 */
	public static String buildResponseData(String resultCode,String msg,Object data,int total){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", msg);
		jsonObject.put("dataList", data);
		jsonObject.put("total", total);
		return JSONObject.toJSONString(jsonObject);
	}
	
	/**
	 * 构建返回json字符串
	 * @param  resultCode 返回标识
	 * @param  resultMsg  返回信息
	 * @return data       返回数据
	 *         total      分页总数
	 */
	public static String buildResponseDataWeb(String resultCode,String msg,Object data,Integer total){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", msg);
		jsonObject.put("data", data);
		jsonObject.put("total",total);//暂未用到
		return JSONObject.toJSONString(jsonObject);
	}
	

	/**
	 * 构建返回json字符串
	 * @returndataList
	 */
	public static String buildResponseOnlyData(Object data){
		return JSONObject.toJSONString(data);
	}
	
	/**
	 * 解析前台传送的update的数据
	 */
	public static Map String2Map(String jsonStr) {
		//构建转化后的需要更新的数据,解析需要更新的数据
		Map<String,Object> result=new HashMap<>();
		List<Map> array=JSONArray.parseArray(jsonStr,Map.class);
		if(Tools.isNull(array)) {
			return null; 
		}
		for(Map map :array) {
			result.put(map.get("path").toString().replace("/", ""), map.get("value"));
		}
		return result;
	}
	
	/**
	 * 用于获取多参数get请求，并将参数转化成map对象
	 * 可以获取头部参数和路径参数
	 * @return
	 */
	public static Map getParamsFromUrl() {
		HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
		Enumeration<String> params= request.getParameterNames();;
		Map<String,Object> jsonObj=new HashMap<>();
		while(params.hasMoreElements()) {
			String name=params.nextElement();
			jsonObj.put(name,request.getParameter(name));
		}
		Enumeration headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        jsonObj.put(key, value);
	    }
	    //封装排序字段对象
//	    if(jsonObj.containsKey("sort")) {
//	    	String [] strs=jsonObj.get("sort").toString().split(",");//按照逗号分割
//	    	List  sortList=new ArrayList<>();
//	        for(int i=0;i<strs.length;i++) {
//	        	if(strs[i].startsWith("-")) {
//	        		sortList.add(HumpToUnderline(strs[i]).substring(1)+" desc");
//	        	}else{
//	        		sortList.add(HumpToUnderline(strs[i]));
//	        	}
//	        }
//	        jsonObj.put("sort", sortList);
//	    }
	    //封装分页信息
//	    if(jsonObj.containsKey("offset")) {
//	    	jsonObj.put("pageIndex",Integer.parseInt(jsonObj.get("offset").toString()));
//	    }else{
//	    	jsonObj.put("pageIndex",PAGE_INDEX);
//	    }
//	    if(jsonObj.containsKey("limit")) {
//	    	jsonObj.put("pageSize",Integer.parseInt(jsonObj.get("limit").toString()));
//	    }else {
//	    	jsonObj.put("pageSize",PAGE_SIZE);
//	    }
		return jsonObj;
	}
	
	/**
	 * 用于集团API接口新增时封装公共的参数
	 */
	public static Map addCommonParams(Map map) {
		if(Tools.isNull(map.get("statusCd"))) {
			map.put("statusCd", "1000");//新增时默认状态为有效
		}
		if(Tools.isNull(map.get("createStaff"))) {
			map.put("createStaff", "123124");//测试使用
		}
		if(Tools.isNull(map.get("updateStaff"))) {
			map.put("updateStaff", "123124");//测试使用
		}
		if(Tools.isNull(map.get("remark"))) {
			map.put("remark", "");//新增默认备注为空
		}
		return map;
	}
	
	
	/***
	 * 下划线命名转为驼峰命名
	 * 
	 * @param para
	 *        下划线命名的字符串
	 */

	    public static String UnderlineToHump(String para){
	        StringBuilder result=new StringBuilder();
	        String a[]=para.split("_");
	        for(String s:a){
	            if(result.length()==0){
	                result.append(s.toLowerCase());
	            }else{
	                result.append(s.substring(0, 1).toUpperCase());
	                result.append(s.substring(1).toLowerCase());
	            }
	        }
	        return result.toString();
	    }



	/***
	* 驼峰命名转为下划线命名
	 * 
	 * @param para
	 *        驼峰命名的字符串
	 */

	    public static String HumpToUnderline(String para){
	            StringBuilder sb=new StringBuilder(para);
	            int temp=0;//定位
	            for(int i=0;i<para.length();i++){
	                if(Character.isUpperCase(para.charAt(i))){
	                    sb.insert(i+temp, "_");
	                    temp+=1;
	                }
	            }
	            return sb.toString().toUpperCase(); 
	    }
}
