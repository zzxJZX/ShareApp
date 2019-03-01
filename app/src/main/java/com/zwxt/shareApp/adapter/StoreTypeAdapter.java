package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.DetailsInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/23.
 */

public class StoreTypeAdapter extends BaseAdapter{

    private List<DetailsInstance> list;
    private Context context;

    public StoreTypeAdapter(List<DetailsInstance> list , Context context){
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
        DetailsInstance detailsInstance = list.get(position);
        TextView pacckName = (TextView) convertView.findViewById(R.id.spinnerName);
        if (detailsInstance.getFree() != Constants.convention){
            pacckName.setText(""+detailsInstance.getStore_type_name()+"(剩余可存:"+detailsInstance.getFree()+"立方)");
        } else {
            pacckName.setText(""+detailsInstance.getStore_type_name());
        }

        return convertView;
    }
}
