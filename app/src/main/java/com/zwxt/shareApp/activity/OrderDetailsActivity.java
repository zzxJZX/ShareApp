package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.view.BLAlert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/27.
 */

public class OrderDetailsActivity extends Activity {

    private Button return_bt;

    private ImageView order_details_image , order_small_image;

    private TextView order_oid , order_name , order_status , order_house_name , order_house_type , order_things_size , oder_days , oder_maintain , order_insurance , oder_price , oder_storage_time;

    private String oid;

    private boolean details;

    private AsyncHttpClient asyncHttpClient;

    private String token;

    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_layout);
        token = Information.getStringConfig(OrderDetailsActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        details = getIntent().getBooleanExtra("details",false);
        oid = getIntent().getStringExtra("oid");
        init();
        if (details){
            goods_stored();
        } else {
            goods_logistics();
        }
    }

    private void init(){
        return_bt = (Button) findViewById(R.id.return_bt);
        order_details_image = (ImageView) findViewById(R.id.order_details_image);
        order_small_image = (ImageView) findViewById(R.id.order_small_image);
        order_oid = (TextView) findViewById(R.id.order_oid);
        order_name = (TextView) findViewById(R.id.order_name);
        order_status = (TextView) findViewById(R.id.order_status);
        order_house_name = (TextView) findViewById(R.id.order_house_name);
        order_house_type = (TextView) findViewById(R.id.order_house_type);
        order_things_size = (TextView) findViewById(R.id.order_things_size);
        oder_days = (TextView) findViewById(R.id.oder_days);
        oder_maintain = (TextView) findViewById(R.id.oder_maintain);
        order_insurance = (TextView) findViewById(R.id.order_insurance);
        oder_price = (TextView) findViewById(R.id.oder_price);
        oder_storage_time = (TextView) findViewById(R.id.oder_storage_time);
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void goods_stored(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods_stored/"+oid+"/show",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goods_stored:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_Object = object.optJSONObject("success");
                        int active_fee = success_Object.optInt("active_fee", Constants.convention);
                        int active_id = success_Object.optInt("active_id", Constants.convention);
                        int aid = success_Object.optInt("aid", Constants.convention);
                        int bid = success_Object.optInt("bid", Constants.convention);
                        int cid = success_Object.optInt("cid", Constants.convention);
                        String cid_name = success_Object.optString("cid_name");
                        int city = success_Object.optInt("city", Constants.convention);
                        int contain = success_Object.optInt("contain", Constants.convention);
                        int days = success_Object.optInt("days", Constants.convention);
                        int goods_logistics_id = success_Object.optInt("goods_logistics_id", Constants.convention);
                        String goods_protect_end_time = success_Object.optString("goods_protect_end_time");
                        int goods_protect_rules_id = success_Object.optInt("goods_protect_rules_id", Constants.convention);
                        String goods_protect_rules_name = success_Object.optString("goods_protect_rules_name");
                        int goods_safe_id = success_Object.optInt("goods_safe_id", Constants.convention);
                        int goods_tid = success_Object.optInt("goods_tid", Constants.convention);
                        int id = success_Object.optInt("id", Constants.convention);
                        String image = success_Object.optString("image");
                        String mark = success_Object.optString("mark");
                        String member_address = success_Object.optString("member_address");
                        String member_name = success_Object.optString("member_name");
                        String member_phone = success_Object.optString("member_phone");
                        int number = success_Object.optInt("number", Constants.convention);
                        String oid = success_Object.optString("oid");
                        int price = success_Object.optInt("price");
                        int province = success_Object.optInt("province");
                        int status = success_Object.optInt("status");
                        int stock_id = success_Object.optInt("stock_id");
                        int store_id = success_Object.optInt("store_id");
                        String store_name = success_Object.optString("store_name");
                        String store_type_name = success_Object.optString("store_type_name");
                        String created_at = success_Object.optString("created_at");
                        String goods_safe_name = success_Object.optString("goods_safe_name");
                        order_oid.setText("订单号:"+oid);
                        Glide.with(OrderDetailsActivity.this).load(Constants.MAIN_LINK+image).error(R.drawable.details_bg).into(order_details_image);
                        Glide.with(OrderDetailsActivity.this).load(Constants.MAIN_LINK+image).into(order_small_image);
                        order_name.setText("物品名称:"+cid_name);
                        statusForGoods_stored(status);
                        order_house_name.setText("仓库名称:"+store_name);
                        order_house_type.setText("仓库类别:"+store_type_name);
                        order_things_size.setText("占据体积:"+contain);
                        oder_days.setText("存放天数:"+days+"天");
                        oder_maintain.setText("维护内容:"+goods_protect_rules_name);
                        order_insurance.setText("保险:"+goods_safe_name);
                        oder_price.setText("金额:"+price+"元");
                        oder_storage_time.setText("入库时间:"+created_at);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goods_stored接口访问不通或网络异常："+throwable.getMessage());
            }

        });

    }

    private void statusForGoods_stored(int status){
        if (status == 0){
            order_status.setText("等待入库");
        } else if (status == 1){
            order_status.setText("已入库");
        } else if (status == 2){
            order_status.setText("已出库");
        } else if (status == 3){
            order_status.setText("申请提前出库");
            order_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone = Information.getStringConfig(OrderDetailsActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

                    BLAlert.showAlert(OrderDetailsActivity.this, "是否申请提前出库?", new BLAlert.OnAlertSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            switch (whichButton){
                                case 0:
                                    //yse

//                                                    userConfirm(phone,instance.getOid(),3,context);

                                    break;
                            }
                        }
                    });
                }
            });
        } else if (status == 4){
            order_status.setText("已同意出库");
        } else if (status == 5){
            order_status.setText("不同意出库");
        }
    }

    private void statusForGoods_logistics(int status){
        if (status == 0){
            order_status.setText("已下单");
        } else if (status == 1){
            order_status.setText("已接单");
        } else if (status == 2){
            order_status.setText("派送中");
        } else if (status == 3){
            order_status.setText("订单完成");
        } else if (status == 4){
            order_status.setText("已入库");
        }
    }

    private void goods_logistics(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods_logistics/"+oid+"/show",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goods_logistics:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success = object.optJSONObject("success");
                        int aid = success.optInt("aid", Constants.convention);
                        String aid_name = success.optString("aid_name");
                        int bid = success.optInt("bid", Constants.convention);
                        int cancel_status = success.optInt("cancel_status", Constants.convention);
                        int cid = success.optInt("cid", Constants.convention);
                        int city = success.optInt("city", Constants.convention);
                        int contain = success.optInt("contain", Constants.convention);
                        String courier_name = success.optString("courier_name");
                        String courier_phone = success.optString("courier_phone");
                        String created_at = success.optString("created_at");
                        int days = success.optInt("days", Constants.convention);
                        int goods_protect_rules_id = success.optInt("goods_protect_rules_id", Constants.convention);
                        int goods_safe_id = success.optInt("goods_safe_id", Constants.convention);
                        int goods_tid = success.optInt("goods_tid", Constants.convention);
                        int id = success.optInt("id", Constants.convention);
                        String logistics_id = success.optString("logistics_id");
                        String logistics_time = success.optString("logistics_time");
                        String mark = success.optString("mark");
                        String member_address = success.optString("member_address");
                        String member_name = success.optString("member_name");
                        String member_phone = success.optString("member_phone");
                        String oid = success.optString("oid");
                        String price = success.optString("price");
                        int province = success.optInt("province", Constants.convention);
                        int status = success.optInt("status", Constants.convention);
                        int stock_id = success.optInt("stock_id", Constants.convention);
                        int store_id = success.optInt("store_id", Constants.convention);
                        String store_name = success.optString("store_name");
                        String store_type_name = success.optString("store_type_name");
                        String updated_at = success.optString("updated_at");
                        String image = success.optString("image");
                        String cid_name = success.optString("cid_name");
                        String bid_name = success.optString("bid_name");
                        String goods_protect_rules_name = success.optString("goods_protect_rules_name");
                        String goods_safe_name = success.optString("goods_safe_name");

                        order_oid.setText("订单号:"+oid);
                        Glide.with(OrderDetailsActivity.this).load(Constants.MAIN_LINK+image).error(R.drawable.details_bg).into(order_details_image);
                        Glide.with(OrderDetailsActivity.this).load(Constants.MAIN_LINK+image).into(order_small_image);
                        if (aid != Constants.convention){
                            order_name.setText("物品名称:"+aid_name);
                        } else if (bid != Constants.convention){
                            order_name.setText("物品名称:"+bid_name);
                        } else if (cid != Constants.convention){
                            order_name.setText("物品名称:"+cid_name);
                        }
                        statusForGoods_logistics(status);
                        order_house_name.setText("仓库名称:"+store_name);
                        order_house_type.setText("仓库类别:"+store_type_name);
                        order_things_size.setText("占据体积:"+contain);
                        oder_days.setText("存放天数:"+days+"天");
                        oder_maintain.setText("维护内容:"+goods_protect_rules_name);
                        order_insurance.setText("保险:"+goods_safe_name);
                        oder_price.setText("金额:"+price+"元");
                        oder_storage_time.setText("入库时间:"+created_at);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goods_logistics接口访问不通或网络异常："+throwable.getMessage());
            }

        });
    }



}
