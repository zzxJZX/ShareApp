package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.CouponListAdapter;
import com.zwxt.shareApp.instance.CouponsListInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/4.
 */

public class CouponActivity extends Activity {

    private Button coupon_back_bt;

    private ListView coupon_list;

    private List<CouponsListInstance> listInstances;

    private CouponListAdapter adapter;

    private float price;

    private AsyncHttpClient asyncHttpClient;

    private String phone;
    private String token;

    private GetCouponBroadCastReceiver broadCastReceiver = new GetCouponBroadCastReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_layout);
        price = getIntent().getFloatExtra("price", Constants.convention);
        listInstances = (List<CouponsListInstance>)(getIntent().getExtras().getSerializable("list"));
        phone = Information.getStringConfig(CouponActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(CouponActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        setTitleHeight();
        couponsList();
        init();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.USER_COUPON);
        registerReceiver(broadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }

    class GetCouponBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.USER_COUPON)){
//                couponsShow();
                int position = intent.getIntExtra("position", Constants.convention);
                if (position != Constants.convention){
                    CouponsListInstance instance = listInstances.get(position);
                    String money_free = instance.getMoney_free();
                    int id = instance.getId();
                    float money_total = instance.getMoney_total();
                    Intent intent1 = new Intent();
                    intent1.putExtra("money_free",money_free);
                    intent1.putExtra("money_total",money_total);
                    intent1.putExtra("couponID",id);
                    CouponActivity.this.setResult(1,intent1);

                    finish();
                }

            }
        }
    }

    private void init(){

        coupon_back_bt = (Button) findViewById(R.id.coupon_back_bt);
        coupon_list = (ListView) findViewById(R.id.coupon_list);
        coupon_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new CouponListAdapter(listInstances,CouponActivity.this,price);
        coupon_list.setAdapter(adapter);

    }

    private void couponsList(){

        if (listInstances == null) {
            listInstances = new ArrayList<>();

            if (asyncHttpClient == null) {
                asyncHttpClient = new AsyncHttpClient();
            }
            asyncHttpClient.setTimeout(1000);
            asyncHttpClient.addHeader("Authorization", "Bearer " + token);
            asyncHttpClient.get(Constants.MAIN_LINK + "/api/coupons/list?phone=" + phone + "&status=0", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    System.out.println("couponsList:" + s);
                    try {
                        JSONObject object = new JSONObject(s);
                        String code = object.optString("code");
                        if (code.equals("0")) {
                            JSONArray success_Array = object.optJSONArray("success");
                            if (success_Array != null && success_Array.length() > 0) {
//                            coupon_number.setText(success_Array.length()+"");
                                for (int i = 0; i < success_Array.length(); i++) {
                                    JSONObject success_Object = success_Array.optJSONObject(i);
                                    int active_id = success_Object.optInt("active_id");
                                    String created_at = success_Object.optString("created_at");
                                    int id = success_Object.optInt("id");
                                    String money_free = success_Object.optString("money_free", "0");
                                    float money_total = Float.parseFloat(success_Object.optString("money_total", "0"));
                                    String phone = success_Object.optString("phone");
                                    int status = success_Object.optInt("status");
                                    String time_end = success_Object.optString("time_end");
                                    String time_start = success_Object.optString("time_start");
                                    String type = success_Object.optString("type");
                                    String updated_at = success_Object.optString("updated_at");
                                    CouponsListInstance instance = new CouponsListInstance(active_id, created_at, id, money_free, money_total, phone, status, time_end, time_start, type, updated_at);
                                    listInstances.add(instance);
                                }
                                adapter = new CouponListAdapter(listInstances,CouponActivity.this,price);
                                coupon_list.setAdapter(adapter);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    super.onFailure(throwable, s);
                    System.out.println("couponsList接口不通或网络异常:" + throwable.getMessage());
                }

            });
        }
    }

    private void setTitleHeight (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        View view = findViewById(R.id.coupon_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

}
