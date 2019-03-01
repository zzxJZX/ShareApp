package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zwxt.shareApp.R;


/**
 * Created by Administrator on 2018/8/21.
 */

public class SuccessActivity extends Activity {

    private Button register_back_bt,success_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);
        init();
    }

    private void init(){
        register_back_bt = (Button) findViewById(R.id.register_back_bt);
        success_bt = (Button) findViewById(R.id.success_bt);
        register_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        success_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
