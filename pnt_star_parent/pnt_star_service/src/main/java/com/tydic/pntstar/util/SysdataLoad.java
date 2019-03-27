package com.tydic.pntstar.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;
import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.entity.AppInfoEntity;
import com.tydic.pntstar.entity.FlowDefEntity;
import com.tydic.pntstar.entity.FlowServRelEntity;
import com.tydic.pntstar.entity.ServInfDefEntity;
import com.tydic.pntstar.entity.StructFieldDef;

public class SysdataLoad {

	private static final Logger logger = LoggerFactory.getLogger(SysdataLoad.class);
	private static int freshFlag = 0; // 0:不刷新 1:准备刷新 2:刷新中 3：刷新完成

	private static HashMap<String, FlowDefEntity> flowDefMap = new HashMap<String, FlowDefEntity>(); // 流程定义
	private static HashMap<String, FlowDefEntity> flowServDefMap = new HashMap<String, FlowDefEntity>(); // 流程定义
	private static LinkedHashMultimap<String, FlowServRelEntity> servFlowEditMap = LinkedHashMultimap.create();// 流程编排定义
	private static HashMap<String, ServInfDefEntity> servInfDefMap = new HashMap<String, ServInfDefEntity>();// 业务能力定义
	private static LinkedHashMultimap<String, StructFieldDef> structFieldDefMap = LinkedHashMultimap.create();// 消息结构定义
	private static LinkedHashMultimap<String, StructFieldDef> structFieldUpDefMap = LinkedHashMultimap.create();// 消息结构定义	
	private static HashMap<Long,String> currentServiceName = new HashMap<Long, String>(); //存储当前请求pid对应的serviceName
 
	private static HashMap<String, FlowDefEntity> flowDefmap2 = new HashMap<String, FlowDefEntity>();
	
	//系统配置 business.properties
	private static HashMap<Object, Object> sysCfg = new HashMap<Object, Object>();
	
	//加载应用和IP
	private static Map<String, AppInfoEntity> appMap = new HashMap<String,AppInfoEntity>();
	//加载应用与服务关系表
	private static Map<String, Set<String>> appServRelMap = new HashMap<String, Set<String>>();
	public SysdataLoad() throws Exception  {
		SysdataLoad.load();
	}
	public static int getFreshFlag() {
		return freshFlag;
	}

	public synchronized static int getFreshFlagSyn() {
		return freshFlag;
	}

	public synchronized static void setFreshFlag(int freshFlag) {
		SysdataLoad.freshFlag = freshFlag;
	}
	
	public static HashMap<String, FlowDefEntity> getFlowDefMap() {
		return flowDefMap;
	}

	public static void setFlowDefMap(HashMap<String, FlowDefEntity> flowDefMap) {
		SysdataLoad.flowDefMap = flowDefMap;
	}
	//当前线程pid对应ServiceName
	
	public static Map<Long,String> getCurrentServiceName() {
		return currentServiceName;
	}

	public static void setCurrentServiceName(HashMap<Long,String> currentServiceName) {
		SysdataLoad.currentServiceName = currentServiceName;
	}
	
	
	public static HashMap<String, FlowDefEntity> getFlowServDefMap() {
		return flowServDefMap;
	}

	public static void setFlowServDefMap(HashMap<String, FlowDefEntity> flowServDefMap) {
		SysdataLoad.flowServDefMap = flowServDefMap;
	}

	public static LinkedHashMultimap<String, FlowServRelEntity> getServFlowEditMap() {
		return servFlowEditMap;
	}

	public static void setServFlowEditMap(LinkedHashMultimap<String, FlowServRelEntity> servFlowEditMap) {
		SysdataLoad.servFlowEditMap = servFlowEditMap;
	}

	public static HashMap<String, ServInfDefEntity> getServInfDefMap() {
		return servInfDefMap;
	}

	public static LinkedHashMultimap<String, StructFieldDef> getStructFieldDefMap() {
		return structFieldDefMap;
	}

	public static void setStructFieldDefMap(LinkedHashMultimap<String, StructFieldDef> structFieldDefMap) {
		SysdataLoad.structFieldDefMap = structFieldDefMap;
	}

