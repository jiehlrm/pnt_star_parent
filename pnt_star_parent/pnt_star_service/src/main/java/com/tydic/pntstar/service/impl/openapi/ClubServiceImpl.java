package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.ClubService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/*
 * 集团俱乐部API实现类
 */
@Service("clubServiceImpl")
public class ClubServiceImpl implements ClubService{
	private static final Logger logger = LoggerFactory.getLogger(PointAcctServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	
	/**
	 * 新增俱乐部
	 */
	@Override
	public String addClub(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj =JSONObject.parseObject(json);
		try {
			jsonObj.put("clubId", dao.getPK("CLUB"));
			Tools.addCommonParams(jsonObj);
			//新增俱乐部
			dao.insert("InsertClub", jsonObj);
			//------------------新增 客户俱乐部会员等级关联服务
			List<Map<String, Object>> clubMbrLevServiceRel = (List<Map<String, Object>>) jsonObj.get("clubMbrLevServiceRel");
			for(Map<String,Object> c:clubMbrLevServiceRel) {
				c.put("levServiceRelId", dao.getPK("CLUB_MBR_LEV_SERVICE_REL"));
				c.put("clubId", jsonObj.get("clubId"));
				Tools.addCommonParams(c);
				dao.insert("InsertClubMbrLevServiceRel", c);
			}
		} catch (Exception e) {
			return Tools.buildResponseData("1", "新增俱乐部失败", null); 
		}
		//返回报文信息
		Map<String, Object> result = dao.queryForOne("QueryClub", jsonObj);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "查询俱乐部详情失败", null);
		}
		List<Map<String, Object>> res = dao.query("QueryClubMbrLevServiceRel", jsonObj);
		result.put("clubMbrLevServiceRel", res);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "新增俱乐部成功", result); 
	}

	/**
	 * 修改俱乐部信息
	 */
	@Override
	public String modifyClub(String json, String clubId) throws Exception {
		logger.info("server receive ===>"+json);
		try {
			List<Map> reqMap = JSONObject.parseArray(json,Map.class);
			for(Map m:reqMap) {
				if("replace".equals(m.get("op"))) {
					m.put("updateStaff", "1111");//测试使用
					String path = m.get("path").toString();
					if(path.contains("clubMbrLevServiceRel")) {
						//-----------------------------修改客户俱乐部会员等级关联服务
						Map<String,Object> clubMbrLevServiceRel = (Map<String, Object>) m.get("value");
						String levServiceRelId = path.substring(path.lastIndexOf('/')+1, path.length());
						clubMbrLevServiceRel.put("levServiceRelId", levServiceRelId);
						dao.update("UpdateClubMbrLevServiceRel", clubMbrLevServiceRel);
					}else {
						m.put("clubId", clubId);
						dao.update("UpdateClub", m);
					}
				}
			}
		} catch (Exception e) {
			return Tools.buildResponseData("1", "修改俱乐部信息失败", null); 
		}
		//返回报文信息
		Map<String,Object> param =  new HashMap<>();
		param.put("clubId", clubId);
		Map<String, Object> result = dao.queryForOne("QueryClub", param);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "修改俱乐部信息失败", null);
		}
		List<Map<String, Object>> res = dao.query("QueryClubMbrLevServiceRel", param);
		result.put("clubMbrLevServiceRel", res);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "修改俱乐部信息成功", result);
	}

	/**
	 * 删除俱乐部
	 */
	@Override
	public String delClub(String json, String clubId) throws Exception {
		logger.info("server receive ===>"+clubId);
		Map<String, Object> param = new HashMap<>();
		param.put("clubId", clubId);
		//dao.delete("DeleteClub", param);//物理删除
		param.put("statusCd", "1100");
		param.put("updateStaff", "1241241");
		dao.update("DeleteClub", param);//逻辑删除-俱乐部状态改为1100无效
		return Tools.buildResponseData("0", "删除俱乐部成功", null);
	}

	/**
	 * 查询俱乐部详情
	 */
	@Override
	public String queryClub(String json, String clubId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("clubId", clubId);
		Map<String, Object> result = dao.queryForOne("QueryClub", param);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("1", "查询俱乐部详情失败", null);
		}
		List<Map<String, Object>> res = dao.query("QueryClubMbrLevServiceRel", param);
		result.put("clubMbrLevServiceRel", res);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询俱乐部详情成功", result);
	}

	/**
	 * 查询俱乐部列表
	 */
	@Override
	public String queryClubList(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object>  jsonObj = null;
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else {
			jsonObj=Tools.String2Map(json);//dubbo
		}
		List<Map<String, Object>> result = dao.query("QueryClub", jsonObj);
		logger.info("查询俱乐部列表信息："+result);
		if (Tools.isNull(result)) {
			return Tools.buildResponseData("0", "查询俱乐部列表为空", null);
		}
		logger.info("查询俱乐部列表下对应客户俱乐部会员等级关联服务信息："+result);
		for(Map<String, Object> r:result) {
			jsonObj.put("clubId", r.get("id"));
			List<Map<String, Object>> res = dao.query("QueryClubMbrLevServiceRel", jsonObj);
			r.put("clubMbrLevServiceRel", res);
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询俱乐部列表成功", result);
	}

}
