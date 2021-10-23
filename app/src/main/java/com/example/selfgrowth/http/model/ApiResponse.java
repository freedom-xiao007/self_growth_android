package com.example.selfgrowth.http.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {

    private int code;
    private String message;
    private Object data;
    private long total;
    private long index;
    private long size;
}
