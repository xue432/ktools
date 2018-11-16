/**
 * 工具
 * @type {{alert: ktUtils.alert, initFileUploadControl: (function(*, *=, *=, *=): (jQuery|HTMLElement))}}
 */
var ktUtils = {
    /**
     * 信息提示框（注意：使用它后面需要加 return false; 否则无效）
     * @param elementId 需要在下面弹出的那个元素ID
     * @param message 提示内容
     */
    alert: function (elementId, message) {
        var alertId = "alert_" + elementId;
        var alertEle = $("#" + alertId);
        var tarEle = $('#' + elementId);

        if (alertEle.length === 0) {
            tarEle.after(
                '<div id="' + alertId + '" class="alert alert-info fade show" style="z-index: 1; position: absolute;">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>小提示：</strong>' + message +
                '</div>'
            );

            // 注册点击任意位置移除提示框事件
            $("body").on("click", function () {
                var alertAfterEle = $("#" + alertId);
                if (alertAfterEle.length !== 0) {
                    alertAfterEle.remove();
                }
            });
        }
    },
    hintMsg: function (msg) {
        this.showMsg('hint', msg);
    },
    warnMsg: function (msg) {
        this.showMsg('warning', msg);
    },
    errorMsg: function (msg) {
        this.showMsg('danger', msg);
    },
    showMsg: function (type, msg) {
        var title;
        var cl;
        if (!msg) {
            msg = '..........';
        }
        if (type === 'warning') {
            title = '警告：';
            cl = 'alert-warning';
        } else if (type === 'danger') {
            title = '错误：';
            cl = 'alert-danger';
        } else {
            title = '提示：';
            cl = 'alert-info';
        }
        var alertEle = $('.alert');
        var bodyEle = $('body');
        if (alertEle.length === 0) {
            bodyEle.append(
                '<div class="alert '+cl+' fade show" style="z-index: 888; position: fixed;top:45%;left:41%;">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>'+title+'</strong>' + msg +
                '</div>'
            );

            // 注册点击任意位置移除提示框事件
            bodyEle.on("click", function () {
                var alertAfterEle = $('.alert');
                if (alertAfterEle.length !== 0) {
                    alertAfterEle.remove();
                }
            });
        }
    },
    modalMsg: function (message) {
        var modalEle = $('#myModal');
        modalEle.find('.modal-body').text(message);
        modalEle.modal('show');
    },
    initFileUploadControl: function(id, uploadUrl, allowFile, params, customParams) {
        var control = $('#' + id);
        if (!allowFile) {
            allowFile = ['jpg', 'png', 'jpeg']
        }
        if (!params) {
            params = {};
        }
        var allParams = {
            language: 'zh', // 设置语言
            uploadUrl: uploadUrl, // 上传的地址
            enctype: 'multipart/form-data',
            allowedFileExtensions: allowFile,// 接收的文件后缀
            showUpload: false, // 是否显示上传按钮
            showCaption: false,// 是否显示标题
            showRemove: false,
            browseClass: "btn btn-primary", // 按钮样式
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            autoReplace: true,
            uploadAsync: true,
            //dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            maxFileSize: 10240, //单位为kb，如果为0表示不限制文件大小
            minFileCount: 1,
            maxFileCount: 1, //表示允许同时上传的最大文件个数
            validateInitialCount: true,
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            uploadExtraData: function () {
                return params;
            }, // 向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
            layoutTemplates: {
                // actionDelete:'', //去除上传预览的缩略图中的删除图标
                actionUpload: '',//去除上传预览缩略图中的上传图片；
                actionZoom: ''   //去除上传预览缩略图中的查看详情预览的缩略图标。
            }
        };
        if (customParams) {
            var keys = Object.keys(customParams);
            keys.forEach(function (key) {
                allParams[key] = customParams[key]
            })
        }
        control.fileinput(allParams);
        return control;
    },
    /**
     * 渲染image tab页签
     * @param key 用于区分哪个页签(1,2,3,4,5...)
     * @param navTabType 页签类型(image,txt,video...)
     */
    renderNavTab: function (key, navTabType) {
        var tabArr;
        if (navTabType === 'image') {
            tabArr = ktConfig.navTabs.image;
        } else if (navTabType === 'txt') {
            tabArr = ktConfig.navTabs.txt;
        } else if (navTabType === 'video') {

        }
        if (!tabArr || !key) {
            error('renderNavTab error: tabArr or key must be not null');
            return;
        }
        tabArr.forEach(function (obj) {
            if (obj.key === key) {
                $('.nav-tabs').append('<li class="nav-item">\n' +
                    '            <a class="nav-link kt-fs13 active" href="' + ktcfg.ctx + obj.url + '">' + obj.name + '</a>\n' +
                    '        </li>');
            } else {
                $('.nav-tabs').append('<li class="nav-item">\n' +
                    '            <a class="nav-link kt-fs13" href="'+ktcfg.ctx + obj.url+'">'+obj.name+'</a>\n' +
                    '        </li>');
            }
        });
    },
    /**
     * 图片容器自适应(只针对带‘fakeimg’class的img标签有效)
     * @param src 图片相对地址
     */
    fixedImg: function (src) {
        var fkiEle = $('.fakeimg');
        if (!src) {
            return;
        }
        var img = new Image();
        img.src = src;
        var imgW = img.width;
        var imgH = img.height;
        var divH = fkiEle.parent().parent().height();
        var divW = fkiEle.parent().parent().width();
        var scaleDivHw= divH / divW;    // 高/宽  比例
        img.onload = function () {
            imgW = img.width;
            imgH = img.height;
            var scaleImgHw = imgH / imgW;
            if ((divH - imgH) >= 0 && (divW - imgW) >= 0) { // img宽高都小于外容器
                fkiEle.attr('style', 'width:auto;height:auto;');
            } else if ((divH - imgH) >= 0 && (divW - imgW) < 0) {   // img高小于外容器，宽大于外容器
                fkiEle.attr('style', 'height:auto;width:' + divW + 'px;');
            } else if ((divH - imgH) < 0 && (divW - imgW) >= 0) {   // img高大于外容器，宽小于外容器
                fkiEle.attr('style', 'width:auto;height:' + divH + 'px;');
            } else {    // img宽高都大于外容器
                if (scaleImgHw > scaleDivHw) {
                    fkiEle.attr('style', 'height:' + divH + 'px;width:auto');
                } else {
                    fkiEle.attr('style', 'height:' + divH + 'px;width:' + divW + 'px;');
                }
            }
        };
    }
};

