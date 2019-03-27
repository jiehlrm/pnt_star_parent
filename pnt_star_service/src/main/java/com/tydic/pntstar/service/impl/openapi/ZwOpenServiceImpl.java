package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoZW;
import com.tydic.pntstar.service.openapi.ZwOpenService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service
@Component("zwOpenServiceImpl")
public class ZwOpenServiceImpl implements ZwOpenService {
	
	private static final Logger logger = LoggerFactory.getLogger(ZwOpenServiceImpl.class);
	private CommonDBDaoZW dao = (CommonDBDaoZW) SpringBeanUtil.getBean("dbdaoZW");

//	@Override//PREFER_TYPE_ID
//	public String queryBillOfferType(String json, String preferTypeId) throws Exception {
//		// TODO Auto-generated method stub
//		logger.info("server receive ===>"+json);
//		Map<String, Object> param = new HashMap<>();
//		param.put("preferTypeId", preferTypeId);
//		Map<String, Object> result = dao.queryForOne("QueryBillOfferType", param);
//		if (Tools.isNull(result)) {
//			return Tools.buildResponseData("1", "暂未查到数据 !", null);
//		}
//		logger.info("server reponse <==="+result);
//		return Tools.buildResponseData("0", "查询销售品类型定义成功", result);
//	}
//
//	@Override
//	public String addBillOfferType(String json) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public String queryBillOfferTypeOfferRel(String json, String recordId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("recordId", recordId);
		Map<String, Object> result = dao.queryForOne("QueryBillOfferTypeOfferRel", param);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "暂未查到数据 !", null);
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询销售品类型销售品关系成功", result);
	}

	@Override
	public String addBillOfferTypeOfferRel(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
		String  recordId=dao.getPK("BILL_OFFER_TYPE_OFFER_REL");
        jsonObj.put("recordId", recordId);
    	dao.insert("InsertBillOfferTypeOfferRel", jsonObj);
    	return Tools.buildResponseData("0", "新增销售品类型销售品关系成功", recordId);
	}

	@Override
	public String queryBillItemOfferRel(String json, String billItemOfferRelId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("billItemOfferRelId", billItemOfferRelId);
		Map<String, Object> result = dao.queryForOne("QueryBillItemOfferRel", param);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "暂未查到数据 !", null);
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询销售品帐单项关系成功", result);
	}

	@Override
	public String addBillItemOfferRel(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
		String  billItemOfferRelId=dao.getPK("BILL_ITEM_OFFER_REL");
        jsonObj.put("billItemOfferRelId", billItemOfferRelId);
    	dao.insert("InsertBillItemOfferRel", jsonObj);
    	return Tools.buildResponseData("0", "新增销售品帐单项关系成功", billItemOfferRelId);
	}

}
