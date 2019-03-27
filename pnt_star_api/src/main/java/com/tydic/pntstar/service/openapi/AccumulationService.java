package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("accumulation")
public interface AccumulationService {
	@Path("{s}")
	@POST
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	public String accumulation(String json,@QueryParam("str") String str,@PathParam("s") String s);
}
