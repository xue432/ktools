
CREATE TABLE `kt_traffic_statistics` (
  `ip` varchar(15) NOT NULL COMMENT '访客IP(主键)',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `page_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT '页面访问总次数',
  `api_visit_times` int(11) NOT NULL DEFAULT '0' COMMENT 'api访问总次数',
  `first_visit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '第一次访问时间',
  PRIMARY KEY (`ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='网站流量统计表';

CREATE TABLE `kt_traffic_records` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` varchar(15) NOT NULL COMMENT '访客IP',
  `isp` varchar(50) NOT NULL COMMENT '访客网络',
  `gegraphic_pos` varchar(255) NOT NULL COMMENT '访客地理位置',
  `req_url` varchar(255) NOT NULL COMMENT '访客请求url',
  `req_url_type` tinyint(1) NOT NULL COMMENT '请求url类型：0-页面 1-api',
  `req_method` varchar(100) NOT NULL COMMENT '请求方法',
  `req_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '请求状态：0-成功 1-失败',
  `req_time` int(6) NOT NULL DEFAULT '0' COMMENT '请求耗时(ms)',
  `req_type` varchar(10) NOT NULL COMMENT '请求方式',
  `req_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `mac` varchar(155) NOT NULL DEFAULT '' COMMENT '访客物理地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='网站流量日志记录表';

CREATE TABLE `sys_menu` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(18) NOT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `url` varchar(200) NOT NULL COMMENT '菜单URL',
  `level` tinyint(2) NOT NULL DEFAULT '0' COMMENT '层级。0：模块；1：一级菜单；2：二级菜单(暂无)',
  `module_type` varchar(25) DEFAULT NULL COMMENT '模块类型',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `banner` varchar(50) NOT NULL COMMENT '菜单牌面图片',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序值。越小越靠前',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态。0：正常；1：禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 初始化菜单表数据
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (1, 0, '图片相关', '/image/artPic', 0, 'IMAGE', '', '', 0, 0, '2018-11-21 16:43:55');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (2, 1, '图片转换艺术图', '/image/artPic', 1, NULL, '', 'static/image/ktpysj.png', 0, 0, '2018-11-21 16:46:54');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (3, 0, '文字相关', '/txt/ascii', 0, 'TXT', '', '', 0, 0, '2018-11-21 16:48:05');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (4, 1, '图片转换字符画', '/image/asciiPic', 1, NULL, '', 'static/image/ktzfh.png', 0, 0, '2018-11-22 17:41:15');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (5, 1, 'GIF转换AsciiGif', '/image/asciiGif', 1, NULL, '', 'static/image/ascgif.gif', 0, 0, '2018-11-22 17:42:15');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (6, 1, '图片转换彩色Ascii图', '/image/colorAsciiPic', 1, NULL, '', 'static/image/clascpic.png', 0, 0, '2018-11-22 17:42:41');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (7, 1, '图片合成GIF动图', '/image/gif', 1, NULL, '', 'static/image/cpgif.gif', 0, 0, '2018-11-22 17:43:13');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (8, 3, '文字转换ascii艺术字', '/txt/ascii', 1, NULL, ' ', 'static/image/ktysj.png', 0, 0, '2018-11-22 17:43:57');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (9, 0, '头像相关', '/head', 0, 'HEAD', '', '', 0, 0, '2018-11-22 17:44:32');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (10, 9, '头像大全', '/head', 1, NULL, '', '', 0, 0, '2018-11-22 17:45:39');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (11, 9, '生成头像', '/head/pixel', 1, NULL, '', '', 0, 0, '2018-11-22 17:46:04');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (12, 0, '便民工具', '', 0, 'CONVENIENCE', '', '', 0, 0, '2018-11-22 17:48:59');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (13, 0, '开发者工具', '', 0, 'DEV', '', '', 0, 0, '2018-11-22 17:49:22');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (14, 0, '其它工具', '', 0, 'OTHER', '', '', 0, 0, '2018-11-22 17:50:23');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (15, 14, '开发中', '', 1, NULL, '', '', 0, 0, '2018-11-22 17:50:56');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (16, 13, '在线压力测试', '/dev/stress/testing', 1, '', '', 'static/image/default_t.png', 0, 0, '2018-12-03 16:41:18');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (17, 13, 'JSON格式化', '/dev/json/format', 1, '', '', '', 0, 0, '2018-12-06 15:20:43');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (18, 12, '二维码生成', '/convenience/qrCode', 1, '', '', '', 0, 0, '2018-12-07 14:35:55');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (19, 12, 'IP归属地查询', '/convenience/ip', 1, '', '', '', 0, 0, '2018-12-07 14:36:37');
INSERT INTO `ktools`.`sys_menu`(`id`, `parent_id`, `name`, `url`, `level`, `module_type`, `icon`, `banner`, `sort`, `status`, `create_time`) VALUES (20, 13, 'linux常用命令字典', '/dev/linux/cmd', 1, '', '', '', 0, 0, '2018-12-10 16:30:33');

CREATE TABLE `kt_cmd_category` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='linux命令分类';

-- 初始化linux命令分类
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (1, '系统信息');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (2, '关机 (系统的关机、重启以及登出 ) ');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (3, '文件和目录');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (4, '文件搜索');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (5, '挂载文件系统');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (6, '磁盘空间');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (7, '用户和群组');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (8, '文件的权限');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (9, '文件的特殊属性');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (10, '压缩及解压文件');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (11, 'RPM包');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (12, 'YUM软件包升级器');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (13, 'DEB包');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (14, 'APT软件工具');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (15, '查看文件内容');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (16, '文本处理');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (17, '字符设置和文件格式转换');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (18, '文件系统分析');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (19, '初始化文件系统');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (20, 'SWAP文件系统');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (21, '备份');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (22, '光盘');
INSERT INTO `ktools`.`kt_cmd_category`(`id`, `name`) VALUES (23, '网络');


CREATE TABLE `kt_linux_cmd` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cmd_category_id` bigint(18) NOT NULL COMMENT '命令分类ID',
  `cmd` varchar(255) NOT NULL COMMENT 'linux命令',
  `name` varchar(150) NOT NULL COMMENT 'linux命令名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='linux命令表';

-- 初始化linux命令表
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (1, 1, 'arch', '显示机器的处理器架构');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (2, 1, 'uname -m', '显示机器的处理器架构');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (3, 1, 'uname -r', '显示正在使用的内核版本');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (4, 1, 'dmidecode -q', '显示硬件系统部件 - (SMBIOS / DMI)');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (5, 1, 'hdparm -i /dev/hda', '罗列一个磁盘的架构特性');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (6, 1, 'hdparm -tT /dev/sda', '在磁盘上执行测试性读取操作');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (7, 1, 'cat /proc/cpuinfo', '显示CPU info的信息');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (8, 1, 'cat /proc/interrupts', '显示中断');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (9, 1, 'cat /proc/meminfo', '校验内存使用');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (10, 1, 'cat /proc/swaps', '显示哪些swap被使用');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (11, 1, 'cat /proc/version', '显示内核的版本');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (12, 1, 'cat /proc/net/dev', '显示网络适配器及统计');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (13, 1, 'cat /proc/mounts', '显示已加载的文件系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (14, 1, 'lspci -tv', '罗列 PCI 设备');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (15, 1, 'lsusb -tv', '显示 USB 设备');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (16, 1, 'date', '显示系统日期');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (17, 1, 'cal [year]', '显示某年的日历表');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (18, 1, 'date [041217002007.00]', '设置日期和时间 - 月日时分年.秒');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (19, 1, 'clock -w', '将时间修改保存到 BIOS');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (20, 2, 'shutdown -h now', '关闭系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (21, 2, 'init 0', '关闭系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (22, 2, 'telinit 0', '关闭系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (23, 2, 'shutdown -h hours:minutes &', '按预定时间关闭系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (24, 2, 'shutdown -c', '取消按预定时间关闭系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (25, 2, 'shutdown -r now', '重启');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (26, 2, 'reboot', '重启');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (27, 2, 'logout', '注销');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (28, 3, 'cd /[path]', '进入某个目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (29, 3, 'cd ..', '返回上一级目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (30, 3, 'cd ../..', '返回上两级目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (31, 3, 'cd', '进入根目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (32, 3, 'cd -', '返回上次所在的目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (33, 3, 'pwd', '显示当前目录路径');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (34, 3, 'ls', '查看目录中的文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (35, 3, 'ls -F', '查看目录中的文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (36, 3, 'ls -l', '显示文件和目录的详细资料');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (37, 3, 'ls -a', '显示隐藏文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (38, 3, 'mkdir [dir]', '创建目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (39, 3, 'mkdir [dir1] [dir2]', '同时创建两个目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (40, 3, 'mkdir -p /[tmp]/[dir1]/[dir2]', '创建一个目录树');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (41, 3, 'rm -f [filename]', '删除文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (42, 3, 'rmdir [dir]', '删除目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (43, 3, 'rm -rf [dir]', '删除目录下所有文件及目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (44, 3, 'rm -rf [dir1] [dir2]', '同时删除两个目录下所有文件及目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (45, 3, 'mv [dir] [new_dir]', '重命名/移动一个目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (46, 3, 'cp [file1] [file2]', '复制文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (47, 3, 'cp -a [dir1] [dir2]', '复制目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (48, 3, 'ln -s [file1] [lnk1]', '创建一个指向文件或目录的软链接');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (49, 3, 'ln file1 lnk1', '创建一个指向文件或目录的物理链接');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (50, 3, 'touch [filename]', '新建文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (51, 4, 'find / -name [filename]', '搜索文件：从 \'/\' 开始进入根文件系统搜索文件和目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (52, 4, 'find / -user [user1]', '搜索属于某用户的文件和目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (53, 4, 'find / -name \\*.txt', '搜索某后缀结尾的文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (54, 4, 'find / -name -type f -mtime -n', '搜索在n天内被创建或者修改过的文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (55, 4, 'whereis [filename]', '显示文件的目录路径');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (56, 4, 'which [exefile]', '显示可执行文件的目录路径');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (57, 5, 'mount /dev/hda2 /mnt/hda2', '挂载文件系统盘');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (58, 5, 'umount /dev/hda2', '卸载挂载点：卸载挂载文件或者卸载挂载点都可以');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (59, 5, 'fuser -km /mnt/hda2', '当设备繁忙时强制卸载');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (60, 5, 'mount /dev/fd0 /mnt/floppy', '挂载一个软盘');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (61, 5, 'mount /dev/cdrom /mnt/cdrom', '挂载一个cdrom或dvdrom');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (62, 5, 'mount /dev/hdc /mnt/cdrecorder', '挂载一个cdrw或dvdrom');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (63, 5, 'mount /dev/hdb /mnt/cdrecorder', '挂载一个cdrw或dvdrom');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (64, 5, 'mount -o loop file.iso /mnt/cdrom', '挂载一个文件或ISO镜像文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (65, 5, 'mount -t vfat /dev/hda5 /mnt/hda5', '挂载一个Windows FAT32文件系统');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (66, 5, 'mount /dev/sda1 /mnt/usbdisk', '挂载一个usb 捷盘或闪存设备');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (67, 5, 'mount -t smbfs -o username=user,password=pass //WinClient/share /mnt/share', '挂载一个windows网络共享');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (68, 6, 'df -h', '显示已经挂载的分区列表');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (69, 6, 'ls -lSr |more', '以尺寸大小排列文件和目录');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (70, 6, 'du -sh [dir1]', '估算目录已使用的磁盘空间');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (71, 6, 'du -sk * | sort -rn', '以容量大小为依据依次显示文件和目录的大小');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (72, 6, 'rpm -q -a --qf \'%10{SIZE}t%{NAME}n\' | sort -k1,1n', '以大小为依据依次显示已安装的rpm包所使用的空间 (fedora, redhat类系统)');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (73, 6, 'dpkg-query -W -f=\'${Installed-Size;10}t${Package}n\' | sort -k1,1n', '以大小为依据显示已安装的deb包所使用的空间 (ubuntu, debian类系统)');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (74, 7, 'groupadd [group_name]', '创建用户组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (75, 7, 'groupdel [group_name]', '删除用户组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (76, 7, 'groupmod -n [new_group_name] [old_group_name]', '重命名用户组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (77, 7, 'useradd -c \"Name Surname \" -g admin -d /home/user1 -s /bin/bash user1', '创建一个属于 \"admin\" 用户组的用户');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (78, 7, 'useradd [user]', '创建用户');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (79, 7, 'userdel -r [user]', '删除用户（ \'-r\' 排除主目录）');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (80, 7, 'usermod -c \"User FTP\" -g system -d /ftp/user1 -s /bin/nologin user1', '修改用户属性');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (81, 7, 'passwd', '修改密码、修改口令');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (82, 7, 'passwd [user]', '修改某个用户的密码、口令');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (83, 7, 'chage -E [2005-12-31] [user1]', '设置用户密码口令的失效期限');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (84, 7, 'pwck', '检查 \'/etc/passwd\' 的文件格式和语法修正以及存在的用户');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (85, 7, 'grpck', '检查 \'/etc/passwd\' 的文件格式和语法修正以及存在的群组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (86, 7, 'newgrp [group_name]', '登陆进一个新的群组以改变新创建文件的预设群组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (87, 8, 'ls -lh', '显示权限、查看权限');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (88, 8, 'ls /tmp | pr -T5 -W$COLUMNS', '将终端划分成5栏显示');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (89, 8, 'chmod ugo+rwx [dir1]', '设置目录的所有人(u)、群组(g)以及其他人(o)以读（r ）、写(w)和执行(x)的权限');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (90, 8, 'chmod go-rwx [dir1]', '删除群组(g)与其他人(o)对目录的读写执行权限');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (91, 8, 'chown [user] [file]', '改变文件的所有人属性');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (92, 8, 'chown -R [user] [dir]', '改变目录的所有人属性并同时改变该目录下所有文件的属性');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (93, 8, 'chgrp [group] [file]', '改变文件的群组');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (94, 8, 'chown [user]:[group] [file]', '改变文件的所有人和群组属性');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (95, 8, 'find / -perm -u+s', '罗列一个系统中所有使用了SUID控制的文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (96, 8, 'chmod u+s /bin/file1', '设置一个二进制文件的 SUID 位 - 运行该文件的用户也被赋予和所有者同样的权限');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (97, 8, 'chmod u-s /bin/file1', '禁用一个二进制文件的 SUID位');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (98, 8, 'chmod g+s /home/public', '设置一个目录的SGID 位 - 类似SUID ，不过这是针对目录的');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (99, 8, 'chmod g-s /home/public', '禁用一个二进制文件的 SUID位');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (100, 8, 'chmod o+t /home/public', '设置一个文件的 STIKY 位 - 只允许合法所有人删除文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (101, 8, 'chmod o-t /home/public', '禁用一个目录的 STIKY 位');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (102, 10, 'bunzip2 file1.bz2', '解压文件：以.bz2后缀的压缩文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (103, 10, 'bzip2 file1', '压缩文件：以.bz2为后缀');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (104, 10, 'gunzip file1.gz', '解压文件：以.gz后缀的压缩文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (105, 10, 'gzip -9 file1', '压缩文件：以最大程度压缩');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (106, 10, 'gzip file1', '压缩文件：以.gz为后缀');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (107, 10, 'tar -cvfz archive.tar.gz', '压缩文件：以.tar.gz为后缀');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (108, 10, 'tar -zxvf archive.tar.gz', '解压文件：以.tar.gz后缀的压缩文件');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (109, 10, 'zip file1.zip file1', '压缩文件：以.zip为后缀');
INSERT INTO `ktools`.`kt_linux_cmd`(`id`, `cmd_category_id`, `cmd`, `name`) VALUES (110, 10, 'unzip file1.zip', '解压文件：以.zip后缀的压缩文件');


CREATE TABLE `kt_user12306` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(150) COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号',
  `password` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '12306账号密码',
  `passenger_id_no` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乘客身份证号',
  `passenger` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乘客姓名',
  `ticket_num` int(6) NOT NULL DEFAULT '0' COMMENT '成功出票次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `12306账号` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='12306账号信息表';

CREATE TABLE `kt_ticket12306_order` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT 'user12306表ID',
  `train_date` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '乘客出发日期，多个使用英文逗号分隔',
  `train_num` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '车次，多个使用英文逗号分隔',
  `seat_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '座席类型。M：一等座；O：二等座；N：无座。多个用英文逗号分隔',
  `ticket_status` tinyint(3) DEFAULT '0' COMMENT '抢票状态。0：已停止；1：进行中',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态：0：正常；1：已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='12306抢票订单信息表';