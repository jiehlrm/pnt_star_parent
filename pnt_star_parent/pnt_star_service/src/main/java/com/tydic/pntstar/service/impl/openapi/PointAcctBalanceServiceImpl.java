package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointAcctBalanceService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;


/*
 * 集团积分账本API实现类
 */
@Service("pointAcctBalanceServiceImpl")
public class PointAcctBalanceServiceImpl implements PointAcctBalanceService{
	private static final Logger logger = LoggerFactory.getLogger(PointAcctServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	
	/**
	 * 根据积分账本id主键查询
	 */
	@Override
	public String queryPointAcctBalance(String json, String pointAcctBalanceId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("pointAcctBalanceId", pointAcctBalanceId);
		Map<String, Object> result = dao.queryForOne("QueryPointAcctBalanceList", param);//查询积分账本主要信息
		logger.info("查询积分账本主要信息："+result);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "查询积分账本失败", null);
		}
		Map<String, Object> p = new HashMap<>();
		p.put("pointTypeId", result.get("pointTypeId"));
		Map<String, Object> res = dao.queryForOne("QueryPointType", p);
		if (Tools.isNull(res)) {
			return Tools.buildResponseData("1", "查询积分账本失败", null);
		}
		result.put("pointType", result.get("pointTypeId")+"("+res.get("pointTypeName")+")");//修饰出参
		List<Map<String, Object>> ponitBalanceSource = dao.query("QueryPointBalanceSource", param);//查询积分账本下积分来源信息
		logger.info("查询积分账本下积分来源信息："+ponitBalanceSource);
		result.put("ponitBalanceSource", ponitBalanceSource);
		result.remove("pointTypeId");
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询积分账本成功", result);
	}

	/**
	 * 新增积分帐本
	 */
	@Override
	public String addPointAcctBalance(String json) throws Exception {
		//入参修改
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj =JSONObject.parseObject(json);
		try {
			jsonObj.put("pointAcctBalanceId", dao.getPK("POINT_ACCT_BALANCE"));//获取积分帐本表主键ID
			String pointType = jsonObj.get("pointType").toString();
			jsonObj.put("pointTypeId", pointType.substring(0, pointType.indexOf('(')));
			jsonObj.put("presentFlag", 0);
			jsonObj.put("pointInitValue", jsonObj.get("pointValue"));
			jsonObj.put("pointCycleEndType", "1");
			jsonObj.put("createStaff", "112123");//测试使用
			jsonObj.put("updateStaff", "124124");//测试使用
			String statusCd = jsonObj.get("statusCd").toString();
			jsonObj.remove("statusCd");
			jsonObj.put("statusCd", statusCd.substring(0, statusCd.indexOf('(')));
			//新增积分账本
			dao.insert("InsertPointAcctBalance",jsonObj);
		} catch (Exception e) {
			return Tools.buildResponseData("1", "新增积分账本失败", null); 
		}
		//返回报文信息
		Map<String, Object> result = dao.queryForOne("QueryPointAcctBalanceList",jsonObj);
		Map<String, Object> p = new HashMap<>();
		p.put("pointTypeId", result.get("pointTypeId"));
		Map<String, Object> res = dao.queryForOne("QueryPointType", p);
		result.put("pointType", result.get("pointTypeId")+"("+res.get("pointTypeName")+")");
		result.remove("pointTypeId");
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "新增积分账本成功", result); 
	}

	/**
	 * 修改积分帐本
	 */
	@Override
	public String modifyPointAcctBalance(String json, String pointAcctBalanceId) throws Exception {
//		logger.info("server receive ===>"+json);
//		Map<String,Object> jsonObj=JSONObject.parseObject(json);
//		jsonObj.put("pointAcctBalanceId", pointAcctBalanceId);
//		dao.update("UpdatePointAcctBalance", jsonObj);
//		//返回报文信息
//		Map<String, Object> result = dao.queryForOne("QueryPointAcctBalanceList",jsonObj);
//		Map<String, Object> p = new HashMap<>();
//		p.put("pointTypeId", result.get("pointTypeId"));
//		Map<String, Object> res = dao.queryForOne("QueryPointType", p);
//		result.put("pointType", result.get("pointTypeId")+"("+res.get("pointTypeName")+")");
//		result.remove("pointTypeId");
//		logger.info("server reponse <==="+result);
//		return Tools.buildResponseData("0", "修改积分账本成功", result);
		logger.info("server receive ===>"+json);
		List<Map> reqMap = JSONObject.parseArray(json,Map.class);
		for(Map m:reqMap) {
			if("replace".equals(m.get("op"))) {
				m.put("updateStaff", "1111");//测试使用
				m.put("pointAcctBalanceId", pointAcctBalanceId);
				dao.update("UpdatePointAcctBalance", m);
			}
		}
		//返回报文信息
		Map<String, Object> param = new HashMap<>();
		param.put("pointAcctBalanceId", pointAcctBalanceId);
		Map<String, Object> result = dao.queryForOne("QueryPointAcctBalanceList",param);
		Map<String, Object> p = new HashMap<>();
		p.put("pointTypeId", result.get("pointTypeId"));
		Map<String, Object> res = dao.queryForOne("QueryPointType", p);
		result.put("pointType", result.get("pointTypeId")+"("+res.get("pointTypeName")+")");
		result.remove("pointTypeId");
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "修改积分账本成功", result);
	}

	/**
	 * 删除积分帐本
	 */
	@Override
	public String delPointAcctBalance(String json, String pointAcctBalanceId) throws Exception {
		logger.info("server receive ===>"+pointAcctBalanceId);
		Map<String, Object> param = new HashMap<>();
		param.put("pointAcctBalanceId", pointAcctBalanceId);
		dao.delete("DeletePointAcctBalance", param);
		return Tools.buildResponseData("0", "删除积分账本成功", null);
	}

	/**
	 * 查询积分帐本列表
	 */
	@Override
	public String queryPointAcctBalanceList(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object>  jsonObj = null;
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else {
			jsonObj=Tools.String2Map(json);//dubbo
		}
		List<Map<String, Object>> result = dao.query("QueryPointAcctBalanceList", jsonObj);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询积分账本列表成功", result);
	}

}
