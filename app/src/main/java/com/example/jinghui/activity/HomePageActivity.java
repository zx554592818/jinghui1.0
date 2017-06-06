package com.example.jinghui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jinghui.R;
import com.example.jinghui.map.MapActivity;
import com.example.jinghui.ui.ToastShow;
import com.example.jinghui.model.DrawerLayoutData;
import com.example.jinghui.fragment.all.air.AirHomePageFragment;
import com.example.jinghui.fragment.all.canyin.CanyinHomePageFragment;
import com.example.jinghui.fragment.all.smoke.YanchenHomePageFragment;
import com.example.jinghui.fragment.all.vocs.VocsHomePageFragment;
import com.example.jinghui.fragment.all.waste.WasteWaterHomePageFragment;
import com.example.jinghui.fragment.all.water.RiverHomePageFragment;

import java.util.ArrayList;

/**
 * Created by zhaoxin on 2017-05-18.
 */
//主页面
public class HomePageActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private AirHomePageFragment airHomePageFragment;
    private RadioGroup radiobtn_homePage;
    private CanyinHomePageFragment canyinHomePageFragment;
    private YanchenHomePageFragment yanchenFragment;
    private VocsHomePageFragment vocsHomePageFragment;
    private WasteWaterHomePageFragment wasteWaterHomePageFragment;
    private RiverHomePageFragment riverHomePageFragment;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private RelativeLayout right;
    private static final int REQUEST_CODE_LOCATION = 1; //请求码  定位权限
    private static String[] PERMISSIONS_LOCATION = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //这个界面我要做首页展示  今天最新的天气状况 和各指数的数值
        setContentView(R.layout.activity_homepageone);
        initView();
        onClickListen();
        initFragment();
    }

    private void onClickListen() {
        radiobtn_homePage.setOnCheckedChangeListener(checkedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            airHomePageFragment = (AirHomePageFragment) fragmentManager.findFragmentByTag("AirHomePageFragment");
            yanchenFragment = (YanchenHomePageFragment) fragmentManager.findFragmentByTag("YanchenHomePageFragment");
            canyinHomePageFragment = (CanyinHomePageFragment) fragmentManager.findFragmentByTag("CanyinHomePageFragment");
            vocsHomePageFragment = (VocsHomePageFragment) fragmentManager.findFragmentByTag("VocsHomePageFragment");
            wasteWaterHomePageFragment = (WasteWaterHomePageFragment) fragmentManager.findFragmentByTag("WasteWaterHomePageFragment");
            riverHomePageFragment = (RiverHomePageFragment) fragmentManager.findFragmentByTag("RiverHomePageFragment");
            if (airHomePageFragment != null) {
                fragmentTransaction.hide(airHomePageFragment);
            }
            if (yanchenFragment != null) {
                fragmentTransaction.hide(yanchenFragment);
            }
            if (canyinHomePageFragment != null) {
                fragmentTransaction.hide(canyinHomePageFragment);
            }
            if (vocsHomePageFragment != null) {
                fragmentTransaction.hide(vocsHomePageFragment);
            }
            if (wasteWaterHomePageFragment != null) {
                fragmentTransaction.hide(wasteWaterHomePageFragment);
            }
            if (riverHomePageFragment != null) {
                fragmentTransaction.hide(riverHomePageFragment);
            }
            switch (checkedId) {
                case R.id.airSystem:
                    //空气系统
                    if (airHomePageFragment == null) {
                        airHomePageFragment = new AirHomePageFragment();
                    }
                    if (!airHomePageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, airHomePageFragment, "AirHomePageFragment");
                    }
                    if (!airHomePageFragment.isVisible()) {
                        fragmentTransaction.show(airHomePageFragment);
                    }
                    break;
                case R.id.yanchenSystem:
                    //烟尘系统
                    if (yanchenFragment == null) {
                        yanchenFragment = new YanchenHomePageFragment();
                    }
                    if (!yanchenFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, yanchenFragment, "YanchenHomePageFragment");
                    }
                    if (!yanchenFragment.isVisible()) {
                        fragmentTransaction.show(yanchenFragment);
                    }

                    break;
                case R.id.canyinSystem:
                    //餐饮系统
                    if (canyinHomePageFragment == null) {
                        canyinHomePageFragment = new CanyinHomePageFragment();
                    }
                    if (!canyinHomePageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, canyinHomePageFragment, "CanyinHomePageFragment");
                    }
                    if (!canyinHomePageFragment.isVisible()) {
                        fragmentTransaction.show(canyinHomePageFragment);
                    }
                    break;
                case R.id.vocSystem:
                    //vocs系统
                    if (vocsHomePageFragment == null) {
                        vocsHomePageFragment = new VocsHomePageFragment();
                    }
                    if (!vocsHomePageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, vocsHomePageFragment, "VocsHomePageFragment");
                    }
                    if (!vocsHomePageFragment.isVisible()) {
                        fragmentTransaction.show(vocsHomePageFragment);
                    }
                    break;
                case R.id.wasteSystem:
                    //废水系统
                    if (wasteWaterHomePageFragment == null) {
                        wasteWaterHomePageFragment = new WasteWaterHomePageFragment();
                    }
                    if (!wasteWaterHomePageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, wasteWaterHomePageFragment, "WasteWaterHomePageFragment");
                    }
                    if (!wasteWaterHomePageFragment.isVisible()) {
                        fragmentTransaction.show(wasteWaterHomePageFragment);
                    }
                    break;
                case R.id.waterSystem:
                    //河流系统
                    if (riverHomePageFragment == null) {
                        riverHomePageFragment = new RiverHomePageFragment();
                    }
                    if (!riverHomePageFragment.isAdded()) {
                        fragmentTransaction.add(R.id.homePageFragment, riverHomePageFragment, "RiverHomePageFragment");
                    }
                    if (!riverHomePageFragment.isVisible()) {
                        fragmentTransaction.show(riverHomePageFragment);
                    }
                    break;
            }
            fragmentTransaction.commit();
        }
    };

    //页面初始化Fragment
    private void initFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (airHomePageFragment == null) {
            airHomePageFragment = new AirHomePageFragment();
        }
        if (!airHomePageFragment.isAdded()) {
            fragmentTransaction.add(R.id.homePageFragment, airHomePageFragment, "AirHomePageFragment");
        }
        if (!airHomePageFragment.isVisible()) {
            fragmentTransaction.show(airHomePageFragment);
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        radiobtn_homePage = (RadioGroup) findViewById(R.id.radiobtn_homePage);
        listView = (ListView) findViewById(R.id.left_listview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        right = (RelativeLayout) findViewById(R.id.right);
        initLeftDrawer(); //初始化左边抽屉
    }

    public void showMap(View view) {
        verifyStoragePermissions();
    }
    //6.0开始 权限需要代码里面也做动态申请
    public void verifyStoragePermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                //检测是否有定位的权限
                int permission = ActivityCompat.checkSelfPermission(this,
                        "android.permission.ACCESS_FINE_LOCATION");
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 没有定位权限，去申请定位的权限，会弹出对话框
                    ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_CODE_LOCATION);
                } else {
                    //如果已经有了定位的权限则怎么样
                    Intent intent = new Intent(HomePageActivity.this, MapActivity.class);
                    startActivity(intent);
                }
            } else {
                //如果是低版本的则不需要动态申请了 直接通过
                Intent intent = new Intent(HomePageActivity.this, MapActivity.class);
                startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //如果同意选择怎么样
            Intent intent = new Intent(HomePageActivity.this, MapActivity.class);
            startActivity(intent);

        } else {
            //拒绝则不跳转
        }
    }

    public void selectRegion(View view) {
        //这里更换地区  目前就一个濮阳
        //点击打开右边边抽屉
        drawerLayout.openDrawer(Gravity.RIGHT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listView的条目点击 根据地名切换Fragment
                //如果是濮阳则什么都不做 关闭抽屉就行
                //如果切换城市到武汉  判断当前的RadioButton 哪一个是被勾选状态 哪一个被勾选则
                switch (position) {
                    case 0:
                        //点击第一栏濮阳什么都不做 关闭抽屉即可
                        break;
                    case 1:
                        new ToastShow(HomePageActivity.this).toastShow("暂未开放");
                        break;
                    case 2:
                        //跳转设置界面
                        Intent intent = new Intent(HomePageActivity.this, InstallActivity.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    private void initLeftDrawer() {
        //背景设置透明度
        right.getBackground().setAlpha(100);
        final ArrayList<DrawerLayoutData> list = new ArrayList<>();
        list.add(new DrawerLayoutData(R.mipmap.dingwei, "濮阳"));
        list.add(new DrawerLayoutData(R.mipmap.dingwei, "武汉"));
        list.add(new DrawerLayoutData(R.mipmap.shezhi, "设置"));
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                if (list == null) {
                    return 0;
                }
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                if (list != null) {
                    return list.get(position);
                }
                return null;
            }

            @Override
            public long getItemId(int position) {
                //这里是得到条目所在行的id 点击条目好辨认
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(parent.getContext(), R.layout.drawerlayout_item, null);
                TextView textView = (TextView) convertView.findViewById(R.id.drawer_tv);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.drawer_iv);
                textView.setText(list.get(position).getText());
                imageView.setImageResource(list.get(position).getImageView());
                return convertView;
            }
        });

    }
}
