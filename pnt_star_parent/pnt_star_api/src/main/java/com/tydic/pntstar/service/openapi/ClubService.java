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
 * 集团俱乐部API
 */
@Path("")
public interface ClubService {
	
	/**
	 * 新增俱乐部
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/club")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addClub(String json) throws Exception;
	
	/**
	 * 修改俱乐部信息
	 * @param json
	 * @param clubId
	 * @return
	 * @throws Exception
	 */
	@Path("/club/{clubId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyClub(String json,@PathParam("clubId") String clubId) throws Exception;
	
	/**
	 * 删除俱乐部
	 * @param json
	 * @param clubId
	 * @return
	 * @throws Exception
	 */
	@Path("/club/{clubId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delClub(String json,@PathParam("clubId") String clubId) throws Exception;

	/**
	 * 查询俱乐部详情
	 * @param json
	 * @param clubId
	 * @return
	 * @throws Exception
	 */
	@Path("/club/{clubId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClub(String json,@PathParam("clubId") String clubId) throws Exception;
	
	/**
	 * 查询俱乐部列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/club")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubList(String json) throws Exception;
}
