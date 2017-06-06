package com.example.jinghui.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jinghui.R;

/**
 * Created by zhaoxin on 2017-05-06.
 */

public class SelectPlaceDialogBuilder extends AlertDialog.Builder {
    public Refresh refresh;
    private WindowManager windowManager;
    private Activity context;

    public SelectPlaceDialogBuilder(Activity context) {
        //这里不能写成单利  因为对话框是依赖于Activity的 不同的Activity 是不同的上下文
        //注意一点 对话框的上下文必须要传Activity
        super(context);
        this.context = context;

    }

    public SelectPlaceDialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setDialog(final String[] monitorData, final View view) {
        AlertDialog alertDialog = setIcon(R.mipmap.dizhi).setTitle("选择").setItems(monitorData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv = (TextView) view;
                tv.setText(monitorData[which]);
            }
        }).create();
        alertDialog.show();  //对话框要先show()  show完才能设置大小
        windowManager = context.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = (int) (d.getHeight() * 0.5);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        alertDialog.getWindow().setAttributes(params);

    }

    public void setHomePageDialog(final String[] monitorData, final View view) {
        AlertDialog alertDialog = setIcon(R.mipmap.dizhi)
                .setTitle("选择").
                        setItems(monitorData, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv = (TextView) view;
                                tv.setText(monitorData[which]);
                                refresh.complete();
                            }
                        }).create();
        alertDialog.show();
        windowManager = context.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = (int) (d.getHeight() * 0.5);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        alertDialog.getWindow().setAttributes(params);
    }

    //回调   通知实现该接口的地方  执行这个接口的方法
    public interface Refresh {
        void complete();
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }

}
