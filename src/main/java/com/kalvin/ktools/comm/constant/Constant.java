package com.kalvin.ktools.comm.constant;

/**
 * 常量类
 */
public class Constant {

    public final static int OK_CODE = 200;
    public final static int FAIL_CODE = 400;
    public final static int OTHER_FAIL_CODE = 333;    // 其它错误
    public final static String OK_MSG = "请求成功";
    public final static String FAIL_MSG = "请求失败";

    // ktools handle image url
    public final static String HANDLE_IMAGE_URL = "/static/image/handle/";
    // ktools classpath handle image url
    public final static String CLASSPATH_HANDLE_IMAGE_URL = "WEB-INF/classes/static/image/handle/";
    // ktools handle image dir
    public final static String HANDLE_IMAGE_REF_DIR = "static/image/handle/";
    // python handle image dir
    public final static String KAPI_HANDLE_IMAGE_DIR = "D:\\Project\\PycharmProjects\\kapi\\static\\images\\handle/";
    // 上传文件名前缀
    public final static String UPLOAD_PREFIX_FILENAME = "upload";
    // 处理文件名前缀
    public final static String HANDLE_PREFIX_FILENAME = "handle";
}
