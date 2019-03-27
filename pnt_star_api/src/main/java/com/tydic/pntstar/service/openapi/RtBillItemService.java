package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("RtBillItem")
public interface RtBillItemService {
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String query(String json) throws Exception;
	
}
