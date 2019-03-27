package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
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
import com.tydic.pntstar.service.openapi.PointAdjustService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.DateUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
/**
 * 积分调整实现类
 * 
 * @author zhouman
 *
 */
@Service("pointAdjustServiceImpl")
public class PointAdjustServiceImpl implements PointAdjustService{
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Autowired
    private PointCommonComponent pointCommonComponent;
	@Autowired
	private DefaultBusinessService defaultBusinessService;
	private static final Logger logger = LoggerFactory.getLogger(PointAdjustServiceImpl.class);
	/**
	 * 获取积分余额信息
	 */
	@Override
	public String getPointBalance(String param)  throws Exception{
		Map<String, Object>json=defaultBusinessService.getParam(param);
		//JSONObject json = JSONObject.parseObject(param);
		Map<String, Object> sqlParam1 = new HashMap<String, Object>();
		sqlParam1.put("CUST_ID", json.get("CUST_ID"));
		// 查询积分账户类型为120300，120400的信息
		List<Map<String, Object>> result1 = dao.query("QueryPointAdjustAcct", sqlParam1);
		// 变为string返回
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", result1);
	}
	/**
	 * 修改账户 账本 来源明细 信息
	 */
	@Override
	public Map<String, Object> doPointAdjust(String param) throws Exception {
		try {
			Map<String, Object> json = defaultBusinessService.getParam(param);
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) json.get("DATA_LIST");
			for (Map<String, Object> data : dataList) {
				Map<String, Object> sqlParam1 = new HashMap<String, Object>();
				sqlParam1.put("CUST_ID", json.get("CUST_ID"));
				int adjustValue = Integer.valueOf(data.get("ADJUST_VALUE").toString());//可正可负
				sqlParam1.put("ADJUST_VALUE", data.get("ADJUST_VALUE"));
				sqlParam1.put("POINT_ACC_TYPE", data.get("POINT_ACC_TYPE"));
				sqlParam1.put("POINT_ACCT_ID", data.get("POINT_ACCT_ID"));
				// 修改积分账户余额
				dao.update("UpdatePointAdjustAcct", sqlParam1, true);
				// 增加积分来源明细
				if (adjustValue > 0) {
					// 修改积分账本信息
					dao.update("UpdatePointAdjustAcctBalanceAdd", sqlParam1, true);
					// 查询积分账本id
					Map<String, Object> result1 = dao.queryForOne("QueryPointAcctBalanceId", sqlParam1);
					Map<String, Object> insertsql1 = new HashMap<String, Object>();
					insertsql1.put("pointAcctBalanceId", result1.get("POINT_ACCT_BALANCE_ID"));
					insertsql1.put("pointBalance", result1.get("POINT_BALANCE"));
					insertsql1.put("adjustReason", json.get("SELECT"));
					insertsql1.put("remark", json.get("REASON_REMARK"));
					insertsql1.put("amount", Math.abs(adjustValue));
					insertsql1.put("basePointValue", Math.abs(adjustValue));
					insertsql1.put("pointSourceType", 7);
					insertsql1.put("pointBalanceSourceId", dao.getPK("point_balance_source"));
					logger.debug(insertsql1.toString());
					dao.insert("InsertExchPointSource", insertsql1, true);
				} else if (adjustValue < 0){//增加积分扣减明细
					Map<String, Object> param2 = new HashMap<String, Object>();
					//复用sql
					List<Map<String, Object>> accList=new ArrayList<Map<String,Object>>();
					Map<String, Object> acc = new HashMap<String, Object>();
					acc.put("pointAcctId",data.get("POINT_ACCT_ID"));
					accList.add(acc);
					param2.put("accList", accList);
					param2.put("exchPoint", adjustValue);
					param2.put("adjustReason", json.get("SELECT"));
					param2.put("remark", json.get("REASON_REMARK"));
					minusPoint(param2);
				}
			}
			dao.commit();
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("resultCode", new String("00000"));
			rtnMap.put("resultMsg", new String("积分调整成功"));
			return rtnMap;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			dao.rollback();
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("积分调整失败"));
			return rtnMap;
		} finally {
			dao.release();
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
		 * 查询积分账本信息，按年排序，优先使用即将过期的账本积分   有三个账本
		 * 
		 */
		int exchPoint=Math.abs( (int) param.get("exchPoint"));
		param.put("exchType", "minus");
		//param  pointAcctId
		List<Map<String,Object>> pointList=dao.query("QueryPointAcctBalanceMinus", param);
		for (Map<String,Object> map : pointList) {
            //该帐本的积分够用
			map.put("exchType","minus");
			int pointBalance=(int) map.get("pointBalance");//账本积分
			if(pointBalance>=exchPoint) {
				map.put("exchPoint",exchPoint);
				map.put("pointBalance", pointBalance-exchPoint);
				exchUpdate(param,map);
            	break;
            }else {
            	map.put("exchPoint", pointBalance);
            	map.put("pointBalance",0);
            	exchUpdate(param,map);
            	exchPoint-=pointBalance;
            	continue;
            }
		
		}
	}
	/**
	 * exchType : minus 标识积分支出 (积分兑换)  add：标志积分收入(积分返销)
	 * 一次扣减进行的操作
	 * 更新积分账本表余额，可用余额的扣减
	 * 更新积分账户表中的余额
	 * 新增一条积分支出信息
	 * @param param   存放其他信息，map存放扣减信息
	 */
	public void exchUpdate(Map<String,Object> param,Map<String,Object> map) {
		dao.update("UpdateExchPointAcctBalance",map,true);//更新积分账本表余额，可用余额的扣减
			map.put("pointBalancePayoutId",dao.getPK("POINT_BALANCE_PAYOUT"));
			//构建支出明细
            map.put("pointPayoutType", 2);
            map.put("amount", map.get("exchPoint"));
            map.put("adjustReason",param.get("adjustReason"));
            map.put("remark",param.get("remark"));
			dao.insert("InsertExchPointPayout", map,true);//新增一条积分支出信息
	}
}
