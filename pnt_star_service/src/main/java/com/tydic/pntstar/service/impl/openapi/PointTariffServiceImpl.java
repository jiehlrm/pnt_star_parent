package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointTariffService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;


@Service("pointTariffServiceImpl")
public class PointTariffServiceImpl implements PointTariffService {

	private static final Logger logger = LoggerFactory.getLogger(PointTariffServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Override
	public String addpointTariff(String json) throws Exception {
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分计算为："+jsonObj.toString());
			String pointTariffId=dao.getPK("POINT_TARIFF");//获取积分规则主键
			jsonObj.put("pointTariffId",pointTariffId);
			//1:插入积分计算
			dao.insert("InsertPointTariff",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分计算规则成功",pointTariffId); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	@Override
	public String delpointTariff(String json, String pointTariffId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String querypointTariff(String json, String pointTariffId) throws Exception {
		//解析参数条件
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointTariffId", pointTariffId);
			logger.info("查询积分计算规则查询条件为："+pointTariffId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointTariff", param);
			if (Tools.isNull(result)) {
				return null;
			}
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分计算规则详情成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	@Override
	public String querypointTariffList(String json) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
