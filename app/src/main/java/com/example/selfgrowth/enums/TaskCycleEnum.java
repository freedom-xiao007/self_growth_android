package com.example.selfgrowth.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TaskCycleEnum {

    DEFAULT("无"),
    DAILY("每天"),
    WEEK("每周"),
    MONTH("每月"),
    YEAR("每年"),
    ;

    private String name;

    public static TaskCycleEnum fromString(final String name) {
        for (TaskCycleEnum e: TaskCycleEnum.values()) {
            if (e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return DEFAULT;
    }

    public static List<String> names() {
        return Arrays.stream(TaskCycleEnum.values()).map(e -> e.name).collect(Collectors.toList());
    }
}
