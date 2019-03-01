package com.zwxt.shareApp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.zwxt.shareApp.activity.CouponActivity;
import com.zwxt.shareApp.activity.MailActivity;
import com.zwxt.shareApp.activity.RecommendActivity;
import com.zwxt.shareApp.activity.ThingsActivity;
import com.zwxt.shareApp.adapter.GoodsListAdapter;
import com.zwxt.shareApp.adapter.GoodsTidAdapter;
import com.zwxt.shareApp.adapter.ProtectAdapter;
import com.zwxt.shareApp.adapter.SaveTypeAdapter;
import com.zwxt.shareApp.adapter.StockSafeAdapter;
import com.zwxt.shareApp.adapter.StoreTypeAdapter;
import com.zwxt.shareApp.instance.DetailsInstance;
import com.zwxt.shareApp.instance.GoodsListInstance;
import com.zwxt.shareApp.instance.ProtectInstance;
import com.zwxt.shareApp.instance.SaveInstance;
import com.zwxt.shareApp.instance.StockSafeInstance;
import com.zwxt.shareApp.instance.StoreTypeInstance;
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
 * Created by Administrator on 2018/8/14.
 */

public class MySendFragment extends Fragment {

    private EditText address_edit;

    private Spinner house_type_spinner , save_type_spinner , goods_spinner ,goods_class_spinner_first ,goods_class_spinner_second ,goods_class_spinner_third ,maintenance_type , stock_safe_spinner;

    private CheckBox wechat_check,ali_check,balance_check;

    private String phone = "";

    private String name = "";

    private String address = "";

    private String token;

    private AsyncHttpClient asyncHttpClient;

    private List<DetailsInstance> storeTypeList;

    private StoreTypeAdapter storeTypeAdapter;

    private ProtectAdapter protectAdapter;

    private List<ProtectInstance> protectInstanceList;

    private List<StoreTypeInstance> goodsTidList;

    private GoodsTidAdapter goodsTidAdapter;

    private List<GoodsListInstance> goodsList_one;

    private GoodsListAdapter goodsListAdapter_one;

    private List<GoodsListInstance> goodsList_two;

    private GoodsListAdapter goodsListAdapter_two;

    private List<GoodsListInstance> goodsList_three;

    private GoodsListAdapter goodsListAdapter_three;

    private List<StockSafeInstance> stockSafeInstances;

    private StockSafeAdapter stockSafeAdapter;

    private Button apply_to_house_bt;

    private EditText house_check_edit;

    private int id = Constants.convention;

    private String houseName = "";

    private EditText logistics_time_edit;

    private int province = Constants.convention;

    private int city = Constants.convention;

    private int stock_id = Constants.convention;

    private float housePrice = 0;

    private int goods_tid = Constants.convention;

    private int aid = Constants.convention;

    private int bid = Constants.convention;

    private int cid = Constants.convention;

    private int houseFree ;

    private EditText volume_edit;

    private EditText days_edit;

    private EditText remarks_edit;

    private EditText maintenance_time_start_edit , maintenance_time_end_edit , coupon_check_edit , save_type_edit;

    private View save_type_view;

    private int goods_protect_rules_id = Constants.convention;

    private String loginPhone ;

    private boolean active = false;

    private LinearLayout start_lin,end_lin , saveLin;

    private View start_view , end_view;

    private int goods_safe_id = Constants.convention;

    private TextView calculation_price_text , save_type_text;

    private int maintenanceDays ;

    private float maintenancePrice;

    private float safePrice;

    private int maintenanceAllDays ;

    private String money_free = "0";

    private float money_total;

    private int couponID;

