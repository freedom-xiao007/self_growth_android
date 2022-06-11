package com.example.selfgrowth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 气修 境界体系
 */
@Getter
@AllArgsConstructor
public enum LianQiStateEnum {

    LIAN_QI(1, "练气境", 1, 10, 60),
    ZHU_JI(2, "筑基境", 1, 10, 60 * 2),
    JIN_DAN(3, "金丹境", 1, 10, 60 * 3),
    YUAN_YING(4, "元婴境", 1, 10, 60 * 4),
    HUA_SHEN(5, "化神境", 1, 10, 60 * 5),
    HE_DAO(6, "合道境", 1, 10, 60 * 6),
    DI_XIAN(7, "地仙境", 1, 10, 60 * 7),
    TIAN_XIAN(8, "天仙境", 1, 10, 60 * 8),
    JIN_XIAN(9, "金仙境", 1, 10, 60 * 9),
    TAI_YI_JIN_XIAN(10, "太乙金仙境", 1, 10, 60 * 10),
    DA_LUO_JIN_XIAN(11, "大罗金仙境", 1, 10, 60 * 11),
    ZHUN_SHENG(12, "准圣", 1, 10, 60 * 12),
    SHENG_REN(13, "圣人", 1, 10, 60 * 13),
    TIAN_DAO(14, "天道", 1, 10, 60 * 14),
    DA_DAO(15, "大道", 1, 10, 60 * 15),
    ;

    private final int index;
    private final String name;
    private final int minState;
    private final int maxState;
    private final int upgradeNeed;

    public static LianQiStateEnum getFromIndex(int i) {
        for (LianQiStateEnum e: LianQiStateEnum.values()) {
            if (e.index == i) {
                return e;
            }
        }
        throw new RuntimeException("不在境界范围内：1-15");
    }
}
