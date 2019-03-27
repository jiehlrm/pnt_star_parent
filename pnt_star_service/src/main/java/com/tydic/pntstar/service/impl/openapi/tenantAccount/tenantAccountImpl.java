package com.tydic.pntstar.service.impl.openapi.tenantAccount;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.dao.CommonDBDaoPAY;
import com.tydic.pntstar.service.impl.openapi.PointCommonComponent;
import com.tydic.pntstar.service.openapi.tenantAccount.tenantAccountService;
import com.tydic.pntstar.util.ExcelExportUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Service("tenantAccountServiceImpl")
public class tenantAccountImpl implements tenantAccountService{
	@Autowired
    private PointCommonComponent pointCommonComponent;
	private CommonDBDaoPAY dao = (CommonDBDaoPAY) SpringBeanUtil.getBean("dbdaoPAY");
	
	@Override
	public String querytenantAccountList(String json) throws Exception {
		return query(json, "QUERY_TENANT_ACCOUNT", "QUERY_TENANT_ACCOUNT_COUNT");
	}
	
	@Override
	public String querypayChannelList(String json) throws Exception {
		return querySelect(json, "QUERY_PAY_CHANNEL_2" );
	}
	
	@Override
	public String querysignTypeList(String json) throws Exception {
		return querySelect(json, "QUERY_SIGN_TYPE" );
	}
	
	private String query(String json, String sql1, String sql2) {
		Map<String,Object> jsonObj=new HashMap<>();
		if(Tools.isNull(json)) {
			jsonObj=Tools.getParamsFromUrl();//rest
		}else{
		    jsonObj =JSONObject.parseObject(json);	
		}
		String count="";//查询总数
		List<Map<String,Object>> data=null;//返回数据集合
		Map<String,Object> result =new HashMap<>();//构建返回对象
		count=dao.queryForOne(sql2, jsonObj).get("count").toString();
        data=dao.query(sql1, jsonObj);
        result.put("count", count);
        result.put("data", data);
        return JSON.toJSONString(result);
    }
	
	private String querySelect(String param, String sql) {
        Map<String, Object> sqlParam = Tools.getParamsFromUrl();
        List<Map<String, Object>> resultList = dao.query(sql, sqlParam);
        return JSON.toJSONString(resultList);
    }

	@Override
	public String addtenantAccount(String json) throws Exception {
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
        dao.insert("INSERT_TENANT_ACCOUNT", jsonObj);
        return "success";
	}

	@Override
	public String querypayUserList(String json) throws Exception {
		return querySelect(json, "QUERY_PAY_USER" );
	}

	@Override
	public String querypayMethodList(String json) throws Exception {
		return querySelect(json, "QUERY_PAY_METHOD" );
	}

	@Override
	public String querytradeTypeList(String json) throws Exception {
		return querySelect(json, "QUERY_TRADE_TYPE" );
	}

	@Override
	public String querypayTypeList(String json) throws Exception {
		return querySelect(json, "QUERY_PAY_TYPE" );
	}

	@Override
	public String querypaySerialList(String json) throws Exception {
		return query(json, "QUERY_PAY_SERIAL", "QUERY_PAY_SERIAL_COUNT");
	}

	@Override
	public String pay(String json) throws Exception {
		Map<String,Object> jsonObj=JSONObject.parseObject(json);	
		String PAY_NODE_ID=dao.queryForOne("QUERY_PAY_NODE_BY_METHOD", jsonObj).get("PAY_NODE_ID").toString();
		jsonObj.put("PAY_NODE_ID", PAY_NODE_ID);
        dao.insert("INSERT_PAY_SERIAL", jsonObj);
        // 获取HttpServletRequest对象
        HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
        // 获取目录地址
        String fileDir = "/home/ctest2/pointQuery/";
//        String fileDir = request.getSession().getServletContext().getRealPath("/") + "\\";
        // 生成附件
        Map<String, String> excelPath = handleExportFile(fileDir, jsonObj);
        // 保存附件信息到表中 
		addFileListInfo(request, excelPath, jsonObj);
        return "success";
	}
	
	/**
	 * 
	* @Title: handleExportFile  
	* @Description:  生成附件  
	* @param request
	* @param jsonObj
	* @param @return    参数  
	* @return Map    返回类型  
	* @throws
	 */
	private Map<String, String> handleExportFile(String fileDir, Map<String, Object> jsonObj) {
		Map<String,String> resultMap = new HashMap<>();
		// 获取内部内容数据列表
		List<Map<String,Object>> innerContentList = getContentDataList(jsonObj, "inner");
        // 获取内部表头数据列表
        List<Map<String,Object>> innerHeadList = getHeadListData("内部流水ID");
        // 导出内部流水生成的excel文件
        resultMap.put("innerPath", fileDir + ExcelExportUtil.exportExcel(innerContentList, JSON.toJSONString(innerHeadList), fileDir));
        // 获取外部内容数据列表
        List<Map<String,Object>> outerContentList = getContentDataList(jsonObj, "outer");
        // 获取外部表头数据列表
        List<Map<String,Object>> outerHeadList = getHeadListData("外部流水ID");
        // 导出外部流水生成的excel文件
        resultMap.put("outerPath", fileDir + ExcelExportUtil.exportExcel(outerContentList, JSON.toJSONString(outerHeadList), fileDir));
		return resultMap;
	}
	
