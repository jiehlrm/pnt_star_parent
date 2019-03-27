package com.tydic.pntstar.service.impl.openapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointDonateService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("pointDonateServiceImpl")
public class PointDonateServiceImpl implements PointDonateService {
    private static final Logger logger = LoggerFactory.getLogger(PointDonateServiceImpl.class);
    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
    
    /**
     * 积分转赠
     * 
     * @param json
     * @return
     * @throws Exception
     */
	@Override
	@Transactional
	public String pointDonate(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonMap =JSONObject.parseObject(json);
		JSONObject reJson = new JSONObject();
		try {
			//1.判断入参是否合法
			if(Tools.isNull(jsonMap) 
					|| Tools.isNull(jsonMap.get("amount")) 
					|| Tools.isNull(jsonMap.get("aPointAcctBalanceId")) 
					|| Tools.isNull(jsonMap.get("zPointAcctBalanceId")) ){
				return Tools.buildResponseData("1", "积分转赠失败,入参异常", reJson);
			}
			Map<String, Object> param1 = new HashMap<String,Object>();
			Map<String, Object> param2 = new HashMap<String,Object>();
			if(!Tools.isNull(jsonMap.get("statusCd"))) {
				List<String> list = new ArrayList<String>();
				list.add("1000");
				list.add("1100");
				list.add("1200");
				if(!list.contains(jsonMap.get("statusCd"))) {
					return Tools.buildResponseData("1", "积分转赠失败,入参异常", reJson);
				}
				param1.put("STATUS_CD", jsonMap.get("statusCd"));
				param2.put("STATUS_CD", jsonMap.get("statusCd"));
			}
			//2.判断id对应的账本是否存在
			Map<String,Object> resultmap1=new HashMap<String,Object>();
			Map<String,Object> resultmap2=new HashMap<String,Object>();
			param1.put("POINT_ACCT_BALANCE_ID", jsonMap.get("aPointAcctBalanceId"));
			resultmap1 = dao.queryForOne("QUERY_POINT_BALANCE", param1);
			param2.put("POINT_ACCT_BALANCE_ID", jsonMap.get("zPointAcctBalanceId"));
			resultmap2 = dao.queryForOne("QUERY_POINT_BALANCE", param2);
			if(Tools.isNull(resultmap1) || Tools.isNull(resultmap2) ) {
				return Tools.buildResponseData("1", "积分转赠失败,账本不存在", reJson);
			}
			if(!resultmap1.get("STATUS_CD").equals("1000")||!resultmap2.get("STATUS_CD").equals("1000")) {
				return Tools.buildResponseData("1", "积分转赠失败,账本状态非正常", reJson);
			}
			
			//3.判断操作积分额是否小于等于转赠人余额积分
			if(Integer.parseInt((String) jsonMap.get("amount")) > Integer.parseInt(resultmap1.get("POINT_BALANCE").toString())) {
				return Tools.buildResponseData("1", "积分转赠失败,操作积分额入参异常", reJson);
			}
			//4.积分转赠
			param1.put("POINT_BALANCE", -Integer.parseInt((String) jsonMap.get("amount")));
			param1.put("POINT_ACCT_ID", resultmap1.get("POINT_ACCT_ID").toString());
			dao.update("UPDATE_POINT_BALANCE", param1);
			dao.update("UPDATE_POINT_ACCT", param1);
			param2.put("POINT_BALANCE", Integer.parseInt((String) jsonMap.get("amount")));
			param2.put("POINT_ACCT_ID", resultmap2.get("POINT_ACCT_ID").toString());
			dao.update("UPDATE_POINT_BALANCE", param2);
			dao.update("UPDATE_POINT_ACCT", param2);
			
			if(Tools.isNull(jsonMap.get("id"))) {
				reJson.put("id", getRandomNum());
			}else {
				reJson.put("id", jsonMap.get("id"));
			}
			reJson.put("href", "/pointDonate/"+jsonMap.get("aPointAcctBalanceId"));
			reJson.put("pointDonateId", jsonMap.get("aPointAcctBalanceId"));
			reJson.put("amount", jsonMap.get("amount"));
			reJson.put("aPointAcctBalanceId", jsonMap.get("aPointAcctBalanceId"));
			reJson.put("zPointAcctBalanceId", jsonMap.get("zPointAcctBalanceId"));
		} catch (Exception e) {
			Tools.buildResponseData("1", "积分转赠失败", null); 
		}
		logger.info("server reponse <==="+reJson);
		return Tools.buildResponseData("0", "积分转赠成功", reJson); 
	}

    
    /**
     * 生成随机码
     * @return
     */
    public static String getRandomNum() {
    	String str = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
    	int rannum = (int)((Math.random()*9+1)*1000);// 获取5位随机数
    	return str+rannum;
    }
}
