package com.tydic.pntstar.service.province;

/**
 * 关键人管理抽象类
 * 
 * @author D11050
 *
 */
public interface KeymanManageService {

	/**
	 * 查询关键人列表信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getKeymanList(String param) throws Exception;
	
	/**
	 * 查询关键人列息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getKeyman(String param) throws Exception;
	
	/**
	 * 调整关键人类型
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String adjKeymanType(String param) throws Exception;
	

	/**
	 * 新增关键人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String insertKeyman(String param) throws Exception;
	
	/**
	 * 删除关键人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String deleteKeyman(String param) throws Exception;
	
	/**
	 * 下转获取关键人信息资料
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getKeymanInfo(String param) throws Exception;
	
	/**
	 * 更新关键人信息资料
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String updateKeyman(String param) throws Exception;
	
	/**
	 * 查询关键人类型修改列表信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getKeymanChangeList(String param) throws Exception;
	
	/**
	 * 导出关键人列表信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String exportKeymanList(String json,String table_title,String realPath) throws Exception;
	
}
