package com.example.jinghui.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.jinghui.R;
import com.example.jinghui.activity.BaseActivity;
import com.example.jinghui.ui.ToastShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxin on 2017-05-19.
 */

public class MapActivity extends BaseActivity implements View.OnClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener {
    private MapView myMapView;
    private BaiduMap myBaiduMap;
    private float current;
    private View defaultBaiduMapScaleButton = null;
    private View defaultBaiduMapLogo = null;
    private View defaultBaiduMapScaleUnit = null;
    private String[] types = {"普通地图", "卫星地图", "热力图"};
    private ImageView mapRoad, selectMapType, mapPanorama;
    private ImageView addScale, lowScale;
    private ImageView myLoaction;
    private ImageView selectLocationMode;
    private TextView locationText;
    private LocationClient myLocationClient;
    private String[] LocationModeString = {"罗盘", "跟随", "基本", "3D实景"};
    private String[] LocationMode = {"去该地", "查看"};
    private boolean isFirstIn = true;
    private double latitude, longtitude;
    private String locationTextString;
    private BitmapDescriptor myBitmapLocation;
    private MyOrientationListener myOrientationListener;
    private float myCurrentX;
    private ImageView addMarkers;
    private BitmapDescriptor myMark;
    private List<Info> marks;
    private List<SearchInfo> searchInfoLists;
    private Info MyMarker;
    private SearchInfo mInfos;
    public BDLocation location1;
    private MyLocationListener myLocationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mapview);
        initMapView();
        changeDefaultBaiduMapView();
        initMapLocation();
    }

    private void initMapView() {

        myMapView = (MapView) findViewById(R.id.bmapView);
        myBaiduMap = myMapView.getMap();
        myLocationClient = new LocationClient(this);//初始化定位对象服务
        myMapView.showZoomControls(false);//隐藏自带缩放
        mapRoad = (ImageView) findViewById(R.id.road_condition);
        selectMapType = (ImageView) findViewById(R.id.map_type);
        addScale = (ImageView) findViewById(R.id.add_scale);
        lowScale = (ImageView) findViewById(R.id.low_scale);
        myLoaction = (ImageView) findViewById(R.id.my_location);
        locationText = (TextView) findViewById(R.id.mylocation_text);
        selectLocationMode = (ImageView) findViewById(R.id.map_location);
        addMarkers = (ImageView) findViewById(R.id.map_marker);
        mapPanorama = (ImageView) findViewById(R.id.map_panorama);

        searchInfoLists = new ArrayList<SearchInfo>();
        mapRoad.setOnClickListener(this);
        selectMapType.setOnClickListener(this);
        addScale.setOnClickListener(this);
        lowScale.setOnClickListener(this);
        myLoaction.setOnClickListener(this);
        selectLocationMode.setOnClickListener(this);
        addMarkers.setOnClickListener(this);
        mapPanorama.setOnClickListener(this);
        myBaiduMap.setOnMarkerClickListener(this);
        myBaiduMap.setOnMapClickListener(this);
    }

    public void mapBack(View view) {
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        myBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //这是地图里面Marker的点击回调
        //因为我做的项目上面有2种Marker 所以设置了各自的标题 类似于ID 好辨认做点击事件
        Bundle bundle = marker.getExtraInfo();
        MyMarker = (Info) bundle.getSerializable("mark");
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setIcon(R.drawable.track_collect_running)
                .setTitle("选择")
                .setItems(LocationMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mode = LocationMode[which];
                        if (mode.equals("去该地")) {
                        } else if (mode.equals("查看")) {
                            SearchInfo searchMarker = new SearchInfo(MyMarker.getName(), MyMarker.getLatitude(), MyMarker.getLongitude(), null, null, null);
                            Intent intent = new Intent(MapActivity.this, OtherPnoramaActivity.class);
                            intent.putExtra("info", searchMarker);
                            startActivity(intent);
                        }
                    }
                }).show();
        return true;
    }

    //设置百度图标为不可见
    private void changeDefaultBaiduMapView() {
        changeInitialzeScaleView();
        for (int i = 0; i < myMapView.getChildCount(); i++) {
            View child = myMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                defaultBaiduMapScaleButton = child;
                break;
            }
        }
