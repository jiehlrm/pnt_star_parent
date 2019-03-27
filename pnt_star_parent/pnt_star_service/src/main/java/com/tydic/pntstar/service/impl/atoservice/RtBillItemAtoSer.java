package com.tydic.pntstar.service.impl.atoservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.flow.ServiceAbstractInterface;
import com.tydic.pntstar.util.SpringBeanUtil;

@Service("RtBillItemAtoSer")
public class RtBillItemAtoSer implements ServiceAbstractInterface {
	private static final Logger logger = LoggerFactory.getLogger(RtBillItemAtoSer.class);
	
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	
	@Override
	public boolean chkNeedHandle(Map<String, Object> atoServiceParam,Map<String, Object> params,Map<String, Object> shareStore) {
		
		return true;
	}

	@Override
	public Map<String, Object> businessProcess(Map<String, Object> atoServiceParam,Map<String, Object> inputParams,Map<String, Object> shareStore) throws Exception {
		logger.debug("原子服务入参："+inputParams);
		List<Map<String, Object>> result = dao.query("QUERY_TEST", atoServiceParam);
		logger.debug(result.toString());
		Map<String, Object> returnJson=new HashMap<String, Object>();
		returnJson.put("resultCode", "0");
		returnJson.put("resultMsg", "操作成功");
		returnJson.put("qryType", "");
		List<JSONObject> rows=new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			JSONObject curRow=new JSONObject();
			curRow.put("chargeNo","chargeNo"+(i+1));
			curRow.put("callType","callType"+(i+1));
			curRow.put("callingType","callingType"+(i+1));
			curRow.put("callingArea","callingArea"+(i+1));
			curRow.put("calledNo","calledNo"+(i+1));
			curRow.put("beginTime","beginTime"+(i+1));
			
			curRow.put("duration", (i+1));
			curRow.put("basicCharge",(i+1));
			curRow.put("longCharge",(i+1));
			curRow.put("totalCharge",(i+1));
			rows.add(curRow);
		}
		returnJson.put("voiceBillItems", rows);
		returnJson.put("page", 1);
		returnJson.put("row", 10);
		returnJson.put("totalRecord", 20);
		return returnJson;
	}

}
