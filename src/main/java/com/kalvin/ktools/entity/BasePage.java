package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 分页基础参数
 */
public class BasePage {

    /**
     * 当前页码
     */
    @TableField(exist = false)
    private int current;
    /**
     * 每页显示条目
     */
    @TableField(exist = false)
    private int size;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
