package com.example.jinghui.map;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaView.ImageDefinition;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.example.jinghui.ui.MyApplication;
import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;


public class PanoramaActivity extends BaseActivity implements PanoramaViewListener {

    private double latitude, longtiude;
    private PanoramaView panoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBMapManager();
        setContentView(R.layout.activity_panorama);
        getSupportActionBar().hide();
        initPanoramaView();
    }

    private void initBMapManager() {

        MyApplication app = MyApplication.getInstance();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }
    }

    private void initPanoramaView() {
        double[] lats = getIntent().getDoubleArrayExtra("panoramaLatLng");
        Toast.makeText(PanoramaActivity.this, "latitude:" + lats[0] + "    longtiude:" + lats[1], Toast.LENGTH_LONG).show();
        latitude = lats[0];
        longtiude = lats[1];
        panoramaView = (PanoramaView) findViewById(R.id.panorama);
        panoramaView.setPanorama(longtiude, latitude);
        panoramaView.setPanoramaViewListener(this);
        panoramaView.setPanoramaImageLevel(ImageDefinition.ImageDefinitionMiddle);
        //	panoramaView.setPanoramaByUid("54f6515b173ab6cc0d3dda17", PanoramaView.PANOTYPE_INTERIOR);
        panoramaView.setIndoorAlbumGone();
        panoramaView.setIndoorAlbumVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        panoramaView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        panoramaView.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoramaView.onResume();
    }


    @Override
    public void onDescriptionLoadEnd(String s) {

    }

    @Override
    public void onLoadPanoramaBegin() {

    }

    @Override
    public void onLoadPanoramaEnd(String arg0) {

    }

    @Override
    public void onLoadPanoramaError(String arg0) {

    }

    @Override
    public void onMessage(String s, int i) {

    }

    @Override
    public void onCustomMarkerClick(String s) {

    }

    public void getBack(View view) {
        finish();
    }

}
