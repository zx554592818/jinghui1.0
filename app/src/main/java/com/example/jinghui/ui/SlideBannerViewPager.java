package com.example.jinghui.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by zhaoxin on 2017-04-24.
 */
//自定义轮播图控件
public class SlideBannerViewPager extends RelativeLayout {

    private List<ImageView> pages;
    private ViewPager viewPager;

    public SlideBannerViewPager(Context context) {
        this(context, null);
    }

    public SlideBannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewPager = new ViewPager(getContext());
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(viewPager);
    }

    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }
    }

    private static final int MSG_CHANGE_PAGE = 101;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CHANGE_PAGE) {
                viewPager.setCurrentItem((Integer) msg.obj);
            }
        }
    };

    // ==========================================================


    public List<ImageView> getPages() {
        return pages;
    }

    public void setPages(final List<ImageView> pages) {
        this.pages = pages;
        viewPager.setAdapter(new BannerAdapter());
        /*Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                int index = (viewPager.getCurrentItem() + 1) % pages.size();
                handler.obtainMessage(MSG_CHANGE_PAGE,index).sendToTarget();
            }
        },1,3, TimeUnit.SECONDS);*/
        new AutoScrollThread().start();
    }

    class AutoScrollThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = (viewPager.getCurrentItem() + 1) % pages.size();
                handler.obtainMessage(MSG_CHANGE_PAGE, index).sendToTarget();
            }
        }
    }
}