package com.example.selfgrowth.http.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 仪表盘统计
 *
 * key : 目前有分组 总览、学习、运动、睡觉、任务
 *     总览 对其他的分支进行了归并
 */
@Data
@Builder
public class DashboardStatistics {

    /**
     * 分组和分组对应的统计
     */
    Map<String, DashboardGroup> groups;

    /**
     * 各个分组统计下，具体的APP使用情况
     * 总览：学习、运动等的总情况
     * 学习、运行、睡觉：对应APP总的使用情况
     * 任务：任务统计
     */
    @Data
    @Builder
    public static class DashboardGroup {

        private String name;
        private long minutes;
        private List<DashboardApp> apps;
    }

    /**
     * APP的使用情况
     * 总时候和具体具体的使用时间分布
     */
    @Data
    @Builder
    public static class DashboardApp {
        private String name;
        private long minutes;
        private List<AppUseLog> logs;
    }

    /**
     * 使用时间记录
     * 开始和结束时间节点记录
     */
    @Data
    @Builder
    public static class AppUseLog {
        private Date startTime;
        private Date endTime;
    }
}
