package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.StatusAdjustService;

@Service("statusAdjustWebService")
public class StatusAdjustWebService extends BaseService {
	private StatusAdjustService statusAdjustServiceImpl = (StatusAdjustService) SpringBeanUtil
			.getBean("statusAdjustServiceImpl");

	/**
	 * 获取查兑状态表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getStatusTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
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
			buildResponse(statusAdjustServiceImpl.getStatusTable(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取查兑状态表格信息失败"));
		}
	}

	/**
	 * 获取查兑表格数量
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	/*public void getStatusTableTotal(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> rtnMap) {
		// 参数必要的判断以及整理
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		JSONObject json = new JSONObject();
		json.put("CUST_ID", cust_id);
		// 获取获取查兑表格数量
		try {
			buildResponse(statusAdjustServiceImpl.getStatusTableTotal(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取查兑表格数量失败"));
		}
	}*/

	/**
	 * 查兑状态调整
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void statusAdjust(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		// 参数获取
		String operator = getEmpeeId(request);
		String cust_id = request.getParameter("CUST_ID");
		String point_limit = request.getParameter("POINT_LIMIT");
		String reason_mark = request.getParameter("REASON_REMARK");
		// 必要校验
		if (cust_id == null || "".equals(cust_id) || point_limit == null || "".equals(point_limit)) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入相应参数"));
			return;
		}
		// 后台参数拼装
		JSONObject json = new JSONObject();
		json.put("CUST_ID", cust_id);
		json.put("OPERATOR", operator);
		json.put("AFTER_STATUS_CD", point_limit);
		json.put("REASON_REMARK", reason_mark);
		json.put("SERVICE_CHANNEL", 1);
		json.put("BATCHNO", 1);
		try {
			statusAdjustServiceImpl.statusAdjust(json.toJSONString());
			rtnMap.put("resultCode", new String("00000"));
			rtnMap.put("resultMsg", new String("查兑状态调整成功"));
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("查兑状态调整失败"));
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