    private int saveType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_send_layout,container,false);

        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        id = getArguments().getInt("id", Constants.convention);
        loginPhone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        houseName = getArguments().getString("houseName");
        init(view);
        goods_protect();
        goodsTid();
        goodsList(0,1);
        safeList();
        getStoreType();

        active = getActivity().getIntent().getBooleanExtra("active",false);
        if (active){
            phone = getActivity().getIntent().getExtras().getString("phone","");
            name = getActivity().getIntent().getExtras().getString("name","");
            address = getActivity().getIntent().getExtras().getString("address","");
            province = getActivity().getIntent().getExtras().getInt("province");
            city = getActivity().getIntent().getExtras().getInt("city");
            System.out.println("addTrue:"+phone+"|"+name+"|"+address);
            address_edit.setText(address);
        }

        return view;
    }

    private void getStoreType(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/store/show/"+id,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getStoreType:"+s);
                storeTypeList.clear();
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_Object = object.optJSONObject("success");
                        JSONArray stock_Arrary = success_Object.optJSONArray("stock");
                        if (stock_Arrary != null && stock_Arrary.length() > 0){
                            for (int i = 0 ; i < stock_Arrary.length() ; i++){
                                JSONObject stock_Object = stock_Arrary.optJSONObject(i);
//                                int id = success_Object.optInt("id",Constants.convention);
                                int total = stock_Object.optInt("total");
                                float price = Float.parseFloat(stock_Object.optString("price","0"));
                                System.out.println("showPirce:"+price);
                                String store_type_name = stock_Object.optString("store_type_name");
                                int free = stock_Object.optInt("free");
                                int store_id = stock_Object.optInt("store_id");
                                int store_type_id = stock_Object.optInt("store_type_id");
                                String create_at = stock_Object.optString("create_at");
                                float discount_store_month = Float.parseFloat(stock_Object.optString("discount_store_month","1"));
                                int id = stock_Object.optInt("id", Constants.convention);
                                String updated_at = stock_Object.optString("updated_at");
                                DetailsInstance detailsInstance = new DetailsInstance(free,store_type_name,total,price,store_id,store_type_id,create_at,discount_store_month,id,updated_at);
//                                String type_name = success_Object.optString("type_name");
//                                StoreTypeInstance storeTypeInstance = new StoreTypeInstance(id,type_name);
                                storeTypeList.add(detailsInstance);
                            }
                            storeTypeAdapter.notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(getActivity(),"获取仓库类型失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"数据解析失败",Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getStoreType接口不通或者网络异常");
            }
        });

    }

    private void goods_protect(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods/protect/list",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goods_protect:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int id = success_Object.optInt("id", Constants.convention);
                                int aid = success_Object.optInt("aid", Constants.convention);
                                int bid = success_Object.optInt("bid", Constants.convention);
                                int cid = success_Object.optInt("cid", Constants.convention);
                                int days = success_Object.optInt("days", Constants.convention);
                                float price = Float.parseFloat(success_Object.optString("price","0"));
                                String content = success_Object.optString("content");
                                ProtectInstance provinceInstance = new ProtectInstance(id,aid,bid,cid,days,price,content);
                                protectInstanceList.add(provinceInstance);
                            }
                            protectAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goods_protect接口不通或者网络异常");
            }
        });
    }

    private void goodsTid(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods/tid/list",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goodsTid:"+s);

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int id = success_Object.optInt("id", Constants.convention);
                                String tid_name = success_Object.optString("tid_name");
                                StoreTypeInstance storeTypeInstance = new StoreTypeInstance(id,tid_name);
                                goodsTidList.add(storeTypeInstance);
                            }
                            goodsTidAdapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goodsTId接口异常或网络不通");
            }
        });
    }

    private void goodsList(int fid , final int level){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods/list?fid="+fid+"&level="+level,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goodsList:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            if (level != 1){
                                for (int i =0 ; i <success_Array.length() ; i++){
                                    JSONObject success_Objcet = success_Array.optJSONObject(i);
                                    int aid = success_Objcet.optInt("aid", Constants.convention);
                                    int bid = success_Objcet.optInt("bid", Constants.convention);
                                    int cid = success_Objcet.optInt("cid", Constants.convention);
                                    int discount_store_month = success_Objcet.optInt("discount_store_month", Constants.convention);
                                    String goods_name = success_Objcet.optString("goods_name");
                                    int id = success_Objcet.optInt("id", Constants.convention);
                                    String image = success_Objcet.optString("image");
                                    int level = success_Objcet.optInt("level", Constants.convention);
                                    int price_rent = success_Objcet.optInt("price_rent", Constants.convention);
                                    int price_sale = success_Objcet.optInt("price_sale", Constants.convention);
                                    int price_store = success_Objcet.optInt("price_store", Constants.convention);
                                    int tid = success_Objcet.optInt("tid", Constants.convention);
                                    GoodsListInstance goodsListInstance = new GoodsListInstance(aid,bid,cid,discount_store_month,goods_name,id,image,level,price_rent,price_sale, price_store,tid);
                                    if (level == 2) {
                                        goodsList_two.add(goodsListInstance);
                                    } else if (level == 3){
                                        goodsList_three.add(goodsListInstance);
                                    }
                                }
                            } else {
                                for (int i =success_Array.length()-1 ; i >= 0 ; i --){
                                    JSONObject success_Objcet = success_Array.optJSONObject(i);
                                    int aid = success_Objcet.optInt("aid", Constants.convention);
                                    int bid = success_Objcet.optInt("bid", Constants.convention);
                                    int cid = success_Objcet.optInt("cid", Constants.convention);
                                    int discount_store_month = success_Objcet.optInt("discount_store_month", Constants.convention);
                                    String goods_name = success_Objcet.optString("goods_name");
                                    int id = success_Objcet.optInt("id", Constants.convention);
                                    String image = success_Objcet.optString("image");
                                    int level = success_Objcet.optInt("level", Constants.convention);
                                    int price_rent = success_Objcet.optInt("price_rent", Constants.convention);
                                    int price_sale = success_Objcet.optInt("price_sale", Constants.convention);
                                    int price_store = success_Objcet.optInt("price_store", Constants.convention);
                                    int tid = success_Objcet.optInt("tid", Constants.convention);
                                    GoodsListInstance goodsListInstance = new GoodsListInstance(aid,bid,cid,discount_store_month,goods_name,id,image,level,price_rent,price_sale, price_store,tid);
                                    goodsList_one.add(goodsListInstance);
                                }
                            }


                            if (level == 1){
                                goodsListAdapter_one.notifyDataSetChanged();
                            } else if (level == 2){
                                goodsListAdapter_two.notifyDataSetChanged();
                            } else if (level == 3){
                                goodsListAdapter_three.notifyDataSetChanged();
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
                System.out.println("goodsList接口无法访问或网络不通");
            }


        });
    }

    private void safeList (){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }

        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/safe/list",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("safeList:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i = 0 ; i < success_Array.length() ; i++){
                                JSONObject success_Objcet = success_Array.optJSONObject(i);
                                int id = success_Objcet.optInt("id");
                                float price = Float.parseFloat(success_Objcet.optString("price","0"));
                                String safe_name = success_Objcet.optString("safe_name");
                                StockSafeInstance stockSafeInstance = new StockSafeInstance(id,price,safe_name);
                                stockSafeInstances.add(stockSafeInstance);
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
                System.out.println("safeList接口不通或网络异常:"+throwable.getMessage());
            }

        });

    }

    private void init(View view){
        address_edit = (EditText) view.findViewById(R.id.address_edit);
        house_type_spinner = (Spinner) view.findViewById(R.id.house_type_spinner);
        save_type_spinner = (Spinner) view.findViewById(R.id.save_type_spinner);
        saveLin = (LinearLayout) view.findViewById(R.id.saveLin);
        save_type_text = (TextView) view.findViewById(R.id.save_type_text);
        save_type_edit = (EditText) view.findViewById(R.id.save_type_edit);
        save_type_view = view.findViewById(R.id.save_type_view);
        goods_spinner = (Spinner) view.findViewById(R.id.goods_spinner);
        goods_class_spinner_first = (Spinner) view.findViewById(R.id.goods_class_spinner_first);
        goods_class_spinner_second = (Spinner) view.findViewById(R.id.goods_class_spinner_second);
        goods_class_spinner_third = (Spinner) view.findViewById(R.id.goods_class_spinner_third);
        maintenance_type = (Spinner) view.findViewById(R.id.maintenance_type);
        wechat_check = (CheckBox) view.findViewById(R.id.wechat_check);
        ali_check = (CheckBox) view.findViewById(R.id.ali_check);
        balance_check = (CheckBox) view.findViewById(R.id.balance_check);
        apply_to_house_bt = (Button) view.findViewById(R.id.apply_to_house_bt);
        maintenance_time_start_edit = (EditText) view.findViewById(R.id.maintenance_time_start_edit);
        maintenance_time_end_edit = (EditText) view.findViewById(R.id.maintenance_time_end_edit);
        house_check_edit = (EditText) view.findViewById(R.id.house_check_edit);
        logistics_time_edit = (EditText) view.findViewById(R.id.logistics_time_edit);
        volume_edit = (EditText) view.findViewById(R.id.volume_edit);
        days_edit = (EditText) view.findViewById(R.id.days_edit);
        remarks_edit = (EditText) view.findViewById(R.id.remarks_edit);
        start_lin = (LinearLayout) view.findViewById(R.id.start_lin);
        end_lin = (LinearLayout) view.findViewById(R.id.end_lin);
        start_view = view.findViewById(R.id.start_view);
        end_view = view.findViewById(R.id.end_view);
        stock_safe_spinner = (Spinner) view.findViewById(R.id.stock_safe_spinner);
        calculation_price_text = (TextView) view.findViewById(R.id.calculation_price_text);
        coupon_check_edit = (EditText) view.findViewById(R.id.coupon_check_edit);

        if (id != Constants.convention){
            house_check_edit.setText(houseName);
        }

        address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),MailActivity.class);
                intent.putExtra("add",true);
                startActivityForResult(intent,1);
            }
        });
        storeTypeList = new ArrayList<>();
