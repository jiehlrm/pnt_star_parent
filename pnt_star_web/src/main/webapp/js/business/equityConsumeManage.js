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
	    });}
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
     * 查看
     */
    $('body').on('click', '.look', function (e) {
    	doLook(e);
    });
    /**
     * 消费
     */
    $('body').on('click', '.convert', function (e) {
    	doConvert(e);
    });
    /**
     * 返销
     */
    $('body').on('click', '.sellBack', function (e) {
    	doSellBack(e);
    });
    /**
     * 输入校验
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
                title: '权益消费',
                choose: true
            }, {
                value: '2',
                title: '权益消费记录'
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
 * 权益消费表格
 * @param {*} p 
 */
function initTable1(p) {
    var param = {
        'serviceName': 'equityConsumeManageWebService',
        'methodName': 'getEquityTable',
        'CUST_ID': cust_id
    };
    table1 = $(p).catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '权益名称',
            id: 'SERVICE_NAME'
        }, {
            name: '权益标准',
            id: 'TOTAL_COUNT'
        }, {
            name: '剩余可用次数',
            id: 'USEABLE_COUNT'
        }, {
            name: '生效时间',
            id: 'EFF_DATE'
        }, {
            name: '失效时间',
            id: 'EXP_DATE'
        }, {
        	name: '查看',
            id: 'LOOK'
        }, {
            name: '扣除数量',
            id: 'DEDUCTION_NUM'
        }, {
            name: '操作',
            id: 'ACTION'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        decorate: {
            'DEDUCTION_NUM': function (row) {
                return '<input data-amount="' + row.USEABLE_COUNT + '" class="lina-inp amount" style="width:100px;height: 10px;line-height: 10px;" value="0">';
            },
            'ACTION': function (row) {
                return '<a data-memServAcctId="' + row['MEM_SERV_ACCT_ID'] + '" data-memberServiceId="' + row['MEMBER_SERVICE_ID'] + '" style="color:#4284d7" class="convert">消费</a>';
            },
            'LOOK': function (row) {
            	if (row.USEABLE_COUNT != row.TOTAL_COUNT){
            		return '<a data-memberServiceId="' + row['MEMBER_SERVICE_ID'] + '" style="color:#4284d7" class="look">查看</a>';
            	}else{
            		return '<a>----</a>';
            	}
            }
        }
    });
}
/**
 * 权益消费记录表格
 * @param {*} p 
 */
function initTable2(p) {
    var param = {
        'serviceName': 'equityConsumeManageWebService',
        'methodName': 'getEquityRecordTable',
        'CUST_ID': cust_id
    };
    table2 = $(p).catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '权益名称',
            id: 'SERVICE_NAME'
        }, {
            name: '权益渠道',
            id: 'SERVICE_PLACE'
        }, {
            name: '权益类型',
            id: 'SERVICE_TYPE'
        }, {
            name: '消费时间',
            id: 'SERVICE_DATE'
        }, {
            name: '消费类型',
            id: 'CONSUME_TYPE'
        }, {
            name: '消费值',
            id: 'CONSUME_TIMES'
        }, {
            name: '消费次数',
            id: 'CONSUME_TIMES'
        }, {
            name: '已返销次数',
            id: 'REBACK_SUM'
        }, {
            name: '操作人',
            id: 'CREATE_STAFF'
        }, {
            name: '操作',
            id: 'ACTION'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        decorate: {
        	'ACTION': function (row) {
                if (row.REBACK_SUM == '0'){
                	return '<a data-orderItemId="' + row['ORDER_ITEM_ID'] + '" style="color:#4284d7" class="sellBack">返销</a>';
                }else{
                	return '<a>已返销</a>';
                }
            },
            'CONSUME_TYPE': function (row) {
            	return '<a>权益扣减</a>';
            }
        }
    });
}
/**
 * 数量的输入
 * @param {} e 
 */
function doAmount(e) {
    var num = $(e.target).val();
    var mixNum = Number($(e.target).data('amount'));
    if (!/^[1-9]+[0-9]*$/.test(num)) {
        layer.msg('请输入正整数');
        $(e.target).val(0);
        return;
    }
    num = Number(num);
    if(num > mixNum){
    	layer.msg('消费次数需小于可用次数');
        $(e.target).val(0);
        return;
    }
}


/**
 * 进行消费
 * @param {*} e 
 */
function doConvert(e) {
	var memServAcctId = $(e.target).data('memservacctid');
	var memberServiceId = $(e.target).data('memberserviceid');
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
        'memServAcctId': memServAcctId,
        'memberServiceId':memberServiceId,
        'consumeTimes': num,
        'objValue': objValue,
        'objType': type == '1' ? '11' : '15',
        'latnId': latnId == 'all' ? '888' : latnId,
        'serviceName': 'equityConsumeManageWebService',
        'methodName': 'equityConsume',
    }
    //后台接口的调用
    var callback = function (data) {
        //1.提示修改成功
        layer.msg("消费成功");
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
/**
 * 进行消费
 * @param {*} e 
 */
function doSellBack(e) {
	var orderItemId = $(e.target).data('orderitemid');
	var objValue = $('#cust_num').val().trim();
    var latnId = $('#lan_search').find('.hover').data('code'); 
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
			'orderItemId':orderItemId,
			'objValue': objValue,
	        'objType': type == '1' ? '11' : '15',
	        'latnId': latnId == 'all' ? '888' : latnId,
			'serviceName': 'equityConsumeManageWebService',
	        'methodName': 'serviceSellBack',	 
	 }
	//后台接口的调用
    var callback = function (data) {
        //1.提示返销成功
        layer.msg("返销成功");
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
/**
 * 查看
 * @param {*} e 
 */
function doLook(e) {
	var memberServiceId = $(e.target).data('memberserviceid');
	var acct_num = $('#cust_num').val().trim();
	//弹框
    var catLayer = $.catLayer({
        content: '<div style="width:800px;"><div class="pointQuery_title"><span>权益消费明细</span><span style="float:right;"><a class="aui_close">×</a></span></div><div id="equityDetail"></div></div>',
        minWidth: 800,
        minHeight: 400
    });
    var param = {
        'serviceName': 'equityConsumeManageWebService',
        'methodName': 'getEquityDetail',
        'custId': cust_id,
        'memberServiceId': memberServiceId
    };
    $('#equityDetail').catTable({
        data: COMMON.getSyncData,
        thead: [{
            name: '权益名称',
            id: 'SERVICE_NAME'
        }, {
            name: '权益渠道',
            id: 'SERVICE_PLACE'
        }, {
            name: '消费类型',
            id: 'CONSUME_TYPE'
        }, {
            name: '消费值',
            id: 'CONSUME_TIMES'
        }, {
            name: '消费数量',
            id: 'CONSUME_TIMES'
        }, {
            name: '返销数量',
            id: 'REBACK_SUM'
        }, {
            name: '消费时间',
            id: 'SERVICE_DATE'
        }, {
            name: '操作人',
            id: 'CREATE_STAFF'
        }, {
        	name: '请求号码',
            id: 'NUMBER'
        }],
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        decorate: {
            'CONSUME_TYPE': function (row) {
            	return '<a>权益扣减</a>';
            },
            'NUMBER': function (row) {
            	return '<a>'+ acct_num +'</a>';
            }
        }
    });

    $('.aui_close').click(function () {
        catLayer.closeLayer();
    });
}

