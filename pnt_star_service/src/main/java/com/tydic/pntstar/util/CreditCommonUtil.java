package com.tydic.pntstar.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class CreditCommonUtil {

	private CreditCommonUtil(){
	}
	/**
	 * 判断列表是否为空（包括null以及数据量为0）
	 * @param list
	 * @return
	 */
	public static boolean isEmptyList(List<?> list){
		if(list == null || list.size() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 返回查询失败
	 * resultCode 10000表示查询失败
	 * @param msg 提示信息
	 * @return
	 */
	public static String reQueryFail(String msg){
		return buildResponse("1",msg);
	}
	/**
	 * 返回删除成功
	 * @param msg
	 * @return
	 */
	public static String reDelSuccess(String msg){
		return buildResponse("0",msg);
	}
	
	
	
	/**
	 * map转json
	 * @param map
	 * @return
	 */
	public static JSONObject map2Json(Map<String,Object> map){
		JSONObject json = new JSONObject();
		if(map != null){
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for(Entry<String, Object> entry :  entrySet){
				json.put(entry.getKey(), entry.getValue());
			}
		}
		return json;
	}
	
	/**
	 * json 转 map
	 * @param json
	 * @return
	 */
	public static Map<String,Object> json2Map(JSONObject json){
		if(json == null){
			return new HashMap<String,Object>(1);
		}
		Map<String,Object> map = new HashMap<String,Object>((int)(json.size()/0.75+1));
		Set<Entry<String, Object>> entrySet = json.entrySet();
		for(Entry<String, Object> entry :  entrySet){
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	/**
	 * list 转 jsonArray
	 * @param list
	 * @return
	 */
	public static JSONArray list2JsonArray(List<Map<String, Object>> list){
		JSONArray jsonArray = new JSONArray();
		if(list == null){
			return jsonArray;
		}
		for(int i = 0 ; i < list.size() ; i++){
			jsonArray.add(map2Json(list.get(i)));
		}
		return jsonArray;
	}
	
	/**
	 * 构建返回json字符串
	 * @param resultCode
	 * @param msg
	 * @return
	 */
	public static String buildResponse(String resultCode,String msg){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", msg);
		return JSONObject.toJSONString(jsonObject);
	}
	
	/**
	 * 构建返回json字符串
	 * @param resultCode
	 * @param msg
	 * @return
	 */
	public static String buildResponseData(String resultCode,String msg,Object data){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", msg);
		jsonObject.put("dataList", data);
		return JSONObject.toJSONString(jsonObject);
	}
	
	
}
