package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.ClubMemberServiceService;
import com.tydic.pntstar.service.province.MemberServAcctService;
@Service("equityConsumeManageWebService")
public class EquityConsumeManageWebService extends BaseService {
	private ClubMemberServiceService clubMemberServiceServiceImpl = (ClubMemberServiceService) SpringBeanUtil
			.getBean("clubMemberServiceServiceImpl");
	@Autowired
	private MemberServAcctService memberServAcctService;
	/**
	 * 获取权益消费表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getEquityTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
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
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(clubMemberServiceServiceImpl.queryClubMemberServiceListbyCustId(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("权益消费表格信息获取失败"));
		}
	}
	/**
	 * 获取权益消费明细表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getEquityDetail(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
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
		String custId = request.getParameter("custId");
		json.put("memberServiceId", request.getParameter("memberServiceId"));
		if (null == custId) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", custId);
		try {
			buildResponse(clubMemberServiceServiceImpl.queryClubMemberServiceRecordList(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("权益消费明细表格信息获取失败"));
		}
	}
	/**
	 * 获取权益消费记录表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getEquityRecordTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
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
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(clubMemberServiceServiceImpl.queryClubMemberServiceRecordList(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("权益消费记录表格信息获取失败"));
		}
	}
	/**
	 * 消费
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void equityConsume(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		json.put("createStaff", getEmpeeId(request));
		json.put("objType", request.getParameter("objType"));
		json.put("objValue", request.getParameter("objValue"));
		json.put("memberServiceId", request.getParameter("memberServiceId"));
		String memServAcctId = request.getParameter("memServAcctId");
		String consumeTimes = request.getParameter("consumeTimes");
		json.put("latnId", request.getParameter("latnId"));
		json.put("memServAcctId", memServAcctId);
		json.put("consumeTimes", consumeTimes);
		try {
			String result = memberServAcctService.serviceMinus(json.toJSONString());
			JSONObject resJson = JSONObject.parseObject(result);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
				rtnMap.put("resultMsg", new String("消费成功"));
			}else{
				rtnMap.put("resultCode", new String("10000"));
				rtnMap.put("resultMsg", resJson.get("resultMsg"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("消费失败"));
		}
	}
	/**
	 * 返销
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void serviceSellBack(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap){
		JSONObject json = new JSONObject();
		json.put("createStaff", getEmpeeId(request));
		json.put("objType", request.getParameter("objType"));
		json.put("objValue", request.getParameter("objValue"));
		json.put("orderItemId", request.getParameter("orderItemId"));
		json.put("latnId", request.getParameter("latnId"));
		try {
			String result = memberServAcctService.serviceBack(json.toJSONString());
			JSONObject resJson = JSONObject.parseObject(result);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
				rtnMap.put("resultMsg", new String("返销成功"));
			}else{
				rtnMap.put("resultCode", new String("10000"));
				rtnMap.put("resultMsg", resJson.get("resultMsg"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("返销失败"));
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
