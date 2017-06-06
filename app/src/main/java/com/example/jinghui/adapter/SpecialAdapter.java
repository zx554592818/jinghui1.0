package com.example.jinghui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.jinghui.ui.MyHScrollView;
import com.example.jinghui.R;
import com.example.jinghui.model.HistoryDatabaseEntity;
import com.example.jinghui.model.TAGAPP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2017-04-22.
 */

public class SpecialAdapter extends BaseAdapter {
    public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
    private ArrayList<HistoryDatabaseEntity> list;
    int id_row_layout;
    private LayoutInflater mInflater;
    private Context context;
    private View mHead;
    private int tag;
    private int i;
    private int j;

    public SpecialAdapter(int tag, Context context, int id_row_layout, View mHead) {
        super();
        this.id_row_layout = id_row_layout;
        this.context = context;
        this.mHead = mHead;
        this.tag = tag;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<HistoryDatabaseEntity> list) {
        this.list = list;

    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();

    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        ViewHolder holder = null;
        if (convertView == null) {
            synchronized (context) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(id_row_layout, null);
                MyHScrollView scrollView1 = (MyHScrollView) convertView
                        .findViewById(R.id.horizontalScrollView1);
                holder.scrollView = scrollView1;
                holder.txt1 = (TextView) convertView
                        .findViewById(R.id.textView1);
                holder.txt2 = (TextView) convertView
                        .findViewById(R.id.textView2);
                holder.txt3 = (TextView) convertView
                        .findViewById(R.id.textView3);
                holder.txt4 = (TextView) convertView
                        .findViewById(R.id.textView4);
                holder.txt5 = (TextView) convertView
                        .findViewById(R.id.textView5);
                holder.txt6 = (TextView) convertView
                        .findViewById(R.id.textView6);
                holder.txt7 = (TextView) convertView
                        .findViewById(R.id.textView7);
                holder.txt8 = (TextView) convertView
                        .findViewById(R.id.textView8);
                if (tag == TAGAPP.AIR_ALLMONITOR) {
                    holder.txt9 = (TextView) convertView
                            .findViewById(R.id.textView9);
                }
                MyHScrollView headSrcrollView = (MyHScrollView) mHead
                        .findViewById(R.id.horizontalScrollView1);
                headSrcrollView
                        .AddOnScrollChangedListener(new OnScrollChangedListenerImp(
                                scrollView1));
                convertView.setTag(holder);
                mHolderList.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String pm25 = list.get(position).getPm25ave();
        String pm10 = list.get(position).getPm10ave();
        if (!pm25.equals("") && !pm10.equals("")) {
            i = (int) Float.parseFloat(pm25);
            j = (int) Float.parseFloat(pm10);
        }
        if (tag == TAGAPP.AIR_ALLMONITOR) {
            holder.txt1.setText(list.get(position).getPlace());
            holder.txt2.setText(list.get(position).getTm());
            holder.txt3.setText(list.get(position).getPm25ave());
            holder.txt4.setText(list.get(position).getPm10ave());
            if (i >= 75) {
                holder.txt3.setTextColor(Color.RED);
            } else {
                holder.txt3.setTextColor(Color.BLACK);
            }
            if (j >= 150) {
                holder.txt4.setTextColor(Color.RED);
            } else {
                holder.txt4.setTextColor(Color.BLACK);
            }
            holder.txt5.setText(list.get(position).getSpeedave());
            holder.txt6.setText(list.get(position).getDirectave());
            holder.txt7.setText(list.get(position).getPressave());
            holder.txt8.setText(list.get(position).getTemperave());
            holder.txt9.setText(list.get(position).getShiduave());
        } else if (tag == TAGAPP.AIR_SINGLEMONITOR) {
            holder.txt1.setText(list.get(position).getTm());
            holder.txt2.setText(list.get(position).getPm25ave());
            holder.txt3.setText(list.get(position).getPm10ave());
            if (i >= 75) {
                holder.txt2.setTextColor(Color.RED);
            } else {
                holder.txt2.setTextColor(Color.BLACK);
            }
            if (j >= 150) {
                holder.txt3.setTextColor(Color.RED);
            } else {
                holder.txt3.setTextColor(Color.BLACK);
            }
            holder.txt4.setText(list.get(position).getSpeedave());
            holder.txt5.setText(list.get(position).getDirectave());
            holder.txt6.setText(list.get(position).getPressave());
            holder.txt7.setText(list.get(position).getTemperave());
            holder.txt8.setText(list.get(position).getShiduave());
        }
        return convertView;
    }

    class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
        MyHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }

    class ViewHolder {
        TextView txt1;
        TextView txt2;
        TextView txt3;
        TextView txt4;
        TextView txt5;
        TextView txt6;
        TextView txt7;
        TextView txt8;
        TextView txt9;
        HorizontalScrollView scrollView;
    }
}