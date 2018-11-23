/**
 * 工具
 * @type {{alert: ktUtils.alert, initFileUploadControl: (function(*, *=, *=, *=): (jQuery|HTMLElement))}}
 */
var ktUtils = {
    /**
     * 信息提示框（注意：使用它后面需要加 return false; 否则无效）
     * @param msg 提示内容
     */
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
     * @param toolName 工具名称(请务必与数据库保持一致)
     * @param navTabType 页签类型(image,txt,video...)
     */
    renderNavTab: function (navTabType) {
        var tabArr = [];
        var toolName = $('.breadcrumb').find('.active').text(); // 工具名称(请务必与数据库保持一致)
        var allMenus = this.getAllMenus();
        allMenus.forEach(function (item) {
            if (item['moduleType'] === navTabType) {
                item['menu'].forEach(function (itm) {
                    tabArr.push(itm);
                });
            }
        });
        if (!tabArr || !toolName) {
            error('renderNavTab error: tabArr or key must be not null');
            return;
        }
        tabArr.forEach(function (obj) {
            if (obj.name === toolName) {
                $('.nav-tabs').append('<li class="nav-item">\n' +
                    '            <a class="nav-link kt-fs13 active" href="' + kfc + obj.url + '">' + obj.name + '</a>\n' +
                    '        </li>');
            } else {
                $('.nav-tabs').append('<li class="nav-item">\n' +
                    '            <a class="nav-link kt-fs13" href="'+kfc + obj.url+'">'+obj.name+'</a>\n' +
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
    },
    /**
     * 处理url
     * @param url
     * @returns {string}
     */
    handleUrl: function (url) {
        return url ? (kfc + url) : '#';
    },
    /**
     * 同步get
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     */
    get: function (url, params) {
        return this.ajx(url, params, 'GET');
    },
    /**
     * 同步post
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     */
    post: function (url, params) {
        return this.ajx(url, params, 'POST');
    },
    ajx: function (url, params, method) {
        var result = "";
        if (!url) {
            return result;
        }
        if (!params) {
            params = {};
        }
        if (!method) {
            method = 'GET';
        }
        $.ajax({
            type: method,
            url: kfc + url,
            data: params,
            async: false,
            success: function (r) {
                result = r;
            }
        });
        return result;
    },
    setCookie: function (key, val) {
        // JSON.stringify(allMenus)
        // expires：（Number|Date）有效期；设置一个整数时，单位是天；也可以设置一个日期对象作为Cookie的过期日期；
        // path：（String）创建该Cookie的页面路径；
        // domain：（String）创建该Cookie的页面域名；
        // secure：（Booblean）如果设为true，那么此Cookie的传输会要求一个安全协议，例如：HTTPS；
        $.cookie(key, JSON.stringify(val), {
            expires: 7,
            path: '/'
            // domain: 'jquery.com',
            // secure: true
        });
    },
    getCookie: function (key) {
        var val = $.cookie(key);
        if (val) {
            return $.parseJSON(val);
        }
        return val;
    },
    delCookie: function (key) {
        $.cookie(key, '', {expires: -1, path: '/'});
    },
    getAllMenus: function () {
        var allMenus = ktUtils.getCookie('allMenus');   // 优先拿cookie中的值
        if (!allMenus) {    // 若cookie没有值，则从后台获取
            allMenus = ktUtils.get(ktConfig.api.allMenus);
            if (allMenus.errorCode === 200) {
                ktUtils.setCookie('allMenus', allMenus.data); // 保存到cookie中
                return allMenus.data;
            } else {
                return '';
            }
        }
        return allMenus;
    }
};

/**
 * 日志输出
 */
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

/**
 * 错误日志输出
 */
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
 * 自定义stringBuilder
 * @constructor
 */
var StringBuilder =  function() {
    this._stringArray = [];
};

StringBuilder.prototype.append = function(str) {
    this._stringArray.push(str);
};

StringBuilder.prototype.toString = function() {
    return this._stringArray.join("");
};

/**
 * 自定义startWith
 * @param str
 * @returns {boolean}
 */
String.prototype.startWith=function(str){
    var reg=new RegExp("^"+str);
    return reg.test(this);
};

/**
 * 自定义endWith
 * @param str
 * @returns {boolean}
 */
String.prototype.endWith=function(str){
    var reg=new RegExp(str+"$");
    return reg.test(this);
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
    }, 20);
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
        pbaEle.text('转换失败');
    }
    pbaEle.removeClass('progress-bar-striped');
    pbaEle.removeClass('progress-bar-animated');
}