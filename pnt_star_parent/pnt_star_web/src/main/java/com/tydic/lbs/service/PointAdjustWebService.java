package com.tydic.lbs.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointAdjustService;

@Service("pointAdjustWebService")
public class PointAdjustWebService extends BaseService{
	private static final Logger logger = LoggerFactory.getLogger(PointAdjustWebService.class);
	private PointAdjustService pointAdjustServiceImpl=(PointAdjustService) SpringBeanUtil.
			getBean("pointAdjustServiceImpl");
	
	/**
	 * 获取积分调整前余额信息
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getPointBalance(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(pointAdjustServiceImpl.getPointBalance(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取积分余额信息失败"));
		}

	}
	/**
	 * 进行积分调整
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void doPointAdjust(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		String select = request.getParameter("SELECT");
		String reason = request.getParameter("REASON_REMARK");
		JSONArray json_array = new JSONArray();
        json_array = JSONArray.parseArray(request.getParameter("DATA_LIST"));
		
        logger.debug(json_array.toJSONString());
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		if(null == select){
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入调整原因"));
			return;
		}
		if(null == reason){
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入调整原因描述"));
			return;
		}
		json.put("CUST_ID", cust_id);
		json.put("SELECT", select);
		json.put("REASON_REMARK", reason);
		json.put("DATA_LIST", json_array);
		logger.debug(json.toJSONString());

		try {
			Map<String, Object> rtn = pointAdjustServiceImpl.doPointAdjust(json.toJSONString());
			
			rtnMap.put("resultCode", rtn.get("resultCode"));
			rtnMap.put("resultMsg", rtn.get("resultMsg"));

		} catch (Exception e) {
			e.printStackTrace();
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
