package com.tydic.lbs.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tydic.lbs.service.RestFulClient;
import com.tydic.lbs.util.ServerConfig;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.lbs.util.StringCommon;
import com.tydic.pntstar.service.system.LoadSysDataService;


public class Constants {
	/***
	 * openApi响应码
	 */
	public static String resultCodeSuccess="0";
	
	public static String resultCodeError="-1";
	
	/************
	 * 调用端标识
	 *************/
	public static String spanId="pnt_star_web";
	/****
	 * 前台页面表头信息，key:service_name,value:列信息集合
	 */
	private static Map<String, List<Map<String, Object>>> tableHead=new HashMap<String, List<Map<String, Object>>>();
	
	/***
	 * 流程对应的URL,key:流程编码，value:URL,METHOD
	 *  URL: http://localhost:20881/pntstar/rtBillItemService
	 *  METHDO:POST
	 */
	private static Map <String,Map<String,String>> urlMap=new HashMap<String,Map<String, String>>();
	
 
	/***
	 * 根据service_code=流程编码，service_name=HEAD_QUERY_FIELDS获取表头
	 * @param params
	 * @return
	 */
	public static  List<Map<String, Object>> getTableHead(Map params) {
		if(tableHead.containsKey(params.get("service_code"))){
			return tableHead.get(params.get("service_code"));
		}else {
			ServerConfig config=null;
			try {
				config = new ServerConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//去后台获取
			LoadSysDataService loadsys=(LoadSysDataService)SpringBeanUtil.getBean("loadSysDataService");
			String jsonResultStr=loadsys.loadTableHead(JSON.toJSONString(params));
			//结果非空
			if (!StringCommon.isNull(jsonResultStr)) {
				List<Map<String, Object>> listMap = JSON.parseObject(jsonResultStr, new TypeReference<List<Map<String,Object>>>(){});
				if (listMap!=null&&listMap.size()>0) {
					tableHead.put(params.get("service_code").toString(), listMap);
				}
			}
		}
		return tableHead.get(params.get("service_code"));
	}
	
	/***
	 * 根据流程编码获取流程对应的openApi中的url和method
	 * @param flowCode
	 * @return
	 */
	public static Map<String,String> getUrlMap(String flowCode) {
		if (urlMap.containsKey(flowCode)) {
			return urlMap.get(flowCode);
		}else {
			//通过restful获取
			ServerConfig config=null;
			try {
				config = new ServerConfig();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//去后台获取
			JSONObject itemJSONObj = new JSONObject();
			itemJSONObj.put("SERVICE_NAME", flowCode);
			//调用restful获取
			LoadSysDataService loadsys=(LoadSysDataService)SpringBeanUtil.getBean("loadSysDataService");
			String jsonResultStr=loadsys.loadFlowInfo(itemJSONObj.toJSONString());
			//结果非空
			if (!StringCommon.isNull(jsonResultStr)) {
				Map<String,String> map = JSON.parseObject(jsonResultStr, new TypeReference<Map<String,String>>(){});
				if (map!=null) {
					urlMap.put(flowCode, map);
				}
			}
		}
		return urlMap.get(flowCode);
	}
	public static void clearTableHead() {
		tableHead=new HashMap<String, List<Map<String, Object>>>();
	}
	
	public static void clearUrlMap() {
		urlMap=new HashMap<String,Map<String, String>>();
	}
	
}
