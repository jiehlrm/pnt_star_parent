package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.dao.CommonDBDaoZW;
import com.tydic.pntstar.service.openapi.ZwService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("zwServiceImpl")
public class ZwServiceImpl implements ZwService{
	private static final Logger logger = LoggerFactory.getLogger(PointAcctServiceImpl.class);
	private CommonDBDaoZW dao = (CommonDBDaoZW) SpringBeanUtil.getBean("dbdaoZW");
	
	@Override
	public String invoiceA(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=JSONObject.parseObject(json);
		String type=jsonObj.get("type").toString();
        if(type.equals("1")) {
        	String  invoiceId=dao.getPK("INVOICE");
            String  invoiceDetailId=dao.getPK("INVOICE_DETAIL");
            String  invOperRecordId=dao.getPK("INV_OPER_RECORD");
			jsonObj.put("OPER_ID", invOperRecordId);
            jsonObj.put("INVOICE_ID", invoiceId);
            jsonObj.put("INVOICE_DETAIL_ID", invoiceDetailId);
        	dao.insert("addComInvoice", jsonObj);
        	dao.insert("addComInvoiceDet", jsonObj);
        }else if(type.equals("2")) {
        	String  vatInvoiceId=dao.getPK("VAT_INVOICE");
            String  vatInvoiceDetId=dao.getPK("VAT_INVOICE_DETAIL");
            jsonObj.put("VAT_INVOICE_ID", vatInvoiceId);
            jsonObj.put("VAT_INVOICE_DTL_ID", vatInvoiceDetId);
        	dao.insert("addProInvoice", jsonObj);
        	dao.insert("addProInvoiceDet", jsonObj);
        }else {
            String  invoiceTypeId=dao.getPK("INVOICE_TYPE");
            String  invPaymentRelId=dao.getPK("INV_PAYMENT_REL");
            jsonObj.put("INVOICE_TYPE_ID", invoiceTypeId);
            jsonObj.put("PAYMENT_ID", invPaymentRelId);
            List<Map<String,Object>> data=dao.query("qurOperId", jsonObj);
            if(data!=null && data.size()!=0) {
            	String qurOperId=data.get(0).get("OPER_ID").toString();
            	jsonObj.put("OPER_ID", qurOperId);
            }else {
            	return "failure";
            }
        	dao.insert("addOperInvoiceRec", jsonObj);
        	dao.insert("addOperInvoiceType", jsonObj);
        	dao.insert("addOperInvoiceRel", jsonObj);
        }
        return "success";
	}

	@Override
	public String invoiceQ(String json, String AccountId) throws Exception {
        Map<String,Object> AccountParam=new HashMap<>();
		AccountParam.put("AccountDetId", json);
		
		Map<String,Object> mapper=new HashMap<>();
		List<Map<String,Object>> AcctBalanceData=dao.query("qurComInvoiceCount", AccountParam);
		
		List<Map<String,Object>> BillData=dao.query("qurProInvoiceCount", AccountParam);
		
		List<Map<String,Object>> PaymentData=dao.query("qurOperInvoiceCount", AccountParam);
		
		mapper.put("acctBalanceData", AcctBalanceData);
		mapper.put("billData", BillData);
		mapper.put("paymentData", PaymentData);
		logger.info("键值对"+mapper.toString());
		return   JSON.toJSONString(mapper);                   
	}

	@Override
	public String acctBalanceQ(String json, String ACCT_BALANCE_ID) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("ACCT_BALANCE_ID", ACCT_BALANCE_ID);
		
		Map<String, Object> result = new HashMap<>();
		//1:查询账本变动日志表
		List<Map<String, Object>> data1= dao.query("qurAcctBalLog", param);
		//2:查询账本支出日志表
		List<Map<String, Object>> data2= dao.query("qurBalPayout", param);
		//3:查询账本来源日志表
		List<Map<String, Object>> data3= dao.query("qurBalSource", param);
		result.put("data1", data1);
		result.put("data2", data2);
		result.put("data3", data3);
       
		logger.info("server reponse <==="+result);
//		return Tools.buildResponseData("0", "查询俱乐部会员详情成功", resClubMember);
		return JSON.toJSONString(result);
	}
	
	@Override
	public String detailAcctBalanceQ(String json, String ACCT_BALANCE_ID) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("ACCT_BALANCE_ID", ACCT_BALANCE_ID);		
		Map<String, Object> result = new HashMap<>();
		//1:查询账本变动日志表
		List<Map<String, Object>> data1= dao.query("qurAcctBalLogDeatil", param);		
		result.put("data1", data1);      
		logger.info("server reponse <==="+result);
//		return Tools.buildResponseData("0", "查询俱乐部会员详情成功", resClubMember);
		return JSON.toJSONString(result);
	}
	
	@Override
	public String acctItemGroupQ(String json, String ACCT_BALANCE_ID) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<>();
		result.put("data1",dao.query("QueryAcctItemType", null));//账目类型下拉列表
		result.put("data2",dao.query("QueryAcctItemSource", null));//账目类型下拉列表
		result.put("data3",dao.query("QueryAcctItemGroup", null));//账目类型下拉列表
		return JSON.toJSONString(result);
	}

	@Override
	public String acctItemListQ(String json) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
    	data=dao.query("QueryAcctItemList", jsonObj);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

	@Override
	public String acctItemA(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
		//获取业务类型
		String  ACCT_ITEM_GROUP_MEM_ID=dao.getPK("ACCT_ITEM_GROUP_MEMBER");
//        String  pointTypeGroupId=dao.getPK("POINT_TYPE_GROUP");
        jsonObj.put("ACCT_ITEM_GROUP_MEM_ID", ACCT_ITEM_GROUP_MEM_ID);
//        jsonObj.put("pointTypeGroupId", pointTypeGroupId);
    	dao.insert("INSERT_ACCT_ITEM_GROUP_MEM", jsonObj);
    	return "success";
	}

	@Override
	public String billItemQ(String json) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
    	data=dao.query("QueryAcctItemList11", jsonObj);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

	@Override
	public String billItemQ2(String json) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
    	data=dao.query("QueryAcctItemList22", jsonObj);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

	@Override
	public String depositQ(String json) throws Exception {
		Map<String,Object> result=new HashMap<>();
		result.put("data",dao.query("QueryDepositType", null));//账目类型下拉列表
		return JSON.toJSONString(result);
	}

	@Override
	public String depositListQ(String json) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
    	data=dao.query("querydepositListQ", jsonObj);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

	@Override
	public String depositListDetailQ(String json) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
    	data=dao.query("querydepositListDetailQ", jsonObj);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

	

}
