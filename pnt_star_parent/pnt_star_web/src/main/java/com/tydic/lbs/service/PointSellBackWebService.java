package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointSellBackService;

@Service("pointSellBackWebService")
public class PointSellBackWebService extends BaseService {
	
	private PointSellBackService pointSellBackServiceImpl = (PointSellBackService) SpringBeanUtil
			.getBean("pointSellBackServiceImpl");
	/**
	 * 获取积分返销表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getPointSellBackTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		json.put("PAGEINDEX", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("PAGESIZE", Integer.parseInt(pageSize));
		try {
			buildResponse(pointSellBackServiceImpl.getPointSellBackTable(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取积分返销表格信息失败"));
		}
	}
	/**
	 * 返销操作
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void sellback(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		json.put("objValue", request.getParameter("ACCT_NUM"));
		json.put("objType", request.getParameter("objType"));
		json.put("extSerialId", request.getParameter("EXT_SERIAL_ID"));
		json.put("updateStaff", getEmpeeId(request));
		json.put("latnId", request.getParameter("latnId"));
		try {
			String resut = pointSellBackServiceImpl.sellBack(json.toJSONString());
			JSONObject resJson = JSONObject.parseObject(resut);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
				rtnMap.put("resultMsg", new String("返销操作成功"));
			}else{
				rtnMap.put("resultCode", new String("20000"));
				rtnMap.put("resultMsg", new String("返销操作失败"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("返销操作失败"));
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
