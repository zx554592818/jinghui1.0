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

public class WindDirectionFragment extends Fragment {
    private View view;
    private ListView windDirectionLv;
    private ArrayList<HistoryDatabaseEntity> directionList;
    private GeneralAdapter windDirectionAdapter;
    private MyApplication myApplication;
    private View head;
    private String monitor, timeType, timeStart, timeFinish;
    private ArrayList<HistoryDatabaseEntity> list;
    private String[] receiveData;
    private BGARefreshLayout mRefreshLayout;
    private Button btn;
    private PullDownAndLoadMore pullDownAndLoadMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_winddirection_airmonitor, null, false);
        head = inflater.inflate(R.layout.head_winddirection, null);
        initView();
        executeNetworkRequest();
        showPicture();
        return view;
    }

    private void showPicture() {
        //展示图表的时候传递的数据是转换方位之前的
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    Intent intent = new Intent(getActivity(), ShowChartActivity.class);
                    myApplication.setData(list);
                    intent.putExtra("name", "风向");
                    getActivity().startActivity(intent);

                } else {
                    new ToastShow(getActivity()).toastShow("没有数据");
                }
            }
        });
    }

    private void initView() {
        myApplication = MyApplication.getInstance();
        receiveData = getArguments().getStringArray("SendData0");
        monitor = receiveData[0];
        timeType = receiveData[1];
        timeStart = receiveData[2];
        timeFinish = receiveData[3];
        if (list == null) {
            list = new ArrayList<HistoryDatabaseEntity>();
        }
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_modulename_refresh);
        windDirectionLv = (ListView) view.findViewById(R.id.wind_direction_lv);
        btn = (Button) view.findViewById(R.id.btn);
        windDirectionLv.addHeaderView(head);
        //初始化设置适配器
        windDirectionAdapter = new GeneralAdapter(myApplication, TAGAPP.DIRECTION);
        windDirectionAdapter.setData(null);
        windDirectionLv.setAdapter(windDirectionAdapter);
        pullDownAndLoadMore = new PullDownAndLoadMore(getContext(),TAGAPP.DIRECTION, this, windDirectionAdapter, windDirectionLv, mRefreshLayout, btn);
        pullDownAndLoadMore.setPullRefresh();
    }

    private void refreshAdapter() {
        //网络请求得到的数据传到刷新类里面
        //传到适配器上的数据是转换了方位之后的
        //将得到的数据转换为方位数据 因为这里需要图表也展示风向数值 所以把二类数据类型的集合区分开
        directionList = getData(list);
        pullDownAndLoadMore.setData(directionList, TAGAPP.NO_DATA);
        pullDownAndLoadMore.refreshAdapter1();
    }

    public void executeNetworkRequest() {
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        if (!list.isEmpty()) {
            list.clear();
        }
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/WindDirectionManager?monitor=" + monitor + "&timeType=" + timeType + "&timeStart=" + timeStart + "&timeFinish=" + timeFinish;
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
                            String directave = jsonObject.getString("directave");
                            //String newTm = tm.replace(" ", "\n"); //将返回时间的字符串格式替换掉
                            if (timeType.equals("日数据")) {
                                newTm = TimeFormat.getInstance().getDayTime(tm);
                            } else {
                                newTm = TimeFormat.getInstance().getHourTime(tm);
                            }
                            HistoryDatabaseEntity historyDatabaseEntity = new HistoryDatabaseEntity(newTm, directave);
                            list.add(historyDatabaseEntity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn.setEnabled(true);
                        btn.setVisibility(View.VISIBLE);
                        refreshAdapter();
                        mRefreshLayout.endRefreshing();

                    }
                });
            }

        });
    }

    //把风向具体化
    private ArrayList<HistoryDatabaseEntity> getData(ArrayList<HistoryDatabaseEntity> list) {
        ArrayList<HistoryDatabaseEntity> newList = new ArrayList<>();
        for (HistoryDatabaseEntity aa : list) {
            String tm = aa.getTm();
            String directave = aa.getDirectave();

            if (directave != null && !directave.equals("")) {
                int i = (int) Float.parseFloat(directave);
                if (i == 0) {  //
                    directave = "正北";
                } else if ((i > 0 && i < 45) || (i % 360 > 0 && i % 360 < 45)) {
                    directave = "东北偏北";
                } else if (i == 45 || i % 360 == 45) {
                    directave = "正东北";
                } else if ((i > 45 && i < 90) || (i % 360 > 45 && i % 360 < 90)) {
                    directave = "东北偏东";
                } else if (i == 90 || i % 360 == 90) {
                    directave = "正东";
                } else if ((i > 90 && i < 135) || (i % 360 > 90 && i % 360 < 135)) {
                    directave = "东南偏东";
                } else if (i == 135 || i % 360 == 135) {
                    directave = "正东南";
                } else if ((i > 135 && i < 180) || i % 360 > 135 && i % 360 < 180) {
                    directave = "东南偏南";
                } else if (i == 180 || i % 360 == 180) {
                    directave = "正南";
                } else if ((i > 180 && i < 225) || (i % 360 > 180 && i % 360 < 225)) {
                    directave = "西南偏南";
                } else if (i == 225 || i % 360 == 225) {
                    directave = "正西南";
                } else if ((i > 225 && i < 270) || (i % 360 > 225 && i % 360 < 270)) {
                    directave = "西南偏西";
                } else if (i == 270 || i % 360 == 270) {
                    directave = "正西";
                } else if ((i > 270 && i < 315) || (i % 360 > 270 && i % 360 < 315)) {
                    directave = "西北偏西";
                } else if (i == 315 || i % 360 == 315) {
                    directave = "正西北";
                } else if ((i > 315 && i < 360) || (i % 360 > 315 && i % 360 < 360)) {
                    directave = "西北偏北";
                } else if (i == 360 || i % 360 == 360) {
                    directave = "正北";
                }
                //因为在构造函数将空值都变成了0.00
            } else {
                directave = "未知方位";
            }
            HistoryDatabaseEntity winDirection = new HistoryDatabaseEntity(tm, directave);
            newList.add(winDirection);
        }
        return newList;
    }
}

