/**
 * 分页列表JS对象
 * 
 * 增加service_name，支撑动态表头
 * 增加selectMode:null则是没有checkbox,single单选，multi多选
 * 
 * =======================使用方式=====================
	1、初始化；
	var customTable = new queryListTableByImport();
	
	customTable.init({
	serviceName:'TPSS_ADJ_QUERY',
	selectMode:'multi',//null,single,multi
	widthMode:'percent',//null,percent
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
	2、查询数据
		//绑定查询按钮
	//根据custOrderId查询订单
	$("#search_btn").click(function(){
		//设置需要提交的参数
		var requestObj=initQueryData();
		//重置当前页
		customTable.initDefaultConfig();
		customTable.ajaxRequest(requestObj);
	});
	3、设置请求的参数，自定义函数样例
	 查询数据，翻页的时候也会自动调用
	function initQueryData(){
		var queryUrl = nowurl+'ControlServlet.do?serviceName=commonServlet&methodName=gridValue_rows';
		var requestObj = new Object();
		requestObj['url'] = queryUrl;
		requestObj['data'] = new Object();
		requestObj['data']['service_name']= 'TPSS_ADJ_QUERY';
		requestObj['data']['ACC_NBR']= 1;
		requestObj['data']['TIME'] = 2;
		requestObj['data']['END_TIME'] =  3;
		requestObj['data']['TYPE'] = 4;
		requestObj['data']['pageIndex'] = customTable.getCurrentPage()-1;
		requestObj['data']['pageSize'] = customTable.getPageSize();
		return requestObj;
	}
	   */
