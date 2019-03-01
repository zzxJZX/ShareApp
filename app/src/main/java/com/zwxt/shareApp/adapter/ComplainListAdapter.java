package com.zwxt.shareApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.activity.ComplainDetailActivity;
import com.zwxt.shareApp.instance.ComplainListInstance;

import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */

public class ComplainListAdapter extends BaseAdapter {

    private List<ComplainListInstance> listInstances;

    private Context context;

    public ComplainListAdapter(List<ComplainListInstance> listInstances , Context context){
        this.listInstances = listInstances;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listInstances.size();
    }

    @Override
    public Object getItem(int position) {
        return listInstances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.complain_item,
                null);

        TextView code_text = (TextView) convertView.findViewById(R.id.code_text);
        ImageView complaint_image = (ImageView) convertView.findViewById(R.id.complaint_image);
        TextView complaint_text = (TextView) convertView.findViewById(R.id.complaint_text);
        TextView state_text = (TextView) convertView.findViewById(R.id.state_text);
        Button details_bt = (Button) convertView.findViewById(R.id.details_bt);

        final ComplainListInstance listInstance = listInstances.get(position);

        code_text.setText("订单编号:"+listInstance.getOid());

        complaint_text.setText(listInstance.getTitle());

        if (listInstance.getStatus() == 0){
            state_text.setText(" 管理员未回复");
        } else if (listInstance.getStatus() == 1){
            state_text.setText(" 管理员已回复");
        } else if (listInstance.getStatus() == 2){
            state_text.setText(" 投诉关闭");
        }

        details_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComplainDetailActivity.class);
                intent.putExtra("id",listInstance.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
