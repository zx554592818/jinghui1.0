package com.example.jinghui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.jinghui.util.CleanCacheUtil;
import com.example.jinghui.R;

/**
 * Created by zhaoxin on 2017-05-23.
 */

public class InstallActivity extends BaseActivity{
    //设置
    private TextView numCache,visionname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        initView();
        setVisionName();
    }

    private void initView() {
        numCache = (TextView) findViewById(R.id.numCache);
        visionname= (TextView) findViewById(R.id.visionName);

        setCacheMessage();
    }

    private void setCacheMessage() {
        //获取缓存
        try {
            String totalCacheSize = CleanCacheUtil.getTotalCacheSize(getApplicationContext());
            numCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void installBack(View view) {
        finish();
    }


    public void cleanCache(View view) {
        CleanCacheUtil.clearAllCache(getApplicationContext()); //清除缓存
        //清除掉之后再获取剩下的缓存设置
        try {
            String totalCacheSize = CleanCacheUtil.getTotalCacheSize(getApplicationContext());
            numCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }
    private  void setVisionName() {
        visionname.setText(getVersionName());
    }
}
