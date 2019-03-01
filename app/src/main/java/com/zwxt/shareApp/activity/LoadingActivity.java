package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;


/**
 * Created by Administrator on 2018/8/10.
 */

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        setTitleHeight();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                String phone = Information.getStringConfig(LoadingActivity.this,Information.SHARE_LOGIN,Information.SHARE_LOGIN_ACCOUNT);
                String pass = Information.getStringConfig(LoadingActivity.this,Information.SHARE_LOGIN,Information.SHARE_LOGIN_PASS);
                if (!phone.equals("") && !pass.equals("")){
                    Intent intent = new Intent(LoadingActivity.this,HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                LoadingActivity.this.finish();
                //do something

            }
        }, 1000);
    }


    private void setTitleHeight (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        View view = findViewById(R.id.load_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }
}
