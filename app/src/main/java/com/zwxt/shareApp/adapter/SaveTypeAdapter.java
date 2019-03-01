package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.instance.DetailsInstance;
import com.zwxt.shareApp.instance.SaveInstance;

import java.util.List;

public class SaveTypeAdapter extends BaseAdapter {

    private Context context;

    private List<SaveInstance> list ;

    public SaveTypeAdapter(Context context , List<SaveInstance> list){
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
        SaveInstance saveInstance = list.get(position);
        TextView pacckName = (TextView) convertView.findViewById(R.id.spinnerName);

        pacckName.setText(saveInstance.getSaveTpe());

        return convertView;
    }
}
