package com.example.createnewproject2;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.litepal.LitePal;

/**
 * Create by 张瀛煜 on 2020-06-08 ：）
 */
public class AppClient extends Application {
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    public static void add(JsonObjectRequest jsonObjectRequest) {
        requestQueue.add(jsonObjectRequest);
    }
}
