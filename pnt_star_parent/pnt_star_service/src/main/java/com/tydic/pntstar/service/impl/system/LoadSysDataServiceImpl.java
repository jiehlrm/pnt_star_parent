package com.tydic.pntstar.service.impl.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.system.LoadSysDataService;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("loadSysDataServiceImpl")
public class LoadSysDataServiceImpl implements LoadSysDataService {
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Override
	public String loadTableHead(String param) {
		Map<String, Object> params = JSONObject.parseObject(param);
		String service_name = params.get("service_name")+"";
		List<Map<String, Object>> headList = dao.query(service_name, params);
		return JSONObject.toJSONString(headList, true);
	}
	@Override
	public String loadFlowInfo(String param) {
		String result = "";
		if(param != null && !"".equals(param.trim())){
			Map<String, Object> params = JSONObject.parseObject(param);
			Map<String, Object> flowInfo = dao.queryForOne("FLOW_QUERY_INFO", params);
			if(flowInfo == null || flowInfo.size() <= 0){
				result = "";
			}else{
				result = JSONObject.toJSONString(flowInfo, true);
			}
		}else{
			List<Map<String, Object>> flowInfoList = dao.query("FLOW_QUERY_INFO", new HashMap<>());
			if(flowInfoList == null || flowInfoList.size() <= 0){
				result = "";
			}else{
				result = JSONObject.toJSONString(flowInfoList, true);
			}
		}
		return result;
	}

	@Override
	public String loadCombox(String param) {
		Map<String, Object> params = JSONObject.parseObject(param);
		List<Map<String, Object>> headList = dao.query(params.get("service_name").toString(), params);
		JSONObject result=new JSONObject();
		result.put("success", "success");
		result.put("data", headList);
		return JSONObject.toJSONString(result);
	}
}
