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
 * 支付中心后台接口
 */
@Path("")
public interface PayDemoService {

	@Path("/payNodeCheckTotal")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String payQuery(String json) throws Exception;
	
	
}
