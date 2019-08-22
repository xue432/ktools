package com.kalvin.ktools.comm.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * KApi 接口地址封装类
 */
@Configuration
@ConfigurationProperties(prefix = "kt.kapi")
public class KApi {

    private String imgUploadUrl;
    private String imgHandleUrl;
    private String img2AsciiUrl;
    private String img2GifUrl;
    private String img2ColorAsciiUrl;
    private String imgGenerateWordCloudUrl;
    private String gif2AsciiUrl;
    private String txt2AsciiUrl;
    private String video2AsciiGifUrl;
    private String devStressTestingUrl;

    public String getImgUploadUrl() {
        return imgUploadUrl;
    }

    public void setImgUploadUrl(String imgUploadUrl) {
        this.imgUploadUrl = imgUploadUrl;
    }

    public String getImgHandleUrl() {
        return imgHandleUrl;
    }

    public void setImgHandleUrl(String imgHandleUrl) {
        this.imgHandleUrl = imgHandleUrl;
    }

    public String getImg2AsciiUrl() {
        return img2AsciiUrl;
    }

    public void setImg2AsciiUrl(String img2AsciiUrl) {
        this.img2AsciiUrl = img2AsciiUrl;
    }

    public String getImg2GifUrl() {
        return img2GifUrl;
    }

    public void setImg2GifUrl(String img2GifUrl) {
        this.img2GifUrl = img2GifUrl;
    }

    public String getImg2ColorAsciiUrl() {
        return img2ColorAsciiUrl;
    }

    public void setImg2ColorAsciiUrl(String img2ColorAsciiUrl) {
        this.img2ColorAsciiUrl = img2ColorAsciiUrl;
    }

    public String getImgGenerateWordCloudUrl() {
        return imgGenerateWordCloudUrl;
    }

    public void setImgGenerateWordCloudUrl(String imgGenerateWordCloudUrl) {
        this.imgGenerateWordCloudUrl = imgGenerateWordCloudUrl;
    }

    public String getGif2AsciiUrl() {
        return gif2AsciiUrl;
    }

    public void setGif2AsciiUrl(String gif2AsciiUrl) {
        this.gif2AsciiUrl = gif2AsciiUrl;
    }

    public String getTxt2AsciiUrl() {
        return txt2AsciiUrl;
    }

    public void setTxt2AsciiUrl(String txt2AsciiUrl) {
        this.txt2AsciiUrl = txt2AsciiUrl;
    }

    public String getVideo2AsciiGifUrl() {
        return video2AsciiGifUrl;
    }

    public void setVideo2AsciiGifUrl(String video2AsciiGifUrl) {
        this.video2AsciiGifUrl = video2AsciiGifUrl;
    }

    public String getDevStressTestingUrl() {
        return devStressTestingUrl;
    }

    public void setDevStressTestingUrl(String devStressTestingUrl) {
        this.devStressTestingUrl = devStressTestingUrl;
    }
}
