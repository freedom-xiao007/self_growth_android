package com.example.selfgrowth.http.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatistics {

    Map<String, DashboardGroup> groups;

    @Data
    @Builder
    public static class DashboardGroup {

        private String name;
        private int minutes;
        private List<DashboardApp> apps;
    }

    @Data
    @Builder
    public static class DashboardApp {
        private String name;
        private int minutes;
    }
}
