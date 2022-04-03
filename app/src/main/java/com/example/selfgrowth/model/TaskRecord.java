package com.example.selfgrowth.model;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRecord {

    private String id;
    private Date completeDate;
    private String name;
    private String description;
    private LabelEnum label;
    private TaskCycleEnum cycleType;
    private TaskTypeEnum taskType;
}
