package com.example.selfgrowth.ui.dashboard;

import com.example.selfgrowth.enums.LabelEnum;

public class DailyLogModel {

    private String message;
    private String date;
    private DailyLogType type;
    private LabelEnum label;

    public DailyLogModel(String message, String date, DailyLogType type, String label) {
        this.message = message;
        this.date = date;
        this.type = type;
        this.label = LabelEnum.fromString(label);
    }

    public DailyLogModel(String message, String date, DailyLogType type, LabelEnum label) {
        this.message = message;
        this.date = date;
        this.type = type;
        this.label = label;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public DailyLogType getType() {
        return type;
    }

    public LabelEnum getLabel() {
        return label;
    }

    public enum DailyLogType {
        ACTIVITY_BEGIN,
        ACTIVITY_END,
        TASK_COMPLETE,
    }
}
