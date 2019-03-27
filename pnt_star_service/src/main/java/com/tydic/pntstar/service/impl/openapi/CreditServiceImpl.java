package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.Global;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.CreditService;
import com.tydic.pntstar.util.CreditCommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;


@Service("creditServiceImpl")
public class CreditServiceImpl implements CreditService {

	private static final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	

	/**
	 * 根据ID查询单个信用度
	 */
	@Override
	public String querySingleCredit(String json, String creditId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("creditId", creditId);
		List<Map<String, Object>> result = dao.query("QUERY_CREDIT_QUERYCREDIT", jsonObject);
		if (CreditCommonUtil.isEmptyList(result)) {
			return CreditCommonUtil.reQueryFail(new String("未查到有效数据!"));
		}
		Map<String,Object> reJson = new HashMap<>();
		reJson.put("dataList", result.get(0));
		reJson.put("resultCode", "0");
		reJson.put("resultMsg", new String("根据ID查询信用度成功"));
		return JSONObject.toJSONString(reJson);
	}

	/**
	 * 新增信用度
	 */
	@Override
	public String addCredit(String json) throws Exception {
		Map<String,Object> map = CreditCommonUtil.json2Map(JSONObject.parseObject(json));
		String creditId=dao.getPK("credit");
		map.put("creditId",creditId);
		dao.update("INSERT_CREDIT_ADDCREDIT", map);
		JSONObject reJson = new JSONObject();
		reJson.put("resultCode", "0");
		reJson.put("resultMsg", "新增信用度成功");
		reJson.put("id", creditId);
//		reJson.put("dataList", dao.query("QUERY_CREDIT_QUERYCREDIT", map).get(0));
		return JSONObject.toJSONString(reJson);
	}

	/**
	 * 修改信用度
	 */
	@Override
	public String modifyCredit(String json, String creditId) throws Exception {
		Map<String,Object> paramMap = CreditCommonUtil.json2Map(JSONObject.parseObject(json));
		paramMap.put("creditId", creditId);
		dao.update("UPDATE_CREDIT_MODIFYCREDIT", paramMap);
		JSONObject reJson = new JSONObject();
		reJson.put("resultCode", "0");
		reJson.put("resultMsg", "修改信用度成功");
		Map<String,Object> param=new HashMap<>();
		param.put("creditId", creditId);
		reJson.put("dataList", dao.query("QUERY_CREDIT_QUERYCREDIT", param).get(0));
		return JSONObject.toJSONString(reJson);
	}

	/**
	 * 删除信用度
	 */
	@Override
	public String delCredit(String json, String creditId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("creditId", creditId);
		dao.delete("DELETE_CREDIT_DELCREDIT", param);
		return CreditCommonUtil.reDelSuccess(new String("删除信用度成功"));
	}

	/**
	 * 查询所有信用度
	 */
	@Override
	public String queryAllCredit(String json) throws Exception {
		List<Map<String, Object>> result = dao.query("QUERY_CREDIT_QUERYCREDIT", Tools.getParamsFromUrl());
		if (CreditCommonUtil.isEmptyList(result)) {
			return CreditCommonUtil.reQueryFail(new String("未查到有效数据!"));
		}
		JSONObject reJson = new JSONObject();
		reJson.put("resultCode", "0");
		reJson.put("resultMsg", new String("查询信用度列表成功"));
		reJson.put("dataList", CreditCommonUtil.list2JsonArray(result));
		return JSONObject.toJSONString(reJson);
	}

	/**
	 * 信用度历史查询 10000号人工业务
	 */
	@Override
	public String CheckServQuotaQuery(String json) throws Exception {
		// TODO Auto-generated method stub
		// 解析参数
		Map<String,Object> jsonObj;
		if(Tools.isNull(json)) {//http请求
			jsonObj=Tools.getParamsFromUrl();
		}else {//dubbo服务
			jsonObj=JSONObject.parseObject(json);
		}
		//参数校验
	    if(Tools.isNull(jsonObj.get("accNum")) || Tools.isNull(jsonObj.get("prodId"))
	    		|| Tools.isNull(jsonObj.get("objType"))){
	    	return CreditCommonUtil.reQueryFail(new String("必填参数不能为空!"));
	    }
		//1:查询数据库，产品实例id
	    List<Map<String,Object>> result1 = dao.query("QueryProdInstId", jsonObj);
	    if(Tools.isNull(result1)||result1.size()<1 ) {
	    	return CreditCommonUtil.buildResponseData("1","参数条件查无结果",null);
	    }
	    jsonObj.put("objId",result1.get(0).get("objId"));
	    //2:查询数据库，获取信用等级
	    List<Map<String,Object>> result2 = dao.query("QueryCreditLog", jsonObj);
		//组装数据，返回固定格式的json报文
	    return CreditCommonUtil.buildResponseData("0","调用成功", result2);
	}

	/**
	 * 10000号根据业务号码查询实时信用额度
	 */
	@Override
	public String queryCrdByServNum(String json) throws Exception {
		// TODO Auto-generated method stub
		// 解析参数
		Map<String,Object> jsonObj;
		if(Tools.isNull(json)) {//http请求
			jsonObj=Tools.getParamsFromUrl();
		}else {//dubbo服务
			jsonObj=JSONObject.parseObject(json);
		}
		Map<String,Object> result=new HashMap<>();
		//参数校验
	    if(Tools.isNull(jsonObj.get("accNum")) || Tools.isNull(jsonObj.get("prodId"))
	    		|| Tools.isNull(jsonObj.get("objType"))){
	    	return CreditCommonUtil.reQueryFail(new String("必填参数不能为空!"));
	    }
		//1:查询数据库，产品实例id   产品要是在售状态的100000
	    jsonObj.put("statusCd", "100000");
	    List<Map<String,Object>> result1 = dao.query("QueryProdInstId", jsonObj);
	    if(Tools.isNull(result1)||result1.size()<1 ) {
	    	return CreditCommonUtil.buildResponseData("1","参数条件查无结果",null);
	    }
	    jsonObj.put("objId",result1.get(0).get("objId"));
	    jsonObj.put("latnId",result1.get(0).get("latnId"));
	    //2:查询数据库，获取信用等级
	    List<Map<String,Object>> result2 = dao.query("QueryCreditLevel", jsonObj);
	    String creditValue;//信用额度值
	    String creditLevel;//信用等级
	    if(Tools.isNull(result2)||result2.size()<1 ) {
	    	creditLevel="";
	    }else {
	    	creditLevel=result2.get(0).get("creditLevel").toString();
	    }
	    //3:查询数据库，获取信用等级
	    List<Map<String,Object>> result3 = dao.query("QueryCreditLimitFee", jsonObj);
	    if(Tools.isNull(result3)||result3.size()<1 ) {
	    	creditValue="";
	    }else {
	    	creditValue=result3.get(0).get("creditLimitFee").toString();
	    }
	    
		//组装数据，返回固定格式的json报文
	    result.put("accNum", jsonObj.get("accNum"));//返回业务号码
	    result.put("prodId", jsonObj.get("prodId"));//返回产品类型
	    result.put("creditLevel", creditLevel);//获取信用等级
	    result.put("creditLimitFee",creditValue );//获取信用额度
		return CreditCommonUtil.buildResponseData("0","信用度查询成功", result);
	}

	@Override
	public String test01(String json) throws Exception {
		// TODO Auto-generated method stub
	//	initCrdFlushService.runInitCrdPdAll();
		return CreditCommonUtil.buildResponseData("0","存储过程调用成功", null);
	}
}
