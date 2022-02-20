package com.example.selfgrowth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TaskLearnTypeEnum {

    Default(-1, "无"),
    Input(0, "输入"),
    Output(1, "输出"),
    ;

    private int index;
    private String name;

    public static int getIndexByName(final String name) {
        return TaskLearnTypeEnum.valueOf(name).index;
    }

    public static String getNameByIndex(final int index) {
        return TaskLearnTypeEnum.values()[index].name;
    }
}
