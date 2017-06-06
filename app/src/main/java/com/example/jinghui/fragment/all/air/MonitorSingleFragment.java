package com.example.jinghui.fragment.all.air;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jinghui.activity.air.AirStatementSingleActivity;
import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.ui.DateTimePickerDialogMyself;
import com.example.jinghui.R;
import com.example.jinghui.ui.SlideBannerViewPager;

import com.example.jinghui.ui.SelectPlaceDialogBuilder;
import com.example.jinghui.ui.ToastShow;
import com.example.jinghui.model.AppSimpleData;
import com.example.jinghui.util.DateTimeFormatUtil;
import com.example.liangmutian.mypicker.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵欣 on 2017/3/1.
 */

public class MonitorSingleFragment extends Fragment implements View.OnClickListener {
    private EditText DateStart, DateFinish, TimeStart, TimeFinish;
    private TextView town_tv_single, site_tv_single;
    private View view;
    private String monitortext, monitorSingleType, timeStart, timeFinish;
    private AppSimpleData simpleData = null;
    private Button inquireSingle_btn;
    private Spinner spinner_single;

    private MyApplication myApplication;
    private String[] sendData;
    private List<ImageView> pagerView;
    private SlideBannerViewPager slideBannerViewPager;
    private int[] image;
    private SelectPlaceDialogBuilder selectPlaceDialogBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_single_airmonitor, null, false);
        initView();
        initViewData();
        initTime();
        spinner_single.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monitorSingleType = (String) spinner_single.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void initView() {
        myApplication = MyApplication.getInstance();
        slideBannerViewPager = (SlideBannerViewPager) view.findViewById(R.id.viewPager);
        if (simpleData == null) {
            simpleData = new AppSimpleData();
        }
        spinner_single = (Spinner) view.findViewById(R.id.spinner_single);
        site_tv_single = (TextView) view.findViewById(R.id.site_tv_single);
        inquireSingle_btn = (Button) view.findViewById(R.id.inquireSingle_btn);
        town_tv_single = (TextView) view.findViewById(R.id.town_tv_single);
        //初始化时间选择 控件  设置初始时间
        DateStart = (EditText) view.findViewById(R.id.monitorSingleDateStart);
        DateFinish = (EditText) view.findViewById(R.id.monitorSingleDateFinish);
        TimeStart = (EditText) view.findViewById(R.id.monitorSingleTimeStart);
        TimeFinish = (EditText) view.findViewById(R.id.monitorSingleTimeFinish);

        town_tv_single.setOnClickListener(this);
        site_tv_single.setOnClickListener(this);
        inquireSingle_btn.setOnClickListener(this);
        TimeFinish.setOnClickListener(this);
        TimeStart.setOnClickListener(this);
        DateStart.setOnClickListener(this);
        DateFinish.setOnClickListener(this);
    }

    //这里是给ViewPager添加适配的图片
    private void initViewData() {
        image = simpleData.getImage();
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
            case R.id.town_tv_single:
                selectPlaceDialogBuilder = new SelectPlaceDialogBuilder(getActivity());
                site_tv_single.setText(null);
                //每次点击清空
                monitortext = null;
                String[] townData = simpleData.getTownData();
                selectPlaceDialogBuilder.setDialog(townData, town_tv_single);
                break;
            case R.id.site_tv_single:
                selectPlaceDialogBuilder = new SelectPlaceDialogBuilder(getActivity());
                town_tv_single.setText(null);
                monitortext = null;
                String[] siteDta = simpleData.getSiteDta();
                selectPlaceDialogBuilder.setDialog(siteDta, site_tv_single);
                break;
            case R.id.monitorSingleDateStart:
                new DateTimePickerDialogMyself(getActivity()).showDateDialog(DateUtil.getDateForString(DateUtil.getToday()), DateStart);
                break;
            case R.id.monitorSingleDateFinish:
                new DateTimePickerDialogMyself(getActivity()).showDateDialog(DateUtil.getDateForString(DateUtil.getToday()), DateFinish);
                break;
            case R.id.monitorSingleTimeStart:
                new DateTimePickerDialogMyself(getActivity()).showTimePick(TimeStart);
                break;
            case R.id.monitorSingleTimeFinish:
                new DateTimePickerDialogMyself(getActivity()).showTimePick(TimeFinish);
                break;
            case R.id.inquireSingle_btn:
                if (!TextUtils.isEmpty(town_tv_single.getText().toString())) {
                    monitortext = town_tv_single.getText().toString();
                } else if (!TextUtils.isEmpty(site_tv_single.getText().toString())) {
                    monitortext = site_tv_single.getText().toString();
                } else {
                    new ToastShow(getActivity()).toastShow("请选择地址");
                }
                if (!TextUtils.isEmpty(monitortext)) {
                    //这里要获取监测点，时间类型，起始时间发送给服务器
                    timeStart = DateStart.getText().toString() + " " + TimeStart.getText().toString();
                    timeFinish = DateFinish.getText().toString() + " " + TimeFinish.getText().toString();
                    sendData = new String[]{monitortext, monitorSingleType, timeStart, timeFinish};
                    Intent intent = new Intent(getActivity(), AirStatementSingleActivity.class);
                    intent.putExtra("SendData", sendData);
                    startActivity(intent);
                }
                break;
        }
    }
    private void initTime() {
        DateStart.setText(DateTimeFormatUtil.getBeforeOneWeek());
        DateFinish.setText(DateTimeFormatUtil.lastDay());
        TimeStart.setText("00:00:00");
        TimeFinish.setText(DateTimeFormatUtil.currentDayTime());
    }
}
