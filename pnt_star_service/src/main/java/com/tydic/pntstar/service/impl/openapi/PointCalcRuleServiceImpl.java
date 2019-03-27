package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointCalcRuleService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("pointCalcRuleServiceImpl")
@com.alibaba.dubbo.config.annotation.Service
public class PointCalcRuleServiceImpl implements PointCalcRuleService {

	private static final Logger logger = LoggerFactory.getLogger(PointCalcRuleServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	//添加计算规则
	@Override
	public String addpointCalcRule(String json) throws Exception {
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分规则条件为："+jsonObj.toString());
			String pointCalcRuleId=dao.getPK("POINT_CALC_RULE");//获取积分规则主键
			jsonObj.put("pointCalcRuleId",pointCalcRuleId);
			//1:插入积分计算规则表
			dao.insert("InsertPointCalcRule",jsonObj);
//	//		2:插入积分计算条件表  POINT_CONDITION
//			if(!Tools.isNull(jsonObj.get("pointCalcFactorId"))){
//				String pointConditionId=dao.getPK("POINT_CONDITION");//积分计算条件主键
//				jsonObj.put("pointConditionId", pointConditionId);
//				dao.insert("InsertPointCondition", jsonObj);				
//			}
		    //3:插入积分计算规则联系表 POINT_CALC_RULE_REL
			if(!Tools.isNull(jsonObj.get("zPointCalcRuleId"))){
				jsonObj.put("aPointCalcRuleId", pointCalcRuleId);
				jsonObj.put("pointCalcRuleRelId", dao.getPK("POINT_CALC_RULE_REL"));
					dao.insert("InsertPointCalcRuleRel", jsonObj);
			}
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分计算规则成功",pointCalcRuleId ); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}


	//查询积分规则详情
	@Override
	public String querypointCalcRule(String json, String pointCalcRuleId) throws Exception {
		//解析参数条件
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointCalcRuleId", pointCalcRuleId);
			logger.info("查询积分计算规则查询条件为："+pointCalcRuleId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointCalcRule", param);
			if (Tools.isNull(result)) {
				return null;
			}
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分计算规则详情成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	@Override
	public String addpointTariff(String json) throws Exception {
		// TODO Auto-generated method stub
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分计算条件为："+jsonObj.toString());
			String pointTariffId=dao.getPK("POINT_TARIFF");//获取积分规则主键
			jsonObj.put("pointTariffId",pointTariffId);
			//1:插入积分计算规则表
			dao.insert("InsertPointTariff",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分计算对象成功",pointTariffId); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	@Override
	public String querypointTariff(String json, String pointTariffId) throws Exception {
		// TODO Auto-generated method stub
		//解析参数条件
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointTariffId", pointTariffId);
			logger.info("查询积分计算查询条件为："+pointTariffId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointTariff", param);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分计算详情成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	@Override
	public String addpointEventType(String json) throws Exception {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分事件类型条件为："+jsonObj.toString());
			String pointEventTypeId=dao.getPK("POINT_EVENT_TYPE");//获取积分规则主键
			jsonObj.put("pointEventTypeId",pointEventTypeId);
			//1:插入积分计算规则表
			dao.insert("InsertPointEventType",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分计算对象成功",pointEventTypeId); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	@Override
	public String querypointEventType(String json, String pointEventTypeId) throws Exception {
		// TODO Auto-generated method stub
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointEventTypeId", pointEventTypeId);
			logger.info("查询积分事件查询条件为："+pointEventTypeId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointEventType", param);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分事件情成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	@Override
	public String addpointCalcRuleRel(String json) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String querypointCalcRuleRel(String json, String pointCalcRuleRelId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addpointCondition(String json) throws Exception {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分条件为："+jsonObj.toString());
			String pointConditionId=dao.getPK("POINT_CONDITION");//获取积分规则主键
			jsonObj.put("pointConditionId",pointConditionId);
			//1:插入积分计算规则表
			dao.insert("InsertPointCondition",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分条件成功",pointConditionId); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	@Override
	public String querypointCondition(String json, String pointConditionId) throws Exception {
		// TODO Auto-generated method stub
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointConditionId", pointConditionId);
			logger.info("查询积分查询条件为："+pointConditionId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointCondition", param);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分条件成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	@Override
	public String addpointCalcFactor(String json) throws Exception {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			logger.info("新增积分计算因子为："+jsonObj.toString());
			String pointCalcFactorId=dao.getPK("POINT_CALC_FACTOR");//获取积分规则主键
			jsonObj.put("pointCalcFactorId",pointCalcFactorId);
			//1:插入积分计算规则表
			dao.insert("InsertPointCalcFactor",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分条件成功",pointCalcFactorId); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}


	@Override
	public String querypointCalcFactor(String json, String pointCalcFactorId) throws Exception {
		// TODO Auto-generated method stub
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointConditionId", pointCalcFactorId);
			logger.info("查询积分查询条件为："+pointCalcFactorId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointCalcFactor", param);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分因子条件成功",result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

}
