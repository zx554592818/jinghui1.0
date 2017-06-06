package com.example.jinghui.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import java.util.List;

/**
 * Created by zhaoxin on 2017-04-14.
 */
//单个监测点的图表绘制
public class ShowSingleChartActivity extends BaseActivity {
    private GraphicalView view;
    private ArrayList<HistoryDatabaseEntity> list;
    private String name;
    private XYSeries series, seriesTwo, seriesThree, seriesFour, seriesFive, seriesSix;
    private Double numberMin;
    private Double numberMax;
    private RelativeLayout relativeLayout;
    public static final double SMALL_WIN_H_SCALE = 0.67;
    public static final double SMALL_WIN_W_SCALE = 0.72;
    private TextView chartTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chart);
        initView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        //  actionBar.setHomeAsUpIndicator(R.drawable.close_button);
        //绘制图表
        lineView();
        //往容器添加图表控件
        relativeLayout.addView(view);

    }

    private void initView() {
        list = MyApplication.getInstance().getSendData();
        name = getIntent().getStringExtra("name");
        //找到布局控件容器
        relativeLayout = (RelativeLayout) findViewById(R.id.chartContain);
        chartTitle = (TextView) findViewById(R.id.chartTitle);
        chartTitle.setText("走势图一览");
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
        List<XYSeriesRenderer> xlist = new ArrayList<>();
        //同样是需要数据dataset和视图渲染器renderer
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rTwo = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rThree = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rFour = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rFive = new XYSeriesRenderer();//(类似于一条线对象)
        XYSeriesRenderer rSix = new XYSeriesRenderer();//(类似于一条线对象)
        r.setColor(Color.RED);//设置颜色
        rTwo.setColor(Color.BLUE);
        rThree.setColor(Color.YELLOW);
        rFour.setColor(Color.GREEN);
        rFive.setColor(Color.GRAY);
        rSix.setColor(Color.BLACK);
        xlist.add(r);
        xlist.add(rTwo);
        xlist.add(rThree);
        xlist.add(rFour);
        xlist.add(rFive);
        xlist.add(rSix);
        series = new XYSeries("PM2.5");
        seriesTwo = new XYSeries("PM10");
        seriesThree = new XYSeries("风速");
        seriesFour = new XYSeries("气压");
        seriesFive = new XYSeries("温度");
        seriesSix = new XYSeries("湿度");
        mDataset.addSeries(series);
        mDataset.addSeries(seriesTwo);
        mDataset.addSeries(seriesThree);
        mDataset.addSeries(seriesFour);
        mDataset.addSeries(seriesFive);
        mDataset.addSeries(seriesSix);

        for (int i = 0; i < list.size(); i++) {
            //这里有数据转换异常
            //这里处理数据的时候必须要判断该数据是否存在 如果为""会报异常
            //或者做一个操作  将这个集合的空值都变成0.0
            series.add(i, Double.parseDouble(list.get(i).getPm25ave()));
            seriesTwo.add(i, Double.parseDouble(list.get(i).getPm10ave()));
            seriesThree.add(i, Double.parseDouble(list.get(i).getSpeedave()));
            seriesFour.add(i, Double.parseDouble(list.get(i).getPressave()));
            seriesFive.add(i, Double.parseDouble(list.get(i).getTemperave()));
            seriesSix.add(i, Double.parseDouble(list.get(i).getShiduave()));
            mRenderer.addXTextLabel(i, list.get(i).getTm());
            //mRenderer.addXTextLabel(list.size(), list.get(list.size() - 1).getTm());
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
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小
        mRenderer.setLegendTextSize(20);//设置图例文本大小
        mRenderer.setPointSize(5f);//设置点的大小
        // mRenderer.setYAxisMin(numberMin);//设置y轴最小值是
        //  mRenderer.setYAxisMax(numberMax);
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）
        mRenderer.setXAxisMax(5);
        mRenderer.setShowGrid(true);//显示网格
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月
        //然后在这里将标签数值 变成每一个对应的时间
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setMargins(new int[]{10, 50, 50, 0});//设置视图位置  上左下右
        // mRenderer.setPanLimits(new double[]{-10, (list.size()) * 2, -10, numberMax});
        // 坐标滑动上、下限
        for (XYSeriesRenderer xySeriesRenderer : xlist) {
            //r.setColor(Color.BLUE);//设置颜色
            xySeriesRenderer.setPointStyle(PointStyle.CIRCLE);//设置点的样式
            xySeriesRenderer.setFillPoints(true);//填充点（显示的点是空心还是实心）
            xySeriesRenderer.setDisplayChartValues(true);//将点的值显示出来
            xySeriesRenderer.setChartValuesSpacing(10);//显示的点的值与图的距离
            xySeriesRenderer.setChartValuesTextSize(25);//点的值的文字大小
            //xySeriesRenderer.setFillBelowLine(true);//是否填充折线图的下方
            //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
            xySeriesRenderer.setLineWidth(3);//设置线宽

            mRenderer.addSeriesRenderer(xySeriesRenderer);
        }

        mRenderer.setZoomButtonsVisible(true);//是否显示放大缩小按钮
        view = ChartFactory.getLineChartView(this.getApplicationContext(), mDataset, mRenderer);
        view.setBackgroundColor(Color.BLACK);
        //设置图表说明文本

    }


}