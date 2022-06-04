package com.example.selfgrowth.cache;

public class UserCache {

    private String userName = null;
    private String key = null;
    private String token = null;
    private boolean login = false;
    private static final UserCache instance = new UserCache();

    public static UserCache getInstance() {
        return instance;
    }

    public void initUser(String userName, String token) {
        this.userName = userName;
        this.token = token;
        this.login = true;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    public boolean isLogin() {
        return this.login;
    }

    public void setAuth(String key, String token) {
        this.key = key;
        this.token = token;
    }
}
