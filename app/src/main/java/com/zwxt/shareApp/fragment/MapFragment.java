package com.zwxt.shareApp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.Rom;
import com.zwxt.shareApp.ShareApplication;
import com.zwxt.shareApp.activity.DetailsActivity;
import com.zwxt.shareApp.instance.PositionListInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/8/14.
 */

public class MapFragment extends BaseFragment implements LocationSource,
        AMapLocationListener, OnCameraChangeListener,AMap.OnInfoWindowClickListener {
    private AMap aMap;
    @Bind(R.id.map)
    MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AsyncHttpClient asyncHttpClient;
    private LatLng locationLog ;
    private String token ;

    private String city_name ;

    private float distance;

    private LocationBroadCastReceiver broadCastReceiver = new LocationBroadCastReceiver();

//    private int city_id;

    private List<PositionListInstance> listInstances;

    @Override
    protected int getLayoutId() {
        return R.layout.locationsource_activity;
    }

    @Override
    protected void initView() {
        mStatusBarView.setVisibility(View.GONE);
        mapView.onCreate(saved);
        if (Rom.isEmui()) {
            System.out.println("isEMUI");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.black));
        } else if (Rom.isFlyme()) {
            System.out.println("isFlyme");
            setMeizuStatusBarDarkIcon(getActivity(), true);
        } else if (Rom.isMiui()) {
            System.out.println("isMiui");
            setMiuiStatusBarDarkMode(getActivity(), true);
        } else {
            System.out.println("isis");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.black));
        }
//        setMeizuStatusBarDarkIcon(getActivity(),true);
//        setMiuiStatusBarDarkMode(getActivity(),true);
//        Window window = getActivity().getWindow();
//        window.setStatusBarColor(getResources().getColor(android.R.color.black));

        if (ShareApplication.locationClient.isStarted()){

        } else {
            ShareApplication.locationClient.startLocation();
        }

    }

    @Override
    protected void initData() {

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        asyncHttpClient = new AsyncHttpClient();
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);

        listInstances = new ArrayList<>();

        positionList();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOCATION_ACTION);
        getActivity().registerReceiver(broadCastReceiver, intentFilter);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("点击："+marker.getTitle());
        for (int i =0  ; i < listInstances.size() ; i++){
            String store_name = listInstances.get(i).getStore_name();
            if (store_name.equals(marker.getTitle())){
                Intent intent = new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra("houseName",store_name);
                intent.putExtra("id",listInstances.get(i).getId());
                intent.putExtra("chooseHouse", false);
                startActivity(intent);
            }
        }
    }


    class LocationBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.LOCATION_ACTION)){
                city_name = intent.getStringExtra("city_name");
                getCityID(city_name);
//                location_text.setText(city_name);
                ShareApplication.locationClient.stopLocation();
            }
        }
    }

    private void getCityID(final String city_name){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/area?city=all&fid=",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("allCity:"+s);
                System.out.println("city_name:"+city_name);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Objcet = success_Array.optJSONObject(i);
                                int id = success_Objcet.optInt("id");
                                String area_name = success_Objcet.optString("area_name");
                                int fid = success_Objcet.optInt("fid");
                                if (!city_name.equals("")){
                                    if (area_name.contains(city_name) || city_name.contains(area_name)){
                                        ShareApplication.cityId = id;
                                    } else {
                                        ShareApplication.cityId = 4;
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("allCity接口不通或网络异常："+throwable.getMessage());
            }

        });
    }

    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));


        // aMap.setMyLocationType()
    }

    @Override
    protected void initListener() {

    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                System.out.println("维度："+aMapLocation.getLongitude()+"|"+aMapLocation.getLatitude());
                locationLog = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                positionList();

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                positionList();
            }
        }
    }


    private void positionList(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/store/positionList?city="+ ShareApplication.cityId,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("维度："+s);
                listInstances.clear();
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length()>0){
                            for (int i = 0 ; i < success_Array.length() ; i ++){
                                JSONObject success_Objcet = success_Array.optJSONObject(i);
                                if (success_Objcet != null){
                                    int id = success_Objcet.optInt("id");
                                    String position = success_Objcet.optString("position");
                                    String store_name = success_Objcet.optString("store_name");
                                    PositionListInstance positionListInstance = new PositionListInstance(id , store_name , position);
                                    listInstances.add(positionListInstance);
                                    System.out.println("position:"+position);
                                    String [] pos = position.split(",");
                                    if (pos.length == 2){
                                        LatLng latLng = new LatLng(Double.parseDouble(pos[1].toString()),Double.parseDouble(pos[0].toString()));
                                        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLng).title(store_name)
                                                .draggable(true));
                                        distance = AMapUtils.calculateLineDistance(locationLog,latLng);
                                        System.out.println("距离："+distance+"m");
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("维度接口访问不通或者网络不通");
            }
        });
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(10000);

            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null){
            mapView.onResume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null){
            mapView.onPause();
        }

        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null){
            mapView.onDestroy();
        }

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }
}