	/**
	 * 
	* @Title: getContentDataList  
	* @Description: 获取内容数据列表  增加字段的话可以直接在方法内添加
	* @param @param jsonObj
	* @param @param type
	* @param @return    参数  
	* @return List<Map<String,Object>>    返回类型  
	* @throws
	 */
	private List<Map<String, Object>> getContentDataList(Map<String, Object> jsonObj, String type) {
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,Object> dataMap = new HashMap<>(); 
        if("outer".equals(type)) {
        	dataMap.put("PAY_SERIAL_ID", Integer.valueOf(jsonObj.get("PAY_SERIAL_ID").toString()) + 1);
        	dataMap.put("AMOUNT", jsonObj.get("REAL_AMOUNT"));
        	dataMap.put("OBJ_TYPE", "支付渠道");
        } else {
        	dataMap.put("PAY_SERIAL_ID", jsonObj.get("PAY_SERIAL_ID"));
        	dataMap.put("AMOUNT", jsonObj.get("AMOUNT"));
        	dataMap.put("OBJ_TYPE", "支付使用系统");
        }
        dataMap.put("PAY_TYPE_ID", jsonObj.get("PAY_TYPE_ID"));
        dataMap.put("COMMODITY_NAME", "充值缴费");
        dataMap.put("COMMODITY_DESC", "话费充值");
        dataMap.put("ACCOUNT_ID", 1190348645);
        dataMap.put("REGION_ID", "安徽省合肥市");
        dataList.add(dataMap);
		return dataList;
	}

