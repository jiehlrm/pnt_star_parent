package com.tydic.pntstar.service.openapi;
/**
 * 积分冻结/解冻
 * @author longinus
 *
 */
public interface PointReleaseService {

	/**
	 * 查询用户账户状态
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getCustStatus(String param) throws Exception;
	/**
	 * 积分冻结/解冻操作
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String changeCustAcctStatus(String param) throws Exception;
}
