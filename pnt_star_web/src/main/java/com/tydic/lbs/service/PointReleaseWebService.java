package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointReleaseService;
@Service("pointReleaseWebService")
public class PointReleaseWebService extends BaseService{

	
	private PointReleaseService pointReleaseServiceImpl = (PointReleaseService) SpringBeanUtil
			.getBean("pointReleaseServiceImpl");
	/**
	 * 该用户是否被冻结
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getCustStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(pointReleaseServiceImpl.getCustStatus(json.toJSONString()), rtnMap);
			;
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取客户状态失败"));
		}
	}
	/**
	 * 解冻操作
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void changeCustAcctStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		String freeze_reason = request.getParameter("FREEZE_REASON");
		if(null == freeze_reason){
			freeze_reason = "";
		}
		json.put("FREEZE_REASON", freeze_reason);
		
		json.put("CREATE_STAFF", getEmpeeId(request));
		try {
			pointReleaseServiceImpl.changeCustAcctStatus(json.toJSONString());
			rtnMap.put("resultCode", "00000");
			rtnMap.put("resultMsg", new String("解冻成功"));
			rtnMap.put("data", new JSONObject());
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("客户解冻失败"));
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
		rtnMap.put("data", json.get("data"));
	}
	/**
	 * 获取工号
	 */
	private String getEmpeeId(HttpServletRequest request) {
		Empee empee = (Empee) request.getSession().getAttribute("empee");
		String empeeId = "24316";
		if (empee != null)
			empeeId = empee.getEmpee_id().toString();
		return empeeId;
	}
}
