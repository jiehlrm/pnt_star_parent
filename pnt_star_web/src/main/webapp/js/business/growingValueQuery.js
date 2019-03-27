/**
 * 成长值查看 
 *--GLOBAL VARIABLE
 *  |- tableHead1 [存储积分信息表格的表头]
 *  |- tableHead2 [存储积分账本变更记录表格的表头]
 *  |- table1 [积分信息表格实例]
 *  |- table2 [积分账本变更记录表格实例]
 *  |- point_tag [tag的实例]
 * 
 *--FUNCTION DESCRIPTION [方法描述以及部分层级关系]
 *  |- $() [初始方法入口] 
 *     |- initSearch [初始化查询]
 *        |- initLan [初始化本地网查询条件]
 *        |- addEvent [页面事件的添加]
 *           |- showCustPoint [点击放大镜图标进行查询事件]
 *              |- custForm [客户基本信息表单的初始化]
 *              |- initTag [tag的初始化]
 *              |- initTable [tag中表格的初始化]
 */
var tableHead1 = [{
    name: '当前成长值',
    id: 'POINT_BALANCE'
}, {
    name: '本月成长值',
    id: ''
}, {
    name: '评定时成长值',
    id: 'POINT_BALANCE'
},{
	name: '距离七星客户还差的成长值',
    id: 'POINT_DVALUE'
},{
	name: '当前网龄成长值',
    id: ''
},{
	name: '成长值生效时间',
    id: 'EFF_DATE'
},{
	name: '成长值失效时间',
    id: 'EXP_DATE'
}];
var table1 = null;
var cust_id = ''; //全局cust_id，代表客户id
var point_tag = null;
$(document).ready(function () {
    //初始化查询
    initSearch();
});
/**
 * 初始化查询
 */
function initSearch() {
    //初始化本地网
    initLan();
    //页面事件添加
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
}
/**
 * 进行客户用户成长值的查询
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
    //4.初始化成长值table
    initTable();
}
/**
 * 初始化客户的积分表单
 */
function custForm() {
    var latn_id = $('#lan_search').find('.hover').data('code');
    if (latn_id == 'all') {
        latn_id = "888";
    }
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getCustFormData',
        'ACCT_NUM': $('#cust_num').val().trim(),
        'TYPE': $('#selectType').val(),
        'LATN_ID': latn_id
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
    //未被初始化，则需要被初始化
    if (point_tag == null) {
        point_tag = $('#pointTag').catTag({
            tags: [{
                value: '1',
                title: '成长值信息',
                choose: true
            }]
        });
    }else{
        //已经被初始化，则需要进行刷新
    }
}


/**
 * 表格初始化
 */
function initTable(methodParam, total) {
	var param1 = {
            'serviceName': 'growingValueQueryWebService',
            'methodName': 'getGrowingValue',
            'CUST_ID': cust_id
        };
    if (table1 == null) {
        table1 = $('#pointTag').catTable({
            data: COMMON.getSyncData,//方法从mock.js中来，模拟从服务器获取数据
            thead: tableHead1,
            dataFrom: 'Server-T',
            param: param1,
            dataComp: ['data', 'total'],
            showFooter:false
          });
        return table1;
    } else {
        table1.setInitParam(param1);
        table1.reloadTable();
    }
}
