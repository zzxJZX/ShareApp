package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.DetailsInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class DetailsAdapter extends BaseAdapter {

    private Context context;

    private List<DetailsInstance> list;

    public DetailsAdapter(List<DetailsInstance> list, Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.details_item,null);
            childViewHolder.house_type = (TextView) convertView.findViewById(R.id.house_type);
            childViewHolder.house_total = (TextView) convertView.findViewById(R.id.house_total);
            childViewHolder.house_free = (TextView) convertView.findViewById(R.id.house_free);
            childViewHolder.house_price = (TextView) convertView.findViewById(R.id.house_price);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        DetailsInstance detailsInstance = list.get(position);

        childViewHolder.house_type.setText(detailsInstance.getStore_type_name());

        childViewHolder.house_total.setText("总面积:"+detailsInstance.getTotal()+"立方");

        childViewHolder.house_free.setText("可用面积:"+detailsInstance.getFree()+"立方");

        childViewHolder.house_price.setText("价格:"+detailsInstance.getPrice()+"元/立方/月");

        return convertView;
    }

    class ChildViewHolder{
        TextView house_type;
        TextView house_total;
        TextView house_free;
        TextView house_price;
    }
}
