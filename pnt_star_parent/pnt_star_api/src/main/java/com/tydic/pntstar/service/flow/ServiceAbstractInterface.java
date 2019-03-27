package com.tydic.pntstar.service.flow;

import java.util.HashMap;
import java.util.Map;


public interface ServiceAbstractInterface {
	
	public boolean chkNeedHandle(Map<String, Object> atoServiceParam, Map<String, Object> inputParams, Map<String, Object> shareStore);
	public Map<String,Object> businessProcess(Map<String, Object> atoServiceParam,Map<String,Object> inputParams, Map<String, Object> shareStore) throws Exception;
	
}
