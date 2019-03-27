package com.tydic.pntstar.filter;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import com.tydic.pntstar.util.StringCommon;

public class AuthRestResFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		String requestId=requestContext.getHeaderString("X-CTG-Request-Id");
		if(StringCommon.isNull(requestId)) {
			requestId=UUID.randomUUID().toString();
		}
		if(responseContext.getHeaders().containsKey("Content-Type")) {
			responseContext.getHeaders().remove("Content-Type");
		}
		responseContext.getHeaders().add("Content-Type", "application/json;charset=utf-8");
		responseContext.getHeaders().add("Location", requestContext.getUriInfo().getRequestUri().getPath());
		responseContext.getHeaders().add("Cache-Control", "private");
		responseContext.getHeaders().add("X-CTG-Request-Id", requestId);
		responseContext.getHeaders().add("X-RateLimit-Limit", "100");
		responseContext.getHeaders().add("X-RateLimit-Remaining", "99");
		responseContext.getHeaders().add("X-RateLimit-Reset", "1");
		
		
		
	}

	

}
