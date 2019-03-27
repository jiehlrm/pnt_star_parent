package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointOverviewReportService;

@Service("pointOverviewReportService")
public class PointOverviewReportWebService extends BaseService{
	private PointOverviewReportService pointOverviewReportServiceImpl = (PointOverviewReportService) SpringBeanUtil
            .getBean("pointOverviewReportServiceImpl");
	/**
     * 获取报表网页表单
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getListReport(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        String latn_id = request.getParameter("LATN_ID");
        String account_period = request.getParameter("ACCOUNT_PERIOD");
        String qry_type=request.getParameter("QRY_TYPE");
        if (null == account_period) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入账期"));
            return;
        }
     
        JSONObject json = new JSONObject();
        json.put("latnId", latn_id);
        json.put("monthId", account_period);
        json.put("qryType",qry_type);
        try {
            buildResponse(pointOverviewReportServiceImpl.getListReport(json.toJSONString()), rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("获取客户表单信息失败"));
        }
    }
    /**
     * 获取兑换清单报表网页表单总数
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getListReportTotal(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
 
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
        String qry_type=request.getParameter("QRY_TYPE");
        if (account_period == null || "".equals(account_period)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("未选择账期！"));
            return;
        }
        //表单标题
        String table_title = request.getParameter("TABLE_TITLE");
        if (table_title == null || "".equals(table_title)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("标题数组为空！"));
            return;
        }
        json.put("latnId", latn_id);
        json.put("monthId", account_period);
        json.put("qryType",qry_type);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try {
            filePath = pointOverviewReportServiceImpl.exportForm(json.toJSONString(), table_title, realPath);
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
        JSONObject data= new JSONObject();
        data.put("data",  json.get("data"));
		data.put("total",1000);
        rtnMap.put("data", data);
    }
	
}
