var cust_num = '';
var pattern = /^\d{4}(0?[1-9]|1[0-2])$/;
var table = null;

//本地网
var tableHead1 = [{
    name :'区域', 
    id:'LATNNAME'
},{
    name :'营业区',
    id:'AREANAME'
},{
    name :'月份', 
    id:'MONTHID'
},{
    name :'本月新增积分', 
    id:'NEWSCORE'
},{
    name :'本月新增积分对应客户数',
    id:'NEWSCORECUSTNUM'
},{
    name :'本月一星客户新增积分',
    id:'NEWONESCORE'
},{
    name :'本月二星客户新增积分',
    id:'NEWTWOSCORE'
},{
    name :'本月三星客户新增积分',
    id:'NEWTHRSCORE'
},{
    name :'本月四星客户新增积分', 
    id:'NEWFOUSCORE'
},{
    name :'本月五星客户新增积分',
    id:'NEWFIVSCORE'
},{
    name :'本月六星客户新增积分',
    id:'NEWSIXSCORE'
},{
    name :'本月七星客户新增积分',
    id:'NEWSEVSCORE'
},{
    name :'本月一星客户新增积分对应客户数',
    id:'NEWONESCORENUM'
},{
    name :'本月二星客户新增积分对应客户数', 
    id:'NEWTWOSCORENUM'
},{
    name :'本月三星客户新增积分对应客户数', 
    id:'NEWTHRSCORENUM'
},{
    name :'本月四星客户新增积分对应客户数', 
    id:'NEWFOUSCORENUM'
},{
    name :'本月五星客户新增积分对应客户数', 
    id:'NEWFIVSCORENUM'
},{
    name :'本月六星客户新增积分对应客户数', 
    id:'NEWSIXSCORENUM'
},{
    name :'本月七星客户新增积分对应客户数',
    id:'NEWSEVSCORENUM'
},{
    name :'本月存量积分',
    id:'POINTBALANCE'
},{
    name :'一星客户累计积分', 
    id:'ONESCORE'
},{
    name :'二星客户累计积分', 
    id:'TWOSCORE'
},{
    name :'三星客户累计积分', 
    id:'THRSCORE'
},{
    name :'四星客户累计积分',
    id:'FOUSCORE'
},{
    name :'五星客户累计积分',
    id:'FIVSCORE'
},{
    name :'六星客户累计积分', 
    id:'SIXSCORE'
},{
    name :'七星客户累计积分',
    id:'SEVSCORE'
},{
    name :'本月作废积分',
    id:'INVALIDSCORE'
},{
    name :'本月冻结积分', 
    id:'CURMONTHFRZSCORE'
},{
    name :'累积冻结积分',
    id:'SUMFRZSCORE'
},{
    name :'达到兑换门限客户数', 
    id:'REACHEXCLEVELNUM'
},{
    name :'本月兑换客户数', 
    id:'EXCCUSTNUM'
},{
    name :'本月兑换积分',
    id:'EXCSCORE'
},{
    name :'本月现存可用积分', 
    id:'NEWBALANCE'
},{
    name :'本月新增营销奖励积分',
    id:'NEWPROMOTE'
},{
    name :'本月新增营销奖励积分涉及客户数',
    id:'NEWPROMOTECUSTNUM'
},{
    name :'本月新增在网时长奖励积分', 
    id:'NEWINNET'
},{
    name :'本月新增在网时长奖励积分涉及客户数',
    id:'NEWINNETCUSTNUM'
}];

