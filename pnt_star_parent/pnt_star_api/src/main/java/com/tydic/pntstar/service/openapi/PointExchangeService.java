package com.tydic.pntstar.service.openapi;
/**
 * 积分兑换
 * @author longinus
 *
 */
public interface PointExchangeService {
	/**
	 * 礼品表格
	 * @param param
	 * @return
	 * @throws Exception
	 */
	 public String getGiftTable(String param) throws Exception;
	 /**
	  * 积分兑换礼品
	  * @param param
	  * @return
	  * @throws Exception
	  */
	 public String exchangeGiftByPoint(String param) throws Exception;

}
