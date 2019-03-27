package com.tydic.pntstar.service.province;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/** 
* @Title: MemberServAcctService.java 
* @Package com.tydic.pntstar.service.province 
* @Description: 该类是省内服务接口
* @author weixsa@gmail.com 
* @date 2019年1月4日 上午9:02:25 
* @version V1.0 
*/
@Path("")
public interface MemberServAcctService {
	
	/**
	   * 扣减俱乐部会员权益
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/serviceMinus")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String serviceMinus(String json) throws Exception;

	
	/**
	 *    服务返销  : 权益服务扣减返销
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/serviceBack")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String serviceBack(String json) throws Exception;
}
