package com.tydic.pntstar.service.demo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

@Path("")
public interface TestService {
	@Path("/sayHello/{jsonStr}/{str}")
	@PATCH
	public void sayHello(@PathParam("jsonStr") String jsonStr,@PathParam("str") String str);
	@Path("/sayWhat")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void sayWhat(String JsonStr);
	
	
	@Path("/testSql")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String testsql(String JsonStr);
	
	
	@Path("/testService")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String testService(String JsonStr);
	
	@Path("/refresh")
	@GET
	public String refresh();
}
