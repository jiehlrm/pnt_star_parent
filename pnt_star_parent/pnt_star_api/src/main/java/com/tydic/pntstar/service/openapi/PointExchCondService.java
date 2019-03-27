/**
 * 
 */
package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;



/**
 * @author root
 *  积分兑换条件API
	根据积分兑换条件id主键查询
	新增积分兑换条件
	修改积分兑换条件
	删除积分兑换条件
	查询积分兑换条件列表
 */

@Path("")
public interface PointExchCondService {

	/**
	 * 根据积分兑换条件id主键查询
	 * @param json
	 * @param pointExchCondId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchCond/{pointExchCondId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchCond(String json,@PathParam("pointExchCondId") String pointExchCondId) throws Exception;
	
	/**
	 * 新增积分兑换条件
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchCond")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointExchCond(String json) throws Exception;
	
	/**
	 * 修改积分兑换条件
	 * @param json
	 * @param pointExchCondId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchCond/{pointExchCondId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointExchCond(String json,@PathParam("pointExchCondId") String pointExchCondId) throws Exception;
	
	/**
	 * 删除积分兑换条件
	 * @param json
	 * @param pointExchCondId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchCond/{pointExchCondId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointExchCond(String json,@PathParam("pointExchCondId") String pointExchCondId) throws Exception;

	/**
	 * 查询积分兑换条件列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchCond")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchCondList(String json) throws Exception;
	
}
