package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.service.flow.FlowService;
import com.tydic.pntstar.service.openapi.RtBillItemService;
import com.tydic.pntstar.util.SpringBeanUtil;
@Service("rtBillItemServiceImpl")
public class RtBillItemServiceImpl implements RtBillItemService {
    private Logger logger = LoggerFactory.getLogger(RtBillItemServiceImpl.class);

	@Override
	public String query(String json) throws Exception {
		FlowService flowService = (FlowService) SpringBeanUtil.getBean("flowService");
		HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
		String traceId = request.getHeader("traceId");//端到端调用标识
		String spanId = request.getHeader("spanId");//调用端标识
		String parentSpanId = request.getHeader("parentSpanId");//父调用端标识
		String sampledFlag = request.getHeader("sampledFlag");//采样标志
		String debugFlag = request.getHeader("debugFlag");//Debug标志
		JSONObject jsonObj = JSONObject.parseObject(json);
		String APIID = "RtBillItem";
		String senceId = "-1";
		Map<String, Object> map = flowService.evRun(traceId, APIID, senceId, jsonObj);
		logger.info("server receive ===>"+json);
		//TODO业务处理，这里是演示代码先写死
		logger.info("server reponse <==="+map);
		return JSONObject.toJSONString(map);
	}

}
