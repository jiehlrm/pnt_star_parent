package com.tydic.lbs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.lbs.frame.BaseService;
import com.tydic.lbs.util.SpringBeanUtil;
import com.tydic.pntstar.service.openapi.PointTypeService;

@Service("pointTypeService")
public class PointTypeWebService extends BaseService {

    private PointTypeService pointTypeServiceImpl = (PointTypeService) SpringBeanUtil.getBean("pointTypeServiceImpl");

    /**
     * 查询积分类型
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void queryPointType(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        query(request, response, rtnMap, "queryPointType", "积分类型");
    }

    /**
     * 查询积分类型组
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void queryPointTypeGroup(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> rtnMap) {
        query(request, response, rtnMap, "queryPointTypeGroup", "积分类型组");
    }

    /**
     * 查询积分类型组成员
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void queryPointTypeGroupMbr(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> rtnMap) {
        query(request, response, rtnMap, "queryPointTypeGroupMbr", "积分类型组成员");
    }

    /**
     * 查询的公用方法
     * 
     * @param request
     * @param response
     * @param rtnMap
     * @param method 方法名
     * @param name 代表哪一个表格 
     */
    private void query(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap,
            String method, String name) {
        String pageIndex = request.getParameter("pageNumber");
        String pageSize = request.getParameter("pageSize");
        String pointTypeGroupId = "";
        if (request.getParameter("pointTypeGroupId") != null
                && !"".equals(request.getParameter("pointTypeGroupId").trim())) {
            pointTypeGroupId = request.getParameter("pointTypeGroupId");
        }
        if (null == pageIndex) {
            pageIndex = "0";
        }
        if (null == pageSize) {
            pageSize = "10";
        }
        JSONObject json = new JSONObject();
        json.put("PAGEINDEX", Integer.parseInt(pageIndex) * Integer.parseInt(pageSize));
        json.put("PAGESIZE", Integer.parseInt(pageSize));
        try {
            String result = "";
            if ("queryPointType".equals(method)) {
                result = pointTypeServiceImpl.queryPointType(json.toJSONString());
            } else if ("queryPointTypeGroup".equals(method)) {
                result = pointTypeServiceImpl.queryPointTypeGroup(json.toJSONString());
            } else if ("queryPointTypeGroupMbr".equals(method)) {
                if (!"".equals(pointTypeGroupId)) {
                    json.put("POINT_TYPE_GROUP_ID", pointTypeGroupId);
                }
                result = pointTypeServiceImpl.queryPointTypeGroupMbr(json.toJSONString());
            } else {
                rtnMap.put("resultCode", new String("20000"));
                rtnMap.put("resultMsg", new String("查询" + name + "失败"));
            }
            buildResponse(result, rtnMap);
        } catch (Exception e) {
            rtnMap.put("resultCode", new String("20000"));
            rtnMap.put("resultMsg", new String("查询" + name + "失败"));
        }
    }

    /**
     * 新增积分类型
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void addPointType(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        addOrUpdate(request, response, rtnMap, "add", "POINT_TYPE");
    }
    
    /**
     * 新增积分类型组
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void addPointTypeGroup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        addOrUpdate(request, response, rtnMap, "add", "POINT_TYPE_GROUP");
    }
    
    /**
     * 修改积分类型
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void updatePointType(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        addOrUpdate(request, response, rtnMap, "update","POINT_TYPE");
    }
    
    /**
     * 修改积分类型组
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    public void updatePointTypeGroup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap) {
        addOrUpdate(request, response, rtnMap, "update", "POINT_TYPE_GROUP");
    }
    
    /**
     * 新增或更新的公用方法
     * 
     * @param request
     * @param response
     * @param rtnMap
     * @param method 新增还是更新
     * @param type 积分类型还是积分类型组
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> rtnMap,  String method, String type) {
        String result = "";
        String operate = "";
        if ("add".equals(method)) {
            operate = "新增";
        } else {
            operate = "修改";
        }
        String methodName = "METHOD_NAME";
        if ("POINT_TYPE".equals(type.trim())) {
            String pointTypeCode = request.getParameter("POINT_TYPE_CODE");
            String pointTypeName = request.getParameter("POINT_TYPE_NAME");
            String pointTypeDesc = request.getParameter("POINT_TYPE_DESC");
            String pointTypeStatus = request.getParameter("POINT_TYPE_STATUS");
            // 校验
            JSONObject json = new JSONObject();
            json.put("POINT_TYPE_CODE", pointTypeCode);
            json.put("POINT_TYPE_NAME", pointTypeName);
            json.put("POINT_TYPE_DESC", pointTypeDesc);
            json.put("POINT_TYPE_STATUS", pointTypeStatus);
            json.put("TYPE", "POINT_TYPE");
            try {
                if ("add".equals(method)) {
                    json.put(methodName, "INSERT_POINT_TYPE");
                    result = pointTypeServiceImpl.addPointType(json.toJSONString());
                } else if ("update".equals(method)) {
                    json.put(methodName, "UPDATE_POINT_TYPE");
                    result = pointTypeServiceImpl.updatePointType(json.toJSONString());
                }
                buildResponse(result, rtnMap);
            } catch (Exception e) {
                rtnMap.put("resultCode", new String("20000"));
                rtnMap.put("resultMsg", new String(operate + "积分类型" + "失败"));
            }
        } else if ("POINT_TYPE_GROUP".equals(type.trim())) {
            String pointTypeGroupName = request.getParameter("POINT_TYPE_GROUP_NAME");
            String pointTypeGroupStatus = request.getParameter("POINT_TYPE_GROUP_STATUS");
            // 校验
            JSONObject json = new JSONObject();
            json.put("POINT_TYPE_GROUP_NAME", pointTypeGroupName);
            json.put("POINT_TYPE_GROUP_STATUS", pointTypeGroupStatus);
            json.put("TYPE", "POINT_TYPE_GROUP");
            try {
                if ("add".equals(method)) {
                    json.put(methodName, "INSERT_POINT_TYPE_GROUP");
                    result = pointTypeServiceImpl.addPointTypeGroup(json.toJSONString());
                } else if ("update".equals(method)) {
                    json.put(methodName, "UPDATE_POINT_TYPE_GROUP");
                    result = pointTypeServiceImpl.updatePointTypeGroup(json.toJSONString());
                }
                buildResponse(result, rtnMap);
            } catch (Exception e) {
                rtnMap.put("resultCode", new String("20000"));
                rtnMap.put("resultMsg", new String("新增" + "积分类型" + "失败"));
            }
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

}
