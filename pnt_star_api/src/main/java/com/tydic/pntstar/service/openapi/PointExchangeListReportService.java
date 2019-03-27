package com.tydic.pntstar.service.openapi;

import java.util.List;
import java.util.Map;

/**
 * 兑换清单报表抽象类
 * 
 * @author hxc
 *
 */
public interface PointExchangeListReportService {

    /**
     * 获取本地网信息
     * 
     * @return
     * @throws Exception
     */
    public String getLan() throws Exception;
    
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
