package com.example.selfgrowth.http.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用使用日志
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppLog {

    private Date date;
    private String packageName;
}
