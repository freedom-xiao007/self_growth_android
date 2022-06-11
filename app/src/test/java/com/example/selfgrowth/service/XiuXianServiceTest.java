package com.example.selfgrowth.service;

import com.example.selfgrowth.db.SharedPreferencesDb;
import com.example.selfgrowth.enums.LianQiStateEnum;
import com.example.selfgrowth.enums.TiXiuStateEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.XiuXianState;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.xiuxian.CalService;
import com.example.selfgrowth.service.backend.xiuxian.XiuXianService;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 修仙相关服务测试
 *
 * 1.初始进入界面，进行昨日结算
 *   如果已经结算过了，不再进行结算
 *   没有进行结算，则进行结算
 *      将昨日所得，累加得到总和，判断是否可以进行升级，升级进度条信息等
 *
 * 2.获取累计修炼值
 *
 * 3.获取今日修炼值
 */
public class XiuXianServiceTest {

    private XiuXianService xiuXianService = XiuXianService.getInstance();
    private final Map<String, Object> db =new HashMap<>(10);

    @Test
    public void yesterdaySettlementTest() {
        SharedPreferencesDb preferencesDb = Mockito.mock(SharedPreferencesDb.class);
        Mockito.doAnswer((Answer<String>) invocation -> {
            String key = invocation.getArgument(0);
            String defaultVal = invocation.getArgument(1);
            return (String) this.db.getOrDefault(key, defaultVal);
        }).when(preferencesDb).getString(Mockito.anyString(), Mockito.anyString());
        Mockito.doAnswer((Answer<Void>) invocation -> {
            String key = invocation.getArgument(0);
            String val = invocation.getArgument(1);
            db.put(key, val);
            return null;
        }).when(preferencesDb).save(Mockito.anyString(), Mockito.anyString());
        Mockito.doAnswer((Answer<Boolean>) invocation -> {
            String key = invocation.getArgument(0);
            return (Boolean) this.db.getOrDefault(key, false);
        }).when(preferencesDb).getBoolean(Mockito.anyString());
        Mockito.doAnswer((Answer<Void>) invocation -> {
            String key = invocation.getArgument(0);
            Boolean val = invocation.getArgument(1);
            db.put(key, val);
            return null;
        }).when(preferencesDb).save(Mockito.anyString(), Mockito.anyBoolean());
        xiuXianService.setXiuXianDb(preferencesDb);

        CalService calService = Mockito.mock(CalService.class);
        Mockito.when(calService.getPeriodData(Mockito.any())).thenReturn(DashboardResult.builder().build());
        xiuXianService.setCalService(calService);

        // 初始境界，练气一层，通脉一层,轮回一层
        XiuXianState state = xiuXianService.yesterdaySettlement();
        Mockito.verify(calService, Mockito.times(1)).getPeriodData(Mockito.any());
        Assert.assertEquals(1, state.getReincarnationAmountOfQiXiu());
        Assert.assertEquals(1, state.getReincarnationAmountOfTiXiu());
        Assert.assertEquals("气修：资源不足，无法突破，请努力修炼", state.getQiXiuUpgradeMsg());
        Assert.assertEquals("体修：资源不足，无法突破，请努力修炼", state.getTiXiuUpgradeMsg());
        Assert.assertEquals(0, state.getYuanLi());
        Assert.assertEquals(0, state.getQiLi());
        Assert.assertEquals(0, state.getTiLi());
        XiuXianState.LianQiState qiXiuState = state.getQiXiuState();
        Assert.assertEquals(LianQiStateEnum.LIAN_QI, qiXiuState.getState());
        Assert.assertEquals(1, qiXiuState.getLevel());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed(), qiXiuState.getUpgradeNeedQiLi());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed() / 2, qiXiuState.getUpgradeNeedYuanLi());
        XiuXianState.TiXiuState tiXiuState = state.getTiXiuState();
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI, tiXiuState.getState());
        Assert.assertEquals(1, tiXiuState.getLevel());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed(), tiXiuState.getUpgradeNeedTiLi());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed() / 2, tiXiuState.getUpgradeNeedYuanLi());

        // 从缓存中获取
        xiuXianService.yesterdaySettlement();
        Mockito.verify(calService, Mockito.times(1)).getPeriodData(Mockito.any());

        // 设置计算累计昨天的修炼
        Mockito.when(preferencesDb.getBoolean(Mockito.anyString())).thenReturn(false);
        // 不升级，数据累计
        Mockito.when(calService.getPeriodData(Mockito.any())).thenReturn(DashboardResult.builder()
                .sleepTime(1L)
                .learnTime(1L)
                .runningTime(1L)
                .build());
        state = xiuXianService.yesterdaySettlement();
        Assert.assertEquals(1, state.getReincarnationAmountOfQiXiu());
        Assert.assertEquals(1, state.getReincarnationAmountOfTiXiu());
        Assert.assertEquals("气修：资源不足，无法突破，请努力修炼", state.getQiXiuUpgradeMsg());
        Assert.assertEquals("体修：资源不足，无法突破，请努力修炼", state.getTiXiuUpgradeMsg());
        Assert.assertEquals(1, state.getYuanLi());
        Assert.assertEquals(1, state.getQiLi());
        Assert.assertEquals(1, state.getTiLi());
        qiXiuState = state.getQiXiuState();
        Assert.assertEquals(LianQiStateEnum.LIAN_QI, qiXiuState.getState());
        Assert.assertEquals(1, qiXiuState.getLevel());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed(), qiXiuState.getUpgradeNeedQiLi());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed() / 2, qiXiuState.getUpgradeNeedYuanLi());
        tiXiuState = state.getTiXiuState();
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI, tiXiuState.getState());
        Assert.assertEquals(1, tiXiuState.getLevel());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed(), tiXiuState.getUpgradeNeedTiLi());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed() / 2, tiXiuState.getUpgradeNeedYuanLi());

        // 升级
        Mockito.when(calService.getPeriodData(Mockito.any())).thenReturn(DashboardResult.builder()
                .sleepTime(60L)
                .learnTime(60L)
                .runningTime(60L)
                .build());
        state = xiuXianService.yesterdaySettlement();
        Assert.assertEquals(1, state.getReincarnationAmountOfQiXiu());
        Assert.assertEquals(1, state.getReincarnationAmountOfTiXiu());
        Assert.assertEquals("天道酬勤，成功突破（练气境1层->练气境2层）", state.getQiXiuUpgradeMsg());
        Assert.assertEquals("天道酬勤，成功突破（通脉境1层->通脉境2层）", state.getTiXiuUpgradeMsg());
        Assert.assertEquals(1, state.getYuanLi());
        Assert.assertEquals(1, state.getQiLi());
        Assert.assertEquals(1, state.getTiLi());
        qiXiuState = state.getQiXiuState();
        Assert.assertEquals(LianQiStateEnum.LIAN_QI, qiXiuState.getState());
        Assert.assertEquals(2, qiXiuState.getLevel());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed(), qiXiuState.getUpgradeNeedQiLi());
        Assert.assertEquals(LianQiStateEnum.LIAN_QI.getUpgradeNeed() / 2, qiXiuState.getUpgradeNeedYuanLi());
        tiXiuState = state.getTiXiuState();
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI, tiXiuState.getState());
        Assert.assertEquals(2, tiXiuState.getLevel());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed(), tiXiuState.getUpgradeNeedTiLi());
        Assert.assertEquals(TiXiuStateEnum.TONG_MAI.getUpgradeNeed() / 2, tiXiuState.getUpgradeNeedYuanLi());

        // 进阶
        Mockito.when(calService.getPeriodData(Mockito.any())).thenReturn(DashboardResult.builder()
                .sleepTime(60L * 8)
                .learnTime(60L * 8)
                .runningTime(60L * 8)
                .build());
        state = xiuXianService.yesterdaySettlement();
        Assert.assertEquals(1, state.getReincarnationAmountOfQiXiu());
        Assert.assertEquals(1, state.getReincarnationAmountOfTiXiu());
        Assert.assertEquals("天道酬勤，成功突破（练气境9层->筑基境1层）", state.getQiXiuUpgradeMsg());
        Assert.assertEquals("天道酬勤，成功突破（通脉境9层->锻骨境1层）", state.getTiXiuUpgradeMsg());
        Assert.assertEquals(1, state.getYuanLi());
        Assert.assertEquals(1, state.getQiLi());
        Assert.assertEquals(1, state.getTiLi());
        qiXiuState = state.getQiXiuState();
        Assert.assertEquals(LianQiStateEnum.ZHU_JI, qiXiuState.getState());
        Assert.assertEquals(1, qiXiuState.getLevel());
        Assert.assertEquals(LianQiStateEnum.ZHU_JI.getUpgradeNeed(), qiXiuState.getUpgradeNeedQiLi());
        Assert.assertEquals(LianQiStateEnum.ZHU_JI.getUpgradeNeed() / 2, qiXiuState.getUpgradeNeedYuanLi());
        tiXiuState = state.getTiXiuState();
        Assert.assertEquals(TiXiuStateEnum.DUAN_GU, tiXiuState.getState());
        Assert.assertEquals(1, tiXiuState.getLevel());
        Assert.assertEquals(TiXiuStateEnum.DUAN_GU.getUpgradeNeed(), tiXiuState.getUpgradeNeedTiLi());
        Assert.assertEquals(TiXiuStateEnum.DUAN_GU.getUpgradeNeed() / 2, tiXiuState.getUpgradeNeedYuanLi());

        // 轮回
        Mockito.when(calService.getPeriodData(Mockito.any())).thenReturn(DashboardResult.builder()
                .sleepTime(60L * 800)
                .learnTime(60L * 1200)
                .runningTime(60L * 410)
                .build());
        state = xiuXianService.yesterdaySettlement();
        System.out.println(state);
        Assert.assertEquals(2, state.getReincarnationAmountOfQiXiu());
        Assert.assertEquals(2, state.getReincarnationAmountOfTiXiu());
        Assert.assertEquals("天道酬勤，成功突破（元婴境1层(轮回2)->元婴境2层(轮回2)）", state.getQiXiuUpgradeMsg());
        Assert.assertEquals("天道酬勤，成功突破（内源境1层(轮回2)->内源境2层(轮回2)）", state.getTiXiuUpgradeMsg());
        Assert.assertEquals(121, state.getYuanLi());
        Assert.assertEquals(781, state.getQiLi());
        Assert.assertEquals(61, state.getTiLi());
        qiXiuState = state.getQiXiuState();
        Assert.assertEquals(LianQiStateEnum.YUAN_YING, qiXiuState.getState());
        Assert.assertEquals(2, qiXiuState.getLevel());
        Assert.assertEquals(LianQiStateEnum.YUAN_YING.getUpgradeNeed() * 2, qiXiuState.getUpgradeNeedQiLi());
        Assert.assertEquals(LianQiStateEnum.YUAN_YING.getUpgradeNeed() / 2 * 2, qiXiuState.getUpgradeNeedYuanLi());
        tiXiuState = state.getTiXiuState();
        Assert.assertEquals(TiXiuStateEnum.NEI_YUAN, tiXiuState.getState());
        Assert.assertEquals(2, tiXiuState.getLevel());
        Assert.assertEquals(TiXiuStateEnum.NEI_YUAN.getUpgradeNeed() * 2, tiXiuState.getUpgradeNeedTiLi());
        Assert.assertEquals(TiXiuStateEnum.NEI_YUAN.getUpgradeNeed() / 2 * 2, tiXiuState.getUpgradeNeedYuanLi());
    }
}
