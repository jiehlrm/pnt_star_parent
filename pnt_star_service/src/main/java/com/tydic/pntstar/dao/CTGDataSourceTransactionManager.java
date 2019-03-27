package com.tydic.pntstar.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/** 
* @Title: CTGDataSourceTransactionManager.java 
* @Package com.tydic.pntstar.dao 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2018年12月27日 下午6:15:52 
* @version V1.0 
*/
public class CTGDataSourceTransactionManager  extends DataSourceTransactionManager {
	
	protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition)
			throws SQLException {
			Statement stmt = con.createStatement();
			try {
				stmt.executeUpdate("UDAL XA START");
			}
			finally {
				stmt.close();
			}
	}

}
