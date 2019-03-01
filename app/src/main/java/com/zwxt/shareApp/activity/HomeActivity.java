package com.zwxt.shareApp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.fragment.HomeFragment;
import com.zwxt.shareApp.fragment.MapFragment;
import com.zwxt.shareApp.fragment.MyFragment;


/**
 * Created by Administrator on 2018/8/9.
 */

public class HomeActivity extends FragmentActivity {

    private LayoutInflater layoutInflater;

    private FragmentTabHost mTabHost;


    private Class fragmentArray[] = {HomeFragment.class,
            MapFragment.class, HomeFragment.class, MyFragment.class};

    private int mImageViewArray[] = {R.drawable.main_tab_item_home,
            R.drawable.main_tab_item_map, R.drawable.main_tab_item_vip,
            R.drawable.main_tab_item_user};

    private String mTextviewArray[] = {"首页", "地图找库", "会员服务", "我的"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_base_page_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        initView();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ShareApplication.locationClient.stopLocation();
    }




    private void initView() {
        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#ffffff"));
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
//                System.out.println("选中："+s);
                upDateTab(mTabHost);
            }
        });

        mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"即将开放,尽请期待！",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void upDateTab(FragmentTabHost mTabHost) {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textview);
            if (mTabHost.getCurrentTab() == i) {//选中
                tv.setTextColor(this.getResources().getColor(R.color.bg));
            } else {//不选中
                tv.setTextColor(this.getResources().getColor(R.color.black));
            }
        }
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }
}
