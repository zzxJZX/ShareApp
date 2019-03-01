package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.BLPullToRefreshView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.ShareApplication;
import com.zwxt.shareApp.adapter.RecommendAdapter;
import com.zwxt.shareApp.instance.RecommendInstance;
import com.zwxt.shareApp.view.BLTimerAlert;
import com.zwxt.shareApp.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class RecommendActivity extends Activity {

    private EditText recommend_edit;
    private ListView recommend_list;
    private Button recommend_back_bt;
    private TextView search_bt;
    private String token ;
    private AsyncHttpClient asyncHttpClient;
    private BLPullToRefreshView mPullRefreshScrollView;
    private int lastPage = 0;
    private List<RecommendInstance> list;
    private boolean Refresh = false;
    private RecommendAdapter recommendAdapter;
    private int page = 1;
    private boolean footerLoad = false;
    private MyProgressDialog myProgressDialog;
    private boolean chooseHouse = false;
    private boolean search = false;
    private EditText recommend_start_time , recommend_end_time;
    private String homeSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_layout);
        homeSearch = getIntent().getStringExtra("homeSearch");
        asyncHttpClient = new AsyncHttpClient();
        token = Information.getStringConfig(RecommendActivity.this, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        System.out.println("token:"+token);
        chooseHouse = getIntent().getBooleanExtra("chooseHouse",false);
        setTitleHeight();
        init();
        initData();
        getList();
    }

    private void getList(){

        myProgressDialog = MyProgressDialog.createDialog(RecommendActivity.this);
        if (search){
            myProgressDialog.setMessage("数据搜索中....");
        } else {
            myProgressDialog.setMessage("数据加载中....");
        }
        myProgressDialog.show();

        if (asyncHttpClient==null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        System.out.println("Link:"+ Constants.MAIN_LINK+"/api/store/list?city="+ ShareApplication.cityId+"&store_name="+recommend_edit.getText().toString()+"&time_start="+recommend_start_time.getText().toString()+"&time_end="+recommend_end_time.getText().toString()+"&page="+page);
        asyncHttpClient.get(Constants.MAIN_LINK+"/api/store/list?city="+ ShareApplication.cityId+"&store_name="+recommend_edit.getText().toString()+"&time_start="+recommend_start_time.getText().toString()+"&time_end="+recommend_end_time.getText().toString()+"&page="+page,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getList:"+s);
                if (Refresh || search){
                    list.clear();
                }
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        JSONObject success_object = object.optJSONObject("success");
                        if (success_object != null){
                            JSONObject page_object = success_object.optJSONObject("page");
                            if (page_object != null){
                                lastPage = page_object.optInt("lastPage",0);
                            }
                            JSONArray list_Array = success_object.optJSONArray("list");
                            if (list_Array != null && list_Array.length() >0){
                                for (int i = 0 ; i < list_Array.length() ; i++){
                                    JSONObject list_Object = list_Array.optJSONObject(i);
                                    int id = list_Object.optInt("id",6899);
                                    String store_name = list_Object.optString("store_name");
                                    int province = list_Object.optInt("province",6899);
                                    int city = list_Object.optInt("city",6899);
                                    String admin_phone = list_Object.optString("admin_phone");
                                    String position = list_Object.optString("position");
                                    String address = list_Object.optString("address");
                                    String phone = list_Object.optString("phone");
                                    String image = list_Object.optString("image");

                                    System.out.println("image:"+image);
                                    RecommendInstance recommendInstance = new RecommendInstance(id,store_name,province,city,admin_phone,position,address,phone,image);
                                    list.add(recommendInstance);
                                }
                                if (!search && page <= lastPage){
                                    page ++;
                                }
                            }
                        }
                        recommendAdapter.notifyDataSetChanged();
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

                myProgressDialog.dismiss();

            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("List接口访问不通或者网络不通");
                myProgressDialog.dismiss();
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
                        Toast.makeText(RecommendActivity.this,"已经到底啦！",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecommendActivity.this,"加载完成",Toast.LENGTH_SHORT).show();
                    }
                    footerLoad = false;
                    Refresh = false;
                    break;

                case 1:
                    mPullRefreshScrollView.onHeaderRefreshFinish();
                    Toast.makeText(RecommendActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();
                    Refresh = false;
                    footerLoad = false;
                    break;
            }
        }
    }

    private void init(){
        search_bt = (TextView) findViewById(R.id.search_bt);
        recommend_start_time = (EditText) findViewById(R.id.recommend_start_time);
        recommend_end_time = (EditText) findViewById(R.id.recommend_end_time);
        recommend_edit = (EditText) findViewById(R.id.recommend_edit);
        recommend_list = (ListView) findViewById(R.id.recommend_list);
        recommend_back_bt = (Button) findViewById(R.id.recommend_back_bt);
        mPullRefreshScrollView = (BLPullToRefreshView) findViewById(R.id.pull_refresh_scrollview);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                search = true;
                getList();
            }
        });
        recommend_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recommend_start_time.getText().toString().equals("")){
                    Calendar calendar=Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);

                    int month = calendar.get(Calendar.MONTH);

                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    BLTimerAlert.showDateAlert(RecommendActivity.this, year-1, month, day, new BLTimerAlert.OnDateAlertClick() {
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

                            recommend_start_time.setText(year+"-"+month_s+"-"+day_s);

                        }
                    });
                } else {
                    String[] years = recommend_start_time.getText().toString().split("-");
                    String year = years[0];
                    String month = years[1];
                    String day = years[2];


                    BLTimerAlert.showDateAlert(RecommendActivity.this, Integer.valueOf(year)-1, Integer.valueOf(month)-1, Integer.valueOf(day)-1, new BLTimerAlert.OnDateAlertClick() {
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

                            recommend_start_time.setText(year+"-"+month_s+"-"+day_s);

                        }
                    });
                }

            }
        });

        recommend_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recommend_end_time.getText().toString().equals("")){
                    Calendar calendar=Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);

                    int month = calendar.get(Calendar.MONTH);

                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    BLTimerAlert.showDateAlert(RecommendActivity.this, year-1, month, day, new BLTimerAlert.OnDateAlertClick() {
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

                            recommend_end_time.setText(year+"-"+month_s+"-"+day_s);

                        }
                    });
                } else {
                    String[] years = recommend_end_time.getText().toString().split("-");
                    String year = years[0];
                    String month = years[1];
                    String day = years[2];


                    BLTimerAlert.showDateAlert(RecommendActivity.this, Integer.valueOf(year)-1, Integer.valueOf(month)-1, Integer.valueOf(day)-1, new BLTimerAlert.OnDateAlertClick() {
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

                            recommend_end_time.setText(year+"-"+month_s+"-"+day_s);

                        }
                    });
                }

            }
        });

        if (homeSearch != null && !homeSearch.equals("")){
            recommend_edit.setText(homeSearch);
        }

        recommend_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    page = 1;
                    search = true;
                    getList();