	public static void setServInfDefMap(HashMap<String, ServInfDefEntity> servInfDefMap) {
		SysdataLoad.servInfDefMap = servInfDefMap;
	}

	public static LinkedHashMultimap<String, StructFieldDef> getStructFieldUpDefMap() {
		return structFieldUpDefMap;
	}

	public static void setStructFieldUpDefMap(LinkedHashMultimap<String, StructFieldDef> structFieldUpDefMap) {
		SysdataLoad.structFieldUpDefMap = structFieldUpDefMap;
	}	

	public static HashMap<Object, Object> getSysCfg() {
		return sysCfg;
	}

	public static Map<String, AppInfoEntity> getAppMap() {
		return appMap;
	}
	public static Map<String, Set<String>> getAppServRelMap() {
		return appServRelMap;
	}
	public static void load() throws Exception{
		try {
			flowInfoLoad();
			flowEditLoad();
			servInfDefLoad();
			structFieldDefLoad();
			appInfoLoad();
			appServRelLoad();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}
	
	// 复制
	public void copy(String sqlCode) {
		if ("ALL".equals(sqlCode)) {
			flowDefmap2.putAll(flowDefMap);

		} else if ("STATIC_FLOWSERVICE".equals(sqlCode)) {
			flowDefmap2.putAll(flowDefMap);
		} else if ("STATIC_STRUCTDEF".equals(sqlCode)) {
		}
	}

	// 刷新
	public void reLoad(String sqlCode) throws Exception {
		if ("ALL".equals(sqlCode)) {
			load();
		} else if ("LOAD_FLOWDEF".equals(sqlCode)) {
			flowInfoLoad();
		} else if ("STATIC_STRUCTDEF".equals(sqlCode)) {
			structFieldDefLoad();
		}
	}

	// 清理
	public void clear(String sqlCode) {
		if ("ALL".equals(sqlCode)) {
			flowDefmap2.clear();

		} else if ("STATIC_FLOWSERVICE".equals(sqlCode)) {
			flowDefmap2.clear();
		} else if ("STATIC_STRUCTDEF".equals(sqlCode)) {

		}
	}

	/**
	 * 流程原子服务
	 * 
	 * @throws Exception
	 */
	static void flowInfoLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2) {
			flowDefMap.clear();
			flowServDefMap.clear();
		}

		if (flowDefMap.size() > 0)
			return;
		
		List<Map<String, Object>> flowList = null;
		try {
			CommonDBDao dao = (CommonDBDao) SpringBeanUtil
					.getBean("dbdao");
			flowList = dao.query("LOAD_SERV_FLOW_DEF", new HashMap<String,Object>());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}		
		
		for (Map<String, Object> data : flowList) {
			FlowDefEntity flowServiceEntity = new FlowDefEntity();
			flowServiceEntity.setServId(StringCommon.trimNull(data.get("SERV_ID")+""));
			flowServiceEntity.setServName(StringCommon.trimNull(data.get("SERV_NAME")+""));
			flowServiceEntity.setSenceId(StringCommon.trimNull(data.get("SCENE_ID")+""));
			flowServiceEntity.setiStructId(StringCommon.trimNull(data.get("I_STRUCT_ID")+""));
			flowServiceEntity.setoStructId(StringCommon.trimNull(data.get("O_STRUCT_ID")+""));

			flowDefMap.put(flowServiceEntity.getServName() + "|" + flowServiceEntity.getSenceId(), flowServiceEntity);
			flowServDefMap.put(flowServiceEntity.getServId(), flowServiceEntity);
		}

