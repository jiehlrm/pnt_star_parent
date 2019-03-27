package com.tydic.pntstar.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.StringCommon;
import com.tydic.pntstar.util.Tools;

 
public class CommonDBDaoImplPAY implements CommonDBDaoPAY {
	private JdbcTemplate jct;
	private static Logger logger = LoggerFactory
			.getLogger(CommonDBDaoImplPAY.class);
	
	
	
	/************************
	 * 保存当前线程中持有的所有的数据库的事务状态
	 * key:dbflag value:TransactionStatus
	 ************************/
	private static ThreadLocal<List<TransVo>> sessionConnection=new ThreadLocal<List<TransVo>>();

	public void setJct(JdbcTemplate jdbcTemplate) {
		this.jct = jdbcTemplate;
	}
	@SuppressWarnings("unchecked")
	public List<String[]> getSqlByString(String sql){
		List<String[]> temps=null;
		temps=this.jct.query(sql,
					 new RowMapper(){
						public Object mapRow(ResultSet rs, int arg1)
								throws SQLException {
							String[] strs = new String[2];
							strs[0] = rs.getString(1);
							strs[1] = rs.getString(2);
							return strs;
						}
		});
		return temps;
	}
	@SuppressWarnings("unchecked")
	public List<String[]> getSqlList(){
		List<String[]> temps=null;
		temps=this.jct.query("SELECT service_name,flag,sql_desc,sql_desc_add,sql_desc_add2,sql_desc_add3,sql_desc_add4 from par_service_sql_desc_tpss order by sql_desc_seq",
					 new RowMapper(){
						
						public Object mapRow(ResultSet rs, int arg1)
								throws SQLException {
							String[] strs = new String[3];
							strs[0] = rs.getString(1);
							strs[1] = rs.getString(2);
							strs[2] = StringCommon.trimNull(rs.getString(3))
									+" "+StringCommon.trimNull(rs.getString(4))
									+" "+StringCommon.trimNull(rs.getString(5))
									+" "+StringCommon.trimNull(rs.getString(6))
									+" "+StringCommon.trimNull(rs.getString(7));
							return strs;
						}
		});
		return temps;
	}
	
