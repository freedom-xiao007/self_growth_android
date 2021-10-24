package com.example.selfgrowth.http.model;

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
    private String label;
    private int cycleType;
    private int type;
}
