package com.tydic.pntstar.service.fresh;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 刷新流程信息（url:http://localhost:20881/pntstar/freshFlowInfo�?
 * @author lenovo
 *
 */
@Path("freshFlowInfo")
public interface FreshFlowInfoService {
	@GET
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String fresh ();
}
