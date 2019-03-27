package com.tydic.pntstar.service.openapi;

import java.util.List;
import java.util.Map;

public interface PointHighAlertService {

    /**
     * 高额预警表格
     * @param param
     * @return
     * @throws Exception
     */
    public String getHightAlertTable(String param) throws Exception;
    
    /**
     * 高额预警核对操作
     * @param param
     * @return
     * @throws Exception
     */
    public String checkHighAlert(String param) throws Exception;
    
    /**
     * 表单导出
     * @param param
     * @param table_title
     * @param realPath
     * @return
     * @throws Exception
     */
    public String exportForm(String param, String table_title, String realPath) throws Exception;

}
