package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointOverviewReportService;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
/**
 * 积分总览报表实现类
 * 
 * @author zhouman
 *
 */
@Service("pointOverviewReportServiceImpl")
public class PointOverviewReportServiceImpl implements PointOverviewReportService{
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

    @Autowired
    private PointCommonComponent pointCommonComponent;
    /**
     * 获取总览报表
     */
	@Override
	public String getListReport(String param) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		
        List<Map<String, Object>> resultList=dao.query("QueryPointReportScoreOverview", json);
        Map<String, Object> result=dao.queryForOne("QueryPointReportScoreOverviewSum", json);
        resultList.add(result);
        // 变为string返回
        return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resultList);
	}
	/**
     * 表单导出
     */
	@Override
	public String exportForm(String param, String table_title, String realPath) throws Exception {
		JSONObject json = JSONObject.parseObject(param);
		List<Map<String, Object>> resultList=dao.query("QueryPointReportScoreOverview", json);
        Map<String, Object> result=dao.queryForOne("QueryPointReportScoreOverviewSum", json);
        resultList.add(result);
        //表单导出
        String filePath = ExcelExportUtil.exportExcel(resultList, table_title, realPath);
        return filePath;
	}
    
}
