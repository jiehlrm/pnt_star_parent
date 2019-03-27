package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("pointDonate")
public interface PointDonateService {

	/**
     * 积分转赠
     * 
     * @param json
     * @return
     * @throws Exception
     */
	@Path("/pointDonate")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public String pointDonate(String json) throws Exception;
}
