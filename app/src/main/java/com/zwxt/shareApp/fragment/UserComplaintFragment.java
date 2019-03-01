package com.zwxt.shareApp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.activity.ThingsActivity;
import com.zwxt.shareApp.view.BLAlert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/9/3.
 */

public class UserComplaintFragment extends Fragment {

    private TextView choose_complaint;

    private EditText complaint_text_edit , choose_oid;

    private Button complaint_sub_bt;

    private AsyncHttpClient asyncHttpClient;

    private String phone;

    private String token;

    private String oid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_complaint_layout,container,false);
        phone = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(getActivity(), Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        oid = getActivity().getIntent().getStringExtra("oid");

        init(view);

        return view;
    }

    private void init(View view){
        choose_complaint = (TextView) view.findViewById(R.id.choose_complaint);
        choose_oid = (EditText) view.findViewById(R.id.choose_oid);
        complaint_text_edit = (EditText) view.findViewById(R.id.complaint_text_edit);
        complaint_sub_bt = (Button) view.findViewById(R.id.complaint_sub_bt);

        choose_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BLAlert.showChooseAlert(getActivity(), new BLAlert.OnChooseId() {
                    @Override
                    public void onClick(int choose) {
                        System.out.println("User:"+choose);
                    }
                });
            }
        });

        choose_oid.setText(oid);

        choose_oid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThingsActivity.class);
                intent.putExtra("state",true);
                startActivityForResult(intent,1);

            }
        });

        complaint_sub_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaint_text_edit.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请填写申诉的详细内容,以便更快处理！",Toast.LENGTH_SHORT).show();
                } else {
                    launchComplain(oid,complaint_text_edit.getText().toString());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2){
            oid = data.getStringExtra("oid");
            choose_oid.setText(oid);
        }
    }

    private void launchComplain(String oid , final String title){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone",phone);
        requestParams.put("oid",oid);
        requestParams.put("title",title);
        System.out.println("launchComplain:"+requestParams.toString());
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
                        getActivity().finish();
                    }
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
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

}
