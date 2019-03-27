package com.tydic.pntstar.util.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.tydic.pntstar.util.ServerConfig;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pool.ConnectBean;
import com.tydic.pool.DCAConnectionPool;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/***
 * 根据用户配置的数据application.properties中的redisClusterMode选用不同的类进行实例化<br/>
 * 使用方法如下：<br/>
 * IRedisClient redisClient=(IRedisClient)SpringBeanUtil.getBean("redisClient");<br/>
 * redisClient.get(key);<br/>
 * redisClient.set(key,value);<br/>
 * 具体的配置信息如下：<br/>
 * redisClusterMode=redisSharded(配置文件redisSharded.properties,redis Hash模式如果有多台机器，客户端会根据值的hash到不同的机器上操作,此模式也适用于单机模式,RedisClientTemplate)<br/>
 * redisClusterMode=redisServerCluster(配置文件redisServerCluster.properties,redis服务端集群模式 JedisClusterClient)<br/>
 * redisClusterMode=codisCluster(配置文件codisCluster.properties,redis客户端集群模式 JodisPoolClient)<br/>
 * redisClusterMode=DCACluster(dca集群模式DCACluster.properties)<br/>
 * @author Administrator
 *
 */
public class RedisFactory {
	
	private static Logger logger = LoggerFactory.getLogger(RedisFactory.class);

