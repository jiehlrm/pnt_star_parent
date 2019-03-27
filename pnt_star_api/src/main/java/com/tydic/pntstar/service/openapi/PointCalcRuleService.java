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
 * 积分计算规则API
 */
@Path("")
public interface PointCalcRuleService {
	
	/**
	 * 新增积分计算规则
	 */
	@Path("/pointCalcRule")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointCalcRule(String json) throws Exception;
	
	/**
	 * 查询积分计算规则详情
	 */
	@Path("/pointCalcRule/{pointCalcRuleId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointCalcRule(String json,@PathParam("pointCalcRuleId") String pointCalcRuleId) throws Exception;
	
	
	/**
	 * 新增积分计算
	 */
	@Path("/pointTariff")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointTariff(String json) throws Exception;
	
	/**
	 * 查询积分计算详情
	 */
	@Path("/pointTariff/{pointTariffId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointTariff(String json,@PathParam("pointTariffId") String pointTariffId) throws Exception;
	
	/**
	 * 新增积分事件类型 POINT_EVENT_TYPE 
	 */
	@Path("/pointEventType")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointEventType(String json) throws Exception;
	
	/**
	 * 查询积分计算规则详情
	 */
	@Path("/pointEventType/{pointEventTypeId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointEventType(String json,@PathParam("pointEventTypeId") String pointEventTypeId) throws Exception;
	
	/**
	 * 积分计算规则关系 POINT_CALC_RULE_REL 
	 */
	@Path("/pointCalcRuleRel")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointCalcRuleRel(String json) throws Exception;
	
	/**
	 * 积分计算规则关系
	 */
	@Path("/pointCalcRuleRel/{pointCalcRuleRelId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointCalcRuleRel(String json,@PathParam("pointCalcRuleRelId") String pointCalcRuleRelId) throws Exception;
	
	/**
	 * 积分计算判断条件  POINT_CONDITION 
	 */
	@Path("/pointCondition")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointCondition(String json) throws Exception;
	
	/**
	 * 积分计算判断条件
	 */
	@Path("/pointCondition/{pointConditionId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointCondition(String json,@PathParam("pointConditionId") String pointConditionId) throws Exception;
	
	
	/**
	 * 积分计算因子定义  POINT_CALC_FACTOR 
	 */
	@Path("/pointCalcFactor")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addpointCalcFactor(String json) throws Exception;
	
	/**
	 * 积分计算因子定义
	 */
	@Path("/pointCalcFactor/{pointCalcFactorId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String querypointCalcFactor(String json,@PathParam("pointCalcFactorId") String pointCalcFactorId) throws Exception;
	
	
	
}
