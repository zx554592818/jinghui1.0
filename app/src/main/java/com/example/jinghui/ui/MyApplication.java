package com.example.jinghui.ui;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.baidu.lbsapi.MKGeneralListener;

import com.baidu.lbsapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;

import com.example.jinghui.model.HistoryDatabaseEntity;

import java.util.ArrayList;

/**
 * Created by zhaoxin on 2017-04-07.
 */

public class MyApplication extends Application {
    public static MyApplication myApplication;
   // public final String DEBUG_BASE_URL = "http://12345fish.xicp.net:28333"; //服务器地址
    // public final String DEBUG_BASE_URL = "http://192.168.1.74:8080";      //本地测试地址
    public final String DEBUG_BASE_URL="http://jhhbkj.vicp.io:28334";
    private ArrayList<HistoryDatabaseEntity> sendData;
    private static Context mContext;
    public BMapManager mBMapManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        if (!"generic".equalsIgnoreCase(Build.BRAND)) {
            SDKInitializer.initialize(this);
        }
        initEngineManager(this);
        mContext = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public void setData(ArrayList<HistoryDatabaseEntity> sendData) {
        this.sendData = sendData;
    }

    public ArrayList<HistoryDatabaseEntity> getSendData() {
        return sendData;
    }


    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetPermissionState(int iError) {

            if (iError != 0) {
            } else {
            }
        }
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(
                    MyApplication.getInstance().getApplicationContext(),
                    "BMapManager!", Toast.LENGTH_LONG).show();
        }
    }
}
