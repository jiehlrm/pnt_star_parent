package com.tydic.pntstar.service.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.util.DateUtil;
import com.tydic.pntstar.util.Tools;

/** 
* @Title: DefaultBusinessService.java 
* @Package com.tydic.pntstar.service.impl.province 
* @Description: service通用的功能
* @author weixsa@gmail.com 
* @date 2018年12月7日 下午12:25:49 
* @version V1.0 
* 	
	可查可兑；
	不是高额预警；
	有有效的账本信息； 
*/
@Service("defaultBusinessService")
public class DefaultBusinessService implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(DefaultBusinessService.class);
	@Autowired
	private CommonDBDao dao;
	//加载本地网列表
	private static Map<String, String> latnMap;
	
	
	/**
	 * 积分客户存在
	 * @throws Exception 
	 */
	public List getCustPointAcct(Map<String,Object> param) throws Exception {
		//基本参数校验
		this.validate(param);
		//查询custId
		String custId=this.getCustId(param);
		if(Tools.isNull(custId)) {
			throw new Exception("未查询到客户标识！");
		}
		//可查可兑
		Map<String,Object> map=new HashMap<>();
		map.put("custId", custId);//客户id
		map.put("statusCd",1000);
		return dao.query("QueryPointAcctList", param);
	}
	
	/**
	 * 星级客户存在
	 * @throws Exception 
	 */
	public List getCustClubMember(Map<String,Object> param) throws Exception {
		//基本参数校验
		this.validate(param);
		param.put("statusCd",1000);//状态有效
		return dao.query("QueryClubMember", param);
	}
	
	/**
	 * 基本条件校验 ：1 ：本地网字段必传  2： 操作类型不能为空 3： 操作值不能为空
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void validate(Map<String,Object> param) throws Exception {
        this.checkLatnId(param);
        if(Tools.isNull(param.get("objType"))) {
        	throw new Exception("操作对象类型不能为空.");
        }
        if(Tools.isNull(param.get("objValue"))) {
        	throw new Exception("操作对象值不能为空.");
        }
	}
	
	/**
	 * 根据传递的参数获取客户custId，如果没有查询到直接抛出未查到客户标识
	 * @throws Exception 
	 */
	public String getCustId(Map<String,Object> param) throws Exception {
		String objType=param.get("objType").toString();//操作对象类型
		String objValue=param.get("objValue").toString();//操作对象值
		String custId=null;
		List<Map<String, Object>> result=null;
		switch(objType){
			case "16"://客户标识CUST_ID
				custId=objValue;
				param.put("custId", custId);
				break;
			case "15"://如果是客户编码，查询客户表获取客户id
				param.put("custNumber", objValue);
				break;
			case "11":
//				param.put("prodId", "xxxx");
			case "12":
//				param.put("prodId", "xxxx");
			case "13":
//				param.put("prodId", "xxxx");
			case "17":
//				param.put("prodId", "xxxx");
				param.put("accNum",objValue );
				break;
			default:
				throw new Exception("不支持的操作类型！");
		}
		result=dao.query("QueryCustId", param);
		//SELECT CUST_ID custId from prod_inst where ACC_NUM=#{accNum}   支持传递latnId
		//SELECT CUST_ID custId from CUSTOMER where CUST_NUMBER=#{custNumber}
		if(Tools.isNull(result)) {
			throw new Exception("未查到客户标识");
		}
		custId=result==null ?null:result.get(0).get("custId").toString();
		return custId;
	}
	
	/**
	 * 根据客户id查询出积分账户号
	 * @param custId
	 * @return
	 * @throws Exception 
	 */
	public List getPointAcctIds(String custId) throws Exception {
		Map<String,Object> param=new HashMap<>();
		param.put("custId",custId );
		List<Map<String,Object>> accList=dao.query("QueryPointAcctListByCustId", param);
		if(Tools.isNull(accList)) {
			throw new Exception("未查到有效的积分账户信息");
		}
		return accList;
	}
	
	
	/**
	 * 根据客户id查询出账本号
	 * @param custId
	 * @return
	 * @throws Exception 
	 */
	public List getPointBalanceIds(String custId) throws Exception {
		Map<String,Object> param =new HashMap<>();
		List<Map<String,Object>> accList=getPointAcctIds(custId);
		param.put("custId", custId);
		param.put("accList", accList);
		List<Map<String,Object>> balanceList=dao.query("QueryPointAcctBalanceListByCond", param);
		if(Tools.isNull(balanceList)){
			throw new Exception("未查到有效的积分账本信息");
		}
		return balanceList;
	}
	
	
	/**
	 * 根据客户id查询客户权限信息
	 * @param custId
	 * @return
	 * @throws Exception 
	 */
	public Map getPointCustStatus(String custId) throws Exception {
		Map<String,Object> param =new HashMap<>();
		param.put("custId", custId);
		Map<String,Object> result=dao.queryForOne("QueryPointCustStatus", param);
		if(Tools.isNull(result)) {
			throw new Exception("暂无积分客户信息");
		}
		return result;
	}
	
	
	/**
	 *    校验积分权限
	 *    积分权限积分权限: (10:未达起兑,11:可查可兑,12:可查禁兑,13:禁查禁兑)
	 * @throws Exception 
	 */
	public String isPointLimit(String custId) throws Exception {
		Map<String,Object> result=this.getPointCustStatus(custId);
		String pointLimit=result.get("pointLimit").toString();
		if("13".equals(pointLimit)) {				
			throw new Exception("禁查禁兑,不允许客户操作");
		}
		return pointLimit;
	}
	
	/**
	 *      校验是否是高额预警
	   *    高额预警：(10：正常，11：未核对)   
	 * @throws Exception 
	 */
	public String isPointHighLimit(String custId) throws Exception {
		Map<String,Object> result=this.getPointCustStatus(custId);
		String pointHighLimit=result.get("pointHighLimit").toString();
		if("11".equals(pointHighLimit)) {
			throw new Exception("高额预警未核对,不允许操作");	
		}
		return pointHighLimit;
	}
	
	/**
	 * 通用组合校验  
	 * 1:高额预警必须是正常状态
	 * 2:必须是可查可兑状态
	 * 3:必须含有有效积分账户 返回账户标识列表
	 * @param custId
	 * @throws Exception
	 */
	public List isDefaultServiceValidate(String custId) throws Exception {
		isPointHighLimit(custId);//校验高额预警状态
		String  pointLimit=isPointLimit(custId);
		if(!"11".equals(pointLimit)) {
			throw new Exception("必须是可查可兑权限");
		}
		Map<String,Object> param=new HashMap<>();
		param.put("custId",custId );
		List<Map<String,Object>> accList=dao.query("QueryPointAcctListByCustId", param);
		if(Tools.isNull(accList)) {
			throw new Exception("未查到有效的积分账户信息");
		}
		return accList;
	}
	
	
	/**
	 * 对参数进行封装解析处理
	 * @param json
	 * @return
	 */
	public Map getParam(String json) {
		Map param=null;
		if(Tools.isNull(json)) {//http请求
			param=Tools.getParamsFromUrl();
		}else {//dubbo服务
			param=JSONObject.parseObject(json);
		}
		param.put("requestXml",json);//封装请求数据
		return param;
	}

	/**
	 * 积分兑换接口基本条件校验    1：本地网字段必传  latnId
	 *                 2： 操作类型不能为空  objType
	 *                 3： 操作值不能为空    objValue
	 *                 4：兑换对象编码   pointExchObjId
	 *                 5：兑换对象数量exchObjAmt
	 *                 CREATE_STAFF createStaff
//	 *                 6：外围订单号
                      *  CREATE_STAFF
                      *  serviceNbr
	 *                
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object>  exchPointValidate(Map<String,Object> param) throws Exception {
		  this.checkLatnId(param);
		  if(Tools.isNull(param.get("objType"))) {
	        	throw new Exception("操作对象类型不能为空.");
	      }
	      if(Tools.isNull(param.get("objValue"))) {
	        	throw new Exception("操作对象值不能为空.");
	      }
	      if(Tools.isNull(param.get("pointExchObjId"))) {
	        	throw new Exception("兑换对象编码不能为空.");
	      }
	      if(Tools.isNull(param.get("exchObjAmt"))) {
	        	throw new Exception("兑换数量不能为空.");
	      }
	      if(Tools.isNull(param.get("createStaff"))) {
	        	throw new Exception("操作工号不能为空.");
	      }
          String objType=param.get("objType").toString();//操作对象类型
          String objValue=param.get("objValue").toString();//操作对象值
          String pointExchObjId =param.get("pointExchObjId").toString();//兑换对象编码 
          int exchObjAmt=Integer.parseInt(param.get("exchObjAmt").toString());//兑换数量
//        String orderNbr=param.get("orderNbr").toString();//外围订单号
//        int unitPoint=Integer.parseInt(param.get("unitPoint").toString());//单价
//        int totalPoint=Integer.parseInt(param.get("totalPoint").toString());//总积分
//        String bizType=param.get("bizType").toString();//兑换业务类型
//        String itemId=param.get("itemId").toString();//规则明细ID
//        String exchType=param.get("exchType").toString();//兑换类型
//        if(Tools.isNull(unitPoint)) {
//        	throw new Exception("兑换单价不能为空.");
//        }
        //兑换资源存在并且充足
        Map<String,Object> result=dao.queryForOne("QueryPointExchObj", param);
        if(Tools.isNull(result)) {
        	throw new Exception("兑换资源不存在！");
        }
        int pureExchAmt=(int) result.get("purePointAmt");
        param.put("pureExchAmt",pureExchAmt);//封装兑换对象单价
        param.put("exchPoint",exchObjAmt*pureExchAmt);//封装兑换总积分
        param.put("extSerialId",getSeqNumber());//EXT_SERIAL_ID  订单号
        if(Tools.isNull(param.get("exchChnlId"))) {
        	param.put("exchChnlId","0000");//外系统标识,默认本系统
        }
        if(Tools.isNull(param.get("serviceNbr"))) {
        	param.put("serviceNbr",objValue);//未传递受惠号码默认是操作值
        }
        if(Tools.isNull(param.get("createStaff"))) {
        	param.put("createStaff","000000");//未传递操作人 默认为系统000000
        }
    	//获取货品属性
		List<Map<String,Object>> goods=dao.query("QueryGoods", param);
		if(Tools.isNull(goods) || goods.size()<1) {
			throw new Exception("商品构成有误");
		}
		param.put("goods", goods);
//        before_point_balance
//        exch_point
//        if(Tools.isNull(totalPoint)) {
//        	throw new Exception("兑换总价不能为空.");
//        }
       
//        if(resNum*unitPoint!=totalPoint){
//        	throw new Exception("总积分有误，请核对！");
//        }
//        if(Tools.isNull(orderNbr)){
//        	throw new Exception("订单号不能为空！");
//        }
//        if(getKey(orderNbr)){
//        	throw new Exception("该订单号正在处理！");
//        }
//		if(getKey(objType+objValue)){
//		throw new Exception("用户请求正在处理.");
//	    }
//        if(Tools.isNull(itemId)) {        	
//        	if(Tools.isNull(param.get("resCode"))) {
//        		throw new Exception("规则明细ID为空时，资源编码不能为空");
//        	}
//        	if(Tools.isNull(param.get("resName"))) {
//        		throw new Exception("规则明细ID为空时，资源名称不能为空");
//        	}
//        }
//        if(Tools.isNull(bizType)){
//			throw new Exception("兑换业务类型不能为空");
//		}
//		if(!"30".equals(bizType) && Tools.isNull(itemId)){
//			throw new Exception("兑换积分系统内部礼品时,规则明细ID不能为空");
//		}
//		if("10".equals(bizType) || "11".equals(bizType) || "12".equals(bizType)){
//			if(Tools.isNull(prodNbr)){
//				throw new Exception("产品号码不能为空.");
//			}
//		}
//		if(!"20".equals(bizType)){ //陕西兑换电信业务需对数量做控制
//			if(resNum!=1){
//				throw new Exception("单次兑换电信业务数量应为1.");
//			}
//		}
        //* 兑换类型 * 10：普通兑换；11：兑换话费，不填为普通兑换
//		if(Tools.isNull(exchType)){
//			param.put("exchType", 10);
//		}else if(!"10".equals(exchType) && !"11".equals(exchType)){
//			throw new Exception("兑换类型只能为10或者11.");
//		}
		
//		  /**

//	     */
//	    @JSONField(name = "ExchType")
//	    private String exchType;
        