		logger.info("load flow  info " + flowList.size() + " records!");
	}

	static void flowEditLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2) {
			servFlowEditMap.clear();
		}

		if (servFlowEditMap.size() > 0)
			return;
		CommonDBDao dao = (CommonDBDao) SpringBeanUtil
				.getBean("dbdao");

		List<Map<String, Object>> flowEditList = dao.query("LOAD_SERV_FLOW_EDIT_DEF", null);
				
		for (Map<String, Object> data : flowEditList) {
			FlowServRelEntity flowEditEntity = new FlowServRelEntity();
			flowEditEntity.setServEditId(StringCommon.trimNull(data.get("SERV_EDIT_ID")+""));
			flowEditEntity.setServId(StringCommon.trimNull(data.get("SERV_ID")+""));
			flowEditEntity.setServInfId(StringCommon.trimNull(data.get("SERV_INF_ID")+""));
			flowEditEntity.setStep(StringCommon.trimNull(data.get("STEP")+""));
			flowEditEntity.setOutPutFlag(StringCommon.trimNull(data.get("OUTPUT_FLAG")+""));

			servFlowEditMap.put(flowEditEntity.getServId(), flowEditEntity);
		}

		logger.info("load flow edit  info " + servFlowEditMap.size() + " records!");
	}

	static void servInfDefLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2) {
			servInfDefMap.clear();
		}

		if (servInfDefMap.size() > 0)
			return;
		
		CommonDBDao dao = (CommonDBDao) SpringBeanUtil
				.getBean("dbdao");
		List<Map<String, Object>> servInfDefList = dao.query("LOAD_SERV_INTERFACE_DEF", null);
		for (Map<String, Object> data : servInfDefList) {
			ServInfDefEntity servInfDefEntity = new ServInfDefEntity();
			servInfDefEntity.setServInfId(StringCommon.trimNull(data.get("SERV_INF_ID")+""));
			servInfDefEntity.setClassName(StringCommon.trimNull(data.get("CLASS_NAME")+""));
			servInfDefEntity.setiStructId(StringCommon.trimNull(data.get("IN_STRUCT_ID")+""));
			servInfDefEntity.setoStructId(StringCommon.trimNull(data.get("OUT_STRUCT_ID")+""));
			servInfDefEntity.setState(StringCommon.trimNull(data.get("STATE")+""));

			servInfDefMap.put(servInfDefEntity.getServInfId(), servInfDefEntity);
		}

		logger.info("load server interface  info " + servInfDefList.size() + " records!");
	}

	/**
	 * 消息结构定义
	 * @throws Exception 
	 */
	static void structFieldDefLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2){
			structFieldDefMap.clear();
			structFieldUpDefMap.clear();
		}
		if (structFieldDefMap.size() > 0)
			return;
		
		CommonDBDao dao = (CommonDBDao) SpringBeanUtil
				.getBean("dbdao");
		// 消息结构定义
		List<Map<String, Object>> dataList = dao.query("LOAD_STRUCT_FIELD_DEF", null);

		LinkedHashMultimap<String, StructFieldDef> tmpStructFieldDefMap = LinkedHashMultimap.create();

		for (Map<String, Object> data : dataList) {
			StructFieldDef StructFieldDef = new StructFieldDef();
			StructFieldDef.setStructId(StringCommon.trimNull(data.get("STRUCT_ID")+""));
			StructFieldDef.setStructFieldId(StringCommon.trimNull(data.get("STRUCT_FIELD_ID")+""));
			StructFieldDef.setFieldName(StringCommon.trimNull(data.get("FIELD_NAME")+""));
			StructFieldDef.setUpFieldId(StringCommon.trimNull(data.get("UP_FIELD_ID")+""));
			StructFieldDef.setFieldLayer(StringCommon.trimNull(data.get("FIELD_LAYER")+""));
			StructFieldDef.setFieldType(StringCommon.trimNull(data.get("FIELD_TYPE")+""));
			StructFieldDef.setMultiFlag(StringCommon.trimNull(data.get("MULTI_FLAG")+""));
			StructFieldDef.setFieldSeq(StringCommon.trimNull(data.get("FIELD_SEQ")+""));
			StructFieldDef.setCheckType(Integer.valueOf(data.get("CHECK_TYPE")+""));
			StructFieldDef.setRequestFiledName(StringCommon.trimNull(data.get("REQUEST_FIELD_NAME")+""));
			StructFieldDef.setDefaultValue(StringCommon.trimNull(data.get("DEFAULT_VALUE")+""));
			StructFieldDef.setValueKey(StringCommon.trimNull(data.get("VALUE_KEY")+""));

			tmpStructFieldDefMap.put(data.get("STRUCT_ID")+"", StructFieldDef);
			structFieldDefMap.put(data.get("STRUCT_ID")+"", StructFieldDef);
		}

		for (String structId : tmpStructFieldDefMap.keySet()) {
			Set<StructFieldDef> structFieldDefSet = tmpStructFieldDefMap.get(structId);
			for (StructFieldDef structFieldDef : structFieldDefSet) {
				if ("-1".equals(structFieldDef.getUpFieldId())) {
					Set<StructFieldDef> childSet = findChild(structFieldDefSet, structFieldDef);
					structFieldDef.setChildStructFieldDef(childSet);
					structFieldUpDefMap.put(structId, structFieldDef);
				}
			}
		}

		logger.info("load structFieldDef " + structFieldDefMap.size() + " records!");
	}

	private static Set<StructFieldDef> findChild(Set<StructFieldDef> structFieldDefSet,
			StructFieldDef upStructFieldDef) {

		Set<StructFieldDef> childSet = new HashSet<StructFieldDef>();
		for (StructFieldDef structFieldDef : structFieldDefSet) {
			if (structFieldDef.getUpFieldId().equals(upStructFieldDef.getStructFieldId())) {
				Set<StructFieldDef> subChildSet = findChild(structFieldDefSet, structFieldDef);
				structFieldDef.setChildStructFieldDef(subChildSet);
				childSet.add(structFieldDef);
			}
		}
		return childSet;
	}
	/**
	 * 加载应用
	 * @throws Exception
	 */
	static void appInfoLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2){
			appMap.clear();
		}
		if (appMap.size() > 0)
			return;
		
		CommonDBDao dao = (CommonDBDao) SpringBeanUtil
				.getBean("dbdao");
		// 消息结构定义
		List<Map<String, Object>> dataList = dao.query("LOAD_APP_INFO", null);

		for (Map<String, Object> data : dataList) {
			AppInfoEntity appInfo = new AppInfoEntity();
			appInfo.setApp_id(StringCommon.trimNull(data.get("APP_ID")+""));
			appInfo.setApp_name(StringCommon.trimNull(data.get("APP_NAME")+""));
			appInfo.setApp_secret(StringCommon.trimNull(data.get("APP_SECRET")+""));
			appInfo.setIf_crypt(StringCommon.trimNull(data.get("IF_CRYPT")+""));
			appInfo.setIp_end(StringCommon.trimNull(data.get("IP_END")+""));
			appInfo.setIp_start(StringCommon.trimNull(data.get("IP_START")+""));
			appInfo.setIs_white(StringCommon.trimNull(data.get("IS_WHITE")+""));
			appMap.put(data.get("APP_ID")+"", appInfo);
		}

		logger.info("load appinfo " + appMap.size() + " records!");
	}
	
	/**
	 * 加载应用
	 * @throws Exception
	 */
	static void appServRelLoad() throws Exception {

		if (SysdataLoad.getFreshFlag() == 2){
			appServRelMap.clear();
		}
		if (appServRelMap.size() > 0)
			return;
		
		CommonDBDao dao = (CommonDBDao) SpringBeanUtil
				.getBean("dbdao");
		// 消息结构定义
		List<Map<String, Object>> dataList = dao.query("LOAD_APP_SERV_REL_INFO", null);
		Set<String> servNameSet = new HashSet<String>();
		for (Map<String, Object> data : dataList) {
			String appId = data.get("APP_ID")+"";
			if(appServRelMap.size() <= 0){
				servNameSet.add(data.get("SERV_NAME")+"");
				appServRelMap.put(appId, servNameSet);
			}else{
				if(appServRelMap.containsKey(appId)){
					servNameSet.add(data.get("SERV_NAME")+"");
				}else{
					servNameSet = new HashSet<String>();
					servNameSet.add(data.get("SERV_NAME")+"");
					appServRelMap.put(appId, servNameSet);
				}
				
			}
		}

		logger.info("load appinfo " + appServRelMap.size() + " records!");
	}
	
}
