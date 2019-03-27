package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoPAY;
import com.tydic.pntstar.service.openapi.PayDemoService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;


@Component("payDemoServiceImpl")
@Service
public class PayDemoServiceImpl implements PayDemoService {
	
	private static final Logger logger = LoggerFactory.getLogger(PayDemoServiceImpl.class);
	private CommonDBDaoPAY dao = (CommonDBDaoPAY) SpringBeanUtil.getBean("dbdaoPAY");
	
	@Override
	public String payQuery(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
//		String num="";//查询总数
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
//    	num=dao.queryForOne("QUERY_POINT_TYPE_COUNT", jsonObj).get("total").toString();
    	data=dao.query("QUERY_PAY_NODE_CHECK_TOTAL", jsonObj);
//        result.put("total", num);
        result.put("data", data);
        return JSON.toJSONString(result);
	}

}