//        storeTypeList.add(new DetailsInstance(Constants.convention,"其他",Constants.convention,Constants.convention,Constants.convention,Constants.convention,"",Constants.convention,Constants.convention,""));
        storeTypeAdapter = new StoreTypeAdapter(storeTypeList,getActivity());
        house_type_spinner.setAdapter(storeTypeAdapter);

        final List<SaveInstance> saveTypeList = new ArrayList<>();
        saveTypeList.add(new SaveInstance("寄存"));
        saveTypeList.add(new SaveInstance("寄租"));
        saveTypeList.add(new SaveInstance("寄售"));

        SaveTypeAdapter saveTypeAdapter = new SaveTypeAdapter(getActivity(),saveTypeList);
        save_type_spinner.setAdapter(saveTypeAdapter);

        protectInstanceList = new ArrayList<>();
        protectInstanceList.add(new ProtectInstance(Constants.convention, Constants.convention, Constants.convention, Constants.convention, Constants.convention, Constants.convention,""));
        protectAdapter = new ProtectAdapter(protectInstanceList,getActivity());
        maintenance_type.setAdapter(protectAdapter);

        goodsTidList = new ArrayList<>();
//        goodsTidList.add(new StoreTypeInstance(Constants.convention,"其他"));
        goodsTidAdapter = new GoodsTidAdapter(goodsTidList,getActivity());
        goods_spinner.setAdapter(goodsTidAdapter);

        goodsList_one = new ArrayList<>();
