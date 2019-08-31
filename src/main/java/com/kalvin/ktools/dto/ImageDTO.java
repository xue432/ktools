package com.kalvin.ktools.dto;

import java.io.Serializable;

/**
 * 图片实体类
 */
public class ImageDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private String base64;
    private Long size;

    public ImageDTO(String name, String base64) {
        this.name = name;
        this.base64 = base64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "name='" + name + '\'' +
                ", base64='" + base64 + '\'' +
                ", size=" + size +
                '}';
    }
}
