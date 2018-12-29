package com.kalvin.ktools.comm.constant;

import cn.hutool.core.util.ClassUtil;

/**
 * 常量类
 */
public class Constant {

    public final static int OK_CODE = 200;
    public final static int FAIL_CODE = 400;
    public final static int OTHER_FAIL_CODE = 333;    // 其它错误
    public final static String OK_MSG = "请求成功";
    public final static String FAIL_MSG = "请求失败";
    public final static int STATUS_0 = 0;   // 可用状态
    public final static int STATUS_1 = 1;   // 禁用状态
    public final static String CACHE_NAME = "ktCache";  // 缓存名称

    // ktools handle image url
    public final static String HANDLE_IMAGE_URL = "/static/image/handle/";
    // ktools classpath handle image url
    public final static String CLASSPATH_HANDLE_IMAGE_DIR = ClassUtil.getClassPath() + "static/image/handle/";
    // ktools handle image dir
    public final static String HANDLE_IMAGE_REF_DIR = "static/image/handle/";
    // python handle image dir
    public final static String KAPI_HANDLE_IMAGE_DIR = "D:\\Project\\PycharmProjects\\kapi\\static\\images\\handle/";
    // 上传文件名前缀
    public final static String UPLOAD_PREFIX_FILENAME = "upload";
    // 处理文件名前缀
    public final static String HANDLE_PREFIX_FILENAME = "handle";
    public final static String HANDLE_PREFIX_FILENAME_GIF = "gif";
    public final static String HANDLE_PREFIX_FILENAME_CHAR_GIF = "char_gif";
    public final static String HANDLE_PREFIX_FILENAME_CHAR_PIC = "char_pic";

    public final static int REQ_URL_TYPE_PAGE = 0;  // 请求url类型：页面
    public final static int REQ_URL_TYPE_API = 1;  // 请求url类型：API

    public final static String DEFAULT_BANNER = "/static/image/default_t_red.png";   // 默认的工具图片
    public final static String DEFAULT_ICON = "/static/image/icon/t.png";   // 默认的工具logo图
}