//    	if(getKey(requestJsonBean.getSvcCont().getOrderNbr())){
//			throw new BusinessException("该订单号正在处理.");
//		}
//		if(getKey(requestJsonBean.getSvcCont().getObjType()+requestJsonBean.getSvcCont().getObjValue())){
//			throw new BusinessException("用户请求正在处理.");
//		}
//		if(getMemcacheKey(requestJsonBean.getSvcCont().getOrderNbr())){
//			throw new BusinessException("该订单号正在处理.");
//		}
//		if(getMemcacheKey(requestJsonBean.getSvcCont().getObjType()+requestJsonBean.getSvcCont().getObjValue())){
//			
        return param;
	}  
	
	/**
	 * 服务消费记录查询基本条件校验
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void qryServiceConsumeInfoValidate(Map<String,Object> param) throws Exception {
		this.validate(param);
        if(Tools.isNull(param.get("svcCode"))) {
        	throw new Exception("权益编码不能为空.");
        }
	} 
	
	/**
	 * 判断是否礼品还是电信业务  电信业务 有打折信息 需要打折
	 * 是否支持VIP折扣率[10:支持,11:不支持] isVipDiscount     兑换标识和积分等级
     * 是否支持网龄折扣率[10:支持,11:不支持] isAgeDiscount     兑换标识和在网时长
     * 是否支持节假日折扣率[10:不支持,11:支持] holidayDiscount  
	 * @param param
	 */
	public void discount(Map<String,Object> param) {
    	long totalPoint=(long) param.get("totalPoint");//总积分
    	//查询打折信息根据兑换对象和客户等级查询打折信息
    	Map<String,Object> vipResult=dao.queryForOne("QueryVipDiscount", param);
    	Map<String,Object> ageResult=dao.queryForOne("QueryAgeDiscount", param);
    	Map<String,Object> holidayResult=dao.queryForOne("QueryHolidayDiscount", param);
    	if(!Tools.isNull("")) {
    		long vipDiscount=vipResult.get("vipDiscount")==null?1l:(long)vipResult.get("vipDiscount");
    		long ageDiscount=ageResult.get("ageDiscount")==null?1l:(long)ageResult.get("ageDiscount");
//    		long holidayDiscount=result.get("holidayDiscount")==null?1l:(long)result.get("holidayDiscount");
    		//1:会员等级折扣率(VIP折扣率)
    		if("10".equals(param.get("isVipDiscount").toString()) && !Tools.isNull(vipDiscount)) {
    			totalPoint=totalPoint*vipDiscount;
    		}
    		//2:网龄折扣率
    		if("10".equals(param.get("isAgeDiscount").toString()) && !Tools.isNull(ageDiscount)) {
    			totalPoint=totalPoint*ageDiscount;
    		}
    		//3:节假日折扣率
    		if("11".equals(param.get("holidayDiscount").toString())) {
//    			totalPoint=totalPoint*holidayDiscount;
    		}
    		param.put("totalPoint",totalPoint );
    	}else {
    		logger.info("暂无打折信息");
    	}
    	
	}
	
	
	/**
	 * 积分扣减操作(积分账本表)
	 * @param param
	 */
	public void minusPoint(Map<String,Object> param) {
		//查询积分账户信息   年账本积分
		//积分扣减操作
		//插入记录表操作
		/*
		 * 查询积分账本信息，按年排序，优先使用即将过期的账本积分   有九个账本
		 * 
		 */
		String pointExchRecordId=dao.getPK("point_exch_record");//获取积分兑换记录标志
		param.put("pointExchRecordId",pointExchRecordId);
		dao.insert("InsertPointExchRecord", param,true);//新增一条积分兑换记录

		int exchPoint= (int) param.get("exchPoint");
		param.put("exchType", "minus");
		List<Map<String,Object>> pointList=dao.query("QueryPointAcctBalanceMinus", param);
		for (Map map : pointList) {
            //该帐本的积分够用
			map.put("exchType","minus");
			int pointBalance=(int) map.get("pointBalance");//账本积分
			if(pointBalance>=exchPoint) {
				map.put("exchPoint",exchPoint);
				map.put("pointBalance", pointBalance-exchPoint);
				exchUpdate(param,map,"minus");
            	break;
            }else {
            	map.put("exchPoint", pointBalance);
            	map.put("pointBalance",0);
            	exchUpdate(param,map,"minus");
            	exchPoint-=pointBalance;
            	continue;
            }
		
		}
		List<Map<String,Object>> goods=(List<Map<String, Object>>) param.get("goods");
		int exchObjAmt=Integer.valueOf(param.get("exchObjAmt").toString());
		for (Map map:goods) {
            map.put("exchObjAmt",Integer.valueOf(map.get("exchObjAmt").toString())*exchObjAmt);//更新兑换数量
            map.put("pointExchRecDetailId",dao.getPK("point_exch_rec_detail"));//获取积分兑换明细标识
            map.put("pointExchRecordId",pointExchRecordId);//积分兑换记录标识
            map.put("extSysId", param.get("extSysId"));
            map.put("extSerialId", param.get("extSerialId"));
            map.put("createStaff", param.get("createStaff"));
            dao.insert("InsertPointExchRecDetail", map,true);//新增一条积分兑换明细
		}
		//减库存操作
//		dao.update("xxxxxx", param);

	}
	
	
	/**
	 * exchType : minus 标识积分支出 (积分兑换)  add：标志积分收入(积分返销)
	 * 一次扣减进行的操作
	 * 更新积分账本表余额，可用余额的扣减
	 * 更新积分账户表中的余额
	 * 新增一条积分支出信息
	 * @param param   存放其他信息，map存放扣减信息
	 */
	public void exchUpdate(Map<String,Object> param,Map<String,Object> map, String exchType) {
		dao.update("UpdateExchPointAcctBalance",map,true);//更新积分账本表余额，可用余额的扣减
		dao.update("UpdateExchPointAcct",map,true);//更新积分账户表中的余额
		if("minus".equals(exchType)) {	
			map.put("pointBalancePayoutId",dao.getPK("POINT_BALANCE_PAYOUT"));
			//构建支出明细
            map.put("pointExchRecordId", param.get("pointExchRecordId"));
            map.put("pointPayoutType", 1);
            map.put("billingCycleId",DateUtil.getNowTimeYM());
            map.put("amount", map.get("exchPoint"));
            map.put("extSysId", param.get("extSysId"));
            map.put("extSerialId",param.get("extSerialId"));
            map.put("createStaff", param.get("createStaff"));
            map.put("updateStaff", param.get("createStaff"));
            map.put("createOrgId",param.get("createOrgId"));
			dao.insert("InsertExchPointPayout", map,true);//新增一条积分支出信息
		}else{
			map.put("pointTariffId", 0);
			map.put("pointSourceType",9);//积分来源类型
			map.put("objType", 1);
			map.put("objId",param.get("objValue"));
			map.put("basePointValue", 0);
			map.put("billingCycleId",DateUtil.getNowTimeYM());
			map.put("pointBalance", Integer.valueOf(map.get("pointBalance").toString())
					+Integer.valueOf(map.get("amount").toString()));
            map.put("extSysId", param.get("extSysId"));
            map.put("extSerialId",param.get("extSerialId"));
            map.put("createStaff", param.get("createStaff"));
            map.put("updateStaff", param.get("createStaff"));
            map.put("createOrgId",param.get("createOrgId"));
			map.put("pointBalanceSourceId",dao.getPK("POINT_BALANCE_SOURCE"));
			dao.insert("InsertExchPointSource", map,true);//新增一条积分来源信息
			dao.update("UpdatePointExchRecordBak",map,true);//更新兑换记录为已返销
		}
	}
	
	
	/**
	 * 生成系统流水号方法
	 * @param type
	 * @return
	 */
	public static String getSeqNumber() {
//	    int num = (int)(Math.random() * 100000)+100000;
//		return DateUtil.getNowTime()+num;
		return String.valueOf(new Date().getTime());
	}
	
	public static void main(String[] args) {
		System.out.println(getSeqNumber());
		System.out.println(new Date().getTime());
	}
	
	
	 /**
	      *    陕西兑换话费充值操作 调接口
	  * @param param
	  */
	 public Map<String,Object> presentCash(Map<String,Object> param) {
		    Map<String,Object> result=null;
			//SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			//SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
		    
			String id = "BUS0001" + "SVC0003";
//			BalRenderRes resp = null;
			String itemId = param.get("itemId").toString();
			//pointRedeemBean.getList().get(0).GET//充值号码
			String serviceNbr = param.get("objValue").toString();
			//this.getSequenceNext("SEQ_POINT_SERVICE_REC_"+pointRedeemBean.getLatnId(), pointRedeemBean.getLatnId());
//			try {
				
				//Service_ServiceLocator locator = new Service_ServiceLocator(config.getDestination());
				//Service_ServiceLocator locator = new Service_ServiceLocator("http://192.3.8.81:8088/htmlTest/servlet/OutXxmlServlet");
				// 1.获取服务定义配置 服务地址在域下的 servcieconfig.xml 配置  改为code表配置 2016年3月23日 18:37:37
			    //调用工具类发起远程调用
				//	Service_ServiceLocator locator = new Service_ServiceLocator(PntCodeTypeManager.getInstance().getTypeValue("BUS0001_SVC0003", "destination"));
				
		//		BalRenderReq in = new BalRenderReq();
//				synchronized (this) {
//				// in.setVBALANCE(String.valueOf(new Float(po.getCashValue()*100).intValue()));
//				in.setVBALANCE(String.valueOf(new Float(pointRedeemBean.getList().get(0).getCashValue()*100).intValue()));// 赠送金额，单位到分
//				//in.setVBALANCE(String.valueOf(pointRedeemBean.getList().get(0).getCashValue()));// 赠送金额，单位到分
//				in.setVBALANCETYPE(PntCodeTypeManager.getInstance().getTypeValue("BUS0001_SVC0003", "requestParameter"));//config.getRequestParameter());// 赠送余额类型标识
//				in.setVBALANCEUSERRULE("0");// 0 账户级余额 1用户级余额
//
//				in.setVCYCLELOWER("-1");// 每个月最多使用的限额，如果不设限填写“-1”
//				logger.info("规则ID(ITEMID)"+pointRedeemBean.getList().get(0).getItemId()+"**************对应值:"+PntCodeTypeManager.getInstance().getTypeValue("REDEEM_THE_BILL",itemId)+"===========没有值,默认为-1");
//	            if(!ObjectIsNull.check(PntCodeTypeManager.getInstance().getTypeValue("REDEEM_THE_BILL",itemId))){
//	            	in.setVCYCLEUPPER(PntCodeTypeManager.getInstance().getTypeValue("REDEEM_THE_BILL", itemId));
//	            }else{
//	            	in.setVCYCLEUPPER("-1");// 每个月最少使用的限额，如果不设限填写“-1”
//	            }
//
//				in.setVEFFDATE(df.format(new Date()));
//				in.setVEXPDATE((Long.valueOf(in.getVEFFDATE().substring(0, 4)) + 3) + in.getVEFFDATE().substring(4));
//				// if(serviceNbr.length() == 11){
//				// in.setVOBJECTID(serviceNbr);
//				// }else{
//				String latn = pointRedeemBean.getLatnId();
//				if (latn.equals("290")) {
//					latn = "029";
//				} else {
//					latn = "0" + latn;
//				}
//				if (latn.equals("0910") || latn.equals("910") ) {
//					// 咸阳本地网 区号都是029
//					latn = "029";
//				}
//				in.setVOBJECTID(latn + serviceNbr);// 资料内容: 如果资料类型是0 那么需要包含area_code+acc_nbr; 如果资料类型是1 那么填写serv_id; 如果资料类型是2 那么填写acct_id;
//
//				// }
//				in.setVOBJECTTYPE("0");// 0 输入设备号 1输入SERV_ID 2 输入账户 ACCT_ID
//				in.setVSYSID("03");// 系统标识
//				in.setVINTERFACEID("JFZS");// 描述调用此服务的外部接口标识 网厅余额赠送接口标识：“WTZS” 掌厅余额赠送接口标识：“ZTZS”
//				in.setVREQUESTTIME(df1.format(new Date()));
//				
//				in.setVREQUESTID(pointRedeemBean.getRequestid());
//				
//				logger.info("*******调用计费余额赠送接口开始111111111111111*******"+pointRedeemBean.getRequestid());
//				try {
//					resp = locator.getService().balRender(in);
//					logger.info("*******调用计费余额赠送接口返回信息*******结果码："+resp.getVRESULTCODE()+"*********结果消息描述:"+resp.getVRESULTDESC());
//					// 插入对账表
////					new CustPointMaintenanceDAO().createOdsAsk(in, resp);
//				} catch (Exception e) {
//					logger.error("调用计费余额赠送接口出错", e);
//				}
//				}
//				logger.info("*******调用计费余额赠送接口结束111111111111111*******");
//
//				/*if (isLock){
//					PooledInit.locker.unlock();
//				}
//				isLock = false;*/
//				
//				resp.setVRESULTCODE("0");
//				
//				if (resp == null || !"0".equals(resp.getVRESULTCODE())) {
//					if (resp != null) {
//						logger.error("resultcode:" + resp.getVRESULTCODE() + "  objectId:" + in.getVOBJECTID() + " requestId:" + resp.getVRESPONSEID());
//						throw new BusinessException("调用余额赠送接口失败!-->>" + resp.getVRESULTDESC());
//					} else {
//						logger.error("接口返回為空" + "  objectId:" + in.getVOBJECTID() + " requestId:" + resp.getVRESPONSEID());
//						throw new BusinessException("调用余额赠送接口失败!-->>接口返回為空");
//					}
//				}
//			} catch (BusinessException e) {
//				throw new BusinessException(e.getMessage(), e);
//			} 
//			ro.setExeCode("0");
			return null;
	 }
	 
	 
	 /**
	      *    陕西兑换话费对账表数据录入
	  * @param param
	  */
	 public void createOdsAsk(Map<String,Object> param) {
		 
	 }
	 
	 /**
	  * 增值业务 开通服务
	  * @param param
	  */
	 public void transactOptionPackage(Map<String,Object> param){
		 
	 }
	 
	 /**
	      *     异步发短信
	  * @param param
	  */
	 public void sendMsg(Map<String,Object> param){
		 
	 }
	
	 
	 /**
	   *        从缓存中查询是否已经存在,没有就添加，有就删除
	  * @param key
	  * @return
	  */
	 public synchronized boolean getKey(String key){
		if (!Tools.isNull(PointConfigUtil.getMsg(key))) {
			return true;
		}else{
			PointConfigUtil.putMsg(key, key);
			return false;
		}
	}
	 
	 /**
	      *    积分返销基础校验
	  * @param param
	 * @throws Exception 
	  */
	public void rebackPointValidate(Map<String,Object> param) throws Exception {
		this.validate(param);
		String extSerialId=param.get("extSerialId").toString();//兑换的外围订单号
//		long rebackPoint=(long) param.get("rebackPoint");//返销积分
//		String rollbackRequestID=param.get("rebackPoint").toString();//返销流水号
		if(Tools.isNull(extSerialId)) {
			throw new Exception("订单号不能为空.");			
		}
//		if(getKey(orderNbr)){
//			throw new Exception("该订单正在处理.");
//		}
	 }

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
//		if(latnMap==null) {
//			latnMap=new HashMap<>();
//			List<Map<String,Object>> result=dao.query("QUERY_PONIT_LAND", null);
//			for(Map map : result) {
//				latnMap.put(map.get("CODE").toString(),map.get("NAME").toString());
//			}
//		}
	}
	
	/**
	 * 校验latn是否正确
	 * @param latnId
	 * @throws Exception 
	 */
	public void checkLatnId(Map param) throws Exception {
		if(Tools.isNull(param.get("latnId"))) {
			throw new  Exception("本地网标识不能为空");
		}
		if(!latnMap.containsKey(param.get("latnId"))) {
			throw new Exception("本地网不存在");
		}
	}
	
	/**
	 * 省内接口调用日志记录
	 * @param param
	 */
	public void recordLog(Map<String,Object> param) {
		if(!Tools.isNull(param.get("extSerialId"))) {//积分兑换和返销
			param.put("msgId",param.get("extSerialId"));//流水号			
		}
		if(!Tools.isNull(param.get("orderItemId"))) {//服务扣减和返销
			param.put("msgId",param.get("orderItemId"));//流水号	
		}
		dao.insert("InsertPointProvinceLog", param);
	}
	
	
	/***
	 * 校验服务扣减参数校验
	 * 1：本地网不能为空并且存在
	 * 2：操作对象不能为空
	 * 3：服务编码不能为空
	 * 4：服务次数为正整数
	 * @throws Exception 
	 */
	public void serviceMinusValidate(Map<String,Object> param) throws Exception {
		this.checkLatnId(param);
		if(Tools.isNull(param.get("objValue"))) {
			throw new Exception("操作对象不能为空！");
		}
		if(Tools.isNull(param.get("objType"))) {
			throw new Exception("操作类型不能为空！");
		}
		if(Tools.isNull(param.get("memberServiceId"))) {
			throw new Exception("服务编码不能为空！");
		}
		if(Tools.isNull(param.get("consumeTimes"))) {
			throw new Exception("服务次数不能为空！");
		}
		try {
			int consumeTimes=Integer.parseInt(param.get("consumeTimes").toString());
			if(consumeTimes<=0) {
				throw new Exception("服务次数填写不正确！");
			}
		}catch(Exception e) {
			throw new Exception("服务次数填写不正确！");
		}
	}
}
