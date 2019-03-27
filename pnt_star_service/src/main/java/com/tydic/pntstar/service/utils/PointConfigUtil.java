package com.tydic.pntstar.service.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @Title: PointConfigUtil.java 
* @Package com.tydic.pntstar.service.utils 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2018年12月17日 下午5:46:31 
* @version V1.0 
*/
public final class PointConfigUtil {
	
	public static final ThreadLocal<String> threadLatnMap = new ThreadLocal<String>();
	public static final ThreadLocal<String> threadMap = new ThreadLocal<String>();
	public static final ThreadLocal<String> codeTypeMap = new ThreadLocal<String>();
	public static final ThreadLocal<String> codeValueMap = new ThreadLocal<String>();
	public static final ThreadLocal<String> redeemFlag = new ThreadLocal<String>();
	public static final ThreadLocal<String> sysCode = new ThreadLocal<String>();
	public static final ThreadLocal<String> hobbyDeleteFlag = new ThreadLocal<String>();
	private static final ConcurrentHashMap<String, Object> msgProcessHashMap = new ConcurrentHashMap<String, Object>();
	public static final ThreadLocal<Map<String,String>> tcpAppend = new ThreadLocal<Map<String,String>>();
	private static Map<String, String> latnMap;
	
	
	public static final void putMsg(String key,Object value){
		msgProcessHashMap.put(key, value);
	}
	
	public static final Object getMsg(String key){
		return msgProcessHashMap.get(key);
	}
	
	public static final void removeMsg(String key){
		msgProcessHashMap.remove(key);
	}
//	
//	public static final void removeAllMsg(){
//		msgProcessHashMap.clear();
//	}
//
//	/**
//	 * 根据本地网查询地市名称
//	 * 
//	 * @param latnId
//	 * @return String
//	 */
//	public static String getLatnNameByLatnId(String latnId) {
//		initMap();
//		return null == latnMap ? null : latnMap.get(latnId); 
//	}
//	
//	public static final void setTcpAppend(Map<String,String> tcpAppendMap){
//		tcpAppend.set(tcpAppendMap);
//	}
//	
//	public static final Map<String,String> getTcpAppend(){
//		return tcpAppend.get();
//	}
//	
//	public static final void removeTcpAppend(){
//		tcpAppend.remove();
//	}
//	/**
//	 * 判断是否为本省本地网
//	 * 
//	 * @param latnId
//	 * @return boolean
//	 */
//	public static boolean emptyLatn(String latnId) {
//		initMap();
//		return null == latnMap ? false : latnMap.containsKey(latnId);
//	}
//
//	private synchronized static void initMap() {
//		if (null != latnMap) {
//			return;
//		}
//		latnMap = CacheKit.get(CacheConstants.PNT_LATN_CACHE_NAME, CacheConstants.PNT_LATN_CACHE_NAME);
//	}
	
//	public static String getWorkFlowUrl(){
////		return PntCodeTypeManager.getInstance().getTypeValue("WORKFLOW_URL", "WORKFLOW_URL");
//		return null;
//	}
	
//	public static boolean is170Number(String inMsgNbr) {
//
//		Pattern p = Pattern.compile("^(17[0])\\d{8}$");
//		Matcher m = p.matcher(inMsgNbr);
//		// System.out.println(m.matches()+"---");
//		return m.matches();
//	}
}
