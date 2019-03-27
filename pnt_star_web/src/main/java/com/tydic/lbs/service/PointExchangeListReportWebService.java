package com.tydic.lbs.service;

import java.util.HashMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;

import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointExchangeListReportService;

@Service("pointExchangeListReportService")
public class PointExchangeListReportWebService extends BaseService {

    private PointExchangeListReportService pointExchangeListReportServiceImpl = (PointExchangeListReportService) SpringBeanUtil
            .getBean("pointExchangeListReportServiceImpl");

    /**
     * 获取本地网信息
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getLan(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        try {
            String result = pointExchangeListReportServiceImpl.getLan();
            buildResponse(result, rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("查询本地网信息失败"));
        }
    }

    /**
     * 获取兑换清单报表网页表单
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getListReport(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        String latn_id = request.getParameter("LATN_ID");
        String account_period = request.getParameter("ACCOUNT_PERIOD");
        if (null == account_period) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入账期"));
            return;
        }
        String pageNumer = request.getParameter("pageNumber");
        if (null == pageNumer) {
            pageNumer = "0";
        }
        String pageSize = request.getParameter("pageSize");
        if (null == pageSize) {
            pageSize = "10";
        }
        JSONObject json = new JSONObject();
        json.put("LATN_ID", latn_id);
        json.put("ACCOUNT_PERIOD", account_period);
        json.put("PAGEINDEX", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
        json.put("PAGESIZE", Integer.parseInt(pageSize));
        try {
            buildResponse(pointExchangeListReportServiceImpl.getListReport(json.toJSONString()), rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("获取客户表单信息失败"));
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
        String latn_id = request.getParameter("LATN_ID");
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
        json.put("LATN_ID", latn_id);
        json.put("ACCOUNT_PERIOD", account_period);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try {
            filePath = pointExchangeListReportServiceImpl.exportForm(json.toJSONString(), table_title, realPath);
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

    /**
     * json 转 map
     * 
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(JSONObject json) {
        if (json == null) {
            return new HashMap<String, Object>(1);
        }
        Map<String, Object> map = new HashMap<String, Object>((int) (json.size() / 0.75 + 1));
        Set<Entry<String, Object>> entrySet = json.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
