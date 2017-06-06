package com.example.jinghui.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.jinghui.R;
import com.example.jinghui.activity.air.AirStatementAllActivity;
import com.example.jinghui.activity.air.AirStatementSingleActivity;
import com.example.jinghui.adapter.GeneralAdapter;
import com.example.jinghui.adapter.SpecialAdapter;
import com.example.jinghui.model.HistoryDatabaseEntity;
import com.example.jinghui.model.TAGAPP;
import com.example.jinghui.fragment.all.air.AirPressureFragment;
import com.example.jinghui.fragment.all.air.DustFragment;
import com.example.jinghui.fragment.all.air.HumidityFragment;
import com.example.jinghui.fragment.all.air.TemperatureFragment;
import com.example.jinghui.fragment.all.air.WindDirectionFragment;
import com.example.jinghui.fragment.all.air.WindSpeedFragment;
import com.example.jinghui.util.ListViwEmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by zhaoxin on 2017-05-17.
 */

public class PullDownAndLoadMore implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private int singlePage = 30;//每页大小
    private int paging_index = 0;//分页索引
    private ArrayList<HistoryDatabaseEntity> list;
    private Map<Integer, ArrayList<HistoryDatabaseEntity>> pageList;//int是page数   后面是每一页的数据
    private ArrayList<HistoryDatabaseEntity> allList;  //用来做分页操作的集合
    private ListView listView;
    private Fragment fragment;
    private BGARefreshLayout mRefreshLayout;
    private boolean isLastPage = false;  //是否已经加载到最后一页
    private int tag;
    private int tag0;
    private boolean isPullRefresh = false;
    private GeneralAdapter generalAdapter;
    private SpecialAdapter specialAdapter;
    private Button button;
    private View emptyView, nullInternet;
    private Activity activity;
    private Context context;

    //传递过来的对象有各对象的标识tag,对象本身fragment,对象的适配器,对象的listView，vie是空态的布局，mRefreshLayout则是对象的滑动组件
    //这个类实现上拉下拉接口  传递各对象的数据统一的在这个类里面操作  就可以实现上拉下拉
    public PullDownAndLoadMore(Context context, int tag, Fragment fragment, GeneralAdapter adapter, ListView listView, BGARefreshLayout mRefreshLayout, Button button) {
        this.generalAdapter = adapter;
        this.listView = listView;
        this.tag = tag;
        this.fragment = fragment;
        this.mRefreshLayout = mRefreshLayout;
        this.context = context;
        this.button = button;
        if (pageList == null) {
            pageList = new HashMap<>();
        }
        if (allList == null) {
            allList = new ArrayList<HistoryDatabaseEntity>();
        }

    }

    public PullDownAndLoadMore(Context context, int tag, Fragment fragment, SpecialAdapter specialAdapter, ListView listView, BGARefreshLayout mRefreshLayout, Button button) {
        this.specialAdapter = specialAdapter;
        this.listView = listView;
        this.tag = tag;
        this.button = button;
        this.fragment = fragment;
        this.mRefreshLayout = mRefreshLayout;
        this.context = context;
        if (pageList == null) {
            pageList = new HashMap<>();
        }
        if (allList == null) {
            allList = new ArrayList<HistoryDatabaseEntity>();
        }
    }

    public PullDownAndLoadMore(Context context, int tag, Activity activity, SpecialAdapter specialAdapter, ListView listView, BGARefreshLayout mRefreshLayout, Button button) {
        this.specialAdapter = specialAdapter;
        this.listView = listView;
        this.tag = tag;
        this.button = button;
        this.activity = activity;
        this.context = context;
        this.mRefreshLayout = mRefreshLayout;
        if (pageList == null) {
            pageList = new HashMap<>();
        }
        if (allList == null) {
            allList = new ArrayList<HistoryDatabaseEntity>();
        }
    }

    //同步数据更新
    public void setData(ArrayList<HistoryDatabaseEntity> list, int tag0) {
        this.tag0 = tag0;
        emptyView = View.inflate(context, R.layout.null_data, null);
        nullInternet = View.inflate(context, R.layout.null_internet, null);
        this.list = list;
        //保证每次下拉都会重新上拉的操作
        isLastPage = false;
        paging_index = 0;
        //每次都清空数据肯定会有点问题  后面再解决这个
        if (!pageList.isEmpty()) {
            pageList.clear();
        }
        if (!allList.isEmpty()) {
            allList.clear();
        }
        if (list == null || list.isEmpty()) {
            isLastPage = true;
        }
    }

    //给数据分组分页  并更新适配器
    public void refreshAdapter1() {
        for (int i = 0; i < list.size(); i++) {
            if (!pageList.containsKey(i / singlePage)) {
                pageList.put(i / singlePage, new ArrayList<HistoryDatabaseEntity>());
            }
            if (!pageList.get(i / singlePage).contains(list.get(i))) {
                //这里应该还要做个判断就是如果集合已经存在这个元素则不添加
                pageList.get(i / singlePage).add(list.get(i));
            }
        }
        //第一次添加到适配器的资源为第一页
        ArrayList<HistoryDatabaseEntity> pagelist = pageList.get(paging_index);
        if (pagelist != null) {
            allList.addAll(pagelist);
        }
        if (tag0 == TAGAPP.NO_DATA) {
            //表示数据为空 展示空态没有数据
            ListViwEmptyView.setEmptyView(listView, emptyView);
        }
        if (tag0 == TAGAPP.NO_INTERNET) {
            //表示没有完网络  展示空态没有网络
            ListViwEmptyView.setEmptyView(listView, nullInternet);
        }
        if (tag == TAGAPP.AIR_SINGLEMONITOR || tag == TAGAPP.AIR_ALLMONITOR) {
            specialAdapter.setData(pagelist);
            specialAdapter.notifyDataSetChanged();
        } else {
            generalAdapter.setData(pagelist);
            generalAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        Log.d("sss", "执行下拉请求");
        isPullRefresh = true;
        if (tag == TAGAPP.DUST) {
            DustFragment fragment = (DustFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.DIRECTION) {
            WindDirectionFragment fragment = (WindDirectionFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.SPEED) {
            WindSpeedFragment fragment = (WindSpeedFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.PRESSURE) {
            AirPressureFragment fragment = (AirPressureFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.TEMPERATURE) {
            TemperatureFragment fragment = (TemperatureFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.SHIDU) {
            HumidityFragment fragment = (HumidityFragment) this.fragment;
            fragment.executeNetworkRequest();
        }
        if (tag == TAGAPP.AIR_SINGLEMONITOR) {
            //这个适配器的对象不一样
            //SubMonitorSingleFragment fragment = (SubMonitorSingleFragment) this.fragment;
            //fragment.executeNetworkRequest();
            AirStatementSingleActivity airStatementSingleActivity = (AirStatementSingleActivity) activity;
            airStatementSingleActivity.executeNetworkRequest();
        }
        if (tag == TAGAPP.AIR_ALLMONITOR) {
            //SubMonitorAllFragment fragment = (SubMonitorAllFragment) this.fragment;
            // fragment.executeNetworkRequest();
            AirStatementAllActivity airStatementAllActivity = (AirStatementAllActivity) this.activity;
            airStatementAllActivity.executeNetworkRequest();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (paging_index + 1 == pageList.size()) {
            isLastPage = true;   //加载到最后一页
            new ToastShow(context).toastShow("数据已经全部加载完");
        }
        if (isLastPage) {
            return false;
        } else {
            paging_index++; //上拉一次页面索引+1  将该索引页面的数据添加到集合显示
            // 延时2秒钟执行里面的操作
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.endLoadingMore();
                    button.setEnabled(true);
                    button.setVisibility(View.VISIBLE);
                    //每次加载得到的数据
                    ArrayList<HistoryDatabaseEntity> pagelist = pageList.get(paging_index);
                    //这里要把每次加载的资源添加到上次加载资源的后面
                    if (pagelist != null) {
                        allList.addAll(pagelist);
                    }
                    if (tag == TAGAPP.AIR_SINGLEMONITOR || tag == TAGAPP.AIR_ALLMONITOR) {
                        //这个适配器的对象不一样  后面优化统一
                        specialAdapter.setData(allList);
                        specialAdapter.notifyDataSetChanged();
                    } else {
                        generalAdapter.setData(allList);
                        generalAdapter.notifyDataSetChanged();
                    }
                    //TODO
                }
            }, 2000);

            return true;
        }
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

    public boolean getIsPullDown() {
        return isPullRefresh;
    }

    public void setIsPullDown(boolean b) {
        isPullRefresh = b;
    }
}
