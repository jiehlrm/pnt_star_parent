package com.tydic.pntstar.service.impl.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.entity.StructFieldDef;
import com.tydic.pntstar.util.MapUtil;
import com.tydic.pntstar.util.StringCommon;

public class FlowServReturnJson {
	
	/***
	 * 根据参数结构生产响应报文
	 * @param structFieldList
	 * @param inputParams 原始入参
	 * @param shareStore 原子服务的结果
	 * @return
	 */
	public static Map   genReturnJson(Set<StructFieldDef> structFieldList,JSONObject inputParams,Map<String, Object> shareStore) {
		Map  resultMap=new HashMap<>();
		genReturnMap(structFieldList,inputParams, shareStore,resultMap);
		return resultMap;
	}
	
	/***
	 * 递归调用，生成需要的json对象
	 * @param structFieldList
	 * @param inputParams
	 * @param curShareStore
	 * @param curLevelMap
	 */
	private static void genReturnMap(Set<StructFieldDef> structFieldList,JSONObject inputParams,Map<String, Object> curShareStore,Map<String,Object> curLevelMap) {
		if (structFieldList==null||structFieldList.size()<1) {
			return;
		}
		for(StructFieldDef  structFieldDef   :structFieldList) {
			//先获取当前节点名称
			String jsonKey=structFieldDef.getFieldName();
			String filedType=structFieldDef.getFieldType();
			//获取当前节点的取值key
			String jsonKeyValue=structFieldDef.getValueKey();
			String defaultValue=structFieldDef.getDefaultValue();
			
			//场景1 原始变量，如果当前节点取值key不为空，并且节点类型不是空，并且当前类型不是聚合类型则认为是子节点了，去当前的map中查找数据
			if(!StringCommon.isNull(jsonKeyValue)&&!StringCommon.isNull(filedType)&&!"100".equals(structFieldDef.getFieldType())&&!"101".equals(structFieldDef.getFieldType())) {
				 String resultVlaue=MapUtil.getValueFromMap(curShareStore, jsonKeyValue);
				 //如果中间变量中没找到，则使用入参数据中找
				 if(StringCommon.isNull(resultVlaue)) {
					 resultVlaue=MapUtil.getValueFromMap(inputParams, jsonKeyValue);
				 }
				 if(StringCommon.isNull(resultVlaue)){
					 //如果入参变量中也没找到，则使用默认值
					 resultVlaue=defaultValue;
				 }
				 //放入结果
				 //TODO
				 /*****1-整型；
					2-小数；
					3-布尔类型；
					4-字符串；根据数据类型放入不同的对象，对于报文节点类型要求比较严格的****/
				 curLevelMap.put(jsonKey, resultVlaue);
			}else 
			//场景2，数组，如果当前节点取值key不为空，并且节点类型101数组,则直接从共享空间取值
			if(!StringCommon.isNull(jsonKeyValue)&&!StringCommon.isNull(filedType)&&"100".equals(structFieldDef.getFieldType())) {
				List<Map<String, Object>>  curDataList=(List<Map<String, Object>>) curShareStore.get(jsonKeyValue);
				
				if(curDataList==null||curDataList.size()<1) {
					//如果没数据的话则放置一个空的数组
					curLevelMap.put(jsonKey, new ArrayList<>());
				}else {
					//如果有数据的话则根据子结构体获取数组对象各个字段
					ArrayList<Map<String, Object>> resultDataList=new ArrayList<>();
					for (int i = 0; i < curDataList.size(); i++) {
						Map<String, Object> curRow=curDataList.get(i);
						//初始化一个map用来保存当前层级的结果
						Map<String, Object> curRowResult=new HashMap<>();
						//递归，获取子结构体对应的数据
						genReturnMap(structFieldDef.getChildStructFieldDef(),inputParams, curRow, curRowResult);
						//放入当前行的记录
						resultDataList.add(curRowResult);
					}
					//当前list到jsonMap中
					curLevelMap.put(jsonKey, resultDataList);
				}
			}
			else
			//场景3，对象
			//如果当前类型是对象，则说明有子节点，遍历所有子节点
			if("101".equals(structFieldDef.getFieldType())) {
				//初始化一个map用来保存当前层级的结果
				Map<String, Object> curRowResult=new HashMap<>();
				//递归，获取子结构体对应的数据
				if(!StringCommon.isNull(jsonKeyValue)) {
					//如果配置的取值参数则直接根据key取值
					Map curRowParam=MapUtil.getMapValueFromMap(curShareStore,jsonKeyValue);
					genReturnMap(structFieldDef.getChildStructFieldDef(),inputParams, curRowParam, curRowResult);
				}else {
					genReturnMap(structFieldDef.getChildStructFieldDef(),inputParams, curShareStore, curRowResult);
				}
				//当前list到jsonMap中
				curLevelMap.put(jsonKey, curRowResult);
			 }
			

		}
	}

		
}
