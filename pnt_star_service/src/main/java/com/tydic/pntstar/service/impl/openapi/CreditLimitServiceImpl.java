package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.CreditConstant;
import com.tydic.pntstar.constant.Global;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.CreditLimitService;
import com.tydic.pntstar.util.CreditCommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;


@Service("creditLimitServiceImpl")
public class CreditLimitServiceImpl implements CreditLimitService {

	private static final Logger logger = LoggerFactory.getLogger(CreditLimitServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	/**
	 * 根据信用额度id主键查询
	 * 
	 * @param json
	 * @param creditLimitId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryCreditLimit(String json, Integer creditLimitId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("creditLimitId", creditLimitId);
		List<Map<String, Object>> result = dao.query(CreditConstant.action.QUERY_ACTION, jsonObject);
		if (CreditCommonUtil.isEmptyList(result)) {
			return CreditCommonUtil.reQueryFail(CreditConstant.resultMsg.QUERY_FAIL_MSG);
		}
		return CreditCommonUtil.buildResponseData("0",CreditConstant.resultMsg.QUERY_SUCCESS_MSG, result.get(0));
	}

	/**
	 * 新增信用额度
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Override
	public String addCreditLimit(String json) throws Exception {
	    Map<String, Object> param = CreditCommonUtil.json2Map(JSONObject.parseObject(json));
//	    int creditLimitId=Global.getPK();
	    String creditLimitId=dao.getPK("credit_Limit");
	    param.put("creditLimitId", creditLimitId);
	    dao.update(CreditConstant.action.ADD_ACTION, param);
	    return CreditCommonUtil.buildResponseData("0",CreditConstant.resultMsg.ADD_SUCCESS_MSG,creditLimitId);
	}

	/**
	 * 修改信用额度
	 * 
	 * @param json
	 * @param creditLimitId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String modifyCreditLimit(String json, Integer creditLimitId) throws Exception {
		Map<String,Object> map=CreditCommonUtil.json2Map(JSONObject.parseObject(json));
		map.put("creditLimitId", creditLimitId);
		dao.query(CreditConstant.action.MODIFY_ACTION, map);
		Map<String,Object> param=new HashMap<>();
		param.put("creditLimitId", creditLimitId);
	    return CreditCommonUtil.buildResponseData("0",CreditConstant.resultMsg.MODIFY_SUCCESS_MSG,dao.query(CreditConstant.action.QUERY_ACTION, param).get(0));
	}

	/**
	 * 删除信用额度（根据实际id）
	 * 
	 * @param json
	 * @param creditLimitId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String deleteCreditLimit(String json, Integer creditLimitId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("creditLimitId", creditLimitId);
		dao.delete(CreditConstant.action.DELETE_ACTION, param);
		return CreditCommonUtil.buildResponseData("0",CreditConstant.resultMsg.DELETE_SUCCESS_MSG,null);
	}

	/**
	 * 查询列表
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Override
	public String listCreditLimit(String json) {
		Map<String, Object> param = null;
		if (json == null || json.equals("")) {
			param = Tools.getParamsFromUrl();
		} else {
			param = CreditCommonUtil.json2Map(JSONObject.parseObject(json));
		}
		List<Map<String, Object>> result = dao.query(CreditConstant.action.QUERY_ACTION, param);
		if (CreditCommonUtil.isEmptyList(result)) {
			return CreditCommonUtil.buildResponseData("1",CreditConstant.resultMsg.QUERY_LIST_FAIL_MSG,result);
		}
		return CreditCommonUtil.buildResponseData("0",CreditConstant.resultMsg.QUERY_LIST_SUCCESS_MSG,result);
	}

	/**
	 * 为Json结果添加返回信息
	 * 
	 * @param json
	 * @param resultCode
	 * @param resultMsg
	 * @return
	 * @throws Exception
	 */
	private static JSONObject addResultMsg(JSONObject json, String resultCode, String resultMsg) {
		json.put("resultCode", resultCode);
		json.put("resultMsg", resultMsg);
		return json;
	}

}
