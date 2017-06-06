package com.example.jinghui.fragment.all.air;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.jinghui.ui.FragmentManagerMyself;
import com.example.jinghui.adapter.FragmentAdapter;
import com.example.jinghui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵欣 on 2017/3/1.
 */

public class AirMonitorStatementFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private View view;
    private MonitorAllFragement monitorAllFragement = null;
    private MonitorSingleFragment monitorSingleFragment = null;
    private RadioGroup monitor_container;
    private FragmentManager childFragmentManager;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_statement_airmonitor, null, false);
        initView();
        initFragment();
        //viewPager的滑动监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                RadioButton rb = (RadioButton) monitor_container.getChildAt(position);
                rb.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        monitor_container.setOnCheckedChangeListener(this);
        return view;
    }

    private void initView() {
        childFragmentManager = getChildFragmentManager();
        monitor_container = (RadioGroup) view.findViewById(R.id.monitor_container);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    private void initFragment() {
        monitorAllFragement = new MonitorAllFragement();
        monitorSingleFragment = new MonitorSingleFragment();
        fragments.add(monitorAllFragement);
        fragments.add(monitorSingleFragment);
        //将这2个fragment对象添加i到一个操作类 方便取出来
        FragmentManagerMyself.getInstance().set(fragments);
        adapter = new FragmentAdapter(childFragmentManager, fragments);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        switch (checkedId) {
            case R.id.monitor_all:
                viewPager.setCurrentItem(0);
                fragmentTransaction.show(monitorAllFragement);
                break;
            case R.id.monitor_single:
                viewPager.setCurrentItem(1);
                fragmentTransaction.show(monitorSingleFragment);
                break;
        }
        fragmentTransaction.commit();
    }
}
