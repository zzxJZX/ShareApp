package com.zwxt.shareApp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.fragment.MailFragment;
import com.zwxt.shareApp.fragment.MyStockFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class ThingsActivity extends FragmentActivity{

    View things_title;

    private TextView myStock_text,setForMy_text;

    private ViewPager things_view_page;

    private List<Fragment> mPageList = new ArrayList<Fragment>();

    private Button things_back_bt;

    private MailFragment mailFragment = new MailFragment();

    private MyStockFragment myStockFragment = new MyStockFragment();

    private boolean active = false;

//    private int id = Constants.convention;
//
//    private String houseName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.things_layout);
        active = getIntent().getBooleanExtra("active",false);
//        id = getIntent().getIntExtra("id",Constants.convention);
//        houseName = getIntent().getStringExtra("houseName");
        setTitleHeight();
        init();
    }


    private void init(){
        myStock_text = (TextView) findViewById(R.id.myStock_text);
        setForMy_text = (TextView) findViewById(R.id.setForMy_text);
        things_view_page = (ViewPager) findViewById(R.id.things_view_page);
        things_back_bt = (Button) findViewById(R.id.things_back_bt);
        mPageList.add(myStockFragment);
        mPageList.add(mailFragment);
        things_view_page.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        if (active){
            things_view_page.setCurrentItem(1);
        }
        myStock_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                things_view_page.setCurrentItem(0);
            }
        });
        setForMy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                things_view_page.setCurrentItem(1);
            }
        });
        things_view_page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                System.out.println("position:"+position);

                if (position == 0) {
//                    myStock_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_blue));
                    myStock_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.red_tran);
                } else {
//                    myStock_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_gray));
                    myStock_text.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0);
                }

                if (position == 1) {
//                    setForMy_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_blue));
                    setForMy_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 , R.drawable.red_tran);
                } else {
//                    setForMy_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_gray));
                    setForMy_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 ,0);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        things_back_bt.setOnClickListener(new View.OnClickListener() {
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
        View view = findViewById(R.id.things_title);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = statusBarHeight1;
        view.setLayoutParams(params);
    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int location) {
            return mPageList.get(location);
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }
    }

}
