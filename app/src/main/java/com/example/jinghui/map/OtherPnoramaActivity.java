package com.example.jinghui.map;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.model.BaiduPoiPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaView.ImageDefinition;
import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
import com.example.jinghui.ui.ToastShow;


public class OtherPnoramaActivity extends BaseActivity {
    private double latitude, longtiude;
    private PanoramaView panoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setTranslucentStatus();

        initBMapManager();
        setContentView(R.layout.activity_other_pnorama);
        initPanoramaView();
    }

    @Override
    protected void onPause() {
        panoramaView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        panoramaView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        panoramaView.destroy();
        super.onDestroy();
    }

    /**
     * @author mikyou
     * ��ʾȫ��
     */
    private void initPanoramaView() {
        SearchInfo info = (SearchInfo) getIntent().getSerializableExtra("info");//获取传递过来的serachInfo对象
        panoramaView = (PanoramaView) findViewById(R.id.panorama);
        panoramaView.setPanoramaImageLevel(ImageDefinition.ImageDefinitionMiddle);//表示展示的内景是中等分辨率,ImageDefinitionHigh是高分辨率
        if (info.getUid() != null) {
            String uid = info.getUid();//得到设置内景必须的uid
            PanoramaRequest request = PanoramaRequest.getInstance(OtherPnoramaActivity.this);
            BaiduPoiPanoData poiPanoData = request.getPanoramaInfoByUid(uid);//将该uid设置给内景API
            if (poiPanoData.hasInnerPano()) {//判断该POI是否有内景
                panoramaView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_INTERIOR);
                panoramaView.setIndoorAlbumGone();//除去内景相册
                panoramaView.setIndoorAlbumVisible();//展示内景相册
            } else if (poiPanoData.hasStreetPano()) {//判断该POI是否有外景，就只能通过经纬度来显示外景
                panoramaView.setPanorama(info.getLongtiude(), info.getLatitude());//没有内景就通过经纬度来展示街景
                new ToastShow(OtherPnoramaActivity.this).toastShow("有该地方的外景,请等待");
            } else {
                new ToastShow(OtherPnoramaActivity.this).toastShow("网络不给力，无法加载全景");
            }
        }
       else {
            panoramaView.setPanorama(info.getLongtiude(), info.getLatitude());//没有内景就通过经纬度来展示街景
        }



    }

    private void initBMapManager() {
        MyApplication app = MyApplication.getInstance();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }
    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);
        tintManager.setNavigationBarTintEnabled(true);

    }

    public void getback(View view) {
        finish();
    }
}
