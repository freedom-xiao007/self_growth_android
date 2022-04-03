package com.example.selfgrowth.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import lombok.SneakyThrows;

public class TaskTypeConvert {

    public static final Map<String, Integer> convertKey = initKey();
    public static final Map<Integer, String> convertValue = initValue();

    private static Map<String, Integer> initKey() {
        return ImmutableMap.<String, Integer>builder()
                .put("输入", 0)
                .put("输出", 1)
                .put("日常", 2)
                .build();
    }

    private static Map<Integer, String> initValue() {
        return ImmutableMap.<Integer, String>builder()
                .put(0, "输入")
                .put(1, "输出")
                .put(2, "日常")
                .build();
    }

    @SneakyThrows
    public static Integer convertToValue(String key) {
        if (!convertKey.containsKey(key)) {
            throw new Exception("没有该映射");
        }
        return convertKey.get(key);
    }

    @SneakyThrows
    public static String convertToKey(Integer value) {
        if (!convertValue.containsKey(value)) {
            throw new Exception("没有该映射");
        }
        return convertValue.get(value);
    }
}
