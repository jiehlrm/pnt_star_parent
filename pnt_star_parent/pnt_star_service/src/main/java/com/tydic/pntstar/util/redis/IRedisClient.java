package com.tydic.pntstar.util.redis;

import java.util.Map;

public interface IRedisClient {
	public String set(String key, String value)  throws Exception;
	public String get(String key) throws Exception;
	public String setMap(String key,Map<String,String> value)throws Exception;
	public Map<String,String> getMap(String key)throws Exception;
    public Long del(String key) throws Exception;
    public Long delMap(String key)throws Exception;

}
