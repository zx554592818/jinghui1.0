package com.example.jinghui.activity.air;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
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

public class AirStatementAllActivity extends BaseActivity {

    private TextView  subMonitorAllTitle, subMonitorAllType;
    private String[] subMonitorAllHead;
    private ArrayList<HistoryDatabaseEntity> list;
    private String monitorType, timeType, dateTime;
    private MyApplication myApplication;
    private BGARefreshLayout mRefreshLayout;
    private RelativeLayout mHead;
    private ListView mListView;
    private SpecialAdapter specialAdapter;
    private boolean isPullRefresh = false;
    private PullDownAndLoadMore pullDownAndLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statament_all);
        initView();
        executeNetworkRequest();
    }

    public void airStatementAllBack(View view) {
        finish();
    }

    //初始化数据
    private void initView() {
        //获取MonitorAllFragment传送过来的数据。设置该界面头部文本信息。
        myApplication = MyApplication.getInstance();
        subMonitorAllHead = getIntent().getStringArrayExtra("SendData");
        monitorType = subMonitorAllHead[0];
        timeType = subMonitorAllHead[1];
        dateTime = subMonitorAllHead[2];
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        subMonitorAllTitle = (TextView) findViewById(R.id.subMonitorAllTitle);
        subMonitorAllType = (TextView) findViewById(R.id.subMonitorAllType);
        mListView = (ListView) findViewById(R.id.listView);
        setHead();
        specialAdapter = new SpecialAdapter(TAGAPP.AIR_ALLMONITOR, myApplication, R.layout.item, mHead);
        specialAdapter.setData(null);
        mListView.setAdapter(specialAdapter);
        //设置上拉下拉监听
        pullDownAndLoadMore = new PullDownAndLoadMore(this, TAGAPP.AIR_ALLMONITOR, this, specialAdapter, mListView, mRefreshLayout, null);
        pullDownAndLoadMore.setPullRefresh();
    }

    //更新适配器数据
    private void refreshAdapter() {
        //网络请求得到的数据传到刷新类里面
        pullDownAndLoadMore.setData(list, TAGAPP.NO_DATA);
        pullDownAndLoadMore.refreshAdapter1();
    }

    private void setHead() {
        mHead = (RelativeLayout) findViewById(R.id.head);
        mHead.setFocusable(true);
        mHead.setClickable(true);
        mHead.setBackground(getResources().getDrawable(R.drawable.bg_head_listview));
        // mHead.setBackgroundColor(Color.WHITE);
        //头部监听
        mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        //listView监听
        mListView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
    }

    //设置网络请求得到的数据   从集合得到数据  然后设置到对应控件  集合有多少条数据  就新建多少行
    public void executeNetworkRequest() {
        //这个操作是让进行下拉操作的时候不跳进度框
        isPullRefresh = pullDownAndLoadMore.getIsPullDown();
        if (isPullRefresh == false) {
            ProgressDialogMyself.showSuccinctProgress(this,
                    "请求数据中···", ProgressDialogMyself.THEME_ULTIMATE, false, true);
        }
        String url = myApplication.DEBUG_BASE_URL + "/jhhb/AirStatementManager?monitorType=" + monitorType + "&timeType=" + timeType + "&dateTime=" + dateTime;
        final Request request = new Request.Builder().url(url).build();
        MyOkHttpClient.getOkHttpClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //dialog.dismiss();
                ProgressDialogMyself.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                        //请求完成才更新UI
                        new ToastShow(AirStatementAllActivity.this).toastShow("服务出错，请检查你的网络设置");
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
                        if (list == null) {
                            list = new ArrayList<HistoryDatabaseEntity>();
                        }
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String monitor = jsonObject.getString("place");
                            String tm = jsonObject.getString("tm");
                            String pm25ave = jsonObject.getString("pm25ave");
                            String pm10ave = jsonObject.getString("pm10ave");
                            String speedave = jsonObject.getString("speedave");
                            String directave = jsonObject.getString("directave");
                            String direction = getDirection(directave); //将数字转变为方位
                            String pressave = jsonObject.getString("pressave");
                            String temperave = jsonObject.getString("temperave");
                            String shiduave = jsonObject.getString("shiduave");
                            //String newTm = tm.replace(" ", "\n"); //将返回时间的字符串格式替换掉
                            if (timeType.equals("日数据")) {
                                newTm = TimeFormat.getInstance().getDayTime(tm);
                            } else {
                                newTm = TimeFormat.getInstance().getHourTime(tm);
                            }
                            HistoryDatabaseEntity allMonitorEntity = new HistoryDatabaseEntity(monitor, newTm, pm25ave, pm10ave, speedave, direction, pressave, temperave, shiduave);
                            list.add(allMonitorEntity);
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
                        mRefreshLayout.endRefreshing();
                        refreshAdapter();
                        //请求完成才更新UI
                        subMonitorAllTitle.setText(monitorType);
                        subMonitorAllType.setText(timeType);
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