//        goodsList_one.add(new GoodsListInstance(Constants.convention,Constants.convention,Constants.convention,Constants.convention,"其他",Constants.convention,"",Constants.convention,Constants.convention,Constants.convention,Constants.convention,Constants.convention));
        goodsListAdapter_one = new GoodsListAdapter(goodsList_one,getActivity());
        goods_class_spinner_first.setAdapter(goodsListAdapter_one);

        goodsList_two = new ArrayList<>();
//        goodsList_two.add(new GoodsListInstance(Constants.convention,Constants.convention,Constants.convention,Constants.convention,"其他",Constants.convention,"",Constants.convention,Constants.convention,Constants.convention,Constants.convention,Constants.convention));
        goodsListAdapter_two = new GoodsListAdapter(goodsList_two,getActivity());
        goods_class_spinner_second.setAdapter(goodsListAdapter_two);

        goodsList_three = new ArrayList<>();
//        goodsList_three.add(new GoodsListInstance(Constants.convention,Constants.convention,Constants.convention,Constants.convention,"其他",Constants.convention,"",Constants.convention,Constants.convention,Constants.convention,Constants.convention,Constants.convention));
        goodsListAdapter_three = new GoodsListAdapter(goodsList_three,getActivity());
        goods_class_spinner_third.setAdapter(goodsListAdapter_three);

        stockSafeInstances = new ArrayList<>();
        stockSafeInstances.add(new StockSafeInstance(Constants.convention, Constants.convention,"不需要保险"));
        stockSafeAdapter = new StockSafeAdapter(getActivity(),stockSafeInstances);
        stock_safe_spinner.setAdapter(stockSafeAdapter);



        house_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DetailsInstance detailsInstance = storeTypeList.get(position);
                stock_id = detailsInstance.getId();
                housePrice = detailsInstance.getPrice();
                houseFree = detailsInstance.getFree();

                calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) + "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveType = position;
                if (saveType == 1){

                    System.out.println("出租");

                    saveLin.setVisibility(View.VISIBLE);

                    save_type_text.setText("出租价格");
                    save_type_edit.setHint("请填写出租价格(元/天)");

                    save_type_view.setVisibility(View.VISIBLE);
                } else if (saveType == 2){

                    System.out.println("售出");

                    saveLin.setVisibility(View.VISIBLE);

                    save_type_text.setText("售出价格");
                    save_type_edit.setHint("请填写售出价格(元)");

                    save_type_view.setVisibility(View.VISIBLE);
                } else {

                    saveLin.setVisibility(View.GONE);
                    save_type_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        maintenance_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProtectInstance protectInstance = protectInstanceList.get(position);
                goods_protect_rules_id = protectInstance.getId();
                maintenanceDays = protectInstance.getDays();
                maintenancePrice = protectInstance.getPrice();
                String name = protectInstance.getContent();
                if (name.equals("")){
                    start_lin.setVisibility(View.GONE);
                    end_lin.setVisibility(View.GONE);
                    start_view.setVisibility(View.GONE);
                    end_view.setVisibility(View.GONE);
                } else {
                    start_lin.setVisibility(View.VISIBLE);
                    end_lin.setVisibility(View.VISIBLE);
                    start_view.setVisibility(View.VISIBLE);
                    end_view.setVisibility(View.VISIBLE);
                }

                if (!maintenance_time_start_edit.getText().toString().equals("") && !maintenance_time_end_edit.getText().toString().equals("")){
                    maintenanceAllDays = (int)daysReduce(maintenance_time_end_edit.getText().toString(),maintenance_time_start_edit.getText().toString())/maintenanceDays;
                }

                if (maintenanceAllDays <= 1){
                    maintenanceAllDays = 1;
                }

                calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goods_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StoreTypeInstance storeTypeInstance = goodsTidList.get(position);
                goods_tid = storeTypeInstance.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goods_class_spinner_first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodsListInstance goodsListInstance = goodsList_one.get(position);
                aid = goodsListInstance.getAid();
                if (aid != Constants.convention){
                    goodsList(aid,2);
                }
                goodsList_two.clear();
//                goodsList_two.add(new GoodsListInstance(Constants.convention,Constants.convention,Constants.convention,Constants.convention,"其他",Constants.convention,"",Constants.convention,Constants.convention,Constants.convention,Constants.convention,Constants.convention));
//                goodsListAdapter_two = new GoodsListAdapter(goodsList_two,getActivity());
//                goods_class_spinner_second.setAdapter(goodsListAdapter_two);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goods_class_spinner_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodsListInstance goodsListInstance = goodsList_two.get(position);
                bid = goodsListInstance.getBid();
                if (bid != Constants.convention){
                    goodsList(bid,3);
                }
                goodsList_three.clear();
//                goodsList_three.add(new GoodsListInstance(Constants.convention,Constants.convention,Constants.convention,Constants.convention,"其他",Constants.convention,"",Constants.convention,Constants.convention,Constants.convention,Constants.convention,Constants.convention));
//                goodsListAdapter_three = new GoodsListAdapter(goodsList_three,getActivity());
//                goods_class_spinner_third.setAdapter(goodsListAdapter_three);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goods_class_spinner_third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodsListInstance goodsListInstance = goodsList_three.get(position);
                cid = goodsListInstance.getCid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        apply_to_house_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goods_protect_rules_id != Constants.convention &&(maintenance_time_start_edit.getText().toString().equals("") || maintenance_time_end_edit.getText().toString().equals(""))){
                    Toast.makeText(getActivity(),"请选择正确的维护时长！",Toast.LENGTH_SHORT).show();
                } else if (days_edit.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请填写存储天数！",Toast.LENGTH_SHORT).show();
                } else if (volume_edit.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请填写存储物品的占用体积！",Toast.LENGTH_SHORT).show();
                } else if (houseName == null){
                    Toast.makeText(getActivity(),"请选择存放的仓库！",Toast.LENGTH_SHORT).show();
                } else if (saveType > 0 && save_type_edit.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请填写租售价格!",Toast.LENGTH_SHORT).show();
                } else {
                    applyToHouse();
                }
            }
        });

        house_check_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecommendActivity.class);
                intent.putExtra("chooseHouse",true);
                startActivityForResult(intent,4);
            }
        });
        maintenance_time_start_edit.setText(getYearTime());

        maintenance_time_start_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] years = maintenance_time_start_edit.getText().toString().split("-");
                String year = years[0];
                String month = years[1];
                String day = years[2];
                BLTimerAlert.showDateAlert(getActivity(), Integer.parseInt(year)-1, Integer.parseInt(month)-1, Integer.parseInt(day)-1, new BLTimerAlert.OnDateAlertClick() {
                    @Override
                    public void onClick(int year, int month, int day) {
                        String month_s = "";
                        String day_s = "";
                        if (month < 10){
                            month_s = "0"+(month);
                        } else {
                            month_s = (month)+"";
                        }

                        if (day < 10){
                            day_s = "0"+(day);
                        } else {
                            day_s = (day)+"";
                        }
                        long days = daysReduce(getTime(),year+"-"+month_s+"-"+day_s);
                        long differDays = Constants.convention ;
                        if (!maintenance_time_end_edit.getText().toString().equals("")){
                            differDays = daysReduce(maintenance_time_end_edit.getText().toString(),year+"-"+month_s+"-"+day_s);
                        }
                        System.out.println("differDays:"+differDays);
                        System.out.println("days:"+days);
                        if (days > 0){
                            Toast.makeText(getActivity(),"开始时间必须大于等于当前时间!",Toast.LENGTH_SHORT).show();
                            maintenance_time_start_edit.setText(getYearTime());
                        } else if (differDays != Constants.convention && differDays <= 0){
                            Toast.makeText(getActivity(),"开始时间必须小于结束时间!",Toast.LENGTH_SHORT).show();
                        } else {
                            maintenance_time_start_edit.setText(year+"-"+month_s+"-"+day_s);

                            if (!maintenance_time_end_edit.getText().toString().equals("")){
                                maintenanceAllDays = (int)daysReduce(maintenance_time_end_edit.getText().toString(),maintenance_time_start_edit.getText().toString())/maintenanceDays;
                            }

                            System.out.println("maintenanceAllDays:"+maintenanceAllDays);

                            if (maintenanceAllDays <= 1){
                                maintenanceAllDays = 1;
                            }

                            System.out.println("maintenanceAllDays:"+maintenanceAllDays);

                            calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");
                        }

                    }
                });
            }
        });

