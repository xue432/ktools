var ktUtils = {
    alert: alert
};

// ktUtils.prototype.


/**
 * 信息提示框（注意：使用它后面需要加 return false; 否则无效）
 * @param elementId 需要在下面弹出的那个元素ID
 * @param message 提示内容
 */
function alert (elementId, message) {
    var alertId = "alert_" + elementId;
    var alertEle = $("#" + alertId);
    var tarEle = $('#' + elementId);

    if (alertEle.length === 0) {
        tarEle.after(
            '<div id="'+alertId+'" class="alert alert-info fade show" style="z-index: 1; position: absolute;">'+
            '<a href="#" class="close" data-dismiss="alert">&times;</a>'+
            '<strong>小提示：</strong>'+ message +
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