package com.tydic.lbs.frame;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tydic.lbs.entity.Constants;
import com.tydic.lbs.util.DateUtil;
import com.tydic.lbs.util.zk.ZookeeperManager;
/***
 * 系统参数加载，采用按需加载的方式，用到的时候才去后台加载，并且放入当前应用内存，不是一次性加载
 * 主要目的是
 * 1、提高启动效率
 * 2、减少网络开销防止一次restful接口请求返回的数据传送量太大造成接口超时
 * @author Administrator
 *
 */
@Service("loadSysData")
public class LoadSysData {
	CuratorFramework zkclient = null;
	private static final String tableHeadPath = "/pntstar/monitor/tableHead";
	private static final String flowInfoPath = "/pntstar/monitor/flowInfo";
	
	private static  String tableHeadPathNowStr="";
	private static  String flowInfoPathNowStr="";

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public LoadSysData() {
		zkclient =ZookeeperManager.newCurator(); 
		try {
			//开启监控
			monitor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @throws Exception *
	 * 
	 * 
	 */
	public void monitor() throws Exception {
		monitorTableHeadPath(zkclient);
		monitorFlowInfo(zkclient);
	}

	public  void monitorTableHeadPath(CuratorFramework client) throws Exception {
		 /*
		 * 节点路径不存在时，set不触发监听
		 * 节点路径不存在，，，创建事件触发监听
		 * 节点路径存在，set触发监听
		 * 节点路径存在，delete触发监听
		 *
		 *
		 * 节点挂掉，未触发任何监听
		 * 节点重连，未触发任何监听
		 * 节点重连 ，恢复监听
		 * */
		
		//先创建一个节点,
		ZookeeperManager.initNode(client,tableHeadPath);
		
		final NodeCache nodeCache = new NodeCache(client, tableHeadPath, false);
		// 如果为true则首次不会缓存节点内容到cache中，默认为false,设置为true首次不会触发监听事件
		nodeCache.start(false);
		//比较时间戳，如果时间戳不一致则进行刷新操作，并更新当前的时间戳位zk上的时间戳
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				String nowDate=new String(nodeCache.getCurrentData().getData());
				if(!tableHeadPathNowStr.equals(nowDate)) {
					logger.info("------freshTableHeadInfo-----"+nowDate);
					tableHeadPathNowStr=nowDate;
					Constants.clearTableHead();
				}
			}
		});
		logger.info("------start monitor -----"+tableHeadPath);

	}
	
	
	public  void monitorFlowInfo(CuratorFramework client) throws Exception {
		 /*
		 * 节点路径不存在时，set不触发监听
		 * 节点路径不存在，，，创建事件触发监听
		 * 节点路径存在，set触发监听
		 * 节点路径存在，delete触发监听
		 *
		 *
		 * 节点挂掉，未触发任何监听
		 * 节点重连，未触发任何监听
		 * 节点重连 ，恢复监听
		 * */
		//先创建一个节点
		ZookeeperManager.initNode(client,flowInfoPath);
				
		final NodeCache nodeCache = new NodeCache(client, flowInfoPath, false);
		nodeCache.start(false);
		
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				//比较时间戳，如果时间戳不一致则进行刷新操作，并更新当前的时间戳位zk上的时间戳
				String nowDate=new String(nodeCache.getCurrentData().getData());
				if(!flowInfoPathNowStr.equals(nowDate)) {
					logger.info("------freshFlowInfo-----"+nodeCache.getCurrentData().getData());
					flowInfoPathNowStr=nowDate;
					Constants.clearUrlMap();
				}
			}
		});
		logger.info("------start monitor -----"+flowInfoPath);

	}
	
	public void handFreshTableHead() throws Exception {
		//写入zk节点,通知所有web程序更新数据
		String nowStr=DateUtil.getNowTimeMs();
		ZookeeperManager.setDataNode(zkclient, tableHeadPath, nowStr);
	}
	public void handFreshFlowInfo() throws Exception {
		//写入zk节点,通知所有web程序更新数据
		String nowStr=DateUtil.getNowTimeMs();
		ZookeeperManager.setDataNode(zkclient, flowInfoPath, nowStr);
	}
}
