package com.tydic.pntstar.service.impl.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.impl.province.ProvinceServiceImpl;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.service.web.PointClearService;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/** 
* @Title: PointClearServiceImpl.java 
* @Package com.tydic.pntstar.service.impl.web 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2019年1月5日 下午2:11:08 
* @version V1.0 
*/
@Service
public class PointClearServiceImpl implements PointClearService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
	@Autowired
	private DefaultBusinessService defaultBusinessService;
	/** 
	 * 1：积分清理记录查询
	 */
	@Override
	public String queryPointClear(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try {
			 //1.解析获取参数
            param=defaultBusinessService.getParam(json);
			//2.参数校验  必填校验
			defaultBusinessService.validate(param);
			//3.获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4业务执行
			List<Map<String, Object>> pointClearList=dao.query("QueryPointClearList", param);//查询清零记录信息
			if(Tools.isNull(pointClearList)) {
				throw new Exception("未查积分清零数据");
			}
			Map<String,Object> map=dao.queryForOne("QueryPointClearListCount", param);//获取查询总数
			if(Tools.isNull(map)) {
				throw new Exception("查询积分清零总数报错");
			}
			return Tools.buildResponseData(Tools.SUCCESS,"积分清零记录查询成功",JSONArray.parseArray(JSON.toJSONString(pointClearList)),Integer.valueOf(map.get("total").toString()));
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"积分清零记录查询失败",e.getMessage());
		}
	}
	
   
	/**
	 * 1：导出积分清零记录成excle文件
	 * 2：json  查询参数，table_title  列表名   根路径
	 */
	@Override
	public String exportPointClear(String json,String table_title,String realPath) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try {
			 //1.解析获取参数
            param=defaultBusinessService.getParam(json);
			//2.参数校验  必填校验
			defaultBusinessService.validate(param);
			//3.获取客户id
			String custId=defaultBusinessService.getCustId(param);
			param.put("custId", custId);
			//4业务执行
			List<Map<String, Object>> pointClearList=dao.query("QueryPointClearList", param);//查询清零记录信息
			//生成excle文件
			String filePath=ExcelExportUtil.exportExcel(pointClearList, table_title, realPath);
			//return Tools.buildResponseData(Tools.SUCCESS,"积分清零记录文件生成成功",filePath);
			return filePath;
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"积分清零记录文件生成失败",e.getMessage());
		}
	}

}
