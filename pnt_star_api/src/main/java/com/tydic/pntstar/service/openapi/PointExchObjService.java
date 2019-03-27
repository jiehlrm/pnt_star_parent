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
 *  积分兑换对象API
	根据积分兑换对象id主键查询
	新增积分兑换对象
	修改积分兑换对象
	删除积分兑换对象
	查询积分兑换对象列表
 */

@Path("")
public interface PointExchObjService {

	/**
	 * 根据积分兑换对象id主键查询
	 * @param json
	 * @param pointExchObjId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchObj/{pointExchObjId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchObj(String json,@PathParam("pointExchObjId") String pointExchObjId) throws Exception;
	
	/**
	 * 新增积分兑换对象
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchObj")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointExchObj(String json) throws Exception;
	
	/**
	 * 修改积分兑换对象
	 * @param json
	 * @param pointExchObjId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchObj/{pointExchObjId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointExchObj(String json,@PathParam("pointExchObjId") String pointExchObjId) throws Exception;
	
	/**
	 * 删除积分兑换对象
	 * @param json
	 * @param pointExchObjId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchObj/{pointExchObjId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointExchObj(String json,@PathParam("pointExchObjId") String pointExchObjId) throws Exception;

	/**
	 * 查询积分兑换对象列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchObj")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchObjList(String json) throws Exception;
	
}
