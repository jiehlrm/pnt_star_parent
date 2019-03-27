package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.StatusAdjustService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("statusAdjustServiceImpl")
public class StatusAdjustServiceImpl implements StatusAdjustService {
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private PointCommonComponent pointCommonComponent;

	/**
	 * 获取查兑状态表格
	 */
	@Override
	public String getStatusTable(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> param2 = new HashMap<String, Object>();
		param2.put("CUST_ID", json.get("CUST_ID"));
		param2.put("PAGEINDEX", json.get("PAGEINDEX"));
		param2.put("PAGESIZE", json.get("PAGESIZE"));
		// 查兑状态表格
		List<Map<String, Object>> result2 = dao.query("QUERY_POINT_STATUSADJUST_TABLE", param2);
		Map<String, Object> param3 = new HashMap<String, Object>();
		param3.put("CUST_ID", json.get("CUST_ID"));
		// 查兑状态表格数量
		List<Map<String, Object>> result3 = dao.query("QUERY_POINT_STATUSADJUST_TABLE_C", param3);
		String total = "0";
		if (!CommonUtil.isEmptyList(result3)) {
			if (result3.get(0).get("TOTAL").toString() != null)
				total = result3.get(0).get("TOTAL").toString();
		}
		JSONObject resObj = new JSONObject();
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(result2));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	/**
	 * 获取表格数量
	 */
	@Override
	public String getStatusTableTotal(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> param3 = new HashMap<String, Object>();
		param3.put("CUST_ID", json.get("CUST_ID"));
		// 查兑状态表格数量
		List<Map<String, Object>> result3 = dao.query("QUERY_POINT_STATUSADJUST_TABLE_C", param3);
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", result3);
	}

	/**
	 * 调整查兑状态
	 */
	@Override
	public void statusAdjust(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> paramMap = CommonUtil.json2Map(json);

		// 1.查询现在的查兑状态
		List<Map<String, Object>> res1 = dao.query("QUERY_POINT_STATUSADJUST_QUERYCURRENTSTATUS", paramMap);
		if (CommonUtil.isEmptyList(res1)) {
			return;
		}
		Object point_limit = res1.get(0).get("POINT_LIMIT");
		if(point_limit == null){
			return;
		}
		paramMap.put("POINT_LIMIT", point_limit);
		//2.插入新的状态
		dao.insert("INSERT_POINT_STATUSADJUST_ADDSTATUSRECORD", paramMap,true);
		//3.更改状态
		dao.update("UPDATE_POINT_STATUSADJUST_UPSTATUS", paramMap,true);
		//4.事务提交
		dao.commit();
	}
}
