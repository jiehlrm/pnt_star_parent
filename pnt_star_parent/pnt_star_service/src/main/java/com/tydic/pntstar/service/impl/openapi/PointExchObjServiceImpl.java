/**
 * 
 */
package com.tydic.pntstar.service.impl.openapi;


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
	 */
	@Override
	public String addPointExchObj(String json) throws Exception {
		//参数校验(必填)
	    try {
	    	//新增积分对象
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			String pointExchObjId=dao.getPK("point_exch_obj");
			Map<String,Object> tariffMap=new HashMap<>();
			jsonObj.put("pointExchObjId", pointExchObjId);
			logger.info("新增积分兑换对象条件为："+jsonObj.toString());
	        dao.insert("InsertPointExchObj",jsonObj,true);
	        //新增积分兑换支付规则
	        List<Map<String,Object>> pointExchTariff=(List<Map<String, Object>>) jsonObj.get("pointExchTariff");
	        if(!Tools.isNull(pointExchTariff)) {
	        	for(int i=0;i<pointExchTariff.size();i++) {
	        		pointExchTariff.get(i).put("pointExchObjId", pointExchObjId);
	        		pointExchTariff.get(i).put("pointExchTariffId", dao.getPK("POINT_EXCH_TARIFF"));
	        		dao.insert("InsertPointExchTariff",pointExchTariff.get(i),true);
	        	}
	        }
	        //新增积分兑换对象构成 
	        List<Map<String,Object>> pointExchObjComp=(List<Map<String, Object>>) jsonObj.get("pointExchObjComp");
            if(!Tools.isNull(pointExchObjComp)) {
            	for(int i=0;i<pointExchObjComp.size();i++) {
            		pointExchObjComp.get(i).put("pointExchObjId", pointExchObjId);
            		pointExchObjComp.get(i).put("pointExchObjCompId", dao.getPK("POINT_EXCH_OBJ_COMP"));
	        		dao.insert("InsertPointExchObjComp",pointExchObjComp.get(i),true);
	        	}
	        }
            dao.commit();
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分兑换对象成功", queryPointExchAll(pointExchObjId)); 
        }catch(Exception e) {
        	dao.rollback();
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }finally {
        	dao.release();
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
		//查询积分兑换对象构成
		result.put("pointExchObjComp",dao.query("QueryPointExchObjCompList", param));
		return result;
	}

}
