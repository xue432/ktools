package com.kalvin.ktools.vo;

import com.kalvin.ktools.entity.RGB;

/**
 * 图片水印参数
 */
public class ImageWatermarkVO {

    private String fileName;
    private String waterMarkContent;
    private RGB rgb;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWaterMarkContent() {
        return waterMarkContent;
    }

    public void setWaterMarkContent(String waterMarkContent) {
        this.waterMarkContent = waterMarkContent;
    }

    public RGB getRgb() {
        return rgb;
    }

    public void setRgb(RGB rgb) {
        this.rgb = rgb;
    }

    @Override
    public String toString() {
        return "ImageWatermarkVO{" +
                "fileName='" + fileName + '\'' +
                ", waterMarkContent='" + waterMarkContent + '\'' +
                ", rgb=" + rgb +
                '}';
    }
}
