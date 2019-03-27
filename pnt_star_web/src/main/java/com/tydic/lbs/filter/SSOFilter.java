package com.tydic.lbs.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AuthVo;
import org.jasig.cas.client.util.BaseConstant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.bp.sso.client.filter.SSOAbstractFilter;
import com.tydic.lbs.entity.Empee;
import com.tydic.lbs.util.StringCommon;


/**
 * 用户登录过滤器
 *
 */
public class SSOFilter extends SSOAbstractFilter{
    /**
     * log4j对象
     */
    private static Logger log = Logger.getLogger(SSOFilter.class);

    @Override
    protected void initClient(FilterConfig filterConfig) {
    	System.out.println("=================initClint -------");
    }
    @Override
    protected void loadAuthInfo(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Map<String, Object> authInfo) {
        log.info("客户端登录成功 ---> "+authInfo);
    	//STEP1 设置当前员工信息
    	Empee empee = new Empee();
    	com.alibaba.fastjson.JSONObject userMap = (com.alibaba.fastjson.JSONObject) authInfo.get("userMap");
		empee.setEmpee_id(Long.parseLong(userMap.get("EMPEE_ID").toString()));
		empee.setEmpee_acct(userMap.get("EMPEE_ACCT").toString());
		empee.setEmpee_code(userMap.get("EMPEE_ACCT").toString());//EMPEE_CODE为空，因此直接使用EMPEE_ACCT来做
		empee.setEmpee_name(userMap.get("EMPEE_NAME").toString());
		empee.setEmpee_pwd(userMap.get("EMPEE_PWD").toString());
		empee.setEmpee_level(Long.parseLong(userMap.get("EMPEE_LEVEL").toString()));
		empee.setLatn_id(Long.parseLong(StringCommon.isNull(userMap.get("LATN_ID").toString())?"888":userMap.get("LATN_ID").toString()));
		empee.setIp_address(paramHttpServletRequest.getRemoteAddr());
		empee.setState("1");//判断是否已经失效
		HttpServletResponse res = (HttpServletResponse) paramHttpServletResponse;
		res.setHeader("P3P","CP=CAO PSA OUR");
		paramHttpServletResponse.setHeader("Access-Control-Allow-Origin","*");
		

		//STEP2 设置权限
		JSONArray jsonarray =  JSONArray.parseArray(authInfo.get("userPrivilege").toString());
		List privilegeList = new ArrayList();
	    for (int i = 0; i < jsonarray.size(); i++) {
	      JSONObject obj = jsonarray.getJSONObject(i);
	      privilegeList.add(obj.getString("PRIVILEGE_CODE"));
	    }
		empee.setEmpee_privilege(privilegeList);
		paramHttpServletRequest.getSession().setAttribute("empee", empee);
		paramHttpServletRequest.getSession().setAttribute("username",empee.getEmpee_acct());
		
		//新系统使用AuthVo设置用户信息
		AuthVo authVo = new AuthVo();
		authVo.setSystemUserId(empee.getEmpee_id().toString());
		authVo.setSystemUserCode(empee.getEmpee_acct());
		authVo.setStaffName(empee.getEmpee_name());
		authVo.setCertNubr(empee.getCert_nbr());
		authVo.setEmailAddr(empee.getEmpee_email_addr());
		authVo.setMobNo(empee.getEmpee_mob_no());
		authVo.setUkeyNum("");
		authVo.setIpAddress(empee.getIp_address());
		authVo.setPrivilegeList(privilegeList);//设置权限码
		paramHttpServletRequest.getSession().setAttribute(BaseConstant.SSO_CLIENT_AUTH_USER , authVo);
    }
}
