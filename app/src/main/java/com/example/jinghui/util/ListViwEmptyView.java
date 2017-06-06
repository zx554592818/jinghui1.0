package com.example.jinghui.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by zhaoxin on 2017-04-27.
 */

public class ListViwEmptyView {
    //这个类用来操作空态   这么设置可以保证空态有时候不显示
    public static void setEmptyView(ListView listview, View emptyView) {
        FrameLayout emptyLayout;
        if (listview.getEmptyView() == null) {
            emptyLayout = new FrameLayout(listview.getContext());
            emptyLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            emptyLayout.addView(emptyView);
            emptyView.setVisibility(View.VISIBLE);
            getParentView((ViewGroup) listview.getParent()).addView(emptyLayout);
            listview.setEmptyView(emptyLayout);
        } else {
            emptyLayout = (FrameLayout) listview.getEmptyView();
            emptyLayout.removeAllViews();
            emptyLayout.setVisibility(View.VISIBLE);
            emptyLayout.addView(emptyView);
        }
    }

    private static ViewGroup getParentView(ViewGroup parent) {
        ViewGroup tempVg = parent;
        if (parent.getParent() != null && parent.getParent() instanceof ViewGroup) {
            tempVg = (ViewGroup) parent.getParent();
            getParentView(tempVg);
        } else {
            return tempVg;
        }
        return tempVg;
    }
}