var log = function () {
    if (ktConfig.logEnable) {
        var len = arguments.length;
        if (len === 1) {
            console.log(arguments[0]);
        }
        if (len === 2) {
            console.log(arguments[0], arguments[1]);
        }
    }
};

var logv = function (val) {
    if (ktConfig.logEnable) {
        console.log('val=', val);
    }
};

var error = function () {
    var len = arguments.length;
    if (len === 1) {
        console.error(arguments[0]);
    }
    if (len === 2) {
        console.error(arguments[0], arguments[1]);
    }
};

/**
 * 进度条定时器
 */
var itl;
function interval() {
    var pro = 0;
    var step = 0.5;
    itl = setInterval(function () {
        pro += step;
        var pbaEle = $('#pba');
        pbaEle.attr('style', 'width:' + pro + '%');
        pbaEle.text(pro.toFixed(1) + '%');
        if (pro >= 80) {    // 到90%放慢进度
            step = 0.1;
        }
        if (pro >= 99.8) {    // 如果大于或等于99%就停止定时器
            clearInterval(itl);
        }
    }, 10);
}

/**
 * 进度条开始
 */
function showLoading(){
    var ele = $('#pbg');
    ele.html('<div class="progress">\n' +
        '                                    <div id="pba" class="progress-bar progress-bar-striped progress-bar-animated" style="width:40%">0%</div>\n' +
        '                                </div>');
    interval();
}

/**
 * 进度条结束
 * @param status 状态码(200, 400, 333)
 */
function finishLoading(status){
    var pbaEle = $('#pba');
    clearInterval(itl);
    if (status === 200) {
        pbaEle.attr('style', 'width:100%');
        pbaEle.text('完成');
        pbaEle.addClass('bg-success');
    } else {
        pbaEle.addClass('bg-danger');
        pbaEle.text('转化失败');
    }
    pbaEle.removeClass('progress-bar-striped');
    pbaEle.removeClass('progress-bar-animated');
}