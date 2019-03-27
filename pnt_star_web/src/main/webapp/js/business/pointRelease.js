/**
 * 积分解冻 autor:唐羽
 *--GLOBAL VARIABLE
 *  |- cust_id [客户标识]
 *  |- cust_info [客户信息]
 * 
 *--FUNCTION DESCRIPTION [方法描述以及部分层级关系]
 *  |- $() [初始方法入口] 
 *     |- initSearch [初始化查询]
 *        |- initLan [初始化本地网查询条件]
 *        |- addEvent [页面事件的添加]
 *           |- showCustPoint [点击放大镜图标进行查询事件]
 *              |- custForm [客户基本信息表单的初始化]
 *              |- initTag [tag的初始化]
 *                 |- tagChoose [tag切换的事件触发]
 *                    |- releaseCust [解冻操作]
 *                       |- isNeedRelease [校验是否需要解冻]
 */
var cust_id = ''; //全局cust_id，代表客户id
var cust_info = '';
$(document).ready(function () {
    //初始化查询
    initSearch();
});
/**
 * 初始化查询
 */
function initSearch() {
    //初始化本地网
    initLan();
    //页面事件添加
    addEvent();
}
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
 * 页面事件添加
 */
function addEvent() {
     //查询事件添加
     $('#doQuery').on('click', function () {
        showCustPoint();
    });
    //重置按钮
    $('#resetQuery').on('click', function () {
        $('.lan-select').find('dt').text('请选择');
        $('.query-object').find('dt').text('请选择');
        $('#cust_num').val('');
    });
}
/**
 * 进行客户用户积分的查询
 */
function showCustPoint() {
    //0.先判断是那种类型
    var typeText = $('.query-object').find('dt').text().trim();
    var type = '1';
    if (typeText == '用户号码') {
        type = '1';
    } else if (typeText == '客户编号') {
        type = '2';
    } else {
        layer.alert('请选择查询对象');
        return;
    }
    //1.先判断acct_num是否存在
    var acct_num = $('#cust_num').val().trim();
    if (acct_num == '') {
        layer.msg(type == '1' ? '用户号码必须填写' : '客户编号必须填写');
        return;
    }
    //2.初始化表单
    custForm();
    //3.初始化两个tag
    initTag();
}
/**
 * 初始化客户的积分表单
 */
function custForm() {
    //0.先判断是那种类型
    var latn_id = $('#lan_search').find('.hover').data('code');
    var typeText = $('.query-object').find('dt').text().trim();
    var type = '1';
    if (typeText == '用户号码') {
        type = '1';
    } else if (typeText == '客户编号') {
        type = '2';
    } else {
        layer.alert('请选择查询对象');
        return;
    }
    var param = {
        'serviceName': 'pointQueryService',
        'methodName': 'getCustFormData',
        'LATN_ID': latn_id,
        'ACCT_NUM': $('#cust_num').val().trim(),
        'TYPE': type
    }
    var data = COMMON.getSyncData(param);
    if (data.length == 0) {
        layer.msg('查无此人信息');
        return;
    }
    var form = data[0];
    for (var o in form) {
        $('.' + o).text(form[o]);
    }
    cust_id = form['CUST_ID'];
}
/**
 * 初始化tag
 */
function initTag() {
    $('#pointTag').empty();
    //首先查看是否该用户被冻结了
    var param = {
        'serviceName': 'pointReleaseWebService',
        'methodName': 'getCustStatus',
        'CUST_ID': cust_id
    }
    var data = COMMON.getSyncData(param);
    if (data.length == 0) {
        layer.msg("该用户未被冻结，暂不需要进行解冻。");
        return;
    }
    cust_info = data[0];
    $('#pointTag').catTag({
        tags: [{
            value: '1',
            title: '积分解冻',
            choose: true
        }],
        afterClick: function (p1, p2) {
            tagChoose(p1, p2);
        }
    });

}
/**
 * 初始化事件
 * @param {*} p1 
 * @param {*} p2 
 */
function tagChoose(p1, p2) {
    if (cust_info == '') {
        layer.msg("该用户未被冻结，暂不需要进行解冻。");
        return;
    }
    $(p2).html('<div id="pointForm">' +
        '<div class="bgBorder mt10 pr50">' +
        '<ul class="new-list mt10">' +
        '<li><label class="w120">本期冻结积分：</label><span class="FREEZE_POINT_BALANCE"></span></li>' +
        '<li style="visibility: hidden;"><label class="w120"></label><span class=""></span></li>' +
        '<li style="width:100%;"><label class="w120">冻结原因：</label><span class="FREEZE_REASON" style="width: calc(100% - 120px);display: inline-block;"></span></li>' +
        '<div class="clear"></div>' +
        '</ul>' +
        '</div>' +
        '</div>');

    for (var i in cust_info) {
        $(p2).find('.' + i).text(cust_info[i]);
    }
    $(p2).append('<div style="padding: 8px;"><div class="search-list dw-rel">' +
        '<dl>' +
        '<dt style="height: 150px;">原因描述：</dt>' +
        '<dd id="reason_description">' +
        '<textarea style="height:120px;margin-top:10px;" class="lina-inp col-5"></textarea>' +
        '</dd>' +
        '<div class="clear"></div>' +
        '</dl>' +
        '<dl class="h42" style="text-align:center;">' +
        '<dd style="margin-left: auto;margin-top:5px;margin-right:auto;width:100%">' +
        '<a href="javascript:void(0);" class="blueBtn w70" style="position: relative;" id="releaseCust">解冻</a>' +
        '</dd>' +
        '<div class="clear"></div>' +
        '</dl>' +
        '</div></div>');
    $('#releaseCust').on('click',function(){
        releaseCust();
    });

}
/**
 * 解冻
 */
function releaseCust(){

    //1.检查是否需要进行解冻
    if(!isNeedRelease()){
        return;
    }
    var freeze_reason = $('#reason_description').val().trim();
    if(freeze_reason.length>50){
        layer.msg("原因应在50字以内。");
        return;
    }
    var param = {
        'CUST_ID': cust_id,
        'FREEZE_REASON': freeze_reason,
        'serviceName': 'pointReleaseWebService',
        'methodName': 'changeCustAcctStatus',
    }
    //后台接口的调用
    var callback = function () {
        //1.提示修改成功
        layer.msg("客户解冻成功");
    }
    COMMON.doSync(param, callback);
}

function isNeedRelease(){
    var param = {
        'serviceName': 'pointReleaseWebService',
        'methodName': 'getCustStatus',
        'CUST_ID': cust_id
    }
    var data = COMMON.getSyncData(param);
    if (data.length == 0) {
        layer.msg("该用户未被冻结，暂不需要进行解冻。");
        return false;
    }
    return true;
}