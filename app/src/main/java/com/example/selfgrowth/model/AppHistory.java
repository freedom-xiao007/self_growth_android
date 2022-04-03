package com.example.selfgrowth.model;

import android.content.SharedPreferences;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * todo
 * 需要进行重构，相关应用标签更新函数的整合
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppHistory {

    private String packageName;
    private String appName;
    private String label;
    private Date createTime;

    public void setLabel(final SharedPreferences preferences) {
        this.label = preferences.getString(appName, "其他");
    }

    public void updateLabel(final SharedPreferences preferences, final String label) {
        this.label = label;
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(appName, label);
        editor.apply();
    }
}
