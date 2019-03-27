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
import com.tydic.pntstar.service.openapi.PointExchangeListReportService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

/**
 * 兑换清单报表实现类
 * 
 * @author longinus
 *
 */
@Service("pointExchangeListReportServiceImpl")
public class PointExchangeListReportServiceImpl implements PointExchangeListReportService{

    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

    @Autowired
    private PointCommonComponent pointCommonComponent;
    
    /**
     * 获取本地网信息
     */
    @Override
    public String getLan() throws Exception {
        return pointCommonComponent.getLan();
    }
    
    /**
     * 获取清单报表
     */
    @Override
    public String getListReport(String param) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object latn_id = json.get("LATN_ID");
        if (null == latn_id) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object account_period = json.get("ACCOUNT_PERIOD");
        if (null == account_period) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object page_index = json.get("PAGEINDEX");
        Object page_size =  json.get("PAGESIZE");
        List<Map<String, Object>> resultList = getListReportData(latn_id, account_period, page_index, page_size);
        if (resultList == null || resultList.size() == 0) {
            return pointCommonComponent.buildRetrun(PointConstant.failCode, "暂无数据,查询失败", null);
        }
        Map<String, Object> resultTotal = getListReportTotal(latn_id, account_period);
        String total = "0";
        if (resultTotal.get("TOTAL") != null) {
            total = resultTotal.get("TOTAL").toString();
        }
        JSONObject resObj = new JSONObject();
        resObj.put("total", total);
        resObj.put("data", CommonUtil.list2JsonArray(resultList));
        // 变为string返回
        return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
    }
    
    /**
     * 表单导出
     */
    @Override
    public String exportForm(String param, String table_title, String realPath) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object latn_id = json.get("LATN_ID");
        Object account_period = json.get("ACCOUNT_PERIOD");
        //查询数据
        List<Map<String, Object>> resultList = getListReportData(latn_id, account_period, null, null);
        //表单导出
        String filePath = ExcelExportUtil.exportExcel(resultList, table_title, realPath);
        return filePath;
    }
    
    /**
     * 获取表单数据
     */
    private List<Map<String, Object>> getListReportData(Object latn_id, Object account_period, Object page_index, Object page_size) throws Exception {
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("LATN_ID", latn_id);
        sqlParam.put("ACCOUNT_PERIOD", account_period);
        if (page_index != null) {
            sqlParam.put("PAGEINDEX", page_index);
        }
        if (page_size != null) {
            sqlParam.put("PAGESIZE", page_size);
        }
        // 获取数据
        List<Map<String, Object>> resultList = dao.query("QUERY_POINT_EXCHANGE_LIST_REPORT_1", sqlParam);
        return resultList;
    }
    
    private Map<String, Object> getListReportTotal(Object latn_id, Object account_period) throws Exception {
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("LATN_ID", latn_id);
        sqlParam.put("ACCOUNT_PERIOD", account_period);
        Map<String, Object> resultTotal = dao.queryForOne("QUERY_POINT_EXCHANGE_LIST_REPORT_C", sqlParam);
        return resultTotal;
    }
    
}
