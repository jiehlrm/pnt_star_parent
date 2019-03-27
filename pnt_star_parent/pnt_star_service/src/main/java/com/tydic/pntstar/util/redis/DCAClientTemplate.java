package com.tydic.pntstar.util.redis;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tydic.dca.DCAClient;
import com.tydic.pool.DCAConnectionPool;
//单例模式，beanId=redisClient
public class DCAClientTemplate implements IRedisClient {

	private static Logger logger = LoggerFactory.getLogger(DCAClientTemplate.class);
	private  DCAConnectionPool pool = null;
	private  DCA dca = null;
	private int reconnectCount = 0;
	
	
	
	public DCAConnectionPool getPool() {
		return pool;
	}
	public void setPool(DCAConnectionPool pool) {
		this.pool = pool;
	}
	public DCA getDca() {
		return dca;
	}
	public void setDca(DCA dca) {
		this.dca = dca;
	}
	public int getReconnectCount() {
		return reconnectCount;
	}
	public void setReconnectCount(int reconnectCount) {
		this.reconnectCount = reconnectCount;
	}
	//实例化一个连接
	/**
	 * 设置单个值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key2, String value) {
//		String key2=key.replace("[", "").replace("]", "");
//		if(!key2.startsWith(dca.getDcaFlag())){
//			key2=dca.getDcaFlag()+"."+key2;
//		}
		int ret = 0;
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = System.currentTimeMillis();
			//step1 从连接池中获取连接
			DCAClient client = pool.getConnection();
			//step2 设置值
			ret=client.set(key2, value);
			//step3 释放连接到池中
			pool.releaseConn(client);
			endTime = System.currentTimeMillis();
			logger.debug("time cost: " + (endTime - startTime) + " dca set key["+key2+"],value["+value+"],redis ret:"
					+ ret);
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error("dca set key["+key2+"],value["+value+"]  error reconnectCount:"+reconnectCount,e);
		}

		return ret+"";
	}
	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key2) {
//		String key2=key.replace("[", "").replace("]", "");
//		if(!key2.startsWith(dca.getDcaFlag())){
//			key2=dca.getDcaFlag()+"."+key2;
//		}
		logger.debug("start get dca key2:"+key2);
		String ret = null;
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = System.currentTimeMillis();
			//step1 从连接池中获取连接
			DCAClient client = pool.getConnection();
			//step2 获取值
			if(1==client.exists(key2)){
				ret=client.get(key2);
			}
			//step3 释放连接到池中
			pool.releaseConn(client);
			endTime = System.currentTimeMillis();
			logger.debug("time cost: " + (endTime - startTime) + ",redis key["+key2+"],ret:"
					+ ret);
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error("dca get key["+key2+"] value error reconnectCount:"+reconnectCount,e);
		}

		return ret;
	}

	public Long del(String key2) {
//		String key2=key.replace("[", "").replace("]", "");
//		if(!key2.startsWith(dca.getDcaFlag())){
//			key2=dca.getDcaFlag()+"."+key2;
//		}
		int ret = 0;
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = System.currentTimeMillis();
			//step1 从连接池中获取连接
			DCAClient client = pool.getConnection();
			//step2 设置值
			ret=client.delete(key2);
			//step3 释放连接到池中
			pool.releaseConn(client);
			endTime = System.currentTimeMillis();
			logger.debug("time cost: " + (endTime - startTime) + ",redis ret:"
					+ ret);
		}  catch (Exception e) {
			logger.error("dca delete value error reconnectCount:"+reconnectCount,e);
			endTime = System.currentTimeMillis();
		}

		return Long.parseLong(ret+"");
	}

	/**
	 * 
	 * Description:map set……
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String setMap(String key, Map<String, String> value) {
		logger.error(" not support for DCA.setMap");
		return null;
	}

	/**
	 * 
	 * Description:map类型get……
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> getMap(String key) {
		logger.error(" not support for DCA.getMap");
		return null;
	}

	/**
	 * 
	 * @Description: 删除map
	 * @param key
	 * @return
	 * @throws
	 * @date 2016年5月23日
	 * @see com.tydic.common.redis.IRedisClient#delMap(java.lang.String)
	 */
	public Long delMap(String key) {
		logger.error(" not support for DCA.delMap");
		return null;
	}
	
}