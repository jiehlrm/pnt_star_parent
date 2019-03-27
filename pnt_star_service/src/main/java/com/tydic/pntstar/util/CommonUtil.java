package com.tydic.pntstar.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class CommonUtil {
     private CommonUtil(){
    	 
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
 	
 	/** 
     * 判断参数的格式是否为“yyyyMMdd”格式的合法日期字符串 
     *  
     */  
    public static boolean isValidDate(String str) {  
        try {  
            if (str != null && !str.equals("")) {  
                if (str.length() == 8) {  
                    // 闰年标志  
                    boolean isLeapYear = false;  
                    String year = str.substring(0, 4);  
                    String month = str.substring(4, 6);  
                    String day = str.substring(6, 8);  
                    int vYear = Integer.parseInt(year);  
                    // 判断年份是否合法  
                    if (vYear < 1900 || vYear > 2200) {  
                        return false;  
                    }  
                    // 判断是否为闰年  
                    if (vYear % 4 == 0 && vYear % 100 != 0 || vYear % 400 == 0) {  
                        isLeapYear = true;  
                    }  
                    // 判断月份  
                    // 1.判断月份  
                    if (month.startsWith("0")) {  
                        String units4Month = month.substring(1, 2);  
                        int vUnits4Month = Integer.parseInt(units4Month);  
                        if (vUnits4Month == 0) {  
                            return false;  
                        }  
                        if (vUnits4Month == 2) {  
                            // 获取2月的天数  
                            int vDays4February = Integer.parseInt(day);  
                            if (isLeapYear) {  
                                if (vDays4February > 29)  
                                    return false;  
                            } else {  
                                if (vDays4February > 28)  
                                    return false;  
                            }  
                        }  
                    } else {  
                        // 2.判断非0打头的月份是否合法  
                        int vMonth = Integer.parseInt(month);  
                        if (vMonth != 10 && vMonth != 11 && vMonth != 12) {  
                            return false;  
                        }  
                    }  
                    // 判断日期  
                    // 1.判断日期  
                    if (day.startsWith("0")) {  
                        String units4Day = day.substring(1, 2);  
                        int vUnits4Day = Integer.parseInt(units4Day);  
                        if (vUnits4Day == 0) {  
                            return false;  
                        }  
                    } else {  
                        // 2.判断非0打头的日期是否合法  
                        int vDay = Integer.parseInt(day);  
                        if (vDay < 10 || vDay > 31) {  
                            return false;  
                        }  
                    }  
                    return true;  
                } else {  
                    return false;  
                }  
            } else {  
                return false;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
}
