package com.example.selfgrowth.http.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    private String packageName;
    private String appName;
    private String label;
}