if (typeof(nowurl) == "undefined") {
	nowurl=document.location.href;
	nowurl=nowurl.substring(0,nowurl.indexOf("html"));
}
queryListTableByImport = function(){
	var temp = this;
	
	//服务名
	this.serviceName=null;
	//列头信息
	this.columnInfos=null;
	//selectMode
	this.selectMode=null;
	//宽度模式widthMode,默认是null绝对宽度， percent按百分比
	this.widthMode=null;
	
	//操作列函数，用来处理每一行上面都有一列操作按钮单元格onActionRenderer可以为空
	this.onActionRenderer=null;
	/**
	 * 列表table展示对象
	 */
	this.table = null;
	this.tablehead = null;
	/**
	 * 列表table body展示对象
	 */
	this.tableTbody = null;
	/**
	 * 列表table page展示对象
	 */
	this.tablePageDiv = null;
	
	/**
	 * 列表table 数据集合（非外部传入）
	 */
	this.pageData = null;
	
	/**
	 * 列表table 列数
	 */
	this.columnNum = null;
	
	/**
	 * 列表行排列集函数
	 */
	this.processRowFun = null;
	
	/**
	 * 初始化条件函数
	 */
	this.initQueryDataFun = null;
	
	/**
	 * 初始化行点击事件(可选)
	 */
	this.rowClickFun = null;
	
	/**
	 * 数据加载完成后执行的扩展方法(可选)
	 */
	this.loadEndFun = null;
		
	/**
	 * 扩展参数
	 */
	this.extConfig = {
		// 是否显示进度条
		isOnloadProgress : true,
		// 是否立即加载
		isOnload : true
	}
	/**
	 * 初始化表格的css
	 */
	this.initCss = function(){
		$(this.tableTbody).find("tr").mouseover(function() { // 如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
			$(this).addClass("over");
		}).mouseout(function() { 
			$(this).removeClass("over");
		})
		$(this.tableTbody).find("tr:even").addClass("alt"); //给class为stripe_tb的表格的偶数行添加class值为alt
		
		//check box,radio box
		$(".checkTable").selectCheck();
		$(".radiobox").selectRadio();
		$(".checkTable").selectRadio();
	}
	/**
	 * 分页数据
	 */
	this.defaultConfig = {
		// 当前页号, 从1开始
		currentPage : 1,
		// 上一页 
		previousPage : 1,
		// 下一页
		nextPage : 1,
		// 总页数
		totlePage : 1,
		// 总记录数
		totleRows : 0,
		// 当前记录数开始序列，默认从0开始
		startNum : 0,
		// 单页显示最大数量
		pageSize : 10,
		// 单页显示数量选择集合
		selectPageSize : new Array(10,20,50,100),
		// 设置当前页
		setCurrent : function(_currentPage) {
			this.currentPage = _currentPage;
			this.previousPage = (this.currentPage == 1) ? 1 : (this.currentPage - 1);
			this.nextPage = (this.currentPage == this.totlePage) ? this.currentPage : (this.currentPage + 1);
			this.startNum = (this.currentPage - 1) * this.pageSize;
		},
		// 设置总记录数
		setTotle : function(_totleRows) {
			this.totleRows = _totleRows;
			var overNum = this.totleRows % this.pageSize;
			this.totlePage = (this.totleRows - overNum) / this.pageSize;
			if(overNum > 0 ) {
				this.totlePage ++ ;
			}
			// 第一次加载时如果有多页数据，会遇到nextPage一直为1，需要初始化一次
			this.setCurrent(this.currentPage);
		}
	}
	
	/**
	 * 针对输入自定义业务条件后查询，需要初始化到第一页(对外方法)
	 */
	this.initDefaultConfig = function() {
		// 当前页号, 从1开始
		this.defaultConfig.currentPage = 1;
		// 上一页 
		this.defaultConfig.previousPage = 1;
		// 下一页
		this.defaultConfig.nextPage = 1;
		// 总页数
		this.defaultConfig.totlePage = 1;
		// 总记录数
		this.defaultConfig.totleRows = 0;
		// 当前记录数开始序列，默认从0开始
		this.defaultConfig.startNum = 0;
	}
	
	/**
	 * 如果设置了pageSize需要初始化
	 */
	this.initPageSize = function(_pageSize) {
		this.defaultConfig.pageSize = _pageSize;
		var isDefault = false;
		for(var i = 0 ; i < this.defaultConfig.selectPageSize.length ; i ++ ) {
			if(_pageSize == this.defaultConfig.selectPageSize[i]) {
				isDefault = true;
				break;
			}
		}
		if(!isDefault) {	// 如果不是默认的需要加入selectPageSize
			this.defaultConfig.selectPageSize.unshift(_pageSize);
		}
	}
	
	/**
	 * 打开和关闭查询进度条
	 */
	this.openProgress = function() {
		if(this.extConfig.isOnloadProgress) {	// 是否显示进度条
			var _this = this;
			customProgressbar.show({
				waitTime : 600 ,	// 进度条的加载时间，单位秒，可以为空，默认超时60秒；
				barClosedFun : function() { // 进度条关闭后触发函数，默认空，自定义
					// 无记录
					var tableBody = '<tr><td colspan="' +_this.columnNum+ '" style="color: red;" align="center">查询超时，请稍后再试……</td></tr>';
					_this.tableTbody.html(tableBody);
					_this.tablePageDiv.html("");
				}
			});
		}
	}
	this.closeProgress = function() {
		if(this.extConfig.isOnloadProgress) {	// 是否显示进度条
			customProgressbar.hide();
		}
	}
	
	/**
	 * 执行后台ajax查询
	 */
	this.ajaxRequest = function(ops) {
		this.openProgress();
		var _this = this;
		$.ajax({
			url : ops["url"],
			type : 'POST',
			async : true,
			cache : false,
			data : ops["data"],
			dataType : 'json',
			timeout : 60000,
			success : function(responseObj) {
				var val = JSON.stringify(responseObj);
				_this.closeProgress();
				//异常处理
				if(null != responseObj&&responseObj.errormsg!=null&&responseObj.errormsg!=undefined&&responseObj.errormsg!=""){
					alert(responseObj.errormsg);
				}
				else {
					if(null != responseObj && null != responseObj.data ) {
						_this.pageData = responseObj.data;
						_this.defaultConfig.setTotle(responseObj.total);
						_this.writeTable();
					}
				}
			},
			error : function() {
				_this.pageData = null;
				// 数据加载完毕后，执行扩展方法
				_this.loadEndEvent(null);
			}
		});
	}
	
	/**
	 * 初始化查询列表数据
	 */
	this.initTable = function() {
		// 初始化条件函数
		var requestObj = this.initQueryDataFun();
		if(null != requestObj ) {
			this.ajaxRequest(requestObj);
		}
	}
	/**
	 * 初始化表头和分页组件
	 */
	this.initTableNoRequest = function() {
		var _this = this;
		_this.pageData = null;
		_this.defaultConfig.setTotle(0);
		if(_this.serviceName!=null&&_this.serviceName!=undefined){
			_this.tableHead=_this.loadHead();
		}
		var tableBody = _this.tableHead;
		this.tableTbody.html(tableBody);
		// 初始化分页数据
		this.initTablePage();
	}
	
	this.writeTable = function() {
		//如果表头没有初始化则先进行初始化
		if(this.serviceName!=null&&this.serviceName!=undefined&&(this.tableHead==null||this.tableHead==undefined||this.tableHead=="")){
			this.tableHead=this.loadHead();
		}
		
		var tableBody = this.tableHead.replace("</tbody>","");
		if(null != this.pageData && this.pageData.length > 0 ) {
			var seqNum = this.defaultConfig.startNum + 1;	// 序号
			// 有记录
			for(var i = 0; i < this.pageData.length ; i ++ ) {
				var tempBean = this.pageData[i];
				// 执行外部封装的行处理展示对象
//				var doubleRowStyle = i % 2 == 0 ? '' : 'class="tableWRow"';
				var doubleRowStyle = '';
				tableBody = tableBody + '<tr id="tr" rowEvent="' +i+ '" ' +doubleRowStyle+ '>';
				tableBody = tableBody + this.processRowFun(seqNum + i,tempBean);
				tableBody = tableBody + '</tr>';
			}
			tableBody=tableBody+"</tbody>";
			this.tableTbody.html(tableBody);
			// 添加行点击事件
			this.addRowClickEvent();
			// 初始化分页数据
			this.initTablePage();
			this.initCss();
			// 数据加载完毕后，执行扩展方法
			this.loadEndEvent(this.pageData);
		} else {
			// 无记录
			tableBody += '<tr>'
					+ '<td colspan="' +this.columnNum+ '" style="color: red;" align="center">没有相关记录</td>'
					+ '</tr>';
			this.tableTbody.html(tableBody);
			// 初始化分页数据
			this.initTablePage();
			// 数据加载完毕后，执行扩展方法
			this.loadEndEvent(null);
		}
		//this.tableTbody.find().hover(function(){ // 鼠标行进入变色
		//	$(this).addClass("tron");
		//},function(){
		//	$(thils).removeClass("tron");
		//});
	}
		
	/**
	 * 添加行点击事件
	 */
	this.addRowClickEvent = function() {
		if(this.rowClickFun) {
			var thisObj = this;
			var rows = this.table.find('tr[rowEvent]');
			rows.css({cursor:"hand"});
			rows.click(function() {
				// 行点击变色
				rows.removeClass("tableClick");
				$(this).addClass("tableClick");
				// 行点击事件
				var dataListIndex = parseInt($(this).attr("rowEvent") );
				var selRowData = thisObj.pageData[dataListIndex];
				selRowData['ROW_INDEX'] = dataListIndex;	// 行号也返回，从0开始计算
				thisObj.rowClickFun(selRowData);
			});
		}
	}
		
	/**
	 * 数据加载完毕后，执行扩展事件
	 */
	this.loadEndEvent = function(tData) {
		// 数据加载完毕后，执行扩展方法
		if(this.loadEndFun) {
			this.loadEndFun(tData);
		}
	}
		
	/**
	 * 初始化分页操作区域数据
	 */
	this.getPageControlHtml = function() {
		var  pageControlHtml = '';
		
		// 统计数量
		pageControlHtml += '<span>共&nbsp;<font color="#0091d6">' +this.defaultConfig.totleRows+ '</font>&nbsp;条记录</span>&nbsp;';	
		pageControlHtml += '当前第&nbsp;<font color="#0091d6">' +this.defaultConfig.currentPage+ '</font>&nbsp;页&nbsp;';
		// 每页显示按钮
		pageControlHtml += '<select name="pageSize" id="pageSize" class="page_inp" goToPage="' +this.defaultConfig.currentPage+ '" >';
//		pageControlHtml += '<div style="display: none;"><select name="pageSize" id="pageSize" class="page_inp" goToPage="1" >';
		for(var i = 0 ; i < this.defaultConfig.selectPageSize.length ; i ++ ) {
			pageControlHtml += '<option ' +(this.defaultConfig.pageSize == this.defaultConfig.selectPageSize[i] ? 'selected="selected"' : '')
			+ ' >' +this.defaultConfig.selectPageSize[i]+ '</option>';
		}
		pageControlHtml += '</select>条/页&nbsp;&nbsp;';
		// 首页按钮
		pageControlHtml += '<a id="firstA" href="javascript:void(0);" ';
		pageControlHtml += (1 == this.defaultConfig.currentPage) ? '' : 'goToPage="1" ';
		pageControlHtml += '>首页</a>';
		// 上一页按钮
		pageControlHtml += '<a id="upA" href="javascript:void(0);" ';
		pageControlHtml += (this.defaultConfig.previousPage == this.defaultConfig.currentPage) ? '' : 'goToPage="' +this.defaultConfig.previousPage+ '" ';
		pageControlHtml += '>上一页</a>';
		// 下一页按钮
		pageControlHtml += '<a id="downA" href="javascript:void(0);" ';
		pageControlHtml += (this.defaultConfig.nextPage == this.defaultConfig.currentPage) ? '' : 'goToPage="' +this.defaultConfig.nextPage+ '" ';
		pageControlHtml += '>下一页</a>';
		// 尾页按钮
		pageControlHtml += '<a id="lastA" href="javascript:void(0);" ';
		pageControlHtml += (this.defaultConfig.totlePage == this.defaultConfig.currentPage) ? '' : 'goToPage="' +this.defaultConfig.totlePage+ '" ';
		pageControlHtml += '>尾页&nbsp;</a>';

		pageControlHtml += '<input id="curPage" class="ml10" name="curPage" type="hidden" value="' +this.defaultConfig.currentPage+ '" />';
		pageControlHtml += '<input name="pageNum" id="pageNum" class="page_inp ml10" style="margin-top:-2px" maxlength="8" />&nbsp;';

		pageControlHtml += '<a type="button" id="goButton" name="goButton" class="page_normal" goToPageByNum="">GO</a>';
		return pageControlHtml;
	}
		
	/**
	 * 初始化分页page数据
	 */
	this.initTablePage = function() {
		var thisObj = this;
		var tempPage = this.getPageControlHtml();
		this.tablePageDiv.html(tempPage);
		if(this.defaultConfig.totlePage > 0) {	// 最大页数大于0
			// 对select选择页数操钮添加事件
			var selectObject = $(this.tablePageDiv.find('select[id=pageSize]'));
			selectObject.change(function() {
				//thisObj.goToPage(selectObject.attr('goToPage'));
				//重定向到首页，防止pageSize太大没有数据的问题
				thisObj.goToPage(1);
			});
			// 对点击首页按钮添加事件
			var firstA = $(this.tablePageDiv.find('a[id=firstA]') );
			if(firstA.attr('goToPage') ) {
				firstA.click(function() {
					thisObj.goToPage(firstA.attr('goToPage'));
				});
			}
			// 对点击上一页按钮添加事件
			var upA = $(this.tablePageDiv.find('a[id=upA]') );
			if(upA.attr('goToPage') ) {
				upA.click(function() {
					thisObj.goToPage(upA.attr('goToPage'));
				});
			}
			// 对点击下一页按钮添加事件
			var downA = $(this.tablePageDiv.find('a[id=downA]') );
			if(downA.attr('goToPage') ) {
				downA.click(function() {
					thisObj.goToPage(downA.attr('goToPage'));
				});
			}
			// 对点击末页的按钮添加事件
			var lastA = $(this.tablePageDiv.find('a[id=lastA]') );
			if(lastA.attr('goToPage') ) {
				lastA.click(function() {
					thisObj.goToPage(lastA.attr('goToPage'));
				});
			}
			// 对点击GO的分页查询按钮添加事件
			this.tablePageDiv.find('#goButton').click(function() {
				thisObj.goToPageByNum();
			});
		}
	}
		
	/**
	 * 分页查询中点击翻页按钮的查询事件
	 * @param pageNum
	 */
	this.goToPage = function(pageNum) {
		//alert("goToPage======" +pageNum);
		var selectedPageSize = this.tablePageDiv.find('#pageSize').val();
		if(selectedPageSize && selectedPageSize.length > 0) {
			this.defaultConfig.pageSize = parseInt(selectedPageSize);
		}
		this.defaultConfig.setCurrent(parseInt(pageNum));
		this.tablePageDiv.find('#curPage').val(pageNum);
		this.initTable();
	}
	
	/**
	 * 分页查询中点击“Go”去指定页数的查询事件
	 */
	this.goToPageByNum = function() {
		var pageNum = this.tablePageDiv.find('#pageNum').val();
		if('' != pageNum && pageNum.match(/^[0-9]*[1-9][0-9]*$/)) {
			pageNum = parseInt(pageNum);
			if(this.defaultConfig.totlePage >= pageNum ) {
				this.defaultConfig.setCurrent(pageNum);
				this.tablePageDiv.find('#curPage').val(pageNum);
				this.initTable();
			}
		}
	}
	
	this.checkParam = function() {
		if(!this.table) {
			alert('列表table展示对象-不能为空，请检查如何使用customTable.js代码！！');
			return false;
		} else if(!this.tableTbody) {
			alert('列表table body展示对象-不能为空，请检查如何使用customTable.js代码！！');
			return false;
		} else if(!this.tablePageDiv) {
			alert('列表table page展示对象-不能为空，请检查如何使用customTable.js代码！！');
			return false;
//		} else if(!this.columnNum) {
//			alert('列表table 列数-不能为空，请检查如何使用customTable.js代码！！');
//			return false;
//		} else if(!this.processRowFun) {
//			alert('列表行排列集函数-不能为空，请检查如何使用customTable.js代码！！');
//			return false;
		} else if(!this.initQueryDataFun) {
			alert('初始化条件函数-不能为空，请检查如何使用customTable.js代码！！');
			return false;
		} else if(!this.serviceName&&(this.columnNum==0||!this.columNum)){
			alert('列表table 列数-不能为空，请检查如何使用customTable.js代码！！');
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 初始化
	 */
	this.init = function(tData) {
	
		//列表table展示对象
		this.table = tData['table'];
		//列表table body展示对象
		this.tableTbody = tData['tableTbody'];
		//列表table page展示对象
		this.tablePageDiv = tData['tablePageDiv'];
		this.tableHead = tData['tablehead'];
		//列表table 列数
		this.columnNum = tData['columnNum'];
		//列表行排列集函数
		if(tData['processRowFun']){
			this.processRowFun = tData['processRowFun'];
		}else{
			//使用默认的函数
			this.processRowFun=this.processRowFunDefault;
		}
		//操作列函数
		if(tData['onActionRenderer']){
			this.onActionRenderer = tData['onActionRenderer'];
		}
		
		if(tData['widthMode']){
			this.widthMode=tData['widthMode'];
		}
		
		//初始化条件函数
		// initQueryDataFun实现的函数返回值必须是Object对象，有且仅有url和data两个属性
		this.initQueryDataFun = tData['initQueryDataFun'];
		//初始化行点击事件（可选）
		this.rowClickFun = tData['rowClickFun'];
		//数据加载完成后执行的自定义方法（可选）
		this.loadEndFun = tData['loadEndFun'];
		//服务名
		if(tData['serviceName']){
			this.serviceName=tData['serviceName'];
		}
		//选择模式
		if(tData['selectMode']){
			this.selectMode=tData['selectMode'];
		}
		if(tData['pageSize']) {	// 如果设置了pageSize需要初始化
			this.initPageSize(tData['pageSize']);
		}
		// 初始化扩展参数
		jQuery.extend(this.extConfig, tData['extConfig']);
		
		if (this.checkParam() ) {	// 校验参数是否传入完整
			if(this.extConfig.isOnload) {	// 立即加载
				this.initTable();
			}else{
				this.initTableNoRequest();
			}
		}
	}
	
	/**
	 * 获取pageSize值，单页最大数量
	 */
	this.getPageSize = function() {
		return this.defaultConfig.pageSize;
	}
	
	/**
	 * 获取startNum值，默认从0开始累加
	 */
	this.getStartNum = function() {
		return this.defaultConfig.startNum;
	}
	/***
	 * 获取当前页
	 */
	this.getCurrentPage = function() {
		return this.defaultConfig.currentPage;
	}

	/****
	 * 以下为根据tpss的特性进行的改造
	 * 处理行数据显示
	 * 
	 */
	this.processRowFunDefault=function(i,tempBean){
		var rowhtml="";
		if(this.selectMode!=null){
			
			if(this.selectMode=="single"){
				rowhtml+='<td><p><span class="radio-check"><input type="radio" /></span></p></td>';
			}else{
				rowhtml+='<td><p><span class="checkbox-check"><input type="checkbox" /></span></p></td>';
			}
			
		 }
		
		for ( var i = 0; i <this.columnInfos.length; i++){
			var info=this.columnInfos[i];
			var colCode=info["field"];
			var cellValue=tempBean[colCode];
			//去除undefined的显示
			if(cellValue==undefined){
				cellValue="";
			}
			
			//先判断是否为隐藏列
			if(info["colType"]==0||info["colType"]=="0"){
				rowhtml+='<td style="display:none;" class="td-overflow" name="'+colCode+'"  >';
			}else{
				rowhtml+='<td class="td-overflow" name="'+colCode+'"  >';
			}
			
			//判断是否为操作列
			if(info["renderer"]=="onActionRenderer"){
				//如果是操作列调用用户自定义的onActionRenderer函数
				var cellHtml=this.onActionRenderer(tempBean,colCode,i,this);
				rowhtml+=cellHtml;
			}else{
				//鼠标悬浮显示
				rowhtml+='<div class="p-r">';
				//判断是否为超链接
				if(info['is_href']=='Y'){
					rowhtml+=this.renderUrl(tempBean,i,cellValue);
				}else{
					rowhtml+='<p>'+cellValue+'</p>'
				}
				//悬浮显示
	            rowhtml+='<div class="hide-text"><i class="jt-up"></i>'+cellValue+'</div>'
	            +'</div>'
			}
			
			+'</td>';
		}
		return rowhtml;
	}
	
	/**
	 * 渲染操作按钮，处理表格中的超链接
	 * @param e
	 * @returns {String}
	 */
	this.renderUrl=function(tempBean,colIdx,cellValue) {
		var record = tempBean;
		var info=this.columnInfos[colIdx];
		var field = info.field;
		var value = cellValue;
		var url="";
		var rst="";
		if(info.name){
			var urlSpi = info.name.split("||");
			url = urlSpi[0];
			if(url.indexOf("?")!=-1){ 
				var str = url.substr(url.indexOf("?")+1); 
				var strs= str.split("&"); 
				for(var i=0;i < strs.length;i++){
					//contract-cust-edit.html?code=#CONTRACT_CODE&name=#CONTRACT_NAME
					//val 就是CONTRACT_CODE
					var val = strs[i].split("=")[1];
					if(val.indexOf("#") != -1){
						//#是来源于表格中的值
						val=val.substr(val.indexOf("#")+1); 
						url = url.replace("#"+val,record[val]);
					}else if(val.indexOf("$") != -1){
						//$是来源于页面表单中的值
						val=val.substr(val.indexOf("$")+1); 
						url = url.replace("$"+val,$('#'+val).val());
					}
				} 
			}
			var href_type = urlSpi[1];
			if(href_type=='1'){
				rst = '<a class="LinkWord" href="javascript:queryListTableByImport.openWin(\'' + url + '\')">'+value+'</a>';
			}else if(href_type=='2'){
				rst = '<a class="LinkWord" href="javascript:queryListTableByImport.jumpPage(\'' + url + '\')">'+value+'</a>';
			}else if(href_type=='3'){
				rst = '<a class="LinkWord" href="javascript:queryListTableByImport.openMini(\'' + url + '\')">'+value+'</a>';
			}else if(href_type=='4'){
				rst = '<a class="LinkWord" href="' + url + value+'" target="_blank" style="padding-left:20px;padding-right:50px;">'+value+'</a>';
			}else{
				rst = '<a class="LinkWord" href="javascript:queryListTableByImport.openWin(\'' + url + '\')">'+value+'</a>';
			}
		}
		
	    return rst;

	}
	
	queryListTableByImport.openWin=function(url){
		window.open(url);
	}
	queryListTableByImport.jumpPage=function(url){
		window.location.href=url;
	}
	/***
	 * 根据serviceName从后台加载表头信息
	 */
	this.loadHead=function(){
		this.columnInfos=[];
		var columnInfos=this.columnInfos;
		var param = {"service_name":"IMPORT_QUERY_FIELDS",
					 "service_code":this.serviceName};
		$.ajax({
	        type: "POST",
	        async: false, 
	        data: param,
	        dataType: "json",
	        url : nowurl+'ControlServlet.do?serviceName=importApproveServlet&methodName=gridValue',
	        success: function(data) {
	        	if(data.length > 0){
	    			var lines = data.length;
	    			for(var i=0;i < data.length; i++){
	    				var obj = data[i];
	    				var info = new Object;
	    				info["width"]=120;
	    				info["headerAlign"]="center";
	    				info["header"]=obj.COL_DESC;
	    				info["colType"]=3;
    					info["field"]=obj.COL_NAME;
    					info["align"]="center";
	    				
	    				columnInfos.push(info);
	    			}
	    		}
	        },
	        beforeSend: function() {
	            //$.jBox.tip("正在查询数据...", 'loading');
	        }

	    });
		//开始生成表头到tr
		/**
		 * 
		 * 
		 * <colgroup>
          <col width="4%">
        	<col width="18%">
        	<col width="6%">
        	<col width="6%">
        	<col width="6%">
        	<col width="6%">
        	<col width="18%">
        	<col width="6%">
        	<col width="13%">
        	<col width="10%">
        	<col width="">
		</colgroup>
        <tr>
            <th><p><span class="checkbox-check"><input type="checkbox" id="checkAll" /></span></p></th>
            <th>导入批次号</th>
            <th>业务类</th>
            <th>本地网</th>
            <th>账期</th>
            <th>记录数</th>
            <th>导入说明</th>
            <th>导入人</th>
            <th>导入时间</th>
            <th>状态</th>
            <th>ITSM工单号</th>
        </tr>
        
        
        
		 * 
		 * 
		  ***/;
		  //宽度
		 var table_colGroup_str="<colgroup>";
		 if(this.selectMode!=null){
			 table_colGroup_str+='<col width="45">';
		 }
		 //th列名称
		 var table_head_tr="<tr>"; 
		 if(this.selectMode!=null){
			 this.columnNum=columnInfos.length+1;
			 if(this.selectMode=="single"){
				 table_head_tr+='<th><p><span>选择</span></p></th>';
				}else{
					 table_head_tr+='<th><p><span class="checkbox-check"><input type="checkbox" id="checkAll" /></span></p></th>';
				}
		 }else{
			this.columnNum=columnInfos.length;
		 }
		
		for ( var i = 0; i <columnInfos.length; i++){
			var info=columnInfos[i];
			var width=info["width"];
			var colName=info["header"];
			//隐藏表头
			if(info["colType"]==0||info["colType"]=="0"){
				table_colGroup_str+='<col width="'+0+'" style="display:none;">';
				table_head_tr+='<th style="display:none;">'+colName+'</th>';
			}else{
				table_colGroup_str+='<col width="'+width+'">';
				table_head_tr+='<th>'+colName+'</th>';
			}
		}
		table_colGroup_str+="</colgroup>";
		 table_head_tr+="</tr>";
		return table_colGroup_str+"<tbody>"+table_head_tr+"</tbody>";
	}
	
	/**
	 * 
	 * 获取选中的行信息，
	 *   如果selectMode=multi返回的是服务器返回的json数组，不是界面上经过格式化后的数据
	 *   如果selectMode=single返回的是一条记录
	 * 选中的行索引在key：_currow_index中保存
	 * 如果需要界面上格式化后的数据，需要根据索引行自行查找
	 * 
	 */
	this.getSelected=function(){
		var selectedArray=[];
		var $trs  = $(this.tableTbody).find("tr");
		//遍历所有行信息
		for(var i=0; i<$trs.length; i++){
		    var $curTr = $trs.eq(i);//循环获取每一行搜索
			var selIdx=$curTr.attr("rowevent");
			if(selIdx!=null&&selIdx!=""){
				
				//如果是多选则找checkbox-checked
				if(this.selectMode=="multi"){
					if($curTr.children(":first").find("p span").hasClass("checkbox-checked")){
						//checkbox选中了，放入数组中
						var selRowData = this.pageData[selIdx];
						//行号
						selRowData["_currow_index"]=selIdx;
						selectedArray.push(selRowData);
					}
				}else{
					if($curTr.children(":first").find("p span").hasClass("radio-checked")){
						//checkbox选中了，放入数组中
						var selRowData = this.pageData[selIdx];
						//行号
						selRowData["_currow_index"]=selIdx;
						selectedArray.push(selRowData);
						return selRowData;
					}
					
				}
				
			}
		}
		if(this.selectMode=="multi"){
			return selectedArray;
		}else{
			return null;
		}
		
		 
	}
	
	
	
}