//       defaultBaiduMapScaleButton.setVisibility(View.GONE);
        defaultBaiduMapLogo = myMapView.getChildAt(1);
        defaultBaiduMapLogo.setPadding(300, 0, 100, 100);
        myMapView.removeViewAt(1);
        defaultBaiduMapScaleUnit = myMapView.getChildAt(2);
        defaultBaiduMapScaleUnit.setPadding(100, 0, 115, 200);
    }

    private void changeInitialzeScaleView() {
        MapStatusUpdate factory = MapStatusUpdateFactory.zoomTo(15.0f);
        myBaiduMap.animateMapStatus(factory);
    }

    //初始化
    public void initMapLocation() {
        myLocationListener = new MyLocationListener();
        myLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        myLocationClient.setLocOption(option);
        //设置方向监控
        useLocationOrientationListener();
    }

    //地图初始化的时候会执行这里
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            location1 = location;
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(myCurrentX)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            myBaiduMap.setMyLocationData(data);
            changeLocationIcon();//给初始化当前位置设置图标
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            if (isFirstIn) {

                getMyLatestLocation(latitude, longtitude);
                isFirstIn = false;
                //得到省市区街道地址
                if (location.getProvince() == null && location.getCity() == null && location.getDistrict() == null && location.getStreet() == null) {
                    locationTextString = "未知地址";
                } else {
                    locationTextString = "" + location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet();
                }
                toast(locationTextString);
                //第一次定位的位置
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationText.setText(locationTextString);
                    }
                });

            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //给初始化当前位置设置图标
    private void changeLocationIcon() {
        myBitmapLocation = BitmapDescriptorFactory.fromResource(R.drawable.mylocation);
        if (isFirstIn) {
            //第一次进来设置为普通定位
            MyLocationConfiguration config = new
                    MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
            myBaiduMap.setMyLocationConfigeration(config);
        }
    }

    private void useLocationOrientationListener() {
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setMyOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                myCurrentX = x;
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //路况图
            case R.id.road_condition:
                if (myBaiduMap.isTrafficEnabled()) {
                    myBaiduMap.setTrafficEnabled(false);
                    mapRoad.setImageResource(R.drawable.icon_roadcondition_off);
                } else {
                    myBaiduMap.setTrafficEnabled(true);
                    mapRoad.setImageResource(R.drawable.icon_roadcondition_on);
                }
                break;
            //地图类型切换
            case R.id.map_type:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.icon)
                        .setTitle("选择地图类型")
                        .setItems(types, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String select = types[which];
                                if (select.equals("普通地图")) {
                                    myBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                                } else if (select.equals("卫星地图")) {
                                    myBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                                } else if (select.equals("热力图") || select.equals("热力图")) {
                                    if (myBaiduMap.isBaiduHeatMapEnabled()) {
                                        myBaiduMap.setBaiduHeatMapEnabled(false);
                                        new ToastShow(MapActivity.this).toastShow("热力关闭");
                                        types[which] = "热力图";
                                    } else {
                                        myBaiduMap.setBaiduHeatMapEnabled(true);
                                        new ToastShow(MapActivity.this).toastShow("热力打开");
                                        types[which] = "热力图";
                                    }
                                }
                            }
                        }).show();
                break;
            //地图放大
            case R.id.add_scale:
                current += 0.5f;
                MapStatusUpdate
                        factory = MapStatusUpdateFactory.zoomTo(15.0f + current);
                myBaiduMap.animateMapStatus(factory);
                break;
            //地图缩小
            case R.id.low_scale:
                current -= 0.5f;
                MapStatusUpdate
                        factory2 = MapStatusUpdateFactory.zoomTo(15.0f + current);
                myBaiduMap.animateMapStatus(factory2);
                break;
            //定位到当前位置
            case R.id.my_location:
                initMapLocation();
                if (!myLocationClient.isStarted()) {
                    myLocationClient.start();
                }
                getMyLatestLocation(latitude, longtitude);
                locationTextString = "" + location1.getProvince() + location1.getCity() + location1.getDistrict() + location1.getStreet();
                toast(locationTextString);
                locationText.setText(locationTextString);
                break;
            //地图定位样式
            case R.id.map_location:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setIcon(R.drawable.track_collect_running)
                        .setTitle("定位样式")
                        .setItems(LocationModeString, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mode = LocationModeString[which];
                                if (mode.equals("罗盘")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("跟随")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("基本")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("3D实景") || mode.equals("3D实景")) {
                                    if (mode.equals("3D实景")) {
                                        UiSettings mUiSettings = myBaiduMap.getUiSettings();
                                        mUiSettings.setCompassEnabled(true);
                                        LocationModeString[which] = "3D实景)";

                                    } else {
                                        MyLocationConfiguration config = new
                                                MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, myBitmapLocation);
                                        myBaiduMap.setMyLocationConfigeration(config);
                                        MyLocationConfiguration config2 = new
                                                MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
                                        myBaiduMap.setMyLocationConfigeration(config2);
                                        LocationModeString[which] = "3D实景";
                                    }
                                }
                            }
                        }).show();
                break;
            //定位到地图标注点
            case R.id.map_marker:
                initMapMarks();
                addOverLayer();
                break;
            //当前位置全景模式
            case R.id.map_panorama:
                Intent intent = new Intent(this, PanoramaActivity.class);
                intent.putExtra("panoramaLatLng", new double[]{latitude, longtitude});
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //设置项目地址的Marker点
    private void initMapMarks() {
        marks = new ArrayList<Info>();
        marks.add(new Info(35.804221, 115.193516, "东北庄杂技园"));
        marks.add(new Info(35.775822, 115.129155, "濮东产业集聚"));
        marks.add(new Info(35.764684, 115.08515, "大庆办"));
        marks.add(new Info(35.766087, 115.077204, "胜利办"));
        marks.add(new Info(35.777759, 115.04089, "黄河办"));
        marks.add(new Info(35.78912, 115.207792, "岳村镇"));
        marks.add(new Info(35.767846, 115.025174, "人民办"));
        marks.add(new Info(35.775501, 115.131435, "濮东办"));
        marks.add(new Info(35.763371, 115.043965, "建设办"));
        marks.add(new Info(35.777951, 115.064904, "长庆办"));
        marks.add(new Info(35.795494, 115.104004, "孟轲乡"));
        marks.add(new Info(35.785358, 115.068549, "中原办"));
        marks.add(new Info(35.773638, 115.095451, "任丘办"));
        marks.add(new Info(35.782855, 115.146358, "龙湖澜岸1号"));
        marks.add(new Info(35.782793, 115.146363, "龙湖澜岸2号"));
        marks.add(new Info(35.78222, 115.11367, "苏北小区"));
        marks.add(new Info(35.78282, 115.14096, "东湖湾2号"));
        marks.add(new Info(35.783106, 115.140938, "东湖湾1号"));
        marks.add(new Info(35.750527, 115.035582, "中央公园1号"));
        marks.add(new Info(35.750455, 115.035599, "中央公园2号"));
        marks.add(new Info(35.821082, 115.119816, "后铁炉"));
        marks.add(new Info(35.752791, 115.07797, "荣域花果园工"));
        marks.add(new Info(35.775147, 115.13331, "检疫局"));
        marks.add(new Info(35.77448, 115.137989, "君悦兰庭"));
    }

    private void addOverLayer() {
        myBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        OverlayOptions text;
        myMark = BitmapDescriptorFactory.fromResource(R.drawable.mark);
        for (int i = 0; i < marks.size(); i++) {
            latLng = new LatLng(marks.get(i).getLatitude(), marks.get(i).getLongitude());
            //添加图片
            options = new MarkerOptions().position(latLng).icon(myMark);
            //添加文字
            text = new TextOptions().text(marks.get(i).getName()).fontSize(24).position(latLng);
            myBaiduMap.addOverlay(text);
            marker = (Marker) myBaiduMap.addOverlay(options);
            //因为我做的项目上面有2种Marker 所以设置了各自的标题 类似于ID 好辨认做点击事件
            Bundle bundle = new Bundle();
            bundle.putSerializable("mark", marks.get(i));
            marker.setExtraInfo(bundle);
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.animateMapStatus(msu);
    }

    private void getMyLatestLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        //这里是以latLng为中心点展示地图
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.animateMapStatus(msu);

    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        super.onDestroy();
        myMapView.onDestroy();
    }

    @Override
    public void onStart() {
        myBaiduMap.setMyLocationEnabled(true);
        if (!myLocationClient.isStarted()) {
            //定位服务开启
            myLocationClient.start();

        }
        //地图开启时 方向监控开启
        myOrientationListener.start();
        super.onStart();
    }
    @Override
    public void onStop() {

        myBaiduMap.setMyLocationEnabled(false);
        //定位服务关闭
        myLocationClient.stop();
        //地图结束时 方向监控也结束
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myMapView.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myMapView.onPause();
    }
}
