var table = null;
var tableHead = [
    {
        name: '区域',
        id: 'LATNNAME'
    },
    {
        name: '营业区',
        id: 'AREANAME'
    },
    {
        name: '月份',
        id: 'MONTHID'
    },
    {
        name: '产生积分',
        id: 'VALUEPOINT'
    },
    {
        name: '现存积分',
        id: 'BALANCEPOINT'
    },
    {
        name: '已兑换积分',
        id: 'POINTEXCHANGE'
    },
    {
        name: '已清零积分',
        id: 'CLEARPOINT'
    },
    {
        name: '已冻积分',
        id: 'VALUEFZE'
    },
    {
        name: '现存有效积分',
        id: 'BALANCEVALID'
    },
    {
        name: '现存积分涉及客户数',
        id: 'CUSTNUM'
    },
    {
        name: '现存积分有效积分涉及客户数',
        id: 'VALIDCUSTNUM'
    }
];
var tableHead1 = [
      {
          name: '区域',
          id: 'LATNNAME'
      },
      {
          name: '月份',
          id: 'MONTHID'
      },
      {
          name: '产生积分',
          id: 'VALUEPOINT'
      },
      {
          name: '现存积分',
          id: 'BALANCEPOINT'
      },
      {
          name: '已兑换积分',
          id: 'POINTEXCHANGE'
      },
      {
          name: '已清零积分',
          id: 'CLEARPOINT'
      },
      {
          name: '已冻积分',
          id: 'VALUEFZE'
      },
      {
          name: '现存有效积分',
          id: 'BALANCEVALID'
      },
      {
          name: '现存积分涉及客户数',
          id: 'CUSTNUM'
      },
      {
          name: '现存积分有效积分涉及客户数',
          id: 'VALIDCUSTNUM'
      }
 ];
var head = tableHead1;
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
    	queryData();
    });
    //导出按钮事件添加
    $('#exportForm').on('click', function () {
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
function queryData(){
    var latnId = $('#lan_search').find('.hover').data('code');
    //var accountPeriod = $('#account_period').val().trim();
    var monthId = $('#account_period').val().trim();
    if (monthId != '' && !/^\d{4}((0([1-9]))|(1(0|1|2)))$/.test(monthId)) {
        layer.msg('输入格式为:yyyyMM,如:201601');
        return;
    }
    if (monthId == '') {
        layer.msg('账期必须填写');
        return;
    }
    var qryType = 0;
    if(latnId != 'all'){
    	qryType = 1;
    	head = tableHead;
    }
    var param = {
            'serviceName': 'pointsClearOverviewReportService',
            'methodName': 'getPointExchangeOverviewReportTable',
            'latnId': latnId == 'all' ? '888' : latnId,
            'monthId': monthId,
            'qryType':qryType
    };
    initTable(param,head);
}
/**
 * 导出表单
 */
function exportForm() {
    var latnId = $('#lan_search').find('.hover').data('code');
    var monthId = $('#account_period').val().trim();
    if (monthId != '' && !/^\d{4}((0([1-9]))|(1(0|1|2)))$/.test(monthId)) {
        layer.msg('输入格式为:yyyyMM,如:201601');
        return;
    }
    if (monthId == '' || monthId == null) {
        layer.msg('账期必须填写');
        return;
    }
    var qryType = 0;
    var tableTitle = JSON.stringify(tableHead1);
    if(latnId != 'all'){
    	qryType = 1;
    	tableTitle = JSON.stringify(tableHead);
    }
    var param = {
            'serviceName': 'pointsClearOverviewReportService',
            'methodName': 'exportForm',
            'latnId': latnId == 'all' ? '888' : latnId,
            'monthId': monthId,
            'qryType':qryType,
            'tableTitle': tableTitle
    };
    var d = COMMON.getSyncData(param);
    $.ajax({
        type : 'post',
        url : nowurl + "ControlServlet.do",
        data : param,
        async : false,
        dataType : 'json',
        success : function(data) {
            if (data.resultCode == '00000') {
                //服务器生成excel完毕
                window.open(nowurl + data.filePath);
            } else {
                layer.msg('导出失败');
                return;
            }
        }
    });
}
/**
 * 初始化表单
 */
function initTable(param,head) {
	if (table != null) {
		$("#pointTag").empty();
	}
	table = $('#pointTag').catTable({
        data: COMMON.getSyncData,
        thead: head,
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        showFooter:false
    });
}