package com.tydic.lbs.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.service.RestFulClient;
import com.tydic.lbs.util.ServerConfig;
@Service("testUserServlet")
public class TestUserServlet  extends BaseService {
	  public void test(HttpServletRequest request,
              HttpServletResponse response, Map<String, Object> rtnMap) {
		  
		 
		 //这里是测试直接调用，正常的应用请调用invokeRestful方法
		 ServerConfig sf=null;
		try {
			sf = new ServerConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 //"http://192.168.128.42:30881/pntstar/user/say"
		 String url=sf.getValue("TestUserRESTFUL");
		 String method="POST";
		 JSONObject param=new JSONObject();
		 param.put("userName", "张三");
		 param.put("age", 18);
		 String jsonResult=RestFulClient.invoke(url, param, method);
		 rtnMap.put("data", jsonResult);
	  }
}
