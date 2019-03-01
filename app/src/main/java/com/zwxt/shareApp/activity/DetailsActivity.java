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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.DetailsAdapter;
import com.zwxt.shareApp.instance.CouponsShowInstance;
import com.zwxt.shareApp.instance.DetailsInstance;
import com.zwxt.shareApp.view.BLAlert;
import com.zwxt.shareApp.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class DetailsActivity extends Activity {

    private Button return_bt , discount_bt;

    private int id;

    private AsyncHttpClient asyncHttpClient;

    private String token ;

    private MyProgressDialog myProgressDialog;

    private TextView address_text,admin_phone_text, price_text,all_total_text,all_free_text,manager_name_text;

    private ListView details_list;

    private ImageView details_image;

    private List<DetailsInstance> list;

    private DetailsAdapter detailsAdapter;

    private int all_total = 0;

    private int all_free = 0 ;

    private float all_price = 0;

    private boolean chooseHouse = false;

    private Button details_Apply_bt;

    private String houseName = "";

    private List<CouponsShowInstance> couponsShowInstanceList;

    private CouponBroadCastReceiver broadCastReceiver = new CouponBroadCastReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        asyncHttpClient = new AsyncHttpClient();
        token = Information.getStringConfig(DetailsActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        id = getIntent().getIntExtra("id",6899);
        chooseHouse = getIntent().getBooleanExtra("chooseHouse",false);
        houseName = getIntent().getStringExtra("houseName");
        System.out.println("id:"+id);
        setTitleHeight();
        init();
        houseDetails();
        couponsShow();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.GET_COUPON);
        registerReceiver(broadCastReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }

    private void init(){
        discount_bt = (Button) findViewById(R.id.discount_bt);
        return_bt = (Button) findViewById(R.id.return_bt);
        address_text = (TextView) findViewById(R.id.address_text);
        admin_phone_text = (TextView) findViewById(R.id.admin_phone_text);
        price_text = (TextView) findViewById(R.id.price_text);
        all_total_text = (TextView) findViewById(R.id.all_total_text);
        all_free_text = (TextView) findViewById(R.id.all_free_text);
        manager_name_text = (TextView) findViewById(R.id.manager_name_text);
        details_list = (ListView) findViewById(R.id.details_list);
        details_image = (ImageView) findViewById(R.id.details_image);
        details_Apply_bt = (Button) findViewById(R.id.details_Apply_bt);

        discount_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    BLAlert.ShowListAlert(DetailsActivity.this, new BLAlert.OnAlertListSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            System.out.println("whichButton:"+whichButton);
                        }
                    }, couponsShowInstanceList);

            }
        });

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        details_Apply_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseHouse){
                    Intent intent= new Intent();
                    intent.putExtra("active",false);
                    intent.putExtra("id",id);
                    intent.putExtra("houseName",houseName);
                    setResult(1,intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("active",true);
                    intent.putExtra("id",id);
                    intent.putExtra("houseName",houseName);
                    intent.setClass(DetailsActivity.this,MailFragmentActivity.class);
                    startActivity(intent);
                }
            }
        });
        list = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(list,DetailsActivity.this);
        details_list.setAdapter(detailsAdapter);
    }

    private void couponsShow(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        couponsShowInstanceList = new ArrayList<>();
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/coupons/show" , new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("couponsShow:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int active_id = success_Object.optInt("active_id");
                                String free = success_Object.optString("free");
                                String total = success_Object.optString("total");
                                CouponsShowInstance instance = new CouponsShowInstance(active_id,free,total);
                                couponsShowInstanceList.add(instance);
                            }
                        }
                    }

                    if (couponsShowInstanceList.size() <= 0){
                        discount_bt.setVisibility(View.INVISIBLE);
                    } else {
                        discount_bt.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    discount_bt.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("couponsShow接口不通或网络异常:"+throwable.getMessage());
                discount_bt.setVisibility(View.INVISIBLE);
            }
        });
    }

    class CouponBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.GET_COUPON)){
                couponsShow();
            }
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
        View view = findViewById(R.id.details_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

    private void houseDetails(){

        myProgressDialog = MyProgressDialog.createDialog(DetailsActivity.this);
        myProgressDialog.setMessage("数据加载中....");
        myProgressDialog.show();

        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/store/show/"+id,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("houseDetails:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_Object = object.optJSONObject("success");
                        if (success_Object != null){
                            String address = success_Object.optString("address");
                            String admin_phone = success_Object.optString("admin_phone");
                            String manager_name = success_Object.optString("manager_name");
                            String image = success_Object.optString("image");
                            address_text.setText(address);
                            admin_phone_text.setText("联系方式："+admin_phone);
                            manager_name_text.setText("经纪人:"+manager_name);
                            if (!image.equals("")){
                                Glide.with(DetailsActivity.this).load(image).into(details_image);
                            }
                            JSONArray stock_Arrary = success_Object.optJSONArray("stock");
                            if (stock_Arrary != null && stock_Arrary.length() > 0){
                                for (int i = 0 ; i < stock_Arrary.length() ; i++){
                                    JSONObject stock_Object = stock_Arrary.optJSONObject(i);
                                    if (stock_Object != null){
                                        int total = stock_Object.optInt("total", Constants.convention);
                                        all_total = total + all_total;
                                        float price = Float.parseFloat(stock_Object.optString("price","0"));
                                        all_price = price + all_price;
                                        String store_type_name = stock_Object.optString("store_type_name");
                                        int free = stock_Object.optInt("free", Constants.convention);
                                        all_free = free + all_free;
                                        int store_id = stock_Object.optInt("store_id", Constants.convention);
                                        int store_type_id = stock_Object.optInt("store_type_id", Constants.convention);
                                        String create_at = stock_Object.optString("create_at");
                                        float discount_store_month = Float.parseFloat(stock_Object.optString("discount_store_month","1"));
                                        int id = stock_Object.optInt("id", Constants.convention);
                                        String updated_at = stock_Object.optString("updated_at");
                                        DetailsInstance detailsInstance = new DetailsInstance(free,store_type_name,total,price,store_id,store_type_id,create_at,discount_store_month,id,updated_at);
                                        list.add(detailsInstance);
                                    }
                                }

                                price_text.setText("总价格:"+all_price+"/立方/月");
                                all_total_text.setText("总面积:"+all_total+"立方");
                                all_free_text.setText("可用面积:"+all_free+"立方");

                                detailsAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myProgressDialog.dismiss();

            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                myProgressDialog.dismiss();
                System.out.println("houseDetails接口访问不通或者网络不通");
            }

        });
    }
}
