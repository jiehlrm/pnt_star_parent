package com.tydic.pntstar.service.impl.openapi;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointSellBackService;
import com.tydic.pntstar.service.province.ProvinceService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("pointSellBackServiceImpl")
public class PointSellBackServiceImpl implements PointSellBackService {
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Autowired
	private PointCommonComponent pointCommonComponent;
	
	@Autowired
	private ProvinceService provinceServiceImpl;

	/**
	 * 积分返销表格
	 */
	@Override
	public String getPointSellBackTable(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> paramMap = CommonUtil.json2Map(json);
		String[] sqlArr = { "QUERY_POINT_POINTBUYBACK_TABLE_1", "QUERY_POINT_POINTBUYBACK_TABLE_2" };
		List<Map<String, Object>> result = pointCommonComponent.selectMultiCommon(sqlArr, paramMap);
		JSONObject reObj = new JSONObject();
		reObj.put("total", getPointSellBackTableTotal(param));
		reObj.put("data", CommonUtil.list2JsonArray(result));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", reObj);
	}

	/**
	 * 积分返销表格数量
	 */
	@Override
	public String getPointSellBackTableTotal(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> paramMap = CommonUtil.json2Map(json);
		List<Map<String, Object>> rsult = dao.query("QUERY_POINT_POINTBUYBACK_TABLE_C", paramMap);
		if (CommonUtil.isEmptyList(rsult)) {
			return new String("0");
		}
		String total = rsult.get(0).get("TOTAL").toString();
		return total == null ? new String("0") : total;
	}

	/**
	 * 积分返销操作
	 */
	@Override
	public String sellBack(String param) throws Exception {
	    JSONObject json = JSONObject.parseObject(param);
        Object latnId = json.get("latnId");
        // 参数判断
        if (null == latnId) {
            return JSONArray.toJSONString(new JSONArray());
        }
		return provinceServiceImpl.rebackPoint(param);
	}

}
