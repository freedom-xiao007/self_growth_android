package com.example.selfgrowth.cache;

public class UserCache {

    private String userName = null;
    private static final UserCache instance = new UserCache();

    public static UserCache getInstance() {
        return instance;
    }

    public void initUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
