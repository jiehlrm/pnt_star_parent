var point_tag = null;
var table1 = null;
var table2 = null;
var table3 = null;
var tableHead1 = [{
    name: '积分类型编码',
    id: 'CODE_PAGE'
}, {
    name: '积分类型名称',
    id: 'NAME_PAGE'
}, {
    name: '积分类型描述',
    id: 'DESC_PAGE'
}, {
    name: '状态',
    id: 'STATUS_PAGE'
}, {
    name: '创建时间',
    id: 'DATE_PAGE'
}, {
    name: '修改',
    id: 'UPDATE'
}];
var tableHead2 = [{
    name: '积分类型组名称',
    id: 'NAME_PAGE'
}, {
    name: '状态',
    id: 'STATUS_PAGE'
}, {
    name: '创建时间',
    id: 'DATE_PAGE'
},{
    name: '修改',
    id: 'UPDATE'
}];
var tableHead3 = [{
    name: '积分类型组名称',
    id: 'GROUP_NAME_PAGE'
}, {
    name: '积分类型名称',
    id: 'NAME_PAGE'
}, {
    name: '状态',
    id: 'STATUS_PAGE'
}, {
    name: '创建时间',
    id: 'DATE_PAGE'
}];
var type = 1; //区别是哪个表格
$(document).ready(function () {
    initTag();
    addEvent();
});
// 初始化tag
function initTag() {
    // 未被初始化，则需要被初始化
    if (point_tag == null) {
        point_tag = $('#pointTag').catTag({
            tags: [{
                value: '1',
                title: '积分类型',
                choose: true
            }, {
                value: '2',
                title: '积分类型组'
            }, {
                value: '3',
                title: '积分类型组成员'
            }],
            afterClick: function (p1, p2) {
                tagChoose(p1, p2);
            }
        });
    } else {
        // 已经被初始化，则需要进行刷新
        tableReload();
    }
}

// 切换标签
function tagChoose(p1, p2) {
    // 显示操作行
    $('#serviceDlg').show();
    // 如果是前两个表格则显示新增和修改按钮
    // 如果是最后一个表格则显示下拉框及查询按钮
    if (p1.value == '1' || p1.value == '2') {
        $('#serviceDlg').find('ul.new-list').hide();
        $('#serviceDlg').find('a.query_button').hide();
        $('#serviceDlg').find('a.add_button').show();
        $('#serviceDlg').find('a.update_button').show();
    } else {
        $('#serviceDlg').find('a.add_button').hide();
        $('#serviceDlg').find('a.update_button').hide();
        $('#serviceDlg').find('ul.new-list').show();
        $('#serviceDlg').find('a.query_button').show();
    }
    
    if (p1.value == '1') {// 积分类型
        type = 1;
        $('#changeForm1').show();
        $('#changeForm2').hide();
        $('#changeForm3').hide();
        // 初始化或重设表格
        var param1 = {
                'serviceName': 'pointTypeService',
                'methodName': 'queryPointType'
        };
        if (table1 == null) {
            // 表格不存在，新建
            // 置空div防止迭代
            table1 = initTable('#changeForm1', tableHead1, param1);
        } else {
            // 表格存在，重设
            // 重设param
            table1.setInitParam(param1);
            // 进行刷新
            table1.reloadTable();
        }
    } else if (p1.value == '2') {// 积分类型组
        type = 2;
        $('#changeForm2').show();
        $('#changeForm1').hide();
        $('#changeForm3').hide();
        // 初始化或重设表格
        var param2 = {
                'serviceName': 'pointTypeService',
                'methodName': 'queryPointTypeGroup'
        };
        if (table2 == null) {
            // 表格不存在，新建
            table2 = initTable('#changeForm2', tableHead2, param2);
        } else {
            // 表格存在，重设
            // 重设param
            table2.setInitParam(param2);
            // 进行刷新
            table2.reloadTable();
        }
    } else {// 积分类型组成员
        type = 3;
        // 下拉框初始化
        initSelectBox();
        $('#changeForm3').show();
        $('#changeForm1').hide();
        $('#changeForm2').hide();
        // 初始化或重设表格
        var param3 = {
                'serviceName': 'pointTypeService',
                'methodName': 'queryPointTypeGroupMbr'
        };
        if (table3 == null) {
            // 表格不存在，新建
            table3 = initTable('#changeForm3', tableHead3, param3);
        } else {
            // 表格存在，重设
            // 重设param
            table3.setInitParam(param3);
            // 进行刷新
            table3.reloadTable();
        }
    }
}

