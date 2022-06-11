package com.example.selfgrowth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 体修 境界体系
 */
@Getter
@AllArgsConstructor
public enum TiXiuStateEnum {

    TONG_MAI(1, "通脉境", 1, 10, 60),
    DUAN_GU(2, "锻骨境", 1, 10, 60),
    LIAN_FU(3, "炼腑境", 1, 10, 60),
    RONG_XUE(4, "融穴境", 1, 10, 60),
    TONG_TI(5, "通体境", 1, 10, 60),
    NEI_YUAN(6, "内源境", 1, 10, 60 * 2),
    XUAN_TI(7, "玄体境", 1, 10, 60 * 2),
    JIN_SHEN(8, "金身境", 1, 10, 60 * 2),
    HE_TI(9, "合体境", 1, 10, 60 * 2),
    NIE_PAN(10, "涅槃境", 1, 10, 60 * 2),
    TONG_SHEN(11, "通神境", 1, 10, 60 * 3),
    HUN_YUAN(12, "混元境", 1, 10, 60 * 3),
    XIAN_TI(13, "仙体", 1, 10, 60 * 3),
    SHEN_TI(14, "圣体", 1, 10, 60 * 3),
    DAO_TI(15, "道体", 1, 10, 60 * 3),
    ;

    private final int index;
    private final String name;
    private final int minState;
    private final int maxState;
    private final int upgradeNeed;

    public static TiXiuStateEnum getFromIndex(int i) {
        for (TiXiuStateEnum e: TiXiuStateEnum.values()) {
            if (e.index == i) {
                return e;
            }
        }
        throw new RuntimeException("不在境界范围内：1-15: " + i);
    }
}
