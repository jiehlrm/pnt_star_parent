package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.ClubMemberServiceService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/*
 * 集团俱乐部会员权益API实现类
 */
@Service("clubMemberServiceServiceImpl")
public class ClubMemberServiceServiceImpl implements ClubMemberServiceService{
	private static final Logger logger = LoggerFactory.getLogger(PointAcctServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Autowired
	private PointCommonComponent pointCommonComponent;
	/**
	 * 新增俱乐部会员权益
	 */
	@Override
	public String addClubMemberService(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj =JSONObject.parseObject(json);
		List<Map<String,Object>> mbrServiceCatalogLoc = (List<Map<String, Object>>) jsonObj.get("mbrServiceCatalogLoc");
		List<Map<String, Object>> clubServiceRel = (List<Map<String, Object>>) jsonObj.get("clubServiceRel");
		try {
			jsonObj.put("memberServiceId", dao.getPK("CLUB_MEMBER_SERVICE"));//获取俱乐部会员服务表主键ID
			if(Tools.isNull(jsonObj.get("clubId"))) {//俱乐部标识--待确认--新增俱乐部会员权益时若不传俱乐部标识-不符合常理
				jsonObj.put("clubId", "1");
			}
			Tools.addCommonParams(jsonObj);
			//新增俱乐部会员权益
			dao.insert("InsertClubMemberService", jsonObj);
			
			//---------------------会员服务目录位置mbrServiceCatalogLoc--具体业务待确认
			
			//---------------------俱乐部服务关系clubServiceRel--具体业务待确认
			
		} catch (Exception e) {
			return Tools.buildResponseData("1", "新增俱乐部会员权益失败", null); 
		}
		//返回报文信息--复用查询俱乐部会员权益信息方法
		String result = this.queryClubMemberService(null, jsonObj.get("memberServiceId").toString());
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "新增俱乐部会员权益成功", result); 
	}

