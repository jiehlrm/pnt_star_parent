package com.tydic.pntstar.util.redis;

import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClientTemplate implements IRedisClient{

	private static Logger log = Logger.getLogger(RedisClientTemplate.class);
 
	private ShardedJedisPool    shardedJedisPool;

    public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
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

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
        	shardedJedis.close();
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
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
        	shardedJedis.close();
        }
        return result;
    }
    
    public Long del(String key) {
        Long result = null;
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
        	shardedJedis.close();
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

	        ShardedJedis shardedJedis = shardedJedisPool.getResource();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hmset(key, value);
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	        	shardedJedis.close();
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
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		if (shardedJedis == null) {
			return result;
		}

		boolean broken = false;
		try {
			result = shardedJedis.hgetAll(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			shardedJedis.close();
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
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
        	shardedJedis.close();
        }
        return result;
    }
}