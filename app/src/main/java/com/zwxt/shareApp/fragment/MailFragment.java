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
import com.zwxt.shareApp.adapter.MailFragmentAdapter;
import com.zwxt.shareApp.instance.LogisticsListInstance;
import com.zwxt.shareApp.view.RecommendListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class MailFragment extends Fragment {

    private RecommendListView stock_list;

    private AsyncHttpClient asyncHttpClient;

    private String token;

    private String phone;

    private List<LogisticsListInstance> listInstances;

    private MailFragmentAdapter mailFragmentAdapter;

    private BLPullToRefreshView mPullRefreshScrollView;

    private boolean Refresh = false;
    private int page = 1;
    private boolean footerLoad = false;
    private int lastPage = 0;

    private boolean state = false;

    private StockBroadCastReceiver broadCastReceiver = new StockBroadCastReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_layout,container,false);
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        phone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        state = getActivity().getIntent().getBooleanExtra("state",false);
        asyncHttpClient = new AsyncHttpClient();
        init(view);
        logisticsList();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.STOCK_STATUS_CHANGE);
        getActivity().registerReceiver(broadCastReceiver, intentFilter);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadCastReceiver);
    }

    class StockBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.STOCK_STATUS_CHANGE)){
                logisticsList();
            }
        }
    }

    private void logisticsList(){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/goods_logistics/list?phone="+phone+"&status=",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("logisticsList:"+s);

//                if (Refresh){
                    listInstances.clear();
//                }

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONArray success_Array = object.optJSONArray("success");
                        if (success_Array != null && success_Array.length() > 0){
                            for (int i =0 ; i < success_Array.length() ; i++){
                                JSONObject success_Object = success_Array.optJSONObject(i);
                                int aid = success_Object.optInt("aid", Constants.convention);
                                int bid = success_Object.optInt("bid", Constants.convention);
                                int cancel_status = success_Object.optInt("cancel_status", Constants.convention);
                                int cid = success_Object.optInt("cid", Constants.convention);
                                int city = success_Object.optInt("city", Constants.convention);
                                int contain = success_Object.optInt("contain", Constants.convention);
                                String courier_name = success_Object.optString("courier_name");
                                String courier_phone = success_Object.optString("courier_phone");
                                int days = success_Object.optInt("days", Constants.convention);
                                int goods_protect_rules_id = success_Object.optInt("goods_protect_rules_id", Constants.convention);
                                int goods_tid = success_Object.optInt("goods_tid", Constants.convention);
                                int id = success_Object.optInt("id", Constants.convention);
                                String logistics_id = success_Object.optString("logistics_id");
                                String mark = success_Object.optString("mark");
                                String member_address = success_Object.optString("member_address");
                                String member_name = success_Object.optString("member_name");
                                String member_phone = success_Object.optString("member_phone");
                                String oid = success_Object.optString("oid");
                                String price = success_Object.optString("price");
                                int province = success_Object.optInt("province", Constants.convention);
                                int status = success_Object.optInt("status", Constants.convention);
                                int stock_id = success_Object.optInt("stock_id", Constants.convention);
                                int store_id = success_Object.optInt("store_id", Constants.convention);
                                if (status != 4){
                                    LogisticsListInstance logisticsListInstance = new LogisticsListInstance(aid,bid,cancel_status,cid,city,contain,courier_name,courier_phone,days,goods_protect_rules_id,goods_tid
                                            ,id,logistics_id,mark,member_address,member_name,member_phone,oid,price,province,status,stock_id,store_id);
                                    listInstances.add(logisticsListInstance);
                                }
                            }

                        }

                        mailFragmentAdapter.notifyDataSetChanged();
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
                System.out.println("logisticsList接口不通或者网络不通:"+throwable.toString());

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
        mailFragmentAdapter = new MailFragmentAdapter(listInstances,getActivity());
        stock_list.setAdapter(mailFragmentAdapter);


        mPullRefreshScrollView.setOnHeaderRefreshListener(new BLPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(BLPullToRefreshView view) {
                //刷新
                Refresh = true;
                footerLoad = false;
                page = 1;
//                getList();
                logisticsList();
            }
        });

        mPullRefreshScrollView.setOnFooterLoadListener(new BLPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(BLPullToRefreshView view) {
                //加载
                Refresh = false;
                footerLoad = true;
//                getList();
                logisticsList();
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
                    intent.putExtra("details",false);
                    intent.putExtra("oid",listInstances.get(position).getOid());
                    startActivity(intent);
                }
            }
        });

    }

}
