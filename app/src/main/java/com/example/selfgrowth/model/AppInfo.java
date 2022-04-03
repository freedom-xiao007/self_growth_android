package com.example.selfgrowth.model;

import android.content.SharedPreferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    public static final String APP_INFO = "app_info";
    private String packageName;
    private String appName;
    private String label;

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
