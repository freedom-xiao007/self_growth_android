package com.example.selfgrowth.model;

import com.example.selfgrowth.enums.LianQiStateEnum;
import com.example.selfgrowth.enums.TiXiuStateEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前的修仙境界
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XiuXianState {

    private LianQiState qiXiuState;
    private TiXiuState tiXiuState;
    private int reincarnationAmountOfQiXiu = 1;
    private int reincarnationAmountOfTiXiu = 1;
    private String qiXiuUpgradeMsg;
    private String tiXiuUpgradeMsg;

    private long yuanLi = 0;
    private long qiLi = 0;
    private long tiLi = 0;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LianQiState {

        private LianQiStateEnum state;
        private int level = 1;
        private int upgradeNeedQiLi;
        private int upgradeNeedYuanLi;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TiXiuState {

        private TiXiuStateEnum state;
        private int level = 1;
        private int upgradeNeedTiLi;
        private int upgradeNeedYuanLi;
    }
}
