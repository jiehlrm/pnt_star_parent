/**
 * 关键人管理 autor:D11050
 */
var tableHead = [{name: '',id: 'operation'}, {name: '用户名称',id: 'prodInstName'}, {name: '关键人类型',id: 'keyType'}, 
	{name: '直享星级',id: 'starLevel'}, {name: '产品实例ID',id: 'prodInstId'}, 
	{name: '用户号码',id: 'accNbr'}, {name: '客户名称',id: 'custName'}, 
	{name: '客户编码',id: 'custCd'}, {name: '介绍人号码',id: 'introducerNbr'},
	{name: '加入时间',id: 'addDate'}, {name: '失效时间',id: 'expDate'}];
var tableHead2 = [{name: '用户名称',id: 'custName'},  {name: '产品实例ID',id: 'prodInstId'}, {name: '用户号码',id: 'accNbr'},
	{name: '客户名称',id: 'custName'}, {name: '客户编码',id: 'custCd'},{name: '变更前类型',id: 'beforeTypeName'}, 
	{name: '变更后类型',id: 'afterTypeName'},{name: '变更时间',id: 'changeDate'}, {name: '变更类型',id: 'changeTypeName'}];
var table = null;
var changeListTable=null;
var cust_id = ''; //全局cust_id，代表客户id
$(document).ready(function () {
    initLan();//初始化本地网
    addEvent();//页面事件添加
    initOperation();
});



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
 * 初始化各个操作的div选项
 */
function initOperation() {
	var param = {
	        'serviceName': 'pointQueryService',
	        'methodName': 'getLan'
	    }
	    var data = COMMON.getSyncData(param);
	    var str = '<div style="display:none;"><a class="hover" data-code="all">全部</a></div>';
	    for (var i = 0; i < data.length; i++) {
	        str += '<div><a data-name="' + data[i]['NAME'] + '" data-code="' + data[i]['CODE'] + '">' + data[i]['NAME'] + '</a></div>';
	    }
	    $('#lan_search_add').html(str);
	    $('#lan_search_add').on('click', 'a', function (e) {
	        $('#lan_search_add').find('a').removeClass('hover');
	        $(e.target).addClass('hover');
	        $('.lan-select-add').find('dt').text($(e.target).text());
	        $('#lan_search_add').css('display', 'none');
	    });
	    //新增关键人
	    $('#keyman_type_add').on('click', 'a', function (e) {
	        $('#keyman_type_add').find('a').removeClass('hover');
	        $(e.target).addClass('hover');
	        $('.keyman-type-add').find('dt').text($(e.target).text());
	        $('#keyman_type_add').css('display', 'none');
	    });
	    $('#star_level_add').on('click', 'a', function (e) {
	        $('#star_level_add').find('a').removeClass('hover');
	        $(e.target).addClass('hover');
	        $('.star-level-add').find('dt').text($(e.target).text());
	        $('#star_level_add').css('display', 'none');
	    });
	    //调整类型
	    $('#keyman_type_adj').on('click', 'a', function (e) {
	        $('#keyman_type_adj').find('a').removeClass('hover');
	        $(e.target).addClass('hover');
	        $('.keyman-type-adj').find('dt').text($(e.target).text());
	        $('#keyman_type_adj').css('display', 'none');
	    });
	    //修改星级
	    $('#star_level_upd').on('click', 'a', function (e) {
	        $('#star_level_upd').find('a').removeClass('hover');
	        $(e.target).addClass('hover');
	        $('.star-level-upd').find('dt').text($(e.target).text());
	        $('#star_level_upd').css('display', 'none');
	    });
}
/**
 * 页面事件添加
 */
function addEvent() {
    //查询事件添加
    $('#doQuery').on('click', function () {
    	initForm();
    });
    //查询导出
    
    $('#doQueryExport').on('click', function () {
    	doQueryExport();
    });
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('.keyman-type').find('dt').text('请选择');
        $('.timeRange').val('');
        $('#cust_num').val('');
    });
    
    $(".aui_close").on('click',function () {
    	$(".catLayer").hide();
    });
    //日期组件
    lay('.date').each(function () {
    	laydate.render({
    		elem: this
    	});
    });
}

/**
 * 初始化表单
 */
