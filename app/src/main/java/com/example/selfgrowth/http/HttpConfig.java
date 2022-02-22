package com.example.selfgrowth.http;

public class HttpConfig {

    private static String address = "http://127.0.0.1:8080/";

    public static void setServerUrl(String url) {
        address = url;
    }

    public static String getServerUrl() {
        return address;
    }
}
