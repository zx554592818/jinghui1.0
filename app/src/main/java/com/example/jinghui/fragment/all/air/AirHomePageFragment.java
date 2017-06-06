package com.example.jinghui.fragment.all.air;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.util.MyOkHttpClient;
import com.example.jinghui.R;
import com.example.jinghui.ui.SelectPlaceDialogBuilder;
import com.example.jinghui.ui.ToastShow;
import com.example.jinghui.activity.air.AirQualityActivity;
import com.example.jinghui.model.AppSimpleData;
import com.example.jinghui.util.DateTimeFormatUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhaoxin on 2017-05-18.
 */

public class AirHomePageFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private View view;
    private BGARefreshLayout mRefreshLayout;
    private TextView selectPlace;
    private AppSimpleData simpleData = null;
    private TextView timeHomePage;
    private TextView week;
    private MyApplication myApplication;
    private TextView textPm25, textPm10, textSpeed, textPress, textTemperature, textShidu, textDirection, feature_Temperature;
    private String pm25ave, pm10ave, speedave, direction, pressave, temperave, shiduave;
    private SelectPlaceDialogBuilder selectPlaceDialogBuilder;
    private Button enter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_homepage_air, null, false);
        initView();
        selectSpecificPlace(); //设置初始化 并选择具体地址
        initTime();//初始化时间
        executeNetworkRequest(); //执行请求
        enter();
        return view;
    }



    private void initView() {
        myApplication = MyApplication.getInstance();
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshHomePage);
        selectPlace = (TextView) view.findViewById(R.id.selectPlace);
        timeHomePage = (TextView) view.findViewById(R.id.timeHomePage);
        week = (TextView) view.findViewById(R.id.week);
        if (simpleData == null) {
            simpleData = new AppSimpleData();
        }
        textPm25 = (TextView) view.findViewById(R.id.textPm25);
        textPm10 = (TextView) view.findViewById(R.id.textPm10);
        textSpeed = (TextView) view.findViewById(R.id.textSpeed);
        textPress = (TextView) view.findViewById(R.id.textPress);
        textTemperature = (TextView) view.findViewById(R.id.textTemperature);
        textShidu = (TextView) view.findViewById(R.id.textShidu);
        textDirection = (TextView) view.findViewById(R.id.textDirection);
        feature_Temperature = (TextView) view.findViewById(R.id.feature_Temperature);
        enter = (Button) view.findViewById(R.id.moreContent);
