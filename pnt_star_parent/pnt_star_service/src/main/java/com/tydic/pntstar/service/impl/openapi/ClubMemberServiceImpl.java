package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.ClubMemberService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/*
 * 集团俱乐部会员API实现类
 */
@Service("clubMemberServiceImpl")
public class ClubMemberServiceImpl implements ClubMemberService{
	private static final Logger logger = LoggerFactory.getLogger(ClubMemberServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	
	/**
	 * 新增俱乐部会员
	 */
	@Override
	public String addClubMember(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object> jsonObj =JSONObject.parseObject(json);
		List<Map<String,Object>> memberServiceRecord = (List<Map<String,Object>>) jsonObj.get("memberServiceRecord");
		List<Map<String,Object>> memServAcct = (List<Map<String,Object>>) jsonObj.get("memServAcct");
		List<Map<String,Object>> memberExtendedAttr = (List<Map<String,Object>>) jsonObj.get("memberExtendedAttr");
		List<Map<String,Object>> memberActionRecord = (List<Map<String,Object>>) jsonObj.get("memberActionRecord");
		List<Map<String,Object>> memberChangeRecord = (List<Map<String,Object>>) jsonObj.get("memberChangeRecord");
		List<Map<String,Object>> memberAssessRecord = (List<Map<String,Object>>) jsonObj.get("memberAssessRecord");
		List<Map<String,Object>> memberCardGrantRecord = (List<Map<String,Object>>) jsonObj.get("memberCardGrantRecord");
		try {
			jsonObj.put("clubMemberId", dao.getPK("CLUB_MEMBER"));//获取俱乐部会员表主键ID
			jsonObj.put("createStaff", "123124");//测试使用
			//新增俱乐部会员
			//dao.insert("InsertClubMember", jsonObj);
			
			//---------------------会员服务记录MemberServiceRecord
			for(Map<String,Object> msr:memberServiceRecord) {
				msr.put("recordId", dao.getPK("MEMBER_SERVICE_RECORD"));//获取会员服务记录表主键ID
				msr.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				if(Tools.isNull(msr.get("memberServiceId"))) {//获取俱乐部服务标识--待确认
					//msr.put("memberServiceId", "");
				}
				Tools.addCommonParams(msr);
				logger.info("新增会员服务记录："+msr);
				//dao.insert("InsertMemberServiceRecord", msr);
			}
			
			//---------------------会员服务帐户MemServAcct
			for(Map<String,Object> msa:memServAcct) {
				msa.put("memServAcctId", dao.getPK("MEM_SERV_ACCT"));//获取会员帐户表主键ID
				msa.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				if(Tools.isNull(msa.get("memberServiceId"))) {//获取俱乐部服务标识--待确认
					//msa.put("memberServiceId", "");
				}
				Tools.addCommonParams(msa);
				logger.info("新增会员服务帐户："+msa);
				//dao.insert("InsertMemServAcct", msa);
			}
			
			//---------------------俱乐部会员扩展属性MemberExtendedAttr-
			for(Map<String,Object> mea:memberExtendedAttr) {
				mea.put("clubMemberAttrId", dao.getPK("MEMBER_EXTENDED_ATTR"));//获取俱乐部会员扩展属性表主键ID
				mea.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				Tools.addCommonParams(mea);
				logger.info("新增俱乐部会员扩展属性："+mea);
				dao.insert("InsertMemberExtendedAttr", mea);
			}
			
			//---------------------会员活动记录MemberActionRecord
			for(Map<String,Object> mar:memberActionRecord) {
				mar.put("recordId", dao.getPK("MEMBER_ACTION_RECORD"));//获取会员活动记录表主键ID
				mar.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				Tools.addCommonParams(mar);
				logger.info("新增会员活动记录："+mar);
				//dao.insert("InsertMemberActionRecord", mar);
			}
			
			//---------------------会员资格变动记录MemberChangeRecord
			for(Map<String,Object> mcr:memberChangeRecord) {
				mcr.put("recordId", dao.getPK("MEMBER_CHANGE_RECORD"));//获取会员资格变动记录表主键ID
				mcr.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				Tools.addCommonParams(mcr);
				logger.info("新增会员资格变动记录："+mcr);
				dao.insert("InsertMemberChangeRecord", mcr);
			}
			
			//---------------------会员等级评定记录memberAssessRecord
			for(Map<String,Object> mar:memberAssessRecord) {
				mar.put("recordId", dao.getPK("MEMBER_ASSESS_RECORD"));//获取会议等级评定记录表主键ID
				mar.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				Tools.addCommonParams(mar);
				logger.info("新增会员等级评定记录："+mar);
				dao.insert("InsertMemberAssessRecord", mar);
			}
			
			//---------------------会员卡发放记录memberCardGrantRecord
			for(Map<String,Object> mcgr:memberCardGrantRecord) {
				mcgr.put("recordId", dao.getPK("MEMBER_CARD_GRANT_RECORD"));//获取会员卡发放记录表主键ID
				mcgr.put("clubMemberId", jsonObj.get("clubMemberId"));//获取俱乐部会员表主键ID
				Tools.addCommonParams(mcgr);
				logger.info("新增会员卡发放记录："+mcgr);
				dao.insert("InsertMemberCardGrantRecord", mcgr);
			}
		} catch (Exception e) {
			return Tools.buildResponseData("1", "新增俱乐部会员失败", null); 
		}
		//返回报文信息--复用查询俱乐部会员信息方法
		String result = this.queryClubMember(null, jsonObj.get("clubMemberId").toString());
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "新增俱乐部会员成功", result); 
	}

