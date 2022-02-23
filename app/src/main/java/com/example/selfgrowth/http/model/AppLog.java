package com.example.selfgrowth.http.model;

import java.util.Comparator;
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
public class AppLog implements Comparator<AppLog> {

    private Date date;
    private String packageName;

    @Override
    public int compare(AppLog o1, AppLog o2) {
        return o2.getDate().after(o1.getDate()) ? 1 : -1;
    }
}
