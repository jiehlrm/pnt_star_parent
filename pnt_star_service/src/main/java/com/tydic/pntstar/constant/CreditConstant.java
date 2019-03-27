package com.tydic.pntstar.constant;

public interface CreditConstant {

    public interface action {
        final static String QUERY_ACTION = "QUERY_CREDITLIMIT_QUERYCREDITLIMIT";
        final static String ADD_ACTION = "INSERT_CREDITLIMIT_ADDCREDITLIMIT";
        final static String MODIFY_ACTION = "UPDATE_CREDITLIMIT_MODIFYCREDITLIMIT";
        final static String DELETE_ACTION = "DELETE_CREDITLIMIT_DELCREDITLIMIT";
        final static String QUERY_TABLE_ACTION = "QUERY_CREDITWEB_CREDITTABLE";
        final static String COUNT_TABLE_ACTION = "QUERY_CREDITWEB_CREDITTABLE_C";
        final static String QUERY_TABLE_HEAD_ACTION = "QUERY_QUERYTABLEHEAD";
        final static String QUERY_LAND_AND_REGION = "QUERY_LAND_AND_REGION";
        final static String QUERY_INTERGRATED_OPTIONS = "QUERY_INTERGRATED_OPTIONS";
        final static String QUERY_QUERYINTERGRATED = "QUERY_QUERYINTERGRATED";
        final static String COUNT_QUERYINTERGRATED = "QUERY_QUERYINTERGRATED_C";
        final static String MODIFY_MODIFYBATCH = "MODIFY_MODIFYBATCH";
        final static String QUERY_QUERYAUDIT = "QUERY_QUERYAUDIT";
        final static String COUNT_QUERYAUDIT = "QUERY_QUERYAUDIT_C";
        final static String ADD_SUBMIT_ADJUST_ACTION = "ADD_SUBMIT_ADJUST_ACTION";
    }
    
    public interface resultMsg {
        final static String QUERY_SUCCESS_MSG = "查询信用额度成功";
        final static String QUERY_FAIL_MSG = "未查到有效数据";
        final static String ADD_SUCCESS_MSG = "添加信用额度成功";
        final static String ADD_FAIL_MSG = "添加信用额度失败";
        final static String MODIFY_SUCCESS_MSG = "修改信用额度成功";
        final static String MODIFY_FAIL_MSG = "修改信用额度失败";
        final static String DELETE_SUCCESS_MSG = "删除信用额度成功";
        final static String DELETE_FAIL_MSG = "删除信用额度失败";
        final static String QUERY_LIST_SUCCESS_MSG = "查询信用额度列表成功";
        final static String QUERY_LIST_FAIL_MSG = "未查到有效数据";
        final static String QUERY_TABLE_SUCCESS_MSG = "查询WEB端数据成功";
        final static String QUERY_TABLE_FAIL_MSG = "查询WEB端数据失败";
        final static String QUERY_TABLE_HEAD_SUCCESS_MSG = "查询WEB端表头成功";
        final static String QUERY_TABLE_HEAD_FAIL_MSG = "查询WEB端表头失败";
        final static String MODIFY_TABLE_DATA_SUCCESS_MSG = "修改数据库表成功";
        final static String MODIFY_TABLE_DATA_FAIL_MSG = "修改数据库表失败";
        final static String QUERY_LAND_AND_REGION_SUCCESS_MSG = "查询本地网及服务区成功";
        final static String QUERY_LAND_AND_REGION_FAIL_MSG = "查询本地网及服务区失败";
        final static String QUERY_INTERGRATED_OPTIONS_FAIL_MSG = "查询信用度综合查询选项失败";
        final static String QUERY_QUERYINTERGRATED_SUCCESS_MSG = "信用度综合查询成功";
        final static String QUERY_QUERYINTERGRATED_FAIL_MSG = "信用度综合查询失败";
        final static String QUERY_QUERYAUDIT_SUCCESS_MSG = "待审批查询成功";
        final static String QUERY_QUERYAUDIT_FAIL_MSG = "待审批查询失败";
        final static String ADD_CREDIT_LOG_FAIL_MSG = "插入信用度日志失败";
    }
    
}
