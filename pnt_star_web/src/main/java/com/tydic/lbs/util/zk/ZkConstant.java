package com.tydic.lbs.util.zk;

/**
 * Auther: wly
 * Date: 2018-09-19 09:25
 * Description:
 */
public class ZkConstant {

    //Zookeeper连接超时时间
    public static String ZOOKEEPER_CONNECTION_TIMEOUT = "zookeeper.connection.timeout";
    //zookeeper连接session超时时间
    public static String ZOOKEEPER_SESSION_TIMEOUT = "zookeeper.session.timeout";
    //zookeeper重试间隔时间
    public static String ZOOKEEPER_RETRY_INTERVAL = "zookeeper.retry.interval";
    //zookeeper重试最大次数
    public static String ZOOKEEPER_RETRY_INTERVAL_CEILING = "zkRetryIntervalCeiling";
    //zookeeper重试最大次数
    public static String ZOOKEEPER_RETRY_TIMES = "zookeeper.retry.times";
    //zookeeper集群主机列表
    public static String ZOOKEEPER_CLUSTER_SERVERS = "zookeeper.cluster.zkServers";
    
    //zookeeper用户名和密码root:root形式
    public static String ZOOKEEPER_CLUSTER_AUTH = "zookeeper.cluster.auth";
    
    //agent监控worker目录
    public static String ZOOKEEPER_AGENT_WORKER_PATH = "/dsf/command/agent";
    //默认字符集编码
    public static String DEFAULT_ENCODING = "UTF-8";
    //目录分割符
    public static String FILE_SEPARATOR = "/";//File.separator;
}
