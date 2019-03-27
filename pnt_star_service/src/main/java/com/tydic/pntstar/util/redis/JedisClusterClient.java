package com.tydic.pntstar.util.redis;

import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisCluster;
/***
 * jedisCluster集群连接方式
 * @author Administrator
 *
 */
public class JedisClusterClient implements IRedisClient{

	private static Logger log = Logger.getLogger(JedisClusterClient.class);
    private JedisCluster     jedisCluster;

    
    public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	/**
     * 设置单个值
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = null;
        try {
            result = jedisCluster.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } 
        return result;
    }

    /**
     * 获取单个值
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        try {
            result = jedisCluster.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
        }
        return result;
    }
    
    public Long del(String key) {
        Long result = null;
        try {
            result = jedisCluster.del(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
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
	 */
	public String setMap(String key,Map<String,String> value) {
		 String result = null;
	        try {
	            result = jedisCluster.hmset(key, value);
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	        } finally {
	        }
	        return result;
	}

    
    /**
	 * 
	 * Description:map类型get……
	 * 
	 * @param key
	 * @return
	 */
	public Map<String,String> getMap(String key) {
		Map<String,String> result = null;
		try {
			result = jedisCluster.hgetAll(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
		}
		return result;
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
        Long result = null;
        try {
            result = jedisCluster.hdel(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
        }
        return result;
    }
}