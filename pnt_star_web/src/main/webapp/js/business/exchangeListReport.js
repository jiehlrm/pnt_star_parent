var latn_id = null;
var account_period = null;
var head = [ {
    name : '区域',
    id : 'LATN_NAME'
}, {
    name : '营业区',
    id : 'REGION_NAME'
}, {
    name : '月份',
    id : 'MONTH_ID'
}, {
    name : '客户ID',
    id : 'CUST_ID'
}, {
    name : '客户名称',
    id : 'CUST_NAME'
}, {
    name : '登录时号码',
    id : 'LOGIN_NBR'
}, {
    name : '兑换时间',
    id : 'EXC_TIME'
}, {
    name : '兑换数量',
    id : 'EXC_NUM'
}, {
    name : '单个分数',
    id : 'UNIT_SCORE'
}, {
    name : '兑换分数',
    id : 'EXC_SCORE'
}, {
    name : '兑换产品名称',
    id : 'EXC_GIFT_NAME'
}, {
    name : '兑换渠道',
    id : 'EXC_CHANNEL'
}, {
    name : '兑换单号',
    id : 'EXC_NBR'
}, {
    name : '兑换人员ID',
    id : 'OPR_ID'
}, {
    name : '兑换人员姓名',
    id : 'OPR_NAME'
}, {
    name : '营业厅',
    id : 'OPR_BUSI'
} ];
$(document).ready(
        function() {
            var param = {
                'field' : null,
                'style' : null,
                'comp' : [ 'data' ],
                'footer' : false,
                'head' : head,
                'events' : [ 'initLan', 'doQueryEvent', 'exportFormEvent',
                        'resetEvent' ],
                'checkParam' : [ 'latn_id', 'account_period' ],
                'dataParam' : {
                    'serviceName' : 'pointExchangeListReportService',
                    'methodName' : 'getListReport',
                    'LATN_ID' : latn_id,
                    'ACCOUNT_PERIOD' : account_period
                },
                'exportParam' : {
                    'serviceName' : 'pointExchangeListReportService',
                    'methodName' : 'exportForm',
                    'LATN_ID' : latn_id,
                    'ACCOUNT_PERIOD' : account_period,
                    'TABLE_TITLE' : JSON.stringify(head)
                }
            }
            common(param);
        });