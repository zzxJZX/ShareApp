package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.ProvinceInstance;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public class PackAdapter extends BaseAdapter {
    List<ProvinceInstance> list;
    Context context;

    public PackAdapter(List<ProvinceInstance> list , Context context){
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
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item,
                null);
        ProvinceInstance provinceInstance = list.get(position);
        TextView pacckName = (TextView) convertView.findViewById(R.id.spinnerName);
        pacckName.setText(""+provinceInstance.getArea_name());
        return convertView;
    }
}