function initForm() {
	var latn_id = $('#lan_search').find('.hover').data('code');
	if (latn_id == 'all'){
		layer.alert('请选择本地网');
		return;
	}
	//获取关键人类型编码
	var keyman_type = $('.keyman-type').find('.cur').text();
	 if (keyman_type == '请选择') {
		 	keyman_type = '';
	    } else if (keyman_type == '政企关键人') {
	    	keyman_type = '1000';
	    } else if (keyman_type == '公众关键人') {
	    	keyman_type = '1002';
	    } else if (keyman_type == '普通关键人') {
	    	keyman_type = '1005';
	    }
	//组装参数
	var param_total = {
			'serviceName': 'keymanManageWebService',
 	        'methodName': 'getKeymanList',
 	        'CUST_NUM': $('#cust_num').val(),//用户号码
 	        'KEYMAN_TYPE': keyman_type,//关键人类型
 	        'LATN_ID': latn_id,//本地网
 	        'TIME_RANGE': $('.timeRange').val()//时间范围
		    };
	//表格渲染
	if (table == null) {
		table = $("#pointForm").catTable({
            data: COMMON.getSyncData,
            thead: tableHead,
            dataFrom: 'Server-T',
            param: param_total,
            dataComp: ['data', 'total'],
            decorate: {
            	'operation': function (row) {//首列多选框
            		//return '<p><span class="checkbox-check mt5" name="ope" ><input id="'+row['pointKeymanId']+'" value="false"  onclick = "selectRow(this)" readonly/></span></p>';
            		return '<p><span class="checkbox-check mt5" name="ope" ><input value="'+row['pointKeymanId']+'"  onclick = "selectRow(this)" readonly/></span></p>';

            	},
            	'prodInstName': function (row) {//用户名称列
                    return '<a style="color:#4284d7" class="convert" onclick="showKeymanDetail('+row['pointKeymanId']+')">'+row['prodInstName']+'</a>';
                },
                'keyType': function (row) {//关键人类型列
                    return '<a style="color:#4284d7" class="convert" onclick="showKeymanTypeDetail('+row['pointKeymanId']+')">'+row['keyType']+'</a>';
                }
            }
        });
    } else {
        table.setInitParam(param_total);
        table.reloadTable();
    }
}

function add(){
	//clean div
	$('#custCd_add').html("");
    $('#custName_add').html("");
    $('#prodInstId_add').html("");  
    $('#star_level_add').find('.hover').removeClass('hover');
    $('#lan_search_add').find('.hover').removeClass('hover');
    $('#keyman_type_add').find('.hover').removeClass('hover');
    $('.keyman-type-add').find('dt').text("选择关键人");
    $('.star-level-add').find('dt').text("选择星级");
    $('.lan-select-add').find('dt').text("请选择");
	$("#addDlg").show();
	var getKeymanData=null;
	
	$("#getMsg").unbind("click");
	$("#getMsg").click(function () {
    	//getKeymanInfo();
		
		var accNum=$.trim($('#accNum_add').val());
		var param = {
		        'serviceName': 'keymanManageWebService',
		        'methodName': 'getKeymanInfo',
		        'accNum': accNum//关键人手机号
		    };
		    var data = COMMON.getSyncData(param);
		    getKeymanData=data;
		    $('#custCd_add').html(data.custCd);
		    $('#custName_add').html(data.custName);
		    $('#prodInstId_add').html(data.prodInstId);  
    });
	$("#sure_add").unbind("click");
	$("#sure_add").click(function () {
    	//insertKeyman();
		if(getKeymanData==null){
			layer.alert('请先获取资料');
			return;
		}
		var starLevel= $('#star_level_add').find('.hover').data('code');
		var latn_id = $('#lan_search_add').find('.hover').data('code');
		var pointKeymanType = $('#keyman_type_add').find('.hover').data('code');
		if(latn_id==null||latn_id=='all'){
			layer.alert('请选择本地网');
			return;
		}else if(starLevel==null){
			layer.alert('请选择星级');
			return;
		}else if(pointKeymanType==null){
			layer.alert('请选择关键人类型');
			return;
		}
		var param = {
		        'serviceName': 'keymanManageWebService',
		        'methodName': 'insertKeyman',
		        'starLevel': starLevel,
		        'latn_id':latn_id,
		        'pointKeymanType':pointKeymanType,
		        'accNbr':$('#accNum_add').val().trim(),
		        'custId':getKeymanData.custId,
		        'custCd':getKeymanData.custCd,
		        'expDate':$('#expDate_add').val(),
		        'prodInstId':getKeymanData.prodInstId,
		        'introducerNbr':$('#introducerNbr_add').val()
		    };
		var data = COMMON.getSyncData(param);
		if(data=="1"){
			layer.msg('新增成功！');
			initForm();
			$("#addDlg").hide();
		}
    });
	$("#close_add").click(function () {
    	$(".catLayer").hide();
    });
}


