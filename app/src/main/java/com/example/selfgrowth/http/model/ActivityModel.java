package com.example.selfgrowth.http.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityModel {

    private String application;
    private String name;
    private Integer times;
}
