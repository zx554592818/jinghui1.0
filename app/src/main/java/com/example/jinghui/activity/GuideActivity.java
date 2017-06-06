package com.example.jinghui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.jinghui.R;
import com.sam.widget.guide_view.simple.SimpleGuideView;

/**
 * Created by 赵欣 on 2017/2/25.
 */

public class GuideActivity extends BaseActivity{
    private SimpleGuideView simpleGuideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);
        initView();

        simpleGuideView.setOnPageChangeListener(new SimpleGuideView.OnPageChangeListener() {
            @Override
            public void onPageChange(int i) {


            }

            @Override
            public void onLastPageSwipe(int i) {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));

            }
        });
    }

    private void initView() {
        simpleGuideView = (SimpleGuideView) findViewById(R.id.guide_view);
    }
}
