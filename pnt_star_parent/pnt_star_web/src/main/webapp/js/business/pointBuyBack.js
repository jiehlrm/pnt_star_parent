/**
 * 积分返销 author tangyu
 *--GLOBAL VARIABLE
 *  |- table [表格实例]
 *  |- point_tag [tag的实例]
 *  |- cust_id [客户id]
 * 
 *--FUNCTION DESCRIPTION [方法描述以及部分层级关系]
 *  |- $() [初始方法入口] 
 *     |- initSearch [初始化查询]
 *        |- initLan [初始化本地网查询条件]
 *        |- addEvent [页面事件的添加]
 *           |- showCustPoint [点击放大镜图标进行查询事件]
 *           |  |- custForm [客户基本信息表单的初始化]
 *           |  |- initTag [tag的初始化]
 *           |     |- tagChoose [tag切换的事件触发]
 *           |        |- initTable [tag中表格的初始化]
 *           |- sellback [返销操作]
 */
var cust_id = '';
var table = null;
var point_tag = null;
$(document).ready(function () {
    initSearch();
});
/**
 * 初始化查询
 */
function initSearch() {
    initLan();
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

    $('body').on('click', '.sellback', function (e) {
        var extSerialId = $(e.target).data('seria');
        sellback(extSerialId);
    });
}

/**
 * 点击放大镜图标进行查询事件
 */
function showCustPoint() {
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
 * 初始化客户基本信息表单
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
                title: '礼品返销',
                choose: true
            }],
            afterClick: function (p1, p2) {
                tagChoose(p1, p2);
            }
        });
    } else {
        //否则需要进行刷新
        var param = {
            'serviceName': 'pointSellBackWebService',
            'methodName': 'getPointSellBackTable',
            'CUST_ID': cust_id
        };
        table.setInitParam(param);
        table.reloadTable();
    }
}
/**
 * tag的点击事件
 * @param {*} p1 
 * @param {*} p2 
 */
function tagChoose(p1, p2) {
    if (table) {
        table.reloadTable();
    } else {
        initTable(p2);
    }

}

function initTable(p2) {
    var param = {
        'serviceName': 'pointSellBackWebService',
        'methodName': 'getPointSellBackTable',
        'CUST_ID': cust_id
    };
    table = $(p2).catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '礼品名称',
            id: 'POINT_EXCH_OBJ_NAME'
        }, {
            name: '兑换渠道',
            id: 'EXCH_CHNL_ID'
        }, {
            name: '礼品类型',
            id: 'EXCH_OBJ_KIND'
        }, {
            name: '订单号',
            id: 'EXT_SERIAL_ID'
        }, {
            name: '兑换数量',
            id: 'EXCH_OBJ_AMT'
        }, {
            name: '礼品单价',
            id: 'PURE_POINT_AMT'
        }, {
            name: '兑换前积分',
            id: 'BEFORE_POINT_BALANCE'
        }, {
            name: '兑换后积分',
            id: 'AFTER_POINT_BALANCE'
        }, {
            name: '影响积分',
            id: 'EXCH_POINT'
        }, {
            name: '兑换时间',
            id: 'CREATE_DATE'
        }, {
            name: '兑换号码',
            id: 'POINT_EXCH_ACC_NBR'
        }, {
            name: '受惠号码',
            id: 'FAVOUR_EXCH_ACC_NBR'
        }, {
            name: '操作',
            id: 'ACTION'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        decorate: {
            'ACTION': function (row) {
                return '<a data-seria="' + row['EXT_SERIAL_ID'] + '" style="color:#4284d7" class="sellback">返销</a>';
            }
        }
    });
}
/**
 * 返销操作
 * @param {*} param 
 */
function sellback(param) {
    //先判断是那种类型
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
    var latnId = $('#lan_search').find('.hover').data('code');
    var param = {
        'EXT_SERIAL_ID': param,
        'ACCT_NUM': $('#cust_num').val().trim(),
        'latnId': latnId == 'all' ? '888' : latnId,
        'serviceName': 'pointSellBackWebService',
        'methodName': 'sellback',
        'objType': type == '1' ? '11' : '15'
    }
    //后台接口的调用
    var callback = function () {
        //1.提示修改成功
        layer.msg("返销成功");
        //3.刷新表格
        table.reloadTable();
    }
    COMMON.doSync(param, callback);
}