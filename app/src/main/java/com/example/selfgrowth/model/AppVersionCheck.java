package com.example.selfgrowth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVersionCheck {

    private Boolean latest;
    private String downloadUrl;
    private String updateMsg;
}
