package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/9.
 */

public class LoginActivity extends Activity {

    private EditText phone_ed , pass_ed;

    private Button getVCode_bt , register_now_bt , login_bt;

    private AsyncHttpClient asyncHttpClient;

    private MyProgressDialog myProgressDialog;

    private String phone,pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
//        test();
        phone = Information.getStringConfig(LoginActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        pass = Information.getStringConfig(LoginActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_PASS);

        asyncHttpClient = new AsyncHttpClient();
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
        System.out.println("statusBarHeight:"+statusBarHeight1);
        View view = findViewById(R.id.title_view);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);

        init();
    }

    private void init(){
        phone_ed = (EditText) findViewById(R.id.phone_ed);
        pass_ed = (EditText) findViewById(R.id.pass_ed);
        getVCode_bt = (Button) findViewById(R.id.getVCode_bt);
        register_now_bt = (Button) findViewById(R.id.register_now_bt);
        login_bt = (Button) findViewById(R.id.login_bt);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_ed.getText().toString().length() >= 11){
                    myProgressDialog = MyProgressDialog.createDialog(LoginActivity.this);
                    myProgressDialog.setMessage("登录中....");
                    myProgressDialog.show();
                    login();
                } else {
                    Toast.makeText(LoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register_now_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();
            }
        });

        if (!phone.equals("")){
            phone_ed.setText(phone);
        }

        if (!pass.equals("")){
            pass_ed.setText(pass);
        }


    }

//    private void test(){
////        if (asyncHttpClient == null){
//            asyncHttpClient = new AsyncHttpClient();
//            RequestParams requestParams = new RequestParams();
//            requestParams.put("custKey","uber88888e33freb8a0366793");
//            requestParams.put("lockerIds","8085024");
//            asyncHttpClient.post("http://www.chinauber.cn/parklock/manager/parklock/locker-open-comm!unlock.do",requestParams,new AsyncHttpResponseHandler(){
//
//                @Override
//                public void onSuccess(String s) {
//                    super.onSuccess(s);
//                    System.out.println("成功："+s);
//                }
//
//                @Override
//                public void onFailure(Throwable throwable, String s) {
//                    super.onFailure(throwable, s);
//                    System.out.println("失败："+s);
//                }
//            });
////        }
//    }

    private void login(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone",phone_ed.getText().toString());
        requestParams.put("password",pass_ed.getText().toString());
        System.out.println("login:"+requestParams.toString());
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.post(Constants.MAIN_LINK+"/api/login",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("login:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    int code = object.optInt("code",6899);
                    String message = object.optString("message");

                    if (code == 0){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                        JSONObject success_object = object.optJSONObject("success");

                        System.out.println("success_object："+success_object.toString());

                        int province = success_object.optInt("province",6899);
                        int city = success_object.optInt("city",6899);
                        String image = success_object.optString("image");
                        int points = success_object.optInt("points");
                        float money = success_object.optInt("money");
                        String position = success_object.optString("position");
                        String address = success_object.optString("address");
                        String name = success_object.optString("name");
                        String wechat_account = success_object.optString("wechat_account");
                        String alipay_account = success_object.optString("alipay_account");
                        String token = success_object.optString("token");

                        System.out.println("wecha:"+wechat_account.equals("null"));

                        HashMap<String, Object> loginMap = new HashMap<>();
                        loginMap.put(Information.SHARE_LOGIN_ACCOUNT,phone_ed.getText().toString());
                        loginMap.put(Information.SHARE_LOGIN_PASS,pass_ed.getText().toString());
                        loginMap.put(Information.ACCOUNT_PROVINCE,province);
                        loginMap.put(Information.ACCOUNT_City,city);
                        loginMap.put(Information.ACCOUNT_HEAD_IMAGE_URL,image);
                        loginMap.put(Information.ACCOUNT_POINTS,points);
                        loginMap.put(Information.ACCOUNT_MONEY,money);
                        loginMap.put(Information.ACCOUNT_POSITION,position);
                        loginMap.put(Information.ACCOUNT_ADDRESS,address);
                        loginMap.put(Information.ACCOUNT_NAME,name);
                        loginMap.put(Information.WECHAT_ACCOUNT,wechat_account);
                        loginMap.put(Information.ALI_ACCOUNT,alipay_account);
                        loginMap.put(Information.ACCOUNT_TOKEN,token);

                        Information.saveInformation(LoginActivity.this, Information.SHARE_LOGIN,loginMap);

                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    } else {
                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    myProgressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    myProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("Login接口访问不通或者网络不通");
                myProgressDialog.dismiss();
            }

        });
    }

}
