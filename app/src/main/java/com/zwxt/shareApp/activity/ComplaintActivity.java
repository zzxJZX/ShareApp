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
import com.zwxt.shareApp.fragment.MyComplaintFragment;
import com.zwxt.shareApp.fragment.UserComplaintFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */

public class ComplaintActivity extends FragmentActivity {

    private TextView user_complaint , my_complaint;

    private ViewPager complaints_view_page;

    private Button complaint_back_bt;

    private List<Fragment> mPageList = new ArrayList<Fragment>();

    private UserComplaintFragment userComplaintFragment = new UserComplaintFragment();

    private MyComplaintFragment myComplaintFragment = new MyComplaintFragment();

    private boolean state = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_layout);
        state = getIntent().getBooleanExtra("state",false);
        setTitleHeight();
        init();
    }

    private void init(){
        user_complaint = (TextView) findViewById(R.id.user_complaint);
        my_complaint = (TextView) findViewById(R.id.my_complaint);
        complaints_view_page = (ViewPager) findViewById(R.id.complaints_view_page);
        complaint_back_bt = (Button) findViewById(R.id.complaint_back_bt);
        mPageList.add(userComplaintFragment);
        mPageList.add(myComplaintFragment);
        complaints_view_page.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        if (!state){
            complaints_view_page.setCurrentItem(1);
        }
        user_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaints_view_page.setCurrentItem(0);
            }
        });
        my_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaints_view_page.setCurrentItem(1);
            }
        });

        complaints_view_page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                System.out.println("position:"+position);

                if (position == 0) {
//                    myStock_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_blue));
                    user_complaint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.red_tran);
                } else {
//                    myStock_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_gray));
                    user_complaint.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0);
                }

                if (position == 1) {
//                    setForMy_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_blue));
                    my_complaint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 , R.drawable.red_tran);
                } else {
//                    setForMy_text.setTextColor(getResources().getColor(R.color.sp_mini_bar_text_gray));
                    my_complaint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 ,0);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        complaint_back_bt.setOnClickListener(new View.OnClickListener() {
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
        View view = findViewById(R.id.complaint_title);
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
