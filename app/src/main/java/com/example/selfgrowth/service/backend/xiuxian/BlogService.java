package com.example.selfgrowth.service.backend.xiuxian;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashSet;
import java.util.Set;

public class BlogService {

    private final static BlogService instance = new BlogService();
    private SharedPreferences db;
    private final String blogDb = "blogDb";
    private final String blogsKey = "blogsKey";

    public static BlogService getInstance() {
        return instance;
    }

    public void initSharedPreferences(Context applicationContext) {
        this.db = applicationContext.getSharedPreferences(blogDb, Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addBlog(final String title, final String url) {
        final String newBlog = String.join("::", title, url);
        Set<String> blogs = db.getStringSet(blogsKey, new HashSet<>(0));
        blogs.add(newBlog);
        db.edit().putStringSet(blogsKey, blogs).apply();
    }
}
