package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zwxt.shareApp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class DiscountAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> list;

    public DiscountAdapter(List<Map<String,Object>> list , Context context){
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
        if (convertView == null){
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.discount_item,null);
            childViewHolder.discount_im = (ImageView) convertView.findViewById(R.id.discount_im);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        if (position == 0| position == 2){
            childViewHolder.discount_im.setImageResource(R.drawable.share_home_bg_three);
        } else if (position == 1){
            childViewHolder.discount_im.setImageResource(R.drawable.share_home_bg_two);
        } else if (position == 3){
            childViewHolder.discount_im.setImageResource(R.drawable.share_home_bg_one);
        }


        return convertView;
    }


    class ChildViewHolder{
        ImageView discount_im;
    }
}
