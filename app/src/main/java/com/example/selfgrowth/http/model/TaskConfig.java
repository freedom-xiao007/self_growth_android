package com.example.selfgrowth.http.model;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.enums.TaskLearnTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskConfig {

    private String id;
    private String name;
    private String description;
    private LabelEnum label;
    private TaskCycleEnum cycleType;
    private TaskLearnTypeEnum learnType;
    private String group;
    private TaskTypeEnum taskTypeEnum;
    private Boolean isComplete;
    private Date completeDate;
}
