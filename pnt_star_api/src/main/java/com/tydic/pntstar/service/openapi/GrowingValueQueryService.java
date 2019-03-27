package com.tydic.pntstar.service.openapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * 成长值查询抽象类
 * 
 * @author zhouman
 *
 */
@Path("")
public interface GrowingValueQueryService {
	/**
	 * 获取客户成长值信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Path("/growingValue")
	@GET
	public String getGrowingValue(String jsonString) throws Exception;

}
