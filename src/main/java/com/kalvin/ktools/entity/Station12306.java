package com.kalvin.ktools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 12306站点名称
 * </p>
 *
 * @author Kalvin
 * @since 2019-01-09
 */
@TableName("kt_station_12306")
public class Station12306 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 开头字母
     */
    private String letter;

    /**
     * 12306缩写大写字母标识
     */
    private String spellS12306;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 站点名称小写全拼
     */
    private String allSpell;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getSpellS12306() {
        return spellS12306;
    }

    public void setSpellS12306(String spellS12306) {
        this.spellS12306 = spellS12306;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllSpell() {
        return allSpell;
    }

    public void setAllSpell(String allSpell) {
        this.allSpell = allSpell;
    }

    @Override
    public String toString() {
        return "Station12306{" +
        "id=" + id +
        ", letter=" + letter +
        ", spellS12306=" + spellS12306 +
        ", name=" + name +
        ", allSpell=" + allSpell +
        "}";
    }
}
