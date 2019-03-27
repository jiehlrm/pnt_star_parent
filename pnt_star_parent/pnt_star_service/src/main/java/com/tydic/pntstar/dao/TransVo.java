package com.tydic.pntstar.dao;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;

/** 
* @Title: TransVo.java 
* @Package com.tydic.pntstar.dao 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2018年12月19日 下午2:48:47 
* @version V1.0 
*/
public class TransVo {
	DataSourceTransactionManager transactionManager;
	TransactionStatus transactionStatus;
	public TransVo(DataSourceTransactionManager transactionManager,TransactionStatus transactionStatus) {
		this.transactionManager=transactionManager;
		this.transactionStatus=transactionStatus;
	}
}
