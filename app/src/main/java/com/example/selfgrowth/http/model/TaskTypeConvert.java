package com.example.selfgrowth.http.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import lombok.SneakyThrows;

public class TaskTypeConvert {

    public static final Map<String, Integer> convert = init();

    private static Map<String, Integer> init() {
        return ImmutableMap.<String, Integer>builder()
                .put("输入", 0)
                .put("输出", 1)
                .put("日常", 2)
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
