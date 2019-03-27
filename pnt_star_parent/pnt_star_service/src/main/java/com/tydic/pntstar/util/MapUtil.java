package com.tydic.pntstar.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MapUtil {
	/**
	 * 按key从map中查找值
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getValueFromMap(Map<String, Object> map,String key) {
		if(map==null){
			return "";
		}
		if (map.containsKey(key)){
			 if(map.get(key)==null){
				 return "";
			 }
			 return map.get(key).toString();
		 }else {
			 for (String sKey : map.keySet()) {
				 if(map.get(sKey) instanceof Map){
					 String childRet=getValueFromMap((Map)map.get(sKey),key);
					 if(childRet!=""){
		        			return childRet;
		        	}
				 }else if(map.get(sKey) instanceof List){
					 List list =(List)map.get(sKey) ;
		        		for(Object object:list){
		        			if(object instanceof Map){
		        				String childRet= getValueFromMap((Map)object,key);
		                		if(childRet!=""){
		                			return childRet;
		                		}
		        			}  
		        		}
				 } 
			 }
		  
		  }
		 return "";
	}
	
	/**
	 * 按key从map中查找值,并且类型是map的
	 * @param map
	 * @param key
	 * @return
	 */
	public static Map getMapValueFromMap(Map<String, Object> map,String key) {
		if(map==null){
			return null;
		}
		if (map.containsKey(key)){
			 if(map.get(key)==null){
				 return null;
			 }
			 if (map.get(key) instanceof Map) {
				return (Map)map.get(key);
			}else {
				return null;
			}
		 }else {
			 for (String sKey : map.keySet()) {
				 if(map.get(sKey) instanceof Map){
					 Map childRet=getMapValueFromMap((Map)map.get(sKey),key);
					 if(childRet!=null){
		        			return childRet;
		        	 }
				 }else if(map.get(sKey) instanceof List){
					 List list =(List)map.get(sKey) ;
		        		for(Object object:list){
		        			if(object instanceof Map){
		        				Map childRet= getMapValueFromMap((Map)object,key);
		                		if(childRet!=null){
		                			return childRet;
		                		}
		        			}  
		        		}
				 } 
			 }
		  }
		 return null;
	}
	
	
	
	public static HashMap<String, Object> JsonToMap(JSONObject jsonObject){
		HashMap<String, Object> resultMap = new HashMap<>();
		
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }		
		return resultMap;
	}
}
