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
 * 集团俱乐部会员API
 */
@Path("")
public interface ClubMemberService {

	/**
	 * 新增俱乐部会员
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMember")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addClubMember(String json) throws Exception;
	
	/**
	 * 修改俱乐部会员信息
	 * @param json
	 * @param clubMemberId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMember/{clubMemberId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyClubMember(String json,@PathParam("clubMemberId") String clubMemberId) throws Exception;
	
	/**
	 * 删除俱乐部会员
	 * @param json
	 * @param clubMemberId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMember/{clubMemberId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delClubMember(String json,@PathParam("clubMemberId") String clubMemberId) throws Exception;

	/**
	 * 查询俱乐部会员详情
	 * @param json
	 * @param clubMemberId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMember/{clubMemberId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMember(String json,@PathParam("clubMemberId") String clubMemberId) throws Exception;
	
	/**
	 * 查询俱乐部会员列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMember")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberList(String json) throws Exception;
	
	/**
	 * 查询俱乐部会员列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberQ")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberQ(String json) throws Exception;
}
