package com.zwxt.shareApp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.Rom;
import com.zwxt.shareApp.ShareApplication;
import com.zwxt.shareApp.activity.CityChoiceActivity;
import com.zwxt.shareApp.activity.GPSNaviActivity;
import com.zwxt.shareApp.activity.MailActivity;
import com.zwxt.shareApp.activity.PayActivity;
import com.zwxt.shareApp.activity.PayDemoActivity;
import com.zwxt.shareApp.activity.RecommendActivity;
import com.zwxt.shareApp.activity.ThingsActivity;
import com.zwxt.shareApp.adapter.DiscountAdapter;
import com.zwxt.shareApp.view.BannerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/8/10.
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.location_text)
    TextView location_text;
    @Bind(R.id.gone_title)
    View gone_title;
    @Bind(R.id.search_Warehouse_Ed)
    EditText search_Warehouse_Ed;
    @Bind(R.id.qr_re)
    RelativeLayout qr_re;
    @Bind(R.id.recommend_re)
    RelativeLayout recommend_re;
    @Bind(R.id.things_re)
    RelativeLayout things_re;
    @Bind(R.id.mail_re)
    RelativeLayout mail_re;
    private GridView discount_grid;
    @Bind(R.id.bannerView)
    BannerView bannerView;

    private List<String> strings;

    private LocationBroadCastReceiver broadCastReceiver = new LocationBroadCastReceiver();

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        mStatusBarView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) gone_title.getLayoutParams();
        params.height = statusBarHeight;
        gone_title.setLayoutParams(params);
        discount_grid = (GridView) mView.findViewById(R.id.discount_grid);

        if (Rom.isEmui()) {
            System.out.println("isEMUI");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        } else if (Rom.isFlyme()) {
            System.out.println("isFlyme");
            setMeizuStatusBarDarkIcon(getActivity(), false);
        } else if (Rom.isMiui()) {
            System.out.println("isMiui");
            setMiuiStatusBarDarkMode(getActivity(), false);
        } else {
            System.out.println("isis");
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        }

        if (ShareApplication.locationClient.isStarted()){

        } else {
            ShareApplication.locationClient.startLocation();
        }

        strings = new ArrayList<>();

        strings.add("http://pic74.nipic.com/file/20150813/10634318_132510392000_2.jpg");
        strings.add("http://pic33.nipic.com/20130914/3267219_115451422382_2.jpg");
        strings.add("http://pic36.nipic.com/20131213/7755667_223712251391_2.jpg");
        strings.add("http://pic81.nipic.com/file/20151028/22036204_104827200000_2.jpg");
        strings.add("http://pic67.nipic.com/file/20150515/12973503_100930685000_2.jpg");

        bannerView.addUrl(strings);

        bannerView.setTextSize(5.0f, R.color.black);
        bannerView.setTextGravity(2);
        bannerView.setIndicatorGravity(2);
//        bannerView.setIndicatorColor(Color.RED,Color.BLUE);
        bannerView.setDefulatTime(2000);
        bannerView.setFooterBackGround(R.color.bg);

        bannerView.setImageViewLoadLinstener(new BannerView.ImageViewLoad() {
            @Override
            public void ImageLoad(ImageView view, String url) {
                Glide.with(getActivity()).load(url).error(R.drawable.t).dontAnimate().into(view);
            }

            @Override
            public void ImageClick(int index) {
            }
        });

        search_Warehouse_Ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    Intent intent = new Intent(getActivity(),RecommendActivity.class);
                    intent.putExtra("homeSearch",search_Warehouse_Ed.getText().toString());
                    startActivity(intent);

//                    Toast.makeText(RecommendActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });



    }

    class LocationBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.LOCATION_ACTION)){
                String city_name = intent.getStringExtra("city_name");
                if (city_name !=null && location_text!=null){
                    location_text.setText(city_name);
                }
                ShareApplication.locationClient.stopLocation();
            }
        }
    }


    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    @Override
    protected void initData() {
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0 ; i < 4 ; i++){
            Map<String , Object> map = new HashMap<>();
            map.put("i",i);
            list.add(map);
        }
        DiscountAdapter discountAdapter = new DiscountAdapter(list,getActivity());
        discount_grid.setAdapter(discountAdapter);
        things_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ThingsActivity.class);
                startActivity(intent);
            }
        });
        recommend_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecommendActivity.class);
                startActivity(intent);
            }
        });
        qr_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PayActivity.class);
                startActivity(intent);
            }
        });
        mail_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MailActivity.class);
                startActivity(intent);
            }
        });
        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CityChoiceActivity.class);
                startActivityForResult(intent,1);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOCATION_ACTION);
        getActivity().registerReceiver(broadCastReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadCastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2){
            String area_name = data.getStringExtra("area_name");
            location_text.setText(area_name);
        }
    }

    @Override
    protected void initListener() {

    }
}
