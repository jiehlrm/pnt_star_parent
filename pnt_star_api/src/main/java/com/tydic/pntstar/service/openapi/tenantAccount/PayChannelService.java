package com.tydic.pntstar.service.openapi.tenantAccount;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("")
public interface PayChannelService {
	
	@Path("/queryPayChannelList")
	@POST
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryPayChannelList(String json) throws Exception;
	
	@Path("/addPayChannelInfo")
	@POST
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String addPayChannelInfo(String json) throws Exception;
	
}
