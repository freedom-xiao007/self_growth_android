package com.example.selfgrowth.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskSyncData {

    private List<TaskConfig> newTask;
    private List<TaskDeleteLog> deleteLogs;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskDeleteLog {

        private String id;
        private String taskId;
        private Long userId;
        private String name;
        private String group;
    }
}