	/**
	 * 修改俱乐部会员权益信息
	 */
	@Override
	public String modifyClubMemberService(String json, String memberServiceId) throws Exception {
		logger.info("server receive ===>"+json);
		try {
			List<Map> reqMap = JSONObject.parseArray(json,Map.class);
			for(Map m:reqMap) {
				if("replace".equals(m.get("op"))) {
					m.put("updateStaff", "1111");//测试使用
					m.put("memberServiceId", memberServiceId);
					dao.update("UpdateClubMemberService", m);
				}
			}
		} catch (Exception e) {
			return Tools.buildResponseData("1", "修改俱乐部会员权益信息失败", null); 
		}
		//返回报文信息--复用查询俱乐部会员信息方法
		String result = this.queryClubMemberService(null, memberServiceId);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "修改俱乐部权益信息成功", result);
	}

	/**
	 * 删除俱乐部会员权益
	 */
	@Override
	public String delClubMemberService(String json, String memberServiceId) throws Exception {
		logger.info("server receive ===>"+memberServiceId);
		Map<String, Object> param = new HashMap<>();
		param.put("memberServiceId", memberServiceId);
		//dao.delete("DeleteClubMemberService", param);//物理删除
		param.put("statusCd", "1100");
		param.put("updateStaff", "1241241");
		dao.update("DeleteClubMemberService", param);//逻辑删除-会员权益状态改为1100无效
		return Tools.buildResponseData("0", "删除俱乐部会员权益成功", null);
	}

	/**
	 * 查询俱乐部会员权益详情
	 */
	@Override
	public String queryClubMemberService(String json, String memberServiceId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("memberServiceId", memberServiceId);
		Map<String, Object> result = dao.queryForOne("QueryClubMemberService", param);//查询俱乐部会员权益详情
		List<Map<String, Object>> mbrServiceCatalogLoc = dao.query("QueryMbrServiceCatalogLoc", param);//查询会员服务目录位置信息
		List<Map<String, Object>> clubServiceRel = dao.query("QueryClubServiceRel", param);//查询俱乐部服务关系
		if(Tools.isNull(result) || Tools.isNull(mbrServiceCatalogLoc) || Tools.isNull(clubServiceRel)) {
			return Tools.buildResponseData("1", "查询俱乐部会员权益详情失败", null);
		}
//		param.put("catalogItemId", mbrServiceCatalogLoc.get("catalogItemId"));
//		Map<String, Object> catalogItemId = dao.queryForOne("QueryCatalogItem", param);//查询对应目录节点信息
//		param.put("serviceId", clubServiceRel.get("serviceId"));
//		Map<String, Object> serviceId = dao.queryForOne("QueryService", param);//查询对应服务信息
		param.put("clubId", result.get("clubId"));
		Map<String, Object> clubId = dao.queryForOne("QueryClubForClubMember", param);//查询俱乐
		//返回出参
//		mbrServiceCatalogLoc.put("catalogItemId", catalogItemId);
//		clubServiceRel.put("serviceId", serviceId);
		result.put("mbrServiceCatalogLoc", mbrServiceCatalogLoc);
		result.put("clubServiceRel", clubServiceRel);
		result.put("clubId", clubId);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询俱乐部会员权益详情成功", result);
	}

	/**
	 * 查询俱乐部会员权益列表
	 */
	@Override
	public String queryClubMemberServiceList(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object>  jsonObj = null;
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else {
			jsonObj=Tools.String2Map(json);//dubbo
		}
		List<Map<String, Object>> result = dao.query("QueryClubMemberService", jsonObj);
		for(Map<String, Object> res:result) {
			res.remove("clubId");
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询俱乐部会员权益列表成功", result);
	}

	@Override
	public String queryClubMemberServiceListbyCustId(String param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> initParam = CommonUtil.json2Map(json);
		String total = "0";
        Object CUST_ID = json.get("CUST_ID");
        initParam.put("custId", CUST_ID);
		List<Map<String, Object>> result = dao.query("QueryClubServiceListByCustId", initParam);
		List<Map<String, Object>> rsultTotal = dao.query("QueryClubServiceListByCustIdCount", initParam);
		if (!CommonUtil.isEmptyList(rsultTotal)) {
			if (rsultTotal.get(0).get("total").toString() != null)
				total = rsultTotal.get(0).get("total").toString();
		}
		JSONObject resObj = new JSONObject();
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(result));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	@Override
	public String queryClubMemberServiceRecordList(String param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> initParam = CommonUtil.json2Map(json);
		String total = "0";
		Object custId = json.get("CUST_ID");
		Object memberServiceId = json.get("memberServiceId");
		initParam.put("memberServiceId", memberServiceId);
        initParam.put("custId", custId);
		List<Map<String, Object>> result = dao.query("QueryMemberServiceRecordListByCustId", initParam);
		List<Map<String, Object>> rsultTotal = dao.query("QueryMemberServiceRecordListByCustIdCount", initParam);
		if (!CommonUtil.isEmptyList(rsultTotal)) {
			if (rsultTotal.get(0).get("total") != null)
				total = rsultTotal.get(0).get("total").toString();
		}
		JSONObject resObj = new JSONObject();
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(result));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
	}

	@Override
	public String equityConsume(String param) throws Exception {
		// TODO Auto-generated method stub
		JSONObject json = JSONObject.parseObject(param);
        JSONObject resObj = new JSONObject();
		Map<String, Object> initParam = CommonUtil.json2Map(json);
		Object memServAcctId = json.get("memServAcctId");
		Object memberServiceId = json.get("memberServiceId");
		Object equityAmt = json.get("equityAmt");
        initParam.put("memServAcctId", memServAcctId);
        initParam.put("memberServiceId", memberServiceId);
        initParam.put("equityAmt", equityAmt);
        try{
        	dao.update("UpdateMemeberServiceUseableCount", initParam, true);
        	//dao.insert("serviceName", initParam, true);
            dao.commit();
        }catch(Exception e){
        	logger.info(e.getMessage(),e);
			dao.rollback();
			return pointCommonComponent.buildRetrun(PointConstant.failCode, "权益消费失败", resObj);
        }
        return pointCommonComponent.buildRetrun(PointConstant.successCode, "权益消费成功", resObj);
	}
}
