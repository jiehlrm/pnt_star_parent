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
 * 积分计算创建API
 */
@Path("")
public interface PointTariffService {
	
	/**
	 * 新增积分计算
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/pointTariff")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointTariff(String json) throws Exception;
	
	
	/**
	 * 删除积分计算
	 * @param json
	 * @param pointTariffId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointTariff/{pointTariffId}")
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON})	
	public String delpointTariff(String json,@PathParam("pointTariffId") String pointTariffId) throws Exception;

	/**
	 * 查询积分计算详情
	 * @param json
	 * @param pointTariffId
	 * @return
	 * @throws Exception
	 */
	@Path("/pointTariff/{pointTariffId}")
	@GET
	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointTariff(String json,@PathParam("pointTariffId") String pointTariffId) throws Exception;
	
	/**
	 * 查询积分计算列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/pointTariff")
	@GET
	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointTariffList(String json) throws Exception;
}
