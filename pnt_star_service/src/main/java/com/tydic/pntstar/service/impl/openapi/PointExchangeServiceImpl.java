package com.tydic.pntstar.service.impl.openapi;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointExchangeService;
import com.tydic.pntstar.service.province.ProvinceService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("pointExchangeServiceImpl")
public class PointExchangeServiceImpl implements PointExchangeService {
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private PointCommonComponent pointCommonComponent;
	@Autowired
	private ProvinceService provinceServiceImpl;
	/**
	 * 获取礼品表格操作
	 */
	@Override
	public String getGiftTable(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> initParam = CommonUtil.json2Map(json);
		String[] sqlArr = { "QUERY_POINT_POINTEXCHANGE_OBJTABLE_1", "QUERY_POINT_POINTEXCHANGE_OBJTABLE_2" };
		List<Map<String, Object>> resultList = pointCommonComponent.selectMultiCommon(sqlArr, initParam);
		String total = "0";
		List<Map<String, Object>> rsultTotal = dao.query("QUERY_POINT_POINTEXCHANGE_OBJTABLE_C", initParam);
		if (!CommonUtil.isEmptyList(rsultTotal)) {
			if (rsultTotal.get(0).get("TOTAL").toString() != null)
				total = rsultTotal.get(0).get("TOTAL").toString();
		}
		JSONObject resObj = new JSONObject();
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(resultList));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	/**
	 * 积分兑换礼品
	 */
	@Override
	public String exchangeGiftByPoint(String param) throws Exception {
	    JSONObject json = JSONObject.parseObject(param);
        Object latnId = json.get("latnId");
        // 参数判断
        if (null == latnId) {
            return JSONArray.toJSONString(new JSONArray());
        }
		return provinceServiceImpl.exchangePoint(param);
	}

}
