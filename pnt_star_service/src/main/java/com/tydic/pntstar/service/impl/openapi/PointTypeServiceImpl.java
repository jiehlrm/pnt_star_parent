package com.tydic.pntstar.service.impl.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.openapi.PointTypeService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

/**
 * @author hxc
 * 积分类型实现类
 */
@Service("pointTypeServiceImpl")
public class PointTypeServiceImpl implements PointTypeService {

    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

    @Autowired
    private PointCommonComponent pointCommonComponent;
    
    @Override
    public String queryPointType(String param) throws Exception {
        return query(param, "QUERY_POINT_TYPE", "QUERY_POINT_TYPE_COUNT");
    }

    @Override
    public String queryPointTypeGroup(String param) throws Exception {
        return query(param, "QUERY_POINT_TYPE_GROUP", "QUERY_POINT_TYPE_GROUP_COUNT");
    }
    
    @Override
    public String queryPointTypeGroupMbr(String param) throws Exception {
        return query(param, "QUERY_POINT_TYPE_GROUP_MBR", "QUERY_POINT_TYPE_GROUP_MBR_COUNT");
    }
    
    private String query(String param, String sql1, String sql2) {
        JSONObject json = JSONObject.parseObject(param);
        Object pageIndex = json.get("PAGEINDEX");
        Object pageSize = json.get("PAGESIZE");
        Object pointTypeGroupId = json.get("POINT_TYPE_GROUP_ID");
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("pageIndex", pageIndex);
        sqlParam.put("pageSize", pageSize);
        if (pointTypeGroupId != null) {
            sqlParam.put("pointTypeGroupId", pointTypeGroupId);
        }
        List<Map<String, Object>> resultList1 = null;
        List<Map<String, Object>> resultList2 = null;
        JSONObject resObj = new JSONObject();
        try {
            resultList1 = dao.query(sql1, sqlParam);
            resultList2 = dao.query(sql2, sqlParam);
        } catch (Exception e) {
            return pointCommonComponent.buildRetrun(PointConstant.failCode, "", resObj);
        }
        if (resultList2.get(0).get("TOTAL") != null) {
            resObj.put("total",resultList2.get(0).get("TOTAL").toString().trim());
            resObj.put("data", CommonUtil.list2JsonArray(resultList1));
            return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
        } else {
            return pointCommonComponent.buildRetrun(PointConstant.failCode, "", resObj);
        }
    }

    @Override
    public String addPointType(String param) throws Exception {
        return addOrUpdate(param);
    }

    @Override
    public String addPointTypeGroup(String param) throws Exception {
        return addOrUpdate(param);
    }

    @Override
    public String updatePointType(String param) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String updatePointTypeGroup(String param) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    private String addOrUpdate(String param) {
        JSONObject json = JSONObject.parseObject(param);
        Object type = json.get("TYPE");
        Object methodName = json.get("METHOD_NAME");
        if ("POINT_TYPE".equals(type.toString().trim())) {
            Object pointTypeCode = json.get("POINT_TYPE_CODE");
            Object pointTypeName = json.get("POINT_TYPE_NAME");
            Object pointTypeDesc = json.get("POINT_TYPE_DESC");
            Object pointTypeStatus = json.get("POINT_TYPE_STATUS");
            Map<String, Object> sqlParam = new HashMap<String, Object>();
            sqlParam.put("pointTypeCode", pointTypeCode);
            sqlParam.put("pointTypeName", pointTypeName);
            sqlParam.put("pointTypeDesc", pointTypeDesc);
            sqlParam.put("pointTypeStatus", pointTypeStatus);
            JSONObject resObj = new JSONObject();
            if ("INSERT_POINT_TYPE".equals(methodName.toString().trim())) {
                Object pointTypeId = dao.getPK("point_type");
                sqlParam.put("pointTypeId", pointTypeId);
                try {
                    dao.insert(methodName.toString().trim(), sqlParam);
                } catch (Exception e) {
                    return pointCommonComponent.buildRetrun(PointConstant.failCode, "", resObj);
                }
                resObj.put("", "");
                return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
            } else if ("UPDATE_POINT_TYPE".equals(methodName.toString().trim())) {
                Object pointTypeId = json.get("POINT_TYPE_ID");
                sqlParam.put("pointTypeId", pointTypeId);
                try {
                    dao.update(methodName.toString().trim(), sqlParam);
                } catch (Exception e) {
                    return pointCommonComponent.buildRetrun(PointConstant.failCode, "", resObj);
                }
                resObj.put("", "");
                return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
            } else {
                return null;
            }
        } else if ("POINT_TYPE_GROUP".equals(type.toString().trim())) {
            if ("INSERT_POINT_TYPE_GROUP".equals(methodName.toString().trim())) {
                Object pointTypeGroupName = json.get("POINT_TYPE_GROUP_NAME");
                Object pointTypeGroupStatus = json.get("POINT_TYPE_GROUP_STATUS");
                Object pointTypeGroupId = dao.getPK("point_type_group");
                Map<String, Object> sqlParam = new HashMap<String, Object>();
                sqlParam.put("pointTypeGroupId", pointTypeGroupId);
                sqlParam.put("pointTypeGroupName", pointTypeGroupName);
                sqlParam.put("pointTypeGroupStatus", pointTypeGroupStatus);
                JSONObject resObj = new JSONObject();
                try {
                    dao.insert(methodName.toString().trim(), sqlParam);
                } catch (Exception e) {
                    return pointCommonComponent.buildRetrun(PointConstant.failCode, "", resObj);
                }
                resObj.put("", "");
                return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
            } else if ("UPDATE_POINT_TYPE_GROUP".equals(methodName.toString().trim())) {
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
}
