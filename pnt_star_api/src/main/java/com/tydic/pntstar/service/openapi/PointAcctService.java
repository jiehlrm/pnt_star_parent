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
 *  积分账户集团API
 */

@Path("")
public interface PointAcctService {

	/**
	 * 根据积分帐户id主键查询积分账户信息
	 * @param json
	 * @param pointAcctId
	 * @return
	 * @throws Exception
	 */
	@Path("pointAcct/{pointAcctId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointAcct(String json,@PathParam("pointAcctId") String pointAcctId) throws Exception;
	
	/**
	 * 新增积分帐户
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("pointAcct/pointAcct")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointAcct(String json) throws Exception;
	
	/**
	 * 修改积分帐户
	 * @param json
	 * @param pointAcctId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcct/{pointAcctId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointAcct(String json,@PathParam("pointAcctId") String pointAcctId) throws Exception;
	
	/**
	 * 删除积分帐户
	 * @param json
	 * @param pointAcctId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcct/{pointAcctId}")
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointAcct(String json,@PathParam("pointAcctId") String pointAcctId) throws Exception;

	/**
	 * 查询积分帐户列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointAcct")
	@GET
	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointAcctList(String json) throws Exception;
	
}
