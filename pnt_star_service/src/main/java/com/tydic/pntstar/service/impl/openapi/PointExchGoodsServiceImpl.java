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
import com.tydic.pntstar.service.openapi.PointExchGoodsService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * @author root
 * 积分兑换货品实现类
 * 1：原子服务
 */
@Service("pointExchGoodsServiceImpl")
public class PointExchGoodsServiceImpl implements PointExchGoodsService {

	private static final Logger logger = LoggerFactory.getLogger(PointExchGoodsServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	
	/**
	 * 根据积分兑换货品id主键查询
	 */
	@Override
	public String queryPointExchGoods(String json, String pointExchGoodsId) throws Exception {
		// TODO Auto-generated method stub
		//解析参数条件
		try {			
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换货品成功", queryPointExchAll(pointExchGoodsId));
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	/**
	 * 新增积分兑换货品
	 */
	@Override
	public String addPointExchGoods(String json) throws Exception {
		//参数校验(必填)
	    try {
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
			String pointExchGoodsId=dao.getPK("point_exch_goods");
			String pointExchGoodsCtlgId=dao.getPK("POINT_EXCH_GOODS_CTLG");
			jsonObj.put("pointExchGoodsId", pointExchGoodsId);
			logger.info("新增积分兑换货品条件为："+jsonObj.toString());
	        dao.insert("InsertPointExchGoods",jsonObj);
//	        Map<String, Object> result = dao.queryForOne("QueryPointExchGoods",jsonObj);
	        //新增积分货品目录POINT_EXCH_GOODS_CTLG
	        Map<String, Object> pointExchGoodsCtlg=(Map<String, Object>) jsonObj.get("pointExchGoodsCtlg");
	        pointExchGoodsCtlg.put("pointExchGoodsId", pointExchGoodsId);
	        pointExchGoodsCtlg.put("pointExchGoodsCtlgId", pointExchGoodsCtlgId);
	        logger.info("新增积分兑换货品目录条件为："+pointExchGoodsCtlg.toString());
	        dao.insert("InsertPointExchGoodsCtlg", pointExchGoodsCtlg);
	        jsonObj.put("pointExchGoodsCtlg", pointExchGoodsCtlg);
	        return Tools.buildResponseData(Tools.SUCCESS, "新增积分兑换货品成功", jsonObj); 
        }catch(Exception e) {
        	return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
        }
	}

	/**
	 * 修改积分兑换货品
	 */
	@Override
	public String modifyPointExchGoods(String json, String pointExchGoodsId) throws Exception {
		try {
			Map<String,Object> jsonObj=JSONObject.parseObject(json);
			jsonObj.put("pointExchGoodsId", pointExchGoodsId);
			logger.info("修改积分兑换货品条件为："+jsonObj.toString());
			dao.update("UpdatePointExchGoods", jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointExchGoods",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "修改积分兑换货品成功", result);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
		
	}
	
	/**
	 * 删除积分兑换货品
	 */
	@Override
	public String delPointExchGoods(String json, String pointExchGoodsId) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointExchGoodsId", pointExchGoodsId);
			logger.info("删除积分兑换货品条件为："+param.toString());
			dao.delete("DeletePointExchGoods", param);
			return Tools.buildResponseData(Tools.SUCCESS, "删除积分兑换货品成功", null);			
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 查询积分兑换货品列表
	 */
	@Override
	public String queryPointExchGoodsList(String json) throws Exception {
		//支持dubbo和rest调用，判断依据json是否为null或者空
		try {
			Map<String,Object>  jsonObj = null;
			if(Tools.isNull(json)) {
				jsonObj=Tools.getParamsFromUrl();
			}else {
				jsonObj=Tools.String2Map(json);
			}
			logger.info("查询积分兑换货品条件为："+jsonObj.toString());
			List<Map<String, Object>> result = dao.query("QueryPointExchGoodsList", jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分兑换货品列表成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}
	
	public Map queryPointExchAll(String pointExchGoodsId) {
		Map<String, Object> param = new HashMap<>();
		param.put("pointExchGoodsId", pointExchGoodsId);
		logger.info("查询积分兑换货品查询条件为："+pointExchGoodsId.toString());
		Map<String, Object> result = dao.queryForOne("QueryPointExchGoods", param);
		if (Tools.isNull(result)) {
			return null;
		}
		//查询积分兑换货品目录位置
		result.put("pointExchGoodsCtlg",dao.query("QueryPointExchGoodsCtlg", param));
		return result;
	}

}
