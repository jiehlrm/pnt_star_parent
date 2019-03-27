package com.tydic.pntstar.service.impl.province;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.province.ProvinceService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/** 
* @Title: ProvinceServiceImpl.java 
* @Package com.tydic.pntstar.service.impl.province 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2018年12月10日 下午4:21:53 
* @version V1.0 
*/
@Service("provinceServiceImpl")
public class ProvinceServiceImpl implements ProvinceService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	@Autowired
	private DefaultBusinessService defaultBusinessService;
	
	
	
	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.ProvinceService#queryPntExchgQuals(java.lang.String)
		(10:未达起兑,11:可查可兑,12:可查禁兑,13:禁查禁兑 ,14:高额未核对,15:欠费双停冻结,16:其他人工冻结)
	 */
	@Override
	public String queryPntExchgQuals(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
            //4:业务校验及业务执行
			/*
			 *  1、查询是否是高额预警，如果是返回高额未核对，可用积分返回0，不可用积分返回实际可用积分
				2、判断是否可查可兑
				3、如果查兑状态正常，判断是否冻结TB_PNT_POINT_FREEZE_#，如果是双停冻结，就查询出双停手机，如果不是就是人工其他冻结
				如果所有状态正常，就返回正常
			 */
			param.put("accList",defaultBusinessService.getPointAcctIds(custId));//获取账户号列表
			param.put("qryType", "1");//查询类型，查询可用积分
			Map<String,Object> pointBalanceTotal=dao.queryForOne("QueryPointBnlanceByQryType", param);//查询可用积分
			String pointBalance=pointBalanceTotal.get("pointBalance").toString();
			Map<String,Object> pointCustStatus=defaultBusinessService.getPointCustStatus(custId);
			String highPointimit=pointCustStatus.get("pointHighLimit").toString();//高额预警状态码
			String pointLimit=pointCustStatus.get("pointLimit").toString();//查询兑换资格
			String freezeReason=pointCustStatus.get("pointLimitReason").toString();//积分兑换状态原因说明
			String freezeType=pointCustStatus.get("freezeType").toString();//客户冻结状态
			if("11".equals(highPointimit)) {//高额未核对
				result.put("availablePnt", 0l);
				result.put("pntExchgQuals", 14);
				result.put("freezePnt", pointBalance);
				result.put("freezeReason", "客户积分高额'未核对'不允许查询兑换！");	
			}else{
				if(!"11".equals(pointLimit)) {//兑换资格不正常
					result.put("pntExchgQuals", pointLimit);
					result.put("availablePnt", 0l);
					result.put("freezePnt",pointBalance);
					result.put("freezeReason",freezeReason);	
				}else {//兑换资格正常判断是否有冻结
					//查询双停冻结表,状态为冻结状态的数据
					result.put("freezeReason",freezeReason);
					result.put("availablePnt",0l);
					result.put("freezePnt",pointBalance);
					if("1300".equals(freezeType)) {//双停冻结，查询双停手机				
						List<Map<String,Object>> prods = dao.query("QueryFreezeAccNumList", param);
						if(null != prods && prods.size()>0){
							List<Map<String,Object>> arrears = new ArrayList<Map<String,Object>>();
							for(Map prod: prods){
								Map arrear = new HashMap();
								arrear.put("accNum",prod.get("accNum"));
								arrear.put("offerName",prod.get("offerName"));
								arrear.put("prodName",prod.get("prodName"));
								arrears.add(arrear);
							}
							result.put("arrears", arrears);
						}				
						result.put("pntExchgQuals", 15);
					}else if("1400".equals(freezeType)){//人工冻结，其他冻结
						result.put("pntExchgQuals", 16);
					}else {//正常
						result.put("availablePnt",pointBalance);
						result.put("pntExchgQuals", 11);
						result.put("freezePnt",0l);
						result.put("freezeReason","可查可兑");
					}
				}
			}
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"积分兑换资格查询失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"积分兑换资格查询成功",result);
	}

	/* 
	 * 不需要校验积分客户状态子类的操作
	 * TB_PNT_CUST_CN_RULE_CHANGE : 兑换规则明细
	 * TB_PNT_CUST_INTEGRAL_RULE : 兑换规则
	 */
	@Override
	public String queryRedeemRes(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		List<Map<String,Object>>  result=null;//返回结果
		try {
			//1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
            if(Tools.isNull(param.get("latnId"))) {
            	throw new Exception("未找到对应本地网");
            }
            if(Tools.isNull(param.get("exchObjType"))) {
            	throw new Exception("兑换对象类型不能为空");
            }
            //10010000：电信业务
            if(!"10010000".equals(param.get("exchObjType").toString())) {
            	throw new Exception("兑换对象类型只能为电信业务");
            }
			//3：业务处理
            //构建查询条件
//            param.put("pageSize", 0l);//每页条数
//            param.put("pageIndex", 0l);//查询页码
            result=dao.query("QueryPointExchObjByCond", param);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"查询兑换资源失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"查询兑换资源成功",result);
	}

	/* 
	 * 
	 */
	@Override
	public String getPointAcct(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4：业务执行
			//4.1查询积分账户表
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			param.put("accList", accList);
			param.put("qryType", "1");//查询类型，查询可用积分
			Map<String,Object> point=dao.queryForOne("QueryPointBnlanceByQryType", param);
			String pointBalance=point.get("pointBalance").toString();//查询可用积分
			String pointValue=point.get("pointValue").toString();//查询累计积分
			param.put("qryType", "2");//查询类型，查询年末到期积分
			Map<String,Object> losePoint=dao.queryForOne("QueryPointBnlanceByQryType", param);
			String losePointBalance=losePoint==null?"0":losePoint.get("pointBalance").toString();//查询年末到期积分
			result.put("custId",custId );//客户标识
			result.put("totalBalance",pointValue);//累计积分值
			result.put("pointBalance",pointBalance);//可用积分值
			result.put("lastPointBalance",losePointBalance);//年末到期积分
			result.put("custType",10);//积分客户类型
			result.put("acctStatus",1000);//积分账本状态
			//待确定POINT_BALANCE_SOURCE
			param.put("balanceList", defaultBusinessService.getPointBalanceIds(custId));
			Map<String,Object>	monthAddPoint=dao.queryForOne("QueryPointBalanceSourceByMonth", param);
			String monthAddPointBalance=monthAddPoint==null?"0":monthAddPoint.get("monthAddPointBalance").toString();
			result.put("monthAddPointBalance",monthAddPointBalance);//当月新增积分
			result.put("basicsPointBalance",null);//当月基准积分值
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"可用积分查询失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"可用积分查询成功",result);
	}

	/*
	 * 按年查询积分接口 
	 */
	@Override
	public String queryYearCn(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			if(Tools.isNull(param.get("qryYear"))) {
            	throw new Exception("查询年份不能为空");
            }
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			//4：业务校验
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			//5:业务执行
			param.put("custId", custId);
			param.put("accList", accList);
			param.put("qryType", "3");//查询类型，按年查询积分
			Map<String,Object> list=dao.queryForOne("QueryPointBnlanceByQryType", param);
			result.put("pointValue", 0);
		    result.put("pointBalance",0);
			if(!Tools.isNull(list)) {
				result.put("pointValue", list.get("pointValue"));
			    result.put("pointBalance", list.get("pointBalance"));
			}
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"按年查询积分失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"按年查询积分成功",result);
	}

	/*
	 * 待清零积分查询 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String queryIntegralClear(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			//4:业务校验
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			//5:业务执行
			param.put("accList", accList);
			param.put("qryType", "2");//查询类型，查询年末到期积分
			Map<String,Object> list=dao.queryForOne("QueryPointBnlanceByQryType", param);
			result.put("pointBalance", "0");//初始赋值
			result.put("objValue", param.get("objValue"));				
			if(!Tools.isNull(list)) {
				result.put("pointBalance", list.get("pointBalance"));
			}
			return Tools.buildResponseData(Tools.SUCCESS,"待清零积分查询成功",result);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"待清零积分查询失败",e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.ProvinceService#exchangePoint(java.lang.String)
	 */
	@Override
	public String exchangePoint(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
            param.put("busCode","积分兑换");
			//2：参数校验  必填校验
            param=defaultBusinessService.exchPointValidate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4：业务执行
			//4.1 查询是否可查可兑，是否高额预警
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			param.put("accList", accList);
			param.put("qryType", "1");//查询类型，查询可用积分
			long beforePointBalance=Long.valueOf(dao.queryForOne("QueryPointBnlanceByQryType", param).get("pointBalance").toString());//查询可用积分
			//4.2查询可用积分余额是否大于总金额
			param.put("beforePointBalance", beforePointBalance);
			if(beforePointBalance<Long.valueOf(param.get("exchPoint").toString())) {
				throw new Exception("可用积分不足，无法兑换！");
			}
			//4.3 兑换对象是否有效，兑换资源是否充足，不知道从哪里获取
			//1:先查询工单表中是否有该订单号  throw new WebServiceException("定单号已存在.", "00641");
//			if(!Tools.isNull(dao.query("QueryPointExchRecDetailList", param))) {
//				throw new Exception("定单号已存在");
//			}
			//4.4 判断是否礼品还是电信业务，如果是电信业务，有打折信息 需要打折(单价打折)
//			if("10010000".equals("PointExchObjType")){
//				defaultBusinessService.discount(param);				
//			}
			//业务实现
			defaultBusinessService.minusPoint(param);
			//电信业务校验
//			String bizType=param.get("bizType").toString();
//			if("10".equals(bizType)) {//如果兑换的是话费
//				//话费充值
//				Map<String,Object> response=defaultBusinessService.presentCash(param);
//				//插入对账表
//			    Map<String,Object> pointOdsAsk=new HashMap<>();//构建对账信息
//			    pointOdsAsk.put("requestId", defaultBusinessService.getSeqNumber("1"));//请求流水
//			    pointOdsAsk.put("requestTime", DateUtil.getNowTime());//请求时间
//			    pointOdsAsk.put("interfaceId", "JFZS");//接口标识，描述调用此服务的外部接口标识 网厅余额赠送接口标识：“WTZS” 掌厅余额赠送接口标识：“ZTZS”
//			    pointOdsAsk.put("objectId",param.get("accNbr"));//资料内容:如果资料类型是0 那么需要包含area_code+acc_nbr,如果资料类型是1 那么填写serv_id,如果资料类型是2 那么填写acct_id
//			    pointOdsAsk.put("objectType", "0");//0 输入设备号 1输入SERV_ID 2 输入账户 ACCT_ID
//			    pointOdsAsk.put("balanceType", "");//赠送余额类型标识
//			    pointOdsAsk.put("balance",param.get("pointBalance"));//赠送金额，单位到分
//			    pointOdsAsk.put("balanceuserrule", "0");//0 账户级余额 1用户级余额
//			    pointOdsAsk.put("cycleupper", "-1");//每个月最多使用的限额，如果不设限填写-1
//			    pointOdsAsk.put("cyclelower", "-1");//每个月最少使用的限额，如果不设限填写-1
//			    pointOdsAsk.put("effdate", "");//生效日期
//			    pointOdsAsk.put("expdate", "");//失效日期
//			    pointOdsAsk.put("resultCode", response.get(""));//计费返回编码
//			    pointOdsAsk.put("resultDesc", response.get(""));//计费返回描述
//			    pointOdsAsk.put("responseTime", response.get(""));//计费返回时间
////				createOdsAsk(pointRedeemBean);
//				dao.insert("InsertPointOdsAsk", pointOdsAsk);
//			}else if("11".equals(bizType) || "12".equals(bizType)){
//				//5.2、如果是增值业务 (短信、流量)
////				//开通增值业务
////				OptionPackageVO optionPackageVO = new OptionPackageVO();
////				optionPackageVO.setPromoteId(pntCustRuleChange.getResourceCode());
////				transactOptionPackage(optionPackageVO,pointRedeemBean);
//				defaultBusinessService.transactOptionPackage(param);
//			}	
			
			
			//异步发短信 陕西配置发送短信开关	
			
			/*
			 * 4:积分扣减
			 * 5:插入兑换明细
			 */
			/*
			 * 
				6、积分扣减，先扣减即将失效的账本，记录账本变更，记录工单
				7、如果兑换的是话费，需要先插入对账表，再进行充值，调充值接口
				8、如果是增值业务 (短信、流量)，需要开通增值业务，往表TB_PNT_LATN_INTER_LOG_#插入数据
				陕西配置发送短信开关，如果是开，需要发送短信
			 */
			result.put("extSerialId",param.get("extSerialId"));//订单号
			dao.commit();
			param.put("status",0 );
			return Tools.buildResponseData(Tools.SUCCESS,"积分兑换成功",result);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			param.put("status",1);
			param.put("errNote",e.getMessage());
			return Tools.buildResponseData(Tools.FAILED,"积分兑换失败",e.getMessage());
		}finally {	
			defaultBusinessService.recordLog(param);
			dao.release();
//			PointConfigUtil.removeMsg(param.get("orderNbr").toString());
//			PointConfigUtil.removeMsg(param.get("objType").toString()+param.get("objValue").toString());
		}
	}

	/* (non-Javadoc)积分兑换明细()
	 * @see com.tydic.pntstar.service.province.ProvinceService#queryConsumeDetail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String queryPointExchDetail(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		List<Map<String,Object>> result = null;//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4：业务执行(需要执行权限吗)
			//4.1查询积分账户表
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			//4.1查询积分账本表
			param.put("accList",accList );
			List<Map<String,Object>> balanceList=dao.query("QueryPointAcctBalanceListByCond", param);
			if(Tools.isNull(balanceList)){
				throw new Exception("未查到有效的积分账本信息");
			}
			param.put("balanceList",balanceList );
			//4.1查询兑换记录明细
			result=dao.query("QueryPointExchRecordListByCond", param);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			
			return Tools.buildResponseData(Tools.FAILED,"积分兑换明细失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"积分兑换明细成功",result);
	}

	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.ProvinceService#qryStarInfoForCrm(java.lang.String)
	 */
	@Override
	public String qryStarInfoForCrm(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4：业务执行
			//4.1查询星级客户信息
			result=dao.queryForOne("QueryClubMemberByCond", param);
			result.put("custId", custId);
			if(!Tools.isNull(result)) {
				result.put("objValue", param.get("objValue"));
			}else {
				result=new HashMap<>();//构建返回对象
				result.put("level",3800);
				result.put("Levelname","0星");
				result.put("objValue", param.get("objValue"));
				return Tools.buildResponseData(Tools.SUCCESS,"星级查询成功",result);
			}
			//查询是否拍照用户
			if(!Tools.isNull(dao.queryForOne("QueryCameraCustByCond", param))){
				result.put("Levelname", result.get("membershipLevelName")+"(拍照)");
			}
			//评级时成长值当前成长值当前成长值类型积分倍增系数
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"星级查询失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"星级查询成功",result);
	}


	

	/**
	 * 服务消费记录查询
	 */
	@Override
	public String qryServiceConsumeInfo(String json) throws Exception {
		Map<String,Object> param = null;//请求参数
		List<Map<String,Object>> result = new ArrayList<>();//构建返回对象
		try {
		    //1.解析获取参数
            param=defaultBusinessService.getParam(json);
			//2.参数校验  必填校验
			defaultBusinessService.qryServiceConsumeInfoValidate(param);
			//3.获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//查询是否可查可兑，是否高额预警
//			Map<String,Object> pointLimit = defaultBusinessService.isPointLimit(custId);
//			if(!"11".equals(pointLimit.get("pointLimit").toString())) {				
//				throw new Exception("客户非可查可兑状态");
//			}
//			defaultBusinessService.isPointHighLimit(custId);
			defaultBusinessService.isDefaultServiceValidate(custId);
			//查询星级客户是否存在
			List<Map<String,Object>> clubMember = defaultBusinessService.getCustClubMember(param);
			if(clubMember.isEmpty()) {
				throw new Exception("星级客户不存在或星级客户状态非有效");
			}
			//4.业务执行
			//判断入参权益编码是不是100400-宽带权益
			if("100400".equals(param.get("svcCode").toString())) {
				throw new Exception("入参权益为宽带权益");
			}
			//开始和结束日期如果不为空就判断日期节点是否合法-yyyyMMdd
			if(!Tools.isNull(param.get("beginCycle")) || !Tools.isNull(param.get("endCycle"))) {
				if(!CommonUtil.isValidDate(param.get("beginCycle").toString()) || !CommonUtil.isValidDate(param.get("endCycle").toString())) {
					return Tools.buildResponseData(Tools.FAILED,"","日期节点格式不合法");
				}
			}
			param.put("memberServiceId", param.get("svcCode"));
			result = dao.query("QueryServiceConsumeInfo", param);
			Map<String,Object> res = dao.queryForOne("QueryClubMemberService", param);
			for(Map<String,Object> r:result) {
				r.put("serviceName", res.get("serviceName"));
				r.put("svcCode", param.get("svcCode"));
				r.put("minusObjType", param.get("objType"));
				r.put("minusObjValue", param.get("objValue"));
			}
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			
			return Tools.buildResponseData(Tools.FAILED,"服务消费记录查询失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"服务消费记录查询成功",result);
	}



	/* (non-Javadoc) 积分支出明细(未找到)
	 * @see com.tydic.pntstar.service.province.ProvinceService#queryPointDetail(java.lang.String)
	 */
	@Override
	public String queryPointPayoutDetail(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		List<Map<String,Object>> result = null;//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
			//2：参数校验  必填校验
			defaultBusinessService.validate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			//4：业务校验
			//4.1查询积分账户表
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			//4.1查询积分账本表
			param.put("custId", custId);
			param.put("accList",accList );
			List<Map<String,Object>> balanceList=dao.query("QueryPointAcctBalanceListByCond", param);
			if(Tools.isNull(balanceList)){
				throw new Exception("未查到有效的积分账本信息");
			}
			param.put("balanceList",balanceList );
			//4.1查询积分支出明细
			result=dao.query("QueryPointBalancePayoutListByCond", param);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"查询积分支出明细失败",e.getMessage());
		}
		return Tools.buildResponseData(Tools.SUCCESS,"查询积分支出明细成功",result);
	}

	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.ProvinceService#rebackPoint(java.lang.String)
	 */
	@Override
	public String rebackPoint(String json) throws Exception {
		Map<String,Object> param = null;//请求参数
		List<Map<String,Object>> result = null;//构建返回对象
		try {
		    //1:解析获取参数
            param=defaultBusinessService.getParam(json);
            param.put("busCode","积分返销");
			//2：参数校验  必填校验
			defaultBusinessService.rebackPointValidate(param);
			//3：获取客户id
			String custId=defaultBusinessService.getCustId(param);
			//4：业务校验，
			List<Map<String,Object>> accList=defaultBusinessService.isDefaultServiceValidate(custId);
			List<Map<String, Object>> rebackInfo=dao.query("QueryPointRecordBack", param);
			if(Tools.isNull(rebackInfo)) {
				throw new Exception("未找到对应兑换订单或该订单已经返销");
			}
			param.put("custId", custId);
			param.put("accList",accList );
			//5:业务执行
			param.put("exchType","add");
			Map<String,Object> map=dao.query("QueryPointAcctBalanceMinus",param).get(0);
			map.put("amount", rebackInfo.get(0).get("amount"));
			map.put("exchPoint", rebackInfo.get(0).get("amount"));
			map.put("exchType","add");
			map.put("pointExchRecordId", rebackInfo.get(0).get("pointExchRecordId"));
			defaultBusinessService.exchUpdate(param,map,"add");
			dao.commit();
			param.put("status",0);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			param.put("status",1);
			param.put("errNote", e.getMessage());
			return Tools.buildResponseData(Tools.FAILED,"积分返销失败",e.getMessage());
		}finally {
//			PointConfigUtil.removeMsg(param.get("orderNbr").toString());
			defaultBusinessService.recordLog(param);
			dao.release();
		}
		return Tools.buildResponseData(Tools.SUCCESS,"积分返销成功",result);
	}

}
