package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("")
public interface IasPayService {
	

	@Path("/isaAcctBalance")//查询余额账目数据
	@GET
	public String isaAcctBalance(String json) throws Exception;
	
	@Path("/isaAcctBalancePay")//充钱销账操作
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String isaAcctBalancePay(String json) throws Exception;

}
