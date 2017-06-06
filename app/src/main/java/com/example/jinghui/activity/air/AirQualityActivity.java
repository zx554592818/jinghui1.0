package com.example.jinghui.activity.air;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
import com.example.jinghui.fragment.all.air.AirMonitorHistoryFragment;
import com.example.jinghui.fragment.all.air.AirMonitorStatementFragment;



/**
 * Created by 赵欣 on 2017/2/24.
 */
//空气质量
public class AirQualityActivity extends BaseActivity {
    private AirMonitorHistoryFragment airhistoryFragment = null;
    private AirMonitorStatementFragment airstatementFragment = null;
    private FragmentManager fragmentManager; //管理actvity 里面的Fragment 所提可以提成成员变量
    private RadioGroup radiobtn_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airmonitor);
        initView();
        initFragment();
    }

    //Frament框架
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        radiobtn_container = (RadioGroup) findViewById(R.id.radiobtn_container);
        radiobtn_container.setOnCheckedChangeListener(checkedChangeListener);
    }

    //页面初始化Fragment
    private void initFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (airhistoryFragment == null)
            airhistoryFragment = new AirMonitorHistoryFragment();
        if (!airhistoryFragment.isAdded())
            fragmentTransaction.add(R.id.fragment_container, airhistoryFragment, "AirhistoryFragment");
        if (!airhistoryFragment.isVisible())
            fragmentTransaction.show(airhistoryFragment);
        fragmentTransaction.commit();
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //必须每次获取  不然会commint()报错
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //这样做的好处是每次切换前面的界面还可以保持原来的数据和展示
            airhistoryFragment = (AirMonitorHistoryFragment) fragmentManager.findFragmentByTag("AirhistoryFragment");
            airstatementFragment = (AirMonitorStatementFragment) fragmentManager.findFragmentByTag("AirstatementFragment");
            //这里加这2行代码是保证在subHistoryFragment里面的时候   点击切换可以隐藏当前的fragment  如果不加下面这2行代码则跳转不了页面
            if (airhistoryFragment != null)
                fragmentTransaction.hide(airhistoryFragment);
            if (airstatementFragment != null)
                fragmentTransaction.hide(airstatementFragment);

            switch (checkedId) {
                case R.id.history_btn:
                    if (airhistoryFragment == null)
                        airhistoryFragment = new AirMonitorHistoryFragment();
                    if (!airhistoryFragment.isAdded())
                        fragmentTransaction.add(R.id.fragment_container, airhistoryFragment, "AirhistoryFragment");
                    if (!airhistoryFragment.isVisible())
                        fragmentTransaction.show(airhistoryFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.statement_btn:
                    if (airstatementFragment == null)
                        airstatementFragment = new AirMonitorStatementFragment();
                    if (!airstatementFragment.isAdded())
                        fragmentTransaction.add(R.id.fragment_container, airstatementFragment, "AirstatementFragment");
                    if (!airstatementFragment.isVisible())
                        fragmentTransaction.show(airstatementFragment);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };

    public void backHome(View view) {
        //返回主界面
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
