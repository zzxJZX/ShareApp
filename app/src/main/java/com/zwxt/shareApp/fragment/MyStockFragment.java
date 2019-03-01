package com.zwxt.shareApp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.BLPullToRefreshView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.activity.OrderDetailsActivity;
import com.zwxt.shareApp.adapter.StockAdapter;
import com.zwxt.shareApp.instance.MailFragmentInstance;
import com.zwxt.shareApp.view.RecommendListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class MyStockFragment extends Fragment {

    private RecommendListView stock_list;

    private AsyncHttpClient asyncHttpClient;

    private String token;

    private String phone;

    private List<MailFragmentInstance> listInstances;

    private StockAdapter stockAdapter;

    private BLPullToRefreshView mPullRefreshScrollView;
    private boolean Refresh = false;
    private int page = 1;
    private boolean footerLoad = false;
    private int lastPage = Constants.convention;

    private boolean state = false;

    private MailBroadCastReceiver broadCastReceiver = new MailBroadCastReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_layout,container,false);
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        phone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        state = getActivity().getIntent().getBooleanExtra("state",false);
        asyncHttpClient = new AsyncHttpClient();
        init(view);
        goodStoredList();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MAIL_STATUS_CHANGE);
        getActivity().registerReceiver(broadCastReceiver, intentFilter);

        return view;
    }

    class MailBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.MAIL_STATUS_CHANGE)){
//                logisticsList();
                page = 1;
                goodStoredList();
                Refresh = true;
            }
        }
    }

    private void goodStoredList(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("phone","18767117520");
//        requestParams.put("page","1");
//        requestParams.put("status","");
        System.out.println("goodStoredList:"+ Constants.MAIN_LINK+"/api/goods_stored/list?phone="+phone+"&page="+page+"&status=");
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods_stored/list?phone="+phone+"&page="+page+"&status=",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("goodStoredList:"+s);

                if (Refresh){
                    listInstances.clear();
                }

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_Object = object.optJSONObject("success");
                        lastPage = success_Object.optInt("last_page", Constants.convention);
                        System.out.println("lastPage:"+lastPage+"|"+page);
                        if (page <= lastPage){
                            page ++ ;

                            JSONArray data_Array = success_Object.optJSONArray("data");
                            if (data_Array != null && data_Array.length() > 0){
                                for (int i =0 ; i < data_Array.length() ; i++){
                                    JSONObject data_Object = data_Array.optJSONObject(i);
                                    int active_fee = data_Object.optInt("active_fee", Constants.convention);
                                    int active_id = data_Object.optInt("active_id", Constants.convention);
                                    int aid = data_Object.optInt("aid", Constants.convention);
                                    int bid = data_Object.optInt("bid", Constants.convention);
                                    int cid = data_Object.optInt("cid", Constants.convention);
                                    int city = data_Object.optInt("city", Constants.convention);
                                    int contain = data_Object.optInt("contain", Constants.convention);
                                    int days = data_Object.optInt("days", Constants.convention);
                                    int goods_logistics_id = data_Object.optInt("goods_logistics_id", Constants.convention);
                                    String goods_protect_end_time = data_Object.optString("goods_protect_end_time");
                                    int goods_protect_rules_id = data_Object.optInt("goods_protect_rules_id", Constants.convention);
                                    String goods_protect_start_time = data_Object.optString("goods_protect_start_time");
                                    String created_at = data_Object.optString("created_at");
                                    String updated_at = data_Object.optString("updated_at");
                                    int goods_safe_id = data_Object.optInt("goods_safe_id", Constants.convention);
                                    int goods_tid = data_Object.optInt("goods_tid", Constants.convention);
                                    int id = data_Object.optInt("id", Constants.convention);
                                    String image = data_Object.optString("image");
                                    String mark = data_Object.optString("mark");
                                    String member_address = data_Object.optString("member_address");
                                    String member_name = data_Object.optString("member_name");
                                    String member_phone = data_Object.optString("member_phone");
                                    String oid = data_Object.optString("oid");
                                    float price = Float.parseFloat(data_Object.optString("price","0"));
                                    int province = data_Object.optInt("province");
                                    int status = data_Object.optInt("status");
                                    int stock_id = data_Object.optInt("stock_id");
                                    int store_id = data_Object.optInt("store_id");
                                    MailFragmentInstance mailFragmentInstance = new MailFragmentInstance(active_fee,active_id,aid,bid,cid,city,contain,days,goods_logistics_id,goods_protect_end_time,
                                            goods_protect_rules_id,goods_protect_start_time,goods_safe_id,goods_tid,id,image,mark,member_address,member_name,member_phone,oid
                                            ,price,province,status,stock_id,store_id,created_at,updated_at);
                                    listInstances.add(mailFragmentInstance);

                                }

                            }

                        }

                        stockAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (footerLoad){
                    Message message = new Message();
                    message.what = 0;
                    mPullRefrshHander.sendMessage(message);
                } else if (Refresh){
                    Message message = new Message();
                    message.what = 1;
                    mPullRefrshHander.sendMessage(message);
                }

            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("goodStoredList接口不通或者网络不通:"+throwable.toString());

                if (footerLoad){
                    Message message = new Message();
                    message.what = 0;
                    mPullRefrshHander.sendMessage(message);
                } else if (Refresh){
                    Message message = new Message();
                    message.what = 1;
                    mPullRefrshHander.sendMessage(message);
                }

            }

        });

    }


    private PullRefrshHander mPullRefrshHander = new PullRefrshHander(Looper.myLooper());
    class PullRefrshHander extends Handler {

        public PullRefrshHander(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mPullRefreshScrollView.onFooterLoadFinish();
                    if (page > lastPage){
                        Toast.makeText(getActivity(),"已经到底啦！",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),"加载完成",Toast.LENGTH_SHORT).show();
                    }
                    footerLoad = false;
                    Refresh = false;
                    break;

                case 1:
                    mPullRefreshScrollView.onHeaderRefreshFinish();
                    Toast.makeText(getActivity(),"刷新完成",Toast.LENGTH_SHORT).show();
                    Refresh = false;
                    footerLoad = false;
                    break;
            }
        }
    }

    private void init(View view){
        stock_list = (RecommendListView) view.findViewById(R.id.stock_list);
        mPullRefreshScrollView = (BLPullToRefreshView) view.findViewById(R.id.pull_refresh_scrollview);
        listInstances = new ArrayList<>();
        stockAdapter = new StockAdapter(listInstances,getActivity());
        stock_list.setAdapter(stockAdapter);

        mPullRefreshScrollView.setOnHeaderRefreshListener(new BLPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(BLPullToRefreshView view) {
                //刷新
                Refresh = true;
                footerLoad = false;
                page = 1;
//                getList();
                goodStoredList();
            }
        });

        mPullRefreshScrollView.setOnFooterLoadListener(new BLPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(BLPullToRefreshView view) {
                //加载
                Refresh = false;
                footerLoad = true;
//                getList();
                goodStoredList();
            }
        });

        stock_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (state){
                    Intent intent = new Intent();
                    intent.putExtra("oid",listInstances.get(position).getOid());
                    getActivity().setResult(2,intent);

                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
                    intent.putExtra("details",true);
                    intent.putExtra("oid",listInstances.get(position).getOid());
                    startActivity(intent);
                }
            }
        });

    }

}
