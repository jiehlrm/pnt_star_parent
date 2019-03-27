/**
 * 公用JS
 */

/*
 * 待修改：传入参数（obj），调用不同方法时依据页面参数（latn_id等）调用后台方法（service,method,param）
 */

/* 表格属性参数 */
var table = null;
var field = null;
var style = null;
var comp = null;
var footer = false;
var decorate = null;
var tableHead = null;

/*
 * 页面参数 var latn_id = null; // 本地网 var account_period = null; // 账期
 */
var checkParam = null;
var dataParam = null;
var exportParam = null;

/* 其他 */
var events = [];
var pattern = /^\d{4}(0?[1-9]|1[0-2])$/; // 账期正则表达式，例：201801
function common(obj) {
    /* 表格属性参数赋值 */
    if (obj['field'] != undefined) {
        field = obj['field'];
    }
    if (obj['style'] != undefined) {
        style = obj['style'];
    }
    if (obj['comp'] != undefined) {
        comp = obj['comp'];
    }
    if (obj['footer'] != undefined) {
        footer = obj['footer'];
    }
    if (obj['decorate'] != undefined) {
        decorate = obj['decorate'];
    }
    if (obj['head'] != undefined) {
        tableHead = obj['head'];
    }

    /* 页面参数赋值 */
    if (obj['checkParam'] != undefined) {
        checkParam = obj['checkParam'];
    }
    if (obj['dataParam'] != undefined) {
        dataParam = obj['dataParam'];
    }
    if (obj['exportParam'] != undefined) {
        exportParam = obj['exportParam'];
    }

    /* 事件添加 */
    if (obj['events'] != undefined) {
        events = obj['events'];
        for (var i = 0; i < events.length; i++) {
            // 根据字符串执行方法
            if (events[i] != null || events.trim() != "") {
                eval(events[i] + "()");
            }
        }
    }
}
/**
 * 初始化本地网
 */
function initLan() {
    var param = {
        'serviceName' : 'pointQueryService',
        'methodName' : 'getLan'
    }
    var data = COMMON.getSyncData(param);
    var str = '<div style="display:none;"><a class="hover" data-code="all">全部</a></div>';
    for (var i = 0; i < data.length; i++) {
        str += '<div><a data-name="' + data[i]['NAME'] + '" data-code="'
                + data[i]['CODE'] + '">' + data[i]['NAME'] + '</a></div>';
    }
    $('#lan_search').html(str);
    $('#lan_search').on('click', 'a', function(e) {
        $('#lan_search').find('a').removeClass('hover');
        $(e.target).addClass('hover');
        $('.lan-select').find('dt').text($(e.target).text());
        $('#lan_search').css('display', 'none');
    });
}
/**
 * 初始化核对状态
 */
function initPointLimit() {
    var str = '<div style="display:none;"><a class="hover" data-code="all">全部</a></div>';
    for (var i = 0; i < pointLimit.length; i++) {
        str += '<div><a data-name="' + pointLimit[i]['name'] + '" data-code="'
                + pointLimit[i]['code'] + '">' + pointLimit[i]['name']
                + '</a></div>';
    }
    $('#point_limit').html(str);
    $('#point_limit').on('click', 'a', function(e) {
        $('#point_limit').find('a').removeClass('hover');
        $(e.target).addClass('hover');
        $('.point-limit').find('dt').text($(e.target).text());
        $('#point_limit').css('display', 'none');
    });
}
// 查询图标的事件添加
function doQueryEvent() {
    $('#doQuery').on('click', function() {
        queryData();
    });
}
// 导出按钮事件添加
function exportFormEvent() {
    $('#exportForm').on('click', function() {
        exportForm();
    })
}
// 重置按钮事件添加
function resetEvent() {
    $('#resetQuery').on('click', function() {
        $('.lan-select').find('dt').text('请选择');
        $('#account_period').val('');
        /*--高额预警--*/
        $('.point-limit').find('dt').text('请选择');
        $('#acc_num').val('');
    });
}
/* 页面参数检查 */
function check() {
    var result = true;
    for (var i = 0; i < checkParam.length; i++) {
        switch (checkParam[i]) {
        case 'latn_id':
            latn_id = $('#lan_search').find('.hover').data('code');
            if (latn_id == 'all') {
                layer.msg('请选择本地网');
                result = false;
            }
            if (dataParam != null) {
                dataParam['LATN_ID'] = latn_id;
            }
            if (exportParam != null) {
                exportParam['LATN_ID'] = latn_id;
            }
            break;
        case 'account_period':
            account_period = $('#account_period').val().trim();
            if (account_period == '' || account_period == null) {
                layer.msg('账期必须填写');
                result = false;
            }
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            var strDate = year + "" + month;
            if (parseInt(account_period) > parseInt(strDate)) {
                layer.msg('账期大于当前月份');
                result = false;
            }
            if (dataParam != null) {
                dataParam['ACCOUNT_PERIOD'] = account_period;
            }
            if (exportParam != null) {
                exportParam['ACCOUNT_PERIOD'] = account_period;
            }
            break;
        case 'point_limit' :
            point_limit = $('#point_limit').find('.hover').data('code');
            if (dataParam != null) {
                dataParam['POINT_LIMIT'] = point_limit;
            }
            if (exportParam != null) {
                exportParam['POINT_LIMIT'] = point_limit;
            }
            break;
        case 'acc_num' :
            acc_num = $('#acc_num').val().trim();
            if (acc_num == '') {
                layer.msg('用户号码必须填写');
                result = false;
            }
            if (dataParam != null) {
                dataParam['ACC_NUM'] = acc_num;
            }
            if (exportParam != null) {
                exportParam['ACC_NUM'] = acc_num;
            }
            break;
        }
        if (result == false) {
            break;
        }
    }
    return result;
}
/**
 * 查询表单结果
 */
function queryData() {
    // 参数判断
    if (check() == false) {
        return;
    }

    // 渲染表格
    if (table == null) {
        table = $('#pointForm').catTable({
            data : COMMON.getSyncData,// 方法从mock.js中来，模拟从服务器获取数据
            thead : tableHead,
            thField : field,
            thStyle : style,
            dataFrom : 'Server-T',
            param : dataParam,
            dataComp : comp,
            showFooter : footer,
            decorate : decorate
        });
        return table;
    } else {
        table.setInitParam(dataParam);
        table.reloadTable();
    }
}
/**
 * 导出表单
 */
function exportForm() {
    // 参数判断
    if (check() == false) {
        return;
    }
    $.ajax({
        type : 'post',
        url : nowurl + "ControlServlet.do",
        data : exportParam,
        async : false,
        dataType : 'json',
        success : function(data) {
            if (data.resultCode == '00000') {
                // 服务器生成excel完毕
                layer.msg('导出成功');
                window.open(nowurl + data.filePath);
            } else {
                layer.msg('导出失败');
                return;
            }
        }
    });
}