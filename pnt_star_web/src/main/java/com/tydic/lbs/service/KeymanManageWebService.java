package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.province.KeymanManageService;

@Service("keymanManageWebService")
public class KeymanManageWebService extends BaseService{
	private static final Logger logger = LoggerFactory.getLogger(PointAdjustWebService.class);
	private KeymanManageService keymanManageServiceImpl = (KeymanManageService) SpringBeanUtil.getBean("keymanManageServiceImpl");
	/**
	 * 单个关键人信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getKeyman(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		try {
			String result = keymanManageServiceImpl.getKeyman(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	public void getOneKeymanbyList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		try {
			String result = keymanManageServiceImpl.getKeymanList(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("查询关键人信息失败"));
		}
	}
	/**
	 * 获取关键人列表信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getKeymanList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		//json.put("SERVICE_NAME", request.getParameter("SERVICE_NAME"));
		json.put("latnId", request.getParameter("LATN_ID"));
		json.put("custNum", request.getParameter("CUST_NUM"));
		json.put("pointKeymanType", request.getParameter("KEYMAN_TYPE"));
		json.put("timeRange", request.getParameter("TIME_RANGE"));
		//json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		
		json.put("pageIndex", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("pageSize", Integer.parseInt(pageSize));
		try {
			String result = keymanManageServiceImpl.getKeymanList(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("查询关键人信息失败"));
		}
	}
	
	/**
	 * 调整关键人类型
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void adjKeymanType(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		json.put("pointKeymanType", request.getParameter("pointKeymanType"));
		if(null!=request.getParameter("expDate")&&!"".equals(request.getParameter("expDate"))) {
			json.put("expDate", request.getParameter("expDate"));
		}
		json.put("adjReason", request.getParameter("adjReason"));
		json.put("beforeType", request.getParameter("beforeType"));
		json.put("afterType", request.getParameter("afterType"));
		try {
			String result = keymanManageServiceImpl.adjKeymanType(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	
	/**
	 * 修改关键人信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void updateKeyman(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		json.put("introducerNbr", request.getParameter("introducerNbr"));
//		if(null!=request.getParameter("expDate")&&!"".equals(request.getParameter("expDate"))) {
//			json.put("expDate", request.getParameter("expDate"));
//		}
		json.put("starLevel", request.getParameter("starLevel"));
		try {
			String result = keymanManageServiceImpl.updateKeyman(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
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
	 * 关键人变更记录查询
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getKeymanChangeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		json.put("pointKeymanId", request.getParameter("pointKeymanId"));
		String pageNumer = request.getParameter("pageNumber");
		String pageSize = request.getParameter("pageSize");
		if (null == pageNumer) {
			pageNumer = "0";
		}
		if (null == pageSize) {
			pageSize = "10";
		}
		
		json.put("pageIndex", Integer.parseInt(pageNumer) * Integer.parseInt(pageSize));
		json.put("pageSize", Integer.parseInt(pageSize));
		try {
			String result = keymanManageServiceImpl.getKeymanChangeList(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	/**
	 * 关键人删除
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void deleteKeyman(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		JSONArray json_array = new JSONArray();
        json_array = JSONArray.parseArray(request.getParameter("pointKeymanIds"));
        json.put("pointKeymanIds", json_array);
		logger.debug(json.toJSONString());
		try {
			String result = keymanManageServiceImpl.deleteKeyman(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	/**
	 * 获取关键人信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void getKeymanInfo(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
        
        json.put("accNum", request.getParameter("accNum"));
		logger.debug(json.toJSONString());
		try {
			String result = keymanManageServiceImpl.getKeymanInfo(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	/**
	 * 增加关键人信息
	 * @param request
	 * @param response
	 * @param rtnMap
	 */
	public void insertKeyman(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
		JSONObject json = new JSONObject();
		
		json.put("starLevel", request.getParameter("starLevel"));
		json.put("accNbr", request.getParameter("accNbr"));
        json.put("latn_id", request.getParameter("latn_id"));
        json.put("pointKeymanType", request.getParameter("pointKeymanType"));
        json.put("custId", request.getParameter("custId"));
        json.put("custCd", request.getParameter("custCd"));
        json.put("expDate", request.getParameter("expDate"));
        json.put("prodInstId", request.getParameter("prodInstId"));
        json.put("introducerNbr", request.getParameter("introducerNbr"));
		logger.debug(json.toJSONString());
		try {
			String result = keymanManageServiceImpl.insertKeyman(json.toJSONString());
			buildResponse(result, rtnMap);
		} catch (Exception e) {
			rtnMap.put("resultCode", new String("20000"));
			rtnMap.put("resultMsg", new String("调整关键人类型失败"));
		}
	}
	/**
	* 导出表单
    * 
    * @param request
    * @param rtnMap
    */
   public void exportKeymanList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
       JSONObject json = new JSONObject();
       json.put("latnId", request.getParameter("LATN_ID"));
		json.put("custNum", request.getParameter("CUST_NUM"));
		json.put("pointKeymanType", request.getParameter("KEYMAN_TYPE"));
		json.put("timeRange", request.getParameter("TIME_RANGE"));
       //表单标题
       String table_title = request.getParameter("TABLE_TITLE");
       if (table_title == null || "".equals(table_title)) {
           rtnMap.put("resultCode", new String("20000"));
           rtnMap.put("resultMsg", new String("标题数组为空！"));
           return;
       }
       json.put("table_title", table_title);
       String realPath = request.getSession().getServletContext().getRealPath("/");
       String filePath = "";
       try {
           filePath = keymanManageServiceImpl.exportKeymanList(json.toJSONString(), table_title, realPath);
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
	
}
