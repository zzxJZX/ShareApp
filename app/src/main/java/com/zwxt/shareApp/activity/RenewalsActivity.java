package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.ProtectAdapter;
import com.zwxt.shareApp.adapter.StockSafeAdapter;
import com.zwxt.shareApp.instance.ProtectInstance;
import com.zwxt.shareApp.instance.StockSafeInstance;
import com.zwxt.shareApp.view.BLTimerAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2018/8/27.
 */

public class RenewalsActivity extends Activity {

    private TextView renewals_oid, renewal_original_days_text, renewals_expire_time_text, renewals_price, renewals_original_cost_text;

    private Spinner renewals_safe_spinner, renewals_maintain_spinner;

    private EditText newly_increased_days_edit, renewals_maintenance_time_start_edit, renewals_maintenance_time_end_edit;

    private Button back_bt, renewals_bt;

    private String oid;

    private int safe_id;

    private int days;

    private String updated_at;

    private int goods_protect_rules_id;

    private boolean goods_id = false;

    private String star_time;

    private String end_time;

    private float price;

    private AsyncHttpClient asyncHttpClient;

    private String token;

    private List<StockSafeInstance> stockSafeInstances;

    private StockSafeAdapter stockSafeAdapter;

    private ProtectAdapter protectAdapter;

    private List<ProtectInstance> protectInstanceList;

    private int maintainDay = 0;

    private int id;

    private int store_id;

    private String phone;

    private int volumes;

    private String store_type_name;

    private float housePrice;

    private int maintenanceAllDays;

    private int maintenanceDays;

    private float maintenancePrice;

    private float safePrice;

    private float discount_store_month;

    private LinearLayout gone_lin;

    private View gone_view;

    private EditText renewals_coupon_check_edit;

    private String money_free = "0";

    private float money_total;

