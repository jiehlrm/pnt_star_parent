package com.tydic.pntstar.util.zk;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tydic.pntstar.util.DateUtil;
import com.tydic.pntstar.util.ServerConfig;
import com.tydic.pntstar.util.StringCommon;


public class ZookeeperManager {
	private static Logger logger = LoggerFactory.getLogger(ZookeeperManager.class);
	static Map conf=new HashMap<String,String>();
	//ACL有用户名密码都可以访问的模式
    private static ACL aclRoot = null;
    private static List<ACL> aclList = new ArrayList<ACL>();
    
    private static CuratorFramework zkclient = null;

	static {
		ServerConfig sc;
		try {
			sc = new ServerConfig();
			
			conf.put(ZkConstant.ZOOKEEPER_RETRY_INTERVAL, sc.getValue(ZkConstant.ZOOKEEPER_RETRY_INTERVAL));
			conf.put(ZkConstant.ZOOKEEPER_RETRY_TIMES, sc.getValue(ZkConstant.ZOOKEEPER_RETRY_TIMES));
			conf.put(ZkConstant.ZOOKEEPER_CONNECTION_TIMEOUT, sc.getValue(ZkConstant.ZOOKEEPER_CONNECTION_TIMEOUT));
			conf.put(ZkConstant.ZOOKEEPER_SESSION_TIMEOUT, sc.getValue(ZkConstant.ZOOKEEPER_SESSION_TIMEOUT));
			conf.put(ZkConstant.ZOOKEEPER_CLUSTER_SERVERS,sc.getValue(ZkConstant.ZOOKEEPER_CLUSTER_SERVERS));
			conf.put(ZkConstant.ZOOKEEPER_CLUSTER_AUTH,sc.getValue(ZkConstant.ZOOKEEPER_CLUSTER_AUTH));
			if(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH)!=null&&!StringCommon.isNull(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH).toString())) {
				try {
					//ACL有用户名密码都可以访问的模式
					aclRoot = new ACL(Perms.ALL,new Id("digest",DigestAuthenticationProvider.generateDigest(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH).toString())));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				aclList.add(aclRoot);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public static CuratorFramework newCurator() {

		if(zkclient==null) {
			CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

			RetryNTimes retryPolicy = new RetryNTimes(Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_RETRY_TIMES)),
					Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_RETRY_INTERVAL)));
			//有授权信息
			if(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH)!=null&&!StringCommon.isNull(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH).toString())) {
				builder.connectString(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_SERVERS).toString()).connectionTimeoutMs(Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_CONNECTION_TIMEOUT)))
				.sessionTimeoutMs(Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_SESSION_TIMEOUT)))
	            .authorization("digest",conf.get(ZkConstant.ZOOKEEPER_CLUSTER_AUTH).toString().getBytes())//权限访问
	            .retryPolicy(retryPolicy);
			}else {
				builder.connectString(conf.get(ZkConstant.ZOOKEEPER_CLUSTER_SERVERS).toString()).connectionTimeoutMs(Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_CONNECTION_TIMEOUT)))
				.sessionTimeoutMs(Integer.valueOf((String) conf.get(ZkConstant.ZOOKEEPER_SESSION_TIMEOUT))).retryPolicy(retryPolicy);
			}
			
			zkclient= builder.build();
			zkclient.start();
		}
		
		return zkclient;
	}
	
	/***
	 * 初始值为当前时间到ms
	 * @param client
	 * @param path
	 * @throws Exception
	 */
	public static void initNode(CuratorFramework client, String path) throws Exception {
		Stat stat = client.checkExists().forPath(path);
		String nowStr=DateUtil.getNowTimeMs();
		if(stat == null) {
			if (aclList.size()>0) {
				String forPath = client.create().creatingParentsIfNeeded().
						withACL(aclList).
						forPath(path, nowStr.getBytes("UTF-8"));
			}else {
				String forPath = client.create().creatingParentsIfNeeded().
						forPath(path, nowStr.getBytes("UTF-8"));
			}
			logger.info("createNode " + path);
		}
		
	}

	public static void setDataNode(CuratorFramework client, String path, String message) throws Exception {
		Stat stat = client.checkExists().forPath(path);
		if(stat == null) {
			initNode(client,path);
		}
		client.setData().forPath(path, message.getBytes("UTF-8"));
		logger.info("setDataNode path:" + path + ",msg: " + message);
		

	}
	
	public static String getDataNode(CuratorFramework client, String path) throws Exception {
		Stat stat = client.checkExists().forPath(path);
		if(stat == null) {
			logger.error("path "+path +"not exists!");
			return null;
		}
		byte[] datas = client.getData().forPath(path);
		String ret= new String(datas,"UTF-8");
		logger.info("getDataNode "+path+",msg:"+ret);
		return ret;

	}
	
	public static void main(String[] args) throws Exception {
		
		
		CuratorFramework zkclient=ZookeeperManager.newCurator();
		zkclient.start();
		
		ZookeeperManager.setDataNode(zkclient, "/dsf/command/agent2", "test");
		ZookeeperManager.getDataNode(zkclient, "/dsf/command/agent2");
		zkclient.close();
	}

}
