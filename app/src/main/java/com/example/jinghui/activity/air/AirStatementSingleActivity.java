package com.example.jinghui.activity.air;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
import com.example.jinghui.activity.ShowSingleChartActivity;
import com.example.jinghui.adapter.SpecialAdapter;
import com.example.jinghui.model.HistoryDatabaseEntity;
import com.example.jinghui.model.TAGAPP;
import com.example.jinghui.model.TimeFormat;
import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.ui.PullDownAndLoadMore;
import com.example.jinghui.ui.ToastShow;
import com.example.jinghui.util.MyOkHttpClient;
import com.example.jinghui.util.ProgressDialogMyself;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhaoxin on 2017-05-25.
 */

public class AirStatementSingleActivity extends BaseActivity {
    private TextView monitorSingleTitle, monitorSingleType;
    private String[] monitorSingleHead;
    private ArrayList<HistoryDatabaseEntity> list;
    private String timeStart, timeFinish, timeType, monitor;
    private MyApplication myApplication;
    private BGARefreshLayout mRefreshLayout;
    private RelativeLayout mHead;
    private ListView mListView;
    private SpecialAdapter specialAdapter;
    private Button btn;
    private boolean isPullRefresh = false;
    private PullDownAndLoadMore pullDownAndLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_single);
        initView();
        setHead();
        executeNetworkRequest();
    }

    public void showPictureAirSingle(View view) {
        //展示图表的时候传递的是list   因为这个图表里面我没有做风向的展示 如果需要做风向的展示的话 这里应该传递的带有数值的风向集合
        if (!list.isEmpty()) {
            Intent intent = new Intent(AirStatementSingleActivity.this, ShowSingleChartActivity.class);
            myApplication.setData(list);
            intent.putExtra("name", "各个监测点");
            startActivity(intent);

        } else {
            new ToastShow(AirStatementSingleActivity.this).toastShow("没有数据");
        }
    }
    public void airStatementSingleBack(View view) {
        finish();
    }

    private void initView() {
        myApplication = MyApplication.getInstance();
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        monitorSingleHead = getIntent().getStringArrayExtra("SendData");
        monitorSingleTitle = (TextView) findViewById(R.id.subMonitorSingleTitle);
        monitorSingleType = (TextView) findViewById(R.id.subMonitorSingleType);
        mHead = (RelativeLayout) findViewById(R.id.head);
        timeStart = monitorSingleHead[2];
        timeFinish = monitorSingleHead[3];
        monitor = monitorSingleHead[0];
        timeType = monitorSingleHead[1];
        if (list == null) {
            list = new ArrayList<HistoryDatabaseEntity>();
        }
        btn = (Button) findViewById(R.id.btn);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        specialAdapter = new SpecialAdapter(TAGAPP.AIR_SINGLEMONITOR, myApplication, R.layout.item_single, mHead);
        specialAdapter.setData(null);
        mListView.setAdapter(specialAdapter);
        pullDownAndLoadMore = new PullDownAndLoadMore(this, TAGAPP.AIR_SINGLEMONITOR, this, specialAdapter, mListView, mRefreshLayout, btn);
        pullDownAndLoadMore.setPullRefresh();
    }

    private void refreshAdapter() {
        //网络请求得到的数据传到刷新类里面
        pullDownAndLoadMore.setData(list, TAGAPP.NO_DATA);
        pullDownAndLoadMore.refreshAdapter1();
    }

    private void setHead() {
        mHead.setFocusable(true);
        mHead.setClickable(true);
        //mHead.setBackgroundColor(Color.parseColor("#b2d235"));
        mHead.setBackground(getResources().getDrawable(R.drawable.bg_head_listview));
        mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
    }

    public void executeNetworkRequest() {
        btn.setEnabled(false);
        //这个操作是让进行下拉操作的时候不跳进度框
        isPullRefresh = pullDownAndLoadMore.getIsPullDown();
        if (isPullRefresh == false) {
            ProgressDialogMyself.showSuccinctProgress(this, "请求数据中···", ProgressDialogMyself.THEME_DOT, false, true);
        }
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/AirHistoryMannger?monitor=" + monitor + "&timeType=" + timeType + "&timeStart=" + timeStart + "&timeFinish=" + timeFinish;
        final Request request = new Request.Builder().url(url).build();
        MyOkHttpClient.getOkHttpClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProgressDialogMyself.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new ToastShow(AirStatementSingleActivity.this).toastShow("服务出错，请检查你的网络设置");
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
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tm = jsonObject.getString("tm");
                            String pm25ave = jsonObject.getString("pm25ave");
                            String pm10ave = jsonObject.getString("pm10ave");
                            String speedave = jsonObject.getString("speedave");
                            String directave = jsonObject.getString("directave");
                            String direction = getDirection(directave);  //将数字转换成具体方位
                            String pressave = jsonObject.getString("pressave");
                            String temperave = jsonObject.getString("temperave");
                            String shiduave = jsonObject.getString("shiduave");
                            // String newTm = tm.replace(" ", "\n"); //将返回时间的字符串格式替换掉
                            if (timeType.equals("日数据")) {
                                newTm = TimeFormat.getInstance().getDayTime(tm);
                            } else {
                                newTm = TimeFormat.getInstance().getHourTime(tm);
                            }
                            HistoryDatabaseEntity singleMonitorData = new HistoryDatabaseEntity(newTm, pm25ave, pm10ave, speedave, direction, pressave, temperave, shiduave);
                            list.add(singleMonitorData);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ProgressDialogMyself.dismiss();
                isPullRefresh = false; //数据请求完毕将条件还原 这是为了保证只有下拉刷新的时候才为true
                //数据请求完毕 给刷新工具类 里面设置还原
                pullDownAndLoadMore.setIsPullDown(isPullRefresh);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn.setEnabled(true);
                        mRefreshLayout.endRefreshing();

                        //下拉刷新给适配器的资源初始化
                        refreshAdapter();
                        monitorSingleTitle.setText(monitor);
                        monitorSingleType.setText(timeType);
                    }

                });
            }
        });
    }

    private String getDirection(String directave) {
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
        } else {
            directave = "未知方位";
        }
        return directave;
    }

    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }
}
