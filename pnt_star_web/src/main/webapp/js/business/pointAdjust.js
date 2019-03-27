var customTable = new queryListTable();
var cust_id = '';
var table1 = null;
var point_tag = null;
var adjust_value1=0;
var adjust_value2=0;

var tableHead = [{
	name:'类型', 
	id:'POINT_ACC_TYPE_NAME'
},{
	name:'调整前可用余额', 
	id:'POINT_BALANCE'
},{
	name:'调整值',
	id:'ADJUST_VALUE'
},{
	name:'调整后可用余额',
	id:'ADJUST_BALANCE'
}];
$(document).ready(function () {
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
function addEvent(){
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
    $('body').on('input', 'input.amount', function (e) {
        doAmount(e);
    });
    $('#sumbitFile').on('click',function(){
    	pointAdjust();
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
                title: '积分调整',
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
function initTable(div, total) {
	var param1 = {
			'serviceName': 'pointAdjustWebService',
            'methodName': 'getPointBalance',
            'CUST_ID': cust_id
        };
    if (table1 == null) {
        table1 = $('#pointTag').catTable({
            data: COMMON.getSyncData,//方法从mock.js中来，模拟从服务器获取数据
            thead: tableHead,
            dataFrom: 'Server-T',
            param: param1,
            dataComp: ['data', 'total'],
            showFooter:false,
            decorate:{ 
            	'ADJUST_VALUE':function (row) {
                return '<input data-id="'+row.POINT_ACC_TYPE+'" data-amount="' + row.POINT_BALANCE + '"class="lina-inp amount" '+
                	   'style="width:100px;height: 10px;line-height: 10px;" value="0" onfocus="this.select()">';
                },
        	    'ADJUST_BALANCE': function () {
        		return '<span class="adjust_balance"></span>';
        		}
            }
          });
        
        return table1;
    } else {
        table1.setInitParam(param1);
        table1.reloadTable();
    }
}
/**
 * 数量的输入
 * @param {} e 
 */
function doAmount(e) {
	var amount = Number($(e.target).data('amount'));
    if (isNaN(amount)) {
        layer.msg('原积分不正确，请联系管理员');
        return;
    }
    var num = $(e.target).val();
    if(num=="-"){} 
    else if (!/^-?[1-9]\d*$/.test(num) ) {
        layer.msg('请输入整数');
        $(e.target).val(0);
        $(e.target).select();
        $(e.target).parents('tr').find('.adjust_balance').text("");
        return;
    }
    if(parseInt(amount)+parseInt(num)<0){
    	layer.msg('积分调整后不能小于0');
        $(e.target).val(0);
        $(e.target).parents('tr').find('.adjust_balance').text("");
        return;
    }
    if(Math.abs(parseInt(num))>1000){
    	layer.msg('积分调整不能超过1000');
        $(e.target).val(0);
        $(e.target).parents('tr').find('.adjust_balance').text("");
        return;
    }
    
    $(e.target).parents('tr').find('.adjust_balance').text(parseInt(amount)+parseInt(num));
}

/**
 * 提交积分调整信息
 */
function pointAdjust() {
	//0.先判断是那种类型
    var typeText = $('.select-object').find('dt').text().trim();
    var select = '活动奖励积分';
    if (typeText == '活动奖励积分') {
    	select = '活动奖励积分';
    } else if (typeText == '积分扣减异常') {
    	select = '积分扣减异常';
    } else if (typeText == '积分出账异常') {
    	select = '积分出账异常';
    } else {
        layer.alert('请选择调整原因');
        return;
    }
	var data_list=table1.getData();
    var reson = $('#reason_description').find('textarea').val().trim();
    if (cust_id == '' || cust_id == undefined || cust_id == null) {
        layer.msg('请先查询出客户');
        return;
    }
    if (reson.length > 50) {
        layer.msg('调整原因请在50字以内');
        return;
    }
    if(isNaN($('input[data-id=120400]').val())||isNaN($('input[data-id=120300]').val())){
    	layer.msg('请输入整数');
    	return;
    }
    
    data_list[1].ADJUST_VALUE=$('input[data-id=120400]').val();
    data_list[0].ADJUST_VALUE=$('input[data-id=120300]').val();

    var param = {
            'CUST_ID': cust_id,
            'SELECT': select,
            'DATA_LIST':JSON.stringify(data_list),
            'REASON_REMARK': reson,
            'serviceName': 'pointAdjustWebService',
            'methodName': 'doPointAdjust',
        };
    //异步调用
    

	customTable.openProgress();
	// 后台接口的调用
	var callback = function() {
		customTable.closeProgress();
		// 1.提示修改成功
		layer.msg("积分调整成功");
		// 2.刷新表单
		custForm();
		// 3.刷新表格
		initTable();
	}
        // COMMON.doSync(param, callback);
    doSync(param, callback);
      
}
//重写异步调用方法，才有进度条
function doSync(data,callback) {
	var d = '';
	$.ajax({
		type : 'post',
		url : nowurl + "ControlServlet.do",
		data : data,
		async : true,
		dataType : 'json',
		success : function(data) {
			if (data.resultCode == '00000') {
				callback(data);
			} else {
				layer.msg(data.resultMsg);
			}
		}
	});
}