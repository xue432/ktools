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
    showMsg: function (message) {
        var alertEle = $('.alert');
        var bodyEle = $('body');
        if (alertEle.length === 0) {
            bodyEle.append(
                '<div class="alert alert-info fade show" style="z-index: 888; position: fixed;top:49%;left:45%;">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>提示：</strong>' + message +
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


function initLoading(){
    $("body").append('<!-- 模态框 -->\n' +
        '    <div class="modal fade" id="myModal" >\n' +
        '        <div class="modal-dialog">\n' +
        '            <div class="modal-content">\n' +
        '                <!-- 模态框头部 -->\n' +
        '                <div class="modal-header">\n' +
        '                    <h4 class="modal-title">提示</h4>\n' +
        '                </div>\n' +
        '                <!-- 模态框主体 -->\n' +
        '                <div class="modal-body">\n' +
        '                    <p>K君正在为您努力转化中，请耐心等候哟...</p>\n' +
        '                    <div class="progress">\n' +
        '                        <div class="progress-bar progress-bar-striped progress-bar-animated" style="width:0"></div>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <!-- 模态框底部 -->\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>');
}

function showLoading(){
    $("body").append('<!-- 模态框 -->\n' +
        '    <div class="modal fade" style="top:20%;" id="myModal" >\n' +
        '        <div class="modal-dialog">\n' +
        '            <div class="modal-content">\n' +
        '                <!-- 模态框头部 -->\n' +
        '                <div class="modal-header">\n' +
        '                    <h4 class="modal-title">提示</h4>\n' +
        '                </div>\n' +
        '                <!-- 模态框主体 -->\n' +
        '                <div class="modal-body">\n' +
        '                    <p>K君正在为您努力转化中，请耐心等候哟~^_^~</p>\n' +
        '                    <div class="progress">\n' +
        '                        <div id="lpg" class="progress-bar progress-bar-striped progress-bar-animated" style="width:0"></div>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <!-- 模态框底部 -->\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>');
    $('#myModal').modal({backdrop:"static"}); // --设置为static后可以防止不小心点击其他区域是弹出框消失
    interval();
}

var itl;
function interval() {
    var pro = 0;
    var step = 0.5;
    itl = setInterval(function () {
        pro += step;
        var pgbEle = $('#lpg');
        pgbEle.attr('style', 'width:' + pro + '%');
        if (pro >= 100) {
            pro = 0;
            // clearInterval(interval);
        }
    }, 10);
}

function hideLoading(){
    $('#lpg').attr('style', 'width:100%');
    clearInterval(itl);
    $("#myModal").modal("hide");
}