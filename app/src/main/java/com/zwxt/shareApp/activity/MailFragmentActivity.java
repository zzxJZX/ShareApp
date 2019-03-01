package com.zwxt.shareApp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.fragment.MySendFragment;


/**
 * Created by Administrator on 2018/8/25.
 */

public class MailFragmentActivity extends FragmentActivity {

    private Button mBackButton;

    private MySendFragment mySendFragment;

    private int id = Constants.convention;

    private String houseName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_fragment_layout);

        setTitleHeight();

        mySendFragment = new MySendFragment();

        id = getIntent().getIntExtra("id", Constants.convention);
        houseName = getIntent().getStringExtra("houseName");

        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("houseName",houseName);
        mySendFragment.setArguments(bundle);

        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mail_menu_frame, mySendFragment);
        t.commit();

        mBackButton = (Button) findViewById(R.id.mail_back_bt);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        View view = findViewById(R.id.mail_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }
}
