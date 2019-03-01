package com.zwxt.shareApp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.Rom;
import com.zwxt.shareApp.activity.ComplaintActivity;
import com.zwxt.shareApp.activity.CouponActivity;
import com.zwxt.shareApp.activity.LoginActivity;
import com.zwxt.shareApp.activity.MailActivity;
import com.zwxt.shareApp.activity.ThingsActivity;
import com.zwxt.shareApp.instance.CouponsListInstance;
import com.zwxt.shareApp.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/8/17.
 */

public class MyFragment extends BaseFragment {

    @Bind(R.id.login_name)
    TextView login_name;
    @Bind(R.id.circleImageView)
    CircleImageView circleImageView;
    @Bind(R.id.my_storage_text)
    TextView my_storage_text;
    @Bind(R.id.my_address_text)
    TextView my_address_text;
    @Bind(R.id.my_help)
    TextView my_help;
    @Bind(R.id.customer_service)
    TextView customer_service;
    @Bind(R.id.suggest_text)
    TextView suggest_text;
    @Bind(R.id.complaint_text)
    TextView complaint_text;
    @Bind(R.id.coupon_number)
    TextView coupon_number;
    @Bind(R.id.coupon_lin)
    LinearLayout coupon_lin;
    @Bind(R.id.loginOut_text)
    TextView loginOut_text;

    private String name;
    private String image_url;
    private String phone;
    private String token;

    private AsyncHttpClient asyncHttpClient;

    private List<CouponsListInstance> listInstances;


    @Override
    protected int getLayoutId() {
        return R.layout.my_layout;
    }

    @Override
    protected void initView() {
        name = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_NAME);
        image_url = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_HEAD_IMAGE_URL);
        phone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        listInstances = new ArrayList<>();
    }

    @Override
    protected void initData() {
        login_name.setText(phone);

        Glide.with(getActivity()).load(image_url).error(R.drawable.t).into(circleImageView);
    }

    @Override
    protected void initListener() {
        couponsList();
        mStatusBarView.setBackgroundColor(getResources().getColor(R.color.myfragmentcolor));
        if (Rom.isEmui()) {
            System.out.println("isEMUI");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        } else if (Rom.isFlyme()) {
            System.out.println("isFlyme");
            setMeizuStatusBarDarkIcon(getActivity(), false);
        } else if (Rom.isMiui()) {
            System.out.println("isMiui");
            setMiuiStatusBarDarkMode(getActivity(), false);
        } else {
            System.out.println("isis");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        }

        loginOut_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> loginMap = new HashMap<>();
                loginMap.put(Information.SHARE_LOGIN_PASS,"");
                Information.saveInformation(getActivity(), Information.SHARE_LOGIN,loginMap);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        my_storage_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ThingsActivity.class);
                startActivity(intent);
            }
        });

        my_address_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MailActivity.class);
                intent.putExtra("add",false);
                startActivity(intent);
            }
        });

        my_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone("400-900-6585");
            }
        });

        suggest_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        complaint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ComplaintActivity.class);
                intent.putExtra("state",true);
                intent.putExtra("oid","");
                startActivity(intent);
            }
        });

        coupon_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CouponActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) listInstances);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    private void couponsList(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/coupons/list?phone="+phone+"&status=0" , new AsyncHttpResponseHandler(){

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
                            coupon_number.setText(success_Array.length()+"");
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
                System.out.println("couponsList接口不通或网络异常:"+throwable.getMessage());
            }

        });
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
}
