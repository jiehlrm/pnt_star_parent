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
 *  积分兑换货品API
	根据积分兑换货品id主键查询
	新增积分兑换货品
	修改积分兑换货品
	删除积分兑换货品
	查询积分兑换货品列表
 */

@Path("")
public interface PointExchGoodsService {

	/**
	 * 根据积分兑换货品id主键查询
	 * @param json
	 * @param pointExchGoodsId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchGoods/{pointExchGoodsId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchGoods(String json,@PathParam("pointExchGoodsId") String pointExchGoodsId) throws Exception;
	
	/**
	 * 新增积分兑换货品
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchGoods")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPointExchGoods(String json) throws Exception;
	
	/**
	 * 修改积分兑换货品
	 * @param json
	 * @param pointExchGoodsId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchGoods/{pointExchGoodsId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyPointExchGoods(String json,@PathParam("pointExchGoodsId") String pointExchGoodsId) throws Exception;
	
	/**
	 * 删除积分兑换货品
	 * @param json
	 * @param pointExchGoodsId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchGoods/{pointExchGoodsId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delPointExchGoods(String json,@PathParam("pointExchGoodsId") String pointExchGoodsId) throws Exception;

	/**
	 * 查询积分兑换货品列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointExchGoods")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPointExchGoodsList(String json) throws Exception;
	
}
