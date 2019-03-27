package com.tydic.pntstar.service.impl.system;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.service.flow.FlowService;
import com.tydic.pntstar.service.system.CommonService;
import com.tydic.pntstar.util.DateUtil;
import com.tydic.pntstar.util.LogUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
@Service("commonServiceImpl")
public class CommonServiceImpl implements CommonService {
    private Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Override
	public String invokeFlow(String jsonStr) {
		FlowService flowService = (FlowService) SpringBeanUtil.getBean("flowService");
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		JSONObject TcpCont = jsonObj.getJSONObject("TcpCont");
		JSONObject SvcCont = jsonObj.getJSONObject("SvcCont");

		String traceId = TcpCont.getString("traceId");//端到端调用标识
		String spanId = TcpCont.getString("spanId");//调用端标识
		String parentSpanId = TcpCont.getString("parentSpanId");//父调用端标识
		String sampledFlag = TcpCont.getString("sampledFlag");//采样标志
		String debugFlag = TcpCont.getString("debugFlag");//Debug标志
		String APIID = TcpCont.getString("flowCode");//流程编码
		String reqTime = TcpCont.getString("reqTime");//端到端调用标识
		String senceId = "-1";
		Map<String, Object> SvcContmap=null;
		try {
			SvcContmap = flowService.evRun(traceId, APIID, senceId, SvcCont);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtil.format(traceId, "invoke flow["+APIID+"] error!"),e);
		}
		logger.info(LogUtil.format(traceId, "server receive ===>"+jsonStr));
		//响应结果
		//step1 拼接响应头TcpCont信息
//		"TcpCont":{
//	        "traceId":"20171130093547692851",
//					"flowCode":"RtBillItem",       
//	        "reqTime":"20171130093547"
//        "rspTime":""
//    },
		Map TcpContResp=new HashMap();
		TcpContResp.put("traceId", traceId);
		TcpContResp.put("flowCode", APIID);
		TcpContResp.put("reqTime", reqTime);
		TcpContResp.put("rspTime", DateUtil.getNowTimeMs());
		//step2 拼接响应头SvcCont信息
		Map jsonResult=new HashMap<>();
		jsonResult.put("TcpCont", TcpContResp);
		jsonResult.put("SvcCont", SvcContmap);
		String jsonResp= JSONObject.toJSONString(jsonResult);
		logger.info(LogUtil.format(traceId, "server reponse <==="+jsonResp));
		return jsonResp;
	}

}
