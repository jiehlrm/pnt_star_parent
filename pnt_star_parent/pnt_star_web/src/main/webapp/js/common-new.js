// JavaScript Document
$(function(){
	//解决placeholder在IE8下无效的问题（仅针对type=text，type=password无效）
	if( !('placeholder' in document.createElement('input')) ){
		$('input[placeholder],textarea[placeholder]').each(function(){  
			var that = $(this),  
			text= that.attr('placeholder');  
			if(that.val()===""){  
				that.val(text).addClass('placeholder');  
			}  
			that.focus(function(){  
				if(that.val()===text){  
					that.val("").removeClass('placeholder');  
				}  
			})  
			.blur(function(){  
				if(that.val()===""){  
					that.val(text).addClass('placeholder');  
				}  
			})  
			.closest('form').submit(function(){  
				if(that.val() === text){  
					that.val('');  
				}  
			});  
		});
	};
});

/*获取url的参数*/
function getUrlParam(name){
	 var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	 var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	 if (r!=null) return unescape(r[2]); return  null; //返回参数值
 }

//高级搜索
$(function(){
    $('.searchMore').click(function (){
		$('.hideSearch').slideToggle();
		$('.hideBtn').slideToggle();
		$(this).toggleClass('hover');
    });
});

//单选复选
$(function(){
	//单选
	$(".radiobox").selectRadio();
		
	//表格复选-checkbox
	$(".checkTable").selectCheck();

});


//tab切换
$(function(){
	var $tabNavItem = $('.TabNav-A').find('span');
	var $tabPane = $('.TabPane-A');
	$tabNavItem.each(function(i){
		$(this).click(function(){
			$(this).parents().addClass('hover').siblings().removeClass('hover');//为tab列表选项增加选中样式
			$tabPane.eq(i).addClass('hover').siblings().removeClass('hover');//为tab列表对应的内容增加显示隐藏样式
			return false;
		});
	});
	
});


//日期范围选择-laydate
$(function(){
	lay('.timeRange').each(function(){
	  laydate.render({
		elem: this
		,range: true
	  });
	}); 
});

//日期范围选择-laydate
$(function(){
	lay('#test').each(function(){
	  laydate.render({
		elem: this
	  });
	}); 
});



//下拉框
$(function(){
	$(".select").each(function(){
		var s=$(this);
		var z=parseInt(s.css("z-index"));
		var dt=$(this).children("dt");
		var dd=$(this).children("dd");
		var _show=function(){dd.slideDown(200);s.css("z-index",z+1);}; //展开效果
		var _hide=function(){dd.slideUp(200);s.css("z-index",z);};//关闭效果
		dt.click(function(){dd.is(":hidden")?_show():_hide();});
		dd.find("a").click(function(){dt.html($(this).html());_hide();dt.addClass("cur")});//选择效果（如需要传值，可自定义参数，在此处返回对应的“value”值 ）
		$("body").click(function(i){ !$(i.target).parents(".select").first().is(s) ? _hide():"";});
	})
})

 

/**
 * 获取下拉框的值
 * @param compId下拉框组件ID
 * @param service_name --sql配置，如果不指定则从GET_TB_PTY_CODE表中获取
 * @param flag --是否显示可选择
 * @param defaultValue 当前下拉框加载完毕后设置的值，用于修改页面
 * @param busi_type --字典表中类型
 * @param param --初始参数
 * @returns {Array}
 */
function selectComboxInfo(compId,service_name,flag,defaultValue,busi_type,param){
	var opt = [];
	if (flag) {
		opt = ["<option value=''>请选择</option>"];
	}
	if(param == null){
		param = new Object();
	}
	if(service_name!=null){
		param["service_name"]=service_name;
	}else{
		param["service_name"]="GET_TB_PTY_CODE";
	}
	param["busiType"] = busi_type;
	$.ajax({
        type: "GET",
        async: false, 
        data: param,
        dataType: "json",
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        url : nowurl+'ControlServlet.do?serviceName=commonServlet&methodName=initComboxInfo',
        success: function(data) {
        	if(data.length > 0){
    			var line = data.length;
    			for(var i=0;i<line;i++){
    				var obj = data[i];
    				opt.push("<option value='" + obj.VALUE+ "'> " + obj.TEXT + "</option>");
    			}
    		}
        	$("#" + compId).append(opt.join(''));
        	if(defaultValue!=null&&defaultValue!=""&&defaultValue!='undefined'){
    		   $("#" + compId).val(defaultValue);
        	}
        },
        beforeSend: function() {
            //$.jBox.tip("正在查询数据...", 'loading');
        }
    });
}


/**
 * ajax上传文件
 * @param url		   上传文件调用的url
 * @param elementID    html中type定义为file的input元素ID
 * @param data	       上传文件需要的额外参数  参数 json格式  {id：1，name:"adc"} 
 * @param callback	   上传成功后的回调函数
 */
