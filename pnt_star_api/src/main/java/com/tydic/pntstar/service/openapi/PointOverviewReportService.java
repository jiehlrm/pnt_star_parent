package com.tydic.pntstar.service.openapi;

/**
 * 积分总览报表抽象类
 * 
 * @author longinus
 *
 */
public interface PointOverviewReportService {

	/**
     * 获取清单报表
     * 
     * @return
     * @throws Exception
     */
    
    public String getListReport(String param) throws Exception;
    
    /**
     * 表单导出
     * 
     * @return
     * @throws Exception
     */
    public String exportForm(String param, String table_title, String realPath) throws Exception;
    

}