//选择一行数据
function selectRow(p){
	$(p).parent().toggleClass("checkbox-checked");

}


//关键人详细信息弹窗
function showKeymanDetail(pointKeymanId){
    var param = {
        'serviceName': 'keymanManageWebService',
        'methodName': 'getOneKeymanbyList',
        'pointKeymanId': pointKeymanId//关键人ID
    };
    var dataArray = COMMON.getSyncData(param);
    var data=dataArray.data;
    //弹框
    var catLayer = $.catLayer({
        content: '<div style="width:800px;">'+
         		 	'<div class="pointQuery_title"><span>关键人详情</span><span style="float:right;"><a class="aui_close">×</a></span>'+
	         		 	'<table class="table1 table1-hover checkbox-lina mt10" width="100%" border="0" cellspacing="0" cellpadding="0">'+
		        	       ' <tr>'+
		        	            '<td class="table1-bg">用户号码：</td>'+
		        	            '<td>'+data[0].accNbr+'</td>'+
		        	            '<td class="table1-bg">关键人类型：</td>'+
		        	            '<td>'+data[0].keyType+'</td>'+
		        	            '<td class="table1-bg">直响星级：</td>'+
		        	            '<td>'+data[0].starLevel+'</td>'+
		        	        '</tr>'+
		        	        '<tr>'+
		        	            '<td class="table1-bg">客户名称：</td>'+
		        	            '<td>'+data[0].custName+'</td>'+
		        	            '<td class="table1-bg">客户编码：</td>'+
		        	            '<td>'+data[0].custCd+'</td>'+
		        	            '<td class="table1-bg">产品实例ID：</td>'+
		        	            '<td>'+data[0].prodInstId+'</td>'+
		        	        '</tr>'+
		        	        '<tr>'+
		        	            '<td class="table1-bg">本地网：</td>'+
		        	            '<td>'+data[0].latnId+'</td>'+
		        	            '<td class="table1-bg">介绍人号码：</td>'+
		        	            '<td>'+data[0].introducerNbr+'</td>'+
		        	            '<td class="table1-bg">生效时间：</td>'+
		        	            '<td>'+data[0].addDate+'</td>'+
		        	        '</tr>'+
		        	        '<tr>'+
		        	            '<td class="table1-bg">失效时间：</td>'+
		        	            '<td>'+data[0].expDate+'</td>'+
		        	        '</tr>  '+ 
		        	    '</table>'+
		        	    '<div class="c-btn mt10">'+
		        	           '<a class="gy-btn2 orgBtn2" id="close">返回</a>'+
		        	    '</div>'+
                 	'</div>'+
        		 '</div>',
        minWidth: 800,
        minHeight: 400
    });
    //关闭弹出框
    $('.aui_close').click(function () {
        catLayer.closeLayer();
    });
    $('#close').click(function () {
        catLayer.closeLayer();
    });
}

//关键人类型变更记录弹窗
function showKeymanTypeDetail(pointKeymanId){
	var param = {
	        'serviceName': 'keymanManageWebService',
	        'methodName': 'getKeymanChangeList',
	        'pointKeymanId': pointKeymanId//关键人ID
	    };
	$("#typeDetailDlg").show();
	//弹框
	if(changeListTable==null){
		changeListTable=$('#keymanTypechangeRecord').catTable({
	        data: COMMON.getSyncData,
	        thead: tableHead2,
	        dataFrom: 'Server-T',
	        param: param,
	        dataComp: ['data', 'total']
	    });
	}else{
		changeListTable.setInitParam(param);
		changeListTable.reloadTable();
	}
    
}



