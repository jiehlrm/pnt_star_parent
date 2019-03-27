package com.tydic.pntstar.service.impl.openapi.tenantAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoPAY;
import com.tydic.pntstar.service.impl.openapi.PointCommonComponent;
import com.tydic.pntstar.service.openapi.tenantAccount.PayChannelService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("PayChannelServiceImpl")
public class PayChannelServiceImpl implements PayChannelService{
	
	/**
	 * 积分公共服务
	 */
	@Autowired
    private PointCommonComponent pointCommonComponent;
	
	/**
	 * 公共dao服务
	 */
	private CommonDBDaoPAY dao = (CommonDBDaoPAY) SpringBeanUtil.getBean("dbdaoPAY");
	
	/**
	 * 获取支付渠道列表信息
	 */
	@Override
	public String queryPayChannelList(String param) throws Exception {
		return query(param, "QUERY_PAY_CHANNEL", "QUERY_PAY_CHANNEL_COUNT");
	}
	
	/**
	 * 
	 */
	@Override
	public String addPayChannelInfo(String json) throws Exception {
		Map<String,Object> jsonObj = JSONObject.parseObject(json);
		dao.insert("INSERT_PAY_CHANNEL_PAY_METHOD", jsonObj);
		dao.insert("INSERT_PAY_CHANNEL_INTERFACE_LIST", jsonObj);
		dao.insert("INSERT_PAY_CHANNEL_INTEF_ATTR", jsonObj);
		return "success";
	}
	
	private String query(String json, String sql1, String sql2) {
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		String count="";//查询总数
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
		count=dao.queryForOne(sql2, jsonObj).get("count").toString();
        data=dao.query(sql1, jsonObj);
        result.put("count", count);
        result.put("data", data);
        return JSON.toJSONString(result);
    }
	
}
