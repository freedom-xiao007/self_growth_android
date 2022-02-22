package com.example.selfgrowth.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LabelEnum {

    DEFAULT("其他"),
    LEARN("学习"),
    RUNNING("运动"),
    SLEEP("睡觉")
    ;

    private String name;

    public static LabelEnum fromString(final String name) {
        for (LabelEnum label: LabelEnum.values()) {
            if (label.name.equalsIgnoreCase(name)) {
                return label;
            }
        }
        return DEFAULT;
    }

    public static List<String> names() {
        return Arrays.stream(LabelEnum.values()).map(e -> e.name).collect(Collectors.toList());
    }
}
