package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

@Path("")
public interface TemporaryCreditLimitService {
 
	/**
	 * 根据ID查询单个信用度 temporaryCreditLimitId
	 */
	@Path("temporaryCreditLimit/{temporaryCreditLimitId}")
	@GET
	public String queryTemporaryCreditLimit(String json,@PathParam("temporaryCreditLimitId") String temporaryCreditLimitId) throws Exception;
	
	/**
	 * 新增信用度
	 */
	@Path("temporaryCreditLimit")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addTemporaryCreditLimit(String json) throws Exception;
	
}
	

