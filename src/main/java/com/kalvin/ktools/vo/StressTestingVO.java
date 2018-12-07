package com.kalvin.ktools.vo;

/**
 * 压力测试参数
 */
public class StressTestingVO {

    /**
     * 请求头信息
     */
    private String header;
    /**
     * 请求方法：GET or POST
     */
    private String methods;
    /**
     * 并发数
     */
    private Integer concurrent;
    /**
     * 重复次数
     */
    private Integer reps;
    /**
     * 延迟时间，秒
     */
    private Integer time;
    /**
     * json字符串 参数
     */
    private String json;
    /**
     * 压测url地址，多个使用换行隔开
     */
    private String urls;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "StressTestingVO{" +
                "header='" + header + '\'' +
                ", methods='" + methods + '\'' +
                ", concurrent=" + concurrent +
                ", reps=" + reps +
                ", time=" + time +
                ", json='" + json + '\'' +
                ", urls='" + urls + '\'' +
                '}';
    }
}
