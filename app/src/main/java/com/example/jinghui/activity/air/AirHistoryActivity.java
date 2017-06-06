package com.example.jinghui.activity.air;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
import com.example.jinghui.adapter.FragmentAdapter;
import com.example.jinghui.fragment.all.air.AirPressureFragment;
import com.example.jinghui.fragment.all.air.DustFragment;
import com.example.jinghui.fragment.all.air.HumidityFragment;
import com.example.jinghui.fragment.all.air.TemperatureFragment;
import com.example.jinghui.fragment.all.air.WindDirectionFragment;
import com.example.jinghui.fragment.all.air.WindSpeedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2017-05-25.
 */

public class AirHistoryActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup history_btn_container;
    private DustFragment dustFragment = null;
    private WindSpeedFragment windSpeedFragment = null;
    private WindDirectionFragment windDirectionFragment = null;
    private AirPressureFragment airPressureFragment = null;
    private TemperatureFragment temperatureFragment = null;
    private HumidityFragment humidityFragment = null;
    private TextView select_place, historyType;
    private String[] receiveData;
    private String monitor, timeType;
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentAdapter adapter;
    private View view;
    int width; //屏幕宽度
    boolean isScroll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_historry);
        initView();
        initFragment();
        history_btn_container.setOnCheckedChangeListener(this);
        //viewPager的滑动监听w
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //这个回调是滑动被停止前一直会调用的   即停止滑动的时候这个是不会调用的
                //position :当前页面，及你点击滑动的页面
                // positionOffset:当前页面偏移的百分比
                //positionOffsetPixels:当前页面偏移的像素位置
                Log.d("sss", position + "");
                if (isScroll) {
                    view.setTranslationX(width*position);
                }

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("sss", position + "" + "onPageSelected");
                //跳转完成后调用这个方法  跳转不完成不走
                RadioButton rb = (RadioButton) history_btn_container.getChildAt(position);
                rb.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //手指放上来开始拖动时 先执性state=1 然后开始执行onPageScrolled   收拾放开的时候会执行state=2  如果翻页成功了则会走onPageSlected  最后无论如何都会走一次state=0
                if (state == 0) {
                    //什么都没做
                    Log.d("sss", state + "什么都没做");
                } else if (state == 1) {
                    //开始滑动
                    Log.d("sss", state + "开始滑动");
                    isScroll = true;
                } else if (state == 2) {
                    //停止滑动
                    Log.d("sss", state + "停止滑动");
                }

            }
        });
        //使用回调   dustFragment里面加载完数据才更新文本
        dustFragment.set(new DustFragment.Load() {
            @Override
            public void complete() {
                select_place.setText(monitor);
                historyType.setText(timeType);
            }
        });
    }

    private void initView() {
        width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        receiveData = getIntent().getStringArrayExtra("SendData");
        monitor = receiveData[0];
        timeType = receiveData[1];
        fragmentManager = getSupportFragmentManager();
        historyType = (TextView) findViewById(R.id.historyType);
        select_place = (TextView) findViewById(R.id.select_place);
        history_btn_container = (RadioGroup) findViewById(R.id.history_btn_container);
        //使用ViewPager让Fragment实现侧滑
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        view = findViewById(R.id.view);
    }

    public void airHistoryBack(View view) {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (checkedId) {
            case R.id.dust_btn:
                viewPager.setCurrentItem(0);
                break;
            case R.id.wind_speed_btn:
                viewPager.setCurrentItem(1);
                break;
            case R.id.wind_direction_btn:
                viewPager.setCurrentItem(2);
                break;
            case R.id.airpressure_btn:
                viewPager.setCurrentItem(3);
                break;
            case R.id.temperature_btn:
                viewPager.setCurrentItem(4);
                break;
            case R.id.humidity_btn:
                viewPager.setCurrentItem(5);
                break;
        }
        fragmentTransaction.commit();
    }

    private void initFragment() {
        dustFragment = new DustFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("SendData0", receiveData);
        dustFragment.setArguments(bundle);

        windSpeedFragment = new WindSpeedFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putStringArray("SendData0", receiveData);
        windSpeedFragment.setArguments(bundle1);

        windDirectionFragment = new WindDirectionFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("SendData0", receiveData);
        windDirectionFragment.setArguments(bundle2);


        airPressureFragment = new AirPressureFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("SendData0", receiveData);
        airPressureFragment.setArguments(bundle3);


        temperatureFragment = new TemperatureFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("SendData0", receiveData);
        temperatureFragment.setArguments(bundle4);

        humidityFragment = new HumidityFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putSerializable("SendData0", receiveData);
        humidityFragment.setArguments(bundle5);

        fragments.add(dustFragment);
        fragments.add(windSpeedFragment);
        fragments.add(windDirectionFragment);
        fragments.add(airPressureFragment);
        fragments.add(temperatureFragment);
        fragments.add(humidityFragment);
        adapter = new FragmentAdapter(fragmentManager, fragments);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(adapter);
    }
}
