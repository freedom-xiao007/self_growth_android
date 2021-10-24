package com.example.selfgrowth.http.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import lombok.SneakyThrows;

public class LabelType {

    private final static Map<String, Integer> convertKey = initKey();

    private static Map<String, Integer> initKey() {
        return ImmutableMap.<String, Integer>builder()
                .put("学习", 0)
                .put("运动", 1)
                .put("睡觉", 2)
                .put("工作", 3)
                .put("其他", 4)
                .build();
    }

    @SneakyThrows
    public static Integer convertToValue(String key) {
        if (!convertKey.containsKey(key)) {
            throw new Exception("没有该映射");
        }
        return convertKey.get(key);
    }
}
