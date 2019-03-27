package com.tydic.pntstar.service.impl.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.constant.PointConstant;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.util.CommonUtil;
import com.tydic.pntstar.util.SpringBeanUtil;

/**
 * 积分公共方法实现类
 * 
 * @author longinus
 *
 */
@Component("pointCommonComponent")
public class PointCommonComponent {

	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	/**
	 * 获取本地网信息
	 */
	public String getLan() throws Exception {
		// TODO Auto-generated method stub
//		List<Map<String, Object>> resultList = dao.query("QUERY_PONIT_LAND", new HashMap<String, Object>());
//		JSONArray jsonArr = CommonUtil.list2JsonArray(resultList);
		return buildRetrun(PointConstant.successCode, new String("本地网查询成功"), null);
	}

	/**
	 * 循环查询的公共方法
	 * 
	 * @param sqlNameArr
	 * @param initParam
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectMultiCommon(String[] sqlNameArr, Map<String, Object> initParam) throws Exception {
		if (sqlNameArr.length == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		// 1.第一个必须是查询的主sql
		List<Map<String, Object>> firstList = dao.query(sqlNameArr[0], initParam);
		if (firstList == null || firstList.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		// 2.循环查询主sql的数据然后进行循环查询
		for (Map<String, Object> resultMap : firstList) {
			initParam.putAll(resultMap);
			for (int i = 1; i < sqlNameArr.length; i++) {
				List<Map<String, Object>> subResMap = dao.query(sqlNameArr[i], initParam);
				if(subResMap == null || subResMap.size() == 0){
					continue;
				}
				Map<String,Object> map = subResMap.get(0);
				if(map == null){
					continue;
				}
				resultMap.putAll(subResMap.get(0));
				initParam.putAll(subResMap.get(0));
			}
		}
		return firstList;
	}
	/**
     * 循环查询的公共方法,当某一步执行的sql返回结果为空时将整条记录移除，保证查询结果正确
     * 
     * @param sqlNameArr
     * @param initParam
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> selectMultiCommon2(String[] sqlNameArr, Map<String, Object> initParam) throws Exception {
        if (sqlNameArr.length == 0) {
            return new ArrayList<Map<String, Object>>();
        }
        // 1.第一个必须是查询的主sql
        List<Map<String, Object>> firstList = dao.query(sqlNameArr[0], initParam);
        if (firstList == null || firstList.size() == 0) {
            return new ArrayList<Map<String, Object>>();
        }
        // 2.循环查询主sql的数据然后进行循环查询
        for (int index = 0; index < firstList.size(); index ++) {
            Map<String, Object> resultMap = firstList.get(index);
            initParam.putAll(resultMap);
            for (int i = 1; i < sqlNameArr.length; i++) {
                List<Map<String, Object>> subResMap = dao.query(sqlNameArr[i], initParam);
                if(subResMap == null || subResMap.size() == 0){
                    firstList.remove(index);
                    //控制不再继续执行下一个sql
                    i = sqlNameArr.length;
                    //执行逻辑上下一组sql查询
                    index--;
                    continue;
                }
                resultMap.putAll(subResMap.get(0));
                initParam.putAll(subResMap.get(0));
            }
        }
        return firstList;
    }
	
	/**
	 * 构造返回字符串
	 * 
	 * @param code
	 * @param msg
	 * @param obj
	 * @return
	 */
	public String buildRetrun(String code, String msg, Object obj) {
		JSONObject resObj = new JSONObject();
		resObj.put("resultCode", code);
		resObj.put("resultMsg", msg);
		resObj.put("data", obj);
		return resObj.toJSONString();
	}

}
