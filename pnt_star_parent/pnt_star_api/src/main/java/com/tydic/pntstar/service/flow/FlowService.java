package com.tydic.pntstar.service.flow;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public interface FlowService {
	
	/*****
	 * 
	 * @param traceId  会话ID
	 * @param servName 流程编码
	 * @param senceId  场景参数，用途未知，默认�?-1参数
	 * @param inputParams 流程入参
	 * @return 按报文规范出的参�?
	 * @throws Exception
	 */
 	public Map<String,Object> evRun(String msgId,String servName,String senceId,JSONObject inputParams) throws Exception;
 	
}