function ajaxUploadFile(url,elementID,data,callback){
	var inputFile = $("#"+elementID+" > input:file")[0];
	var params = new Object();
	
	$.each(data,function(key,value){
		//数组参数，需要转成字符串
		if(isArray(value)){
			//params[key] = encodeURI(encodeURI(value));
			params[key] = encodeURI(JSON.stringify(value));
		}else{
			params[key] = value;
		}
	});
	
	$.ajaxFileUpload({
        url: url,                 										 //用于文件上传的服务器端请求地址
        fileElementId: inputFile,               						 //文件上传域的ID
        data: params,            							 			 //附加的额外参数
        dataType: 'text',                   							 //返回值类型 一般设置为json
        success: function (data, status)    							 //服务器成功响应处理函数
        {
        	if(data){
        		callback(data);
            }else{
                callback();
            }
        },
        error: function (data, status, e)   //服务器响应失败处理函数
        {
        	
        	 if(status=="error"){
                 return ;
             }
             alert("系统异常!")
        },
        complete: function () {
            var jq = $("#"+elementID+" > input:file");
            jq.before(inputFile);
            jq.remove();
        }
    });
}

//URL参数获取函数
function getParam(paramName) { 
    paramValue = "", isFound = !1; 
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) { 
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0; 
        while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++ 
    } 
    return paramValue == "" && (paramValue = null), paramValue 
} 


function isArray(o) {
    return Object.prototype.toString.call(o) === '[object Array]';
}


/**
 * 生成导出文件并下载
 * @param url
 * @param parms
 */

function DownLoad(url, parms) {
	var form = document.createElement("form");
	 form.style.display='none';;
	 form.action = url;
	 form.method="post";
	 form.target = "_blank";
	 document.body.appendChild(form);
	 for(var key in parms){
	  var input = document.createElement("input");
	  input.type = "hidden";
	  input.name = key;
	  input.value = parms[key];
	  form.appendChild(input);
	 }
	 form.submit();
}

/**
 * 获取用户权限码
 * @returns
 */
function getUserPrivilege(){
	var param = new Object();
	var privileges = "";
	$.ajax({
        type: "POST",
        async: false, 
        data: param,
        dataType: "json",
        url : nowurl+'ControlServlet.do?serviceName=commonServlet&methodName=getPrivilegeCode',
        success: function(data) {
        	privileges=data;
        },
        beforeSend: function() {
            //$.jBox.tip("正在查询数据...", 'loading');
        }

    });
	return privileges;
}


/*生成num位随机数*/
function randomNum(num) {
    var randomNum = '';
    for (var i = 0; i < num; i ++) {
        randomNum += Math.floor(Math.random()*10);
    }
    return randomNum;
}

/**
*
* @param select 控件ID
* @param service_name 服务名
* @param showAllFlag 是否显示全部
* @param busiType 如果从字典表取，指定busiType
*/
function setSingleSelData(select, service_name, showAllFlag, busiType, async) {
   if (async == undefined || async == null) {
       async = true;
   }
   var dataObj = {
       "sV1": busiType,
       "sV2": "",
       "service_name": service_name
   };
   $.ajax({
       url: nowurl + 'ControlServlet.do?serviceName=commonServlet&methodName=gridValue',
       type: 'POST',
       async: async,
       cache: false,
       data: dataObj,
       dataType: 'json',
       timeout: 60000,
       success: function (responseObj) {
           if (showAllFlag) {
               select.append("<option value='-1'>全部</option>");
           }
           var arr = responseObj;
           var html = "";
           if(arr!=undefined&&arr!=null) {
               for (var i = 0; i < arr.length; i++) {
                   html += "<option value='" + arr[i].VALUE + "'>" + arr[i].TEXT + "</option>";

               }
               select.append(html);
           }
       },
       error: function () {
           //
       }
   });
}


/**
 * 兼容ie8及以下不支持indexOf方法
 * @returns
 */
function addIndexOfToIE8(){
    if (!Array.prototype.indexOf){
        Array.prototype.indexOf = function(elt /*, from*/){
            var len = this.length >>> 0;

            var from = Number(arguments[1]) || 0;
            from = (from < 0)
                ? Math.ceil(from)
                : Math.floor(from);
            if (from < 0)
                from += len;

            for (; from < len; from++){
                if (from in this && this[from] === elt)
                    return from;
            }
            return -1;
        };
    }
}

//对字符串进行加密
function compileStr(code){ 
	var c=String.fromCharCode(code.charCodeAt(0)+code.length);
	for(var i=1;i<code.length;i++){      
		c+=String.fromCharCode(code.charCodeAt(i)+code.charCodeAt(i-1));
	}   
	return escape(c);   
}

//字符串进行解密 
function uncompileStr(code){      
	code=unescape(code); 
	if(code==null||code=="null") return null;
	var c=String.fromCharCode(code.charCodeAt(0)-code.length);      
	for(var i=1;i<code.length;i++)
	{      
		c+=String.fromCharCode(code.charCodeAt(i)-c.charCodeAt(i-1));      
	}      
	return c;   
}