package com.tydic.pntstar.service.impl.openapi;

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
import com.tydic.pntstar.service.openapi.PointsClearOverviewReportService;
import com.tydic.pntstar.service.utils.DefaultBusinessService;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;
@Service("pointsClearOverviewReportServiceImpl")
public class PointsClearOverviewReportServiceImpl implements PointsClearOverviewReportService {
	private static final Logger logger = LoggerFactory.getLogger(ProvinceServiceImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
    @Autowired
	private DefaultBusinessService defaultBusinessService;
	@Override
	public String queryPointsClearOverviewReport(String json) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try{
			//1.解析获取参数
            param=defaultBusinessService.getParam(json);
			List<Map<String, Object>> pointExchangeList=dao.query("QueryPointsClearOverviewReport", param);//查询兑换积分总览记录信息
			pointExchangeList.add(dao.queryForOne("QueryPointsClearOverviewReportSum", param));
			if(Tools.isNull(pointExchangeList)) {
				throw new Exception("未查到待清零积分总览记录");
			}
			return Tools.buildResponseData(Tools.SUCCESS,"待清零积分总览记录查询成功",JSONArray.parseArray(JSON.toJSONString(pointExchangeList)));
		}catch(Exception e){
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"待清零积分总览记录查询失败",e.getMessage());
		}
	}

	@Override
	public String exportPointsClearOverviewReport(String json, String table_title, String realPath) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> param = null;//请求参数
		Map<String,Object> result = new HashMap();//构建返回对象
		try {
			 //1.解析获取参数
            param=defaultBusinessService.getParam(json);
			List<Map<String, Object>> pointExchangeList=dao.query("QueryPointsClearOverviewReport", param);//查询清零记录信息
			pointExchangeList.add(dao.queryForOne("QueryPointsClearOverviewReportSum", param));
			//生成excle文件
			String filePath=ExcelExportUtil.exportExcel(pointExchangeList, table_title, realPath);
			//return Tools.buildResponseData(Tools.SUCCESS,"积分清零记录文件生成成功",filePath);
			return filePath;
		}catch(Exception e) {
			logger.info(e.getMessage(),e);
			return Tools.buildResponseData(Tools.FAILED,"待清零积分总览记录文件生成失败",e.getMessage());
		}
	}
}
