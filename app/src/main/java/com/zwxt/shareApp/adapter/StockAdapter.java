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
import com.zwxt.shareApp.activity.PayActivity;
import com.zwxt.shareApp.activity.RenewalsActivity;
import com.zwxt.shareApp.instance.MailFragmentInstance;
import com.zwxt.shareApp.view.BLAlert;
import com.zwxt.shareApp.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 */

public class StockAdapter extends BaseAdapter {

    private List<MailFragmentInstance> list;
    private Context context;

    private AsyncHttpClient asyncHttpClient;

    private String phone;


    public StockAdapter(List<MailFragmentInstance> list, Context context) {
        this.list = list;
        this.context = context;
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
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.stock_item, null);
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
            childViewHolder.takeOut_bt2 = (Button) convertView.findViewById(R.id.takeOut_bt2);
            childViewHolder.complaint_text = (TextView) convertView.findViewById(R.id.complaint_text);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        String image_url = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_HEAD_IMAGE_URL);
        Glide.with(context).load(image_url).error(R.drawable.t).into(childViewHolder.stock_head);

        final MailFragmentInstance instance = list.get(position);

        String image = instance.getImage();

        System.out.println(Constants.MAIN_LINK+image);

        Glide.with(context).load(Constants.MAIN_LINK+image).error(R.drawable.details_bg).into(childViewHolder.stock_im);

        childViewHolder.code_text.setText("订单编号:" + instance.getOid());

        System.out.println("good_id:" + instance.getGoods_protect_rules_id());

        if (instance.getGoods_protect_rules_id() == Constants.convention) {
            childViewHolder.maintain.setVisibility(View.GONE);
            System.out.println("INVISIBLE");
        } else {
            childViewHolder.maintain.setVisibility(View.VISIBLE);
        }

//        System.out.println("image:"+instance.getImage()+"|"+position);
        if (!instance.getImage().equals("")) {
            Glide.with(context).load(Constants.MAIN_LINK + instance.getImage()).into(childViewHolder.stock_im);
        }
//        childViewHolder.takeOut.setVisibility(View.GONE);

        if (instance.getStatus() == 0) {
            childViewHolder.status_bt.setText("等待入库");
            childViewHolder.takeOut.setVisibility(View.VISIBLE);
            childViewHolder.takeOut.setText("同意入库");
            childViewHolder.takeOut_bt2.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setText("取消订单");
        } else if (instance.getStatus() == 1) {
            childViewHolder.status_bt.setText("已入库");
            childViewHolder.takeOut.setVisibility(View.GONE);
            childViewHolder.takeOut_bt2.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setText("申请提前出库");
        } else if (instance.getStatus() == 2) {
            childViewHolder.status_bt.setText("已出库");
            childViewHolder.takeOut.setVisibility(View.GONE);
            childViewHolder.takeOut_bt2.setVisibility(View.GONE);
//            childViewHolder.takeOut.setVisibility(View.INVISIBLE);
        } else if (instance.getStatus() == 3) {
            childViewHolder.status_bt.setText("申请提前出库中");
            childViewHolder.takeOut.setVisibility(View.GONE);
            childViewHolder.takeOut_bt2.setVisibility(View.GONE);
//            childViewHolder.takeOut.setVisibility(View.INVISIBLE);
        } else if (instance.getStatus() == 4) {
            childViewHolder.status_bt.setText("已同意出库");
            childViewHolder.takeOut.setVisibility(View.GONE);
            childViewHolder.takeOut_bt2.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setText("支付订单");
//            childViewHolder.takeOut.setVisibility(View.INVISIBLE);
        } else if (instance.getStatus() == 5) {
            childViewHolder.status_bt.setText("不同意出库");
//            childViewHolder.takeOut.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setText("申请提前出库");
        } else if (instance.getStatus() == 6){
            childViewHolder.status_bt.setText("待出库");
            childViewHolder.takeOut_bt2.setText("订单已支付");
            childViewHolder.status_bt.setVisibility(View.VISIBLE);
            childViewHolder.takeOut_bt2.setVisibility(View.VISIBLE);
        }

        getStatus(instance.getOid(),childViewHolder.thermostat,childViewHolder.insurance);
        final String star_time = instance.getGoods_protect_start_time();

        final String end_time = instance.getGoods_protect_end_time();

        final String updated_at = instance.getUpdated_at();

        final String create_at = instance.getCreated_at();

        int day = instance.getDays();

        childViewHolder.takeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

                if (childViewHolder.takeOut.getText().toString().equals("同意入库")){
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
            }
        });


        childViewHolder.takeOut_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.SHARE_LOGIN_ACCOUNT);

                if (childViewHolder.takeOut_bt2.getText().toString().equals("申请提前出库")){
                    BLAlert.showAlert(context, "是否申请提前出库?", new BLAlert.OnAlertSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            switch (whichButton) {
                                case 0:
                                    //yse

                                    userConfirm(phone, instance.getOid(), 3, context);

                                    break;
                            }
                        }
                    });
                } else if(childViewHolder.takeOut_bt2.getText().toString().equals("取消订单")){
                    BLAlert.showAlert(context, "是否取消订单?", new BLAlert.OnAlertSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            switch (whichButton) {
                                case 0:
                                    //yse

                                    userConfirm(phone, instance.getOid(), 7, context);

                                    break;
                            }
                        }
                    });
                } else if (childViewHolder.takeOut_bt2.getText().toString().equals("支付订单")){
                    Intent intent = new Intent(context, PayActivity.class);
                    intent.putExtra("price",instance.getPrice());
                    intent.putExtra("oid",instance.getOid());
                    context.startActivity(intent);
                }


            }
        });

