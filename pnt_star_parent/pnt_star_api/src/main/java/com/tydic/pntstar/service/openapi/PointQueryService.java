package com.tydic.pntstar.service.openapi;

/**
 * 积分查询抽象类
 * 
 * @author longinus
 *
 */
public interface PointQueryService {

	/**
	 * 获取本地网信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLan() throws Exception;

	/**
	 * 获取客户积分表单信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getCustFormData(String param) throws Exception;
	
	/**
	 * 获取年积分
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getYearPoint(String param)  throws Exception;
	
	/**
	 * 获取年积分明细
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getYearDetail(String param) throws Exception;
	
	/**
	 * 获取年积分明细总数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getYearDetailTotal(String param) throws Exception;
	
	/**
	 * 获取获取积分账本变更记录
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getChangeList(String jsonString)throws Exception;
}
