/**
 * 积分兑换 author tangyu
 *--GLOBAL VARIABLE
 *  |- table1 [礼品表格实例]
 *  |- table2 [礼品兑换记录表格实例]
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
 *           |        |- initTable1 [tag中礼品表格的初始化]
 *           |        |- initTable2 [tag中兑换记录表格的初始化]
 *           |- doConvert [兑换操作]
 *           |- doAmount [数量改变对应表格所需积分改变事件]
 */

var cust_id = '';
var table1 = null;
var table2 = null;
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
    /**
     * 兑换
     */
    $('body').on('click', '.convert', function (e) {
        doConvert(e);
    });
    /**
     * 数量与所需积分联动
     */
    $('body').on('input', 'input.amount', function (e) {
        doAmount(e);
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
                title: '礼品兑换',
                choose: true
            }, {
                value: '2',
                title: '礼品兑换清单'
            }],
            afterClick: function (p1, p2) {
                tagChoose(p1, p2);
            }
        });
    } else {
        if (table1) {
            table1.reloadTable();
        }
        if (table2) {
            table2.reloadTable();
        }
    }
}
/**
 * tag的点击事件
 * @param {*} p1 
 * @param {*} p2 
 */
function tagChoose(p1, p2) {
    switch (p1.value) {
        case '1':
            if (table1) {
                table1.reloadTable();
            } else {
                $(p2).html();
                initTable1(p2);
            }
            break;
        case '2':
            if (table2) {
                table2.reloadTable();
            } else {
                initTable2(p2);
            }
            break;
    }

}
/**
 * 礼品兑换表格
 * @param {*} p 
 */
function initTable1(p) {
    var param = {
        'serviceName': 'pointExchangeWebService',
        'methodName': 'getGiftTable',
    };
    table1 = $(p).catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '礼品名称',
            id: 'POINT_EXCH_OBJ_NAME'
        }, {
            name: '单价',
            id: 'PURE_POINT_AMT'
        }, {
            name: '供应数量',
            id: 'PROVIDE_NUM'
        }, {
            name: '已兑数量',
            id: 'EXCH_NUM'
        }, {
            name: '可兑日期',
            id: 'PROVIDE_DATE'
        }, {
            name: '需兑数量',
            id: 'NEED_NUM'
        }, {
            name: '所需积分',
            id: 'NEED_POINT'
        }, {
            name: '操作',
            id: 'ACTION'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        decorate: {
            'PROVIDE_DATE': function (row) {
                return '<span>' + (row['EFF_DATE'] ? row['EFF_DATE'] : '-') + '至' + (row['EXP_DATE'] ? row['EXP_DATE'] : '-') + '</span>';
            },
            'NEED_NUM': function (row) {
                return '<input data-amount="' + row.PURE_POINT_AMT + '"class="lina-inp amount" style="width:100px;height: 10px;line-height: 10px;" value="0">';
            },
            'ACTION': function (row) {
                return '<a data-poinObjId="' + row['POINT_EXCH_OBJ_ID'] + '" style="color:#4284d7" class="convert">兑换</a>';
            },
            'NEED_POINT': function () {
                return '<span class="need_num"></span>';
            }
        }
    });
}
/**
 * 礼品兑换清单表格
 * @param {*} p 
 */
function initTable2(p) {
    var param = {
        'serviceName': 'pointSellBackWebService',
        'methodName': 'getPointSellBackTable',
        'CUST_ID': cust_id
    };
    table2 = $(p).catTable({
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
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total']
    });
}
/**
 * 数量的输入
 * @param {} e 
 */
function doAmount(e) {
    var num = $(e.target).val();
    if (!/^[1-9]+[0-9]*$/.test(num)) {
        layer.msg('请输入正整数');
        $(e.target).val(0);
        $(e.target).parents('tr').find('.need_num').text("");
        return;
    }

    var amount = Number($(e.target).data('amount'));
    if (isNaN(amount)) {
        layer.msg('单价不正确，请联系管理员');
        return;
    }
    $(e.target).parents('tr').find('.need_num').text(num * amount);
}
/**
 * 进行兑换
 * @param {*} e 
 */
function doConvert(e) {
    var point_exch_obj_id = $(e.target).data('poinobjid');
    var num = $(e.target).parents('tr').find('input.amount').val();
    var objValue = $('#cust_num').val().trim();
    var latnId = $('#lan_search').find('.hover').data('code');
    if (num == '' || !/^[1-9]+[1-9]*$/.test(num)) {
        layer.msg('请输入合适的数量');
        return;
    }
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
        'pointExchObjId': point_exch_obj_id,
        'exchObjAmt': num,
        'objValue': objValue,
        'objType': type == '1' ? '17' : '15',
        'latnId': latnId == 'all' ? '888' : latnId,
        'serviceName': 'pointExchangeWebService',
        'methodName': 'exchangeGiftByPoint'
    }
    //后台接口的调用
    var callback = function () {
        //1.提示修改成功
        layer.msg("礼品兑换成功");
        //3.刷新表格
        if (table1) {
            table1.reloadTable();
        }
        if (table2) {
            table2.reloadTable();
        }
    }
    COMMON.doSync(param, callback);
}