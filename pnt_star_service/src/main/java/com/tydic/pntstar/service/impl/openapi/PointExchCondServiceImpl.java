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
import com.tydic.pntstar.service.openapi.PointExchCondService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * @author root
 * 积分兑换条件实现类
 * 1：原子服务
 */
@Service("pointExchCondServiceImpl")
public class PointExchCondServiceImpl implements PointExchCondService {

	private static final Logger logger = LoggerFactory.getLogger(PointExchCondServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	/**
	 * 根据积分兑换条件id主键查询
	 */
	@Override
	public String queryPointExchCond(String json, String pointExchCondId) throws Exception {
		// TODO Auto-generated method stub
		//解析参数条件
		try {			
			Map<String, Object> param = new HashMap<>();
			param.put("pointExchCondId", pointExchCondId);
			logger.info("查询积分兑换条件查询条件为："+pointExchCondId.toString());
			Map<String, Object> result = dao.queryForOne("QueryPointExchCond", param);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换条件成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	/**
	 * 新增积分兑换条件
	 */
	@Override
	public String addPointExchCond(String json) throws Exception {
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			String pointExchCondId=dao.getPK("point_exch_cond");
			jsonObj.put("pointExchCondId", pointExchCondId);
			logger.info("新增积分兑换条件条件为："+jsonObj.toString());
	        dao.insert("InsertPointExchCond",jsonObj);
	        Map<String, Object> result = dao.queryForOne("QueryPointExchCond",jsonObj);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分兑换条件成功", result); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	/**
	 * 修改积分兑换条件
	 */
	@Override
	public String modifyPointExchCond(String json, String pointExchCondId) throws Exception {
		try {
			Map<String,Object> jsonObj=JSONObject.parseObject(json);
			jsonObj.put("pointExchCondId", pointExchCondId);
			logger.info("修改积分兑换条件条件为："+jsonObj.toString());
			dao.update("UpdatePointExchCond", jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointExchCond",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "修改积分兑换条件成功", result);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
		
	}
	
	/**
	 * 删除积分兑换条件
	 */
	@Override
	public String delPointExchCond(String json, String pointExchCondId) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointExchCondId", pointExchCondId);
			logger.info("删除积分兑换条件条件为："+param.toString());
			dao.delete("DeletePointExchCond", param);
			return Tools.buildResponseData(Tools.SUCCESS, "删除积分兑换条件成功", null);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 查询积分兑换条件列表
	 */
	@Override
	public String queryPointExchCondList(String json) throws Exception {
		//支持dubbo和rest调用，判断依据json是否为null或者空
		try {
			Map<String,Object>  jsonObj = null;
			if(Tools.isNull(json)) {
				jsonObj=Tools.getParamsFromUrl();
			}else {
				jsonObj=Tools.String2Map(json);
			}
			logger.info("查询积分兑换条件条件为："+jsonObj.toString());
			List<Map<String, Object>> result = dao.query("QueryPointExchCondList", jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换条件列表成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

}
