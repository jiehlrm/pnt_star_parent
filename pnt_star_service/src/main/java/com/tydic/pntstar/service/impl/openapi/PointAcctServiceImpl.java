/**
 * 
 */
package com.tydic.pntstar.service.impl.openapi;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.Global;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointAcctService;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * @author root
 * 积分账号实现类
 */
@Service("pointAcctServiceImpl")
public class PointAcctServiceImpl implements PointAcctService {

	private static final Logger logger = LoggerFactory.getLogger(PointAcctServiceImpl.class);

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	
	/**
	 * 根据积分帐户id主键查询
	 */
	@Override
	public String queryPointAcct(String json, String pointAcctId) throws Exception {
		//解析参数条件
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointAcctId", pointAcctId);
			Map<String, Object> result = dao.queryForOne("QueryPointAcct", param);
			if (Tools.isNull(result)) {
				return Tools.buildResponseData(Tools.FAILED, "查询积分账户失败", null);
			}
			//查询积分冻结解冻记录
			param.remove("pointAcctId");
			param.put("objId",pointAcctId);
//			param.put("objType", result.get("objType"));
			List<Map<String, Object>> pointFrozenRecord = dao.query("QueryPointFrozenRecord", param);
			result.put("pointFrozenRecord", pointFrozenRecord);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分账户成功", result);		
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}


	/**
	 * 新增积分帐户
	 */
	@Override
	public String addPointAcct(String json) throws Exception {
		try {
			//参数校验(必填)
			Map<String,Object> jsonObj =JSONObject.parseObject(json);
//			jsonObj.put("pointAcctId", dao.getPK("point_acct"));
			jsonObj.put("pointAcctId",Global.getPK());
			dao.insert("InsertPointAcct",jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointAcct",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "新增积分账户成功", result); 
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 修改积分帐户
	 */
	@Override
	public String modifyPointAcct(String json, String pointAcctId) throws Exception {
		try {
			Map<String,Object> jsonObj=JSONObject.parseObject(json);
			jsonObj.put("pointAcctId", pointAcctId);
			dao.update("UpdatePointAcct", jsonObj);
			Map<String, Object> result = dao.queryForOne("QueryPointAcct",jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "修改积分账户成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除积分帐户
	 */
	@Override
	public String delPointAcct(String json, String pointAcctId) throws Exception {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("pointAcctId", pointAcctId);
			dao.delete("DeletePointAcct", param);
			return Tools.buildResponseData(Tools.SUCCESS, "删除积分账户成功", null);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

	/**
	 * 查询积分帐户列表
	 */
	@Override
	public String queryPointAcctList(String json) throws Exception {
		try {
			//支持dubbo和rest调用，判断依据json是否为null或者空
			Map<String,Object>  jsonObj = null;
			if(Tools.isNull(json)) {
				jsonObj=Tools.getParamsFromUrl();
			}else {
				jsonObj=Tools.String2Map(json);
			}
			List<Map<String, Object>> result = dao.query("QueryPointAcctList", jsonObj);
			return Tools.buildResponseData(Tools.SUCCESS, "查询积分账户列表成功", result);
		}catch(Exception e) {
			return Tools.buildResponseData(Tools.FAILED, e.getMessage(), null);
		}
	}

}
