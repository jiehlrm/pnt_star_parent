package com.tydic.pntstar.service.task;

import java.util.Map;

/** 
* @Title: MonthCalcTask.java 
* @Package com.tydic.pntstar.service.task 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2019年1月18日 上午11:04:22 
* @version V1.0 
*/
public interface MonthCalcTask {
	
	void monthCalcTask();
	void dealOne(Map<String,Object> param) throws Exception;

}
