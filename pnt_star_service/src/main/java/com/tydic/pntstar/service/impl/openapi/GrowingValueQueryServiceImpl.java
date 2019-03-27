package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.GrowingValueQueryService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * 成长值查询实现类
 * 
 * @author zhouman
 *
 */
@Service("growingValueQueryServiceImpl")
public class GrowingValueQueryServiceImpl  implements GrowingValueQueryService{
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Autowired
    private PointCommonComponent pointCommonComponent;
	/**
	 * 获取成长值信息
	 */
	@Override
	public String getGrowingValue(String param) throws Exception{
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("CUST_ID", json.get("CUST_ID"));
		
		//sqlParam.put("CUST_ID", "55144320669");
		sqlParam.put("POINT_ACC_TYPE", "120100");
		// 查询成长值信息
		List<Map<String, Object>> resultList = dao.query("QUERY_GROWING_VALUE", sqlParam);
		//Map<String, Object> result = dao.queryForOne("QUERY_GROWING_VALUE", sqlParam);
		// 变为string返回
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resultList);
	}

}
