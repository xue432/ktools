INSERT INTO `sys_menu` VALUES (1, 0, '图片工具', '/image/artPic', 0, 'IMAGE', '', '', '', 0, 0, 0, '2018-11-21 16:43:55');
INSERT INTO `sys_menu` VALUES (2, 1, '艺术图片生成', '/image/artPic', 1, NULL, '/static/image/icon/art.png', 'static/image/ktpysj.png', '在线图片/照片一键变成卡通、素描、黑白', 5, 0, 0, '2018-11-21 16:46:54');
INSERT INTO `sys_menu` VALUES (3, 0, '文字工具', '/txt/ascii', 0, 'TXT', '', '', '', 0, 0, 0, '2018-11-21 16:48:05');
INSERT INTO `sys_menu` VALUES (4, 1, '转换字符画', '/image/asciiPic', 1, NULL, '/static/image/icon/zhuan.png', 'static/image/ktzfh.png', '图片制作、转换成字符画', 3, 0, 0, '2018-11-22 17:41:15');
INSERT INTO `sys_menu` VALUES (5, 1, 'GIF字符动画生成', '/image/asciiGif', 1, NULL, '/static/image/icon/gif.png', 'static/image/ascgif.gif', 'GIF动态图制作成字符动画', 1, 0, 0, '2018-11-22 17:42:15');
INSERT INTO `sys_menu` VALUES (6, 1, '彩色ASCII图生成', '/image/colorAsciiPic', 1, NULL, '/static/image/icon/cai.png', 'static/image/clascpic.png', '快速将图片制作成ASCII图片', 1, 0, 0, '2018-11-22 17:42:41');
INSERT INTO `sys_menu` VALUES (7, 1, 'GIF动态图片制作', '/image/gif', 1, NULL, '/static/image/icon/gif.png', 'static/image/cpgif.gif', 'GIF动态图片在线制作、合成', 1, 0, 0, '2018-11-22 17:43:13');
INSERT INTO `sys_menu` VALUES (8, 3, 'ASCII艺术字生成', '/txt/ascii', 1, NULL, '/static/image/icon/ascii.png', 'static/image/ktysj.png', 'ASCII文字制作、ASCII字形生成', 4, 0, 0, '2018-11-22 17:43:57');
INSERT INTO `sys_menu` VALUES (9, 0, '头像相关', '/head', 0, 'HEAD', '', '', '', 0, 0, 1, '2018-11-22 17:44:32');
INSERT INTO `sys_menu` VALUES (10, 9, '头像大全', '/head', 1, NULL, '', '', '', 0, 0, 0, '2018-11-22 17:45:39');
INSERT INTO `sys_menu` VALUES (11, 9, '生成头像', '/head/pixel', 1, NULL, '', '', '', 0, 0, 0, '2018-11-22 17:46:04');
INSERT INTO `sys_menu` VALUES (12, 0, '便民工具', '', 0, 'CONVENIENCE', '', '', '', 0, 0, 0, '2018-11-22 17:48:59');
INSERT INTO `sys_menu` VALUES (13, 0, '开发者工具', '', 0, 'DEV', '', '', '', 0, 0, 0, '2018-11-22 17:49:22');
INSERT INTO `sys_menu` VALUES (14, 0, '其它工具', '', 0, 'OTHER', '', '', '', 0, 0, 1, '2018-11-22 17:50:23');
INSERT INTO `sys_menu` VALUES (15, 14, '开发中', '', 1, NULL, '', '', '', 0, 0, 1, '2018-11-22 17:50:56');
INSERT INTO `sys_menu` VALUES (16, 13, '压力测试', '/dev/stress/testing', 1, '', '/static/image/icon/ya.png', 'static/image/default_t.png', '压力测试、网站性能测试、API压测', 2, 0, 0, '2018-12-03 16:41:18');
INSERT INTO `sys_menu` VALUES (17, 13, 'JSON格式化', '/dev/json/format', 1, '', '/static/image/icon/json.png', '', 'JSON格式化、校验工具', 2, 0, 0, '2018-12-06 15:20:43');
INSERT INTO `sys_menu` VALUES (18, 12, '二维码生成', '/convenience/qrCode', 1, '', '/static/image/icon/2.png', '', '二维码生成及下载', 1, 0, 0, '2018-12-07 14:35:55');
INSERT INTO `sys_menu` VALUES (19, 12, 'IP归属地查询', '/convenience/ip', 1, '', '/static/image/icon/ip.png', '', '查询IP或域名信息、如归属地查询、运营商查询', 1, 0, 0, '2018-12-07 14:36:37');
INSERT INTO `sys_menu` VALUES (20, 13, 'linux常用命令字典', '/dev/linux/cmd', 1, '', '/static/image/icon/linux.png', '', '集合开发者常用linux命令，提供快速搜索', 2, 0, 0, '2018-12-10 16:30:33');
INSERT INTO `sys_menu` VALUES (21, 13, '颜色选择器', '/dev/colorPicker', 1, '', '/static/image/icon/yuan.png', '', '颜色选择器、网站颜色代码选择、拾色器', 3, 0, 0, '2018-12-20 11:51:37');
INSERT INTO `sys_menu` VALUES (22, 1, '图片加水印', '/image/watermark', 1, '', '/static/image/icon/water.png', '', '图片添加自定义水印文字和水印颜色', 1, 0, 0, '2019-01-24 11:07:34');
INSERT INTO `sys_menu` VALUES (23, 12, '12306抢票助手', '/convenience/shakedown12306', 1, '', '', '', '12306自动抢票程序，稳定快速', 0, 0, 1, '2019-01-28 17:42:04');
INSERT INTO `sys_menu` VALUES (24, 1, '词云图生成', '/image/wordCloud', 1, '', '/static/image/icon/cy.png', '', '一键生成逼格满满的词云图', 0, 0, 0, '2019-06-21 11:09:01');
INSERT INTO `sys_menu` VALUES (25, 13, 'markdown编辑器', '/dev/markdown', 1, '', '/static/image/icon/md.png', '', 'markdown在线编辑器', 0, 0, 0, '2019-08-20 17:17:30');
INSERT INTO `sys_menu` VALUES (26, 13, 'JS/HTML/CSS格式化', '/dev/jsHtmlCss/format', 1, '', '/static/image/icon/jhc.png', '', ' JS/HTML/CSS代码一键格式化、美化', 0, 0, 0, '2019-08-21 13:46:40');
INSERT INTO `sys_menu` VALUES (27, 3, '字符统计', '/txt/charStatistics', 1, '', '/static/image/icon/char.png', '', '在线实时分析统计各类字符个数', 0, 0, 0, '2019-08-21 17:51:11');
INSERT INTO `sys_menu` VALUES (28, 12, '困难选择器', '/convenience/selectors', 1, '', '/static/image/icon/diffc.png', '', '选择，交给我', 0, 0, 0, '2019-08-26 11:33:11');
INSERT INTO `sys_menu` VALUES (29, 1, '图片压缩', '/image/compress', 1, '', '/static/image/icon/compress.png', '', '在线压缩图片', 0, 0, 0, '2019-08-28 10:57:23');
INSERT INTO `sys_menu` VALUES (30, 3, '文章生成器', '/txt/bullshitGenerator', 1, '', '/static/image/icon/wz.png', '', '生成狗屁不通的文章', 0, 0, 0, '2019-12-19 11:17:01');
