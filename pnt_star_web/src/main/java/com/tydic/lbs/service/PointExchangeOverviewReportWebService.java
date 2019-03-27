package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointExchOverviewReportService;

@Service("pointExchangeOverviewReportService")
public class PointExchangeOverviewReportWebService extends BaseService {

    private PointExchOverviewReportService pointExchOverviewReportImpl = (PointExchOverviewReportService) SpringBeanUtil
            .getBean("pointExchOverviewReportImpl");
    /**
	 * 获取兑换积分总览报表
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getPointExchangeOverviewReportTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("latnId", request.getParameter("latnId"));
		json.put("monthId", request.getParameter("monthId"));
		json.put("qryType", request.getParameter("qryType"));
		try {
			String result = pointExchOverviewReportImpl.queryPointExchange(json.toJSONString());
			JSONObject resJson = JSONObject.parseObject(result);
			buildResponse(result, rtnMap);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
			}else{
				rtnMap.put("resultCode", new String("10000"));
				rtnMap.put("resultMsg", resJson.get("resultMsg"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取兑换总览信息失败"));
		}
	}
	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void exportForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("latnId", request.getParameter("latnId"));
		json.put("monthId", request.getParameter("monthId"));
		String tableTitle = request.getParameter("tableTitle");
		json.put("qryType", request.getParameter("qryType"));
		json.put("tableTitle",tableTitle);
		String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try{
        	filePath = pointExchOverviewReportImpl.exportPointExchange(json.toJSONString(), tableTitle, realPath);
        }catch(Exception e){
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
		data.put("data",  json.get("dataList"));
		data.put("total",1000);
		rtnMap.put("data",data);
		//rtnMap.put("total",json.get("total"));
    }
}
