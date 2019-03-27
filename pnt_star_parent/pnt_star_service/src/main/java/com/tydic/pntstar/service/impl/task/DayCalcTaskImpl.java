package com.tydic.pntstar.service.impl.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tydic.pntstar.dao.CommonDBDao;
import com.tydic.pntstar.service.task.DayCalcTask;
import com.tydic.pntstar.service.task.MonthCalcTask;
import com.tydic.pntstar.util.SpringBeanUtil;
import com.tydic.pntstar.util.Tools;

@Component
public class DayCalcTaskImpl implements DayCalcTask {
	private static Logger logger = Logger.getLogger(DayCalcTaskImpl.class);
	private CommonDBDao dao = (CommonDBDao) SpringBeanUtil.getBean("dbdao");
    private static int pageSize=100;//每次处理的数据条数
    @Autowired
    private MonthCalcTask monthCalcTask;
	@Override
	public void dayCalcTask() {
		List<Map<String,Object>> dataList=null;
		Map<String,Object> queryParam=new HashMap<>();//查询参数
		queryParam.put("pageSize", pageSize);
		try{
			while(true) {
			//获取数据每次获取100条
			dataList= dao.query("QueryPointDayData", queryParam);
			if(Tools.isNull(dataList)) {
				logger.info("未发现数据,结束处理！");
				return;
			}
			Map<String,Object> newCustParam=new HashMap<>();
			Map<String,Object> oldCustParam=new HashMap<>();
			Map<String,Object> param=null;
			for(int i=0;i<dataList.size();i++) {
				try {
					param=dataList.get(i);
					logger.info("**********处理数据中："+param.get("custId"));
					dealdayCalcOne(param,newCustParam,oldCustParam);
					dao.delete("DeletePointDayData", param);
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
		} catch (Exception e){
			logger.error("出账失败");
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}	
	}
	/*
	 * 评定等级
	 */
	public int ratingLevel(Map<String, Object> ratingLevelParam) throws Exception {
		// TODO Auto-generated method stub
		int rank = getRank(Integer.parseInt(ratingLevelParam.get("ratingLevelGrowthValue").toString()));
		if (ratingLevelParam.get("flag").toString().equals("0")){
			dao.insert("InsertPointCustStatus", ratingLevelParam);
		}
		return rank;
		
	}
	public int getRank(int ratingLevelValue){
		if (ratingLevelValue < 600){
			return 3800;
		}else if (ratingLevelValue < 1000){
			return 3100;
		}else if (ratingLevelValue < 1500){
			return 3200;
		}else if (ratingLevelValue < 2300){
			return 3300;
		}else if (ratingLevelValue < 4000){
			return 3400;
		}else if (ratingLevelValue < 6000){
			return 3500;
		}else if (ratingLevelValue < 8000){
			return 3600;
		}else{
			return 3700;
		}
	}

	/*
	 * 获取关键人直享成长值
	 */
	public int findMixKeyManGrowthValue(Map<String,Object> param) throws Exception{
		int keyManGrowthValue = 0;
		Map<String,Object> keyManData=dao.queryForOne("QuerykeyManData", param);
		if(!Tools.isNull(keyManData)) {
			keyManGrowthValue=Integer.parseInt(keyManData.get("value").toString());
		}
		return keyManGrowthValue;
	}
	
	
	public void dealdayCalcOne(Map<String,Object> param,Map<String,Object> newCustParam,Map<String,Object> oldCustParam) throws Exception {
		String flag=param.get("flag").toString();//数据标识
		Map<String,Object> monthParam=new HashMap<>();
		monthParam.put("custId",param.get("custId"));//客户标识
		monthParam.put("dayDataId", param.get("dayDataId"));//数据标识
		int assessLevel=3800;
		if ("0".equals(flag)){//0代表是新客户
			int directGrowthValue = Integer.parseInt(param.get("directGrowthValue").toString());
			newCustParam.put("custId",param.get("custId"));
			int keyManGrowthValue = findMixKeyManGrowthValue(newCustParam);
			int ratingLevelGrowthValue = 0;
			ratingLevelGrowthValue = Math.max(keyManGrowthValue, directGrowthValue);
			
			newCustParam.put("directGrowthValue",directGrowthValue);
			newCustParam.put("ratingLevelGrowthValue",ratingLevelGrowthValue);
			newCustParam.put("flag", "0");
			insertBalanceRecord(newCustParam);
			assessLevel=ratingLevel(newCustParam);
			param.put("membershipLevel",assessLevel);
			dao.insert("InsertClubMemberCalc", param,true);
		}else{
			int directGrowthValue = Integer.parseInt(param.get("directGrowthValue").toString());
			oldCustParam.put("custId",param.get("custId"));
			int keyManGrowthValue = findMixKeyManGrowthValue(oldCustParam);
			int ratingLevelGrowthValue = Integer.parseInt(dao.queryForOne("QueryRatingLevelGrowthValue", oldCustParam).get("ratingLevelGrowthValue").toString());
			int temp = Math.max(keyManGrowthValue, directGrowthValue);
			ratingLevelGrowthValue = Math.max(temp,ratingLevelGrowthValue);
			oldCustParam.put("directGrowthValue",directGrowthValue);
			oldCustParam.put("ratingLevelGrowthValue",ratingLevelGrowthValue);
			oldCustParam.put("flag","1");
			insertBalanceRecord(oldCustParam);
			assessLevel=ratingLevel(oldCustParam);
		}
		monthParam.put("assessLevel", assessLevel);//评定等级
//		monthParam.put("oldStarLevel", oldStarLevel);//旧星级
//		monthParam.put("maxGrewValue", maxGrewValue);//最大成长值
//		monthParam.put("consumValue", consumValue);//消费成长值
//		monthParam.put("flag", flag);//数据标识
		monthCalcTask.dealOne(monthParam);
	}
	/*
	 * 生成相应成长值账户记录
	 */
	public void insertBalanceRecord(Map<String,Object> param) throws Exception {
		if (param.get("flag").toString().equals("0")){
	      		param.put("pointAcctId", dao.getPK("POINT_ACCT"));
				dao.insert("InsertRatingLevelGrowthValueRecord", param, true);
				param.put("pointAcctId", dao.getPK("POINT_ACCT"));
				dao.insert("InsertDirectGrowthValueRecord", param, true);
//				dao.insert("InsertPointAccount", param, true);
//				dao.insert("InsertPointBalance", param, true);
		}else{
				dao.update("UpdateRatingLevelGrowthValueRecord", param, true);
				dao.update("UpdateDirectGrowthValueRecord", param, true);
		}
	}
}
