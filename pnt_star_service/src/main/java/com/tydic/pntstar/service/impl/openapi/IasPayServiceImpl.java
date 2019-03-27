package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoZW;
import com.tydic.pntstar.service.openapi.IasPayService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service
@Component("iasPayServiceImpl")
public class IasPayServiceImpl implements IasPayService {
	private static final Logger logger = LoggerFactory.getLogger(IasPayServiceImpl.class);
	private CommonDBDaoZW dao = (CommonDBDaoZW) SpringBeanUtil.getBean("dbdaoZW");

	@Override
	public String isaAcctBalance(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else {
			jsonObj =JSONObject.parseObject(json);
		}
		Map<String, Object> result = new HashMap<>();
		//1:查询余额账本数据
		List<Map<String, Object>> data1= dao.query("QueryAcctBalanceIAS", jsonObj);
		//2:查询是否欠费
		List<Map<String, Object>> data2= dao.query("QueryAcctItemIAS", jsonObj);
		result.put("data1", data1);
		result.put("data2", data2);
      
		logger.info("server reponse <==="+result);
//		return Tools.buildResponseData("0", "查询俱乐部会员详情成功", resClubMember);
		return JSON.toJSONString(result);
	}

	@Override
	public String isaAcctBalancePay(String json) throws Exception {
		// TODO Auto-generated method stub
		//获取参数，解析数据
		Map<String,Object> jsonObj=JSONObject.parseObject(json);
	    //信息增强
		int AMOUNT=Integer.parseInt(jsonObj.get("AMOUNT").toString())*100;
		int QfAMOUNT=Tools.isNull(jsonObj.get("QfAMOUNT"))?0:Integer.parseInt(jsonObj.get("QfAMOUNT").toString())*100;
		
		jsonObj.put("AMOUNT",AMOUNT);
		jsonObj.put("QfAMOUNT", QfAMOUNT);
		jsonObj.put("ZSAMOUNT",AMOUNT-QfAMOUNT);
		jsonObj.put("OPER_PAYOUT_ID", "");
		//1：更新账目记录表，销账状态
		if(!Tools.isNull(jsonObj.get("ACCT_ITEM_ID"))){			
			dao.update("UpdateIasAcctItem", jsonObj);			
		}
		//2：新增付款记录表
		String PAYMENT_ID=dao.getPK("PAYMENT");
		jsonObj.put("PAYMENT_ID", PAYMENT_ID);
		dao.insert("InsertIasPayment", jsonObj);
		//3：新增销账记录
		String BILL_ID=dao.getPK("BILL");
		jsonObj.put("BILL_ID", BILL_ID);
		dao.insert("InsertIasBill", jsonObj);
		//4：更新余额账本信息
		dao.update("UpdateIasAcctBalance", jsonObj);
		//5：新增支出记录
		if(!Tools.isNull(jsonObj.get("ACCT_ITEM_ID"))){	
		String OPER_PAYOUT_ID=dao.getPK("BALANCE_PAYOUT");
		jsonObj.put("OPER_PAYOUT_ID", OPER_PAYOUT_ID);
		dao.insert("InsertIasBalancePayout", jsonObj);
		//6：支出账目
		String BAL_ACCT_ITEM_PAYED_ID=dao.getPK("BALANCE_ACCT_ITEM_PAYED");
		jsonObj.put("BAL_ACCT_ITEM_PAYED_ID", BAL_ACCT_ITEM_PAYED_ID);
		dao.insert("InsertIasBalAcctItemPayed", jsonObj);
		}
		//7：新增来源记录
		String OPER_INCOME_ID=dao.getPK("BALANCE_SOURCE");
		jsonObj.put("OPER_INCOME_ID", OPER_INCOME_ID);
		dao.insert("InsertIasBalanceSource", jsonObj);
		//8：日志表
		String BALANCE_LOG_ID=dao.getPK("ACCT_BALANCE_LOG");
		jsonObj.put("BALANCE_LOG_ID", BALANCE_LOG_ID);
		dao.insert("InsertIasAcctBalanceLog", jsonObj);
		return "success";
	}

}
