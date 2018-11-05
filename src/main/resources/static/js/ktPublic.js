/**
 * 公共js
 * @type {index|BigInt|number|*|jQuery}
 */

// 页签选择更新面包悄事件
$('.nav-tabs').find('.nav-link').on('click', function (event) {
    $('.breadcrumb').find('.active').text($(event.currentTarget).text());
});

// 鼠标经过头像框触发动画事件
$('.kt-img-box').hover(function(event) {
    $(event.currentTarget).find('.kt-img-inf').animate({"margin-top": "-20px"}, 100, "swing");
},function(event) {
    $(event.currentTarget).find('.kt-img-inf').stop(true, false).animate({"margin-top": "0"}, 100, "swing");
});

// 鼠标经过导航栏下拉导航展开效果
var dropdownEle = $('.dropdown');
log(dropdownEle);
dropdownEle.on('mouseover', function (event) {
    log(event.currentTarget);
    $(event.currentTarget).addClass('show');
});

// 鼠标离开导航栏下拉导航收起效果
dropdownEle.on('mouseleave', function (event) {
    $(event.currentTarget).removeClass('show');
});