	public void init() {
		ServerConfig sConfig=null;
		try {
			sConfig = new ServerConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String redisClusterMode=sConfig.getValue("redisClusterMode");
		if ("redisSharded".equals(redisClusterMode)) {
			Resource addressConfig=new ClassPathResource("redis/redisSharded.properties");
			Properties prop = new Properties();  
            try {
				prop.load(addressConfig.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}  
            
			//redis Hash模式
			//ste1 初始化ShardedJedisPool
			JedisPoolConfig  jedisPoolConfig=new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(Integer.parseInt(prop.getProperty("redis.maxIdle")));
			jedisPoolConfig.setMaxTotal(Integer.parseInt(prop.getProperty("redis.maxActive")));
			jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(prop.getProperty("redis.maxWait")));
			jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("redis.testOnBorrow")));
			
			//step2 JedisShardInfo
			//ShardedJedis里面采用了一致性哈希的算法,不同的机器里面放置的值是不一样的
			String redisUrl=prop.getProperty("redis.url");
			String url[] =redisUrl.split(";");
			List<JedisShardInfo> infoList=new ArrayList<>();
			for (int i = 0; i < url.length; i++) {
				String curAddress=url[i];
				JedisShardInfo jedisShardInfo=new JedisShardInfo(curAddress);
				infoList.add(jedisShardInfo);
			}
			ShardedJedisPool pool=new ShardedJedisPool(jedisPoolConfig,infoList);
			//实例化RedisClientTemplate
			RedisClientTemplate clientTemplate=new RedisClientTemplate();
			clientTemplate.setShardedJedisPool(pool);
			
			//注入spring,beanId=redisClient
			ApplicationContext ac=SpringBeanUtil.getApplicationContext();
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
			if(acf.containsBean("redisClient")){
				acf.destroySingleton("redisClient");
			}
			acf.registerSingleton("redisClient", clientTemplate);
			
		}else if ("redisServerCluster".equals(redisClusterMode)) {
			//redis3.0服务端集群
			Resource addressConfig=new ClassPathResource("redis/redisServerCluster.properties");
			Properties prop = new Properties();  
            try {
				prop.load(addressConfig.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}  
            
			//ste1 初始化ShardedJedisPool
			JedisPoolConfig  jedisPoolConfig=new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(Integer.parseInt(prop.getProperty("redis.maxIdle")));
			jedisPoolConfig.setMaxTotal(Integer.parseInt(prop.getProperty("redis.maxActive")));
			jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(prop.getProperty("redis.maxWait")));
			jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("redis.testOnBorrow")));
			
			//step2 JedisClusterFactory
			
			JedisClusterFactory factory=new JedisClusterFactory();
			factory.setAddressConfig(addressConfig);
			factory.setAddressKeyPrefix("cluster");
			factory.setTimeout(Integer.parseInt(prop.getProperty("redis.timeout", "300000")));
			factory.setMaxRedirections(Integer.parseInt(prop.getProperty("redis.reconnectCount", "3")));
			factory.setGenericObjectPoolConfig(jedisPoolConfig);
			factory.setSoTimeout(Integer.parseInt(prop.getProperty("redis.soTimeout", "30000")));
			factory.setPassword(prop.getProperty("redis.password", null));
			try {
				factory.afterPropertiesSet();
			} catch (Exception e) {
				logger.error("error init jediscluster ",e);
			}
			
			//step3 实例化JedisClusterClient
			JedisClusterClient jedisClusterClient=new JedisClusterClient();
			jedisClusterClient.setJedisCluster(factory.getJedisCluster());
			  
			 
			//注入spring,beanId=redisClient
			ApplicationContext ac=SpringBeanUtil.getApplicationContext();
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
			if(acf.containsBean("redisClient")){
				acf.destroySingleton("jedisClusterClient");
			}
			acf.registerSingleton("redisClient", jedisClusterClient);
			
			
		}else if ("codisCluster".equals(redisClusterMode)) {
			//JodisPoolClient
			//step1 初始化属性
			Resource addressConfig=new ClassPathResource("redis/codisCluster.properties");
			Properties prop = new Properties();  
            try {
				prop.load(addressConfig.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}  
            //step2 实例化
            String zkServers=prop.getProperty("codis.zk_address");
            String proxyDir=prop.getProperty("codis.proxydir");
            JodisPoolClient jodisPoolClient=new JodisPoolClient(zkServers, proxyDir);
            
            //step3注入spring容器
			//注入spring,beanId=redisClient
			ApplicationContext ac=SpringBeanUtil.getApplicationContext();
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
			if(acf.containsBean("redisClient")){
				acf.destroySingleton("redisClient");
			}
			acf.registerSingleton("redisClient", jodisPoolClient);
		}else if ("DCACluster".equals(redisClusterMode)) {
			//dca场景
			//step1 初始化DCA属性
			Resource addressConfig=new ClassPathResource("redis/DCACluster.properties");
			Properties prop = new Properties();  
            try {
				prop.load(addressConfig.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}  
			
			
			DCA dca=new DCA();
			dca.setDcaIp(prop.getProperty("dca.ip"));
			dca.setDcaPort(Integer.parseInt(prop.getProperty("dca.port")));
			dca.setDcaUUID(prop.getProperty("dca.UUID"));
			dca.setDcaAcctid(prop.getProperty("dca.acctid"));
			dca.setDcaUsername(prop.getProperty("dca.username"));
			dca.setDcaPasswd(prop.getProperty("dca.passwd"));
			dca.setDcaProcessname(prop.getProperty("dca.processname"));
			dca.setDcaPid(Integer.parseInt(prop.getProperty("dca.pid")));
			dca.setDcaFlag(prop.getProperty("dca.flag"));
			dca.setReconnectCount(Integer.parseInt(prop.getProperty("dca.reconnectCount")));
			dca.setReconnectInterval(Integer.parseInt(prop.getProperty("dca.reconnectInterval")));
			dca.setTimeout(Integer.parseInt(prop.getProperty("dca.timeout")));
			dca.setMaxFreeConnections(Integer.parseInt(prop.getProperty("dca.MaxFreeConnections")));
			dca.setMaxActiveConnections(Integer.parseInt(prop.getProperty("dca.MaxActiveConnections")));
		 	//step2 初始化DCAClientTemplate
			DCAClientTemplate dcaClientTemplate=new DCAClientTemplate();
			try {
				ConnectBean connectBean = new ConnectBean();
				connectBean.setIp(dca.getDcaIp());
				connectBean.setPort(dca.getDcaPort());
				connectBean.setAcctid(dca.getDcaAcctid());
				connectBean.setUserName(dca.getDcaUsername());
				connectBean.setPassword(dca.getDcaPasswd());
				connectBean.setProcessname(dca.getDcaProcessname());
				connectBean.setPid(dca.getDcaPid());
				connectBean.setInitConnections(5);//设置每次初始化多少量
				connectBean.setMaxFreeConnections(dca.getMaxFreeConnections());//默认10
				connectBean.setMaxActiveConnections(dca.getMaxActiveConnections());//默认100
				DCAConnectionPool pool = new DCAConnectionPool(connectBean);
				
				dcaClientTemplate.setDca(dca);
				dcaClientTemplate.setPool(pool);
				dcaClientTemplate.setReconnectCount(dca.getReconnectCount());
				logger.debug("DCA connect !");
			} catch (Exception e) {
				logger.error("init dca error", e);
			}
			//step3注入spring容器
			//注入spring,beanId=redisClient
			ApplicationContext ac=SpringBeanUtil.getApplicationContext();
			DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
			if(acf.containsBean("redisClient")){
				acf.destroySingleton("redisClient");
			}
			acf.registerSingleton("redisClient", dcaClientTemplate);
		}
	}
}
