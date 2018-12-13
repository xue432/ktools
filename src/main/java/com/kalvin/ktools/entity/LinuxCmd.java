package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * linux命令表
 * </p>
 *
 * @author Kalvin
 * @since 2018-12-11
 */
@TableName("kt_linux_cmd")
public class LinuxCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 命令分类ID
     */
    private Long cmdCategoryId;

    /**
     * linux命令
     */
    private String cmd;

    /**
     * linux命令名称
     */
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCmdCategoryId() {
        return cmdCategoryId;
    }

    public void setCmdCategoryId(Long cmdCategoryId) {
        this.cmdCategoryId = cmdCategoryId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LinuxCmd{" +
        "id=" + id +
        ", cmdCategoryId=" + cmdCategoryId +
        ", cmd=" + cmd +
        ", name=" + name +
        "}";
    }
}
