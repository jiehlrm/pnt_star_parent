$(function(){	
	//01复选组-全选/反选
	$("#CheckAll").click(function() { 
		var flag = $(this).attr("checked"); 
		$("[name=checkbox]").each(function() { 
		$(this).attr("checked",flag); 
		});
	});
	//01上传附件
	$("#file").change(function(){  // 当id为file的对象发生变化时
         $("#input").val($("#file").val());  //将#file的值赋给#input 
	})
	//02表格隔行换色
	$(".tableBox tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
	$(this).addClass("over");}).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
	$(this).removeClass("over");}) //移除该行的class
	$(".tableBox tr:even").addClass("alt"); //给class为stripe_tb的表格的偶数行添加class值为alt
})

//01单选组
$(function(){
	$('.label-radio').click(function(){ setupRadio(); });
	});//点击label-radio时执行以下事件setupRadio()
function setupRadio(){
	if($('.label-radio input').length) { 
	$('.label-radio').each(function(){ $(this).removeClass('radio-checked'); }); //移除.radio-checked的选中样式
	$('.label-radio input:checked').each(function(){ $(this).parent('label').addClass('radio-checked'); }); };//选中单选选项时给其父元素.label-radio增加选中样式
	};
	
//01复选组
$(function(){
	$('.label-check').click(function(){ setupCheck(); });
	});//点击label-check时执行以下事件setupCheck()
function setupCheck(){
	if($('.label-check input').length) {
	$('.label-check').each(function(){ $(this).removeClass('check-checked'); });//移除.label-check的选中样式
	$('.label-check input:checked').each(function(){ $(this).parent('label').addClass('check-checked'); }); };//选中复选选项时给其父元素.label-check增加选中样式
};

//展开收起
$(function(){
	$(".moreOpen").click(function(){
		if($(this).html() == "&nbsp;&nbsp;&nbsp;&nbsp;"){
			$(this).html("&nbsp;&nbsp;&nbsp;&nbsp;");
		}else{
			$(this).html("&nbsp;&nbsp;&nbsp;&nbsp;");
		};//文字改变
		$(this).toggleClass("moreClose");//箭头切换
		$(this).parents().next(".hideDiv").slideToggle(300);//收起展开
		});
	  //执行展开隐藏效果时判断控制左右高度一致
	  $(".leftnav").height("");
      $(".rightmain").height("");
      var hl = $(".leftnav").outerHeight(); //获取左侧left层的高度 
      var hr = $(".rightmain").outerHeight(); //获取右侧right层的高度  
      var mh = Math.max(hl,hr); //比较hl与hr的高度，并将最大值赋给变量mh
      $(".leftnav").height(mh); //将left层高度设为最大高度mh  
      $(".rightmain").height(mh); //将right层高度设为最大高度
})
