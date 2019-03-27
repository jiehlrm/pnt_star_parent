package com.tydic.pntstar.service.openapi;

/**
 * 兑换积分总览报表抽象类
 * 
 * @author hxc
 *
 */
public interface PointExchOverviewReportService {

   
    
    
    public String queryPointExchange(String json) throws Exception;
    
    public String exportPointExchange(String json, String table_title, String realPath) throws Exception;
    
}
