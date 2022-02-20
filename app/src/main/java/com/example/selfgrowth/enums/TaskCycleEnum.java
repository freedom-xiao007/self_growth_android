package com.example.selfgrowth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TaskCycleEnum {

    No(-1, "无"),
    Daily(0, "每天"),
    Week(1, "每周"),
    Month(2, "每月"),
    Year(3, "没年"),
    ;

    private int index;
    private String name;

    public static int getIndexByName(final String name) {
        return TaskCycleEnum.valueOf(name).index;
    }

    public static String getNameByIndex(final int index) {
        return TaskCycleEnum.values()[index].name;
    }
}