//资料修改按钮
function upd(){
	var checkbox = document.getElementsByName("ope");
	var checked_counts = 0;
	var checked_value ;
	//获取被选中的checkbox个数
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].className == "checkbox-check mt5 checkbox-checked"){    
			checked_value= checkbox[i].firstChild.value;
			checked_counts++;
		}
	}
	if(checked_counts == 0 || checked_counts > 1){
		layer.msg('请注意：必须选择一条记录修改！');
	}else if(checked_counts == 1){
		var param = {
	        'serviceName': 'keymanManageWebService',
	        'methodName': 'getOneKeymanbyList',
	        'pointKeymanId': checked_value//关键人ID
	    };
	    var dataArray = COMMON.getSyncData(param);
	    var data=dataArray.data[0];
	    var starLevel;
	    $('#custName_upd').html(data.custName);
	    $('#lan_upd').html(data.latnId);
	    $('#accNbr_upd').html(data.accNbr);
	    $('#pointKeymanTypeName_upd').html(data.keyType);
	    $('#expDate_upd').html(data.expDate);
	    $('#prodInstId_upd').html(data.prodInstId);
	    $('#custCd_upd').html(data.custCd);
	    $('#introducerNbr_upd').val(data.introducerNbr);
	    var beforeLevel=data.starLevelCode;
	    if(beforeLevel=='3100'){
	    	starLevel='一星';
	    }else if(beforeLevel=='3200'){
	    	starLevel='二星';
	    }else if(beforeLevel=='3300'){
	    	starLevel='三星';
	    }else if(beforeLevel=='3400'){
	    	starLevel='四星';
	    }else if(beforeLevel=='3500'){
	    	starLevel='五星';
	    }else if(beforeLevel=='3600'){
	    	starLevel='六星';
	    }else if(beforeLevel=='3700'){
	    	starLevel='七星';
	    }else if(beforeLevel=='3800'){
	    	starLevel='非星级星';
	    }
	    $('#beforeLevel_upd').html(starLevel);
	    $('#star_level_upd').find('.hover').removeClass('hover');
	    //弹框
	    $('#updateDlg').show();
	    //点击确认保存修改的内容
	    $("#sure_upd").unbind("click");
	    $('#sure_upd').click(function () {
	    	var star_level_upd=$('#star_level_upd').find('.hover').data('code');
	    	if(star_level_upd==null){
	    		star_level_upd=beforeLevel;
	    	}
	    	var param = {
	    	        'serviceName': 'keymanManageWebService',
	    	        'methodName': 'updateKeyman',
	    	        'pointKeymanId': checked_value,//关键人ID
	    	        'starLevel': star_level_upd,//修改后星级
	    	        'introducerNbr': $('#introducerNbr_upd').val()//修改后介绍人号码
	    	    };
	    	    var data = COMMON.getSyncData(param);
        	    if(data =="1"){
        	    	layer.msg('修改成功！');
        	    	$('#updateDlg').hide();
        	    	initForm();
        	    }
	    });
	    $('#close_upd').click(function () {
	    	 $('#updateDlg').hide();
	    });
	}else{
		layer.alert('程序异常！');
	}
}