	/**
	 * 
	* @Title: getHeadListData  
	* @Description: 获取表头数据列表  
	* @param @param name
	* @param @return    参数  
	* @return List<Map<String,Object>>    返回类型  
	* @throws
	 */
	private List<Map<String,Object>> getHeadListData(String name) {
		List<Map<String,Object>> headlist = new ArrayList<>();
        Map<String,Object> headMap = new HashMap<>(); 
        headMap.put("name", name);
        headMap.put("id", "PAY_SERIAL_ID");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "交易类型标识");
        headMap.put("id", "PAY_TYPE_ID");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "金额");
        headMap.put("id", "AMOUNT");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "商品名称");
        headMap.put("id", "COMMODITY_NAME");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "商品描述");
        headMap.put("id", "COMMODITY_DESC");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "对象类型");
        headMap.put("id", "OBJ_TYPE");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "帐户标识");
        headMap.put("id", "ACCOUNT_ID");
        headlist.add(headMap);
        headMap = new HashMap<>();
        headMap.put("name", "归属区域");
        headMap.put("id", "REGION_ID");
        headlist.add(headMap);
		return headlist;
	}

	/**
	 * 
	* @Title: addFileListInfo  
	* @Description: 保存附件信息到表中  生成的有两个文件 所以进行遍历处理 
	* @param @param request
	* @param @param pathMap
	* @param @param innerId    参数  
	* @return void    返回类型  
	* @throws
	*/
	private void addFileListInfo(HttpServletRequest request, Map<String, String> pathMap, Map<String, Object> jsonObj) {
		if(pathMap.size() > 0) {
			Map<String,Object> fileInfoMap = null;
			Map<String,Object> recordMap = null;
			// 遍历map
			for(Map.Entry<String, String> entry : pathMap.entrySet()) {
				File file = new File(entry.getValue());
				// 首先判断父级目录是否存在 
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();  
				}
				if(file.exists()) {
					fileInfoMap = new HashMap<String, Object>();
					recordMap = new HashMap<String, Object>();
					// 内部路径和外部路径处理不一样
					if("innerPath".equals(entry.getKey())) {
						fileInfoMap.put("SEND_IP", request.getServerName());
						fileInfoMap.put("SEND_PORT", request.getServerPort());
						fileInfoMap.put("SEND_PATH", entry.getValue());
						fileInfoMap.put("DIRECTION", 2);
						recordMap.put("PAY_SERIAL_ID", jsonObj.get("PAY_SERIAL_ID").toString());
					} else {
						fileInfoMap.put("GATHER_IP", request.getServerName());
						fileInfoMap.put("GATHER_PORT", request.getServerPort());
						fileInfoMap.put("GATHER_PATH", entry.getValue());
						fileInfoMap.put("DIRECTION", 1);
						recordMap.put("PAY_SERIAL_NBR", Integer.valueOf(jsonObj.get("PAY_SERIAL_ID").toString()) + 1);
					}
					fileInfoMap.put("PROTOCAL_ID", dao.queryForOne("QUERY_CHECK_FILE_PATH", jsonObj).get("PROTOCAL_ID"));
					dao.insert("INSERT_CHECK_FILE_PATH", fileInfoMap);
					dao.insert("INSERT_GATHER_FILE_PATH_ATTR", fileInfoMap);
					// 封装数据添加信息到对帐文件列表表中
					fileInfoMap.put("FILE_NAME", file.getName());
					fileInfoMap.put("FILE_PATH", entry.getValue());
					dao.insert("INSERT_CHECK_FILE_LIST", fileInfoMap);
					// 封装数据并保存到对帐文件记录表中
					recordMap.put("FILE_ID", fileInfoMap.get("FILE_ID"));
					recordMap.put("AMOUNT", jsonObj.get("AMOUNT"));
					recordMap.put("PAY_TYPE_ID", jsonObj.get("PAY_TYPE_ID"));
					dao.insert("INSERT_CHECK_FILE_RECORD", recordMap);
				}
			}
			// 根据节点Id获取对帐汇总标识
			Map<String, Object> idMap = dao.queryForOne("GET_CHECK_TOTAL_ID", jsonObj);
			String checkTotalId = (idMap == null) ? "" : idMap.get("CHECK_TOTAL_ID").toString();
			// 封装checkTotalMap数据
			Map<String,Object> checkTotalMap = new HashMap<String, Object>();
			checkTotalMap.put("DIFF_AMOUNT", Math.abs(Integer.parseInt(jsonObj.get("REAL_AMOUNT").toString())-Integer.parseInt(jsonObj.get("AMOUNT").toString())));
			checkTotalMap.put("TOTAL_AMOUNT", jsonObj.get("REAL_AMOUNT"));
			checkTotalMap.put("PAY_NODE_ID", jsonObj.get("PAY_NODE_ID"));
			if(Integer.parseInt(jsonObj.get("REAL_AMOUNT").toString())-Integer.parseInt(jsonObj.get("AMOUNT").toString()) == 0) {
				checkTotalMap.put("DIFF_RECORD", 0);
			}else {
				checkTotalMap.put("DIFF_RECORD", 1);
			}
			if(StringUtils.isEmpty(checkTotalId)) {
				// 插入到pay_node_check_total表中
				dao.insert("INSERT_PAY_NODE_CHECK_TOTAL", checkTotalMap);
				// 插入到check_diff表中
				Map<String,Object> checkDiffMap = new HashMap<String, Object>();
				checkDiffMap.put("PAY_SERIAL_ID", jsonObj.get("PAY_SERIAL_ID"));
				checkDiffMap.put("USER_AMOUNT", jsonObj.get("AMOUNT"));
				checkDiffMap.put("CHANNEL_AMOUNT", jsonObj.get("REAL_AMOUNT"));
				checkDiffMap.put("OPER_SERIAL_NBR", Integer.valueOf(jsonObj.get("PAY_SERIAL_ID").toString()) + 1);
				checkDiffMap.put("CHECK_TOTAL_ID", checkTotalMap.get("CHECK_TOTAL_ID"));
				checkDiffMap.put("PAY_TYPE_ID", jsonObj.get("PAY_TYPE_ID"));
				checkDiffMap.put("RECORD_ID_A", dao.queryForOne("QUERY_CHECK_FILE_RECORD", jsonObj).get("RECORD_ID"));
				dao.insert("INSERT_CHECK_DIFF", checkDiffMap);
			} else {
				dao.update("UPDATE_PAY_NODE_CHECK_TOTAL", checkTotalMap);
				// 插入到check_diff表中
				Map<String,Object> checkDiffMap = new HashMap<String, Object>();
				checkDiffMap.put("PAY_SERIAL_ID", jsonObj.get("PAY_SERIAL_ID"));
				checkDiffMap.put("USER_AMOUNT", jsonObj.get("AMOUNT"));
				checkDiffMap.put("CHANNEL_AMOUNT", jsonObj.get("REAL_AMOUNT"));
				checkDiffMap.put("OPER_SERIAL_NBR", Integer.valueOf(jsonObj.get("PAY_SERIAL_ID").toString()) + 1);
				checkDiffMap.put("CHECK_TOTAL_ID", checkTotalId);
				checkDiffMap.put("PAY_TYPE_ID", jsonObj.get("PAY_TYPE_ID"));
				checkDiffMap.put("RECORD_ID_A", dao.queryForOne("QUERY_CHECK_FILE_RECORD", jsonObj).get("RECORD_ID"));
				dao.insert("INSERT_CHECK_DIFF", checkDiffMap);
			}
			
		}
	}
}
