package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.MailInstance;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public class MailAdapter extends BaseAdapter {
    List<MailInstance> list;
    Context context;

    public MailAdapter(List<MailInstance> list , Context context){
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
        convertView = LayoutInflater.from(context).inflate(R.layout.mail_item,
                null);
        MailInstance mailInstance = list.get(position);
        TextView province_city_text = (TextView) convertView.findViewById(R.id.province_city_text);
        TextView address_text = (TextView) convertView.findViewById(R.id.address_text);
        TextView nameAndPhoneText = (TextView) convertView.findViewById(R.id.nameAndPhoneText);
        province_city_text.setText("浙江省杭州市");
        address_text.setText(mailInstance.getAddress());
        nameAndPhoneText.setText(mailInstance.getName()+"   "+mailInstance.getPhone());
//        pacckName.setText(""+provinceInstance.getArea_name());
        return convertView;
    }
}
