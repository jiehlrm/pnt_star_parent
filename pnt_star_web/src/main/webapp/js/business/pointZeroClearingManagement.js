var tableHead1 = [{
    name: '客户编码',
    id: 'custNumber'
}, {
    name: '客户名称',
    id: 'custName'
}, {
    name: '清零类型',
    id: 'clearEventTypeName'
},{
	name: '清零积分余额',
    id: 'clearBalance'
},{
	name: '清零积分值',
    id: 'clearValue'
},{
	name: '清零账期',
    id: 'monthId'
},{
	name: '清零时间',
    id: 'createDate'
}];
var table = null;
var cust_id = ''; //全局cust_id，代表客户id
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
    //导出按钮事件添加
    $('#exportForm').on('click', function () {
        exportForm();
    })
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('.zero-type').find('dt').text('请选择');
        $('.condition-type').find('dt').text('请选择');
        $('#cust_num').val('');
        $('#account_period').val('');
    });
}
/**
* 导出表单
*/
function exportForm() {
    //获取参数
    var latnId = $('#lan_search').find('.hover').data('code');
  //判断是清零类型
	var zeroTypeText = $('.zero-type').find('dt').text().trim();
	var zeroType = null;
	if(zeroTypeText == '客户离网'){
		zeroType = '01';
	}else if (zeroTypeText == '空壳客户'){
		zeroType = '03';
	}else if (zeroTypeText == '过期清零'){
		zeroType = '04';
	}else if (zeroTypeText == '全部类型'){
		zeroType = null;
	}else{
		layer.alert('请选择清零类型');
        return;
	}
	  //判断条件类型
    var conditionTypeText = $('.condition-type').find('dt').text().trim();
    var conditionType = '1';
    if (conditionTypeText == '用户号码') {
    	conditionType = '1';
    } else if (conditionTypeText == '客户编号') {
    	conditionType = '2';
    } else {
        layer.alert('请选择条件类型');
        return;
    }
    var objValue = $('#cust_num').val().trim();
    if (objValue == '') {
        layer.msg('用户号码必须填写');
        return;
    }
    var monthId = $('#account_period').val().trim();
    if (monthId != '' && !/^\d{4}((0([1-9]))|(1(0|1|2)))$/.test(monthId)) {
        layer.msg('输入格式为:yyyyMM,如:201601');
        return;
    }
    //删除操作表头
    var temp_table_head = tableHead1.slice(0, tableHead1.length);
    var table_title = JSON.stringify(temp_table_head);
    var param = {
    		'serviceName': 'pointZeroClearingManageWebService',
            'methodName': 'exportForm',
            'objValue': objValue,
            'objType': conditionType == '1' ? '11' : '15',
            'latnId': latnId == 'all' ? '888' : latnId,
            'monthId': monthId,
            'clearEventType':zeroType,
            'tableTitle':table_title
    };
    $.ajax({
        type : 'post',
        url : nowurl + "ControlServlet.do",
        data : param,
        async : false,
        dataType : 'json',
        success : function(data) {
            if (data.resultCode == '00000') {
                //服务器生成excel完毕
                layer.msg('导出成功');
                window.open(nowurl + data.filePath);
            } else {
                layer.msg('导出失败');
                return;
            }
        }
    });
}
/**
 * 点击图标进行查询事件
 */
function showCustPoint() {
	//0.先判断是清零类型
	var zeroTypeText = $('.zero-type').find('dt').text().trim();
	var zeroType = null;
	if(zeroTypeText == '客户离网'){
		zeroType = '01';
	}else if (zeroTypeText == '空壳客户'){
		zeroType = '03';
	}else if (zeroTypeText == '过期清零'){
		zeroType = '04';
	}else if (zeroTypeText == '全部类型'){
		zeroType = null;
	}else{
		layer.alert('请选择清零类型');
        return;
	}
	//1.先判断是条件类型
    var conditionTypeText = $('.condition-type').find('dt').text().trim();
    var conditionType = '1';
    if (conditionTypeText == '用户号码') {
    	conditionType = '1';
    } else if (conditionTypeText == '客户编号') {
    	conditionType = '2';
    } else {
        layer.alert('请选择条件类型');
        return;
    }

    //2.先判断acct_num是否存在
    var acct_num = $('#cust_num').val().trim();
    if (acct_num == '') {
        layer.msg(conditionType == '1' ? '用户号码必须填写' : '客户编号必须填写');
        return;
    }
    var account_period = $('#account_period').val().trim();
    if (account_period != '' && !/^\d{4}((0([1-9]))|(1(0|1|2)))$/.test(account_period)) {
        layer.msg('输入格式为:yyyyMM,如:201601');
        return;
    }
    var objValue = $('#cust_num').val().trim();
	var monthId = $('#account_period').val().trim();
    var latnId = $('#lan_search').find('.hover').data('code');
    //3.初始化tag
    initTag();
  //4.积分清单详情table
    initTable();
}
/**
 * 初始化tag
 */
function initTag() {
    //需要进行tag初始化,否则不需要
	//未被初始化，则需要被初始化
    if (point_tag == null) {
        point_tag = $('#pointTag').catTag({
            tags: [{
                value: '1',
                title: '清零详情信息',
                choose: true
            }]
        });
    }else{
        //已经被初始化，则需要进行刷新
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
/**
 * 表格初始化
 */
function initTable() {
	var objValue = $('#cust_num').val().trim();
	var monthId = $('#account_period').val().trim();
    var latnId = $('#lan_search').find('.hover').data('code');
  //先判断条件类型
    var conditionTypeText = $('.condition-type').find('dt').text().trim();
    var conditionType = '1';
    if (conditionTypeText == '用户号码') {
    	conditionType = '1';
    } else if (conditionTypeText == '客户编号') {
    	conditionType = '2';
    } else {
        layer.alert('请选择条件类型');
        return;
    }
  //再判断是清零类型
	var zeroTypeText = $('.zero-type').find('dt').text().trim();
	var zeroType = null;
	if(zeroTypeText == '客户离网'){
		zeroType = '01';
	}else if (zeroTypeText == '空壳客户'){
		zeroType = '03';
	}else if (zeroTypeText == '过期清零'){
		zeroType = '04';
	}else if (zeroTypeText == '全部类型'){
		zeroType = null;
	}else{
		layer.alert('请选择清零类型');
        return;
	}
	var param = {
            'serviceName': 'pointZeroClearingManageWebService',
            'methodName': 'getPointClearTable',
            'objValue': objValue,
            'objType': conditionType == '1' ? '11' : '15',
            'latnId': latnId == 'all' ? '888' : latnId,
            'monthId': monthId,
            'clearEventType':zeroType
        };
	if (table == null) {
    	table = $('#pointTag').catTable({
            data: COMMON.getSyncData,
            thead: tableHead1,
            dataFrom: 'Server-T',
            param: param,
            dataComp: ['data', 'total']
        });
    	return table;
	}else {
        table.setInitParam(param);
        table.reloadTable();
    }
}