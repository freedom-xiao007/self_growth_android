package com.example.selfgrowth.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActivityRecordModel {

    private Date date;
    private String activity;
    private String application;
}
