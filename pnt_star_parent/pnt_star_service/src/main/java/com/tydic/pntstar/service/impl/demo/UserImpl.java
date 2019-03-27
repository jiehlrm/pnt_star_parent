package com.tydic.pntstar.service.impl.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tydic.pntstar.service.demo.User;

@org.springframework.stereotype.Service("userImp")
public class UserImpl implements User {
	private static Logger logger = LoggerFactory.getLogger(User.class);

	public String say(String jsonStr) {
		logger.info("server receive ===>"+jsonStr);
		//TODO业务处理
		
		logger.info("server reponse <==="+jsonStr);
 		return jsonStr;
	}

}