    private int couponID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renewals_layout);
        phone = Information.getStringConfig(RenewalsActivity.this, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(RenewalsActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient = new AsyncHttpClient();
        id = getIntent().getIntExtra("id", Constants.convention);
        store_id = getIntent().getIntExtra("store_id", Constants.convention);
        oid = getIntent().getStringExtra("oid");
        safe_id = getIntent().getIntExtra("safe_id", Constants.convention);
        days = getIntent().getIntExtra("days", Constants.convention);
        updated_at = getIntent().getStringExtra("updated_at");
        goods_protect_rules_id = getIntent().getIntExtra("goods_protect_rules_id", Constants.convention);
        if (goods_protect_rules_id == Constants.convention) {
            System.out.println("2222");
            goods_id = false;
        } else {
            System.out.println("1111");
            goods_id = true;
        }
        star_time = getIntent().getStringExtra("star_time");
        end_time = getIntent().getStringExtra("end_time");
        System.out.println("star_time:" + star_time + "|" + star_time.equals("null") + "|");
        price = getIntent().getFloatExtra("price", Constants.convention);
        setTitleHeight();
        init();
        getSafe();
        getProtect();
        getVolume();
    }

    private float getStoragePrice() {
        if (housePrice != Constants.convention && volumes != 0 && !newly_increased_days_edit.getText().toString().equals("")) {
            System.out.println("getStoragePrice:" + housePrice + "*" + volumes + "*" + getAllDays());
            if (Integer.parseInt(newly_increased_days_edit.getText().toString()) >= 30) {
                return housePrice * volumes * ( Integer.parseInt(newly_increased_days_edit.getText().toString())+days ) * discount_store_month;
            } else {
                return housePrice * volumes * ( Integer.parseInt(newly_increased_days_edit.getText().toString())+days );
            }


        }
        return 0;
    }

    private float getMaintenancePrice() {
        if (maintenanceAllDays != Constants.convention && maintenanceDays != Constants.convention && maintenancePrice != Constants.convention &&
                !renewals_maintenance_time_start_edit.getText().toString().equals("") && !renewals_maintenance_time_end_edit.getText().toString().equals("")) {
            System.out.println("getMaintenancePrice:" + maintenanceAllDays + "*" + maintenancePrice);
            return maintenanceAllDays * maintenancePrice;
        }
        return 0;
    }

    private float getSafePrice() {
        if (safePrice != Constants.convention) {
            System.out.println("getSafePrice:" + safePrice);
            return safePrice;
        }
        return 0;
    }

    private float userCoupon(float price , float couponPrice){

        float userCouponPrice = price - couponPrice;
        if (price != 0){
            if (userCouponPrice > 0){
                return userCouponPrice;
            } else {
                renewals_price.setText("");
                money_free = "0";
                money_total = Constants.convention;
                couponID = Constants.convention;
                Toast.makeText(RenewalsActivity.this,"预算价格不满足优惠券的满减，请重新选择优惠券！",Toast.LENGTH_SHORT).show();
                return  price;
            }
        } else {
            return price;
        }


    }


    private void getProtect() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);

        asyncHttpClient.get(Constants.MAIN_LINK + "/api/goods/protect/list", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getProtect:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    int cd = Constants.convention;
                    if (code.equals("0")) {
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0) {
                            for (int i = 0; i < success_Array.length(); i++) {
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int id = success_Object.optInt("id", Constants.convention);
                                int aid = success_Object.optInt("aid", Constants.convention);
                                int bid = success_Object.optInt("bid", Constants.convention);
                                int cid = success_Object.optInt("cid", Constants.convention);
                                int days = success_Object.optInt("days", Constants.convention);
                                float price = Float.parseFloat(success_Object.optString("price", "0"));
                                String content = success_Object.optString("content");

                                ProtectInstance provinceInstance = new ProtectInstance(id, aid, bid, cid, days, price, content);
                                protectInstanceList.add(provinceInstance);

                                if (id == goods_protect_rules_id) {
                                    cd = i;
                                }

                            }

                            protectAdapter.notifyDataSetChanged();

                            renewals_maintain_spinner.setSelection(cd, true);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getProtect接口不通或网络异常：" + throwable.getMessage());
            }

        });
    }

    private void getVolume() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);
        asyncHttpClient.get(Constants.MAIN_LINK + "/api/goods_stored/" + oid + "/show", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")) {
                        JSONObject success_Object = object.optJSONObject("success");
                        volumes = success_Object.optInt("contain", 0);
                        store_type_name = success_Object.optString("store_type_name");
                        getStorePrice();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getVolume接口异常或网络不通：" + throwable.getMessage());
            }
        });
    }

    private void getStorePrice() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);
        System.out.println("getStorePrice:" + store_id);
        asyncHttpClient.get(Constants.MAIN_LINK + "/api/store/show/" + store_id, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getStorePrice:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")) {
                        JSONObject success_Object = object.optJSONObject("success");
                        JSONArray stock_Arrary = success_Object.optJSONArray("stock");
                        if (stock_Arrary != null && stock_Arrary.length() > 0) {
                            for (int i = 0; i < stock_Arrary.length(); i++) {
                                JSONObject stock_Object = stock_Arrary.optJSONObject(i);
                                float price = Float.parseFloat(stock_Object.optString("price", "0"));
                                String store_type_name2 = stock_Object.optString("store_type_name");
                                if (store_type_name2.equals(store_type_name)) {
                                    housePrice = price;
                                    discount_store_month = Float.parseFloat(stock_Object.optString("discount_store_month"));
                                    return;
                                }
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
                System.out.println("getStorePrice接口不通或网络异常:" + throwable.getMessage());
            }
        });
    }

    private void getSafe() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);

        asyncHttpClient.get(Constants.MAIN_LINK + "/api/safe/list", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getSafe:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")) {
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0) {
                            stockSafeInstances.add(new StockSafeInstance(Constants.convention, Constants.convention, "请选择需要的保险"));
                            for (int i = 0; i < success_Array.length(); i++) {
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int id = success_Object.optInt("id");
                                float price = Float.parseFloat(success_Object.optString("price", "0"));
                                String safe_name = success_Object.optString("safe_name");
                                if (id == safe_id) {
                                    stockSafeInstances.clear();
                                    stockSafeInstances.add(new StockSafeInstance(id, price, safe_name));
                                    return;
                                } else {
                                    StockSafeInstance stockSafeInstance = new StockSafeInstance(id, price, safe_name);
                                    stockSafeInstances.add(stockSafeInstance);
                                }
                            }

                            stockSafeAdapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getSafe接口不通或网络异常：" + throwable.getMessage());
            }

        });

    }

    private void init() {
        renewals_oid = (TextView) findViewById(R.id.renewals_oid);
        renewal_original_days_text = (TextView) findViewById(R.id.renewal_original_days_text);
        renewals_expire_time_text = (TextView) findViewById(R.id.renewals_expire_time_text);
        renewals_price = (TextView) findViewById(R.id.renewals_price);
        renewals_original_cost_text = (TextView) findViewById(R.id.renewals_original_cost_text);
        renewals_safe_spinner = (Spinner) findViewById(R.id.renewals_safe_spinner);
        renewals_maintain_spinner = (Spinner) findViewById(R.id.renewals_maintain_spinner);
        newly_increased_days_edit = (EditText) findViewById(R.id.newly_increased_days_edit);
        renewals_maintenance_time_start_edit = (EditText) findViewById(R.id.renewals_maintenance_time_start_edit);
        renewals_maintenance_time_end_edit = (EditText) findViewById(R.id.renewals_maintenance_time_end_edit);
        renewals_coupon_check_edit = (EditText) findViewById(R.id.renewals_coupon_check_edit);
        back_bt = (Button) findViewById(R.id.back_bt);
        renewals_bt = (Button) findViewById(R.id.renewals_bt);
        gone_lin = (LinearLayout) findViewById(R.id.gone_lin);
        gone_view = findViewById(R.id.gone_view);
        if (!oid.equals("")) {
            renewals_oid.setText(oid);
        }
        if (days != Constants.convention) {
            renewal_original_days_text.setText(days + "天");
        }
        if (!updated_at.equals("")) {
            renewals_expire_time_text.setText("(" + updated_at + "物品到期)");
        }

        if (!star_time.equals("") && !star_time.equals("null")) {
            String[] years = star_time.split(" ");
            renewals_maintenance_time_start_edit.setText(years[0]);
            renewals_maintenance_time_start_edit.setEnabled(false);
        } else {
            renewals_maintenance_time_start_edit.setText(getTime());
        }

        if (!end_time.equals("") && !end_time.equals("null")) {
            String[] years = end_time.split(" ");
            renewals_maintenance_time_end_edit.setText(years[0]);
        }

        if (price != Constants.convention) {
            renewals_original_cost_text.setText("(原价为：" + price + "元)");
        }


        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        renewals_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newly_increased_days_edit.getText().toString().equals("")){
                    Toast.makeText(RenewalsActivity.this,"请填写正确的新增天数!",Toast.LENGTH_SHORT).show();
                } else if (renewals_maintenance_time_start_edit.getText().toString().equals("") || renewals_maintenance_time_end_edit.getText().toString().equals("")){
                    Toast.makeText(RenewalsActivity.this,"请选择正确的维护时间!",Toast.LENGTH_SHORT).show();
                } else {
                    renewals();
                }
            }
        });

        renewals_maintenance_time_start_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] years = null;
                if (renewals_maintenance_time_start_edit.getText().toString().equals("")) {
                    years = getYearTime().split("-");
                } else {
                    years = renewals_maintenance_time_start_edit.getText().toString().split("-");
                }

                String year = years[0];
                String month = years[1];
                String day = years[2];

                BLTimerAlert.showDateAlert(RenewalsActivity.this, Integer.parseInt(year)-1, Integer.parseInt(month)-1, Integer.parseInt(day)-1, new BLTimerAlert.OnDateAlertClick() {
                    @Override
                    public void onClick(int year, int month, int day) {

                        String month_s = "";
                        String day_s = "";
                        if (month < 10) {
                            month_s = "0" + (month);
                        } else {
                            month_s = (month) + "";
                        }

                        if (day < 10) {
                            day_s = "0" + (day);
                        } else {
                            day_s = (day) + "";
                        }
                        long days_ = daysReduce(getTime(), year + "-" + month_s + "-" + day_s);
                        System.out.println("days_:" + days_);
                        long differDays = Constants.convention;
                        if (!renewals_maintenance_time_start_edit.getText().toString().equals("")) {
                            differDays = daysReduce(year + "-" + month_s + "-" + day_s, getTime());
                        }

                        System.out.println("differDays:" + differDays);

                        if (days_ > 0) {
                            Toast.makeText(RenewalsActivity.this, "开始时间必须大于等于当前时间!", Toast.LENGTH_SHORT).show();
                            renewals_maintenance_time_start_edit.setText(getYearTime());
                        } else if (differDays > getAllDays()) {
                            Toast.makeText(RenewalsActivity.this, "开始时间必须小于当前时间+总存放天数!", Toast.LENGTH_SHORT).show();
                        } else {

                            renewals_maintenance_time_start_edit.setText(year + "-" + month_s + "-" + day_s);

                            long startReduceEnd = daysReduce(renewals_maintenance_time_start_edit.getText().toString() , renewals_maintenance_time_end_edit.getText().toString());

                            System.out.println("startReduceEnd:"+startReduceEnd);

                            if (startReduceEnd > 0){
                                renewals_maintenance_time_end_edit.setText(year + "-" + month_s + "-" + day_s);
                            }

                            if (!renewals_maintenance_time_end_edit.getText().toString().equals("")) {
                                maintenanceAllDays = (int) daysReduce(renewals_maintenance_time_end_edit.getText().toString(), renewals_maintenance_time_start_edit.getText().toString()) / maintenanceDays;
                            }

                            System.out.println("maintenanceAllDays:" + maintenanceAllDays);

                            if (maintenanceAllDays <= 1) {
                                maintenanceAllDays = 1;
                            }

                            System.out.println("maintenanceAllDays:" + maintenanceAllDays);

                            renewals_price.setText(userCoupon(getStoragePrice() + getMaintenancePrice() + getSafePrice(),Float.parseFloat(money_free)) + "");

                        }

                    }
                });
            }
        });

        renewals_maintenance_time_end_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] years = null;
                if (renewals_maintenance_time_end_edit.getText().toString().equals("")) {
                    years = getYearTime().split("-");
                } else {
                    years = renewals_maintenance_time_end_edit.getText().toString().split("-");
                }

                String year = years[0];
                String month = years[1];
                String day = years[2];

                BLTimerAlert.showDateAlert(RenewalsActivity.this, Integer.parseInt(year) -1, Integer.parseInt(month)-1, Integer.parseInt(day)-1, new BLTimerAlert.OnDateAlertClick() {
                    @Override
                    public void onClick(int year, int month, int day) {

                        String month_s = "";
                        String day_s = "";
                        if (month < 10) {
                            month_s = "0" + (month);
                        } else {
                            month_s = (month) + "";
                        }

                        if (day < 10) {
                            day_s = "0" + (day);
                        } else {
                            day_s = (day) + "";
                        }
                        long days_ = daysReduce(year + "-" + month_s + "-" + day_s, renewals_maintenance_time_start_edit.getText().toString());

                        long differDays = Constants.convention;
                        if (!renewals_maintenance_time_start_edit.getText().toString().equals("")) {
                            differDays = daysReduce(year + "-" + month_s + "-" + day_s, getTime());
                        }
                        System.out.println("differDays:" + differDays);
                        System.out.println("days:" + days);
                        if (days_ < 0) {
                            Toast.makeText(RenewalsActivity.this, "结束时间必须大于开始时间!", Toast.LENGTH_SHORT).show();
//                            renewals_maintenance_time_end_edit.setText("");
                        } else if (differDays+1 > getAllDays()) {
                            Toast.makeText(RenewalsActivity.this, "维护时长必须小于总存放天数!", Toast.LENGTH_SHORT).show();
                        } else {
                            renewals_maintenance_time_end_edit.setText(year + "-" + month_s + "-" + day_s);

                            maintenanceAllDays = (int) daysReduce(renewals_maintenance_time_end_edit.getText().toString(), renewals_maintenance_time_start_edit.getText().toString()) / maintenanceDays;

                            System.out.println("maintenanceAllDays:" + maintenanceAllDays);

                            if (maintenanceAllDays <= 1) {
                                maintenanceAllDays = 1;
                            }

                            System.out.println("maintenanceAllDays:" + maintenanceAllDays);

                            renewals_price.setText(userCoupon(getStoragePrice() + getMaintenancePrice() + getSafePrice(),Float.parseFloat(money_free)) + "");

                        }

                    }
                });
            }
        });

        stockSafeInstances = new ArrayList<>();
        stockSafeAdapter = new StockSafeAdapter(RenewalsActivity.this, stockSafeInstances);
        renewals_safe_spinner.setAdapter(stockSafeAdapter);

        renewals_safe_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StockSafeInstance instance = stockSafeInstances.get(position);
                safe_id = instance.getId();
                safePrice = instance.getPrice();
                renewals_price.setText(userCoupon(getStoragePrice() + getMaintenancePrice() + getSafePrice(),Float.parseFloat(money_free)) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        protectInstanceList = new ArrayList<>();
//        protectInstanceList.add(new ProtectInstance(Constants.convention, Constants.convention, Constants.convention, Constants.convention, Constants.convention, Constants.convention, "其他"));
        protectAdapter = new ProtectAdapter(protectInstanceList, RenewalsActivity.this);
        renewals_maintain_spinner.setAdapter(protectAdapter);

        renewals_maintain_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProtectInstance protectInstance = protectInstanceList.get(position);
                maintainDay = protectInstance.getDays();
                goods_protect_rules_id = protectInstance.getId();
                maintenanceDays = protectInstance.getDays();
                maintenancePrice = protectInstance.getPrice();

                if (!renewals_maintenance_time_start_edit.getText().toString().equals("") && !renewals_maintenance_time_end_edit.getText().toString().equals("")) {
                    maintenanceAllDays = (int) daysReduce(renewals_maintenance_time_end_edit.getText().toString(), renewals_maintenance_time_start_edit.getText().toString()) / maintenanceDays;
                }

                if (maintenanceAllDays <= 1) {
                    maintenanceAllDays = 1;
                }

                renewals_price.setText(userCoupon(getStoragePrice() + getMaintenancePrice() + getSafePrice(),Float.parseFloat(money_free)) + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        newly_increased_days_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!renewals_maintenance_time_start_edit.getText().toString().equals("") && !renewals_maintenance_time_end_edit.getText().toString().equals("")) {
//                    int reduceDays = (int) daysReduce(renewals_maintenance_time_end_edit.getText().toString(), renewals_maintenance_time_start_edit.getText().toString());
//                    System.out.println("reduceDays:"+reduceDays);
                System.out.println("getAllDays:"+getAllDays());
                renewals_maintenance_time_end_edit.setText(Constants.getDateStr(renewals_maintenance_time_start_edit.getText().toString(),getAllDays()));
                if (!renewals_maintenance_time_end_edit.getText().toString().equals("") && !renewals_maintenance_time_start_edit.getText().toString().equals("")){
                    maintenanceAllDays = (int) daysReduce(renewals_maintenance_time_end_edit.getText().toString(), renewals_maintenance_time_start_edit.getText().toString()) / maintenanceDays;
                }

                renewals_price.setText(userCoupon(getStoragePrice() + getMaintenancePrice() + getSafePrice(),Float.parseFloat(money_free)) + "");
//                    if (reduceDays > getAllDays()) {
//                        Toast.makeText(RenewalsActivity.this, "总天数不能小于维护时长!请重新填写天数或调整维护时长", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (!end_time.equals("") && !end_time.equals("null")) {
//                            String[] years = end_time.split(" ");
//                            renewals_maintenance_time_end_edit.setText(years[0]);
//                            renewals_price.setText(getStoragePrice() + getMaintenancePrice() + getSafePrice() + "");
//                        }
//                    }

//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        renewals_coupon_check_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(renewals_price.getText().toString()) != 0){
                    Intent intent = new Intent(RenewalsActivity.this, CouponActivity.class);
                    intent.putExtra("price",Float.parseFloat(renewals_price.getText().toString()));
                    startActivityForResult(intent,0);
                } else {
                    Toast.makeText(RenewalsActivity.this,"请先确定价格再使用优惠券！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1){
            money_free = data.getStringExtra("money_free");
            money_total = data.getFloatExtra("money_total", Constants.convention);
            couponID = data.getIntExtra("couponID", Constants.convention);
        }
    }

    private int getAllDays() {
        if (!newly_increased_days_edit.getText().toString().equals("")) {
            return Integer.parseInt(newly_increased_days_edit.getText().toString()) + days;
        } else {
            return days;
        }
    }

    private void renewals() {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);

        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        if (goods_protect_rules_id != Constants.convention) {
            requestParams.put("goods_protect_rules_id", goods_protect_rules_id + "");
            requestParams.put("goods_protect_start_time", renewals_maintenance_time_start_edit.getText().toString());
            requestParams.put("goods_protect_end_time", renewals_maintenance_time_end_edit.getText().toString());
        }
        if (safe_id != Constants.convention) {
            requestParams.put("goods_safe_id", safe_id + "");
        }
        requestParams.put("price", renewals_price.getText().toString()+ "");
        requestParams.put("days", Integer.parseInt(newly_increased_days_edit.getText().toString()) + days + "");

        System.out.println("renewals:" + requestParams.toString());

        asyncHttpClient.post(Constants.MAIN_LINK + "/api/goods_stored/" + oid + "/update", requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("renewals:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    Toast.makeText(RenewalsActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (code.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction(Constants.STOCK_STATUS_CHANGE);
                        sendBroadcast(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("renewals接口不通或网络异常:" + throwable.getMessage());
            }

        });
    }

    private long daysReduce(String end_time, String start_time) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = df.parse(end_time);
            Date d2 = df.parse(start_time);
            long diff = d1.getTime() - d2.getTime();
            System.out.println("diff:" + d1.getTime());
            System.out.println("diff:" + d2.getTime());
            System.out.println("diff:" + diff);
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
            return Constants.convention;
        }
    }

    private String getYearTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private void setTitleHeight() {
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
        View view = findViewById(R.id.renewals_title);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

}
