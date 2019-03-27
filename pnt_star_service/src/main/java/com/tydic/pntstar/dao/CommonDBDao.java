package com.tydic.pntstar.dao;

import java.util.List;
import java.util.Map;

public abstract interface CommonDBDao
{
  public abstract List<String[]> getSqlList();
  public abstract List<String[]> getSqlByString(String sql);
  
  public abstract void insert(String serviceName,Map<String, Object> param);
  
  public void insert(String serviceName,Map<String, Object> param,boolean flag);//flag是否开始分布式事务操作
  
  public abstract void update(String serviceName,Map<String, Object> param);
  
  public abstract void update(String serviceName,Map<String, Object> param,boolean flag);
  
  public abstract List<Map<String, Object>> query(String serviceName,Map<String, Object> param);
  
  public abstract Map<String, Object> queryForOne(String serviceName,Map<String, Object> param);
  
  public abstract void delete(String serviceName,Map<String, Object> param);
  
  public abstract void delete(String serviceName,Map<String, Object> param ,boolean flag);
  
  public abstract void insertBatch(String serviceName,List<Map<String, Object>> paramList);
  
  public abstract void updateBatch(String serviceName,List<Map<String, Object>> paramList);
  
  public abstract void deleteBatch(String serviceName,List<Map<String, Object>> paramList);
    
  public abstract void runBatch(List<String> serviceNameList,
			Map<String,List<Map<String, Object>>> paramMap,Map<String,String> operMap);
  
  public abstract String getPK(String table);
  
  public void commit() ;//提交当前线程对应的事务
  
  public void rollback() ;//回滚当前线程对应的事务
  
  public void release();//释放资源
  
 
 }
