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
public interface CreditService {
 
	/**
	 * 根据ID查询单个信用度
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("credit/{creditId}")
	@GET
	public String querySingleCredit(String json,@PathParam("creditId") String creditId) throws Exception;
	
	/**
	 * 新增信用度
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("credit")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addCredit(String json) throws Exception;
	
	/**
	 * 修改信用额度
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("credit/{creditId}")
	@PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyCredit(String json,@PathParam("creditId") String creditId) throws Exception;
	
	/**
	 * 删除信用额度
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("credit/{creditId}")
	@DELETE
	public String delCredit(String json,@PathParam("creditId") String creditId) throws Exception;

	/**
	 * 查询信用度列表
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("credit")
	@GET
	public String queryAllCredit(String json) throws Exception;

	/**
	 * 信用度历史查询 10000号人工业务
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("/queryHistory")
	@GET
	public String CheckServQuotaQuery(String json) throws Exception;
	

	/**
	 * 10000号根据业务号码查询实时信用额度
	 * @param json
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@Path("/queryNow")
	@GET
	public String queryCrdByServNum(String json) throws Exception;
	
	/**
	 * 测试存储过程
	 * @param 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/test01")
	@GET
	public String test01(String json) throws Exception;
}
