package com.tydic.pntstar.service.province;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/** 
* @Title: ProvinceService.java 
* @Package com.tydic.pntstar.service.province 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2018年12月10日 下午3:58:25 
* @version V1.0 
*/

/*
省内接口API
查询积分兑换资格
查询兑换资源
可用积分查询   
按年查询积分接口
待清零积分查询
积分支出明细
积分兑换明细
积分维护(积分返销)
积分兑换接口
扣减俱乐部会员权益
服务返销
服务消费记录查询
查询俱乐部会员详情
星级查询(CRM)
 */
@Path("")
public interface ProvinceService {
	
	/**
	   * 查询积分兑换资格
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryPntExchgQuals")
	@GET
	public String queryPntExchgQuals(String json) throws Exception;
	
	
	/**
	   * 查询兑换资源
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryRedeemRes")
	@GET
	public String queryRedeemRes(String json) throws Exception;

	/**
	   * 可用积分查询   
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/getPointAcct")
	@GET
	public String getPointAcct(String json) throws Exception;

	/**
	   * 按年查询积分接口   
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryYearCn")
	@GET
	public String queryYearCn(String json) throws Exception;
	
	/**
	   * 待清零积分查询 
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryIntegralClear")
	@GET
	public String queryIntegralClear(String json) throws Exception;
	
	/**
	   * 积分兑换接口
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/exchangePoint")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String exchangePoint(String json) throws Exception;
	

	/**
	   * 积分返销接口
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/rebackPoint")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String rebackPoint(String json) throws Exception;
	
	/**
	   * 积分兑换明细
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryPointExchDetail")
	@GET
	public String queryPointExchDetail(String json) throws Exception;
	
	/**
	 *    积分支出明细
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/queryPointPayoutDetail")
	@GET
	public String queryPointPayoutDetail(String json) throws Exception;

//	

	/**
	   * 星级查询(CRM)
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/qryStarInfoForCrm")
	@GET
	public String qryStarInfoForCrm(String json) throws Exception;
	
	
	
	
	
	
	/**
	   *    服务消费记录查询
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/qryServiceConsumeInfo")
	@POST
	public String qryServiceConsumeInfo(String json) throws Exception;
}
