package com.example.jinghui.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.R;
import com.example.jinghui.model.HistoryDatabaseEntity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhaoxin on 2017-04-14.
 */
//图表绘制引擎
public class ShowChartActivity extends BaseActivity {
    private GraphicalView view;
    private ArrayList<HistoryDatabaseEntity> list;
    private String name;
    private XYSeries series;
    private XYSeries seriesTwo;
    private Double numberMin;
    private Double numberMax;
    private RelativeLayout relativeLayout;
    public static final double SMALL_WIN_H_SCALE = 0.67;
    public static final double SMALL_WIN_W_SCALE = 0.72;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private String timeMax25, timeMin25, timeMax10, timeMin10, timeMax, timeMin;
    private double PM25SUM, PM10SUM, SUM;
    private String directave;
    private TextView chartTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chart);
        initView();
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);*/
        //  actionBar.setHomeAsUpIndicator(R.drawable.close_button);
        //绘制图表
        lineView();
        //往容器添加图表控件
        relativeLayout.addView(view);

      /*  Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();

        WindowManager wm = (WindowManager) getApplication().getSystemService(this.WINDOW_SERVICE); //获取屏幕高度
        int height = wm.getDefaultDisplay().getHeight();
        int windth = wm.getDefaultDisplay().getWidth();
        lp.width = windth;
        lp.height = height / 2;
        lp.gravity = Gravity.BOTTOM;//设置对话框置顶显示
        win.setAttributes(lp);*/
    }

    private void initView() {
        list = MyApplication.getInstance().getSendData();
        name = getIntent().getStringExtra("name");
        //找到布局控件容器
        relativeLayout = (RelativeLayout) findViewById(R.id.chartContain);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        chartTitle = (TextView) findViewById(R.id.chartTitle);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.activity_dialog_close_enter, R.anim.activity_dialog_close_exit);
        resizeActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void resizeActivity() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        setFinishOnTouchOutside(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.gravity = Gravity.CENTER;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //layoutParams.x = 200;
        layoutParams.width = (int) (displayMetrics.widthPixels * SMALL_WIN_W_SCALE);
        layoutParams.height = (int) (displayMetrics.heightPixels * SMALL_WIN_H_SCALE);

        layoutParams.dimAmount = 0.7f;
        layoutParams.alpha = 1.0f;
        getWindow().setAttributes(layoutParams);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = getWindow().getDecorView();
        if (view != null) {
            view.setBackgroundResource(R.drawable.dialog_activity_bg);
            view.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //去除Activity自带动画效果
        //overridePendingTransition(0, 0);
        overridePendingTransition(R.anim.activity_dialog_close_enter, R.anim.activity_dialog_close_exit);
    }

    public void lineView() {
        //同样是需要数据dataset和视图渲染器renderer
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rTwo = new XYSeriesRenderer();//(类似于一条线对象)
        double[] d = new double[list.size()];
        double[] d1 = new double[list.size()];

        if (name.equals("扬尘")) {
            series = new XYSeries("PM2.5");
            seriesTwo = new XYSeries("PM10");

            for (int i = 0; i < list.size(); i++) {
                String pm25ave = list.get(i).getPm25ave();
                String pm10ave = list.get(i).getPm10ave();
                if (!TextUtils.isEmpty(pm25ave)) {
                    series.add(i, Double.parseDouble(pm25ave));
                    d1[i] = Double.parseDouble(pm25ave);
                } else {
                    series.add(i, Double.parseDouble("0.0"));
                    d1[i] = 0.0;
                }
                if (!TextUtils.isEmpty(pm10ave)) {
                    seriesTwo.add(i, Double.parseDouble(pm10ave));
                    d[i] = Double.parseDouble(pm10ave);
                } else {
                    seriesTwo.add(i, Double.parseDouble("0.0"));
                    d[i] = 0.0;
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                PM25SUM += d1[i];
                PM10SUM += d[i];
            }
            rTwo.setColor(getResources().getColor(R.color.blue_head));//设置颜色
            rTwo.setPointStyle(PointStyle.CIRCLE);//设置点的样式
            rTwo.setFillPoints(true);//填充点（显示的点是空心还是实心）
            rTwo.setDisplayChartValues(true);//将点的值显示出来
            rTwo.setChartValuesSpacing(10);//显示的点的值与图的距离
            rTwo.setChartValuesTextSize(25);//点的值的文字大小

            rTwo.setFillBelowLine(true);//是否填充折线图的下方
            //  rTwo.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
            rTwo.setLineWidth(3);//设置线宽
            mRenderer.addSeriesRenderer(rTwo);
            mRenderer.setYTitle("ug/m3");//设置y轴的标题
            mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);//设置标签居Y轴的方向   好像是 个反反向
            mDataset.addSeries(series);
            mDataset.addSeries(seriesTwo);

            Arrays.sort(d);  //将集合里面的数据从小到大排序
            Arrays.sort(d1);

            if (d[0] < d1[0]) {
                numberMin = d[0];  //得到最小值
            } else {
                numberMin = d1[0];
            }
            if (d[list.size() - 1] < d1[list.size() - 1]) {
                numberMax = d1[list.size() - 1]; //得到最大值
            } else {
                numberMax = d[list.size() - 1]; //得到最大值
            }
            //这里再进行一次遍历  把各自最大值和最小值对应的时间找出来

            for (int i = 0; i < list.size(); i++) {
                String pm25ave = list.get(i).getPm25ave();
                String pm10ave = list.get(i).getPm10ave();
                if (!TextUtils.isEmpty(pm25ave)) {
                    if ((Double.parseDouble(pm25ave) == d1[list.size() - 1])) {
                        timeMax25 = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(pm25ave) == d1[0])) {
                        timeMin25 = list.get(i).getTm();
                    }
                }
                if (!TextUtils.isEmpty(pm10ave)) {
                    if ((Double.parseDouble(pm10ave) == d[list.size() - 1])) {
                        timeMax10 = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(pm10ave) == d[0])) {
                        timeMin10 = list.get(i).getTm();
                    }
                }
            }
            //在这里插入每一个刻度对应的数值  即每一个时间对应的数值
        } else if (name.equals("风速")) {
            series = new XYSeries("风速");
            for (int i = 0; i < list.size(); i++) {
                String speedave = list.get(i).getSpeedave();
                if (!TextUtils.isEmpty(speedave)) {
                    series.add(i, Double.parseDouble(speedave));
                    d[i] = Double.parseDouble(speedave);
                } else {
                    series.add(i, Double.parseDouble("0.0"));
                    d[i] = Double.parseDouble("0.0");
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                // mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                SUM += d[i];
            }

            mRenderer.setYTitle("m/s");//设置y轴的标题
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mDataset.addSeries(series);
            Arrays.sort(d);  //将集合里面的数据从小到大排序
            numberMax = d[list.size() - 1]; //得到最大值
            numberMin = d[0];  //得到最小值

            for (int i = 0; i < list.size(); i++) {
                String speedave = list.get(i).getSpeedave();
                if (!TextUtils.isEmpty(speedave)) {
                    if ((Double.parseDouble(speedave) == d[list.size() - 1])) {
                        timeMax = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(speedave) == d[0])) {
                        timeMin = list.get(i).getTm();
                    }
                }
            }
        } else if (name.equals("风向")) {
            series = new XYSeries("风向");
            for (int i = 0; i < list.size(); i++) {
                String directave = list.get(i).getDirectave();
                if (!TextUtils.isEmpty(directave)) {
                    series.add(i, Double.parseDouble(directave));
                    d[i] = Double.parseDouble(directave);
                } else {
                    series.add(i, Double.parseDouble("0.0"));
                    d[i] = Double.parseDouble("0.0");
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                SUM += d[i];
            }
            mRenderer.setYTitle("度");//设置y轴的标题
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mDataset.addSeries(series);
            Arrays.sort(d);  //将集合里面的数据从小到大排序
            numberMax = d[list.size() - 1]; //得到最大值
            numberMin = d[0];  //得到最小值

            for (int i = 0; i < list.size(); i++) {
                String directave = list.get(i).getDirectave();
                if (!TextUtils.isEmpty(directave)) {
                    if ((Double.parseDouble(directave) == d[list.size() - 1])) {
                        timeMax = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(directave) == d[0])) {
                        timeMin = list.get(i).getTm();
                    }
                }
            }
        } else if (name.equals("气压")) {
            series = new XYSeries("气压");
            for (int i = 0; i < list.size(); i++) {
                String pressave = list.get(i).getPressave();
                if (!TextUtils.isEmpty(pressave)) {
                    series.add(i, Double.parseDouble(pressave));
                    d[i] = Double.parseDouble(pressave);
                } else {
                    series.add(i, Double.parseDouble("0.0"));
                    d[i] = Double.parseDouble("0.0");
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                SUM += d[i];
            }
            mRenderer.setYTitle("kpa");//设置y轴的标题
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mDataset.addSeries(series);
            Arrays.sort(d);  //将集合里面的数据从小到大排序
            numberMax = d[list.size() - 1]; //得到最大值
            numberMin = d[0];  //得到最小值


            for (int i = 0; i < list.size(); i++) {
                String pressave = list.get(i).getPressave();
                if (!TextUtils.isEmpty(pressave)) {
                    if ((Double.parseDouble(pressave) == d[list.size() - 1])) {
                        timeMax = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(pressave) == d[0])) {
                        timeMin = list.get(i).getTm();
                    }
                }
            }
        } else if (name.equals("温度")) {
            series = new XYSeries("温度");
            for (int i = 0; i < list.size(); i++) {
                String temperave = list.get(i).getTemperave();
                if (!TextUtils.isEmpty(temperave)) {
                    series.add(i, Double.parseDouble(temperave));
                    d[i] = Double.parseDouble(temperave);
                } else {
                    series.add(i, 0.0);
                    d[i] = 0.0;
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                SUM += d[i];
            }
            mRenderer.setYTitle("摄氏度℃");//设置y轴的标题
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mDataset.addSeries(series);
            Arrays.sort(d);  //将集合里面的数据从小到大排序
            numberMax = d[list.size() - 1]; //得到最大值
            numberMin = d[0];  //得到最小值
            for (int i = 0; i < list.size(); i++) {
                String temperave = list.get(i).getTemperave();
                if (!TextUtils.isEmpty(temperave)) {
                    if ((Double.parseDouble(temperave) == d[list.size() - 1])) {
                        timeMax = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(temperave) == d[0])) {
                        timeMin = list.get(i).getTm();
                    }
                }
            }
        } else if (name.equals("湿度")) {
            series = new XYSeries("湿度");
            for (int i = 0; i < list.size(); i++) {
                String shiduave = list.get(i).getShiduave();
                if (!TextUtils.isEmpty(shiduave)) {
                    series.add(i, Double.parseDouble(shiduave));
                    d[i] = Double.parseDouble(shiduave);
                } else {
                    series.add(i, 0.0);
                    d[i] = 0.0;
                }
                mRenderer.addXTextLabel(i, list.get(i).getTm());
                //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
                SUM += d[i];
            }
            mRenderer.setYTitle("百分比%");//设置y轴的标题
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mDataset.addSeries(series);
            Arrays.sort(d);  //将集合里面的数据从小到大排序
            numberMax = d[list.size() - 1]; //得到最大值
            numberMin = d[0];  //得到最小值
            for (int i = 0; i < list.size(); i++) {
                String shiduave = list.get(i).getShiduave();
                if (!TextUtils.isEmpty(shiduave)) {
                    if ((Double.parseDouble(shiduave) == d[list.size() - 1])) {
                        timeMax = list.get(i).getTm();
                    }
                    if ((Double.parseDouble(shiduave) == d[0])) {
                        timeMin = list.get(i).getTm();
                    }
                }
            }
        }

        //设置图表的X轴的当前方向
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setXTitle("日期");//设置为X轴的标题
        mRenderer.setXLabelsPadding(30);//设置X轴下标签与X轴的距离
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小
        mRenderer.setApplyBackgroundColor(true); // 设置图表背景
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setLabelsColor(Color.BLACK); // 设置标签颜色
        mRenderer.setAxesColor(Color.BLACK); // 设置坐标轴颜色
        // x、y轴上刻度颜色
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        // mRenderer.setChartTitle(name + "走势图");//设置图表标题
        chartTitle.setText(name + "走势图");
        // mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小
        mRenderer.setLegendTextSize(20);//设置图例文本大小
        mRenderer.setPointSize(5f);//设置点的大小
        mRenderer.setYAxisMin(numberMin);//设置y轴最小值是
        mRenderer.setYAxisMax(numberMax);
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）
        mRenderer.setXAxisMax(5);
        mRenderer.setShowGrid(true);//显示网格
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月
        //然后在这里将标签数值 变成每一个对应的时间
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等
        mRenderer.setMargins(new int[]{10, 50, 50, 0});//设置视图位置  上左下右
        // mRenderer.setPanLimits(new double[]{-10, (list.size()) * 2, -10, numberMax});
        // 坐标滑动上、下限
        // mRenderer.setPanLimits(new double[]{numberMax,0, numberMin, list.size()});
        r.setColor(Color.BLUE);//设置颜色
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）
        r.setDisplayChartValues(true);//将点的值显示出来
        r.setChartValuesSpacing(10);//显示的点的值与图的距离
        r.setChartValuesTextSize(25);//点的值的文字大小
        r.setFillBelowLine(true);//是否填充折线图的下方
        //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
        r.setLineWidth(3);//设置线宽
        mRenderer.addSeriesRenderer(r);
        mRenderer.setZoomButtonsVisible(true);//是否显示放大缩小按钮
        view = ChartFactory.getLineChartView(this.getApplicationContext(), mDataset, mRenderer);
        //view.setBackground(getResources().getDrawable(R.mipmap.air));

        view.setBackgroundColor(Color.BLACK);
        //设置图表说明文本
        //对DOUBLE进行格式化  只保留2位小数
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        if (name.equals("扬尘")) {

            tv1.setText("PM2.5的最大值:" + d1[list.size() - 1] + "\n时间:" + timeMax25 + "\n最小值:" + d1[0] + "\n时间:" + timeMin25);
            tv1.setTextColor(getResources().getColor(R.color.blue_head));
            tv2.setTextColor(Color.BLUE);
            tv2.setText("PM10的最大值:" + d[list.size() - 1] + "\n时间:" + timeMax10 + "\n最小值:" + d[0] + "\n时间:" + timeMin10);
            tv3.setText("PM2.5平均值:" + df.format(PM25SUM / list.size()) + "\nPM10平均值:" + df.format(PM10SUM / list.size()));
        } else if (name.equals("风向")) {
            tv1.setText("最大值:" + numberMax + "(" + change(numberMax) + ")" + "\n时间:" + timeMax);
            tv2.setText("最小值:" + numberMin + "(" + change(numberMin) + ")" + "\n时间:" + timeMin);
            tv3.setText("平均值:" + df.format(SUM / list.size()) + "(" + change(SUM / list.size()) + ")");
        } else {
            tv1.setText("最大值:" + numberMax + "\n时间:" + timeMax);
            tv2.setText("最小值:" + numberMin + "\n时间:" + timeMin);
            tv3.setText("平均值:" + df.format(SUM / list.size()));
        }
    }

    private String change(double i) {
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
        } else

        {
            directave = "未知方位";
        }
        return directave;
    }

}