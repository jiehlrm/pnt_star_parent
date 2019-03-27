package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.StarStatReportService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

/**
 * 星级统计报表实现类
 * 
 * @author hxc
 *
 */
@Service("starStatReportServiceImpl")
public class StarStatReportServiceImpl implements StarStatReportService {

    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

    @Autowired
    private PointCommonComponent pointCommonComponent;

    @Override
    public String getStarStatReport(String param) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object account_period = json.get("ACCOUNT_PERIOD");
        if (null == account_period) {
            return JSONArray.toJSONString(new JSONArray());
        }
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("ACCOUNT_PERIOD", account_period);
        // 循环的sql
        List<Map<String, Object>> resultList = dao.query("QUERY_POINT_STAR_STAT_FORM_1", sqlParam);
        if (resultList == null || resultList.size() == 0) {
            return pointCommonComponent.buildRetrun(PointConstant.failCode, "暂无数据，查询失败", null);
        }
        Map<String, Object> result = dao.queryForOne("QUERY_POINT_STAR_STAT_FORM_2", sqlParam);
        resultList.add(result);
        JSONObject resObj = new JSONObject();
        resObj.put("data", CommonUtil.list2JsonArray(resultList));
        return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
    }

    /**
     * 表单导出
     */
    @Override
    public String exportForm(String param, String table_title, String realPath) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object account_period = json.get("ACCOUNT_PERIOD");
        //查询数据
        List<Map<String, Object>> resultList = getStarStatReportData(account_period, null, null);
        //表单导出
        String filePath = ExcelExportUtil.exportExcel(resultList, table_title, realPath);
        return filePath;
    }
    
    /**
     * 获取表单数据
     */
    private List<Map<String, Object>> getStarStatReportData(Object account_period, Object page_index, Object page_size) throws Exception {
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("ACCOUNT_PERIOD", account_period);
        // 循环的sql
        List<Map<String, Object>> resultList = dao.query("QUERY_POINT_STAR_STAT_FORM_1", sqlParam);
        if (resultList == null || resultList.size() == 0) {
            return null;
        }
        Map<String, Object> result = dao.queryForOne("QUERY_POINT_STAR_STAT_FORM_2", sqlParam);
        resultList.add(result);
        return resultList;
    }
    
}
