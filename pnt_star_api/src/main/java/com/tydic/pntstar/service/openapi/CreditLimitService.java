package com.tydic.pntstar.service.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.tydic.jtcrm.openapi.annontion.PATCH;

@Path("")
public interface CreditLimitService {

    /**
     * 根据信用额度id主键查询
     * 
     * @param json
     * @param creditLimitId
     * @return
     * @throws Exception
     */
    @Path("creditLimit/{creditLimitId}")
    @GET
    public String queryCreditLimit(String json, @PathParam("creditLimitId") Integer creditLimitId) throws Exception;

    /**
     * 新增信用额度
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @Path("creditLimit")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public String addCreditLimit(String json) throws Exception;

    /**
     * 修改信用额度
     * 
     * @param json
     * @param creditLimitId
     * @return
     * @throws Exception
     */
    @Path("creditLimit/{creditLimitId}")
    @PATCH
    @Consumes({ MediaType.APPLICATION_JSON })
    public String modifyCreditLimit(String json, @PathParam("creditLimitId") Integer creditLimitId) throws Exception;

    /**
     *删除信用额度（根据实际id）
     * 
     * @param json
     * @param creditLimitId
     * @return
     * @throws Exception
     */
    @Path("creditLimit/{creditLimitId}")
    @DELETE
    public String deleteCreditLimit(String json, @PathParam("creditLimitId") Integer creditLimitId) throws Exception;

    /**
     * 查询列表
     * 
     * @param json
     * @param creditLimitId
     * @param objType
     * @param objId
     * @return
     * @throws Exception
     */
    @Path("creditLimit")
    @GET
    public String listCreditLimit(String json);

}
