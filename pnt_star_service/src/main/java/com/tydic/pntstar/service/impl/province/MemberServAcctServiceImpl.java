package com.tydic.pntstar.service.impl.province;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.province.MemberServAcctService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/** 
* @Title: MemberServAcctServiceImpl.java 
* @Package com.tydic.pntstar.service.impl.province 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2019年1月4日 上午9:08:53 
* @version V1.0 
*/
@Service
public class MemberServAcctServiceImpl implements MemberServAcctService {

	
	private static final Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private DefaultBusinessService defaultBusinessService;
	
	/*
	 * 会员服务扣减
	 * 1:参数校验  (产品号码  accNbr,本地网  latnId,权益编码  memberServiceId,服务扣减次数  consumeTimes,创建人  createStaff) 
	 * 2:校验俱乐部会员是否存在，否者提示俱乐部会员不存在，校验该会员是否具有此权益，否者不存在该权益
	 * 3:校验服务权益次数是否足够扣减
	 * 4:进行服务扣减操作
	 */
	@Override
	public String serviceMinus(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try {
			 //1.解析获取参数
            param=defaultBusinessService.getParam(json);
            param.put("busCode","服务权益扣减");
			//2.参数校验  必填校验
			defaultBusinessService.serviceMinusValidate(param);
			//3.获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//查询俱乐部会员信息
			Map<String, Object> clubMemeber=dao.queryForOne("QueryClubMember", param);
			if(Tools.isNull(clubMemeber)) {
				throw new Exception("未查到俱乐部会员信息");
			}
			//查询俱乐部会员服务账户信息
			param.put("clubMemberId",clubMemeber.get("id"));
			List<Map<String, Object>> memberAcct=dao.query("QueryMemServAcct",param);
			if(Tools.isNull(memberAcct)) {
				throw new Exception("未查到服务账户");
			}
			int equityAmt=Integer.parseInt(param.get("consumeTimes").toString());//服务次数
			int useableCount=Integer.parseInt(memberAcct.get(0).get("useableCount").toString());//可用次数
			if(equityAmt>useableCount){
				throw new Exception("服务可用次数不足");
			}
//			param.put("equityAmt",equityAmt);
			param.put("memServAcctId",memberAcct.get(0).get("id"));
			//4业务执行
			dao.update("UpdateMemeberServiceUseableCount", param,true);//更新账户权益
			//拼接插入参数
			String orderItemId=defaultBusinessService.getSeqNumber();//流水订单号
			param.put("orderItemId", orderItemId);
			param.put("statusCd",1000);
			param.put("recordId",dao.getPK("member_service_record"));
			dao.insert("InsertMemberServiceRecord", param,true);//插入会员服务消费记录
			result.put("orderItemId",orderItemId);//订单号
			dao.commit();
			param.put("status",0 );
			return Tools.buildResponseData(Tools.SUCCESS,"服务消费成功",result);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			param.put("status",1);
			param.put("errNote",e.getMessage());
			return Tools.buildResponseData(Tools.FAILED,"服务消费失败",e.getMessage());
		}finally {
			defaultBusinessService.recordLog(param);
			dao.release();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.ProvinceService#serviceBack(java.lang.String)
	 */
	@Override
	public String serviceBack(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
            param.put("busCode","服务返销");
			//2：参数校验  必填校验
            defaultBusinessService.validate(param);
			if(Tools.isNull(param.get("orderItemId"))) {
				throw new Exception("消费单号不能为空");
			}
//			if(Tools.isNull(param.get("accNbr"))) {
//				throw new Exception("产品号码不能为空");
//			}
//			if(Tools.isNull(param.get("backTimes"))) {
//				throw new Exception("返销次数不能为空");
//			}
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4：业务执行
			//4.1查询会员服务记录
			Map<String, Object> memeberId=dao.queryForOne("QueryClubMember", param);
			if(Tools.isNull(memeberId)) {
				throw new Exception("未查到客户会员信息");
			}
			Map<String,Object> list=dao.queryForOne("QueryMemberServiceRecord", param);
			if (Tools.isNull(list)){
				throw new Exception("未查到对应的积分消费记录");
			}
			if("1".equals(list.get("rebackFlag").toString())) {
				throw new Exception("已返销不允许再次返销");
			}
			param.put("consumeTimes",list.get("consumeTimes"));//获取需要返销的次数
			param.put("clubMemberId", list.get("clubMemberId"));
			param.put("memberServiceId", list.get("memberServiceId"));
			//4.2查询返销次数并且大于0进行返销操作
			//4.3非有效状态的星级客户不能返销此权益
			/*
			 * 判断是否是无限次权益，如果是直接抛出异常 无限次权益不需要返销 
			 * 找对应的规则的周期、判断权益消费时间是否在权益配置对应的权益周期内，如果不在权益周期内的不能返销
			 * 此工单对应的权益赠送规则不存在或是已过期，不能返销该记录
			 */
			//dao.update("updateMemberRecord", param, true);
			//更新权益次数
			dao.update("UpdateMemberAcct", param,true);
			//更新服务记录为已返销
			Map param1=new HashMap<>();
			param1.put("recordId", list.get("id"));
			dao.update("UpdateMemberServiceRecord", param1,true);
			param.put("status",0 );
			dao.commit();
			return Tools.buildResponseData(Tools.SUCCESS,"服务返销成功",null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			param.put("status",1);
			param.put("errNote",e.getMessage());
			return Tools.buildResponseData(Tools.FAILED,"服务返销失败",e.getMessage());
		}finally {
			defaultBusinessService.recordLog(param);
			dao.release();
		}
	}

}
