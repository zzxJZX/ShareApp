package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.ShareApplication;
import com.zwxt.shareApp.adapter.MailAdapter;
import com.zwxt.shareApp.instance.MailInstance;
import com.zwxt.shareApp.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class MailActivity extends Activity{

    private Button mail_back_bt;
    private ListView mail_list;
    private Button add_new_bt;
    private AsyncHttpClient asyncHttpClient;
    private String token;

    private String phone;

    private List<MailInstance> list;

    private MailAdapter adapter;

    private MyProgressDialog myProgressDialog;

    private boolean add = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_stock_layout);
        add = getIntent().getBooleanExtra("add",false);
        asyncHttpClient = new AsyncHttpClient();
        token = Information.getStringConfig(MailActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        phone = Information.getStringConfig(MailActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

        setTitleHeight();
        init();
        getAddressList();
    }

    private void init(){
        mail_back_bt = (Button) findViewById(R.id.mail_back_bt);
        mail_list = (ListView) findViewById(R.id.mail_list);
        add_new_bt = (Button) findViewById(R.id.add_new_bt);
        mail_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_new_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MailActivity.this,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
        list = new ArrayList<>();
        adapter = new MailAdapter(list,MailActivity.this);
        mail_list.setAdapter(adapter);
        mail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MailInstance mailInstance = list.get(position);
                String phone = mailInstance.getPhone();
                String name = mailInstance.getName();
                String address = mailInstance.getAddress();
                int province = mailInstance.getProvince();
                int city = mailInstance.getCity();
                if (add){
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    intent.putExtra("province",province);
                    intent.putExtra("city",city);
                    intent.putExtra("name",name);
                    intent.putExtra("address",address);
                    setResult(2,intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    intent.putExtra("province",province);
                    intent.putExtra("city",city);
                    intent.putExtra("name",name);
                    intent.putExtra("address",address);
                    intent.putExtra("active",true);
                    intent.setClass(MailActivity.this,MailFragmentActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode:"+requestCode+"|resultCode"+resultCode);
        if (requestCode == 1 && resultCode == 2){
            list.clear();
            getAddressList();
        }
    }

    private void getAddressList(){

        myProgressDialog = MyProgressDialog.createDialog(MailActivity.this);
        myProgressDialog.setMessage("数据获取中....");
        myProgressDialog.show();

        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        System.out.println("getAddressList:"+ Constants.MAIN_LINK+"/api/account/address/list?phone="+phone+"&city="+ ShareApplication.cityId);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/account/address/list?phone="+phone+"&city="+ ShareApplication.cityId,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getAddressList:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0 ){
                            for(int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                if (success_Object != null){
                                    int id = success_Object.optInt("id");
                                    String phone = success_Object.optString("phone");
                                    int province = success_Object.optInt("province");
                                    int city = success_Object.optInt("city");
                                    String address = success_Object.optString("address");
                                    String name = success_Object.optString("name");
                                    String order_phone = success_Object.optString("order_phone");
                                    MailInstance instance = new MailInstance(id,phone,province,city,address,name,order_phone);
                                    list.add(instance);
                                }

                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MailActivity.this,"数据解析异常",Toast.LENGTH_SHORT).show();
                }

                myProgressDialog.dismiss();
            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getAddressList接口异常或者网络不通");
                myProgressDialog.dismiss();
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
        View view = findViewById(R.id.mail_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }
}
