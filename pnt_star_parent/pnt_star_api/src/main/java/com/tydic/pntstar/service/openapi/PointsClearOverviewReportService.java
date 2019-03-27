package com.tydic.pntstar.service.openapi;

public interface PointsClearOverviewReportService {
	/**
     * 获取待清零积分总览报表
     * 
     * @return
     * @throws Exception
     */ 
	public String queryPointsClearOverviewReport(String json) throws Exception; 
	/**
     * 导出
     * 
     * @return
     * @throws Exception
     */
	 public String exportPointsClearOverviewReport(String json, String table_title, String realPath) throws Exception;
}
