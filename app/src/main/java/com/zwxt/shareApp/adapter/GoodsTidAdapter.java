package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.StoreTypeInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/23.
 */

public class GoodsTidAdapter extends BaseAdapter {
    private List<StoreTypeInstance> list;
    private Context context;

    public GoodsTidAdapter(List<StoreTypeInstance> list , Context context){
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
        StoreTypeInstance storeTypeInstance = list.get(position);
        TextView pacckName = (TextView) convertView.findViewById(R.id.spinnerName);
        pacckName.setText(""+storeTypeInstance.getType_name());

        return convertView;
    }
}
