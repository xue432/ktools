package com.kalvin.ktools.entity;

/**
 * RGB实体类
 */
public class RGB {

    private Integer r;
    private Integer g;
    private Integer b;

    public Integer getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Integer getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public Integer getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "RGB{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
