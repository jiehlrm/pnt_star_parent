package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoZW;
import com.tydic.pntstar.service.openapi.PayServiceAccountService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("payServiceAccountServiceImpl")

public class PayServiceAccountServiceImpl implements PayServiceAccountService {

	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceAccountServiceImpl.class);

	private CommonDBDaoZW dao = (CommonDBDaoZW) SpringBeanUtil.getBean("dbdaoZW");
	
	/*
	 * 查询余额共享规则类型列表
	 * */
	@Override
	public String queryPayServiceShareRuleType(String param) throws Exception {
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额共享规则类型列表成功", dao.query("PayServiceQryBalanceShareRuleType", null));
	}
	
	/*
	 * 查询某个余额共享规则类型信息
	 * */
	@Override	
	public String queryPayServiceShareRuleTypeById(String param, String BaSRTId) throws Exception {
		Map<String,Object> bARSTParam=new HashMap<>();
		bARSTParam.put("shareRuleTypeID", BaSRTId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额共享规则类型信息成功", dao.query("PayServiceQryBalanceShareRuleTypeById", bARSTParam));
	}
	
	/*
	 * 新增余额共享规则类型
	 * */
	@Override
	public String addBaSRT(String param) throws Exception {
		StringBuilder result=new StringBuilder();  //返回操作结果
		
		//将json字符串转化为json格式		
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddBalanceShareRuleTypeData:"+param);
		//获取参数
		String balanceShareRuleTypeName=json.getString("shareRuleTypeName");
		String balanceShareRuleTypeStatusCD=json.getString("statusCD");
		String balanceShareRuleTypeCreateStaff=json.getString("createStaff");
		
		//判断数据是否有效
		if(balanceShareRuleTypeName==null||balanceShareRuleTypeName.isEmpty())
		{
			result.append("Result: Please enter useful information about shareRuleTypeName!");
			logger.info("AddBalanceShareRuleTypeData "+result.toString());
			return  result.toString();
		}
		if(balanceShareRuleTypeStatusCD==null||balanceShareRuleTypeStatusCD.isEmpty())
		{
			result.append("Result: Please enter useful information about statusCD!");
			logger.info("AddBalanceShareRuleTypeData "+result.toString());
			return  result.toString();
		}
		if(balanceShareRuleTypeCreateStaff==null||balanceShareRuleTypeCreateStaff.isEmpty())
		{
			result.append("Result: Please enter useful information about createStaff!");
			logger.info("AddBalanceShareRuleTypeData "+result.toString());
			return  result.toString();
		}
		
        //传递过来的数据检查无误
		
		//查询是否已存在该类型名称
		Map<String,Object> bARSTParam=new HashMap<>();
		bARSTParam.put("shareRuleTypeName", balanceShareRuleTypeName);	
		String tooldate=Tools.buildResponseData(Tools.SUCCESS,null,dao.query("PayServiceQryBalanceShareRuleTypeByName",bARSTParam));		
		JSONObject searchResult = JSONObject.parseObject(tooldate);
		com.alibaba.fastjson.JSONArray array= searchResult.getJSONArray("dataList");
		logger.info("----------Receive AddBalanceShareRuleTypeData array---- :"+array);
 		
		
		if(array.isEmpty())//不存在可插入
		{
			//查询序列号
			Map<String,Object> qryParam=new HashMap<>();
			qryParam.put("tableName","balance_share_rule_type" );
			logger.info("参数"+qryParam.toString());;
			String qryResult=Tools.buildResponseData(Tools.SUCCESS,null,dao.query("PayServiceQryCount",qryParam));				
			JSONObject qryOResult = JSONObject.parseObject(qryResult);
			com.alibaba.fastjson.JSONArray qryArray= qryOResult.getJSONArray("dataList");
			int count=qryArray.getJSONObject(0).getInteger("count");
			logger.info("返回的序列数:"+count);
			bARSTParam.put("shareRuleTypeID", count);
			bARSTParam.put("statusCD", balanceShareRuleTypeStatusCD);
			bARSTParam.put("createStaff", balanceShareRuleTypeCreateStaff);
			try {
			    dao.insert("PayServiceInsBalanceShareRuleType",bARSTParam);
				result.append("Result: Insert Successed!");
			}catch (Exception e) {
				result.append("Result: Insert failed! please try again later!");
			}		
		}
		else 
		{
			result.append("Result: This BalanceShareRuleTypeName is exist ,Please try enter another BalanceShareRuleTypeName again");	    
		}
		logger.info("AddBalanceShareRuleTypeData "+result.toString());
		return result.toString();
	}

	

	
	/*
	 * 修改余额共享规则类型信息
	 * */
	@Override
	public String modifyPayServiceShareRuleType(String param, String BaSRTId) throws Exception {
		
		return null;
	}

	
	/*
	 * 删除余额共享规则类型信息
	 * */
	@Override
	public String delPayServiceShareRuleType(String param, String BaSRTId) throws Exception {
		
		return null;
	}

	
	//----------------------产品实例与账户关系信息+关系属性表+账户表
	/*
	 * 查询产品实例与账户关系信息
	 * */
	@Override
	public String queryPayServiceProdInstAcctRel(String param) throws Exception {		
		return Tools.buildResponseData(Tools.SUCCESS, "查询产品实例与账务信息成功", dao.query("PayServiceQryProdInstAcctRel", null));		
	}
	
	/*
	 * 通过ID查询产品实例与账户关系信息
	 * */
	@Override
	public String queryPayServiceProdInstAcctRelById(String param, String PIARId) throws Exception {		
		Map<String,Object> bARSTParam=new HashMap<>();
		bARSTParam.put("shareRuleTypeID", PIARId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询产品实例与账务信息成功", dao.query("PayServiceQryProdInstAcctRelById", bARSTParam));
	}
	/*
	 * 添加产品实例与账户关系信息
	 * */

	@Override
	public String addPayServiceProdInstAcctRel(String param) throws Exception {
		
		//将json字符串转化为json格式		
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceProdInstAcctRelData:"+param);	
		//获取产品实例与账户关系主键
		String PAIRId=dao.getPK("prod_inst_acct_rel");
		//获取产品实例与账户关系参数,并添加主键
		Map<String,Object> pIARParam=json.getJSONObject("PIAR");	
		pIARParam.put("prodInstAcctRelId", PAIRId);
		try {
			dao.insert("PayServiceAddProdInstAcctRel", pIARParam);
			logger.info("新增产品实例与账户关系数据");
		}catch (Exception e) {
			//发生错误删除已添加数据
			Map<String,Object>deleteParam=new HashMap<String,Object>();
			deleteParam.put("prodInstAcctRelId",PAIRId);
			dao.delete("PayServiceDelProdInstAcctRel",deleteParam);
			return "Result: PIAR  insert failed! please try again later!\n";
		}
		//获取产品实例与账户关系附属关系参数
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> pIARAList=(List<Map<String,Object>>) json.get("PIARA");
		List<String> PIARAPKList=new ArrayList<>();
		if(!Tools.isNull(pIARAList)){
			for(Map<String,Object> pIARAInfo:pIARAList){
				String PK=dao.getPK("prod_inst_acct_rel_attr");
				PIARAPKList.add(PK);
				pIARAInfo.put("prodInstAcctRelAttrId", PK);
				pIARAInfo.put("prodInstAcctRelId",PAIRId);				
				logger.info("新增产品实例与账户关系附加属性参数："+pIARAInfo);
				try {
					dao.insert("PayServiceAddProdInstAcctRelAttr", pIARAInfo);
				} catch (Exception e) {
					Map<String,Object>deletePIARParam=new HashMap<String,Object>();
					deletePIARParam.put("prodInstAcctRelId",PAIRId);
					dao.delete("PayServiceDelProdInstAcctRel",deletePIARParam);
					for(int i=0;i<PIARAPKList.size();i++)
					{
						Map<String,Object>deletePIARAParam=new HashMap<String,Object>();
						deletePIARAParam.put("prodInstAcctRelAttrId",PIARAPKList.get(i));
						dao.delete("PayServiceDelProdInstAcctRelAttr",deletePIARAParam);
						return "Result: PIARA  insert failed! please try again later!";
					}
					logger.info("新增产品实例与账户关系附加属性参数出现错误：数据回滚!");
				}
			}			
		}		
       return "Result:Insert Succeed!";
	}
	/*
	 * 修改产品实例与账户关系信息
	 * */

	@Override
	public String modifyPayServiceProdInstAcctRel(String param, String PIARId) throws Exception {
		
		return null;
	}
	/*
	 * 删除产品实例与账户关系信息
	 * */

	@Override
	public String delPayServiceProdInstAcctRel(String param, String PIARId) throws Exception {
		
		return null;
	}

	
	
	//----------------------------------分割线   违约金减免记录明细-------------------------------------
	
	@Override
	public String queryPayServiceLaterFeeAdjust(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金减免记录明细成功", dao.query("PayServiceQryLateFeeAdjust", null));
	}
	
	@Override
	public String queryPayServiceLaterFeeAdjustInfo(String param, String LFAId) throws Exception {
		Map<String,Object> bARSTParam=new HashMap<>();
		bARSTParam.put("lateFeeAdjustId", LFAId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金减免记录信息成功", dao.query("PayServiceQryLateFeeAdjustById", bARSTParam));
	}

	@Override
	public String addPayServiceLaterFeeAdjust(String param) throws Exception {
		
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceProdLateFeeAdjustData:"+param);	
		
		//获取产品实例与账户关系主键
		String lFAId=dao.getPK("late_fee_adjust");
		json.put("lateFeeAdjustId", lFAId);
		try {
			dao.insert("PayServiceAddLateFeeAdjust", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ lFAId+"}";
	}

	@Override
	public String modifyPayServiceLaterFeeAdjust(String param, String LFAId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceLaterFeeAdjust(String param, String LFAId) throws Exception {
		
		return null;
	}

	
	//----------------------------------分割线   违约金减免规则-------------------------------------
	
	@Override
	public String queryPayServiceLaterFeeComputeRule(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金减免规则成功", dao.query("PayServiceQryLateFeeComputeRule", null));
	}

	@Override
	public String queryPayServiceLaterFeeComputeRuleInfo(String param, String LFCRId) throws Exception {
		Map<String,Object> lFCRParam=new HashMap<>();
		lFCRParam.put("lateFeeRuleId", LFCRId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金减免规则明细成功", dao.query("PayServiceQryLateFeeComputeRuleById", lFCRParam));
	}

	@Override
	public String addPayServiceLaterFeeComputeRule(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceProdLateFeeComputeRuleData:"+param);	
		
		//获取产品实例与账户关系主键
		String lFAId=dao.getPK("late_fee_compute_rule");
		json.put("lateFeeRuleId", lFAId);
		try {
			dao.insert("PayServiceAddLateFeeComputeRule", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ lFAId+"}";
	}

	@Override
	public String modifyPayServiceLaterFeeComputeRule(String param, String LFCRId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceLaterFeeComputeRule(String param, String LFCRId) throws Exception {
		
		return null;
	}
	
	
	
	
	//----------------------------------分割线   违约金计费比例配置-------------------------------------
	
	
	
	@Override
	public String queryPayServiceLaterFeeRate(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金计费比例配置成功", dao.query("PayServiceQryLateFeeRate", null));
	}

	@Override
	public String queryPayServiceLaterFeeRateInfo(String param, String LFRId) throws Exception {
		Map<String,Object> lFRParam=new HashMap<>();
		lFRParam.put("lateFeeRateId", LFRId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询违约金计费比例配置成功", dao.query("PayServiceQryLateFeeRateById", lFRParam));
	}

	@Override
	public String addPayServiceLaterFeeRate(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceProdLateFeeRateData:"+param);	
		
		//获取产品实例与账户关系主键
		String lFRd=dao.getPK("late_fee_rate");
		json.put("lateFeeRateId", lFRd);
		try {
			dao.insert("PayServiceAddLateFeeRate", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ lFRd+"}";
	}

	@Override
	public String modifyPayServiceLaterFeeRate(String param, String LFRId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceLaterFeeRate(String param, String LFRId) throws Exception {
		
		return null;
	}

	
	//----------------------------------分割线   银行分行-------------------------------------
	@Override
	public String queryPayServiceBankBranch(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询银行分行成功", dao.query("PayServiceQryBankBranch", null));
	}

	@Override
	public String queryPayServiceBankBranch(String param, String BBId) throws Exception {
		Map<String,Object> BBParam=new HashMap<>();
		BBParam.put("bankBranchId", BBId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询银行分行成功", dao.query("PayServiceQryBankBranchById", BBParam));
	}

	@Override
	public String addPayServiceBankBranch(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceBankBranchData:"+param);	
		//获取银行分行信息
		String BBId=dao.getPK("bank_branch");
		json.put("bankBranchId", BBId);
		try {
			dao.insert("PayServiceAddBankBranch", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ BBId+"}";
	}

	@Override
	public String modifyPayServiceBankBranch(String param, String BBId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceBankBranch(String param, String BBId) throws Exception {
		
		return null;
	}
	
	//----------------------------------分割线   一次计费结果-------------------------------------
	@Override
	public String queryPayServiceOneItemCalc(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询一次计费结果成功", dao.query("PayServiceQryOneItemCalc", null));
	}

	@Override
	public String queryPayServiceOneItemCalcById(String param, String OICId) throws Exception {
		Map<String,Object> OTCParam=new HashMap<>();
		OTCParam.put("oneAcctItemId", OICId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询一次计费结果成功", dao.query("PayServiceQryOneItemCalcById", OTCParam));
	}

	@Override
	public String addPayServiceOneItemCalc(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceOneItemCalcData:"+param);	
		//获取
		String OTCId=dao.getPK("one_item_calc");
		json.put("oneAcctItemId",OTCId);
		try {
			dao.insert("PayServiceAddOneItemCalc", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ OTCId+"}";
	}

	@Override
	public String modifyPayServiceOneItemCalc(String param, String OTCId) throws Exception {
	
		return null;
	}

	@Override
	public String delPayServiceOneItemCalc(String param, String OTCId) throws Exception {
	
		return null;
	}

	//----------------------------------分割线   一次计费账目-------------------------------------
	
	@Override
	public String queryPayServiceOneItemResult(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询一次计费账目成功", dao.query("PayServiceQryOneItemResult", null));
	}

	@Override
	public String queryPayServiceOneItemResultById(String param, String OIRId) throws Exception {
	
		Map<String,Object> OIRParam=new HashMap<>();
		OIRParam.put("oneItemCalcId", OIRId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询一次计费账目成功", dao.query("PayServiceQryOneItemResultById", OIRParam));
	}

	
	@SuppressWarnings("unused")
	@Override
	public String addPayServiceOneItemResult(String param) throws Exception {
		//将json字符串转化为json格式		
				JSONObject json = JSONObject.parseObject(param);
				logger.info("Receive AddPayServiceOneItemResultData:"+param);	
				//获取产品实例与账户关系主键
				String OTRId=dao.getPK("one_item_result");
				//获取产品实例与账户关系参数,并添加主键
				Map<String,Object> oTRParam=json.getJSONObject("OIR");	
				oTRParam.put("oneAcctItemId", OTRId);
				try {
					dao.insert("PayServiceAddOneItemResult", oTRParam);
					logger.info("新增一次费账目");
				}catch (Exception e) {
					//发生错误删除已添加数据
					Map<String,Object>deleteParam=new HashMap<String,Object>();
					deleteParam.put("oneAcctItemId",OTRId);
					dao.delete("PayServiceDelOneItemResult",deleteParam);
					return "Result: OIR  insert failed! please try again later!\n";
				}
				//获取产品实例与账户关系附属关系参数
				logger.info("进入这里");
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> oIDLList=(List<Map<String,Object>>) json.get("OIDL");
				List<String> OIDLPKList=new ArrayList<>();
				if(!Tools.isNull(oIDLList)){
					for(Map<String,Object> oIDLInfo:oIDLList){
						String PK=dao.getPK("one_item_discount_log");
						OIDLPKList.add(PK);
						oIDLInfo.put("oneChargeDiscountId", PK);
						oIDLInfo.put("oneAcctItemId",OTRId);				
						logger.info("新增一次费记录："+oIDLInfo);
						try {
							dao.insert("PayServiceAddOneItemDiscountLog", oIDLInfo);
						} catch (Exception e) {
							Map<String,Object>deleteOIRParam=new HashMap<String,Object>();
							deleteOIRParam.put("oneAcctItemId",OTRId);
							dao.delete("PayServiceDelOneItemResult",deleteOIRParam);
							for(int i=0;i<OIDLPKList.size();i++)
							{
								Map<String,Object>deleteOIDLParam=new HashMap<String,Object>();
								deleteOIDLParam.put("oneChargeDiscountId",OIDLPKList.get(i));
								dao.delete("PayServiceDelOneItemDiscountLog", deleteOIDLParam);
								return "Result: OIDL  insert failed! please try again later!";
							}
							logger.info("新增一次费记录出现错误：数据回滚!");
						}
					}			
				}		
		       return "Result: Insert Succeed! The new id is {"+ OTRId+"}";
	}

	@Override
	public String modifyPayServiceOneItemResult(String param, String OIRId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceOneItemResult(String param, String OIRId) throws Exception {
		
		return null;
	}
	
	//----------------------------------分割线  补收补退：一次费-------------------------------------

	@Override
	public String queryPayServiceAcctItemPlusMinus(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询补收补退：一次费", dao.query("PayServiceQryAcctItemPlusMinus", null));
	}

	@Override
	public String queryPayServiceAcctItemPlusMinusById(String param, String AIPId) throws Exception {
		
		Map<String,Object> AIPParam=new HashMap<>();
		AIPParam.put("plusSeqNbr", AIPId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询补收补退：一次费", dao.query("PayServiceQryAcctItemPlusMinusById", AIPParam));
	}

	@Override
	public String addPayServiceAcctItemPlusMinus(String param) throws Exception {
		
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceAcctItemPlusMinus:"+param);	
		//获取
		String AIPId=dao.getPK("acct_item_plusminus");
		json.put("plusSeqNbr",AIPId);
		try {
			dao.insert("PayServiceAddAcctItemPlusMinus", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIPId+"}";
	}

	@Override
	public String modifyPayServiceAcctItemPlusMinus(String param, String ATPId) throws Exception {
		
		return null;
	}

	@Override
	public String delPayServiceAcctItemPlusMinus(String param, String OIRId) throws Exception {
		
		return null;
	}

	//----------------------------------分割线  余额共享+余额共享类型-------------------------------------
	
	@Override
	public String queryPayServiceBalanceShareRuleAndType(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额共享成功", dao.query("PayServiceQryBalanceShareRuleAndType", null));
	}

	@Override
	public String queryPayServiceBalanceShareRuleAndType(String param, String BSRATId) throws Exception {
		Map<String,Object> BSRATParam=new HashMap<>();
		BSRATParam.put("plusSeqNbr", BSRATId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额共享成功", dao.query("PayServiceQryPayServiceQryBalanceShareRuleAndTypeById",BSRATParam));
	}

	@Override
	public String addPayServiceBalanceShareRuleAndType(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceBalanceShareRuleAndType:"+param);	
		//获取
		String BSRATId=dao.getPK("balance_share_rule");
		json.put("shareRuleId",BSRATId);
		try {
			dao.insert("PayServiceAddBalanceShareRule", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again later!";
		}
		return "Result: Insert Succeed! The new id is {"+ BSRATId+"}";
	}

	//----------------------------------分割线  调账单-------------------------------------
	@Override
	public String queryPayServiceAcctItemAdjust(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询调账单成功", dao.query("PayServiceQryAcctItemAdjust", null));
	}

	@Override
	public String queryPayServiceAcctItemAdjustById(String param, String AIAId) throws Exception {
		Map<String,Object> AIAParam=new HashMap<>();
		AIAParam.put("adjustItemId", AIAId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询调账单成功", dao.query("PayServiceQryAcctItemAdjustById",AIAParam));
	}

	@Override
	public String addPayServiceAcctItemAdjust(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceAcctItemAdjust:"+param);	
		//获取
		String AIAId=dao.getPK("acct_item_adjust");
		json.put("adjustItemId",AIAId);
		try {
			dao.insert("PayServiceInsAcctItemAdjust", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again  later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIAId+"}";
	}

	//----------------------------------分割线  余额类型-------------------------------------
	@Override
	public String queryPayServiceBalanceType(String param) throws Exception {		
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额类型成功", dao.query("PayServiceQryBalanceType", null));
	}

	@Override
	public String queryPayServiceBalanceTypeById(String param, String BalanceTypeId) throws Exception {
		Map<String,Object> BalanceTypeParam=new HashMap<>();
		BalanceTypeParam.put("balanceTypeId", BalanceTypeId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询余额类型成功", dao.query("PayServiceQryBalanceTypeById",BalanceTypeParam));
	}

	//----------------------------------分割线  税率+税目-------------------------------------
	
	@Override
	public String queryPayServiceTaxItemAndRateConfig(String param) throws Exception {
	
		return Tools.buildResponseData(Tools.SUCCESS, "查询税率信息成功", dao.query("PayServiceQryTaxItemAndConfig", null));
	}

	@Override
	public String queryPayServiceTaxItemAndRateConfigById(String param, String TaxAndRateId) throws Exception {
		Map<String,Object> TaxAndRateParam=new HashMap<>();
		TaxAndRateParam.put("taxRateConfig", TaxAndRateId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询税率信息成功", dao.query("PayServiceQryTaxItemAndConfigById",TaxAndRateParam));
	}

	@Override
	public String addPayServiceTaxItem(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceTaxItem:"+param);	
		//获取
		String AIAId=dao.getPK("tax_item");
		json.put("taxItemId",AIAId);
		try {
			dao.insert("PayServiceInsTaxItem", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again  later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIAId+"}";
	}

	@Override
	public String addPayServiceTaxRateConfig(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceTaxRateConfig:"+param);	
		//获取
		String AIAId=dao.getPK("tax_rate_config");
		json.put("taxRateConfigId",AIAId);
		try {
			dao.insert("PayServiceInsTaxRateConfig", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again  later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIAId+"}";
	}

	//----------------------------------分割线 专款+专款详情-------------------------------------
	@Override
	public String queryPayServiceBalanceSpecialRule(String param) throws Exception {
		
		return Tools.buildResponseData(Tools.SUCCESS, "查询专款详情", dao.query("PayServiceQryBalanceSpecialPaymentAndDesc", null));
	}

	@Override
	public String queryPayServiceBalanceSpecialRuleById(String param, String BSPId) throws Exception {
		Map<String,Object> BSPIdParam=new HashMap<>();
		BSPIdParam.put("spePaymentDetId", BSPId);
		return Tools.buildResponseData(Tools.SUCCESS, "查询专款详情成功", dao.query("PayServiceQryBalanceSpecialPaymentAndDescById",BSPIdParam));
	}

	@Override
	public String addPayServiceBalanceSpecialRule(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceBalanceSpecialRuleDate:"+param);	
		//获取
		String AIAId=dao.getPK("balance_special_payment");
		json.put("spePaymentDetId",AIAId);
		try {
			dao.insert("PayServiceInsBalanceSpecialPayment", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again  later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIAId+"}";
	}

	@Override
	public String addPayServiceBalanceSpecialRuleDesc(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		logger.info("Receive AddPayServiceBalanceSpecialRuleDesc:"+param);	
		//获取
		String AIAId=dao.getPK("balance_special_payment_desc");
		json.put("spePaymentId",AIAId);
		try {
			dao.insert("PayServiceInsBalanceSpecialPaymentDesc", json);
		} catch (Exception e) {
			logger.info(e.toString());
			return "Result: Insert failed ! Please try  again  later!";
		}
		return "Result: Insert Succeed! The new id is {"+ AIAId+"}";
	}

	@Override
	public String queryPayServiceAccountInfo(String param, String AccountId) throws Exception {
		logger.info("receive data!"+param +"参数  "+AccountId);
		
		Map<String,Object> AccountParam=new HashMap<>();
		AccountParam.put("AccountDetId", AccountId);
		
		Map<String,Object> mapper=new HashMap<>();
		List<Map<String,Object>> AcctBalanceData=dao.query("PayServiceQryAcctBalance", AccountParam);
		
		List<Map<String,Object>> BillData=dao.query("PayServiceQryBill", AccountParam);
		
		List<Map<String,Object>> PaymentData=dao.query("PayServiceQryPayment", AccountParam);
		
		mapper.put("acctBalanceData", AcctBalanceData);
		mapper.put("billData", BillData);
		mapper.put("paymentData", PaymentData);
		logger.info("键值对"+mapper.toString());
		return   JSON.toJSONString(mapper);
	}

	@Override
	public String queryPayServiceInvoice(String param, String invoiceId) throws Exception {
		Map<String,Object> BSPIdParam=new HashMap<>();
		BSPIdParam.put("acctID", invoiceId);
		return JSON.toJSONString(dao.query("PayserviceQryAcctInvoice",BSPIdParam));
	}

	@Override
	public String queryPayServiceNorAndSpeInvoice(String param, String billItemQId) throws Exception {
       logger.info("receive data!"+param +"参数  "+billItemQId);
		
		Map<String,Object> InvoiceParam=new HashMap<>();
		InvoiceParam.put("invoiceId", billItemQId);
		
		Map<String,Object> mapper=new HashMap<>();
		
		List<Map<String,Object>> NorInvoiceData=dao.query("PayserviceQryNorInvoice", InvoiceParam);
		
		List<Map<String,Object>> SpeInvoiceData=dao.query("PayserviceQrySpeInvoice", InvoiceParam);
		
		
		
		mapper.put("NorInvoiceData", NorInvoiceData);
		mapper.put("SpeInvoiceData", SpeInvoiceData);
		logger.info("键值对"+mapper.toString());
		return   JSON.toJSONString(mapper);
	}
	
	
	


	
	
	@Override
	public String iasAcctItemU(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> param =new HashMap<>();//构建查询对象
		//1:更新记录
    	dao.update("iasAcctItemU", jsonObj);
    	//2:插入调账记录
    	param=dao.queryForOne("iasAcctItemQuery", jsonObj);
    	String adjust_item_id =dao.getPK("acct_item_adjust");
    	param.put("adjust_item_id", adjust_item_id);
    	param.put("ACCT_ITEM_ID", jsonObj.get("ACCT_ITEM_ID"));
    	param.put("ADJUST_TYPE", jsonObj.get("ADJUST_TYPE"));
    	param.put("CHARGE", jsonObj.get("CHARGE"));
    	param.put("ADJUST_DESC", jsonObj.get("ADJUST_DESC"));
    	dao.update("iasAcctItemUT", param);
       // result.put("data", data);
        return ("success");
	}
}
