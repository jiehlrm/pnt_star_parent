package com.tydic.lbs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	public LoginFilter() {
		super();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("P3P","CP=CAO PSA OUR");
		HttpSession session = req.getSession(true);
		String requestUrl = req.getRequestURL().toString();
		String path = req.getRequestURI();
		//TODO记录访问日志
		logger.debug("requestUrl===>"+requestUrl);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig conf) throws ServletException {

	}

	public void destroy() {
	}

	
}