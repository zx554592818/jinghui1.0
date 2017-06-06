package com.example.jinghui.fragment.all.air;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.jinghui.activity.air.AirStatementAllActivity;
import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.ui.DateTimePickerDialogMyself;
import com.example.jinghui.R;
import com.example.jinghui.ui.SlideBannerViewPager;
import com.example.jinghui.model.AppSimpleData;
import com.example.jinghui.util.DateTimeFormatUtil;
import com.example.liangmutian.mypicker.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵欣 on 2017/3/1.
 */

public class MonitorAllFragement extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private View monitor_all_view;
    private AppSimpleData getData = null;
    private Spinner observation_sp, type_sp;
    private Button inquireAll_btn;
    private EditText monitorAllDate, monitorAllTime;
    private String SubMonitorAllTitle, SubMonitorAllType;
    private List<ImageView> pagerView;
    private SlideBannerViewPager slideBannerViewPager;
    private int[] image;
    private MyApplication myApplication;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        monitor_all_view = inflater.inflate(R.layout.layout_all_airmonitor_, null, false);
        initView();
        initViewData();
        //暂时就这样  后面优化
        observation_sp.setOnItemSelectedListener(this);
        type_sp.setOnItemSelectedListener(this);

        //日期输出选择框

        monitorAllDate.setOnClickListener(this);
        monitorAllTime.setOnClickListener(this);
        inquireAll_btn.setOnClickListener(this);
        return monitor_all_view;
    }

    private void initView() {
        myApplication = MyApplication.getInstance();
        if (getData == null) {
            getData = new AppSimpleData();
        }
        //初始化时间选择   设定初始时间
        monitorAllDate = (EditText) monitor_all_view.findViewById(R.id.monitorAllDate);
        monitorAllDate.setText(DateTimeFormatUtil.lastDay());
        monitorAllTime = (EditText) monitor_all_view.findViewById(R.id.monitorAllTime);
        monitorAllTime.setText(DateTimeFormatUtil.currentDayTime());

        inquireAll_btn = (Button) monitor_all_view.findViewById(R.id.inquireAll_btn);
        observation_sp = (Spinner) monitor_all_view.findViewById(R.id.observation_sp);
        type_sp = (Spinner) monitor_all_view.findViewById(R.id.type_sp);

        slideBannerViewPager = (SlideBannerViewPager) monitor_all_view.findViewById(R.id.viewPager);
    }

    private void initViewData() {
        image = new AppSimpleData().getImage();
        if (pagerView == null) {
            pagerView = new ArrayList<ImageView>();
        }
        for (int imgId : image) {
            ImageView imageView = new ImageView(myApplication);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            pagerView.add(imageView);
        }
        slideBannerViewPager.setPages(pagerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inquireAll_btn:
                //数据库的时间 特别是小时时间  分和秒都是00 如果不是00：00 就获取不到指定数据   将分和秒转为00：00
                String dateTime = monitorAllDate.getText().toString() + " " + monitorAllTime.getText().toString();
                Intent intent = new Intent(getActivity(), AirStatementAllActivity.class);
                intent.putExtra("SendData", new String[]{SubMonitorAllTitle, SubMonitorAllType, dateTime});
                startActivity(intent);
                break;
            case R.id.monitorAllDate:
                new DateTimePickerDialogMyself(getActivity()).showDateDialog(DateUtil.getDateForString(DateUtil.getToday()), monitorAllDate);
                break;
            case R.id.monitorAllTime:
                new DateTimePickerDialogMyself(getActivity()).showTimePick(monitorAllTime);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.observation_sp:
                String subMonitorAllTitle = (String) observation_sp.getSelectedItem();
                SubMonitorAllTitle = subMonitorAllTitle;
                break;
            case R.id.type_sp:
                String subMonitorAllType = (String) type_sp.getSelectedItem();
                SubMonitorAllType = subMonitorAllType;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