//        String[] years = getTime().split("-");
//        String year = years[0];
//        String month = years[1];
//        String day = years[2]+1;

//        maintenance_time_end_edit.setText(year+"-"+month+"-"+day);

        maintenance_time_end_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] years  = null;
                if (maintenance_time_end_edit.getText().toString().equals("")){
                    years = getYearTime().split("-");
                } else {
                    years = maintenance_time_end_edit.getText().toString().split("-");
                }

                String year = years[0];
                String month = years[1];
                String day = years[2];

                BLTimerAlert.showDateAlert(getActivity(), Integer.parseInt(year)-1, Integer.parseInt(month)-1, Integer.parseInt(day)-1, new BLTimerAlert.OnDateAlertClick() {
                    @Override
                    public void onClick(int year, int month, int day) {
                        String month_s = "";
                        String day_s = "";
                        if (month < 10){
                            month_s = "0"+(month);
                        } else {
                            month_s = (month)+"";
                        }

                        if (day < 10){
                            day_s = "0"+(day);
                        } else {
                            day_s = (day)+"";
                        }

                        long days = daysReduce(year+"-"+month_s+"-"+day_s,maintenance_time_start_edit.getText().toString());
                        System.out.println("days:"+days);
                        if (days_edit.getText().toString().equals("")){
                            days_edit.setText(1+"");
                        }
                        if (days < 0){
                            Toast.makeText(getActivity(),"结束时间必须大于开始时间!",Toast.LENGTH_SHORT).show();
                            maintenance_time_end_edit.setText("");
                        } else if (Math.abs(days) > Integer.parseInt(days_edit.getText().toString())){
                            Toast.makeText(getActivity(),"维护时长不能超过储存天数！",Toast.LENGTH_SHORT).show();
                        }else {
                            maintenance_time_end_edit.setText(year+"-"+month_s+"-"+day_s);

                            maintenanceAllDays = (int)daysReduce(maintenance_time_end_edit.getText().toString(),maintenance_time_start_edit.getText().toString())/maintenanceDays;

                            System.out.println("maintenanceAllDays:"+maintenanceAllDays);

                            if (maintenanceAllDays <= 1){
                                maintenanceAllDays = 1;
                            }

                            System.out.println("maintenanceAllDays:"+maintenanceAllDays);

//                            int number = (int)days / maintenanceDays;
//                            if (number <= 0){
//                                number = 1;
//                            }
//
//                            float maintainPrices = maintenancePrice * number;
//
//                            System.out.println("价格："+maintainPrices);

                            calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");

                        }

                    }
                });

            }
        });


        logistics_time_edit.setText(getTime());
        logistics_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] yearsAndTimes = logistics_time_edit.getText().toString().split(" ");
                String[] years = yearsAndTimes[0].split("-");
                String[] times = yearsAndTimes[1].split(":");
                BLTimerAlert.showYearAlert(getActivity(),Integer.parseInt(years[0]),Integer.parseInt(years[1])-1,Integer.parseInt(years[2])-1,Integer.parseInt(times[0]),Integer.parseInt(times[1]) , new BLTimerAlert.OnYearAlertClick() {
                    @Override
                    public void onClick(int year, int month, int day, int hour, int min) {
                        String month_s = "";
                        String day_s = "";
                        String hour_s = "";
                        String min_s = "";
                        if (month < 9){
                            month_s = "0"+(month+1);
                        } else {
                            month_s = (month+1)+"";
                        }

                        if (day < 9){
                            day_s = "0"+(day+1);
                        } else {
                            day_s = (day+1)+"";
                        }

                        if (hour < 10){
                            hour_s = "0"+(hour);
                        } else {
                            hour_s = (hour)+"";
                        }

                        if (min < 10){
                            min_s = "0"+(min);
                        } else {
                            min_s = (min)+"";
                        }



                        logistics_time_edit.setText(year+"-"+month_s+"-"+day_s+" "+hour_s+":"+min_s+":"+"00");
                    }
                });
