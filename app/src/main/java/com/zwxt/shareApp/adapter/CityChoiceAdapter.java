package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.CityInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 */

public class CityChoiceAdapter extends BaseAdapter {

    private List<CityInstance> list;

    private Context context;

    public CityChoiceAdapter(List<CityInstance> list , Context context){
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
        convertView = LayoutInflater.from(context).inflate(R.layout.city_item,
                null);
        CityInstance cityInstance = list.get(position);

        TextView city_name_text = (TextView) convertView.findViewById(R.id.city_name_text);

        city_name_text.setText(cityInstance.getArea_name());

        return convertView;
    }
}
