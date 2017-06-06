package com.example.jinghui.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.example.liangmutian.mypicker.TimePickerDialog;

import java.util.List;

/**
 * Created by zhaoxin on 2017-05-09.
 */

public class DateTimePickerDialogMyself {
    private Dialog dateDialog, timeDialog;
    private Context context;

    public DateTimePickerDialogMyself(Context context) {
        this.context = context;
    }


    public void showDateDialog(List<Integer> date, final View view) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(context);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                EditText editText = (EditText) view;
                editText.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
            }

            @Override
            public void onCancel() {

            }
        }).setSelectYear(date.get(0) - 1).setSelectMonth(date.get(1) - 1).setSelectDay(date.get(2) - 1);
        builder.setMaxYear(DateUtil.getYear());
        //得到当前月
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        //怎么样得到当前日期的前面一个星期的日期
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    public void showTimePick(final View view) {
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(context);
        timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int[] times) {
                EditText editText = (EditText) view;
                editText.setText((times[0] > 9 ? times[0] : ("0" + times[0])) + ":" + (times[1] > 9 ? times[1] : ("0" + times[1])) + ":00");
            }

            @Override
            public void onCancel() {
            }

        }).create();
        timeDialog.show();

    }
}
