package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.StockSafeInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/27.
 */

public class StockSafeAdapter extends BaseAdapter{

    private Context context;

    private List<StockSafeInstance> list;

    public StockSafeAdapter(Context context, List<StockSafeInstance> list){
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

        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item,
                null);
        StockSafeInstance instance = list.get(position);
        TextView pacckName = (TextView) convertView.findViewById(R.id.spinnerName);
        if (instance.getPrice() != Constants.convention){
            pacckName.setText(""+instance.getSafe_name()+"("+instance.getPrice()+"元)");
        } else {
            pacckName.setText(""+instance.getSafe_name());
        }


        return convertView;
    }
}
