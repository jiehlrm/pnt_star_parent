package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

@Path("")
public interface PayServiceAccountService {
	/**
	 * 查询余额共享规则类型
	 * @param json
	 * @param BaSRTId
	 * @return
	 * @throws Exception
	 */
	@Path("/BaSRTList")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPayServiceShareRuleType(String param) throws Exception;
	
	@Path("/BaSRTList/{BaSRTId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPayServiceShareRuleTypeById(String param,@PathParam("BaSRTId") String BaSRTId) throws Exception;
	
	/**
	 * 新增余额共享规则类型
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/BaSRT")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addBaSRT(String param) throws Exception;
	
	/**
	 * 修改余额共享规则类型
	 * @param json
	 * @param BaSRTId
	 * @return
	 * @throws Exception
	 */
	@Path("/BaSRT/{BaSRTId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPayServiceShareRuleType(String param,@PathParam("BaSRTId") String BaSRTId) throws Exception;
	
	/**
	 * 删除余额共享规则类型
	 * @param json
	 * @param BaSRTId
	 * @return
	 * @throws Exception
	 */
	@Path("/BaSRT/{BaSRTId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPayServiceShareRuleType(String param,@PathParam("BaSRTId") String BaSRTId) throws Exception;
	
	
	
//-------------------------------------分割  产品实例与账务信息------------------------------	
	/**
	 * 查询产品实例与账务信息
	 * @param json
	 * @param PIARId
	 * @return
	 * @throws Exception
	 */
	@Path("/PIARList")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPayServiceProdInstAcctRel(String param) throws Exception;
	
	@Path("/PIARList/{PIARId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPayServiceProdInstAcctRelById(String param,@PathParam("PIARId") String PIARId) throws Exception;
	
	
	
	
	/**
	 * 新增产品实例与账务信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/PIAR")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPayServiceProdInstAcctRel(String param) throws Exception;
	
	/**
	 * 修改产品实例与账务信息
	 * @param json
	 * @param PIARId
	 * @return
	 * @throws Exception
	 */
	@Path("/PIAR/{PIARId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPayServiceProdInstAcctRel(String param,@PathParam("PIARId") String PIARId) throws Exception;
	
	/**
	 * 删除产品实例与账务信息
	 * @param json
	 * @param PIARId
	 * @return
	 * @throws Exception
	 */
	@Path("/PIAR/{PIARId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPayServiceProdInstAcctRel(String param,@PathParam("PIARId") String PIARId) throws Exception;
	
	
	//---------------------------------分割 违约金减免记录------------------------------		
		
		@Path("/LFA")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeAdjust(String param) throws Exception;
		
	
	    /**
		 * 查询违约金减免记录明细
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFA/{LFAId}")
		@GET
      //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeAdjustInfo(String param,@PathParam("LFAId") String LFAId) throws Exception;
				
		/**
		 * 新增违约金减免记录明细
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFA")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceLaterFeeAdjust(String param) throws Exception;
		
		/**
		 * 修改违约金减免记录明细
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFA/{LFAId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceLaterFeeAdjust(String param,@PathParam("LFAId") String LFAId) throws Exception;
		
		/**
		 * 删除违约金减免记录明细
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFA/{LFAId}")
		@DELETE
//		@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceLaterFeeAdjust(String param,@PathParam("LFAId") String LFAId) throws Exception;
		

//---------------------------------分割 违约金减免规则------------------------------		

		@Path("/LFCR")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeComputeRule(String param) throws Exception;
		
	
	    /**
		 * 查询违约金减免规则
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFCR/{LFCRId}")
		@GET
      //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeComputeRuleInfo(String param,@PathParam("LFCRId") String LFCRId) throws Exception;
				
		/**
		 * 新增违约金减免规则
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFCR")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceLaterFeeComputeRule(String param) throws Exception;
		
		/**
		 * 修改违约金减免规则
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFCR/{LFCRId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceLaterFeeComputeRule(String param,@PathParam("LFCRId") String LFCRId) throws Exception;
		
		/**
		 * 删除违约金减免规则
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFCR/{LFCRId}")
		@DELETE
//				@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceLaterFeeComputeRule(String param,@PathParam("LFCRId") String LFCRId) throws Exception;



	//---------------------------------分割 违约金计费比例配置-----------------------------		
	
       /**
		 * 查询违约金计费比例配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFR")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeRate(String param) throws Exception;
		
	
	    /**
		 * 查询违约金计费比例配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFR/{LFRId}")
		@GET
      //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceLaterFeeRateInfo(String param,@PathParam("LFRId") String LFRId) throws Exception;
				
		/**
		 * 新增违约金计费比例配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/LFR")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceLaterFeeRate(String param) throws Exception;
		
		/**
		 * 修改违约金计费比例配置
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFR/{LFRId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceLaterFeeRate(String param,@PathParam("LFRId") String LFRId) throws Exception;
		
		/**
		 * 删除违约金计费比例配置
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/LFR/{LFRId}")
		@DELETE
//		@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceLaterFeeRate(String param,@PathParam("LFRId") String LFRId) throws Exception;

	
	//---------------------------------分割 银行分行-----------------------------		
	
       /**
		 * 查询银行分行
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BankBranch")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBankBranch(String param) throws Exception;
		
	
	    /**
		 * 查询银行分行
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BankBranch/{BBId}")
		@GET
      //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBankBranch(String param,@PathParam("BBId") String BBId) throws Exception;
				
		/**
		 * 新增银行分行
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BankBranch")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceBankBranch(String param) throws Exception;
		
		/**
		 * 修改银行分行
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/BankBranch/{LFRId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceBankBranch(String param,@PathParam("BBId") String BBId) throws Exception;
		
		/**
		 * 删除银行分行
		 * @param json
		 * @param PIARId
		 * @return
		 * @throws Exception
		 */
		@Path("/BankBranch/{LFRId}")
		@DELETE
//			@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceBankBranch(String param,@PathParam("BBId") String BBId) throws Exception;
								
		
		//---------------------------------分割 一次费计算结果-----------------------------		
		 /**
		 * 一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIC")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceOneItemCalc(String param) throws Exception;
		
	
	    /**
		 * 查询一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIC/{OICId}")
		@GET
      //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceOneItemCalcById(String param,@PathParam("OICId") String OICId) throws Exception;
				
		/**
		 * 新增一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIC")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceOneItemCalc(String param) throws Exception;
		
		/**
		 * 修改一次费计算结果
		 * @param json
		 * @param OTCId
		 * @return
		 * @throws Exception
		 */
		@Path("/OIC/{OICId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceOneItemCalc(String param,@PathParam("OICId") String OICId) throws Exception;
		
		/**
		 * 删除一次费计算结果
		 * @param json
		 * @param OICId
		 * @return
		 * @throws Exception
		 */
		@Path("/OIC/{OICId}")
		@DELETE
//			@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceOneItemCalc(String param,@PathParam("OICId") String OICId) throws Exception;
									
		
		
		//---------------------------------分割 一次费记录-----------------------------		
		 /**
		 * 一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIR")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceOneItemResult(String param) throws Exception;
		
	
	    /**
		 * 查询一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIR/{OIRId}")
		@GET
     //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceOneItemResultById(String param,@PathParam("OIRId") String OIRId) throws Exception;
				
		/**
		 * 新增一次费计算结果
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/OIR")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceOneItemResult(String param) throws Exception;
		
		/**
		 * 修改一次费计算结果
		 * @param json
		 * @param OTCId
		 * @return
		 * @throws Exception
		 */
		@Path("/OIR/{OIRId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceOneItemResult(String param,@PathParam("OIRId") String OIRId) throws Exception;
		
		/**
		 * 删除一次费计算结果
		 * @param json
		 * @param OICId
		 * @return
		 * @throws Exception
		 */
		@Path("/OIR/{OIRId}")
		@DELETE
//			@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceOneItemResult(String param,@PathParam("OIRId") String OIRId) throws Exception;
									

		//---------------------------------分割 补收补退：一次费-----------------------------		
		 /**
		 *  补收补退：一次费
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIP")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceAcctItemPlusMinus(String param) throws Exception;
		
	
	    /**
		 * 查询 补收补退：一次费
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIP/{AIPId}")
		@GET
     //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceAcctItemPlusMinusById(String param,@PathParam("AIPId") String AIPId) throws Exception;
				
		/**
		 * 新增 补收补退：一次费
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIP")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceAcctItemPlusMinus(String param) throws Exception;
		
		/**
		 * 修改 补收补退：一次费
		 * @param json
		 * @param OTCId
		 * @return
		 * @throws Exception
		 */
		@Path("/AIP/{AIPId}")
	    @PATCH
		@Consumes({MediaType.APPLICATION_JSON})	
		public String modifyPayServiceAcctItemPlusMinus(String param,@PathParam("AIPId") String AIPId) throws Exception;
		
		/**
		 * 删除 补收补退：一次费
		 * @param json
		 * @param OICId
		 * @return
		 * @throws Exception
		 */
		@Path("/AIP/{AIPId}")
		@DELETE
//			@Consumes({MediaType.APPLICATION_JSON})	
		public String delPayServiceAcctItemPlusMinus(String param,@PathParam("AIPId") String AIPId) throws Exception;



		//---------------------------------分割 余额共享+余额共享类型-----------------------------		
		 /**
		 *  余额共享+余额共享类型
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSRAT")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceShareRuleAndType(String param) throws Exception;
		
	
	    /**
		 * 余额共享+余额共享类型
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSRAT/{BSRATId}")
		@GET
    //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceShareRuleAndType(String param,@PathParam("BSRATId") String BSRATId) throws Exception;
				
		/**
		 * 余额共享+余额共享类型
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSRAT")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceBalanceShareRuleAndType(String param) throws Exception;
		

		//---------------------------------调账单-----------------------------		
		 /**
		 *  查询调账单
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIA")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceAcctItemAdjust(String param) throws Exception;
		
	
	    /**
		 * 查询调账单
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIA/{AIAId}")
		@GET
   //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceAcctItemAdjustById(String param,@PathParam("AIAId") String AIAId) throws Exception;
				
		/**
		 *  新增调账单
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AIA")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceAcctItemAdjust(String param) throws Exception;

		//---------------------------------余额类型-----------------------------		
		 /**
		 *  查询余额类型
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BalanceType")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceType(String param) throws Exception;
		
	
	    /**
		 * 查询余额类型
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BalanceType/{BalanceTypeId}")
		@GET
  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceTypeById(String param,@PathParam("BalanceTypeId") String BalanceTypeId) throws Exception;
				
		//---------------------------------税目配置+税率配置-----------------------------		
		 /**
		 *  查询税目配置+税率配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/TaxAndRate")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceTaxItemAndRateConfig(String param) throws Exception;
		
	
	    /**
		 * 查询税目配置+税率配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/TaxAndRate/{TaxAndRateId}")
		@GET
  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceTaxItemAndRateConfigById(String param,@PathParam("TaxAndRateId") String AIAId) throws Exception;
				
		/**
		 *  新增税目配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/TaxItem")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceTaxItem(String param) throws Exception;
		
		/**
		 *  新增税率配置
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/TaxRate")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceTaxRateConfig(String param) throws Exception;

		
		
		//---------------------------------专款专用详情+转款专用详情描述-----------------------------		
		 /**
		 *  查询专款专用详情+转款专用详情描述
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSP")
		@GET
	  //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceSpecialRule(String param) throws Exception;
		
	
	    /**
		 * 查询专款专用详情+转款专用详情描述
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSP/{BSPId}")
		@GET
        //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceBalanceSpecialRuleById(String param,@PathParam("BSPId") String BSPId) throws Exception;
				
		/**
		 *  新增专款专用详情
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSP")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceBalanceSpecialRule(String param) throws Exception;
		
		/**
		 *  新增转款专用详情描述
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/BSP")
		@POST
		@Consumes({MediaType.APPLICATION_JSON})	
		public String addPayServiceBalanceSpecialRuleDesc(String param) throws Exception;

		
		//---------------------------------账户=余额账本+付款记录+销账记录-----------------------------		
		
		 /**
		 * 查询专款专用详情+转款专用详情描述
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@Path("/AccountID/{AccountId}")
		@GET
        //@Consumes({MediaType.APPLICATION_JSON})	
		public String queryPayServiceAccountInfo(String param,@PathParam("AccountId") String AccountId) throws Exception;
		
		
		
		//--------账务-------------
		@Path("/iasAcctItemQ")
		@GET
//		@Consumes({MediaType.APPLICATION_JSON})	
		public String iasAcctItemQt(String json) throws Exception;
		
		@Path("/iasAcctItemU")
		@GET
//		@Consumes({MediaType.APPLICATION_JSON})	
		public String iasAcctItemU(String json) throws Exception;
}
