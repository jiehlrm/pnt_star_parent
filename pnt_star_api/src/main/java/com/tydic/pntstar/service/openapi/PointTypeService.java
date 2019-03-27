package com.tydic.pntstar.service.openapi;

/**
 * 积分类型抽象类
 * 
 * @author hxc
 *
 */
public interface PointTypeService {

    /**
     * 查询积分类型
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String queryPointType(String param) throws Exception;
    
    /**
     * 查询积分类型组
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String queryPointTypeGroup(String param) throws Exception;
    
    /**
     * 查询积分类型组成员
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String queryPointTypeGroupMbr(String param) throws Exception;
    
    /**
     * 新增积分类型
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String addPointType(String param) throws Exception;
    
    /**
     * 新增积分类型组
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String addPointTypeGroup(String param) throws Exception;
    
    /**
     * 修改积分类型
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String updatePointType(String param) throws Exception;
    
    /**
     * 修改积分类型组
     * @param param 
     * 
     * @return
     * @throws Exception
     */
    public String updatePointTypeGroup(String param) throws Exception;
    
}
