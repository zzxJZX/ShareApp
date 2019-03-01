package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.CouponsListInstance;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/9/4.
 */

public class CouponListAdapter extends BaseAdapter {

    private List<CouponsListInstance> listInstances;

    private Context context;

    private float price;

    public CouponListAdapter(List<CouponsListInstance> listInstances , Context context , float price){
        this.context = context;
        this.listInstances = listInstances;
        this.price = price;
    }

    @Override
    public int getCount() {
        return listInstances.size();
    }

    @Override
    public Object getItem(int position) {
        return listInstances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.coupon_item,
                null);

        TextView money_free_text = (TextView) convertView.findViewById(R.id.money_free_text);
        TextView money_total_text = (TextView) convertView.findViewById(R.id.money_total_text);
        TextView time_end_text = (TextView) convertView.findViewById(R.id.time_end_text);
        TextView remaining_time_text = (TextView) convertView.findViewById(R.id.remaining_time_text);
        Button user_bt = (Button) convertView.findViewById(R.id.user_bt);
        CouponsListInstance instance = listInstances.get(position);

        money_free_text.setText(instance.getMoney_free()+"¥");
        money_total_text.setText("满"+instance.getMoney_total()+"元可用");
        String time = instance.getTime_end().split(" ")[0];
        time_end_text.setText(time+"到期");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (instance.getTime_start() != null && !instance.getTime_start().equals("null") && instance.getTime_end() != null && !instance.getTime_end().equals("null")) {
                Date d1 = df.parse(instance.getTime_end().split(" ")[0]);
                Date d2 = df.parse(instance.getTime_start().split(" ")[0]);
                long diff = d1.getTime() - d2.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                System.out.println("days:" + days);
                remaining_time_text.setText("剩" + (days) + "天");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (price < instance.getMoney_total()){
            user_bt.setEnabled(false);
            user_bt.setBackgroundColor(Color.GRAY);
            user_bt.setText("未满满减");
             if (price == Constants.convention){
                 user_bt.setVisibility(View.INVISIBLE);
            }
        }

        user_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Constants.USER_COUPON);
                intent.putExtra("position",position);
                context.sendBroadcast(intent);
            }
        });


        return convertView;
    }
}
