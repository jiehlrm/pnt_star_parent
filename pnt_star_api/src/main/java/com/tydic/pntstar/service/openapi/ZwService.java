package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("")
public interface ZwService {
	
//	---------------------------------------发票---------------------------------
	
	@Path("/invoiceA")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String invoiceA(String json) throws Exception;
	
	@Path("/invoiceQ/{AccountId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String invoiceQ(String json,@PathParam("AccountId") String AccountId) throws Exception;	
	
	
	@Path("/detailAcctBalanceQ/{ACCT_BALANCE_ID}")//查询账本详细
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String detailAcctBalanceQ(String json,@PathParam("ACCT_BALANCE_ID") String ACCT_BALANCE_ID) throws Exception;
	
	@Path("/acctBalanceQ/{ACCT_BALANCE_ID}")//查询账本详细
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String acctBalanceQ(String json,@PathParam("ACCT_BALANCE_ID") String ACCT_BALANCE_ID) throws Exception;
	
	
	@Path("/acctItemGroupQ")//查询账本详细
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String acctItemGroupQ(String json,@PathParam("ACCT_BALANCE_ID") String ACCT_BALANCE_ID) throws Exception;
	
	
	@Path("/acctItemListQ")//查询账目组明细
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String acctItemListQ(String json) throws Exception;
	
	@Path("/acctItemA")//插入账目组数据
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String acctItemA(String json) throws Exception;
	
	
	@Path("/billItemQ")//插入账目组数据
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String billItemQ(String json) throws Exception;
	
	@Path("/billItemQ2")//插入账目组数据
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String billItemQ2(String json) throws Exception;
	
	@Path("/depositQ")//查询押金类型下拉列表
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String depositQ(String json) throws Exception;
	
	@Path("/depositListQ")//查询押金主表数据
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String depositListQ(String json) throws Exception;
	
	@Path("/depositListDetailQ")//查询押金明细数据
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String depositListDetailQ(String json) throws Exception;

}
