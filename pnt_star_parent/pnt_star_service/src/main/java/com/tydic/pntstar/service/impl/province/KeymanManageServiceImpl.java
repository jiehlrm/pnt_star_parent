package com.tydic.pntstar.service.impl.province;

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
import com.tydic.pntstar.service.province.KeymanManageService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/**
 * 关键人管理实现类
 * 
 * @author D11050
 *
 */
@Service("keymanManageServiceImpl")
public class KeymanManageServiceImpl implements KeymanManageService{
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	private static final Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);
	
	@Autowired
	private DefaultBusinessService defaultBusinessService;
	
	
	
	/* 
	 * 获取关键人信息
	 */
	@Override
	public String getKeyman(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			if(Tools.isNull(json.get("pointKeymanId"))){
				throw new Exception("关键人标识不能为空");
			}
			//2：查询信息
			List<Map<String, Object>> list = dao.query("QUERY_KEYMAN", json);
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人详情查询成功", list.get(0),null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人详情查询失败",e.getMessage(),null);
		}
	}
	
	@Override
	public String getKeymanList(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
            if(Tools.isNull(json)) {
            	throw new Exception("参数不能为空");
            }
			if(!Tools.isNull(json.get("timeRange"))){
				String timeRange=json.get("timeRange").toString();
				String beginTime = timeRange.substring(0, timeRange.indexOf(" - "));
				String endTime = timeRange.substring(timeRange.indexOf(" - ")+3, timeRange.length());
				json.put("beginTime", beginTime);
				json.put("endTime", endTime);
			}
			//2：查询信息
			List<Map<String, Object>> list = dao.query("QUERY_KEYMAN_LIST", json);
			if(!json.containsKey("pointKeymanId") && !Tools.isNull(list)) {
				result.put("total", dao.queryForOne("QUERY_KEYMAN_LIST_COUNT", json).get("total"));
			}
			result.put("data", list);
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人列表查询成功", result,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人列表查询失败",e.getMessage(),null);
		}
	}

	@Override
	public String adjKeymanType(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		Map<String,Object> result = new HashMap<>();//构建返回对象
		try {
			//1:获取业务参数
			json = JSONObject.parseObject(param);
			//更新关键人类型 
			dao.update("UPDATE_KEYMAN", json,true);
			json.put("recordId", dao.getPK("POINT_KEYMAN_CHANGE_RECORD"));	
			json.put("changeType",11);
			dao.insert("INSERT_KEYMAN_CHANGE_RECORD", json,true);
			dao.commit();			
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人类型修改成功",1,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人类型修改失败",e.getMessage(),null);
		}finally{
			dao.release();
		}
	}

	/* 
	 * 新增关键人
	 */
	@Override
	public String insertKeyman(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			if(Tools.isNull(json)){
				throw new Exception("参数不能为空");
			}
			Map<String,Object> paramter=new HashMap<String,Object>();
			paramter.put("pointKeymanCd",10);
			paramter.put("custId",json.get("custId"));
			//如果已经是关键人了就不能再添加了
			List<Map<String, Object>> list = dao.query("QUERY_KEYMAN", paramter);
			if(!Tools.isNull(list)) {
				throw new Exception("该客户已经是关键人了");
			}
			String pointKeymanId=dao.getPK("point_keyman");
			json.put("pointKeymanId",pointKeymanId);
			//2：业务执行
			dao.insert("INSERT_POINT_KEYMAN", json,true);//插入一条关键人记录
			String recordId=dao.getPK("POINT_KEYMAN_CHANGE_RECORD");
			json.put("recordId",recordId );
			json.put("changeType",10 );
			json.put("adjReason","新增关键人");
			json.put("beforeType","");
			json.put("afterType", json.get("pointKeymanType"));
			dao.insert("INSERT_KEYMAN_CHANGE_RECORD", json,true);//插入一条关键人类型调整记录
			dao.commit();
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人添加成功", 1,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			dao.rollback();
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人添加失败",e.getMessage(),null);
		}finally {
			dao.release();
		}
	}

	/* 
	 * 删除关键人信息
	 */
	@Override
	public String deleteKeyman(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			if(Tools.isNull(json) || Tools.isNull(json.get("pointKeymanIds"))){
				throw new Exception("关键人标识不能为空");
			}
			//2：业务执行			
			dao.update("DELETE_POINT_KEYMAN", json);
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人删除成功",1,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人删除失败",e.getMessage(),null);
		}
	}

	/** 
	 * 下转获取关键人资料
	 * 传递参数  accNum   用户号码  
	 */
	@Override
	public String getKeymanInfo(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			if(Tools.isNull(json) ){
				throw new Exception("用户号码不能为空");
			}
			//2：业务校验，是否是积分客户
			List<Map<String,Object>> result=dao.query("QUERY_POINT_KEYMAN_LOAD", json);//下转获取加载信息
			if(Tools.isNull(result)) {
				throw new Exception("未查到客户信息");
			}
			String custId=result.get(0).get("custId").toString();//获取客户标识
			json.put("custId", custId);
			
			List<Map<String,Object>> list=dao.query("QueryPointCustStatus", json);
			if(Tools.isNull(list)) {
				throw new Exception("该客户不是积分客户");
			}		
			return Tools.buildResponseDataWeb(PointConstant.successCode, "获取资料成功",result.get(0) ,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "获取资料失败",e.getMessage(),null);
		}
	}

	/* 
	 * 更新
	 */
	@Override
	public String updateKeyman(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			if(Tools.isNull(json) ){
				throw new Exception("参数不能为空");
			}
			//2：业务校验，是否是积分客户
	        dao.update("UPDATE_KEYMAN", json);
			return Tools.buildResponseDataWeb(PointConstant.successCode, "更新关键人信息成功",1,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "更新关键人信息失败",e.getMessage(),null);
		}
	}

	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.province.KeymanManageService#getKeymanChangeList(java.lang.String)
	 */
	@Override
	public String getKeymanChangeList(String param) throws Exception {
		Map<String,Object> json = null;//请求参数
		Map<String,Object> result =null;//构建返回对象
		List<Map<String, Object>> list=null;
		int pageIndex=0;
		try {
			//1:解析参数
			json=defaultBusinessService.getParam(param);
			pageIndex=(int) json.get("pageIndex");
			json.put("pageIndex", 0);
			if(Tools.isNull(json) || Tools.isNull(json.get("pointKeymanId"))){
				throw new Exception("关键人标识不能为空");
			}
			Map<String,Object> keyman=dao.queryForOne("QUERY_KEYMAN_LIST", json);
			//2：业务执行
			json.put("pageIndex", pageIndex);
			Map<String,Object> count=dao.queryForOne("QUERY_KEYMAN_CHANGE_LIST_COUNT", json);
			json.put("prodInstId", keyman.get("prodInstId"));//产品实例标识
			json.put("custCd", keyman.get("custCd"));
			json.put("custName", keyman.get("custName"));
			json.put("accNbr", keyman.get("accNbr"));
			int total= count.get("total")==null?0:Integer.valueOf(count.get("total").toString());	
			if(total>0) {
				result = new HashMap<>();
				list= dao.query("QUERY_KEYMAN_CHANGE_LIST", json);				
				result.put("total", total);
				result.put("data", list);
			}
			return Tools.buildResponseDataWeb(PointConstant.successCode, "关键人类型变更记录查询成功", result,null);
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseDataWeb(PointConstant.failCode, "关键人类型变更记录查询失败",e.getMessage(),null);
		}
	}

	 
	/**
	 * 1：导出关键人信息列表成excle文件
	 * 2：json  查询参数，table_title  列表名   根路径
	 */
	@Override
	public String exportKeymanList(String json,String table_title,String realPath) throws Exception {
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try {
			//1:解析参数
			param=defaultBusinessService.getParam(json);
            if(Tools.isNull(param)) {
            	throw new Exception("参数不能为空");
            }
			if(!Tools.isNull(param.get("timeRange"))){
				String timeRange=param.get("timeRange").toString();
				String beginTime = timeRange.substring(0, timeRange.indexOf(" - "));
				String endTime = timeRange.substring(timeRange.indexOf(" - ")+3, timeRange.length());
				param.put("beginTime", beginTime);
				param.put("endTime", endTime);
			}
			//2业务执行
			List<Map<String, Object>> keymanList=dao.query("QUERY_KEYMAN_LIST", param);//查询关键人信息列表
			//生成excle文件
			String filePath=ExcelExportUtil.exportExcel(keymanList, table_title, realPath);
			return filePath;
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"关键人信息文件生成失败",e.getMessage());
		}
	}

	

}
