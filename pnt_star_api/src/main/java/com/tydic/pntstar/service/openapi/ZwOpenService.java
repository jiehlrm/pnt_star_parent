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
public interface ZwOpenService {
 
//	/**
//	 * 销售品类型定义查询：BILL_OFFER_TYPE
//	 */
//	@Path("iasBillOfferType/{billOfferTypeId}")
//	@GET
//	public String queryBillOfferType(String json,@PathParam("billOfferTypeId") String billOfferTypeId) throws Exception;
//	
//	/**
//	 * 新增 销售品类型定义
//	 */
//	@Path("iasBillOfferType")
//	@POST
//	@Consumes({MediaType.APPLICATION_JSON})	
//	public String addBillOfferType(String json) throws Exception;
	
	
	/**
	 * 销售品类型销售品关系查询：  RECORD_ID
	 */
	@Path("iasBillOfferTypeOfferRel/{recordId}")
	@GET
	public String queryBillOfferTypeOfferRel(String json,@PathParam("recordId") String recordId) throws Exception;
	
	/**
	 * 新增销售品类型销售品关系
	 */
	@Path("iasBillOfferTypeOfferRel")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addBillOfferTypeOfferRel(String json) throws Exception;
	
	/**
	 * 销售品帐单项关系查询：BILL_ITEM_OFFER_REL_ID  BILL_ITEM_OFFER_REL_ID
	 */
	@Path("iasBillItemOfferRel/{billItemOfferRelId}")
	@GET
	public String queryBillItemOfferRel(String json,@PathParam("billItemOfferRelId") String billItemOfferRelId) throws Exception;
	
	/**
	 * 新增销售品帐单项关系
	 */
	@Path("iasBillItemOfferRel")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addBillItemOfferRel(String json) throws Exception;
	
}
	

