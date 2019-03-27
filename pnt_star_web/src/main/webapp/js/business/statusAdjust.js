/**
 * 查兑状态调整 autor:唐羽
 *--GLOBAL VARIABLE
 *  |- table [查兑状态表格实例]
 *  |- point_tag [tag的实例]
 *  |- cust_id [客户id]
 * 
 *--FUNCTION DESCRIPTION [方法描述以及部分层级关系]
 *  |- $() [初始方法入口] 
 *     |- initSearch [初始化查询]
 *        |- initLan [初始化本地网查询条件]
 *        |- initStatus [初始化查兑状态的交互]
 *        |- addEvent [页面事件的添加]
 *           |- showCustPoint [点击放大镜图标进行查询事件]
 *           |  |- custForm [客户基本信息表单的初始化]
 *           |  |- initTag [tag的初始化]
 *           |     |- tagChoose [tag切换的事件触发]
 *           |        |- initTable [tag中表格的初始化]
 *           |        |- tableReload [当表格已经被建立过后表格的刷新]
 *           |        |- getTotal [表格数据总数的获取]
 *           |- statusChange [查兑状态修改的提交]
 */


var point_tag = null;
var cust_id = '';
var table = null;
$(document).ready(function () {
    initSearch();
});
/**
 * 初始化查询
 */
function initSearch() {
    initLan();
    initStatus();
    addEvent();
}

/**
 * 初始化本地网 
 */
function initLan() {
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getLan'
    }
    var data = COMMON.getSyncData(param);
    var str = '<div style="display:none;"><a class="hover" data-code="all">全部</a></div>';
    for (var i = 0; i < data.length; i++) {
        str += '<div><a data-name="' + data[i]['NAME'] + '" data-code="' + data[i]['CODE'] + '">' + data[i]['NAME'] + '</a></div>';
    }
    $('#lan_search').html(str);
    $('#lan_search').on('click', 'a', function (e) {
        $('#lan_search').find('a').removeClass('hover');
        $(e.target).addClass('hover');
        $('.lan-select').find('dt').text($(e.target).text());
        $('#lan_search').css('display', 'none');
    });
}
/**
 * 调整状态
 */
function initStatus() {
    $('#adjust_reason').on('click', 'a', function () {
        if (!$(this).hasClass('hover')) {
            $('#adjust_reason').find('a').removeClass('hover');
            $(this).addClass('hover');
        }
    });
}
/**
 * 页面事件添加
 */
function addEvent() {
    //查询事件添加
    $('#doQuery').on('click', function () {
        showCustPoint();
    });
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('.query-object').find('dt').text('请选择');
        $('#cust_num').val('');
    });

    $('#sumbitFile').on('click', function () {
        statusChange();
    });

}
/**
 * 点击放大镜图标进行查询事件
 */
function showCustPoint() {
    //0.先判断是那种类型
    var typeText = $('.query-object').find('dt').text().trim();
    var type = '1';
    if (typeText == '用户号码') {
        type = '1';
    } else if (typeText == '客户编号') {
        type = '2';
    } else {
        layer.alert('请选择查询对象');
        return;
    }
    //1.先判断acct_num是否存在
    var acct_num = $('#cust_num').val().trim();
    if (acct_num == '') {
        layer.msg(type == '1' ? '用户号码必须填写' : '客户编号必须填写');
        return;
    }
    //2.初始化表单
    custForm();

    //3.初始化tag
    initTag();
}

/**
 * 初始化客户的积分表单
 */
function custForm() {
    //0.先判断是那种类型
    var latn_id = $('#lan_search').find('.hover').data('code');
    var typeText = $('.query-object').find('dt').text().trim();
    var type = '1';
    if (typeText == '用户号码') {
        type = '1';
    } else if (typeText == '客户编号') {
        type = '2';
    } else {
        layer.alert('请选择查询对象');
        return;
    }
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getCustFormData',
        'LATN_ID': latn_id,
        'ACCT_NUM': $('#cust_num').val().trim(),
        'TYPE': type
    }
    var data = COMMON.getSyncData(param);
    if (data.length == 0) {
        layer.msg('查无此人信息');
        return;
    }
    var form = data[0];
    for (var o in form) {
        $('.' + o).text(form[o]);
    }
    cust_id = form['CUST_ID'];
}
/**
 * 初始化tag
 */
function initTag() {
    //需要进行tag初始化,否则不需要
    if (point_tag == null) {
        point_tag = $('#pointTag').catTag({
            tags: [{
                value: '1',
                title: '查兑状态调整记录',
                choose: true
            }],
            afterClick: function (p1, p2) {
                tagChoose(p1, p2);
            }
        });
    } else {
        tableReload();
        $('#reason_description').find('textarea').val('');
    }
}
/**
 * tag点击事件
 * @param {*} p1 
 * @param {*} p2 
 */
function tagChoose(p1, p2) {
    //如果存在则刷新
    if (table) {
        tableReload();
    } else {
        //table未存在则新建
        $(p2).html('<div id="statusTable"></div>');
        initTable();
    }

}
/**
 * 表格的刷新
 */
function tableReload() {
    var param1 = {
        'serviceName': 'statusAdjustWebService',
        'methodName': 'getStatusTable',
        'CUST_ID': cust_id
    }
    /*var paramT = {
        'serviceName': 'statusAdjustWebService',
        'methodName': 'getStatusTableTotal',
        'CUST_ID': cust_id
    }*/
    //重设param
    table.setInitParam(param1);
    //重设total
    //table.setTotal(getTotal(paramT));
    //进行刷新
    table.reloadTable();
}
/**
 * 初始化表格
 */
function initTable() {
    var param = {
        'serviceName': 'statusAdjustWebService',
        'methodName': 'getStatusTable',
        'CUST_ID': cust_id
    };
    /*var param1 = {
        'serviceName': 'statusAdjustWebService',
        'methodName': 'getStatusTableTotal',
        'CUST_ID': cust_id
    }*/
    table = $('#statusTable').catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '调整前状态',
            id: 'BEFORE_STATUS_CD'
        }, {
            name: '调整后状台',
            id: 'AFTER_STATUS_CD'
        }, {
            name: '调整时间',
            id: 'CRT_DATE'
        }, {
            name: '操作人',
            id: 'OPERATOR'
        }, {
            name: '操作渠道',
            id: 'SERVICE_CHANNEL'
        }, {
            name: '调整原因',
            id: 'REASON_REMARK'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total']
    });
}
/**
 * 获取数据总量
 * @param {} param 
 */
/*function getTotal(param) {
    var d = COMMON.getSyncData(param);
    if (d.length == 0) {
        return 0;
    }
    return Number(d[0].TOTAL) ? Number(d[0].TOTAL) : 0;
}*/

/**
 * 查兑状态调整
 */
function statusChange() {
    var point_limit = $('#adjust_reason').find('.hover').data('code');
    var reson = $('#reason_description').find('textarea').val().trim();
    if (cust_id == '' || cust_id == undefined || cust_id == null) {
        layer.msg('请先查询出客户');
        return;
    }
    if (reson.length > 50) {
        layer.msg('调整原因请在50字以内');
        return;
    }
    var param = {
        'CUST_ID': cust_id,
        'POINT_LIMIT': point_limit,
        'REASON_REMARK': reson,
        'serviceName': 'statusAdjustWebService',
        'methodName': 'statusAdjust',
    }
    //后台接口的调用
    var callback = function () {
        //1.提示修改成功
        layer.msg("查兑状态修改成功");
        //2.刷新表单
        custForm();
        //3.刷新表格
        tableReload();
    }
    COMMON.doSync(param, callback);
}