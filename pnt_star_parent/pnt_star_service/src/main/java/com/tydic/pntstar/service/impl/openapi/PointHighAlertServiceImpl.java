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
import com.tydic.pntstar.service.openapi.PointHighAlertService;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;
/**
 * 积分高额预警实现类
 * 
 * @author hxc
 *
 */
@Service("pointHighAlertServiceImpl")
public class PointHighAlertServiceImpl implements PointHighAlertService {

    private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
    
    @Autowired
    private PointCommonComponent pointCommonComponent;
    
    @Override
    public String getHightAlertTable(String param) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object latn_id = json.get("LATN_ID");
        if (null == latn_id) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object point_limit = json.get("POINT_LIMIT");
        if (null == point_limit) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object acc_num = json.get("ACC_NUM");
        if (null == acc_num) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("LATN_ID", json.get("LATN_ID"));
        param1.put("ACC_NUM", json.get("ACC_NUM"));
        List<Map<String, Object>> resultList = getHightAlertData(latn_id, point_limit, acc_num);
        Map<String, Object> resultMap = dao.queryForOne("QUERY_POINT_HIGHT_ALERT_FORM_1", param1);
        if (resultMap == null || resultMap.size() == 0) {
            return pointCommonComponent.buildRetrun(PointConstant.failCode, "对不起，暂无查询记录", null);
        }
        Map<String, Object> param2 = new HashMap<String, Object>();
        param2.put("POINT_LIMIT", json.get("POINT_LIMIT"));
        param2.put("CUST_ID", resultMap.get("CUST_ID"));
        Map<String, Object> resultList2 = dao.queryForOne("QUERY_POINT_HIGHT_ALERT_FORM_C", param2);
        JSONObject resObj = new JSONObject();
        String total = "0";
        if (resultList2 != null) {
			if (resultList2.get("TOTAL") != null)
				total = resultList2.get("TOTAL").toString();
		}
		resObj.put("total",total);
		resObj.put("data", CommonUtil.list2JsonArray(resultList));
		return pointCommonComponent.buildRetrun(PointConstant.successCode, "", resObj);
    }

    @Override
    public String checkHighAlert(String param) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object cust_id = json.get("CUST_ID");
        if (null == cust_id) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object point_limit = json.get("POINT_LIMIT");
        if (null == point_limit) {
            return JSONArray.toJSONString(new JSONArray());
        }
        Object point_limit_reason = json.get("POINT_LIMIT_REASON");
        if (null == point_limit_reason) {
            return JSONArray.toJSONString(new JSONArray());
        }
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("CUST_ID", cust_id);
        sqlParam.put("POINT_LIMIT", point_limit);
        sqlParam.put("POINT_LIMIT_REASON", point_limit_reason);
        try {
            dao.update("QUERY_POINT_HIGHT_ALERT_FORM_8", sqlParam);
        } catch (Exception e) {
            e.printStackTrace();
            return Tools.buildResponseData(Tools.FAILED,"核对失败", null);
        }
        return Tools.buildResponseData(Tools.SUCCESS,"核对成功", null);
    }

    /**
     * 表单导出
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public String exportForm(String param, String table_title, String realPath) throws Exception {
        JSONObject json = JSONObject.parseObject(param);
        Object latn_id = json.get("LATN_ID");
        Object point_limit = json.get("POINT_LIMIT");
        Object acc_num = json.get("ACC_NUM");
        List<Map<String, Object>> resultList = getHightAlertData(latn_id, point_limit, acc_num);
        String filePath = ExcelExportUtil.exportExcel(resultList, table_title, realPath);
        return filePath;
    }

    /**
     * 获取高额预警数据
     * 
     * @param request
     * @param response
     * @param rtnMap
     */
    private List<Map<String, Object>> getHightAlertData(Object latn_id, Object point_limit, Object acc_num) throws Exception{
        // 初始参数赋值
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("LATN_ID", latn_id);
        sqlParam.put("POINT_LIMIT", point_limit);
        sqlParam.put("ACC_NUM", acc_num);
        // 循环的sql
        String[] sql = {"QUERY_POINT_HIGHT_ALERT_FORM_1", "QUERY_POINT_HIGHT_ALERT_FORM_2", 
                        "QUERY_POINT_HIGHT_ALERT_FORM_3"};
        List<Map<String, Object>> resultList = pointCommonComponent.selectMultiCommon2(sql, sqlParam);
        for (Map<String, Object> map : resultList) {
            //一个用户对应多个账户，此处获取其用户下所有账户对应的消费积分
            //先获取积分账户id
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("CUST_ID", map.get("CUST_ID"));
            List<Map<String, Object>> point_acct_id_list = dao.query("QUERY_POINT_HIGHT_ALERT_FORM_4", param1);
            //获取积分账户对应balance的id
            Map<String, Object> param2 = new HashMap<String, Object>();
            param2.put("paramList", point_acct_id_list);
            List<Map<String, Object>> point_acct_balance_id_list = dao.query("QUERY_POINT_HIGHT_ALERT_FORM_5", param2);
            //获取消费积分
            Map<String, Object> param3 = new HashMap<String, Object>();
            param3.put("paramList", point_acct_balance_id_list);
            //月消费积分
            Map<String, Object> amount_month = dao.queryForOne("QUERY_POINT_HIGHT_ALERT_FORM_6", param3);
            if (amount_month != null) {
                map.put("AMOUNT_MONTH", amount_month.get("AMOUNT_MONTH"));
            } else {
                map.put("AMOUNT_MONTH", "0");
            }
            //三年消费积分
            Map<String, Object> amount_year = dao.queryForOne("QUERY_POINT_HIGHT_ALERT_FORM_7", param3);
            if (amount_year != null) {
                map.put("AMOUNT_THREE_YEAR", amount_year.get("AMOUNT_THREE_YEAR"));
            } else {
                map.put("AMOUNT_THREE_YEAR", "0");
            }
        }
        return resultList;
    }
}
