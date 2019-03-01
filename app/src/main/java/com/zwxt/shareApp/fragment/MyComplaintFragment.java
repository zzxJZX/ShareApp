package com.zwxt.shareApp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.ComplainListAdapter;
import com.zwxt.shareApp.instance.ComplainListInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */

public class MyComplaintFragment extends Fragment{

    private List<ComplainListInstance> listInstances;

    private ListView complaint_list;

    private ComplainListAdapter adapter;

    private AsyncHttpClient asyncHttpClient;

    private String token;

    private String phone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_complaint_layout,container,false);
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        phone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

        init(view);

        complainList();

        return view;
    }

    private void init(View view){
        listInstances = new ArrayList<>();
        complaint_list = (ListView) view.findViewById(R.id.complaint_list);
        adapter = new ComplainListAdapter(listInstances,getActivity());
        complaint_list.setAdapter(adapter);
    }


    private void complainList(){

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

                            adapter.notifyDataSetChanged();

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

}
