package com.example.jinghui.fragment.all.air;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.util.MyOkHttpClient;
import com.example.jinghui.ui.PullDownAndLoadMore;
import com.example.jinghui.R;
import com.example.jinghui.ui.ToastShow;
import com.example.jinghui.activity.ShowChartActivity;
import com.example.jinghui.model.TAGAPP;
import com.example.jinghui.model.TimeFormat;
import com.example.jinghui.adapter.GeneralAdapter;
import com.example.jinghui.model.HistoryDatabaseEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 赵欣 on 2017/2/28.
 */

public class WindSpeedFragment extends Fragment {
    private ListView windSpeedLv;
    private View view;
    private GeneralAdapter windSpeedAdapter;
    private MyApplication myApplication;
    private Button btn;
    private View head;
    private BGARefreshLayout mRefreshLayout;
    private String monitor, timeType, timeStart, timeFinish;
    private ArrayList<HistoryDatabaseEntity> list;
    private String[] receiveData;
    private PullDownAndLoadMore pullDownAndLoadMore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_windspeed_airmonitor, null, false);
        head = inflater.inflate(R.layout.head_windspeed, null);
        initView();
        executeNetworkRequest();
        showPicture();
        return view;
    }

    private void showPicture() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    Intent intent = new Intent(getActivity(), ShowChartActivity.class);
                    myApplication.setData(list);
                    intent.putExtra("name", "风速");
                    getActivity().startActivity(intent);
                } else {
                    new ToastShow(getActivity()).toastShow("没有数据");
                }
            }
        });
    }

    private void initView() {
        receiveData = getArguments().getStringArray("SendData0");
        monitor = receiveData[0];
        timeType = receiveData[1];
        timeStart = receiveData[2];
        timeFinish = receiveData[3];
        if (list == null) {
            list = new ArrayList<HistoryDatabaseEntity>();
        }
        myApplication = MyApplication.getInstance();
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_modulename_refresh);
        windSpeedLv = (ListView) view.findViewById(R.id.wind_speed_lv);
        windSpeedLv.addHeaderView(head);
        btn = (Button) view.findViewById(R.id.btn);
        //初始化设置适配器
        windSpeedAdapter = new GeneralAdapter(myApplication, TAGAPP.SPEED);
        windSpeedAdapter.setData(null);
        windSpeedLv.setAdapter(windSpeedAdapter);
        pullDownAndLoadMore = new PullDownAndLoadMore(getContext(),TAGAPP.SPEED, this, windSpeedAdapter, windSpeedLv, mRefreshLayout, btn);
        pullDownAndLoadMore.setPullRefresh();
    }

    private void refreshAdapter() {
        //网络请求得到的数据传到刷新类里面
        pullDownAndLoadMore.setData(list, TAGAPP.NO_DATA);
        pullDownAndLoadMore.refreshAdapter1();
    }
    public void executeNetworkRequest() {
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        if (!list.isEmpty()) {
            list.clear();
        }
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/WindSpeedManager?monitor=" + monitor + "&timeType=" + timeType + "&timeStart=" + timeStart + "&timeFinish=" + timeFinish;
        final Request request = new Request.Builder().url(url).build();
        MyOkHttpClient.getOkHttpClient(myApplication).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new ToastShow(getActivity()).toastShow("服务出错，请检查你的网络设置");
                        mRefreshLayout.endRefreshing();
                        pullDownAndLoadMore.setData(list, TAGAPP.NO_INTERNET);
                        pullDownAndLoadMore.refreshAdapter1();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String newTm;
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tm = jsonObject.getString("tm");
                            String speedave = jsonObject.getString("speedave");
                            String speedmax = jsonObject.getString("speedmax");
                            String speedmin = jsonObject.getString("speedmin");
                            //String newTm = tm.replace(" ", "\n"); //将返回时间的字符串格式替换掉
                            if (timeType.equals("日数据")) {
                                newTm = TimeFormat.getInstance().getDayTime(tm);
                            } else {
                                newTm = TimeFormat.getInstance().getHourTime(tm);
                            }
                            HistoryDatabaseEntity historyDatabaseEntity = new HistoryDatabaseEntity(newTm, null, null, speedave, speedmax, speedmin, null, null, null, null, null, null, null, null, null, null);
                            list.add(historyDatabaseEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshAdapter();
                        btn.setEnabled(true);
                        btn.setVisibility(View.VISIBLE);
                        mRefreshLayout.endRefreshing();

                    }
                });
            }
        });
    }
}
