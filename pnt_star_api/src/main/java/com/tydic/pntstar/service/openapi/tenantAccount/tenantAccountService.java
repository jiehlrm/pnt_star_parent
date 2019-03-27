package com.tydic.pntstar.service.openapi.tenantAccount;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/*
 */
@Path("")
public interface tenantAccountService {

	/*
	 * 商户帐号信息API
	 */
	@Path("/tenantAccountQ")
	@GET
	public String querytenantAccountList(String json) throws Exception;

	@Path("/tenantAccountA")
	@POST
	public String addtenantAccount(String json) throws Exception;
	
	@Path("/payChannelQ")
	@GET
	public String querypayChannelList(String json) throws Exception;
	
	@Path("/signTypeQ")
	@GET
	public String querysignTypeList(String json) throws Exception;
	
	
	/*
	 * 支付界面API
	 */
	@Path("/payUserQ")
	@GET
	public String querypayUserList(String json) throws Exception;
	
	@Path("/payMethodQ")
	@GET
	public String querypayMethodList(String json) throws Exception;
	
	@Path("/tradeTypeQ")
	@GET
	public String querytradeTypeList(String json) throws Exception;

	@Path("/payTypeQ")
	@GET
	public String querypayTypeList(String json) throws Exception;
	
	@Path("/paySerialQ")
	@GET
	public String querypaySerialList(String json) throws Exception;
	
	@Path("/pay")
	@POST
	public String pay(String json) throws Exception;
	
}
