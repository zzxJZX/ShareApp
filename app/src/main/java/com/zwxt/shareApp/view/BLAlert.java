package com.zwxt.shareApp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zwxt.shareApp.CallBack;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.DetailsCouponLIstAdapter;
import com.zwxt.shareApp.instance.CouponsShowInstance;

import java.util.List;


/**
 * Created by Administrator on 2018/8/27.
 */

public class BLAlert {

    public static final int YES = 0;

    public static final int NO = 1;


    public interface OnAlertSelectId {
        void onClick(int whichButton);
    }

    public interface OnChooseId{
        void onClick(int choose);
    }

    public interface OnAlertListSelectId {
        void onClick(int whichButton);
    }

    public static Dialog ShowListAlert(Context context , final  OnAlertListSelectId selectId , List<CouponsShowInstance> instanceList){
        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_menu_layout, null);
        final ListView list = (ListView) layout.findViewById(R.id.content_list);

        CallBack callBack = new CallBack() {
            @Override
            public void onClick(boolean onClick) {
                if (onClick){
                    dlg.dismiss();
                }
            }
        };

        DetailsCouponLIstAdapter adapter = new DetailsCouponLIstAdapter(instanceList , context , callBack);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectId.onClick(position);
                dlg.dismiss();
                list.requestFocus();
            }
        });

        TextView cancelText = (TextView) layout.findViewById(R.id.cancel_text);
        cancelText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();

        return dlg;
    }

    public static Dialog showChooseAlert(Context context,final OnChooseId chooseId){
        final Dialog dlg = new Dialog(context, R.style.BLTheme_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.choose_reason_layout, null);
        final RadioGroup radio_group = (RadioGroup) layout.findViewById(R.id.radio_group);
        Button confimButton = (Button) layout.findViewById(R.id.dialog_yes);
        Button cacelButton = (Button) layout.findViewById(R.id.dialog_no);

        confimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (radio_group.getCheckedRadioButtonId()){

                    case R.id.logistics_problem:

                        chooseId.onClick(0);

                        break;

                    case R.id.item_problem:

                        chooseId.onClick(1);

                        break;

                    case R.id.other_problem:

                        chooseId.onClick(2);

                        break;

                }
                dlg.dismiss();
            }
        });

        cacelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });


        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.CENTER;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(false);
        dlg.setContentView(layout);
        dlg.show();

        return dlg;
    }

    public static Dialog showAlert(Context context, String messageId, final OnAlertSelectId alertDo) {
        final Dialog dlg = new Dialog(context, R.style.BLTheme_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bl_alert_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        TextView titleView = (TextView) layout.findViewById(R.id.dialog_title);
        TextView messTextView = (TextView) layout.findViewById(R.id.dialog_msg);
//        messTextView.setGravity(messageGravity);
        Button confimButton = (Button) layout.findViewById(R.id.dialog_yes);
        Button cacelButton = (Button) layout.findViewById(R.id.dialog_no);

//        if(confimButtonText != null){
//            confimButton.setText(confimButtonText);
//        }
//
//        if (cancleButtonText != null) {
//            cacelButton.setText(cancleButtonText);
//        }

//        if(!TextUtils.isEmpty(title)){
//            titleView.setText(title);
//        }

        if (messageId != null) {
            messTextView.setText(messageId);
        }

        confimButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDo.onClick(YES);
                dlg.dismiss();
            }
        });

        cacelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDo.onClick(NO);
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.CENTER;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(false);
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }

}
