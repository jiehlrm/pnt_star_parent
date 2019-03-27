package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.StarStatReportService;

@Service("starStatReportWebService")
public class StarStatReportWebService extends BaseService {

    StarStatReportService starStatReportServiceImpl = (StarStatReportService) SpringBeanUtil
            .getBean("starStatReportServiceImpl");
    
    /**
     * 获取星级统计报表表格
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getStarStatReport(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        String account_period = request.getParameter("ACCOUNT_PERIOD");
        if (null == account_period) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入月份"));
            return;
        }
        JSONObject json = new JSONObject();
        json.put("ACCOUNT_PERIOD", account_period);
        try {
            buildResponse(starStatReportServiceImpl.getStarStatReport(json.toJSONString()), rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("获取星级统计报表失败"));
        }
    }
    
    /**
     * 导出表单
     * 
     * @param request
     * @param rtnMap
     */
    public void exportForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        JSONObject json = new JSONObject();
        String account_period = request.getParameter("ACCOUNT_PERIOD");
        if (account_period == null || "".equals(account_period)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("未选择账期！"));
            return;
        }
        // 表单标题
        String table_title = request.getParameter("TABLE_TITLE");
        if (table_title == null || "".equals(table_title)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("标题数组为空！"));
            return;
        }
        json.put("ACCOUNT_PERIOD", account_period);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try {
            filePath = starStatReportServiceImpl.exportForm(json.toJSONString(), table_title, realPath);
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("导出失败"));
            return;
        }
        rtnMap.put("filePath", filePath);
        rtnMap.put("resultCode", new String("00000"));
        rtnMap.put("resultMsg", new String("导出成功"));
    }
    
    /**
     * 构建成功的返回
     * 
     * @param result
     * @param rtnMap
     */
    private void buildResponse(String result, Map<String, Object> rtnMap) {
        JSONObject json = JSONObject.parseObject(result);
        rtnMap.put("resultCode", json.get("resultCode"));
        rtnMap.put("resultMsg", json.get("resultMsg"));
        rtnMap.put("data", json.get("data"));
    }
}
