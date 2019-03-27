package com.tydic.pntstar.service.openapi;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

/**
 * 积分调整抽象类
 * 
 * @author zhouman
 *
 */
@Path("")
public interface PointAdjustService {
	/**
	 * 获取积分余额信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Path("/getPointBalance")
	@GET
	public String getPointBalance(String jsonString) throws Exception;
	@Path("/doPointAdjust")
	@PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public Map<String, Object> doPointAdjust(String jsonString) throws Exception;

}
