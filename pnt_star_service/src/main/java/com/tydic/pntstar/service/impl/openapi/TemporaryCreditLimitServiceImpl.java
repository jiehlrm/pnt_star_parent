package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.TemporaryCreditLimitService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service
@Component("temporaryCreditLimitServiceImpl")
public class TemporaryCreditLimitServiceImpl implements TemporaryCreditLimitService {
	
	private static final Logger logger = LoggerFactory.getLogger(TemporaryCreditLimitServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Override
	public String queryTemporaryCreditLimit(String json, String temporaryCreditLimitId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("temporaryCreditLimitId", temporaryCreditLimitId);
		Map<String, Object> result = dao.queryForOne("QueryTemporaryCreditLimit", param);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "暂未查到数据 !", null);
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询临时信用度成功", result);
	}

	@Override
	public String addTemporaryCreditLimit(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
		//获取业务类型
		String  temporaryCreditLimitId=dao.getPK("TEMPORARY_CREDIT_LIMIT");
        jsonObj.put("temporaryCreditLimitId", temporaryCreditLimitId);
    	dao.insert("InsertTemporaryCreditLimit", jsonObj);     
        return Tools.buildResponseData("0", "新增临时信用度成功", temporaryCreditLimitId);
	}

}
