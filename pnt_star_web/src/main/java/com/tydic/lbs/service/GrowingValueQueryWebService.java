package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.GrowingValueQueryService;

@Service("growingValueQueryWebService")
public class GrowingValueQueryWebService extends BaseService{
	
	private GrowingValueQueryService growingValueQueryServiceImpl =(GrowingValueQueryService) SpringBeanUtil.
			getBean("growingValueQueryServiceImpl");
	
	/**
	 * 获取年累计积分信息
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getGrowingValue(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(growingValueQueryServiceImpl.getGrowingValue(json.toJSONString()), rtnMap);
			;
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取年累计积分信息失败"));
		}

	}
	/**
	 * 构建成功的返回
	 * 
	 * @param result
	 * @param rtnMap
	 */
	private void buildResponse(String result, Map<String, Object> rtnMap) {
		JSONObject json = JSONObject.parseObject(result);
		rtnMap.put("resultCode", json.get("resultCode"));
		rtnMap.put("resultMsg", json.get("resultMsg"));
		JSONObject data= new JSONObject();
        data.put("data",  json.get("data"));
		data.put("total",1000);
        rtnMap.put("data", data);
	}
}
