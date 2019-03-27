package com.tydic.pntstar.service.impl.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tydic.pntstar.service.task.UpdatePointCustStatusTask;

/** 
* @Title: UpdatePointCustStatusTaskImpl.java 
* @Package com.tydic.pntstar.service.impl.task 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2019年1月3日 下午5:00:50 
* @version V1.0 
*/
@Component
public class UpdatePointCustStatusTaskImpl implements UpdatePointCustStatusTask {

	private static final Logger logger = LoggerFactory.getLogger(UpdatePointCustStatusTaskImpl.class);
	/* (non-Javadoc)
	 * @see com.tydic.pntstar.service.task.UpdatePointCustStatusTask#updatePointCustStatus()
	 */
	@Override
	public void updatePointCustStatus() {
		// TODO Auto-generated method stub

	}
	
	public void test() {
		logger.debug("定时任务开始。。。。。。。。。");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
