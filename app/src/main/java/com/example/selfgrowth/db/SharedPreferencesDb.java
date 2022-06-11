package com.example.selfgrowth.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.selfgrowth.utils.GsonUtils;

public class SharedPreferencesDb {

    private final SharedPreferences db;

    public SharedPreferencesDb(Context applicationContext, String dbName) {
        this.db = applicationContext.getSharedPreferences(dbName, Context.MODE_PRIVATE);
    }

    public String getString(String key, String defaultVal) {
        return db.getString(key, defaultVal);
    }

    public Boolean getBoolean(String key) {
        return db.getBoolean(key, false);
    }

    public void save(String stateKey, String val) {
        db.edit().putString(stateKey, GsonUtils.getInstance().toJson(val)).apply();
    }

    public void save(String stateKey, Boolean bool) {
        db.edit().putBoolean(stateKey, bool).apply();
    }
}
