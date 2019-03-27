/**
 * 
 */
package com.tydic.pntstar.service.impl.openapi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointExchObjService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * @author root
 * 积分兑换对象实现类
 * 1：原子服务
 */
@Service("pointExchObjServiceImpl")
public class PointExchObjServiceImpl implements PointExchObjService {

	private static final Logger logger = LoggerFactory.getLogger(PointExchObjServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	
	/**
	 * 根据积分兑换对象id主键查询
	 */
	@Override
	public String queryPointExchObj(String json, String pointExchObjId) throws Exception {
		// TODO Auto-generated method stub
		//解析参数条件
		try {					
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换对象成功", queryPointExchAll(pointExchObjId));
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	/**
	 * 新增积分兑换对象
	 * point_exch_cond
point_exch_tariff
point_exch_obj_cond_rel
point_exch_obj_ctlg
point_exch_tariff_cond_rel

	 */
	@Override
	public String addPointExchObj(String json) throws Exception {
		//参数校验(必填)
	    try {
	    	//新增积分兑换对象
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			String pointExchObjId=dao.getPK("point_exch_obj");
			jsonObj.put("pointExchObjId", pointExchObjId);
			logger.info("新增积分兑换对象条件为："+jsonObj.toString());
			//抽取查询数据
			List<Map<String,Object>> pointExchTariff=(List<Map<String, Object>>) jsonObj.get("pointExchTariff");//兑换规则
			List<Map<String,Object>> pointExchCond=(List<Map<String, Object>>) jsonObj.get("pointExchCond");//兑换条件
			List<Map<String,Object>> pointExchObjCtlg=(List<Map<String, Object>>) jsonObj.get("pointExchObjCtlg");//兑换条件
			List<Map<String,Object>> condRelList=new ArrayList<>();//支付关系表
			//1:新增积分兑换对象
	        dao.insert("InsertPointExchObj",jsonObj);
	        //2:新增积分兑换支付规则POINT_EXCH_TARIFF 
	        if(!Tools.isNull(pointExchTariff)) {
	        	for(int i=0;i<pointExchTariff.size();i++) {
	        		pointExchTariff.get(i).put("pointExchObjId", pointExchObjId);
	        		String pointExchTariffId=dao.getPK("POINT_EXCH_TARIFF");
	        		pointExchTariff.get(i).put("pointExchTariffId", pointExchTariffId);
	        		dao.insert("InsertPointExchTariff",pointExchTariff.get(i));
	        		condRelList=(List<Map<String, Object>>) pointExchTariff.get(i).get("condRelList");
	        		if(!Tools.isNull(condRelList)){//创建积分支付和条件关联表
	        			for(int j=0;j<condRelList.size();j++) {
	        				String pointExchTariffCondRelId=dao.getPK("POINT_EXCH_TARIFF_COND_REL");
	        				pointExchTariff.get(i).put("pointExchTariffCondRelId", pointExchTariffCondRelId);
	        				pointExchTariff.get(i).put("pointExchCondId", condRelList.get(j).get("pointExchCondId"));
	        				dao.insert("InsertPointExchTariffCondRel", pointExchTariff.get(i));	        				
	        			}
	        		}
	        	}
	        }
	        //3:新增积分兑换条件信息point_exch_cond
	        if(!Tools.isNull(pointExchCond)) {
	        	for(int i=0;i<pointExchCond.size();i++) {
	        		String pointExchCondId=dao.getPK("POINT_EXCH_COND");
	        		String pointExchObjCondRelId=dao.getPK("POINT_EXCH_Obj_COND_rel");
	        		pointExchCond.get(i).put("pointExchCondId", pointExchCondId);
	        		dao.insert("InsertPointExchCond",pointExchCond.get(i));
	        		pointExchCond.get(i).put("pointExchObjId", pointExchObjId);
	        		pointExchCond.get(i).put("pointExchObjCondRelId", pointExchObjCondRelId);
	        		dao.insert("InsertPointExchObjCondRel",pointExchCond.get(i));
	        	}
	        }
	        
	        //4:新增积分兑换对象目录  point_exch_obj_ctlg
	        if(!Tools.isNull(pointExchObjCtlg)) {
	        	for(int i=0;i<pointExchObjCtlg.size();i++) {
	        		pointExchObjCtlg.get(i).put("pointExchObjId", pointExchObjId);
	        		pointExchObjCtlg.get(i).put("pointExchObjCtlgId", dao.getPK("POINT_EXCH_OBJ_CTLG"));
	        		dao.insert("InsertPointExchObjCtlg",pointExchObjCtlg.get(i));
	        	}
	        }
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分兑换对象成功", pointExchObjId); 
	        //queryPointExchAll(pointExchObjId)
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	/**
	 * 修改积分兑换对象
	 */
	@Override
	public String modifyPointExchObj(String json, String pointExchObjId) throws Exception {
		try {
			Map<String,Object> jsonObj=JSONObject.parseObject(json);
			jsonObj.put("pointExchObjId", pointExchObjId);
			logger.info("修改积分兑换对象条件为："+jsonObj.toString());
			dao.update("UpdatePointExchObj", jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointExchObj",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "修改积分兑换对象成功", result);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
		
	}
	
	/**
	 * 删除积分兑换对象
	 */
	@Override
	public String delPointExchObj(String json, String pointExchObjId) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointExchObjId", pointExchObjId);
			logger.info("删除积分兑换对象条件为："+param.toString());
			dao.delete("DeletePointExchObj", param);
			return Tools.buildResponseData(Tools.SUCCESS, "删除积分兑换对象成功", null);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 查询积分兑换对象列表
	 */
	@Override
	public String queryPointExchObjList(String json) throws Exception {
		//支持dubbo和rest调用，判断依据json是否为null或者空
		try {
			Map<String,Object>  jsonObj = null;
			if(Tools.isNull(json)) {
				jsonObj=Tools.getParamsFromUrl();
			}else {
				jsonObj=Tools.String2Map(json);
			}
			logger.info("查询积分兑换对象条件为："+jsonObj.toString());
			List<Map<String, Object>> result = dao.query("QueryPointExchObjList", jsonObj);
			if(!Tools.isNull(result)) {
				Map<String,Object> map=new HashMap<>();
				for(int i =0 ;i<result.size();i++) {
					map.put("pointExchObjId", result.get(i).get("pointExchObjId"));
					result.get(i).put("pointExchObjComp",dao.query("QueryPointExchObjCompList", map));
				}
			}
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换对象列表成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}
	
	public Map queryPointExchAll(String pointExchObjId) {
		Map<String, Object> param = new HashMap<>();
		param.put("pointExchObjId", pointExchObjId);
		logger.info("查询积分兑换对象查询条件为："+pointExchObjId.toString());
		Map<String, Object> result = dao.queryForOne("QueryPointExchObj", param);
		if (Tools.isNull(result)) {
			return null;
		}
		//查询积分兑换支付规则
		result.put("pointExchTariff",dao.query("QueryPointExchTariffList", param));
		//查询积分条件
        result.put("pointExchCond", dao.query("QueryPointExchCondByObj", param));
		//查询
    	Map<String, Object> pointExchObjCtlg=(Map<String, Object>) dao.queryForOne("QueryPointExchObjCtlg", param);
    	
        if(!Tools.isNull(pointExchObjCtlg)){
        	result.put("pointExchObjCtlg", pointExchObjCtlg.get("catalogItemId"));        	
        }
		return result;
	}

}
