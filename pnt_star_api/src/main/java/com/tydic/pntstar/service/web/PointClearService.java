package com.tydic.pntstar.service.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** 
* @Title: PointClearService.java 
* @Package com.tydic.pntstar.service.web 
* @Description:积分清零记录服务
* @author weixsa@gmail.com 
* @date 2019年1月5日 下午2:07:55 
* @version V1.0 
*/
@Path("")
public interface PointClearService {
	
	 //查询积分清零记录信息
	 @Path("/queryPointClearList")
	 @GET
	 String queryPointClear(String json) throws Exception;
	 
	 
	 //查询积分清零记录信息
	 @Path("/exportPointClearList")
	 @GET
	 String exportPointClear(String json,String table_title,String realPath) throws Exception;
	
}
