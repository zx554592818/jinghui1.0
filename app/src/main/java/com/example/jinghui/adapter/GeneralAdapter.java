package com.example.jinghui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jinghui.R;
import com.example.jinghui.model.HistoryDatabaseEntity;
import com.example.jinghui.model.TAGAPP;

import java.util.ArrayList;

/**
 * Created by zhaoxin on 2017-04-21.
 */

public class GeneralAdapter extends BaseAdapter {
    private ArrayList<HistoryDatabaseEntity> list;
    private Context context;
    private int i;
    private int j;
    private int TAG;
    public GeneralAdapter(Context context, int TAG) {
        this.context = context;
        this.TAG = TAG;
    }
    public void setData(ArrayList<HistoryDatabaseEntity> list) {
    this.list=list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            if (TAG == TAGAPP.DUST) {
                convertView = View.inflate(context, R.layout.item_airhistory_dust, null);
                viewHolder.textView1 = (TextView) convertView.findViewById(R.id.dustTV1);
                viewHolder.textView2 = (TextView) convertView.findViewById(R.id.dustTV2);
                viewHolder.textView3 = (TextView) convertView.findViewById(R.id.dustTV3);
            } else if (TAG == TAGAPP.DIRECTION) {
                convertView = View.inflate(context, R.layout.item_airhistory_winddirection, null);
                viewHolder.textView1 = (TextView) convertView.findViewById(R.id.windDirectionTV1);
                viewHolder.textView2 = (TextView) convertView.findViewById(R.id.windDirectionTV2);
            } else {
                convertView = View.inflate(context, R.layout.item_airhistory_windspeed, null);
                viewHolder.textView1 = (TextView) convertView.findViewById(R.id.WindSpeedTV1);
                viewHolder.textView2 = (TextView) convertView.findViewById(R.id.WindSpeedTV2);
                viewHolder.textView3 = (TextView) convertView.findViewById(R.id.WindSpeedTV3);
                viewHolder.textView4 = (TextView) convertView.findViewById(R.id.WindSpeedTV4);
            }
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (TAG == TAGAPP.DUST) {
            String pm25 = list.get(position).getPm25ave();
            String pm10 = list.get(position).getPm10ave();
            if (!pm25.equals("") && !pm10.equals("")) {
                i = (int) Float.parseFloat(pm25);
                j = (int) Float.parseFloat(pm10);
            }
            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(pm25);
            viewHolder.textView3.setText(pm10);
            if (i >= 75) {
                viewHolder.textView2.setTextColor(Color.RED);
            } else {
                viewHolder.textView2.setTextColor(Color.BLACK);
            }

            if (j >= 150) {

                viewHolder.textView3.setTextColor(Color.RED);
            } else {
                viewHolder.textView3.setTextColor(Color.BLACK);
            }
        } else if (TAG == TAGAPP.DIRECTION) {
            String directave = list.get(position).getDirectave();

            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(list.get(position).getDirectave());
        } else if (TAG == TAGAPP.SPEED) {
            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(list.get(position).getSpeedave());
            viewHolder.textView3.setText(list.get(position).getSpeedmax());
            viewHolder.textView4.setText(list.get(position).getSpeedmin());
        } else if (TAG == TAGAPP.PRESSURE) {
            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(list.get(position).getPressave());
            viewHolder.textView3.setText(list.get(position).getPressmax());
            viewHolder.textView4.setText(list.get(position).getPressmin());
        } else if (TAG == TAGAPP.TEMPERATURE) {
            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(list.get(position).getTemperave());
            viewHolder.textView3.setText(list.get(position).getTempermax());
            viewHolder.textView4.setText(list.get(position).getTempermin());
        } else if (TAG == TAGAPP.SHIDU) {
            viewHolder.textView1.setText(list.get(position).getTm());
            viewHolder.textView2.setText(list.get(position).getShiduave());
            viewHolder.textView3.setText(list.get(position).getShidumax());
            viewHolder.textView4.setText(list.get(position).getShidumin());
        }
        return convertView;
    }
}

class ViewHolder {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
}
