var customTable = new queryListTableByExcel();
//全局的服务名，修改业务类型的时候，动态获取
var globServiceName="";

//编辑
function editRow(row_uid,latn_id) {
     window.open(nowurl+"html/test/editTest.html?IMP_BATCH_NO="+row_uid+"&LATN_ID="+latn_id);
}

//删除
function delRow(row_uid,latn_id) {
	 //删除对象
	alert("删除记录：IMP_BATCH_NO="+row_uid+",LATN_ID="+latn_id);
     //TODO具体的业务实现
}

//详情
function detailRow(row_uid,latn_id) {
     window.open(nowurl+"html/test/detailTest.html?IMP_BATCH_NO="+row_uid+"&LATN_ID="+latn_id);
}


$().ready(function(){
	
	/***
	 * 查询数据，翻页的时候也会自动调用
	 */
	function initQueryData(){
		var queryUrl = nowurl+'ControlServlet.do?serviceName=commonServlet&methodName=gridValue_rows';
		var requestObj = new Object();
		requestObj['url'] = queryUrl;
		requestObj['data'] = new Object();
		//系统参数
		requestObj['data']['service_name']= globServiceName;
		requestObj['data']['pageIndex'] = customTable.getCurrentPage()-1;
		requestObj['data']['pageSize'] = customTable.getPageSize();
		
		//如果有需要可以根据globServiceName动态变更传递的参数，建议不用根据服务名的不同传递不同的参数，统一参数方便sql编写
		//业务参数
		requestObj['data']['ACC_NBR']= 1;
		requestObj['data']['TIME'] = 2;
		requestObj['data']['END_TIME'] =  3;
		requestObj['data']['TYPE'] = 4;
		return requestObj;
	}
	
	/***
	 * 数据加装完调用的函数
	 * pageDate，服务器返回的json数组对象
	 * 
	 */
	function loadEnd(pageData){
		
	}
	/***
	 * 
	 * @tempBean 服务器返回的当前行的json对象
	 * @param colCode 列编码
	 * @param rowIdx 行索引
	 * @table  当前queryListTable对象
	 */
	function onActionRenderer(tempBean,colCode,rowIdx,table){
        var uid = tempBean["IMP_BATCH_NO"];
        var latn_id=tempBean["LATN_ID"];
        var s =  
        	'<a  href="javascript:editRow(\'' + uid + '\',\''+latn_id+'\')" ><span class=" btn-ico ico-xg mr10" title="编辑"></span></a>'
        	+'<a  href="javascript:delRow(\'' + uid + '\',\''+latn_id+'\')" ><span class=" btn-ico ico-sc mr10" title="删除"></span></a>'
        	+'<a  href="javascript:detailRow(\'' + uid + '\',\''+latn_id+'\')" ><span class=" btn-ico ico-sp mr10" title="明细"></span></a>'
         ;
        return s;
	}
    
	/****
	 * 
	 * 动态初始化表头
	 */
	function initTableHeadWithServiceName(){
		//重置当前页
		customTable.initDefaultConfig();
		customTable.init({
			serviceName:globServiceName,
			selectMode:'multi',//null没有选择,单选single,多选multi
			widthMode:'percent',//null,percent
			onActionRenderer:onActionRenderer,//格式化操作列函数
			table : $('#checkTable'), //列表table展示对象
			tableTbody : $('#checkTable'),
			tablePageDiv :$('#tablePageDiv'), //列表table page展示对象
			pageSize : 10, // 每页显示的条数，可以不设定，默认是10
			initQueryDataFun :initQueryData,
			loadEndFun : loadEnd,   // 数据加载完成后执行的自定义方法（可选）
			extConfig : {
				// 是否显示进度条
				isOnloadProgress : true,
				// 是否立即加载
				isOnload : false
			}
		});
		
	}
	
	//切换业务类型，显示不同的表头
	$("#busi_type").change(function(){
		//初始化表头信息
		globServiceName=$(this).val();
		initTableHeadWithServiceName()
	});
	
	//绑定查询按钮
	$("#search_btn").click(function(){
		//设置需要提交的参数
		var requestObj=initQueryData();
		//重置当前页
		customTable.initDefaultConfig();
		customTable.ajaxRequest(requestObj);
	});
	
	
	//提交审批
	$("#btn_commit").click(function(){
		//设置需要提交的参数
		var selections=customTable.getSelected();
		var selectedIds="";
		var IdColKey="IMP_BATCH_NO";
		
		//根据不同的业务取不同的列
		if(globServiceName=="TPSS_ADJ_QUERY"){
			IdColKey="IMP_BATCH_NO";
		}else if(globServiceName=="TPSS_MINGZI_QUERY"){
			IdColKey="IMP_BATCH_NO";
		}else if(globServiceName=="TPSS_OTHER_QUERY"){
			IdColKey="IMP_BATCH_NO";
		}
		
		//如果有选中的值
		/***selectMode="multi"表格是多选的场景*******/
		if(selections.length > 0 ){
			// 有记录
			for(var i = 0; i < selections.length ; i ++ ) {
				var selected = selections[i];
				if(i>0){
					selectedIds+=","
				}
				selectedIds += selected[IdColKey];
			}
		}
		
		/***selectMode=single表格是单选的场景
		selectedIds=selections["IMP_BATCH_NO"];
		****/
		alert(selectedIds);
	});
	
	//页面加载完成后初始化表头信息
	function initPage(){
		globServiceName=$('#busi_type').val();
		initTableHeadWithServiceName();
	}
	initPage();
});