// 表格初始化
function initTable(div, tableHead, param) {
    var decorateParam = null;
    if (type == 1) {
        decorateParam = {
                'CODE_PAGE': function (row) {
                    var code_page = row['CODE_PAGE'];
                    if (code_page == undefined) {
                        code_page = '';
                    }
                    return '<div><a data-code_page="' +  code_page + '">' + code_page + '</a></div>';
                },
                'NAME_PAGE': function (row) {
                    var name_page = row['NAME_PAGE'];
                    if (name_page == undefined) {
                        name_page = '';
                    }
                    return '<div><a data-name_page="' +  name_page + '">' + name_page + '</a></div>';
                },
                'DESC_PAGE': function (row) {
                    var desc_page = row['DESC_PAGE'];
                    if (desc_page == undefined) {
                        desc_page = '';
                    }
                    return '<div><a data-desc_page="' +  desc_page + '">' + desc_page + '</a></div>';
                },
                'STATUS_PAGE': function (row) {
                    var status_page = row['STATUS_PAGE'];
                    if (status_page == undefined) {
                        status_page = '';
                    }
                    return '<div><a data-status_page="' +  status_page + '">' + status_page + '</a></div>';
                },
                'UPDATE': function (row) {
                    
                } 
        }
    } else if (type == 2) {
        decorateParam = {
                'NAME_PAGE': function (row) {
                    var name_page = row['NAME_PAGE'];
                    if (name_page == undefined) {
                        name_page = '';
                    }
                    return '<div><a data-name_page="' +  name_page + '">' + name_page + '</a></div>';
                },
                'STATUS_PAGE': function (row) {
                    var status_page = row['STATUS_PAGE'];
                    if (status_page == undefined) {
                        status_page = '';
                    }
                    return '<div><a data-status_page="' +  status_page + '">' + status_page + '</a></div>';
                }, 
                'UPDATE': function (row) {
                    
                } 
        }
    }
    return $(div).catTable({
        data: COMMON.getSyncData, //方法从mock.js中来，模拟从服务器获取数据
        thead: tableHead,
        dataFrom: 'Server-T',
        param: param,
        dataComp: ['data', 'total'],
        showFooter:true,
        decorate : decorateParam
    });
}

// 下拉框初始化
function initSelectBox() {
    var param = {
            'serviceName': 'pointTypeService',
            'methodName': 'queryPointTypeGroup'
    }
    var result = COMMON.getSyncData(param);
    var data = result.data;
    var str = '<div style="display:none;"><a class="hover" data-code="">请选择</a></div>';
    for (var i = 0; i < data.length; i++) {
        str += '<div><a data-name="' + data[i]['NAME_PAGE'] + '" data-code="' + data[i]['ID_PAGE'] + '">' + data[i]['NAME_PAGE'] + '</a></div>';
    }
    $('#point_type_group').html(str);
}

// 事件添加
function addEvent() {
    // 下拉框
    $('#point_type_group').on('click', function (e) {
        $('#point_type_group').find('a').removeClass('hover');
        $(e.target).addClass('hover');
        $('.point_type_group').find('dt').text($(e.target).text());
        $('#point_type_group').css('display', 'none');
    });
    // 查询按钮
    $('#query_button').on('click', function () {
        query();
    });
    $('#add_button').on('click', function () {
        add();
    });
    $('#update_button').on('click', function () {
        update();
    });
}

function query() {
    var pointTypeGroupId = $('#point_type_group').find('a.hover').data('code');
    if (pointTypeGroupId == undefined || pointTypeGroupId == '') {
        layer.alert('请选择积分类型组');
        return;
    }
    var param = {
            'serviceName': 'pointTypeService',
            'methodName': 'queryPointTypeGroupMbr',
            'pointTypeGroupId': pointTypeGroupId
    }
    if (table3 == null) {
        // 表格不存在，新建
        table3 = initTable('#changeForm', tableHead3, param3);
    } else {
        // 表格存在，重设
        // 重设param
        table3.setInitParam(param);
        // 进行刷新
        table3.reloadTable();
    }
}

