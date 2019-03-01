package com.zwxt.shareApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wxebffe4592504d37c";

    public static final String API_KEY = "e1f32fcc5f28f5f89c745fe7053f189f";

    public static final String MCH_ID = "1516941201";

    public static final String WX_LINK = "http://user.hlwj.ctthn.com";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

    public static int convention = -1;

    public static final String LOCATION_ACTION = "LOCATION_ACTION";

    public static final String MAIL_STATUS_CHANGE = "MAIL_STATUS_CHANGE";

    public static final String STOCK_STATUS_CHANGE = "STOCK_STATUS_CHANGE";

    public static final String GET_COUPON = "GET_COUPON";

    public static final String USER_COUPON = "USER_COUPON";

    public static final String LINK = "http://47.99.42.142";

    public static final String MAIN_LINK = "http://sharing.5955555.cn";

    public static final String HEAD_LINK = "http://admin.5955555.cn";

    public static boolean isNumber(String cardNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) cardNum);
        boolean result=matcher.matches();
        return result;
    }

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }



}
