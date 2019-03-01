package com.zwxt.shareApp;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.instance.ComplainListInstance;
import com.zwxt.shareApp.instance.CouponsListInstance;
import com.zwxt.shareApp.instance.CouponsShowInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Text extends Activity{

    private AsyncHttpClient asyncHttpClient;

    private String token = Information.getStringConfig(Text.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);;

    private String phone = Information.getStringConfig(Text.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);;

    private List<ComplainListInstance> listInstances ;

    private void complainList(String phone){

        listInstances = new ArrayList<>();


        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/complain/list?phone="+phone+"&status=",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("complainList:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int id = success_Object.optInt("id", Constants.convention);
                                String title = success_Object.optString("title");
                                String phone = success_Object.optString("phone");
                                String oid = success_Object.optString("oid");
                                int status = success_Object.optInt("status", Constants.convention);
                                ComplainListInstance instance = new ComplainListInstance(id,title,phone,oid,status);
                                listInstances.add(instance);
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
                System.out.println("complainList接口不通或网络异常："+throwable.getMessage());
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

    private void launchComplain(String phone , String oid , final String title){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone",phone);
        requestParams.put("oid",oid);
        requestParams.put("title",title);
        asyncHttpClient.post(Constants.MAIN_LINK+"/api/complain/add",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("launchComplain:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    if (code.equals("0")){

                    }
                    Toast.makeText(Text.this,message,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("launchComplain接口不通或网络异常:"+throwable.getMessage());
            }
        });
    }

    private void couponsList(String phone){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/coupons/list?phone="+phone+"&status=" , new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("couponsList:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int active_id = success_Object.optInt("active_id");
                                String created_at = success_Object.optString("created_at");
                                int id = success_Object.optInt("id");
                                String money_free = success_Object.optString("money_free","0");
                                float money_total = Float.parseFloat(success_Object.optString("money_total","0"));
                                String phone = success_Object.optString("phone");
                                int status = success_Object.optInt("status");
                                String time_end = success_Object.optString("time_end");
                                String time_start = success_Object.optString("time_start");
                                String type = success_Object.optString("type");
                                String updated_at = success_Object.optString("updated_at");
                                CouponsListInstance instance = new CouponsListInstance(active_id,created_at,id,money_free,money_total,phone,status,time_end,time_start,type,updated_at);
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
                System.out.println("couponsList接口不通或网络异常:"+throwable.getMessage());
            }

        });
    }

    private void couponsShow(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
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
                System.out.println("couponsShow接口不通或网络异常:"+throwable.getMessage());
            }
        });
    }

    private void getCoupon(int active_id , String phone , final String total){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);

        RequestParams requestParams = new RequestParams();
        requestParams.put("active_id",active_id+"");
        requestParams.put("phone",phone);
        requestParams.put("total",total);

        asyncHttpClient.post(Constants.MAIN_LINK+"/api/coupons/get",requestParams ,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getCoupon："+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){

                    }
                    String message = object.optString("message");
                    Toast.makeText(Text.this,message,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getCoupon接口不通或网络异常："+throwable.getMessage());
            }
        });
    }

}
