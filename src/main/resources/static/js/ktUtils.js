/**
 * 工具
 * @type {{alert: alert}}
 */
var ktUtils = {
    alert: alert,
    initFileUploadControl: function(id, uploadUrl, params) {
        var control = $('#' + id);
        if (!params) {
            params = {};
        }
        control.fileinput({
            language: 'zh', // 设置语言
            uploadUrl: uploadUrl, // 上传的地址
            enctype: 'multipart/form-data',
            allowedFileExtensions : ['jpg', 'png','gif', 'jpeg'],// 接收的文件后缀
            showUpload: false, // 是否显示上传按钮
            showCaption: false,// 是否显示标题
            showRemove:false,
            browseClass: "btn btn-primary", // 按钮样式
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            autoReplace: true,
            //dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
            minFileCount: 1,
            maxFileCount: 1, //表示允许同时上传的最大文件个数
            // enctype: 'multipart/form-data',
            // validateInitialCount:true,
            // msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            uploadExtraData:function (previewId, index) {
                //向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
                return params;
            },
            layoutTemplates :{
                // actionDelete:'', //去除上传预览的缩略图中的删除图标
                actionUpload:'',//去除上传预览缩略图中的上传图片；
                actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
            }
        });
        return control;
    },
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
// ktUtils.prototype.


/**
 * 信息提示框（注意：使用它后面需要加 return false; 否则无效）
 * @param elementId 需要在下面弹出的那个元素ID
 * @param message 提示内容
 */
function alert(elementId, message) {
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
}
