var cust_num = '';
var tableHead = [{display1:'地市', id:'a'}, {display1:'积分兑换人次', id:'b'}, {display1:'积分兑换量（分）', id:'c'}, {display1:'其中：自有产品兑换', id:'d', children:[{display1:'人次', id:'d1'}, {display1:'兑换量', id:'d2'}]}, {display1:'其中：礼品兑换', id:'e', children:[{display1:'人次', id:'e1'}, {display1:'兑换量', id:'e2'}]}];
$(document).ready(function () {
    initSearch();
    initForm();
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
    var cat = $('#pointForm').catTable({
        data: getData(),//方法从mock.js中来，模拟从服务器获取数据
        thead: tableHead,
        thField: ['display1', 'id'],
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