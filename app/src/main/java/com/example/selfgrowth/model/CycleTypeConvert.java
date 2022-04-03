package com.example.selfgrowth.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import lombok.SneakyThrows;

/**
 * 任务周期枚举
 */
public class CycleTypeConvert {

    private final static Map<String, Integer> convertKey = initKey();
    private final static Map<Integer, String> convertValue = initValue();

    private static Map<String, Integer> initKey() {
        return ImmutableMap.<String, Integer>builder()
                .put("单次", 0)
                .put("每日", 1)
                .put("每周", 2)
                .put("每月", 3)
                .put("每年", 4)
                .build();
    }

    private static Map<Integer, String> initValue() {
        return ImmutableMap.<Integer, String>builder()
                .put(0, "单次")
                .put(1, "每日")
                .put(2, "每周")
                .put(3, "每月")
                .put(4, "每年")
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
