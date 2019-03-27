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
 *  积分兑换记录API
	根据积分兑换记录id主键查询
	新增积分兑换记录
	修改积分兑换记录
	删除积分兑换记录
	查询积分兑换记录列表
 */

@Path("")
public interface PointExchRecordService {

	/**
	 * 根据积分兑换记录id主键查询
	 * @param json
	 * @param pointExchRecordId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchRecord/{pointExchRecordId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchRecord(String json,@PathParam("pointExchRecordId") String pointExchRecordId) throws Exception;
	
	/**
	 * 新增积分兑换记录
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchRecord")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointExchRecord(String json) throws Exception;
	
	/**
	 * 修改积分兑换记录
	 * @param json
	 * @param pointExchRecordId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchRecord/{pointExchRecordId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointExchRecord(String json,@PathParam("pointExchRecordId") String pointExchRecordId) throws Exception;
	
	/**
	 * 删除积分兑换记录
	 * @param json
	 * @param pointExchRecordId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchRecord/{pointExchRecordId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointExchRecord(String json,@PathParam("pointExchRecordId") String pointExchRecordId) throws Exception;

	/**
	 * 查询积分兑换记录列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchRecord")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchRecordList(String json) throws Exception;
	
}
