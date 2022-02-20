package com.example.selfgrowth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TaskTypeEnum {

    Default(-1, "无"),
    Code(0, "代码"),
    Note(1, "博客笔记"),
    Book(3, "书籍"),
    ;

    private int index;
    private String name;

    public static int getIndexByName(final String name) {
        return TaskTypeEnum.valueOf(name).index;
    }

    public static String getNameByIndex(final int index) {
        return TaskTypeEnum.values()[index].name;
    }
}
