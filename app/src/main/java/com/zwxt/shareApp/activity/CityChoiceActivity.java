package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.ShareApplication;
import com.zwxt.shareApp.adapter.CityChoiceAdapter;
import com.zwxt.shareApp.instance.CityInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 */

public class CityChoiceActivity extends Activity {

    private ListView city_list;
    private Button return_bt;
    private AsyncHttpClient asyncHttpClient;
    private String token;
    private List<CityInstance> list;
    private CityChoiceAdapter cityChoiceAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choice_layout);
        token = Information.getStringConfig(CityChoiceActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        init();
        allCity();
    }

    private void init(){
        city_list = (ListView) findViewById(R.id.city_list);
        return_bt = (Button) findViewById(R.id.return_bt);
        list = new ArrayList<>();
        cityChoiceAdapter = new CityChoiceAdapter(list,CityChoiceActivity.this);
        city_list.setAdapter(cityChoiceAdapter);
        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityInstance cityInstance = list.get(position);
                String area_name = cityInstance.getArea_name();
                ShareApplication.cityId = cityInstance.getId();
                ShareApplication.fid = cityInstance.getFid();
                Intent intent = new Intent();
                intent.putExtra("area_name",area_name);
                setResult(2,intent);
                finish();
            }
        });
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void allCity(){
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
                                int level = success_Objcet.optInt("level");
                                String position = success_Objcet.optString("position");
//                                System.out.println("allCity:"+area_name);
                                CityInstance cityInstance = new CityInstance(id,area_name,fid,level,position);
                                list.add(cityInstance);
                            }
                            cityChoiceAdapter.notifyDataSetChanged();
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
}
