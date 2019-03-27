var cust_num = '';
var tableHead = ['名称', '积分对象', '调整积分值', '操作人', '调整原因', '操作时间'];
$(document).ready(function () {
    initSearch();
    initForm();
});
/**
 * 初始化查询
 */
function initSearch() {
    lay('#dateFrom').each(function () {
        laydate.render({
            elem: this
        });
    });
    lay('#dateTo').each(function () {
        laydate.render({
            elem: this
        });
    });
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
    var cat = $('#pointForm').catTable({
        data: getData(),//方法从mock.js中来，模拟从服务器获取数据
        thead: tableHead,
        dataFrom:'Local',
        param:{
          'a':1,
          'b':2
         },
        total:100
      });
    return cat;
}
function getData() {
    var data = [];
    return data;
}
/**
 * 获取搜索的参数值
 */
function getSearchParam() {
    var lan_id = $('#lan_search').find('.hover').data('value');
    var cust_num = $('#cust_num').val().trim();
}