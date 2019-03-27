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
import com.tydic.pntstar.service.openapi.PointExchRecordService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * @author root
 * 积分兑换记录实现类
 * 1：原子服务
 */

@Service("pointExchRecordServiceImpl")
public class PointExchRecordServiceImpl implements PointExchRecordService {

	private static final Logger logger = LoggerFactory.getLogger(PointExchRecordServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	/**
	 * 根据积分兑换记录id主键查询
	 */
	@Override
	public String queryPointExchRecord(String json, String pointExchRecordId) throws Exception {
		// TODO Auto-generated method stub
		//解析参数条件
		try {			
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换记录成功", queryPointExchAll(pointExchRecordId));
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	/**
	 * 新增积分兑换记录
	 */
	@Override
	public String addPointExchRecord(String json) throws Exception {
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			String pointExchRecordId=dao.getPK("point_exch_Record");
			jsonObj.put("pointExchRecordId", pointExchRecordId);
			logger.info("新增积分兑换记录条件为："+jsonObj.toString());
			dao.insert("InsertPointExchRecord",jsonObj);
	        //新增积分兑换记录对象明细
	        List<Map<String,Object>> pointExchRecDetail=(List<Map<String, Object>>) jsonObj.get("pointExchRecDetail");
	        if(!Tools.isNull(pointExchRecDetail)) {
	        	for(int i=0;i<pointExchRecDetail.size();i++) {
	        		pointExchRecDetail.get(i).put("pointExchRecordId", pointExchRecordId);//dao.getPK("POINT_EXCH_REC_DETAIL")
	        		pointExchRecDetail.get(i).put("pointExchRecDetailId",dao.getPK("POINT_EXCH_REC_DETAIL"));
	        		dao.insert("InsertPointExchRecDetail",pointExchRecDetail.get(i));
	        	}
	        }
	        dao.commit();
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分兑换记录成功", queryPointExchAll(pointExchRecordId)); 
        }catch(Exception e) {
        	dao.rollback();
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }finally{
        	dao.release();
        }
	}

	/**
	 * 修改积分兑换记录
	 */
	@Override
	public String modifyPointExchRecord(String json, String pointExchRecordId) throws Exception {
		try {
			Map<String,Object> jsonObj=JSONObject.parseObject(json);
			jsonObj.put("pointExchRecordId", pointExchRecordId);
			logger.info("修改积分兑换记录条件为："+jsonObj.toString());
			dao.update("UpdatePointExchRecord", jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointExchRecord",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "修改积分兑换记录成功", result);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
		
	}
	
	/**
	 * 删除积分兑换记录
	 */
	@Override
	public String delPointExchRecord(String json, String pointExchRecordId) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointExchRecordId", pointExchRecordId);
			logger.info("删除积分兑换记录条件为："+param.toString());
			dao.delete("DeletePointExchRecord", param);
			return Tools.buildResponseData(Tools.SUCCESS, "删除积分兑换记录成功", null);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 查询积分兑换记录列表
	 */
	@Override
	public String queryPointExchRecordList(String json) throws Exception {
		//支持dubbo和rest调用，判断依据json是否为null或者空
		try {
			Map<String,Object>  jsonObj = null;
			if(Tools.isNull(json)) {
				jsonObj=Tools.getParamsFromUrl();
			}else {
				jsonObj=Tools.String2Map(json);
			}
			logger.info("查询积分兑换记录条件为："+jsonObj.toString());
			List<Map<String, Object>> result = dao.query("QueryPointExchRecordList", jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换记录列表成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}
	
	public Map queryPointExchAll(String pointExchRecordId) {
		Map<String, Object> param = new HashMap<>();
		param.put("pointExchRecordId", pointExchRecordId);
		logger.info("查询积分兑换记录查询条件为："+pointExchRecordId.toString());
		Map<String, Object> result = dao.queryForOne("QueryPointExchRecord", param);
		if (Tools.isNull(result)) {
			return null;
		}
		//查询积分兑换记录对象明细
		result.put("pointExchRecDetail",dao.query("QueryPointExchRecDetailList", param));
		return result;
	}

}