//                for (int i = 0 ; i < yearsAndTimes.length ; i++){
//                    System.out.println("sss:"+yearsAndTimes[i]);
//                }
            }
        });

        stock_safe_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StockSafeInstance instance = stockSafeInstances.get(position);
                goods_safe_id = instance.getId();
                safePrice = instance.getPrice();

                calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volume_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (houseName != null){
                    if (!volume_edit.getText().toString().equals("")){
                        if (houseFree > Float.parseFloat(volume_edit.getText().toString())){
//                        if (!days_edit.getText().toString().equals("")  && housePrice != Constants.convention ){
                            calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");
//                        }
                        } else {
                            Toast.makeText(getActivity(),"物品占用体积超过该仓库剩余体积,如需入库,请重新选择仓库！",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {

                    Toast.makeText(getActivity(),"您尚未选择仓库，请选择仓库后再填写占用体积",Toast.LENGTH_SHORT).show();

//                    volume_edit.setText("");

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        days_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!days_edit.getText().toString().equals("") && !volume_edit.getText().toString().equals("") && housePrice != Constants.convention){
//                    float allPrice = storagePrice()+
                    calculation_price_text.setText(userCoupon(getStoragePrice()+getMaintenancePrice()+getSafePrice(),Float.parseFloat(money_free)) +"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        coupon_check_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(calculation_price_text.getText().toString()) != 0){
                    Intent intent = new Intent(getActivity(), CouponActivity.class);
                    intent.putExtra("price",Float.parseFloat(calculation_price_text.getText().toString()));
                    startActivityForResult(intent,0);
                } else {
                    Toast.makeText(getActivity(),"请先确定价格再使用优惠券！",Toast.LENGTH_SHORT).show();
                }

            }
        });

}



    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private String getYearTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private float getStoragePrice(){
        if (housePrice != Constants.convention && !volume_edit.getText().toString().equals("") && !days_edit.getText().toString().equals("")){
            System.out.println("getStoragePrice:"+housePrice+"*"+Float.parseFloat(volume_edit.getText().toString())+"*"+Float.parseFloat(days_edit.getText().toString()));
            return housePrice * Float.parseFloat(volume_edit.getText().toString()) * Float.parseFloat(days_edit.getText().toString());
        }
        return 0;
    }

    private float getMaintenancePrice(){
        if (maintenanceAllDays != Constants.convention && maintenanceDays != Constants.convention && maintenancePrice != Constants.convention &&
                !maintenance_time_end_edit.getText().toString().equals("") && !maintenance_time_start_edit.getText().toString().equals("")){
            System.out.println("getMaintenancePrice:"+maintenanceAllDays+"*"+maintenancePrice);
            return maintenanceAllDays * maintenancePrice;
        }
        return 0;
    }

    private float getSafePrice(){
        if (safePrice != Constants.convention){
            System.out.println("getSafePrice:"+safePrice);
            return safePrice;
        }
        return 0;
    }

    private float userCoupon(float price , float couponPrice){

        float userCouponPrice = price - couponPrice;
        if (userCouponPrice > 0){
            return userCouponPrice;
        } else {
            coupon_check_edit.setText("");
            money_free = "0";
            money_total = Constants.convention;
            couponID = Constants.convention;
            Toast.makeText(getActivity(),"预算价格不满足优惠券的满减，请重新选择优惠券！",Toast.LENGTH_SHORT).show();
            return  price;
        }

    }


    private long daysReduce(String end_time , String start_time){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = df.parse(end_time);
            Date d2 = df.parse(start_time);
            long diff = d1.getTime() - d2.getTime();
            System.out.println("diff:"+d1.getTime());
            System.out.println("diff:"+d2.getTime());
            System.out.println("diff:"+diff);
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
            return Constants.convention;
        }
    }

    private void applyToHouse(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);

