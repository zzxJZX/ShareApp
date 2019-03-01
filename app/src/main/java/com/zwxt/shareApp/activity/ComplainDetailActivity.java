package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/9/3.
 */

public class ComplainDetailActivity extends Activity{

    private Button cp_back_bt;

    private TextView cp_detail_now_time , cp_detail_oid , cp_detail_text , cp_detail_time , cp_detail_content , cp_detail_describe_text;

    private ImageView cp_detail_image;

    private AsyncHttpClient asyncHttpClient;

    private String phone;

    private String token;

    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_detail_layout);
        id = getIntent().getIntExtra("id",-1);
        phone = Information.getStringConfig(ComplainDetailActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(ComplainDetailActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        setTitleHeight();
        init();
        complainShow(id);
    }

    private void init(){
        cp_back_bt = (Button) findViewById(R.id.cp_back_bt);
        cp_detail_now_time = (TextView) findViewById(R.id.cp_detail_now_time);
        cp_detail_oid = (TextView) findViewById(R.id.cp_detail_oid);
        cp_detail_text = (TextView) findViewById(R.id.cp_detail_text);
        cp_detail_time = (TextView) findViewById(R.id.cp_detail_time);
        cp_detail_content = (TextView) findViewById(R.id.cp_detail_content);
        cp_detail_describe_text = (TextView) findViewById(R.id.cp_detail_describe_text);
        cp_detail_image = (ImageView) findViewById(R.id.cp_detail_image);
        cp_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void goods_logistics(String oid){
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
                        int bid = success.optInt("bid", Constants.convention);
                        int cid = success.optInt("cid", Constants.convention);
                        String aid_name = success.optString("aid_name");
                        String cid_name = success.optString("cid_name");
                        String bid_name = success.optString("bid_name");
                        String image = success.optString("image");
                        Glide.with(ComplainDetailActivity.this).load(Constants.MAIN_LINK+image).into(cp_detail_image);
                        if (aid != Constants.convention){
                            cp_detail_describe_text.setText("物品名称:"+aid_name);
                        } else if (bid != Constants.convention){
                            cp_detail_describe_text.setText("物品名称:"+bid_name);
                        } else if (cid != Constants.convention){
                            cp_detail_describe_text.setText("物品名称:"+cid_name);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goods_logistics接口不通或网络异常:"+throwable.getMessage());
            }

        });
    }


    private void complainShow(int id){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/complain/"+id+"/show" , new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("complainShow:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_Object = object.optJSONObject("success");
                        int id = success_Object.optInt("id", Constants.convention);
                        String phone = success_Object.optString("phone");
                        String title = success_Object.optString("title");
                        String content = success_Object.optString("content");
                        int status = success_Object.optInt("status");
                        String oid = success_Object.optString("oid");
                        String complain_time = success_Object.optString("complain_time");
                        String created_at = success_Object.optString("created_at");
                        String updated_at = success_Object.optString("updated_at");
                        cp_detail_now_time.setText(complain_time);
                        cp_detail_oid.setText("订单编号:"+oid);
                        cp_detail_text.setText("投诉原因:"+title);
                        cp_detail_time.setText("投诉时间:"+created_at);
                        if (content != null && !content.equals("") && !content.equals("null")){
                            cp_detail_content.setText("管理员回复:"+content);
                        }
                        goods_logistics(oid);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("complainShow接口不通或网络异常:"+throwable.getMessage());
            }

        });
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
        View view = findViewById(R.id.cp_detail_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

}
