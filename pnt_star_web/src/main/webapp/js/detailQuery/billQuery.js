var customTable = new queryListTable();
//全局的服务名
var globServiceName="RtBillItem";

$().ready(function(){
	
	/***
	 * 查询数据，翻页的时候也会自动调用
	 */
	function initQueryData(){
		//账期
		var billingCycleId = $("#billingCycleId").val();
		//类型
		var qryType = $("#qryType").val();
		//号码
		var phoneNum = $("#phoneNum").val();

		
		var queryUrl = nowurl+'ControlServlet.do?serviceName=commonServlet&methodName=gridValue_rows';
		var requestObj = new Object();
		requestObj['url'] = queryUrl;
		requestObj['data'] = new Object();
		requestObj['SvcCont'] = new Object();
		var SvcCont = new Object();

		//系统参数
		requestObj['data']['flowCode']= globServiceName;//流程编码
		requestObj['data']['pageIndex'] = customTable.getCurrentPage()-1;
		requestObj['data']['pageSize'] = customTable.getPageSize();
		
		//如果有需要可以根据globServiceName动态变更传递的参数，建议不用根据服务名的不同传递不同的参数，统一参数方便sql编写
		//step1业务参数,操作人属性operAttrStruct由后台BaseService.getOperAttrStruct进行包装
		SvcCont['qryType'] = qryType;//查询类型
		SvcCont['billingCycleId']= billingCycleId;//查询帐期
		SvcCont['objType'] = "3";//对象类型 3用户号码
		SvcCont['objValue'] = phoneNum;//对象值 号码
		SvcCont['objAttr'] = "2";//用户号码属性 2移动  
		requestObj['data']['SvcCont']=JSON.stringify(SvcCont);
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
			selectMode:null,//null没有选择,单选single,多选multi
			widthMode:'percent',//null,percent
//			onActionRenderer:onActionRenderer,//格式化操作列函数
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
//		globServiceName=$(this).val();
//		initTableHeadWithServiceName()
	});
	
	//绑定查询按钮
	$("#search_btn").click(function(){
		//设置需要提交的参数
		var requestObj=initQueryData();
		//重置当前页
		customTable.initDefaultConfig();
		customTable.ajaxRequest(requestObj);
	});
	
	//初始化下拉框值
	function initSelect(){
		/**
		 * @param compId:qryType下拉框组件ID
		 * @param service_name:null --sql配置，如果不指定则从GET_TB_PTY_CODE表中获取
		 * @param flag:false --是否显示可选择
		 * @param defaultValue:1 当前下拉框加载完毕后设置的值，用于修改页面
		 * @param busi_type:billQuery_qryType --字典表中类型
		 * @param param:null --初始参数
		 */
		selectComboxInfo("qryType",null,false,1,"billQuery_qryType",null);//
	}
	
	//页面加载完成后初始化表头信息
	function initPage(){
		//web前端调用的流程名称在APIID后面加一个_web的后缀
		globServiceName="RtBillItem";
		//初始化下拉框的值
		initSelect();
		//初始化表格
		initTableHeadWithServiceName();
	}
	initPage();
});