//高额预警弹框HTML代码
function addHdczHtml(cust_name, cust_number, amount_month, point_limit) {
    //根据弹框前的核对状态初始化弹框内容
    var str = '';
    var array = [
        {
            name: '可查可兑',
            code: '11'
        },
        {
            name: '可查禁兑',
            code: '12'
        },
        {
            name: '禁查禁兑',
            code: '13'
        }
    ];
    //对应的状态class设为hover
    for (var i=0; i <array.length;i++) {
        var name = array[i]['name'];
        var code = array[i]['code'];
        if (code == point_limit) {
            str += '<a class="hover" data-code="' + code + '">' + name + '</a>'
        } else {
            str += '<a data-code="' + code + '">' + name + '</a>'
        }
    }
    //弹框html代码
    var hdczHtml = 
        '<div class="cust-high-alert-list dw-rel">                                                                                          '+
        '            <dl>                                                                                                          '+
        '                <dt class="h42">客户名称：</dt>                                                                          ' +
        '                <dd id="cust_name" class="mt5">                                                                       '+
        cust_name +
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+
        '            <dl>                                                                                                          '+
        '                <dt class="h42">客户编码：</dt>                                                                          ' +
        '                <dd id="cust_number" class="mt5">                                                                       '+
        cust_number + 
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+
        '            <dl>                                                                                                          '+
        '                <dt class="h42">月消费积分：</dt>                                                                          ' +
        '                <dd id="amount_month" class="mt5">                                                                       '+
        amount_month + 
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+        
        '            <dl>                                                                                                          '+
        '                <dt class="h42">核对状态：</dt>                                                                          ' +
        '                <dd id="point_limit_alert" class="mt5">                                                                       '+
        str +
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+
        '            <dl>                                                                                                          '+
        '                <dt style="height: 150px;">核对情况说明：</dt>                                                               ' +
        '                <dd>                                                                              '+
        '                    <textarea style="height:120px;width:350px;margin-top:10px;" class="lina-inp" id="point_limit_reason"></textarea>                    '+
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+
        '            <dl class="h42" style="text-align:center;">                                                                   '+
        '                <dd style="margin-left: auto;margin-top:5px;margin-right:auto;width:100%">                                '+
        '                    <a href="javascript:void(0);" class="blueBtn w70" style="position: relative;" id="save">保存</a>'+
        '                </dd>                                                                                                     '+
        '                <div class="clear"></div>                                                                                 '+
        '            </dl>                                                                                                         '+
        '        </div>                                                                                                            ';
    return hdczHtml;
}