//        childViewHolder.takeOut.setText("续费");

//        childViewHolder.takeOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RenewalsActivity.class);
//                intent.putExtra("id", instance.getId());
//                intent.putExtra("oid", instance.getOid());
//                System.out.println("oid:" + instance.getOid());
//                intent.putExtra("safe_id", instance.getGoods_safe_id());
//                intent.putExtra("days", instance.getDays());
//                intent.putExtra("updated_at", updated_at);
//                intent.putExtra("goods_protect_rules_id", instance.getGoods_protect_rules_id());
//                intent.putExtra("star_time", star_time);
//                intent.putExtra("end_time", end_time);
//                intent.putExtra("price", instance.getPrice());
//                intent.putExtra("store_id", instance.getStore_id());
//                context.startActivity(intent);
//            }
//        });

        childViewHolder.complaint_text.setVisibility(View.VISIBLE);

        childViewHolder.complaint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplaintActivity.class);
                intent.putExtra("state",true);
                intent.putExtra("oid",instance.getOid());
                context.startActivity(intent);
            }
        });

//        String [] times = end_time.split("-");

//        int year = Integer.parseInt(times[0]);
//
//        int month = Integer.parseInt(times[1]);
//
//        int day = Integer.parseInt(times[2]);

//        String [] nowTimes = getTime().split("-");

//        int nowYear = Integer.parseInt(nowTimes[0]);
//
//        int nowMonth = Integer.parseInt(nowTimes[1]);
//
//        int nowDay = Integer.parseInt(nowTimes[2]);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (updated_at != null && !updated_at.equals("null")) {
                Date d1 = df.parse(getTime());
                Date d2 = df.parse(create_at);
                long diff = d1.getTime() - d2.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                System.out.println("days:" + days);
                childViewHolder.stock_time_text.setVisibility(View.VISIBLE);
                childViewHolder.stock_time_text.setText("剩余时间：" + (day - days) + "天");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    private void getStatus(String oid , final Button button , final Button button2) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
        String token = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);
        asyncHttpClient.get(Constants.MAIN_LINK + "/api/goods_stored/" + oid + "/show", new AsyncHttpResponseHandler() {

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


    private void userConfirm(String phone, String oid, int status, final Context context) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
        String token = Information.getStringConfig(context, Information.SHARE_LOGIN, Information.ACCOUNT_TOKEN);
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.addHeader("Authorization", "Bearer " + token);

        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("oid", oid);
        requestParams.put("status", status + "");
        System.out.println("userConfirm:" + requestParams.toString());
        asyncHttpClient.post(Constants.MAIN_LINK + "/api/goods_stored/userConfirm", requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("userConfirm:" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String message = object.optString("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    if (code.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction(Constants.MAIL_STATUS_CHANGE);
                        context.sendBroadcast(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("userConfirm接口不通或网络异常:" + throwable.getMessage());
            }

        });

    }


    class ChildViewHolder {
        CircleImageView stock_head;
        TextView code_text, stock_time_text , complaint_text;
        ImageView stock_im;
        Button takeOut, contact, insurance, thermostat, maintain, status_bt, takeOut_bt2;
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
