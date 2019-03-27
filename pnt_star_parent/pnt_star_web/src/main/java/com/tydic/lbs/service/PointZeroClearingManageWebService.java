package com.tydic.lbs.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.web.PointClearService;
@Service("pointZeroClearingManageWebService")
public class PointZeroClearingManageWebService extends BaseService{
	private PointClearService pointClearService =(PointClearService) SpringBeanUtil.
			getBean("pointClearServiceImpl");
	/**
	 * 获取积分清零信息表格
	 * 
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getPointClearTable(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		json.put("PAGEINDEX", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("PAGESIZE", Integer.parseInt(pageSize));
		json.put("objValue", request.getParameter("objValue"));
		json.put("objType", request.getParameter("objType"));
		json.put("latnId", request.getParameter("latnId"));
		json.put("monthId", request.getParameter("monthId"));
		json.put("clearEventType", request.getParameter("clearEventType"));
		try {
			String result = pointClearService.queryPointClear(json.toJSONString());
			JSONObject resJson = JSONObject.parseObject(result);
			buildResponse(result, rtnMap);
			if("0".equals(resJson.get("resultCode"))){
				rtnMap.put("resultCode", new String("00000"));
				//rtnMap.put("resultMsg", new String("获取积分清零信息表格信息成功"));
			}else{
				rtnMap.put("resultCode", new String("10000"));
				rtnMap.put("resultMsg", resJson.get("resultMsg"));
			}
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("获取积分清零信息表格信息失败"));
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
		json.put("objValue", request.getParameter("objValue"));
		json.put("objType", request.getParameter("objType"));
		json.put("latnId", request.getParameter("latnId"));
		json.put("monthId", request.getParameter("monthId"));
		json.put("clearEventType", request.getParameter("clearEventType"));
		String tableTitle = request.getParameter("tableTitle");
		json.put("tableTitle",tableTitle);
		String realPath = request.getSession().getServletContext().getRealPath("/");
        String filePath = "";
        try {
            filePath = pointClearService.exportPointClear(json.toJSONString(), tableTitle, realPath);
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
		data.put("data",  json.get("dataList"));
		data.put("total",json.get("total"));
		rtnMap.put("data",data);
		rtnMap.put("total",json.get("total"));
	}
}
