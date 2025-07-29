package com.ninghua.common.core.enums;

import com.ninghua.common.core.result.DictItemResult;
import com.ninghua.common.core.result.DictResult;

import java.util.ArrayList;
import java.util.List;

public enum Gender {

    /**
     * 男
     */
    MALE("男"),
    /**
     * 女
     */
    FEMALE("女"),
    /**
     * 未知
     */
    UNKNOWN("未知");

    // 枚举项对应的文本描述
    private final String desc;

    // 构造函数：关联枚举值与文本
    Gender(String desc) {
        this.desc = desc;
    }

    // 获取文本描述
    public String getDesc() {
        return desc;
    }

    public static DictResult toDict() {
        DictResult result = new DictResult();
        result.setName("性别");
        result.setCode("gender");
        List<DictItemResult> dictList = new ArrayList<>();
        dictList.add(new DictItemResult(MALE.getDesc(), MALE.name()));
        dictList.add(new DictItemResult(FEMALE.getDesc(), FEMALE.name()));
        result.setItems(dictList);
        return result;
    }


    /**
     * 转换为字典列表
     * @return 包含所有枚举项的 DictItemResult 列表
     */
    public static List<DictItemResult> toDictItems() {
        List<DictItemResult> dictList = new ArrayList<>();
        for (Gender gender : Gender.values()) {
            dictList.add(new DictItemResult(gender.desc, gender.name()));
        }
        return dictList;
    }
}