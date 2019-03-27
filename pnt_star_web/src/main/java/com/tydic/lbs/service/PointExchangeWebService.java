package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointExchangeService;

@Service("pointExchangeWebService")
public class PointExchangeWebService extends BaseService {
	private PointExchangeService pointExchangeServiceImpl = (PointExchangeService) SpringBeanUtil
			.getBean("pointExchangeServiceImpl");

	/**
	 * 获取礼品表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getGiftTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
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
		
		if(request.getParameter("POINT_EXCH_OBJ_NAME")!=null){
			json.put("POINT_EXCH_OBJ_NAME", request.getParameter("POINT_EXCH_OBJ_NAME"));
		}
		if(request.getParameter("MIN_POINT_AMT")!=null){
			json.put("MIN_POINT_AMT", request.getParameter("MIN_POINT_AMT"));
		}
		if(request.getParameter("MAX_POINT_AMT")!=null){
			json.put("MAX_POINT_AMT", request.getParameter("MAX_POINT_AMT"));
		}
		try {
			buildResponse(pointExchangeServiceImpl.getGiftTable(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("礼品表格信息获取失败"));
		}
	}
	
	/**
	 * 兑换
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void exchangeGiftByPoint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		json.put("createStaff", getEmpeeId(request));
		json.put("objType", request.getParameter("objType"));
		json.put("objValue", request.getParameter("objValue"));
		json.put("pointExchObjId", request.getParameter("pointExchObjId"));
		json.put("exchObjAmt", request.getParameter("exchObjAmt"));
		json.put("exchChnlId", "1");
		//本地网
		json.put("latnId", request.getParameter("latnId"));
		try {
			String resut = pointExchangeServiceImpl.exchangeGiftByPoint(json.toJSONString());
			
			JSONObject resJson = JSONObject.parseObject(resut);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
				rtnMap.put("resultMsg", new String("礼品兑换成功"));
			}else{
				rtnMap.put("resultCode", new String("10000"));
				rtnMap.put("resultMsg", resJson.get("resultMsg"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("礼品兑换失败"));
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
