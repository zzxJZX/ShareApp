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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.PackAdapter;
import com.zwxt.shareApp.instance.ProvinceInstance;
import com.zwxt.shareApp.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/11.
 */

public class AddAddressActivity extends Activity {

    EditText nameEdit,phoneEdit,addressEdit;

    private Button add_address_back_bt;

    private Button save_address_bt;

    private Spinner province_spinner,city_spinner;

    private AsyncHttpClient asyncHttpClient;

    private String token ;

    private List<ProvinceInstance> province_list ;

    private List<ProvinceInstance> city_list;

    private PackAdapter province_Adapter , city_Adapter;

    private int provinceID = 6899;

    private int cityID = 6899;

    private String phone;

    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_layout);
        asyncHttpClient = new AsyncHttpClient();
        token = Information.getStringConfig(AddAddressActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        phone = Information.getStringConfig(AddAddressActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        setTitleHeight();
        init();
        getProvince();
    }

    private void init(){
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        addressEdit = (EditText) findViewById(R.id.addressEdit);
        add_address_back_bt = (Button) findViewById(R.id.add_address_back_bt);
        save_address_bt = (Button) findViewById(R.id.save_address_bt);
        province_spinner = (Spinner) findViewById(R.id.province_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        add_address_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        province_list = new ArrayList<>();
        city_list = new ArrayList<>();
        province_Adapter = new PackAdapter(province_list,AddAddressActivity.this);
        city_Adapter = new PackAdapter(city_list,AddAddressActivity.this);
        province_spinner.setAdapter(province_Adapter);
        city_spinner.setAdapter(city_Adapter);
        province_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvinceInstance provinceInstance = province_list.get(position);
                provinceID = provinceInstance.getId();
                getCity(provinceID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvinceInstance provinceInstance = city_list.get(position);
                cityID = provinceInstance.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        save_address_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdit.getText().toString().equals("")){
                    Toast.makeText(AddAddressActivity.this,"姓名不能为空！",Toast.LENGTH_SHORT).show();
                } else if (phoneEdit.getText().toString().equals("")){
                    Toast.makeText(AddAddressActivity.this,"电话号码不能为空！",Toast.LENGTH_SHORT).show();
                } else if (addressEdit.getText().toString().equals("")){
                    Toast.makeText(AddAddressActivity.this,"详细地址不能为空！",Toast.LENGTH_SHORT).show();
                } else if (provinceID == 6899) {
                    Toast.makeText(AddAddressActivity.this,"请选择正确的省份！",Toast.LENGTH_SHORT).show();
                } else if (cityID == 6899){
                    Toast.makeText(AddAddressActivity.this,"请选择正确的地区！",Toast.LENGTH_SHORT).show();
                } else {
                    myProgressDialog = MyProgressDialog.createDialog(AddAddressActivity.this);
                    myProgressDialog.setMessage("数据传输中....");
                    myProgressDialog.show();

                    saveAddress(provinceID,cityID);
                }
            }
        });
    }

    private void saveAddress(int provinceID , int cityID){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        RequestParams requestParams = new RequestParams();
        requestParams.put("province",provinceID+"");
        requestParams.put("city",cityID+"");
        requestParams.put("name",nameEdit.getText().toString());
        requestParams.put("address",addressEdit.getText().toString());
        requestParams.put("order_phone",phoneEdit.getText().toString());
        requestParams.put("phone",phone);

        asyncHttpClient.post(Constants.MAIN_LINK+"/api/account/address/store",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("saveAddress:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    int code = object.optInt("code",6899);
                    if (code == 0 ){
                        String message = object.optString("message");
                        Toast.makeText(AddAddressActivity.this,message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("addAddress",true);
                        setResult(2,intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddAddressActivity.this,"数据解析异常",Toast.LENGTH_SHORT).show();
                }

                myProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("saveAddress接口访问不通或者网络不通");

                myProgressDialog.dismiss();
            }

        });
    }

    private void getCity(int id){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/area?fid="+id,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getProvince:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length()>0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Objcet = success_Array.optJSONObject(i);
                                int id = success_Objcet.optInt("id" , 6899);
                                String area_name = success_Objcet.optString("area_name");
                                int fid = success_Objcet.optInt("fid");
                                int level = success_Objcet.optInt("level");
                                String position = success_Objcet.optString("position");
                                ProvinceInstance provinceInstance = new ProvinceInstance(id,area_name,fid,level,position);
                                city_list.add(provinceInstance);
                            }
                            city_Adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getProvince接口访问不通或者网络不通");
            }

        });

    }

    private void getProvince(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/area?fid=",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getProvince:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length()>0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Objcet = success_Array.optJSONObject(i);
                                int id = success_Objcet.optInt("id" , 6899);
                                String area_name = success_Objcet.optString("area_name");
                                int fid = success_Objcet.optInt("fid");
                                int level = success_Objcet.optInt("level");
                                String position = success_Objcet.optString("position");
                                ProvinceInstance provinceInstance = new ProvinceInstance(id,area_name,fid,level,position);
                                province_list.add(provinceInstance);
                            }
                            province_Adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getProvince接口访问不通或者网络不通");
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
        View view = findViewById(R.id.address_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }
}
