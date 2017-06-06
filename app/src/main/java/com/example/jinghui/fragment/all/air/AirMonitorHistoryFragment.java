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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jinghui.activity.air.AirHistoryActivity;
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
 * Created by 赵欣 on 2017/2/24.
 */

public class AirMonitorHistoryFragment extends Fragment implements View.OnClickListener {
    private TextView town_tv, site_tv;
    private AppSimpleData simpleData = null;
    private String monitortext, historyType;
    private View inflate;
    private Spinner spinner;
    private Button inquire_btn;
    private EditText dateStart, timeStart, dateFinish, timeFinish;

    private String[] sendData;
    private int[] image;
    private List<ImageView> pagerView;
    private SlideBannerViewPager slideBannerViewPager;
    private ArrayList<String> spinnerList;
    private SelectPlaceDialogBuilder selectPlaceDialogBuilder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history_airmonitor, null, false);
        initView();
        initData();
        initViewData(); //轮播图添加图片
        initTime();
        return inflate;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        slideBannerViewPager = (SlideBannerViewPager) inflate.findViewById(R.id.viewPager);
        dateStart = (EditText) inflate.findViewById(R.id.dateStart);
        timeStart = (EditText) inflate.findViewById(R.id.timeStart);
        dateFinish = (EditText) inflate.findViewById(R.id.dateFinish);
        timeFinish = (EditText) inflate.findViewById(R.id.timeFinish);
        town_tv = (TextView) inflate.findViewById(R.id.town_tv);
        site_tv = (TextView) inflate.findViewById(R.id.site_tv);
        spinner = (Spinner) inflate.findViewById(R.id.spinner);
        inquire_btn = (Button) inflate.findViewById(R.id.inquire_btn);

        town_tv.setOnClickListener(this);
        site_tv.setOnClickListener(this);
        inquire_btn.setOnClickListener(this);
        dateStart.setOnClickListener(this);
        timeStart.setOnClickListener(this);
        dateFinish.setOnClickListener(this);
        timeFinish.setOnClickListener(this);
        //初始化dialog 对话框必须要使用activity的上下文  上下问引用AirMonitorActivity的上下文
        //这里可以打印出来上下文的名字看看
        //String simpleName = getContext().getClass().getSimpleName();
        //Log.d("SSSS", simpleName);

    }

    private void initViewData() {
        image = new AppSimpleData().getImage();
        if (pagerView == null) {
            pagerView = new ArrayList<ImageView>();
        }
        for (int imgId : image) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            pagerView.add(imageView);
        }
        slideBannerViewPager.setPages(pagerView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化数据
        if (simpleData == null) {
            simpleData = new AppSimpleData();
        }
        spinnerList = new ArrayList<String>();
        spinnerList.add(simpleData.getDataSimple()[0]);
        spinnerList.add(simpleData.getDataSimple()[1]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, spinnerList) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                convertView = View.inflate(getContext(), R.layout.spinner_dropdown, null);
                TextView label = (TextView) convertView
                        .findViewById(R.id.spinner_item_label);
                label.setText(spinnerList.get(position));
                return convertView;
            }

        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                historyType = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.town_tv:
                //每次点击都重新获取  这样可以保证对话框的依附一直存在
                selectPlaceDialogBuilder = new SelectPlaceDialogBuilder(getActivity());
                //清空下一个输入框的内容
                site_tv.setText(null);
                //每次点击清空
                monitortext = null;
                final String[] townData = simpleData.getTownData();
                selectPlaceDialogBuilder.setDialog(townData, town_tv);
                break;
            case R.id.site_tv:
                selectPlaceDialogBuilder = new SelectPlaceDialogBuilder(getActivity());
                town_tv.setText(null);
                monitortext = null;
                String[] siteDta = simpleData.getSiteDta();
                selectPlaceDialogBuilder.setDialog(siteDta, site_tv);
                break;
            case R.id.inquire_btn:
                if (!TextUtils.isEmpty(town_tv.getText().toString())) {
                    monitortext = town_tv.getText().toString();
                } else if (!TextUtils.isEmpty(site_tv.getText().toString())) {
                    monitortext = site_tv.getText().toString();
                } else {
                    new ToastShow(getActivity()).toastShow("请选择地址");
                }
                if (!TextUtils.isEmpty(monitortext)) {
                    String tmStart = dateStart.getText().toString() + " " + timeStart.getText().toString(); //拼接日期与时间传到下个界面  在下个界面进行网络请求
                    String tmFinish = dateFinish.getText().toString() + " " + timeFinish.getText().toString();
                    sendData = new String[]{monitortext, historyType, tmStart, tmFinish};
                    Intent intent = new Intent(getActivity(), AirHistoryActivity.class);
                    intent.putExtra("SendData", sendData);
                    startActivity(intent);
                }
                break;

            case R.id.dateStart:
                new DateTimePickerDialogMyself(getActivity()).showDateDialog(DateUtil.getDateForString(DateUtil.getToday()), dateStart);
                break;
            case R.id.timeStart:
                new DateTimePickerDialogMyself(getActivity()).showTimePick(timeStart);
                break;
            case R.id.dateFinish:
                new DateTimePickerDialogMyself(getActivity()).showDateDialog(DateUtil.getDateForString(DateUtil.getToday()), dateFinish);

                break;
            case R.id.timeFinish:
                new DateTimePickerDialogMyself(getActivity()).showTimePick(timeFinish);
                break;
        }
    }

    private void initTime() {
        //设为为当前时间前一个星期
        dateStart.setText(DateTimeFormatUtil.getBeforeOneWeek());
        dateFinish.setText(DateTimeFormatUtil.lastDay());
        timeStart.setText("00:00:00");
        timeFinish.setText(DateTimeFormatUtil.currentDayTime());
    }
}

