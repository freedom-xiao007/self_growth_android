package com.example.selfgrowth.service.backend.xiuxian;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.db.SharedPreferencesDb;
import com.example.selfgrowth.enums.LianQiStateEnum;
import com.example.selfgrowth.enums.TiXiuStateEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.XiuXianState;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.GsonUtils;

import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

public class XiuXianService {

    private final static XiuXianService instance = new XiuXianService();
    private SharedPreferencesDb xiuXianDb;
    private final String stateKey = "state";
    private CalService calService;

    public static XiuXianService getInstance() {
        return instance;
    }

    public void init(final Context applicationContext, CalService dashboardService) {
        xiuXianDb = new SharedPreferencesDb(applicationContext, "XIU_XIAN");
        this.calService = dashboardService;
    }

    public void setXiuXianDb(SharedPreferencesDb xiuXianDb) {
        this.xiuXianDb = xiuXianDb;
    }

    public void setCalService(CalService calService) {
        this.calService = calService;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public XiuXianState yesterdaySettlement() {
        String stateStr = xiuXianDb.getString(stateKey, "{}");
        XiuXianState state = GsonUtils.getInstance().fromJson(stateStr, XiuXianState.class);
        state.setQiXiuUpgradeMsg("");
        state.setTiXiuUpgradeMsg("");
        if (state.getQiXiuState() == null) {
            state.setQiXiuState(new XiuXianState.LianQiState(LianQiStateEnum.LIAN_QI, 1,
                    LianQiStateEnum.LIAN_QI.getUpgradeNeed(), LianQiStateEnum.LIAN_QI.getUpgradeNeed() / 2));
        }
        if (state.getTiXiuState() == null) {
            state.setTiXiuState(new XiuXianState.TiXiuState(TiXiuStateEnum.TONG_MAI, 1,
                    TiXiuStateEnum.TONG_MAI.getUpgradeNeed(), TiXiuStateEnum.TONG_MAI.getUpgradeNeed() / 2));
        }

        String calYesterdayData = String.format("calYesterdayData:%s", DateUtils.toCustomDay(new Date()));
        if (xiuXianDb.getBoolean(calYesterdayData)) {
            System.out.println("已进行过升级模拟，直接取今日数据");
            return state;
        }

        state.setQiXiuUpgradeMsg("");
        state.setTiXiuUpgradeMsg("");
        addYesterdayData(state);

        updateUpgradeNeed(state);

        xiuXianDb.save(stateKey, GsonUtils.getInstance().toJson(state));
        xiuXianDb.save(calYesterdayData, true);
        return state;
    }

    private void updateUpgradeNeed(XiuXianState state) {
        int tiLiNeed = state.getTiXiuState().getState().getUpgradeNeed() * state.getReincarnationAmountOfTiXiu();
        int yuanLiNeed = tiLiNeed / 2;
        state.getTiXiuState().setUpgradeNeedTiLi(tiLiNeed);
        state.getTiXiuState().setUpgradeNeedYuanLi(yuanLiNeed);

        int qiLiNeed = state.getQiXiuState().getState().getUpgradeNeed() * state.getReincarnationAmountOfQiXiu();
        yuanLiNeed = qiLiNeed / 2;
        state.getQiXiuState().setUpgradeNeedQiLi(qiLiNeed);
        state.getQiXiuState().setUpgradeNeedYuanLi(yuanLiNeed);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addYesterdayData(XiuXianState state) {
        System.out.println("进行升级模拟");
        DashboardResult res = calService.getPeriodData(new Date());
        state.setYuanLi(state.getYuanLi() + res.getSleepTime());
        state.setQiLi(state.getQiLi() + res.getLearnTime());
        state.setTiLi(state.getTiLi() + res.getRunningTime());
        while (tiXiuUpgrade(state));
        while (qiXiuUpgrade(state));
    }

    private boolean tiXiuUpgrade(XiuXianState state) {
        long tiLiNeed = state.getTiXiuState().getUpgradeNeedTiLi();
        long yuanLiNeed = tiLiNeed / 2;
        if (tiLiNeed > state.getTiLi() || yuanLiNeed > state.getYuanLi()) {
            System.out.printf(Locale.CHINA, "体修突破，资源不足：需要体力和元力： %d %d, 当前：%d %d%n", tiLiNeed, yuanLiNeed, state.getTiLi(), state.getYuanLi());
            if (state.getTiXiuUpgradeMsg().isEmpty()) {
                state.setTiXiuUpgradeMsg("体修：资源不足，无法突破，请努力修炼");
            }
            return false;
        }

        System.out.printf(Locale.CHINA, "%s(轮回%d) %d 体修突破成功：需要体力和元力： %d %d, 当前：%d %d%n",
                state.getTiXiuState().getState().getName(), state.getReincarnationAmountOfTiXiu(), state.getTiXiuState().getLevel(),
                tiLiNeed, yuanLiNeed, state.getTiLi(), state.getYuanLi());
        XiuXianState.TiXiuState statePre = state.getTiXiuState();
        String preName = statePre.getState().getName();
        int preLevel = statePre.getLevel();
        int preRe = state.getReincarnationAmountOfTiXiu();

        state.setTiLi(state.getTiLi() - tiLiNeed);
        state.setYuanLi(state.getYuanLi() - yuanLiNeed);
        state.getTiXiuState().setLevel(state.getTiXiuState().getLevel() + 1);
        if (state.getTiXiuState().getLevel() >= state.getTiXiuState().getState().getMaxState()) {
            if (state.getTiXiuState().getState().equals(TiXiuStateEnum.DAO_TI)) {
                state.setReincarnationAmountOfTiXiu(state.getReincarnationAmountOfTiXiu() + 1);
                state.getTiXiuState().setState(TiXiuStateEnum.TONG_MAI);
                state.getTiXiuState().setLevel(TiXiuStateEnum.TONG_MAI.getMinState());
            } else {
                state.getTiXiuState().setLevel(TiXiuStateEnum.getFromIndex(state.getTiXiuState().getState().getIndex() + 1).getMinState());
                state.getTiXiuState().setState(TiXiuStateEnum.getFromIndex(state.getTiXiuState().getState().getIndex() + 1));
            }
        }

        if (state.getReincarnationAmountOfTiXiu() == 1) {
            state.setTiXiuUpgradeMsg(String.format(Locale.CHINA, "天道酬勤，成功突破（%s%d层->%s%d层）",
                    preName, preLevel,
                    state.getTiXiuState().getState().getName(), state.getTiXiuState().getLevel()));
        } else {
            state.setTiXiuUpgradeMsg(String.format(Locale.CHINA, "天道酬勤，成功突破（%s%d层(轮回%d)->%s%d层(轮回%d)）",
                    preName, preLevel, preRe,
                    state.getTiXiuState().getState().getName(), state.getTiXiuState().getLevel(), state.getReincarnationAmountOfTiXiu()));
        }

        updateUpgradeNeed(state);
        return tiXiuUpgrade(state);
    }

    private boolean qiXiuUpgrade(XiuXianState state) {
        long qiLiNeed = state.getQiXiuState().getUpgradeNeedQiLi();
        long yuanLiNeed = qiLiNeed / 2;
        if (qiLiNeed > state.getQiLi() || yuanLiNeed > state.getYuanLi()) {
            System.out.printf(Locale.CHINA, "气修突破，资源不足：需要气力和元力： %d %d, 当前：%d %d%n", qiLiNeed, yuanLiNeed, state.getQiLi(), state.getYuanLi());
            if (state.getQiXiuUpgradeMsg().isEmpty()) {
                state.setQiXiuUpgradeMsg("气修：资源不足，无法突破，请努力修炼");
            }
            return false;
        }

        System.out.printf(Locale.CHINA, "%s(轮回%d) %d 气修突破成功：需要气力和元力： %d %d, 当前：%d %d%n",
                state.getQiXiuState().getState().getName(), state.getReincarnationAmountOfQiXiu(), state.getQiXiuState().getLevel(),
                qiLiNeed, yuanLiNeed, state.getQiLi(), state.getYuanLi());
        XiuXianState.LianQiState statePre = state.getQiXiuState();
        String preName = statePre.getState().getName();
        int preLevel = statePre.getLevel();
        int preRe = state.getReincarnationAmountOfQiXiu();

        state.setQiLi(state.getQiLi() - qiLiNeed);
        state.setYuanLi(state.getYuanLi() - yuanLiNeed);
        state.getQiXiuState().setLevel(state.getQiXiuState().getLevel() + 1);
        if (state.getQiXiuState().getLevel() >= state.getQiXiuState().getState().getMaxState()) {
            if (state.getQiXiuState().getState().equals(LianQiStateEnum.DA_DAO)) {
                state.setReincarnationAmountOfQiXiu(state.getReincarnationAmountOfQiXiu() + 1);
                state.getQiXiuState().setState(LianQiStateEnum.LIAN_QI);
                state.getQiXiuState().setLevel(LianQiStateEnum.LIAN_QI.getMinState());
            } else {
                state.getQiXiuState().setLevel(LianQiStateEnum.getFromIndex(state.getQiXiuState().getState().getIndex() + 1).getMinState());
                state.getQiXiuState().setState(LianQiStateEnum.getFromIndex(state.getQiXiuState().getState().getIndex() + 1));
            }
        }

        if (state.getReincarnationAmountOfQiXiu() == 1) {
            state.setQiXiuUpgradeMsg(String.format(Locale.CHINA, "天道酬勤，成功突破（%s%d层->%s%d层）",
                    preName, preLevel,
                    state.getQiXiuState().getState().getName(), state.getQiXiuState().getLevel()));
        } else {
            state.setQiXiuUpgradeMsg(String.format(Locale.CHINA, "天道酬勤，成功突破（%s%d层(轮回%d)->%s%d层(轮回%d)）",
                    preName, preLevel, preRe,
                    state.getQiXiuState().getState().getName(), state.getQiXiuState().getLevel(), state.getReincarnationAmountOfQiXiu()));
        }

        updateUpgradeNeed(state);
        return qiXiuUpgrade(state);
    }
}
