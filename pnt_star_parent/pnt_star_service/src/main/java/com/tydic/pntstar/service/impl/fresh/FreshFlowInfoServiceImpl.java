package com.tydic.pntstar.service.impl.fresh;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.frame.Monitor;
import com.tydic.pntstar.service.fresh.FreshFlowInfoService;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("freshFlowInfoServiceImpl")
public class FreshFlowInfoServiceImpl implements FreshFlowInfoService {

	@Override
	public String fresh() {
		Monitor monitor = (Monitor) SpringBeanUtil.getBean("monitor");
		JSONObject jsonObject = new JSONObject();
		try {
			monitor.handFreshFlowInfo();
			jsonObject.put("resultCode", "0");
			jsonObject.put("resultMsg", "fresh success");
		} catch (Exception e) {
			jsonObject.put("resultCode", "0");
			jsonObject.put("resultMsg", e.getMessage());
			e.printStackTrace();
		}
		return JSONObject.toJSONString(jsonObject);
		
	}

}
