package com.tydic.pntstar.util.redis;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 
 * @Package com.tydic.calcdemo.redis
 * @ClassName JodisPoolClient.java
 * @author xiangsl
 * @date 2016年3月26日 上午10:41:51
 * @Description: 此处添加类描述……
 * @version V1.0
 */
public class JodisPoolClient implements IRedisClient {
	private static Logger logger = Logger.getLogger(JodisPoolClient.class);
	private String zkServers;
	private String proxyDir;
	private JedisResourcePool jedisPool;

	public JodisPoolClient(String zkServers, String proxyDir) {
		this.zkServers = zkServers;
		this.proxyDir = proxyDir;
		jedisPool = RoundRobinJedisPool.create().curatorClient(this.zkServers, 30000).zkProxyDir(this.proxyDir).build();
	}

	public JedisResourcePool getJedisPool() {
		return jedisPool;
	}

	/**
	 * 
	 * Description:单字符串set……
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public String set(String key, String value) throws Exception {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.set error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	/**
	 * 
	 * Description:序列化操作……
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public String set(byte[] key, byte[] value) throws Exception{
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.set error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	

	
	/**
	 * 
	 * Description:map set……
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public String setMap(String key,Map<String,String> value) throws Exception {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.hmset(key, value);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.set error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}


	/**
	 * 
	 * Description:单字符串get……
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public String get(String key) throws Exception {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.get error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * Description:map类型get……
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Map<String,String> getMap(String key) throws Exception {
		Jedis jedis = null;
		Map<String,String> result = null;
		try {
			jedis = getJedis();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.get error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}

	/**
	 * 
	 * Description:序列化get……
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public byte[] get(byte[] key) throws Exception {
		Jedis jedis = null;
		byte[] result = null;
		try {
			jedis = getJedis();
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.get error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}

	/**
	 * 
	 * Description:单字符串删除……
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Long del(String key) throws Exception {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.del(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.del error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * Description:删除map
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Long delMap(String key) throws Exception {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.hdel(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.del error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}

	/**
	 * 
	 * Description:序列化删除……
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Long del(byte[] key) throws Exception {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = getJedis();
			result = jedis.del(key);
		} catch (Exception e) {
			logger.error("RedisClientTemplate.del error:", e);
			throw e;
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	/***
	 * 获取redis连接若连接失败则重置pool，若重试后仍然失败则放弃本次连接
	 ****/
	static int reconnectCount=0; 
	private Jedis getJedis() throws Exception{
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			logger.error("get redis Resource error ", e);
			try {
				jedisPool.close();
				Thread.sleep(1000);
			} catch (IOException e1) {
			} catch (InterruptedException e1) {
			}
			
			//当前用户请求失败，则可以重连n次
			while(reconnectCount<3){
				
				reconnectCount++;
		        logger.info("retry reset pool....... "+ reconnectCount+" times!");
				jedisPool = RoundRobinJedisPool.create().curatorClient(this.zkServers, 30000).zkProxyDir(this.proxyDir).build();

		        return getJedis();
			} 
			reconnectCount=0;
	        throw e;

		}
		return jedis;
	}
}