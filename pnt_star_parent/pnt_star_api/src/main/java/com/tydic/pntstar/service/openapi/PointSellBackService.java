package com.tydic.pntstar.service.openapi;

/**
 * 积分返销抽象类
 * @author longinus
 *
 */
public interface PointSellBackService {
	/**
	 * 获取积分返销表格
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPointSellBackTable(String param) throws Exception;
	/**
	 * 获取积分返销表格数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPointSellBackTableTotal(String param) throws Exception;
	/**
	 * 积分返销操作
	 * @param param
	 * @throws Exception
	 */
	public String sellBack(String param) throws Exception;

}
