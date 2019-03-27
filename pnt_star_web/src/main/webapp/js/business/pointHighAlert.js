var table = null;
var latn_id = null;
var point_limit = null;
var acc_num = null;
var head = [ {
    name : '客户名称',
    id : 'CUST_NAME'
}, {
    name : '客户编码',
    id : 'CUST_NUMBER'
}, {
    name : '预警类型',
    id : 'POINT_HIGH_LIMIT'
}, {
    name : '月消费积分',
    id : 'AMOUNT_MONTH'
}, {
    name : '三年消费积分',
    id : 'AMOUNT_THREE_YEAR'
}, {
    name : '账期',
    id : 'ACCOUNT_PERIOD'
}, {
    name : '生成日期',
    id : 'HIGHT_CREATE_DATE'
}, {
    name : '核对状态',
    id : 'POINT_LIMIT'
}, {
    name : '核对时间',
    id : 'HIGHT_UPDATE_DATE'
}, {
    name : '核对情况说明',
    id : 'POINT_LIMIT_REASON'
}, {
    name : '操作',
    id : 'ACTION'
} ]
var pointLimit = [ {
    name : '未达起兑',
    code : '10'
}, {
    name : '可查可兑',
    code : '11'
}, {
    name : '可查禁兑',
    code : '12'
}, {
    name : '禁查禁兑',
    code : '13'
} ]
$(document).ready(
        function() {
            // 删除操作表头
            var temp_table_head = head.slice(0, head.length - 1);
            var table_title = JSON.stringify(temp_table_head);
            var param = {
                'field' : null,
                'style' : null,
                'comp' : [ 'data', 'total' ],
                'footer' : false,
                'head' : head,
                'events' : [ 'initLan', 'initPointLimit', 'doQueryEvent', 'exportFormEvent', 'resetEvent' ],
                'checkParam' : [ 'latn_id', 'point_limit', 'acc_num' ],
                'decorate' : {
                    'ACTION': function (row) {
                        return '<a data-cust_name="' + row['CUST_NAME'] + '" ' + 'data-cust_number="' + row['CUST_NUMBER'] + '" ' + 'data-amount_month="' + row['AMOUNT_MONTH'] + '" ' + 'data-cust_id="' + row['CUST_ID'] + '" style="color:#4284d7" class="check">核对</a>';
                    }
                },
                'dataParam' : {
                    'serviceName' : 'pointHighAlertWebService',
                    'methodName' : 'getHightAlertTable',
                    'LATN_ID' : latn_id,
                    'POINT_LIMIT' : point_limit,
                    'ACC_NUM' : acc_num
                },
                'exportParam' : {
                    'serviceName' : 'pointHighAlertWebService',
                    'methodName' : 'exportForm',
                    'LATN_ID' : latn_id,
                    'POINT_LIMIT' : point_limit,
                    'ACC_NUM' : acc_num,
                    'TABLE_TITLE' : table_title
                }
            }
            common(param);
            //页面内单独的逻辑
            addEvent();
        });

/**
 * 页面事件添加
 */
function addEvent() {
    // 核对按钮事件添加
    $('body').on('click', '.check', function(e) {
        var cust_id = $(e.target).data('cust_id');
        var cust_name = $(e.target).data('cust_name');
        var cust_number = $(e.target).data('cust_number');
        var amount_month = $(e.target).data('amount_month');
        var point_limit = $('#point_limit').find('.hover').data('code');
        checkEvent(cust_id, cust_name, cust_number, amount_month, point_limit);
    });
}
/**
 * 核对
 */
function checkEvent(cust_id, cust_name, cust_number, amount_month, point_limit) {
    // 弹出框
    var dialog = art
            .dialog({
                title : '<div style="width:600px;"><div class="pointQuery_title" style="margin: 0px;"><span>月初账单明细</span><span style="float:right;"><a class="aui_close">×</a></span></div></div>',
                cancel : false,
                width : '600px',
                height : '400px',
                content : $(addHdczHtml(cust_name, cust_number, amount_month,
                        point_limit))[0],
                lock : true,
                drag : false, // 禁止弹出层拖动
                resize : false
            // 禁止放大缩小
            });
    // 弹出框关闭按钮事件添加
    $('.aui_close').click(function() {
        dialog.close();
    });
    // 弹出框核对状态事件添加
    $('#point_limit_alert').on('click', 'a', function() {
        if (!$(this).hasClass('hover')) {
            $('#point_limit_alert').find('a').removeClass('hover');
            $(this).addClass('hover')
        }
    });
    // 弹出框保存按钮事件添加
    $('#save').click(function() {
        var point_limit = $('#point_limit_alert').find('.hover').data('code');
        var point_limit_reason = $('#point_limit_reason').val();
        save(cust_id, point_limit, point_limit_reason, dialog);
    });
}
/**
 * 保存
 */
function save(cust_id, point_limit, point_limit_reason, dialog) {
    // 参数判断
    if (cust_id == '' || cust_id == null) {
        layer.msg('未传递用户id');
        return;
    }
    if (point_limit == '' || point_limit == null) {
        layer.msg('未传递核对状态');
        return;
    }
    if (point_limit_reason == '' || point_limit_reason == null) {
        layer.msg('未传递核对情况说明');
        return;
    }
    // 组装参数
    var param = {
        'serviceName' : 'pointHighAlertWebService',
        'methodName' : 'checkHighAlert',
        'CUST_ID' : cust_id,
        'POINT_LIMIT' : point_limit,
        'POINT_LIMIT_REASON' : point_limit_reason
    };
    var data = COMMON.getSyncData(param);
    // 关闭弹出框
    dialog.close();
    // 表格刷新
    table.reloadTable();
}