	@Override
	public void insert(String serviceName, Map<String, Object> param) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		sqlSession.insert(serviceName, param);
		
	}
	

	@Override
	public void update(String serviceName, Map<String, Object> param) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		sqlSession.update(serviceName, param);
		
	}

	@Override
	public List<Map<String, Object>> query(String serviceName,
			Map<String, Object> param) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		return sqlSession.selectList(serviceName, param);
	}
	
	@Override
	public void delete(String serviceName, Map<String, Object> param) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		sqlSession.delete(serviceName, param);
		
	}

	@Override
	public void insertBatch(String serviceName,
			List<Map<String, Object>> paramList) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		Map paraMap = new HashMap();
		paraMap.put("paramList",paramList);
		sqlSession.insert(serviceName, paraMap);
	}
	
	@Override
	public void updateBatch(String serviceName,
			List<Map<String, Object>> paramList) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		Map paraMap = new HashMap();
		paraMap.put("paramList",paramList);
		createTrans(serviceName,null);
		sqlSession.update(serviceName, paraMap);
	}
	
	@Override
	public void deleteBatch(String serviceName,
			List<Map<String, Object>> paramList) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		Map paraMap = new HashMap();
		paraMap.put("paramList",paramList);
		createTrans(serviceName,null);
		sqlSession.delete(serviceName, paraMap);
	}

	@Override
	public Map<String, Object> queryForOne(String serviceName,
			Map<String, Object> param) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		return sqlSession.selectOne(serviceName,param);
	}
	 
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void runBatch(List<String> serviceNameList,
			Map<String,List<Map<String, Object>>> paramMap,Map<String,String> operMap){
		
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		
	
		String operFlag = "";
		List<Map<String, Object>> paraList = new ArrayList<Map<String, Object>>();
		
		//循环每个service_name处理
		for(String serviceName : serviceNameList){
			operFlag = operMap.get(serviceName)+"";
			paraList = paramMap.get(serviceName);
			
			if(operFlag.equals("A")){
				this.insertBatch(serviceName, paraList);
			}else if(operFlag.equals("AO")){
				sqlSession.insert(serviceName, paraList.get(0));
			}if(operFlag.equals("U")){
				this.updateBatch(serviceName, paraList);
			}else if(operFlag.equals("UO")){
				sqlSession.update(serviceName, paraList.get(0));
			}else if(operFlag.equals("D")){
				this.deleteBatch(serviceName, paraList);
			}else if(operFlag.equals("DO")){
				sqlSession.delete(serviceName, paraList.get(0));
			}
		}
	}
	@Override
	public String getPK(String table) {
		// TODO Auto-generated method stub
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		Map<String, Object> param =new HashMap<>();
		param.put("tableName", table);
		param=sqlSession.selectOne("QueryPKPAY",param);
		return param.get("id").toString();
	}
	
	
	/***
	 * 创建当前线程的事务
	 */
	private void createTrans(String serviceName,String flag) {
		//获取当前dbFlag
//		String dbFlag=serviceNameDbFlag.get(serviceName+"_"+flag);  多数据源
		List<TransVo> list=sessionConnection.get();
		//如果当前业务已经开始了业务处理则直接返回
		if(list!=null&&list.size()>0){
			//已经开启了事务了，则直接返回，xa事务一个connection只要开启一次
			 return;
		}else{
			if(list==null) {
				list=new ArrayList<TransVo>();
		    }
			
			//1.开启一个新的事务
			DataSourceTransactionManager transactionManager =(DataSourceTransactionManager) SpringBeanUtil.getBean(
//			        "transactionManager_"+dbFlag);
		"transactionManager");
	        //2.获取事务定义
	        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	        //3.设置事务隔离级别，开启新事务
	        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	        //4.获得事务状态
	        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
	        TransVo vo=new TransVo(transactionManager, transactionStatus);
	        //5.事务状态压入当前线程
	        list.add(vo);
	        sessionConnection.set(list);
		}
	}
	/***
	 * 提交当前线程对应的事务
	 */
	public void commit() {
		List<TransVo> list=sessionConnection.get();
		if (list!=null) {
			ListIterator<TransVo> i=list.listIterator(list.size());
			while(i.hasPrevious()) {
				TransVo t=i.previous();
				DataSourceTransactionManager transactionManager =t.transactionManager;
				logger.debug("commit---------- ");
				transactionManager.commit(t.transactionStatus);
			}
			list.clear();
		}
	}
	
	/***
	 * 回滚事务
	 */
	public void rollback() {
		List<TransVo> list=sessionConnection.get();
		if (list!=null) {
			ListIterator<TransVo> i=list.listIterator(list.size());
			while(i.hasPrevious()) {
				TransVo t=i.previous();
				DataSourceTransactionManager transactionManager =t.transactionManager;
				if (!t.transactionStatus.isCompleted()) {
					logger.debug("rollback---------- ");
					transactionManager.rollback(t.transactionStatus);
				}else {
					logger.debug("rollback by spring auto---------- ");
				}
			}
			list.clear();
		}
	}
	
	/****
	 * 释放当前线程变量,如果还没开事务就不用处理
	 */
	public void release() {
		logger.debug("release----------");
		if(!Tools.isNull(sessionConnection.get())) {
			sessionConnection.get().clear();
		}
	}

	/* 
	 * 开启分布式事务
	 */
	@Override
	public void insert(String serviceName, Map<String, Object> param, boolean flag) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		createTrans(serviceName,null);
		sqlSession.insert(serviceName, param);
	}
	/* (non-Javadoc)
	 * @see com.tydic.pntstar.dao.CommonDBDao#update(java.lang.String, java.util.Map, boolean)
	 */
	@Override
	public void update(String serviceName, Map<String, Object> param, boolean flag) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		createTrans(serviceName,null);
		sqlSession.update(serviceName, param);
		
	}
	/* 
	 * 
	 */
	@Override
	public void delete(String serviceName, Map<String, Object> param, boolean flag) {
		SqlSessionTemplate sqlSession = (SqlSessionTemplate) SpringBeanUtil
				.getBean("sessionTemplatePAY");
		createTrans(serviceName,null);
		sqlSession.delete(serviceName, param);
	}

}