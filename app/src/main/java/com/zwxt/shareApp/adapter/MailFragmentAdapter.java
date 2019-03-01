package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.activity.ComplaintActivity;
import com.zwxt.shareApp.instance.LogisticsListInstance;
import com.zwxt.shareApp.view.BLAlert;
import com.zwxt.shareApp.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class MailFragmentAdapter extends BaseAdapter {

    private Context context;

    private List<LogisticsListInstance> list;

    private AsyncHttpClient asyncHttpClient;

    private String phone;


    public MailFragmentAdapter(List<LogisticsListInstance> list, Context context){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null){
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.stock_item,null);
            childViewHolder.stock_head = (CircleImageView) convertView.findViewById(R.id.stock_head);
            childViewHolder.code_text = (TextView) convertView.findViewById(R.id.code_text);
            childViewHolder.stock_time_text = (TextView) convertView.findViewById(R.id.stock_time_text);
            childViewHolder.stock_im = (ImageView) convertView.findViewById(R.id.stock_im);
            childViewHolder.takeOut = (Button) convertView.findViewById(R.id.takeOut);
            childViewHolder.contact = (Button) convertView.findViewById(R.id.contact);
            childViewHolder.insurance = (Button) convertView.findViewById(R.id.insurance);
            childViewHolder.thermostat = (Button) convertView.findViewById(R.id.thermostat);
            childViewHolder.maintain = (Button) convertView.findViewById(R.id.maintain);
            childViewHolder.status_bt = (Button) convertView.findViewById(R.id.status_bt);
            childViewHolder.complaint_text = (TextView) convertView.findViewById(R.id.complaint_text);
            childViewHolder.takeOut_bt2 = (Button) convertView.findViewById(R.id.takeOut_bt2);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        String image_url = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_HEAD_IMAGE_URL);
        Glide.with(context).load(image_url).error(R.drawable.t).into(childViewHolder.stock_head);

        Glide.with(context).load(R.drawable.details_bg).into(childViewHolder.stock_im);

        childViewHolder.takeOut_bt2.setVisibility(View.GONE);
        childViewHolder.contact.setVisibility(View.GONE);

        final LogisticsListInstance instance = list.get(position);

        childViewHolder.code_text.setText("订单编号: "+instance.getOid());
        if (instance.getGoods_protect_rules_id() == Constants.convention) {
            childViewHolder.maintain.setVisibility(View.GONE);
        } else {
            childViewHolder.maintain.setVisibility(View.VISIBLE);
        }

        if (instance.getStatus() == 0){
            childViewHolder.status_bt.setText("已下单");
        } else if (instance.getStatus() == 1){
            childViewHolder.status_bt.setText("已接单");
        } else if (instance.getStatus() == 2){
            childViewHolder.status_bt.setText("派送中");
        } else if (instance.getStatus() == 3){
            childViewHolder.status_bt.setText("订单完成");
        } else if (instance.getStatus() == 4){
            childViewHolder.status_bt.setText("已入库");
        }

        getStatus(instance.getOid(),childViewHolder.thermostat,childViewHolder.insurance);

        childViewHolder.takeOut.setVisibility(View.GONE);

        childViewHolder.takeOut.setText("确认价格");

        childViewHolder.takeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

                BLAlert.showAlert(context, "入库价格为"+instance.getPrice()+",是否同意该入库价格?", new BLAlert.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        switch (whichButton){
                            case 0:
                                //yse

                                userConfirm(phone,instance.getOid(),1,context);

                                break;

                            case 1:
                                //no

                                userConfirm(phone,instance.getOid(),4,context);

                                break;
                        }
                    }
                });
            }
        });

        childViewHolder.complaint_text.setVisibility(View.VISIBLE);

        childViewHolder.complaint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ComplaintActivity.class);
                intent.putExtra("state",true);
                intent.putExtra("oid",instance.getOid());
                context.startActivity(intent);
            }
        });


        return convertView;
    }


    class ChildViewHolder{
        CircleImageView stock_head;
        TextView code_text ,stock_time_text ,complaint_text;
        ImageView stock_im;
        Button takeOut, contact, insurance, thermostat, maintain ,status_bt , takeOut_bt2;
    }

    private void getStatus(String oid , final Button button , final Button button2) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
        String token = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);
        asyncHttpClient.get(Constants.MAIN_LINK + "/api/goods_logistics/" + oid + "/show", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("getStatus:"+s);

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")) {
                        JSONObject success_Object = object.optJSONObject("success");
                        String store_type_name = success_Object.optString("store_type_name");
                        String goods_safe_name = success_Object.optString("goods_safe_name");
                        if (store_type_name.equals("")){
                            button.setVisibility(View.GONE);
                        } else {
                            button.setText(store_type_name);
                            button.setVisibility(View.VISIBLE);
                        }
                        button.setText(store_type_name);
                        if (goods_safe_name.equals("")){
                            button2.setVisibility(View.GONE);
                        } else {
                            button2.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("getStatus接口不通或网络异常："+throwable.getMessage());
            }

        });
    }


    private void userConfirm(String phone , String oid , int status , final Context context){
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }

        String token = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);

        RequestParams requestParams = new RequestParams();
        requestParams.put("phone",phone);
        requestParams.put("oid",oid);
        requestParams.put("status",status+"");
        System.out.println("userConfirm:"+requestParams.toString());
        asyncHttpClient.post(Constants.MAIN_LINK+"/api/goods_stored/userConfirm",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("userConfirm:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    if (code.equals("0")){
                        Intent intent = new Intent();
                        intent.setAction(Constants.STOCK_STATUS_CHANGE);
                        context.sendBroadcast(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("userConfirm接口不通或网络异常:"+throwable.getMessage());
            }

        });

    }

}