//全省
var tableHead2 = [{
    name :'区域', 
    id:'LATNNAME'
},{
    name :'月份', 
    id:'MONTHID'
},{
    name :'本月新增积分', 
    id:'NEWSCORE'
},{
    name :'本月新增积分对应客户数',
    id:'NEWSCORECUSTNUM'
},{
    name :'本月一星客户新增积分',
    id:'NEWONESCORE'
},{
    name :'本月二星客户新增积分',
    id:'NEWTWOSCORE'
},{
    name :'本月三星客户新增积分',
    id:'NEWTHRSCORE'
},{
    name :'本月四星客户新增积分', 
    id:'NEWFOUSCORE'
},{
    name :'本月五星客户新增积分',
    id:'NEWFIVSCORE'
},{
    name :'本月六星客户新增积分',
    id:'NEWSIXSCORE'
},{
    name :'本月七星客户新增积分',
    id:'NEWSEVSCORE'
},{
    name :'本月一星客户新增积分对应客户数',
    id:'NEWONESCORENUM'
},{
    name :'本月二星客户新增积分对应客户数', 
    id:'NEWTWOSCORENUM'
},{
    name :'本月三星客户新增积分对应客户数', 
    id:'NEWTHRSCORENUM'
},{
    name :'本月四星客户新增积分对应客户数', 
    id:'NEWFOUSCORENUM'
},{
    name :'本月五星客户新增积分对应客户数', 
    id:'NEWFIVSCORENUM'
},{
    name :'本月六星客户新增积分对应客户数', 
    id:'NEWSIXSCORENUM'
},{
    name :'本月七星客户新增积分对应客户数',
    id:'NEWSEVSCORENUM'
},{
    name :'本月存量积分',
    id:'POINTBALANCE'
},{
    name :'一星客户累计积分', 
    id:'ONESCORE'
},{
    name :'二星客户累计积分', 
    id:'TWOSCORE'
},{
    name :'三星客户累计积分', 
    id:'THRSCORE'
},{
    name :'四星客户累计积分',
    id:'FOUSCORE'
},{
    name :'五星客户累计积分',
    id:'FIVSCORE'
},{
    name :'六星客户累计积分', 
    id:'SIXSCORE'
},{
    name :'七星客户累计积分',
    id:'SEVSCORE'
},{
    name :'本月作废积分',
    id:'INVALIDSCORE'
},{
    name :'本月冻结积分', 
    id:'CURMONTHFRZSCORE'
},{
    name :'累积冻结积分',
    id:'SUMFRZSCORE'
},{
    name :'达到兑换门限客户数', 
    id:'REACHEXCLEVELNUM'
},{
    name :'本月兑换客户数', 
    id:'EXCCUSTNUM'
},{
    name :'本月兑换积分',
    id:'EXCSCORE'
},{
    name :'本月现存可用积分', 
    id:'NEWBALANCE'
},{
    name :'本月新增营销奖励积分',
    id:'NEWPROMOTE'
},{
    name :'本月新增营销奖励积分涉及客户数',
    id:'NEWPROMOTECUSTNUM'
},{
    name :'本月新增在网时长奖励积分', 
    id:'NEWINNET'
},{
    name :'本月新增在网时长奖励积分涉及客户数',
    id:'NEWINNETCUSTNUM'
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
	// 查询图标的事件添加
    $('#doQuery').on('click', function () {
        queryData();
    });
    // 导出按钮事件添加
    $('#exportForm').on('click', function() {
        exportForm();
    })
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('#account_period').val('');
    });
}
/**
 * 查询表单结果
 */
function queryData() {
	var tableHead=null;
	var qryType=0;
    var latn_id = $('#lan_search').find('.hover').data('code');
    if (latn_id == 'all') {
        latn_id = "";
        qryType=0; 
        tableHead=tableHead2;
    }else{
    	qryType=1;  
    	tableHead=tableHead1;
    }
    var account_period = $('#account_period').val().trim();
    if (account_period == '') {
        layer.msg('账期必须填写');
        return;
    }
    if (!pattern.test(account_period)) {
        layer.msg('账期格式错误，例:201812');
        return;
    }

    // 获取表单数据
    var methodParam = {
        'serviceName' : 'pointOverviewReportService',
        'methodName' : 'getListReport',
        'LATN_ID' : latn_id,
        'QRY_TYPE':qryType,
        'ACCOUNT_PERIOD' : account_period
    };
//    根据本地网不同初始化不同表格
     initForm(methodParam,tableHead);
}
/**
 * 初始化表单
 */
function initForm(methodParam,tableHead) {
	if (table != null) {
		$("#pointForm").empty();
	}
	table = $('#pointForm').catTable({
		data : COMMON.getSyncData,// 方法从mock.js中来，模拟从服务器获取数据
		thead : tableHead,
		dataFrom: 'Server-T',
        param: methodParam,
        dataComp: ['data', 'total'],
        showFooter:false
	});
    //更改表格渲染长度  
    $('#pointForm').css('overflow-x', 'scroll');
    $('#table1').css('width', '400%');
    return table;

}
/**
 * 导出表单
 */
function exportForm() {
	var tableHead=null;
	//0 全省 1 本地网
	var qryType=0;
    var latn_id = $('#lan_search').find('.hover').data('code');
    if (latn_id == 'all') {
        latn_id = "";
        qryType=0;
        tableHead=tableHead2;
    }else{
    	qryType=1;  
    	tableHead=tableHead1;
    }
    var account_period = $('#account_period').val().trim();
    if (account_period == '' || account_period == null) {
        layer.msg('账期必须填写');
        return;
    }
    var table_title = JSON.stringify(tableHead)
    var param = {
        'serviceName' : 'pointOverviewReportService',
        'methodName' : 'exportForm',
        'LATN_ID' : latn_id,
        'ACCOUNT_PERIOD' : account_period,
        'QRY_TYPE':qryType,
        'TABLE_TITLE' : table_title
    };
    // var d = COMMON.getSyncData(param);
    $.ajax({
        type : 'post',
        url : nowurl + "ControlServlet.do",
        data : param,
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