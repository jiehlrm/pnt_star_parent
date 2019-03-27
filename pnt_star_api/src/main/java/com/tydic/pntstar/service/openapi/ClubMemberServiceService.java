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
 * 集团俱乐部会员权益(服务)API
 */
@Path("")
public interface ClubMemberServiceService {

	/**
	 * 新增俱乐部会员权益
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})	
	public String addClubMemberService(String json) throws Exception;
	
	/**
	 * 修改俱乐部会员信息权益
	 * @param json
	 * @param memberServiceId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService/{memberServiceId}")
    @PATCH
	@Consumes({MediaType.APPLICATION_JSON})	
	public String modifyClubMemberService(String json,@PathParam("memberServiceId") String memberServiceId) throws Exception;
	
	/**
	 * 删除俱乐部会员权益
	 * @param json
	 * @param memberServiceId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService/{memberServiceId}")
	@DELETE
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String delClubMemberService(String json,@PathParam("memberServiceId") String memberServiceId) throws Exception;

	/**
	 * 查询俱乐部会员权益详情
	 * @param json
	 * @param memberServiceId
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService/{memberServiceId}")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberService(String json,@PathParam("memberServiceId") String memberServiceId) throws Exception;
	
	/**
	 * 查询俱乐部会员权益列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberServiceList(String json) throws Exception;
	/**
	 * 查询俱乐部会员权益列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberServiceListbyCustId(String json) throws Exception;
	/**
	 * 查询俱乐部会员权益消费记录列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService")
	@GET
//	@Consumes({MediaType.APPLICATION_JSON})	
	public String queryClubMemberServiceRecordList(String json) throws Exception;
	/**
	 * 查询俱乐部会员权益消费记录列表
	 * @param json
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@Path("/clubMemberService/{memServAcctId}")
	@PATCH
//	@Consumes({MediaType.APPLICATION_JSON})
	public String equityConsume(String json) throws Exception;
}