//                    Toast.makeText(RecommendActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("点击");
                Intent intent = new Intent(RecommendActivity.this,DetailsActivity.class);
                intent.putExtra("houseName",list.get(position).getStore_name());
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("chooseHouse", chooseHouse);
                startActivityForResult(intent,0);

            }
        });
        recommend_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPullRefreshScrollView.setOnHeaderRefreshListener(new BLPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(BLPullToRefreshView view) {
                //刷新
                Refresh = true;
                footerLoad = false;
                page = 1;
                recommend_edit.setText("");
                getList();
            }
        });

        mPullRefreshScrollView.setOnFooterLoadListener(new BLPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(BLPullToRefreshView view) {
                //加载
                Refresh = false;
                footerLoad = true;
                getList();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1){
            int id = data.getIntExtra("id", Constants.convention);
            String houseName = data.getStringExtra("houseName");
            boolean active = data.getBooleanExtra("active",false);
            Intent intent = new Intent();
            intent.putExtra("active",false);
            intent.putExtra("id",id);
            intent.putExtra("houseName",houseName);
            setResult(3,intent);
            finish();
        }
    }

    private void initData(){
//        List<Map<String , Object>> list = new ArrayList<>();
//        for (int i = 0 ; i < 5 ; i++){
//            Map<String,Object> map = new HashMap<>();
//            map.put("i",i);
//            list.add(map);
//        }
        list = new ArrayList<>();
        recommendAdapter = new RecommendAdapter(list,RecommendActivity.this);
        recommend_list.setAdapter(recommendAdapter);

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
        View view = findViewById(R.id.recommend_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }
}
