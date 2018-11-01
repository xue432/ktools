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