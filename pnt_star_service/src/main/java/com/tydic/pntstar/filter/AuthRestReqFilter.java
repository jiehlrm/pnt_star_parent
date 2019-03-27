package com.tydic.pntstar.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.filter.AuthRestReqFilter;
import com.tydic.pntstar.entity.AppInfoEntity;
import com.tydic.pntstar.util.IPUtil;
import com.tydic.pntstar.util.MD5Util;
import com.tydic.pntstar.util.StringCommon;
import com.tydic.pntstar.util.SysdataLoad;



/****
 * 外部系统通过restful协议访问的时候
 * 1、head需要增加APP_ID应用标识（要求所有外部系统必须设置APP_ID）
 * 2、head需要增加APP_SECRET，md5对私钥加密后的16进制形式的，加密字符串
 * 3、head需要增加flowCode流程编码
 * 4、外部系统需要提供相应的IP地址段，计费账务系统会做IP地址校验
 * 
 * FAQ:
 * 1、如何外部系统不想设置APP_ID怎么办？
 *   把dubbo_provider.xml中的过滤器去掉
 * 2、如何外部系统不想设置APP_SECRET怎么办？
 *   配置的时候表里面把该系统设置为白名单
 * 3、如何外部系统不想设置flowCode怎么办？ 
 *   配置的时候表里面把该系统设置为白名单
 * @author Administrator
 *
 */
public class AuthRestReqFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest servletRequest;
	private static final Logger logger = LoggerFactory.getLogger(AuthRestReqFilter.class);
	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		//step1 获取访问客户端ip地址
//		String remoteAddr = IPUtil.getIp(servletRequest);
//		remoteAddr=InetAddress.getByName(remoteAddr).getHostAddress();
//		//step2 获取应用信息,判断IP段
//		String appId = getValueFromReq("APP_ID");
//		AppInfoEntity appInfo = SysdataLoad.getAppMap().get(appId);
//		if(appInfo == null){
//			JSONObject resultMap = new JSONObject();
//			resultMap.put("ResultCode", "-1");
//			resultMap.put("ResultMsg", "未获取到应用信息");
//			Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//			requestContext.abortWith(response);
//			return;
//		}
//		boolean isHave = IPUtil.ipIsValid(appInfo.getIp_start(), appInfo.getIp_end(), remoteAddr);
//		if(!isHave){
//			JSONObject resultMap = new JSONObject();
//			resultMap.put("ResultCode", "-1");
//			resultMap.put("ResultMsg", "客户端IP不属于应用IP段");
//			Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//			requestContext.abortWith(response);
//			return;
//		}
//		
//		//step3 判断秘钥
//		//获取是否加密
//		String ifCrypt = appInfo.getIf_crypt();
//		if(!StringCommon.isNull(ifCrypt) && "1".equals(ifCrypt)){
//			String reqSecret = getValueFromReq("APP_SECRET");
//			if(StringCommon.isNull(reqSecret) ){
//				JSONObject resultMap = new JSONObject();
//				resultMap.put("ResultCode", "-1");
//				resultMap.put("ResultMsg", "加密字符串无效");
//				Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//				requestContext.abortWith(response);
//				return;
//			}
//			//获取加密私钥
//			
//			String appSecret = appInfo.getApp_secret();
//			//根据规则加密
//			 Date date = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHH");
//			Calendar nowtime = new GregorianCalendar();
//			String CurrentTime1= sdf.format(nowtime.getTime());
//			nowtime.add(Calendar.HOUR, -1);
//			String CurrentTime2= sdf.format(nowtime.getTime());
//			nowtime.add(Calendar.HOUR, 2);
//			String CurrentTime3= sdf.format(nowtime.getTime());
//			String origin1=appSecret+CurrentTime1;
//			String origin2=appSecret+CurrentTime2;
//			String origin3=appSecret+CurrentTime3;
//		
//			String secretStr1 = MD5Util.MD5Encode(origin1);
//			String secretStr2 = MD5Util.MD5Encode(origin2);
//			String secretStr3 = MD5Util.MD5Encode(origin3);
//			//String secretStr = appSecret;
//			//获取请求报文中的加密字符串
//			if(!(reqSecret.equals(secretStr1)||reqSecret.equals(secretStr2)||reqSecret.equals(secretStr3))){
//				JSONObject resultMap = new JSONObject();
//				resultMap.put("ResultCode", "-1");
//				resultMap.put("ResultMsg", "秘钥无效");
//				Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//				requestContext.abortWith(response);
//				return;
//			}
//		
//		}
//		
//		//step4 判断APP是否是白名单
//		String isWhite = appInfo.getIs_white();
//		//如果应用不属于白名单，判断可访问的服务
//		if(StringCommon.isNull(isWhite) || "0".equals(isWhite)){
//			//step4 判断可访问的服务
//			//获取可访问服务集合
//			Set<String> servSet = SysdataLoad.getAppServRelMap().get(appId);
//			//获取请求服务
//			String serviceName = getValueFromReq("flowCode");
//			if(servSet != null && servSet.size() > 0){
//				if(!servSet.contains(serviceName)){
//					JSONObject resultMap = new JSONObject();
//					resultMap.put("ResultCode", "-1");
//					resultMap.put("ResultMsg", "该应用没有权限访问【"+serviceName+"】");
//					Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//					requestContext.abortWith(response);
//					return;
//				}
//			}else{
//				JSONObject resultMap = new JSONObject();
//				resultMap.put("ResultCode", "-1");
//				resultMap.put("ResultMsg", "该应用没有权限访问【"+serviceName+"】");
//				Response response = Response.ok(resultMap.toJSONString()).status(200).type(MediaType.APPLICATION_JSON).build();
//				requestContext.abortWith(response);
//				return;
//			}
//		}
		 
	}
	
	/**
	 * 获取请求头部信息
	 * @param key
	 * @return
	 */
	private String getValueFromReq(String key){
		String value = servletRequest.getHeader(key);
		if(StringCommon.isNull(value)){
			logger.debug("head doesn't exist value for "+key);
			return "";
		}
		return value;
	}

}