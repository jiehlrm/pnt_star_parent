package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointQueryService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * 积分查询实现类
 * 
 * @author longinus
 *
 */
@Service("pointQueryServiceImpl")
public class PointQueryServiceImpl implements PointQueryService {

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private PointCommonComponent pointCommonComponent;

	@Autowired
	private DefaultBusinessService defaultBusinessService;
	/**
	 * 获取本地网信息
	 */
	@Override
	public String getLan() throws Exception {
		return pointCommonComponent.getLan();
	}

	/**
	 * 获取客户积分表单信息
	 */
	@Override
	public String getCustFormData(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Object latn_id = json.get("LATN_ID");
		Object acct_num = json.get("ACCT_NUM");
		// 参数判断
		if (null == acct_num) {
			return JSONArray.toJSONString(new JSONArray());
		}

		String type = json.get("TYPE").toString();
		// 初始参数赋值
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		if (latn_id != null) {
		    sqlParam.put("LATN_ID", latn_id);
		}
		sqlParam.put("ACC_NUM", acct_num);

		String firstSql = "1".equals(type) ? "QUERY_POINT_POINTQUERY_FORM_1" : "QUERY_POINT_POINTQUERY_FORM_0";
		// 循环的sql
		String[] sql = { firstSql, "QUERY_POINT_POINTQUERY_FORM_2", "QUERY_POINT_POINTQUERY_FORM_3",
				"QUERY_POINT_POINTQUERY_FORM_4", "QUERY_POINT_POINTQUERY_FORM_5", "QUERY_POINT_POINTQUERY_FORM_6",
				"QUERY_POINT_POINTQUERY_FORM_7", "QUERY_POINT_POINTQUERY_FORM_8" };
		// 获取的值
		List<Map<String, Object>> resultList = pointCommonComponent.selectMultiCommon(sql, sqlParam);
		// 变为string返回
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resultList);
	}

	/**
	 * 获取年积分信息
	 */
	@Override
	public String getYearPoint(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", json.get("CUST_ID"));
		// 查询年积分信息
		List<Map<String, Object>> resultList = dao.query("QUERY_POINT_YEARPOINT", sqlParam);
		JSONObject resObj = new JSONObject();
		resObj.put("total",1000);
		resObj.put("data", CommonUtil.list2JsonArray(resultList));
		// 变为string返回
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	/**
	 * 获取年积分明细
	 */
	@Override
	public String getYearDetail(String param) throws Exception {
		// 参数整理
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", json.get("CUST_ID"));
		sqlParam.put("YEAR", json.get("YEAR"));
		// 查询账本
		List<Map<String, Object>> resultList1 = dao.query("QUERY_POINT_YEARPOINT_DETAIL_1", sqlParam);
		// 如果没有账本则直接返回
		if (resultList1 == null || resultList1.size() == 0) {
			return JSONArray.toJSONString(new JSONArray());
		}
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("paramList", resultList1);
		param2.put("PAGEINDEX", json.get("PAGEINDEX"));
		param2.put("PAGESIZE", json.get("PAGESIZE"));
		// 查询具体的明细
		Map<String, Object> param3 = new HashMap<String, Object>();
		param3.put("paramList", resultList1);
		List<Map<String, Object>> resultList2 = dao.query("QUERY_POINT_YEARPOINT_DETAIL_2", param2);
		// 获取总数
		List<Map<String, Object>> resultList3 = dao.query("QUERY_POINT_YEARPOINT_DETAIL_3", param3);
		JSONObject resObj = new JSONObject();
        String total = "0";
        if (resultList3 != null) {
			if (resultList3.get(0).get("TOTAL") != null)
				total = resultList3.get(0).get("TOTAL").toString();
		}
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(resultList2));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	/**
	 * 获取年积分明细总数
	 */
	@Override
	public String getYearDetailTotal(String param) throws Exception {
		// 参数整理
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", json.get("CUST_ID"));
		sqlParam.put("YEAR", json.get("YEAR"));
		// 查询账本
		List<Map<String, Object>> resultList1 = dao.query("QUERY_POINT_YEARPOINT_DETAIL_1", sqlParam);
		// 如果没有账本则直接返回
		if (resultList1 == null || resultList1.size() == 0) {
			return JSONArray.toJSONString(new JSONArray());
		}
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("paramList", resultList1);
		// 获取总数
		List<Map<String, Object>> resultList3 = dao.query("QUERY_POINT_YEARPOINT_DETAIL_3", param2);
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resultList3);
	}
	/**
	 * 获取
	 */
	@Override
	public String getChangeList(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
            if(Tools.isNull(json)) {
            	throw new Exception("参数不能为空");
            }
			if(!Tools.isNull(json.get("timeRange"))){
				String timeRange=json.get("timeRange").toString();
				String beginTime = timeRange.substring(0, timeRange.indexOf(" - "));
				String endTime = timeRange.substring(timeRange.indexOf(" - ")+3, timeRange.length());
				json.put("beginTime", beginTime);
				json.put("endTime", endTime);
			}
			//2：查询信息
//			List<Map<String, Object>> accBalanceList = dao.query("QueryPointAcctBalanceIdByCustID", json);
			List<Map<String, Object>> accList = dao.query("QueryPointAcctListByCustId", json);
			json.put("accList",accList);
			List<Map<String, Object>> accBalanceList = dao.query("QueryPointAcctBalanceListByCond", json);

			json.put("accBalanceList", accBalanceList);
			List<Map<String, Object>> list = dao.query("QueryPointChangeList", json);

			if(!Tools.isNull(list)) {
				result.put("total", dao.queryForOne("QueryPointChangeListTotal", json).get("total"));
			}
			result.put("data", list);
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人列表查询成功", result,null);
		}catch(Exception e) {
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人列表查询失败",e.getMessage(),null);
		}
	}

}
