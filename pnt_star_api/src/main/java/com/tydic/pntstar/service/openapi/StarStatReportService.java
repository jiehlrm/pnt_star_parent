package com.tydic.pntstar.service.openapi;

public interface StarStatReportService {

    /**
     * 星级统计报表
     * @param param
     * @return
     * @throws Exception
     */
    public String getStarStatReport(String param) throws Exception;
    
    /**
     * 表单导出
     * 
     * @return
     * @throws Exception
     */
    public String exportForm(String param, String table_title, String realPath) throws Exception;
    
}
