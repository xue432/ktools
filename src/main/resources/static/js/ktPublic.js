/**
 * 公共js
 * @type {index|BigInt|number|*|jQuery}
 */

// 初始化navbar菜单
var allMenus = ktUtils.getAllMenus();
var navbarEle = $('#collapsibleNavbar').find('ul');
log('allMenus=', allMenus);
if (allMenus) {
    var sb = new StringBuilder();
    allMenus.forEach(function (item) {
        if (item['menu'].length > 0) {
            sb.append('                <li class="nav-item dropdown">');
            sb.append('                    <a class="nav-link dropdown-toggle" id="navbardrop"  data-toggle="dropdown" href="' + ktUtils.handleUrl(item['url']) + '">'+item['module']+'</a>');
            sb.append('                    <div class="dropdown-menu mt-0">');
            // 遍历子菜单
            item['menu'].forEach(function (itm) {
                sb.append('                         <a class="dropdown-item" href="' + ktUtils.handleUrl(itm['url']) + '">'+itm['name']+'</a>');
            });
            sb.append('                         <div class="dropdown-divider"></div>');
            sb.append('                     </div>');
            sb.append('                 </li>');
        } else {
            sb.append('                <li class="nav-item">');
            sb.append('                    <a class="nav-link" href="' + ktUtils.handleUrl(item['url']) + '">'+item['module']+'</a>');
            sb.append('                </li>');
        }
    });
    navbarEle.append(sb.toString());
}


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
dropdownEle.on('mouseover', function (event) {
    $(event.currentTarget).addClass('show');
    $(event.currentTarget).find('div').addClass('show');
});

// 鼠标离开导航栏下拉导航收起效果
dropdownEle.on('mouseleave', function (event) {
    $(event.currentTarget).removeClass('show');
    $(event.currentTarget).find('div').removeClass('show');
});

// 网站标题特效事件
$('.skew-title').children('span').hover((function() {
    var $el, n;
    $el = $(this);
    n = $el.index() + 1;
    $el.addClass('flat');
    if (n % 2 === 0) {
        return $el.prev().addClass('flat');
    } else {
        if (!$el.hasClass('last')) {
            return $el.next().addClass('flat');
        }
    }
}), function() {
    return $('.flat').removeClass('flat');
});

// 初始化模态框
$("body").append('<!-- 模态框 -->\n' +
    '  <div class="modal fade" id="myModal" style="top: 31%;">\n' +
    '    <div class="modal-dialog modal-sm">\n' +
    '      <div class="modal-content">\n' +
    '        <!-- 模态框头部 -->\n' +
    '        <div class="modal-header" style="background: #73a6c1">\n' +
    '          <h4 class="modal-title">提示</h4>\n' +
    '          <button type="button" class="close" data-dismiss="modal">&times;</button>\n' +
    '        </div>\n' +
    '        <!-- 模态框主体 -->\n' +
    '        <div class="modal-body kt-fc-title">\n' +
    '        </div>\n' +
    '        <!-- 模态框底部 -->\n' +
    '        <div class="modal-footer">\n' +
    '          <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>\n' +
    '        </div>\n' +
    '      </div>\n' +
    '    </div>\n' +
    '  </div>');

// 实例化tooltips
$('[data-toggle="tooltip"]').tooltip();