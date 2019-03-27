package com.tydic.pntstar.service.impl.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tydic.pntstar.entity.FlowDefEntity;
import com.tydic.pntstar.entity.FlowServRelEntity;
import com.tydic.pntstar.entity.ServInfDefEntity;
import com.tydic.pntstar.entity.StructFieldDef;
import com.tydic.pntstar.service.flow.FlowService;
import com.tydic.pntstar.service.flow.ServiceAbstractInterface;
import com.tydic.pntstar.util.LogUtil;
import com.tydic.pntstar.util.MapUtil;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.SysdataLoad;

/**
 * 流程服务
 * 
 * @author apple
 *
 */
public class FlowServiceImpl implements FlowService {
	private static final Logger logger = LoggerFactory.getLogger(FlowServiceImpl.class);
	
	/*******
	 * 流程服务入口
	 * inputParams原始的请求报文
	 */
	public Map<String, Object> evRun(String traceId, String servName, String senceId, JSONObject inputParams)
			throws Exception {
		// 使用局部参数,原子服务的返回结果也要放入此内存
		HashMap<String, Object> shareStore = new HashMap<String, Object>();// 流程共享缓存区
		Long pid = Thread.currentThread().getId();
		logger.debug(LogUtil.format(traceId, "***current pid is" + pid));
		logger.debug(LogUtil.format(traceId, "CurrentServiceMap,key[" + pid + "],value[" + servName + "]"));

		// 根据流程编码获取流程定义
		FlowDefEntity flowDefEntity = SysdataLoad.getFlowDefMap().get(servName + "|" + senceId);
		if (flowDefEntity == null) {
			throw new Exception(LogUtil.format(traceId, "Not find flow def [" + servName + "]!"));
		}

		// 根据流程编码获取流程与原子服务的关系
		Set<FlowServRelEntity> flowEditList = SysdataLoad.getServFlowEditMap().get(flowDefEntity.getServId());
		if (flowEditList.size() == 0) {
			throw new Exception(LogUtil.format(traceId, "Not find flowedit under flow [" + servName + "]!"));
		}
		// 获取流程出参定义
		Set<StructFieldDef> flowStrut = SysdataLoad.getStructFieldUpDefMap().get(flowDefEntity.getoStructId());
		if (flowStrut.size() == 0) {
			throw new Exception(LogUtil.format(traceId, "Not find flow strut under flow [" + servName + "]!"));
		}
		
		
		logger.info(LogUtil.format(traceId, "****start flow[" + servName + "]****"));

		// 执行流程下各个业务能力接口
		for (FlowServRelEntity service : flowEditList) {
			// 获取原子服务对象
			ServInfDefEntity servInfDefEntity = SysdataLoad.getServInfDefMap().get(service.getServInfId());

			logger.info(LogUtil.format(traceId,
					"step[" + service.getStep() + "],serv class[" + servInfDefEntity.getClassName() + "] start !"));

			// 1、获取原子服务接口
			// 先检测是否有原子服务的bean
			if (!SpringBeanUtil.containsBean(servInfDefEntity.getClassName())) {
				logger.error(LogUtil.format(traceId, "Not find bean: " + servInfDefEntity.getClassName()));
				return returnErrorOutput("-1", "原子服务[" + servInfDefEntity.getClassName() + "]未定义！", traceId);
			}
			// 获取原子服务的bean
			ServiceAbstractInterface abstractService = (ServiceAbstractInterface) SpringBeanUtil
					.getBean(servInfDefEntity.getClassName());
			
			// 2、解析原子服务入参,当流程只有一个原子服务时，也可以直接使用流程的入参作为原子服务的入参
			// 当前原子服务的入参
			// 获取流程出参定义
			Set<StructFieldDef> atoServiceStrut = SysdataLoad.getStructFieldUpDefMap().get(servInfDefEntity.getiStructId());
			if (atoServiceStrut.size() == 0) {
				throw new Exception(LogUtil.format(traceId, "Not find atoservice strut under service [" + servInfDefEntity.getClassName() + "]!"));
			}
			//拼接原子服务入参map，方式与流程出参一致
			Map<String, Object> atoServiceParam = FlowServReturnJson.genReturnJson(atoServiceStrut, inputParams, shareStore);
			// 3、执行原子服务
			Map<String, Object> outputParams = new HashMap<String, Object>();
			// 先判断是否需要执行此原子服务
			boolean chkHand = false;
			try {
				chkHand = abstractService.chkNeedHandle(atoServiceParam,inputParams,shareStore);
			} catch (Exception e) {
				logger.error(LogUtil.format(traceId, "chkNeedHandle error bean: " + servInfDefEntity.getClassName()),
						e);
				return returnErrorOutput("-1", "检查原子服务[" + servInfDefEntity.getClassName() + "]是否需要执行异常！", traceId);
			}
			// 调用原子服务
			try {
				if(chkHand){
					outputParams = abstractService.businessProcess(atoServiceParam,inputParams,shareStore);
				}else{
					continue;
				}
			} catch (Exception e) {
				logger.error(LogUtil.format(traceId, "invoke error bean: " + servInfDefEntity.getClassName()), e);
				return returnErrorOutput("-1", "执行原子服务[" + servInfDefEntity.getClassName() + "]异常！", traceId);
			}

			// 4、将执行结果放入共享缓存
			if ("1".equals(service.getOutPutFlag()) && outputParams != null) {
				shareStore.putAll(outputParams);
			}
			
			// 判断原子服务ResultCode,若不为0 ,则认为执行执行失败，不再执行下一个原子服务
			String ResultCode = outputParams.get("resultCode").toString();
			if (!ResultCode.equals("0")) {
				logger.info(LogUtil.format(traceId, "step[" + service.getStep() + "],servInfId["
						+ servInfDefEntity.getClassName() + "] got error! ResultCode:" + ResultCode));
				logger.info(LogUtil.format(traceId, "****end flow[" + servName + "]****"));
				// 根据输出参数定义组装返回报文
				outputParams = parseParams(flowDefEntity.getoStructId(), flowDefEntity.getServName(), traceId,
						shareStore);
				shareStore.clear();
				return outputParams;
			}
			logger.info(LogUtil.format(traceId,
					"step[" + service.getStep() + "],servInfId[" + servInfDefEntity.getClassName() + "] end !"));
		}
		logger.debug(LogUtil.format(servName, "流程结束,提交事务"));
		logger.info(LogUtil.format(traceId, "****end flow[" + servName + "]****"));

		// 根据输出参数定义组装返回报文
		
				
		Map<String, Object> outputParams =  FlowServReturnJson.genReturnJson(flowStrut, inputParams, shareStore);
		shareStore.clear();
		return outputParams;
	}

