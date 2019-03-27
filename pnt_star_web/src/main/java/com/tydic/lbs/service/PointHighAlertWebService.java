package com.tydic.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;

import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointHighAlertService;

@Service("pointHighAlertWebService")
public class PointHighAlertWebService extends BaseService {

    PointHighAlertService pointHightAlertServiceImpl = (PointHighAlertService) SpringBeanUtil
            .getBean("pointHighAlertServiceImpl");

    /**
     * 获取高额预警表格
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void getHightAlertTable(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> rtnMap) {
        String latn_id = request.getParameter("LATN_ID");
        if (null == latn_id) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入本地网"));
            return;
        }
        String point_limit = request.getParameter("POINT_LIMIT");
        if (null == point_limit) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入核对状态"));
            return;
        }
        String acc_num = request.getParameter("ACC_NUM");
        if (null == acc_num) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入用户号码"));
            return;
        }
        JSONObject json = new JSONObject();
        json.put("LATN_ID", latn_id);
        json.put("POINT_LIMIT", point_limit);
        json.put("ACC_NUM", acc_num);
        try {
            buildResponse(pointHightAlertServiceImpl.getHightAlertTable(json.toJSONString()), rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("获取高额预警表单失败"));
        }
    }

    /**
     * 高额预警核对
     * 
     * @param request
     * @param response
     * @param rtnMap
     */

    public void checkHighAlert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        String cust_id = request.getParameter("CUST_ID");
        if (null == cust_id) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入用户ID"));
            return;
        }
        String point_limit = request.getParameter("POINT_LIMIT");
        if (null == point_limit) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入核对状态"));
            return;
        }
        String point_limit_reason = request.getParameter("POINT_LIMIT_REASON");
        if (null == point_limit_reason) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("请传入核对状态说明"));
            return;
        }
        JSONObject json = new JSONObject();
        json.put("CUST_ID", cust_id);
        json.put("POINT_LIMIT", point_limit);
        json.put("POINT_LIMIT_REASON", point_limit_reason);
        try {
            buildResponse(pointHightAlertServiceImpl.checkHighAlert(json.toJSONString()), rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("核对失败"));
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
        String point_limit = request.getParameter("POINT_LIMIT");
        if (point_limit == null || "".equals(point_limit)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("未选择核对状态！"));
            return;
        }
        String acc_num = request.getParameter("ACC_NUM");
        if (point_limit == null || "".equals(acc_num)) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("未输入用户号码！"));
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
        json.put("POINT_LIMIT", point_limit);
        json.put("ACC_NUM", acc_num);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try {
            filePath = pointHightAlertServiceImpl.exportForm(json.toJSONString(), table_title, realPath);
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
