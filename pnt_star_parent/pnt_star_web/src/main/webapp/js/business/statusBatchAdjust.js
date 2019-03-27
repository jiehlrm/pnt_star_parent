var cust_num = '';
$(document).ready(function () {
    initSearch();
    initButton();
});
/**
 * 初始化查询
 */
function initSearch() {

}

function initLan() {
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getLan'
    }
    var data = COMMON.getSyncData(param);

    $('#lan_search').html();
}
/**
 * 页面事件添加
 */
function addEvent(){

}
/**
 * 初始化表单
 */
function initForm() {

}
/**
 * 获取搜索的参数值
 */
function getSearchParam() {
    var lan_id = $('#lan_search').find('.hover').data('value');
    var cust_num = $('#cust_num').val().trim();
}

/**
 * 初始化上传和提交按钮
 */
function initButton() {
    /**
     * input框内容的显示
     */
    $("#file").on(
            'input',
            function() { // 当id为file的对象发生变化时
                $("#input").val(
                        $("#file")[0].files[0] == undefined ? ''
                                : $("#file")[0].files[0].name); // 将#file的值赋给#input
                /*$('#resutlMsg').html('');*/
            });
    /**
     * 提交上传附件
     */
    $('#sumbitFile').on('click', function() {
        sumbitFile();
    });
}
/**
 * 开始导入
 */
function sumbitFile() {
    
}