	/**
	 * 根据传入的structid判断是入参或者出参ID，如果是入参ID则表示信息增强，如果是出参则拼接出参报文
	 * 
	 * @param iStructId
	 *            --- 原子服务入参ID
	 * @param serverName
	 *            --- 原子服务
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> parseParams(String iStructId, String serverName, String traceId,
			HashMap<String, Object> shareStore) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();

		// 获取入参定义
		Set<StructFieldDef> structFieldList = SysdataLoad.getStructFieldUpDefMap().get(iStructId);
		if (structFieldList.size() == 0) {
			logger.warn(
					LogUtil.format(traceId, "not config for struct id: " + iStructId + ", server name: " + serverName));
			return inputParams;
		}

		// TODO 加载字段
		for (StructFieldDef structFieldDef : structFieldList) {
			if (structFieldDef.getChildStructFieldDef() != null && structFieldDef.getChildStructFieldDef().size() > 0) {
				if ((structFieldDef.getRequestFiledName() != null)
						&& (!"".equals(structFieldDef.getRequestFiledName()))) {
					// 若出参节点有子节点,判断是否有入参限制 by xygao at 20180710
					if (shareStore.get(structFieldDef.getRequestFiledName()) == null
							|| "".equals(shareStore.get(structFieldDef.getRequestFiledName()))) {
						logger.info(structFieldDef.getRequestFiledName() + " 无对应入参节点，不返回数据");
						continue;
					}
				}
				if (shareStore.get(structFieldDef.getFieldName()) instanceof List) { // 入参多条

					List<Object> list = (List<Object>) shareStore.get(structFieldDef.getFieldName());// 获取入参
					ArrayList<HashMap<String, Object>> cl = new ArrayList<HashMap<String, Object>>();
					if (list.size() < 1) {
						cl.add(findChild(structFieldDef, inputParams, shareStore));
					} else {
						for (int i = 0; i < list.size(); i++) {
							HashMap<String, Object> mapObject;
							try {
								mapObject = (HashMap<String, Object>) list.get(i);
							} catch (Exception e) {
								mapObject = MapUtil.JsonToMap((JSONObject) list.get(i));
							}
							cl.add(findChild(structFieldDef, mapObject, shareStore));
						}
					}
					inputParams.put(structFieldDef.getFieldName(), cl);

				} else {
					HashMap<String, Object> mapObject1;
					try {
						mapObject1 = (HashMap<String, Object>) shareStore.get(structFieldDef.getFieldName());
						if (null == mapObject1) {
							logger.warn("node [" + structFieldDef.getFieldName() + "] has no data in shareStore");
							continue;
						}
					} catch (Exception e) {
						mapObject1 = MapUtil.JsonToMap((JSONObject) shareStore.get(structFieldDef.getFieldName()));
					}

					inputParams.put(structFieldDef.getFieldName(), findChild(structFieldDef, mapObject1, shareStore));
				}
			} else {
				String value = MapUtil.getValueFromMap(shareStore, structFieldDef.getFieldName());
				if (value == "") {
					logger.warn("do not find value for: [" + structFieldDef.getFieldName() + "] from shareStore");
				}
				inputParams.put(structFieldDef.getFieldName(), value);
			}
		}
		return inputParams;
	}

	/**
	 * 
	 * @param structFieldList
	 *            --- 入参定义集合
	 * @param upStructFieldDef
	 *            --- 入参定义对象
	 * @param inputParams
	 *            --- 入参
	 * @return
	 */
	private HashMap<String, Object> findChild(StructFieldDef upStructFieldDef, HashMap<String, Object> inputParams,
			HashMap<String, Object> shareStore) {
		HashMap<String, Object> chilidInputParams = new HashMap<String, Object>();
		HashMap<String, Object> tempInputParams = null;
		Set<StructFieldDef> childStructFieldList = upStructFieldDef.getChildStructFieldDef();// 获取子节点定义对象
		List<HashMap<String, Object>> cl = new ArrayList<HashMap<String, Object>>();

		for (StructFieldDef childStructFieldDef : childStructFieldList) {
			if (inputParams == null) {
				if (childStructFieldDef.getChildStructFieldDef() != null
						&& childStructFieldDef.getChildStructFieldDef().size() > 0) {
					logger.info("childStructFieldDef.getFieldName():" + childStructFieldDef.getFieldName());
					// 如果子节点下还有子节点，递归调用该方法
					cl.add(findChild(childStructFieldDef, inputParams, shareStore));
					chilidInputParams.putIfAbsent(childStructFieldDef.getFieldName(), cl);
				} else {
					logger.info("childStructFieldDef.getFieldName():" + childStructFieldDef.getFieldName());
					// 如果入参中没有该节点对应的值，则赋予默认值
					chilidInputParams.put(childStructFieldDef.getFieldName(), childStructFieldDef.getDefaultValue());
				}
			} else {
				if (childStructFieldDef.getChildStructFieldDef() != null
						&& childStructFieldDef.getChildStructFieldDef().size() > 0) {
					logger.info("childStructFieldDef.getFieldName():" + childStructFieldDef.getFieldName());

					// 若是list类型,循环处理每一条记录,再将获取到的各个map add 进返回list 中
					// ，最后以childStructFieldDef.getFieldName()为key,将整个list为value 返回给父级
					if ((inputParams.get(childStructFieldDef.getFieldName()) != null)
							&& (inputParams.get(childStructFieldDef.getFieldName()) instanceof List)) {
						List<HashMap<String, Object>> tempList = (List<HashMap<String, Object>>) inputParams
								.get(childStructFieldDef.getFieldName());
						if (tempList.size() > 0) {
							for (int k = 0; k < tempList.size(); k++) {
								Object obj = tempList.get(k);

								String type = obj.getClass().getName();
								if (type.contains("HashMap")) {
									tempInputParams = (HashMap<String, Object>) obj;
								} else if (type.contains("JSONObject")) {
									tempInputParams = MapUtil.JsonToMap((JSONObject) obj);
								} else {
									logger.error("unspported type: " + obj.getClass().getName());
								}

								cl.add(findChild(childStructFieldDef, tempInputParams, shareStore));
							}
						}
						// TODO 如果list长度为0，如何遍历返回参数配置表中子节点的数据
						chilidInputParams.putIfAbsent(childStructFieldDef.getFieldName(), cl);

					} else {
						logger.info("childStructFieldDef.getFieldName():" + childStructFieldDef.getFieldName());
						HashMap<String, Object> mapObject1;
						try {
							mapObject1 = (HashMap<String, Object>) inputParams.get(childStructFieldDef.getFieldName());
							if (null == mapObject1) {
								logger.warn(
										"node [" + childStructFieldDef.getFieldName() + "] has no data in shareStore");
								continue;
							}
						} catch (Exception e) {
							mapObject1 = MapUtil
									.JsonToMap((JSONObject) shareStore.get(childStructFieldDef.getFieldName()));
						}

						chilidInputParams.put(childStructFieldDef.getFieldName(),
								findChild(childStructFieldDef, mapObject1, shareStore));
					}

				} else {
					String value = MapUtil.getValueFromMap(inputParams, childStructFieldDef.getFieldName());
					chilidInputParams.put(childStructFieldDef.getFieldName(), value);
				}

			}
		}
		return chilidInputParams;
	}

	private HashMap<String, Object> returnErrorOutput(String resultCode, String resultMsg, String traceId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMsg", resultMsg);
		resultMap.put("traceId", traceId);
		return resultMap;

	}
}