function add() {
    var str = '';
    var array = [
        {
            name: '有效',
            code: '1000'
        },
        {
            name: '无效',
            code: '1100'
        },
        {
            name: '未生效',
            code: '1200'
        }
    ];
    //对应的状态class设为hover
    for (var i=0; i <array.length;i++) {
        var name = array[i]['name'];
        var code = array[i]['code'];
        if (i == 0) {
            str += '<a class="hover" data-code="' + code + '">' + name + '</a>'
        } else {
            str += '<a data-code="' + code + '">' + name + '</a>'
        }
    }
    var html1 =    '<div class="cust-high-alert-list dw-rel">                                                             	'+
    '            <dl>                                                                                                    	'+
    '                <dt class="h42">积分类型编码：</dt>                                                                 	'+
    '                <dd id="cust_name" class="mt5">                                                                     	'+
    '                   <input style="height:10px;width:150px;" class="lina-inp" id="point_type_code"></input>              '+
    '                </dd>                                                                                               	'+
    '                <div class="clear"></div>                                                                           	'+
    '            </dl>                                                                                                   	'+
    '            <dl>                                                                                                    	'+
    '                <dt class="h42">积分类型名称：</dt>                                                                 	'+
    '                <dd id="cust_name" class="mt5">                                                                     	'+
    '                   <input style="height:10px;width:150px;" class="lina-inp" id="point_type_name"></input>              '+
    '                </dd>                                                                                               	'+
    '                <div class="clear"></div>                                                                           	'+
    '            </dl>                                                                                                   	'+
    '            <dl>                                                                                                    	'+
    '                <dt class="h42">积分类型描述：</dt>                                                                 	'+
    '                <dd id="cust_name" class="mt5">                                                                     	'+
    '                   <input style="height:10px;width:150px;" class="lina-inp" id="point_type_desc"></input>              '+
    '                </dd>                                                                                               	'+
    '                <div class="clear"></div>                                                                           	'+
    '            </dl>                                                                                                   	'+
    '            <dl>                                                                                                       '+
    '                <dt class="h42">状态：</dt>                                                                         	'+
    '                <dd id="point_type_status" class="mt5">                                                                '+
                            str                                                                                              +
    '                </dd>                                                                                               	'+
    '                <div class="clear"></div>                                                                           	'+
    '            </dl>                                                                                                   	'+
    '            <dl class="h42" style="text-align:center;">                                                             	'+
    '                <dd style="margin-left: auto;margin-top:5px;margin-right:auto;width:100%">                          	'+
    '                    <a href="javascript:void(0);" class="blueBtn w70" style="position: relative;" id="save">保存</a>	'+
    '                </dd>                                                                                               	'+
    '                <div class="clear"></div>                                                                           	'+
    '            </dl>                                                                                                   	'+
    '        </div>';
    var html2 =   '<div class="cust-high-alert-list dw-rel">                                                                '+
    '            <dl>                                                                                                       '+
    '                <dt class="h42">积分类型组名称：</dt>                                                                   '+
    '                <dd id="cust_name" class="mt5">                                                                        '+
    '                   <input style="height:10px;width:150px;" class="lina-inp" id="point_type_group_name"></input>              '+
    '                </dd>                                                                                                  '+
    '                <div class="clear"></div>                                                                              '+
    '            </dl>                                                                                                      '+
    '            <dl>                                                                                                       '+
    '                <dt class="h42">状态：</dt>                                                                           '+
    '                <dd id="point_type_group_status" class="mt5">                                                                '+
                            str                                                                                              +
    '                </dd>                                                                                                  '+
    '                <div class="clear"></div>                                                                              '+
    '            </dl>                                                                                                      '+
    '            <dl class="h42" style="text-align:center;">                                                                '+
    '                <dd style="margin-left: auto;margin-top:5px;margin-right:auto;width:100%">                             '+
    '                    <a href="javascript:void(0);" class="blueBtn w70" style="position: relative;" id="save">保存</a> '+
    '                </dd>                                                                                                  '+
    '                <div class="clear"></div>                                                                              '+
    '            </dl>                                                                                                      '+
    '        </div>';;
    if (type == 1) {
        html = html1;
    } else {
        html = html2;
    }
    // 弹出框
    var dialog = art
            .dialog({
                title : '<div style="width:600px;"><div class="pointQuery_title" style="margin: 0px;"><span>月初账单明细</span><span style="float:right;"><a class="aui_close">×</a></span></div></div>',
                cancel : false,
                width : '600px',
                height : '400px',
                content : html,
                lock : true,
                drag : false, // 禁止弹出层拖动
                resize : false
            // 禁止放大缩小
            });
    // 弹出框关闭按钮事件添加
    $('.aui_close').click(function() {
        dialog.close();
    });
    // 弹出框状态事件添加
    $('#point_type_status').on('click', 'a', function() {
        if (!$(this).hasClass('hover')) {
            $('#point_type_status').find('a').removeClass('hover');
            $(this).addClass('hover')
        }
    });
    // 弹出框保存按钮事件添加
    $('#save').click(function() {
        var param = null;
        if (type == 1) {
            var point_type_code = $('#point_type_code').val();
            var point_type_name = $('#point_type_name').val();
            var point_type_desc = $('#point_type_desc').val();
            var point_type_status = $('#point_type_status').find('.hover').data('code');
            // 组装参数
            param = {
                'serviceName' : 'pointTypeService',
                'methodName' : 'addPointType',
                'POINT_TYPE_CODE' : point_type_code,
                'POINT_TYPE_NAME' : point_type_name,
                'POINT_TYPE_DESC' : point_type_desc,
                'POINT_TYPE_STATUS' : point_type_status
            };
        } else {
            var point_type_group_name = $('#point_type_group_name').val();
            var point_type_group_status = $('#point_type_group_status').find('.hover').data('code');
            // 组装参数
            param = {
                'serviceName' : 'pointTypeService',
                'methodName' : 'addPointTypeGroup',
                'POINT_TYPE_GROUP_NAME' : point_type_group_name,
                'POINT_TYPE_GROUP_STATUS' : point_type_group_status
            };
        }
        
        var data = COMMON.getSyncData(param);
        // 关闭弹出框
        dialog.close();
        // 表格刷新
        if (type == 1) {
            table1.reloadTable();
        } else {
            table2.reloadTable();
        }
        
    });
}
function update() {
    
}