package com.example.jinghui.util;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * Created by 赵欣 on 2017/3/6.
 */

public class MyOkHttpClient extends OkHttpClient {
    public static MyOkHttpClient myOkHttpClient;

    public MyOkHttpClient(Context context) {

    }

    public static MyOkHttpClient getOkHttpClient(Context context) {
        if (myOkHttpClient == null) {
            myOkHttpClient = new MyOkHttpClient(context);
        }
        return myOkHttpClient;
    }
}