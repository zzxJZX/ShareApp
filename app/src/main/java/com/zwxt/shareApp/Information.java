package com.zwxt.shareApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/21.
 */

public class Information {

    public static String SHARE_LOGIN = "Share_Login";

    public static String SHARE_LOGIN_ACCOUNT = "Share_Login_Account";

    public static String SHARE_LOGIN_PASS = "Share_Login_Pass";

    public static String ACCOUNT_HEAD_IMAGE_URL = "Account_Head_Image_Url";

    public static String WECHAT_ACCOUNT = "Wechat_Account";

    public static String ALI_ACCOUNT = "ALi_Account";

    public static String ACCOUNT_ADDRESS = "Account_Address";

    public static String ACCOUNT_POINTS = "Account_Points";

    public static String ACCOUNT_NAME = "Account_Name";

    public static String ACCOUNT_MONEY = "Account_Money";

    public static String ACCOUNT_PROVINCE = "Account_Province";

    public static String ACCOUNT_City = "Account_City";

    public static String ACCOUNT_POSITION = "Account_Position";

    public static String ACCOUNT_TOKEN = "Account_Token";

    public static String WX_PAY_OID = "WX_Pay_Oid";

    public static String WX_PAY_PRICE = "WX_Pay_Price";

    public static void saveInformation(Context context , String category , Map<String , Object> map){
        if (map == null || map.isEmpty())
            return;
        SharedPreferences sp = context.getSharedPreferences(category, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = map.get(key);
            if (value instanceof Boolean)
                editor.putBoolean(key, (Boolean) value);
            else if (value instanceof Integer)
                editor.putInt(key, (Integer) value);
            else if (value instanceof Long)
                editor.putLong(key, (Long) value);
            else if (value instanceof Float)
                editor.putFloat(key, (Float) value);
            else if (value instanceof Double)
                editor.putFloat(key, ((Double) value).floatValue());
            else if (value instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) value;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                String imageBase64 = new String(android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT));
                editor.putString(key, imageBase64);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Serializable) {
                String encodedString = null;
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(value);
                    oos.close();
                    encodedString = new String(android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (encodedString != null) {
                    editor.putString(key, encodedString);
                }
            }
            else if (value != null)
                editor.putString(key, value.toString());
        }
        editor.commit();
    }

    public static String getStringConfig(Context context , String category , String key) {
        if (key == null)
            return null;
        SharedPreferences sp = context.getSharedPreferences(category , Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

}
