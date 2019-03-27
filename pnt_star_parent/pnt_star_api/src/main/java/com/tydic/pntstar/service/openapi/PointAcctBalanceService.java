package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

/*
 * 集团积分账本API
 */
@Path("")
public interface PointAcctBalanceService {

	/**
	 * 根据积分账本id主键查询
	 * @param json
	 * @param pointAcctBalanceId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcctBalance/{pointAcctBalanceId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointAcctBalance(String json,@PathParam("pointAcctBalanceId") String pointAcctBalanceId) throws Exception;
	
	/**
	 * 新增积分帐本
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcctBalance")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointAcctBalance(String json) throws Exception;
	
	/**
	 * 修改积分帐本
	 * @param json
	 * @param pointAcctBalanceId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcctBalance/{pointAcctBalanceId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointAcctBalance(String json,@PathParam("pointAcctBalanceId") String pointAcctBalanceId) throws Exception;
	
	/**
	 * 删除积分帐本
	 * @param json
	 * @param pointAcctId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcctBalance/{pointAcctBalanceId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointAcctBalance(String json,@PathParam("pointAcctBalanceId") String pointAcctBalanceId) throws Exception;

	/**
	 * 查询积分帐户帐本
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcctBalance")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointAcctBalanceList(String json) throws Exception;
	
}
