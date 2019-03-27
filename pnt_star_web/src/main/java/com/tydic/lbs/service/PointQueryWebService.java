package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointQueryService;

@Service("pointQueryService")
public class PointQueryWebService extends BaseService {

	private PointQueryService pointQueryServiceImpl = (PointQueryService) SpringBeanUtil
			.getBean("pointQueryServiceImpl");

	/**
	 * 获取本地网信息
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getLan(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		try {
			String result = pointQueryServiceImpl.getLan();
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("查询本地网信息失败"));
		}
	}

	/**
	 * 获取客户表单信息
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getCustFormData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String latn_id = request.getParameter("LATN_ID");
		if (null == latn_id) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入本地网"));
            return;
        }
//		if (!"888".equals(latn_id)) {
//		    json.put("LATN_ID", latn_id);
//		}
		String acct_num = request.getParameter("ACCT_NUM");
		if (null == acct_num) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入业务号码"));
			return;
		}
		String type = request.getParameter("TYPE");
		if(null == type || (!"1".equals(type) && !"2".equals(type))){
			type = "1";
		}
		json.put("LATN_ID", latn_id);
		json.put("ACCT_NUM", acct_num);
		json.put("TYPE", type);

		try {
			buildResponse(pointQueryServiceImpl.getCustFormData(json.toJSONString()), rtnMap);
			;
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取客户表单信息失败"));
		}
	}

	/**
	 * 获取年累计积分信息
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getYearPoint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String cust_id = request.getParameter("CUST_ID");
		if (null == cust_id) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识"));
			return;
		}
		json.put("CUST_ID", cust_id);
		try {
			buildResponse(pointQueryServiceImpl.getYearPoint(json.toJSONString()), rtnMap);
			;
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取年累计积分信息失败"));
		}

	}

	/**
	 * 获取年积分明细
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getYearDetail(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		// 参数必要的判断以及整理
		String cust_id = request.getParameter("CUST_ID");
		String year = request.getParameter("YEAR");
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == cust_id || null == year) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识以及年份"));
			return;
		}
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		JSONObject json = new JSONObject();
		json.put("CUST_ID", cust_id);
		json.put("YEAR", year);
		json.put("PAGEINDEX", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("PAGESIZE", Integer.parseInt(pageSize));
		// 获取年积分细节;;
		try {
			buildResponse(pointQueryServiceImpl.getYearDetail(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取年积分明细信息失败"));
		}

	}

	/**
	 * 获取年积分明细总数
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	/*public void getYearDetailTotal(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> rtnMap) {
		// 参数必要的判断以及整理
		String cust_id = request.getParameter("CUST_ID");
		String year = request.getParameter("YEAR");
		if (null == cust_id || null == year) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("请传入客户标识以及年份"));
			return;
		}
		JSONObject json = new JSONObject();
		json.put("CUST_ID", cust_id);
		json.put("YEAR", year);
		// 获取年积分明细总数;;
		try {
			buildResponse(pointQueryServiceImpl.getYearDetailTotal(json.toJSONString()), rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取年积分明细总数信息失败"));
		}
	}*/

	/**
	 * 获取积分账本变更记录
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getChangeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("custId", request.getParameter("cust_id"));
		json.put("serviceTypeIn", request.getParameter("serviceTypeIn"));
		json.put("serviceTypeOut", request.getParameter("serviceTypeOut"));
		json.put("timeRange", request.getParameter("timeRange"));
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		
		json.put("pageIndex", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("pageSize", Integer.parseInt(pageSize));
		try {
			String result = pointQueryServiceImpl.getChangeList(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("查询关键人信息失败"));
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

}
