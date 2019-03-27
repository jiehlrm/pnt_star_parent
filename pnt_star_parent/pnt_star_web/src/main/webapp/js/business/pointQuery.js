/**
 * 积分查看 autor:唐羽
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
 *           |- showCustPoint [查询事件]
 *           |  |- custForm [客户基本信息表单的初始化]
 *           |  |- initTag [tag的初始化]
 *           |     |- tagChoose [tag切换的事件触发]
 *           |        |- initTable [tag中表格的初始化]
 *           |- function [重置事件]
 *           |- yearDetail [年积分详情弹框事件]
 * --UNDO
 *  1.积分账本变更记录表格未做
 */
var tableHead1 = [{
    name: '年份',
    id: 'Y'
}, {
    name: '当年累计积分',
    id: 'POINT_INIT_VALUE'
}, {
    name: '当年可用积分',
    id: 'POINT_BALANCE'
}];
var tableHead2 = [{
    name: '积分服务事件',
    id: 'SERVICE_TYPE'
}, {
    name: '操作人',
    id: 'UPDATE_STAFF'
}, {
    name: '账本年份',
    id: 'YEAR_DATE'
}, {
    name: '积分类型',
    id: 'POINT_TYPE'
}, {
    name: '操作前积分',
    id: 'BALANCE_BEFORE'
}, {
    name: '操作后积分',
    id: 'POINT_BALANCE'
}, {
    name: '影响积分数',
    id: 'AMOUNT'
}, {
    name: '操作时间',
    id: 'UPDATE_DATE'
}];
var table1 = null;
var table2 = null;
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
    $('#doPointChangeList').on('click', function () {
    	$('.cat-tag-hover').click();
    });
    
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('.query-object').find('dt').text('请选择');
        $('#cust_num').val('');
    });
    $('#resetPointChangeList').on('click', function () {
        $('.timeRange').val('');
    });
    
    //年积分详情弹框事件
    $('body').on('click', '.pointInitValue', function (e) {
        var year = $(e.target).data('year');
        yearDetail(year);
    });
    //积分服务事件
    $('#service_type').on('click', 'a', function (e) {
        $('#service_type').find('a').removeClass('hover');
        $(e.target).addClass('hover');
        $('.service-type').find('dt').text($(e.target).text());
        $('#service_type').css('display', 'none');
    });
}
/**
 * 进行客户用户积分的查询
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
    //3.初始化两个tag
    initTag();
}
/**
 * 初始化客户的积分表单
 */
function custForm() {

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

    var latn_id = $('#lan_search').find('.hover').data('code');
    if (latn_id == 'all') {
        latn_id = "888";
    }
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getCustFormData',
        'ACCT_NUM': $('#cust_num').val().trim(),
        'TYPE': type,
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
 * 表格初始化
 */
function initTable(div, tableHead, param) {
    return $(div).catTable({
        data: COMMON.getSyncData, //方法从mock.js中来，模拟从服务器获取数据
        thead: tableHead,
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        showFooter:false,
        decorate: {
            'POINT_INIT_VALUE': function (p) {
                return {
                    tagName: 'a',
                    props: {
                        'class': 'pointInitValue',
                        'data-year': p.Y,
                        'style': 'color: #4284d7;'
                    },
                    children: [p.POINT_INIT_VALUE]
                }
            }
        }
    });
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
                title: '积分信息',
                choose: true
            }, {
                value: '2',
                title: '积分账本变更记录'
            }],
            afterClick: function (p1, p2) {
                tagChoose(p1, p2);
            }
        });
    } else {
        //已经被初始化，则需要进行刷新
    	tableReload();
    }
}
//刷新表格
function tableReload() {
	var param1 = {
            'serviceName': 'pointQueryService',
            'methodName': 'getYearPoint',
            'CUST_ID': cust_id
    };
	var serviceTypeIn = $('#service_type').find('.hover').data('typein');
	var serviceTypeOut = $('#service_type').find('.hover').data('typeout');
    var param2 = {
    		'serviceName': 'pointQueryService',
            'methodName': 'getChangeList',
            'cust_id': cust_id,
            'timeRange':$('.timeRange').val(),//时间范围
            'serviceTypeIn':serviceTypeIn,
            'serviceTypeOut':serviceTypeOut
    };
    table1.setInitParam(param1);
    //重设param
    table2.setInitParam(param2);
    //进行刷新
    table2.reloadTable();
    table1.reloadTable();
}
//切换标签
function tagChoose(p1, p2) {
    //第一个tag
    if (p1.value == '1') {
        var param1 = {
            'serviceName': 'pointQueryService',
            'methodName': 'getYearPoint',
            'CUST_ID': cust_id
        };
        //存在table
    	$('#serviceDlg').hide();
    	$('#changeForm').hide();
        if (table1) {
            //重设param
            table1.setInitParam(param1);
            //进行刷新
            table1.reloadTable();
        } else {
            //不存在则需要新建
            table1 = initTable(p2, tableHead1, param1);
        }
    } else {
        //第二个tag 积分账本变更记录
    	//获取关键人类型编码
    	
    	$('#serviceDlg').show();
    	$('#changeForm').show();
		var serviceTypeIn = $('#service_type').find('.hover').data('typein');
		var serviceTypeOut = $('#service_type').find('.hover').data('typeout');
        var param2 = {
        		'serviceName': 'pointQueryService',
                'methodName': 'getChangeList',
                'cust_id': cust_id,
                'timeRange':$('.timeRange').val(),//时间范围
                'serviceTypeIn':serviceTypeIn,
                'serviceTypeOut':serviceTypeOut
        };
        
        if (table2) {
        	 //重设param
            table2.setInitParam(param2);
            //进行刷新
            table2.reloadTable();
        } else {
        	//不存在则需要新建
            table2 = initTable2(p2, tableHead2, param2);
        }
    }
}
function initTable2(div, tableHead, param) {
    return $('#changeForm').catTable({
        data: COMMON.getSyncData, //方法从mock.js中来，模拟从服务器获取数据
        thead: tableHead,
        dataFrom: 'Server-T',
        param: param,
        showFooter: true,
        dataComp: ['data', 'total'],
    });
}

/**
 * 年积分明细
 */
function yearDetail(year) {
    //弹框
    var catLayer = $.catLayer({
        content: '<div style="width:600px;"><div class="pointQuery_title"><span>月初账单明细</span><span style="float:right;"><a class="aui_close">×</a></span></div><div id="yearDetail"></div></div>',
        minWidth: 600,
        minHeight: 400
    });
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getYearDetail',
        'CUST_ID': cust_id,
        'YEAR': year
    };
    /*var param1 = {
        'serviceName': 'pointQueryService',
        'methodName': 'getYearDetailTotal',
        'CUST_ID': cust_id,
        'YEAR': year
    }
    //获取总数量
    function getTotal(param) {
        var d = COMMON.getSyncData(param);
        return d[0].TOTAL;
    }*/
    $('#yearDetail').catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '月份',
            id: 'BILLING_CYCLE_ID'
        }, {
            name: '积分类型',
            id: 'POINT_SOURCE_TYPE'
        }, {
            name: '消费基础分',
            id: 'BASE_POINT_VALUE'
        }, {
            name: '倍数',
            id: 'MULTIPLE'
        }, {
            name: '积分值',
            id: 'AMOUNT'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total']
    });

    $('.aui_close').click(function () {
        catLayer.closeLayer();
    });
}