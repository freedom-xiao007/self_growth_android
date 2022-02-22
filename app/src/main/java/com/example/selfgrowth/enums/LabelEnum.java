package com.example.selfgrowth.enums;

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
}
