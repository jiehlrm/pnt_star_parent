package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointReleaseService;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("pointReleaseServiceImpl")
public class PointReleaseServiceImpl implements PointReleaseService {

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private PointCommonComponent pointCommonComponent;

	/**
	 * 获取用户账户状态
	 */
	@Override
	public String getCustStatus(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);

		Object cust_id = json.get("CUST_ID");
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", cust_id);
		String[] sqlArr = { "QUERY_POINT_POINTRELEASE_QUERYISFREEZED", "QUERY_POINT_POINTRELEASE_FREEZEPOINT" };
		List<Map<String, Object>> resultList = pointCommonComponent.selectMultiCommon(sqlArr, sqlParam);
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resultList);
	}

	/**
	 * 积分解冻/冻结操作
	 */
	@Override
	public String changeCustAcctStatus(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", json.get("CUST_ID"));
		sqlParam.put("FREEZE_REASON", json.get("FREEZE_REASON"));
		sqlParam.put("CREATE_STAFF", json.get("CREATE_STAFF"));
		try{
			dao.update("UPDATE_POINT_POINTRELEASE_RELEASE_1", sqlParam, true);
			dao.update("UPDATE_POINT_POINTRELEASE_RELEASE_2", sqlParam, true);
			//暂时不管账本
			//dao.update("UPDATE_POINT_POINTRELEASE_RELEASE_3", sqlParam, true);
			String pk = dao.getPK("POINT_FROZEN_RECORD");
			sqlParam.put("POINT_FROZEN_RECORD_ID", pk);
			dao.insert("INSERT_POINT_POINTRELEASE_RELEASE_4", sqlParam, true);
			dao.commit();
		}catch(Exception e){
			dao.rollback();
			throw e;
		}finally{
			dao.release();
		}
		return new String();
	}
}