//类型调整按钮
function adj(){
	//clean div
	$('#reason_adj').val("");
    $('#expDate_adj').val(""); 
    $('#keyman_type_adj').find('.hover').removeClass('hover');
    $('.keyman-type-adj').find('dt').text("选择关键人");
    
	var checkbox = document.getElementsByName("ope");
	var checked_counts = 0;
	var checked_value ;
	var beforType=null;
	//获取被选中的checkbox个数
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].className == "checkbox-check mt5 checkbox-checked"){    
			checked_value= checkbox[i].firstChild.value;
			checked_counts++;
		}
	}
	if(checked_counts == 0 || checked_counts > 1){
		layer.msg('请注意：必须选择一条记录修改！');
	}else if(checked_counts == 1){
		var pointKeymanId=$(".checkbox-checked").find("input").val();
		var param = {
	        'serviceName': 'keymanManageWebService',
	        'methodName': 'getKeyman',
	        'pointKeymanId': checked_value//关键人ID
	    };
	    var data = COMMON.getSyncData(param);
	    var beforeTypeCode=data.pointKeymanType;
	    if(data.pointKeymanType=='1000'){
	    	beforType='政企关键人';
	    }else if(data.pointKeymanType=='1002'){
	    	beforType='公众关键人';
	    }else if(data.pointKeymanType=='1005'){
	    	beforType='普通关键人';
	    }
	    $("#typeBefore_adj").html(beforType);
	    $("#expDateBefore_adj").html(data.expDate);
		    //弹框
		$('#adjustDlg').show();
		$("#sure_adj").unbind("click");
	    $('#sure_adj').click(function () {
	    	var adjReason =$("#reason_adj").val();
	    	var afterTypeCode = $('#keyman_type_adj').find('.hover').data('code');
	    	if(afterTypeCode==null){
	    		layer.msg('调整类型不能为空！');
	        	return;
	    	}
	        if(adjReason =="" || adjReason == null){
	        	layer.msg('调整原因不能为空！');
	        	return;
	        }else{
	        	var param = {
	        	        'serviceName': 'keymanManageWebService',
	        	        'methodName': 'adjKeymanType',
	        	        'pointKeymanId': checked_value,//关键人ID
	        			'pointKeymanType': afterTypeCode,//调整类型
	        			'expDate':$('#expDate_adj').val(),
	        			'adjReason': adjReason,//调整原因
	        			'beforeType':beforeTypeCode,
	        			'afterType':afterTypeCode,     			
	        	    };
	        	    var data = COMMON.getSyncData(param);
	        	    if(data =="1"){
	        	    	layer.msg('调整成功！');
	        	    	$("#adjustDlg").hide();
	        	    	initForm();
	        	    }
	        }
	    });
	    $('#close_adj').click(function () {
	    	$("#adjustDlg").hide();
	    });
}else{
	layer.alert('程序异常！');
}
}
//删除按钮
function del(){
	var checkbox = document.getElementsByName("ope");
	//获取被选中的checkbox个数
	var checked_counts = 0;
	//获取被选中的各个关键人ID数组-支持多条删除(pointkeymanId)
	var checked_value =[];
	for(var i=0;i<checkbox.length;i++){
		if(checkbox[i].className == "checkbox-check mt5 checkbox-checked"){    
			checked_value[checked_counts]= checkbox[i].firstChild.value;
			checked_counts++;
		}
	}
	if(checked_counts == 0 ){
		layer.msg('请注意：必须选择一条记录删除！');
	}else if(checked_counts >= 1){
		layer.confirm('确定删除选中记录？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				var param = {
        	        'serviceName': 'keymanManageWebService',
        	        'methodName': 'deleteKeyman',
        	        'pointKeymanIds': JSON.stringify(checked_value),//关键人ID
        	    };
        	    var data = COMMON.getSyncData(param);
        	    if(data =="1"){
        	    	layer.msg('删除成功！');
        	    	initForm();
        	    }
			}, function(){
			});
	}else{
		layer.alert('程序异常！');
	}
}
function doQueryExport(){
	var latn_id = $('#lan_search').find('.hover').data('code');
	if (latn_id == 'all'){
		layer.alert('请选择本地网');
		return;
	}
	//获取关键人类型编码
	var keyman_type = $('.keyman-type').find('.cur').text();
	 if (keyman_type == '请选择') {
		 	keyman_type = '';
	    } else if (keyman_type == '政企关键人') {
	    	keyman_type = '1000';
	    } else if (keyman_type == '公众关键人') {
	    	keyman_type = '1002';
	    } else if (keyman_type == '普通关键人') {
	    	keyman_type = '1005';
	    }
	//组装参数
	var param= {
			'serviceName': 'keymanManageWebService',
 	        'methodName': 'exportKeymanList',
 	        'CUST_NUM': $('#cust_num').val(),//用户号码
 	        'KEYMAN_TYPE': keyman_type,//关键人类型
 	        'LATN_ID': latn_id,//本地网
 	        'TIME_RANGE': $('.timeRange').val(),//时间范围
 	        'TABLE_TITLE':JSON.stringify(tableHead)
		    };
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
