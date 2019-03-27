package com.tydic.pntstar.service.system;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/***
 * 给内部系统前台web调用
 * @author Administrator
 *
 */
@Path("commonService")
public interface CommonService {
	
	/***
	 * 通用方法,调用后台流程编排
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/invokeFlow")
	public String invokeFlow(String jsonStr);
}
