package com.tydic.pntstar.service.impl.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.task.MonthCalcTask;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

/** 
* @Title: MonthCalcTaskImpl.java 
* @Package com.tydic.pntstar.service.impl.task 
* @Description: TODO
* @author weixsa@gmail.com 
* @date 2019年1月18日 上午11:05:05 
* @version V1.0 
*/
@Component
public class MonthCalcTaskImpl implements MonthCalcTask,InitializingBean {
	private static Logger logger = Logger.getLogger(MonthCalcTaskImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");

	private static int pageSize=100;//每次处理的数据条数
	
	private static Map<String,List<Map<String,Object>>> serviceList=null;//星级权益列表
	
	private static List<Map<String,Object>> starList=null;//星级等级列表
	
	private static Map<String,Object> growValueList=null;//等级成长值列表
	
	@Override
	public void monthCalcTask() {
		List<Map<String,Object>> dataList=null;
		Map<String,Object> queryParam=new HashMap<>();//查询参数
		queryParam.put("pageSize", pageSize);
		try{
			while(true) {
				dataList= dao.query("QueryPointMonthData", queryParam);//获取数据每次获取100条
				if(Tools.isNull(dataList)) {
					logger.info("未发现数据,结束处理！");
					return;
				}			
				Map<String,Object> param=null;
				for(int i=0;i<dataList.size();i++) {
					try {
						param=dataList.get(i);
						logger.info("**********处理数据中："+param.get("custId"));
						dealOne(param);
						//5：删除临时表中的处理完的数据
						dao.delete("DeletePointMonthData", param,true);
						dao.commit();
					}catch (Exception e) {
						// TODO Auto-generated catch block
						dao.rollback();
						e.printStackTrace();
						logger.info("*****数据处理异常"+param.toString());
						logger.info(e.getMessage(),e);
					}finally {
						dao.release();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}	

	}
	
	public void dealOne(Map<String,Object> param) throws Exception {
		    param=refreshLevel(param);//信息转换
			//0：查询俱乐部会员信息
			Map<String,Object> clubMember=dao.queryForOne("QueryClubMember",param);
			clubMember.put("clubMemberId",clubMember.get("id"));
			clubMember.remove("id");
			if(Tools.isNull(clubMember)) {
				throw new Exception("未找到该积分客户信息");
			}
			//0：更新高额预警信息
			int consumValue=param.get("consumValue")==null?0:(int)param.get("consumValue");
			if(consumValue>3000) {
				param.put("pointHighLimit",11);
				dao.update("UpdatePointCustStatus",param,true);
			}
			param.putAll(clubMember);//参数增强
			//1：更新俱乐部会员等级信息
			dao.update("UpdateClubMember", param,true);
			//2：插入会员等级评定表记录
//		    param.put("recordId",dao.getPK("MEMBER_ASSESS_RECORD"));
			dao.insert("InsertMemberAssessRecord", param,true);
			//0：未评级原因表
			if((int)param.get("assessLevel")==3800) {
				dao.insert("InsertPointNotCalcCust", param);
			}
			//3：更新会员账户服务记录表
			dao.update("UpdateMemServAcct", param,true);
			//4：插入会员账户权益信息
			logger.info("获取权益参数"+serviceList.get(param.get("assessLevel").toString()));
			List<Map<String,Object>> data= (List<Map<String, Object>>) serviceList.get(param.get("assessLevel").toString());
			if(!Tools.isNull(data)) {				
				for(Map<String,Object> temp:data) {
					temp.putAll(param);
//					temp.put("memServAcctId", dao.getPK("MEM_SERV_ACCT"));
					dao.insert("InsertMemServAcct",temp,true);
				}
			}
		
	}
	
 
	/**
	 * 1：星级重测，具有保级功能
	 */
	public Map refreshLevel(Map<String,Object> param) {
		//重新评测记录
		if((int)param.get("effDate")<=Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {// ：重新评测   保级校验  90%   dict中获取等级参数
			int maxGrewValue=param.get("maxGrewValue")==null?0:(int)param.get("maxGrewValue");
			//获取
			int oldStarLevel=param.get("oldStarLevel")==null?0:(int)param.get("oldStarLevel");//旧星级
			int assessLevel=param.get("assessLevel")==null?0:(int)param.get("assessLevel");//评定星级
			int assessLevelCast=assessLevel==3800?3000:assessLevel;//评定星级
			int oldStarLevelCast=oldStarLevel==3800?3000:oldStarLevel;
			//不相等评定等级低保级校验
			//相等直接评级
			if(assessLevel==oldStarLevel){
				return param;
			}else if(assessLevelCast<oldStarLevelCast) {
				assessLevel=maxGrewValue>=(int) ((int) growValueList.get("oldStarLevel")*0.9)?oldStarLevel:assessLevel;
				param.put("assessLevel", assessLevel);
				return param;
			}
		}
		return param;
		
	}
	
	
	/* 
	 * 初始化等级和权益信息
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if(Tools.isNull(starList)) {
			starList=dao.query("QueryPointStarList", null);
		}
		if(Tools.isNull(serviceList)) {//获取星级权益
			serviceList=new HashMap<>();
			for(int i=0;i<starList.size();i++) {
				serviceList.put(starList.get(i).get("code").toString(),dao.query("QueryClubMbrLevServiceRelInit", starList.get(i)));				
			}
		}
		if(Tools.isNull(growValueList)) {//获取星级最低成长值
			growValueList=new HashMap<String,Object>();
			for(Map temp:dao.query("QueryPointGrewValueList", null)) {
				growValueList.put(temp.get("code").toString(),temp.get("value").toString());				
			}
		}
		logger.info("星级参数加载完成");
	}

}
