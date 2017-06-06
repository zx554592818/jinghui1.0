package com.example.jinghui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhaoxin on 2017-05-24.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //不等于空等于是杀死了activity重新创建的  这个时候直接重启
        if (savedInstanceState != null) {
            final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  设置竖屏
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
    }
}
