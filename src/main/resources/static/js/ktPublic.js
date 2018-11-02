/**
 * 公共js
 * @type {index|BigInt|number|*|jQuery}
 */

// 导航选中
var itemEle = $('.navbar-nav').find('.nav-link');
itemEle.on('click', function (event) {
    var src = $(event.currentTarget).attr('href');
    var txt = $(event.currentTarget).text();
    log(txt)
});

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