//        JSONArray loginPhone_Array = new JSONArray();
//        loginPhone_Array.put(loginPhone);
//
//        JSONArray province_Array = new JSONArray();
//        province_Array.put(province);
//
//        JSONArray city_Array = new JSONArray();
//        city_Array.put(city);
//
//        JSONArray stock_id_Array = new JSONArray();
//        stock_id_Array.put(stock_id);
//
//        JSONArray member_name_Array = new JSONArray();
//        member_name_Array.put(name);
//
//        JSONArray member_phone_Array = new JSONArray();
//        member_phone_Array.put(phone);
//
//        JSONArray member_address_Array = new JSONArray();
//        member_address_Array.put(address);
//
//        JSONArray logistics_time_Array = new JSONArray();
//        logistics_time_Array.put(logistics_time_edit.getText().toString());
//
//        JSONArray goods_tid_Array = new JSONArray();
//        goods_tid_Array.put(goods_tid);
//
//        JSONArray aid_Array = new JSONArray();
//        aid_Array.put(aid);
//
//        JSONArray bid_Array = new JSONArray();
//        bid_Array.put(bid);
//
//        JSONArray cid_Array = new JSONArray();
//        cid_Array.put(cid);
//
//        JSONArray contain_Array = new JSONArray();
//        contain_Array.put(volume_edit.getText().toString());
//
//        JSONArray days_Array = new JSONArray();
//        days_Array.put(days_edit.getText().toString());
//
//        JSONArray price_Array = new JSONArray();
//        price_Array.put(10);
//
//        JSONArray mark_Array = new JSONArray();
//        mark_Array.put(remarks_edit.getText().toString());
//
//        JSONArray goods_protect_rules_id_Array = new JSONArray();
//        goods_protect_rules_id_Array.put(goods_protect_rules_id);

        RequestParams requestParams = new RequestParams();
        requestParams.put("phone",loginPhone);
        requestParams.put("province",province+"");
        requestParams.put("city",city+"");
        requestParams.put("stock_id",stock_id+"");
        requestParams.put("member_name",name);
        requestParams.put("member_phone",phone);
        requestParams.put("member_address",address);
        requestParams.put("logistics_time",logistics_time_edit.getText().toString());
        requestParams.put("goods_tid",goods_tid+"");
        requestParams.put("aid",aid+"");
        requestParams.put("bid",bid+"");
        requestParams.put("cid",cid+"");
        requestParams.put("contain",volume_edit.getText().toString());
        requestParams.put("days",days_edit.getText().toString());
        requestParams.put("price",calculation_price_text.getText().toString());
        requestParams.put("mark",remarks_edit.getText().toString());
        requestParams.put("goods_protect_rules_id",goods_protect_rules_id+"");
        requestParams.put("goods_safe_id",goods_safe_id+"");
        requestParams.put("act_type",saveType+"");
        if (couponID != Constants.convention){
            requestParams.put("coupons_id",couponID+"");
        }
        if (saveType!=0){
            requestParams.put("act_price",save_type_edit.getText().toString());
        }

        System.out.println("applyToHouse:"+ Constants.MAIN_LINK+"/api/store/"+id+"/put");

        System.out.println("applyToHouse:"+requestParams.toString());

        asyncHttpClient.post(Constants.MAIN_LINK+"/api/store/"+id+"/put",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("applyToHouse:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        Toast.makeText(getActivity(),"申请入库成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),ThingsActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("applyToHouse接口异常或者网络不通:"+throwable.getMessage());
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2){
            phone = data.getExtras().getString("phone","");
            name = data.getExtras().getString("name","");
            address = data.getExtras().getString("address","");
            province = data.getExtras().getInt("province");
            city = data.getExtras().getInt("city");
            System.out.println("addTrue:"+phone+"|"+name+"|"+address);
            address_edit.setText(address);
        } else if (requestCode == 4 && resultCode == 3){
            id = data.getIntExtra("id", Constants.convention);
            houseName = data.getStringExtra("houseName");
            house_check_edit.setText(houseName);
            getStoreType();
        } else if (requestCode == 0 && resultCode == 1){
            money_free = data.getStringExtra("money_free");
            money_total = data.getFloatExtra("money_total", Constants.convention);
            couponID = data.getIntExtra("couponID", Constants.convention);
            coupon_check_edit.setText("满"+money_total+"元减"+money_free+"元");
            calculation_price_text.setText(Float.parseFloat(calculation_price_text.getText().toString()) - Float.parseFloat(money_free) +"");

        }
    }
}
