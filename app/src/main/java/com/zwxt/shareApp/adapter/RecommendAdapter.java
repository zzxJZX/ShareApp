package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.RecommendInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class RecommendAdapter extends BaseAdapter {

    private Context context;

    private List<RecommendInstance> list;

    public RecommendAdapter(List<RecommendInstance> list, Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_item,null);
            childViewHolder.recommend_name = (TextView) convertView.findViewById(R.id.recommend_name);
            childViewHolder.recommend_price = (TextView) convertView.findViewById(R.id.recommend_price);
            childViewHolder.recommend_list_image = (ImageView) convertView.findViewById(R.id.recommend_list_image);
            childViewHolder.recommend_insurance = (Button) convertView.findViewById(R.id.recommend_insurance);
            childViewHolder.recommend_envi = (Button) convertView.findViewById(R.id.recommend_envi);
            childViewHolder.recommend_maintain = (Button) convertView.findViewById(R.id.recommend_maintain);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        RecommendInstance recommendInstance = list.get(position);

        childViewHolder.recommend_name.setText(recommendInstance.getStore_name());

        if (recommendInstance.getImage() != null && !recommendInstance.getImage().equals("")){
            Glide.with(context).load(recommendInstance.getImage()).into(childViewHolder.recommend_list_image);
        }

        return convertView;
    }


    class ChildViewHolder{
        TextView recommend_name ,recommend_price ;
        ImageView recommend_list_image;
        Button recommend_insurance, recommend_envi, recommend_maintain;
    }
}
