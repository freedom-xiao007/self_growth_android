package com.example.selfgrowth.http.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import java.util.Map;

import lombok.SneakyThrows;

/**
 * 任务周期枚举
 */
public class CycleTypeConvert {

    private final static Map<String, Integer> convert = init();

    private static Map<String, Integer> init() {
        return ImmutableMap.<String, Integer>builder()
                .put("单次", 0)
                .put("每日", 1)
                .put("每周", 2)
                .put("每月", 3)
                .put("每年", 4)
                .build();
    }

    @SneakyThrows
    public static Integer convert(String key) {
        if (!convert.containsKey(key)) {
            throw new Exception("没有该映射");
        }
        return convert.get(key);
    }
}