	/**
	 * 修改俱乐部会员信息
	 */
	@Override
	public String modifyClubMember(String json, String clubMemberId) throws Exception {
		logger.info("server receive ===>"+json);
		try {
			List<Map> reqMap = JSONObject.parseArray(json,Map.class);
			for(Map m:reqMap) {
				if("replace".equals(m.get("op"))) {
					m.put("updateStaff", "1111");//测试使用
					m.put("clubMemberId", clubMemberId);
					dao.update("UpdateClubMember", m);
				}
			}
		} catch (Exception e) {
			return Tools.buildResponseData("1", "修改俱乐部会员信息失败", null); 
		}
		//返回报文信息--复用查询俱乐部会员信息方法
		String result = this.queryClubMember(null, clubMemberId);
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "修改俱乐部信息成功", result);
	}

	/**
	 * 删除俱乐部会员
	 */
	@Override
	public String delClubMember(String json, String clubMemberId) throws Exception {
		logger.info("server receive ===>"+clubMemberId);
		Map<String, Object> param = new HashMap<>();
		param.put("clubMemberId", clubMemberId);
		//dao.delete("DeleteClubMember", param);//物理删除
		param.put("statusCd", "1100");
		param.put("updateStaff", "1241241");
		dao.update("DeleteClubMember", param);//逻辑删除-会员状态改为1100无效
		return Tools.buildResponseData("0", "删除俱乐部会员成功", null);
	}

	/**
	 * 查询俱乐部会员详情
	 */
	@Override
	public String queryClubMember(String json, String clubMemberId) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String, Object> param = new HashMap<>();
		param.put("clubMemberId", clubMemberId);
		Map<String, Object> resClubMember = dao.queryForOne("QueryClubMember", param);
		List<Map<String, Object>> resMemberServiceRecord = dao.query("QueryMemberServiceRecord", param);
		List<Map<String, Object>> resMemServAcct = dao.query("QueryMemServAcct", param);
		List<Map<String, Object>> resMemberExtendedAttr = dao.query("QueryMemberExtendedAttr", param);
		List<Map<String, Object>> resMemberActionRecord = dao.query("QueryMemberActionRecord", param);
		List<Map<String, Object>> resMemberChangeRecord = dao.query("QueryMemberChangeRecord", param);
		List<Map<String, Object>> resMemberAssessRecord = dao.query("QueryMemberAssessRecord", param);
		List<Map<String, Object>> resMemberCardGrantRecord = dao.query("QueryMemberCardGrantRecord", param);
//		if (Tools.isNull(resClubMember) || Tools.isNull(resMemberServiceRecord) || Tools.isNull(resMemServAcct) || Tools.isNull(resMemberExtendedAttr)
//				|| Tools.isNull(resMemberActionRecord) || Tools.isNull(resMemberChangeRecord) || Tools.isNull(resMemberAssessRecord) || Tools.isNull(resMemberCardGrantRecord)) {
//			return Tools.buildResponseData("1", "查询俱乐部会员失败", null);
//		}
		Map<String, Object> resParam = new HashMap<>();
		for(Map<String,Object> res:resMemberServiceRecord) {
			resParam.put("memberServiceId", res.get("memberServiceId"));
			res.remove("memberServiceId");
			res.put("memberServiceId", dao.queryForOne("QueryClubMemberServiceForClubMember", resParam));
		}
		resClubMember.put("resMemberServiceRecord", resMemberServiceRecord);
		resParam.clear();
		for(Map<String,Object> res:resMemServAcct) {
			resParam.put("memberServiceId", res.get("memberServiceId"));
			res.remove("memberServiceId");
			res.put("memberServiceId", dao.queryForOne("QueryClubMemberServiceForClubMember", resParam));
		}
		resClubMember.put("resMemServAcct", resMemServAcct);
		resParam.clear();
		for(Map<String,Object> res:resMemberExtendedAttr) {
			resParam.put("actionId", res.get("actionId"));
			res.remove("actionId");
			res.put("actionId", dao.queryForOne("QueryClubMemberAction", resParam));
		}
		resClubMember.put("resMemberExtendedAttr", resMemberExtendedAttr);
		resClubMember.put("resMemberActionRecord", resMemberActionRecord);
		resClubMember.put("resMemberChangeRecord", resMemberChangeRecord);
		resClubMember.put("resMemberAssessRecord", resMemberAssessRecord);
		resClubMember.put("resMemberCardGrantRecord", resMemberCardGrantRecord);
		resParam.clear();
		resClubMember.remove("clubId");
		resParam.put("clubId", resClubMember.get("clubId"));
		resClubMember.put("clubId", dao.queryForOne("QueryClubForClubMember", resParam));
		logger.info("server reponse <==="+resClubMember);
		return Tools.buildResponseData("0", "查询俱乐部会员详情成功", resClubMember);
	}

	/**
	 * 查询俱乐部会员列表
	 */
	@Override
	public String queryClubMemberList(String json) throws Exception {
		logger.info("server receive ===>"+json);
		Map<String,Object>  jsonObj = null;
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else {
			jsonObj=Tools.String2Map(json);//dubbo
		}
		List<Map<String, Object>> result = dao.query("QueryClubMember", jsonObj);
		for(Map<String, Object> res:result) {
			res.remove("clubId");
		}
		logger.info("server reponse <==="+result);
		return Tools.buildResponseData("0", "查询俱乐部会员列表成功", result);
	}

}
