var account_period = null;
var head = [ {
    display1 : '本地网',
    id : 'LATNNAME'
}, {
    display1 : '本月统计',
    id : 'MONTH',
    children : [ {
        display1 : '七星',
        id : 'SEV',
        children : [ {
            display1 : '定级成长',
            id : 'PSEV'
        }, {
            display1 : '直享星级',
            id : 'TSEV'
        }, {
            display1 : '智能光宽直享',
            id : 'GSEV'
        }, {
            display1 : '关键人直享',
            id : 'KSEV'
        }, {
            display1 : '合计',
            id : 'SEVSUM'
        } ]
    }, {
        display1 : '六星',
        id : 'SIX',
        children : [ {
            display1 : '定级成长',
            id : 'PSIX'
        }, {
            display1 : '直享星级',
            id : 'TSIX'
        }, {
            display1 : '智能光宽直享',
            id : 'GSIX'
        }, {
            display1 : '关键人直享',
            id : 'KSIX'
        }, {
            display1 : '合计',
            id : 'SIXSUM'
        } ]
    }, {
        display1 : '五星',
        id : 'FIV',
        children : [ {
            display1 : '定级成长',
            id : 'PFIV'
        }, {
            display1 : '直享星级',
            id : 'TFIV'
        }, {
            display1 : '智能光宽直享',
            id : 'GFIV'
        }, {
            display1 : '关键人直享',
            id : 'KFIV'
        }, {
            display1 : '合计',
            id : 'FIVSUM'
        } ]
    }, {
        display1 : '四星',
        id : 'FOU',
        children : [ {
            display1 : '定级成长',
            id : 'PFOU'
        }, {
            display1 : '智能光宽直享',
            id : 'GFOU'
        }, {
            display1 : '关键人直享',
            id : 'KFOU'
        }, {
            display1 : '合计',
            id : 'FOUSUM'
        } ]
    }, {
        display1 : '三星',
        id : 'THR',
        children : [ {
            display1 : '定级成长',
            id : 'PTHR'
        }, {
            display1 : '智能光宽直享',
            id : 'GTHR'
        }, {
            display1 : '关键人直享',
            id : 'KTHR'
        }, {
            display1 : '合计',
            id : 'THRSUM'
        } ]
    }, {
        display1 : '二星',
        id : 'TWO',
        children : [ {
            display1 : '定级成长',
            id : 'PTWO'
        }, {
            display1 : '智能光宽直享',
            id : 'GTWO'
        }, {
            display1 : '关键人直享',
            id : 'KTWO'
        }, {
            display1 : '合计',
            id : 'TWOSUM'
        } ]
    }, {
        display1 : '一星',
        id : 'ONE',
        children : [ {
            display1 : '定级成长',
            id : 'PONE'
        }, {
            display1 : '智能光宽直享',
            id : 'GONE'
        }, {
            display1 : '关键人直享',
            id : 'KONE'
        }, {
            display1 : '合计',
            id : 'ONESUM'
        } ]
    }, {
        display1 : '总合计',
        id : 'SUM',
        children : [ {
            display1 : '定级成长',
            id : 'PSUM'
        }, {
            display1 : '直享星级',
            id : 'TSUM'
        }, {
            display1 : '智能光宽直享',
            id : 'GSUM'
        }, {
            display1 : '关键人直享',
            id : 'KSUM'
        }, {
            display1 : '合计',
            id : 'SUMY'
        } ]
    } ]
}, {
    display1 : '与上月差额',
    id : 'DIFFERENCE',
    children : [ {
        display1 : '七星',
        id : 'SEV2',
        children : [ {
            display1 : '本月新增',
            id : 'XSEV'
        }, {
            display1 : '本月失效',
            id : 'SSEV'
        } ]
    }, {
        display1 : '六星',
        id : 'SIX2',
        children : [ {
            display1 : '本月新增',
            id : 'XSIX'
        }, {
            display1 : '本月失效',
            id : 'SSIX'
        } ]
    }, {
        display1 : '五星',
        id : 'FIV2',
        children : [ {
            display1 : '本月新增',
            id : 'XFIV'
        }, {
            display1 : '本月失效',
            id : 'SFIV'
        } ]
    }, {
        display1 : '四星',
        id : 'FOU2',
        children : [ {
            display1 : '本月新增',
            id : 'XFOU'
        }, {
            display1 : '本月失效',
            id : 'SFOU'
        } ]
    }, {
        display1 : '三星',
        id : 'THR2',
        children : [ {
            display1 : '本月新增',
            id : 'XTHR'
        }, {
            display1 : '本月失效',
            id : 'STHR'
        } ]
    }, {
        display1 : '二星',
        id : 'TWO2',
        children : [ {
            display1 : '本月新增',
            id : 'XTWO'
        }, {
            display1 : '本月失效',
            id : 'STWO'
        } ]
    }, {
        display1 : '一星',
        id : 'ONE2',
        children : [ {
            display1 : '本月新增',
            id : 'XONE'
        }, {
            display1 : '本月失效',
            id : 'SONE'
        } ]
    }, {
        display1 : '合计',
        id : 'SUMS'
    } ]
} ];
$(document).ready(function() {
    var param = {
        'field' : [ 'display1', 'id' ],
        'style' : {
            style : 'min-width:150px'
        },
        'comp' : [ 'data' ],
        'footer' : false,
        'head' : head,
        'events' : [ 'initLan', 'doQueryEvent', 'exportFormEvent' ],
        'checkParam' : [ 'account_period' ],
        'dataParam' : {
            'serviceName' : 'starStatReportWebService',
            'methodName' : 'getStarStatReport',
            'ACCOUNT_PERIOD' : account_period
        },
        'exportParam' : {
            'serviceName' : 'starStatReportWebService',
            'methodName' : 'exportForm',
            'ACCOUNT_PERIOD' : account_period,
            'TABLE_TITLE' : JSON.stringify(head)
        }
    }
    common(param);
});