package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zwxt.shareApp.CallBack;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.CouponsShowInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/9/4.
 */

public class DetailsCouponLIstAdapter extends BaseAdapter {

    private List<CouponsShowInstance> list;

    private Context context;

    private AsyncHttpClient asyncHttpClient;

    private String phone;

    private String token;

    private CallBack callBack;

    public DetailsCouponLIstAdapter(List<CouponsShowInstance> list , Context context , CallBack callBack){
        this.list = list;
        this.context = context;
        this.callBack = callBack;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.coupon_item,
                null);
        TextView money_free_text = (TextView) convertView.findViewById(R.id.money_free_text);
        TextView money_total_text = (TextView) convertView.findViewById(R.id.money_total_text);
        TextView time_end_text = (TextView) convertView.findViewById(R.id.time_end_text);
        TextView remaining_time_text = (TextView) convertView.findViewById(R.id.remaining_time_text);
        Button user_bt = (Button) convertView.findViewById(R.id.user_bt);
        time_end_text.setVisibility(View.GONE);
        remaining_time_text.setVisibility(View.GONE);
        final CouponsShowInstance instance = list.get(position);

        money_free_text.setText(instance.getFree()+"¥");
        money_total_text.setText("满"+instance.getTotal()+"元可用");

        user_bt.setText("领取");

        user_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(true);
                getCoupon(instance.getActive_id(),instance.getTotal(),context , instance.getFree());
            }
        });

        return convertView;
    }

    private void getCoupon(int active_id , final String total , final Context context , String free){
        phone = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);
        token = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        if (asyncHttpClient == null){
            asyncHttpClient = new AsyncHttpClient();
        }
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization","Bearer "+token);

        RequestParams requestParams = new RequestParams();
        requestParams.put("active_id",active_id+"");
        requestParams.put("phone",phone);
        requestParams.put("total",total);
        requestParams.put("free",free+"");

        System.out.println("getCoupon:"+requestParams.toString());

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
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setAction(Constants.GET_COUPON);
                    context.sendBroadcast(intent);

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