//得到对话框对象
        setPullRefresh();
    }

    private void enter() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AirQualityActivity.class);
                startActivity(intent);

            }
        });
    }

    //选择地址 弹出对话框
    private void selectSpecificPlace() {
        final String[] allData = simpleData.getAllData();
        selectPlace.setText(allData[0]);  //默认选择地址
        selectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlaceDialogBuilder = new SelectPlaceDialogBuilder(getActivity());
                selectPlaceDialogBuilder.setHomePageDialog(allData, selectPlace);
                //这里要做一个回调选择完地址之后需要回调执行网络请求
                selectPlaceDialogBuilder.setRefresh(new SelectPlaceDialogBuilder.Refresh() {
                    @Override
                    public void complete() {
                        initTime();
                        executeNetworkRequest();
                    }
                });

            }
        });


    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //下拉时间更新 执行请求
        initTime();
        executeNetworkRequest();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    public void setPullRefresh() {
        mRefreshLayout.setDelegate(this);
        BGAMeiTuanRefreshViewHolder refreshViewHolder = new BGAMeiTuanRefreshViewHolder(MyApplication.getInstance(), true);
        //开始下拉 和结束下拉时展示的样式
        refreshViewHolder.setPullDownImageResource(R.drawable.bga_refresh_mt_refreshing);
        //设置下拉刷新背景颜色
        //refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.gray);
        //设置下拉刷新开始刷新临界点
        refreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        //设置下拉刷新正在刷新的状态
        refreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_loding);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    //执行网络请求
    public void executeNetworkRequest() {
        //因为数据库的时间更新慢1个小时 所以这里我们请求的时间设置为当前2小时以前
        //开始时间和结束时间传一样的 即可以得到这个时间的数值
        //时间类型就传小时数据  因为当天的天数据数据库没更新完毕
        //濮阳市城市代码101181301
        //城市天气接口    http://www.weather.com.cn/data/cityinfo/城市代码.html

        String eTime = DateTimeFormatUtil.getBeforeTwoHour();
        String sTime = DateTimeFormatUtil.getBeforeTwoHour();
        String tType = "小时数据";
        String monitor = selectPlace.getText().toString();
        //目前只有濮阳的数据  以后如果有了别的城市的数据 这里的url要进行更改 要判断 如果是濮阳 则请求哪个接口   如果是武汉则请求武汉数据的接口
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/AirHistoryMannger?monitor=" + monitor + "&timeType=" + tType + "&timeStart=" + sTime + "&timeFinish=" + eTime;
        final Request request = new Request.Builder().url(url).build();
        MyOkHttpClient.getOkHttpClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                        new ToastShow(getActivity()).toastShow("服务出错，无法获取到准确信息");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            pm25ave = jsonObject.getString("pm25ave");
                            pm10ave = jsonObject.getString("pm10ave");
                            speedave = jsonObject.getString("speedave");
                            String directave = jsonObject.getString("directave");
                            direction = getWindDirection(directave);  //将数字转换成具体方位
                            pressave = jsonObject.getString("pressave");
                            temperave = jsonObject.getString("temperave");
                            shiduave = jsonObject.getString("shiduave");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                        //设置文本
                        textDirection.setText(direction);
                        textPm25.setText(pm25ave);
                        textPm10.setText(pm10ave);
                        textSpeed.setText(speedave);
                        textPress.setText(pressave);
                        textTemperature.setText(temperave);
                        textShidu.setText(shiduave);
                        feature_Temperature.setText(temperave + "℃");
                        if (!TextUtils.isEmpty(pm25ave) && !TextUtils.isEmpty(pm10ave)) {
                            int i = (int) Float.parseFloat(pm25ave);
                            int j = (int) Float.parseFloat(pm10ave);
                            if (i > 75) {
                                textPm25.setTextColor(Color.RED);
                            }
                            if (j > 150) {
                                textPm10.setTextColor(Color.RED);
                            }
                        }

                    }
                });
            }
        });
    }


    private void initTime() {
        //执行网络请求之后这里也要重新走一遍
        timeHomePage.setText(DateTimeFormatUtil.lastDay());
        //得到星期
        week.setText(DateTimeFormatUtil.getDayOfWeek());
    }

    private String getWindDirection(String directave) {
        if (!TextUtils.isEmpty(directave)) {
            int i = (int) Float.parseFloat(directave);
            if (i == 0) {  //
                directave = "北风";
            } else if ((i > 0 && i < 45) || (i % 360 > 0 && i % 360 < 45)) {
                directave = "东北风";
            } else if (i == 45 || i % 360 == 45) {
                directave = "东北风";
            } else if ((i > 45 && i < 90) || (i % 360 > 45 && i % 360 < 90)) {
                directave = "东北风";
            } else if (i == 90 || i % 360 == 90) {
                directave = "东风";
            } else if ((i > 90 && i < 135) || (i % 360 > 90 && i % 360 < 135)) {
                directave = "东南风";
            } else if (i == 135 || i % 360 == 135) {
                directave = "东南风";
            } else if ((i > 135 && i < 180) || i % 360 > 135 && i % 360 < 180) {
                directave = "东南风";
            } else if (i == 180 || i % 360 == 180) {
                directave = "南风";
            } else if ((i > 180 && i < 225) || (i % 360 > 180 && i % 360 < 225)) {
                directave = "西南风";
            } else if (i == 225 || i % 360 == 225) {
                directave = "西南风";
            } else if ((i > 225 && i < 270) || (i % 360 > 225 && i % 360 < 270)) {
                directave = "西南风";
            } else if (i == 270 || i % 360 == 270) {
                directave = "正西";
            } else if ((i > 270 && i < 315) || (i % 360 > 270 && i % 360 < 315)) {
                directave = "西北风";
            } else if (i == 315 || i % 360 == 315) {
                directave = "西北风";
            } else if ((i > 315 && i < 360) || (i % 360 > 315 && i % 360 < 360)) {
                directave = "西北风";
            } else if (i == 360 || i % 360 == 360) {
                directave = "北风";
            }
        } else {
            directave = "未知风向";
        }
        return directave;
    }
}
