package com.tydic.pntstar.service.openapi;
/**
 * 查兑状态调整的抽象类
 * @author longinus
 *
 */
public interface StatusAdjustService {
	/**
	 * 获取查兑状态表格
	 * @param param
	 * @return
	 * @throws Exception
	 */
   public String getStatusTable(String param) throws Exception;
   /**
	 * 获取查兑状态表格数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
  public String getStatusTableTotal(String param) throws Exception;
   
   /**
    * 状态调整
    * @param param
    * @throws Exception
    */
   public void statusAdjust(String param) throws Exception;
}
