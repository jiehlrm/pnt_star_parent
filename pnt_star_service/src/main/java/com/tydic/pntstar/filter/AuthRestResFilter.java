package com.tydic.pntstar.filter;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.util.StringUtils;

import com.tydic.pntstar.util.StringCommon;

public class AuthRestResFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		String requestId=requestContext.getHeaderString("X-CTG-Request-Id");
		if(StringCommon.isNull(requestId)) {
			requestId=UUID.randomUUID().toString();
		}
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		//设置默认头
		if(StringUtils.isEmpty(headers.getFirst("Content-Type")) ){
			headers.putSingle("Content-Type", "application/json; charset=utf-8");
		}
		 headers.add("Access-Control-Allow-Origin", "*"); // If you want to be more restrictive it could be localhost:4200
		    headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS"); // You can add HEAD, DELETE, TRACE, PATCH
		    headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Accept-Language"); // You can add many more

		    if (requestContext.getMethod().equals("OPTIONS"))
		        responseContext.setStatus(200);
//		if(responseContext.getHeaders().containsKey("Content-Type")) {
//			responseContext.getHeaders().remove("Content-Type");
//		}
//		responseContext.getHeaders().add("Content-Type", "application/json;charset=utf-8");
//		responseContext.getHeaders().add("Location", requestContext.getUriInfo().getRequestUri().getPath());
//		responseContext.getHeaders().add("Cache-Control", "private");
//		responseContext.getHeaders().add("X-CTG-Request-Id", requestId);
//		responseContext.getHeaders().add("X-RateLimit-Limit", "100");
//		responseContext.getHeaders().add("X-RateLimit-Remaining", "99");
//		responseContext.getHeaders().add("X-RateLimit-Reset", "1");
		
	
//		String origin = requestContext.getHeaderString("Origin");
//		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
//		responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//		responseContext.getHeaders().add("Access-Control-Max-Age", "360000");
//		responseContext.getHeaders().add("Access-Control-Allow-Headers", "x-requested-with,Authorization");
//		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		 

		
		
		
	}

	

}
