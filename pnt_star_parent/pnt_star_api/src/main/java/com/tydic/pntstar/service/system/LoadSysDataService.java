package com.tydic.pntstar.service.system;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("loadSysData")
public interface LoadSysDataService {
	/**
	 * 获取表头
	 * @param param
	 * @return
	 */
	@Path("/loadTableHead")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String loadTableHead(String param);
	
	@Path("/loadFlowInfo")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String loadFlowInfo(String param);
	
	@Path("/loadCombox")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String loadCombox(String param);
}
