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
public enum TaskLearnTypeEnum {

    DEFAULT("无"),
    INPUT("输入"),
    OUTPUT("输出"),
    ;

    private String name;

    public static TaskLearnTypeEnum fromString(final String name) {
        for (TaskLearnTypeEnum e: TaskLearnTypeEnum.values()) {
            if (e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return DEFAULT;
    }

    public static List<String> names() {
        return Arrays.stream(TaskLearnTypeEnum.values()).map(e -> e.name).collect(Collectors.toList());
    }
}
