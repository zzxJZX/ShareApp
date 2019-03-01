package com.zwxt.shareApp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.activity.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    private String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(
                            Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private void wxPay(){
        String phone = Information.getStringConfig(WXPayEntryActivity.this,Information.SHARE_LOGIN,Information.SHARE_LOGIN_ACCOUNT);
        String oid = Information.getStringConfig(WXPayEntryActivity.this,Information.SHARE_LOGIN,Information.WX_PAY_OID);
        String price = Information.getStringConfig(WXPayEntryActivity.this,Information.SHARE_LOGIN,Information.WX_PAY_PRICE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("oid",oid);
        requestParams.put("total_money",price+"");
        requestParams.put("check",getMD5(oid+phone));

        asyncHttpClient.post(Constants.MAIN_LINK+"/api/alipay/check_total",requestParams,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                System.out.println("aliPay:"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if (code.equals("0")){
                        Intent intent = new Intent();
                        intent.setAction(Constants.MAIL_STATUS_CHANGE);
                        sendBroadcast(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                System.out.println("aliPay接口异常或网络异常:"+throwable.getMessage());
            }
        });

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int code = baseResp.errCode;

        switch (code){
            case 0:
                Toast.makeText(WXPayEntryActivity.this,"支付成功",Toast.LENGTH_SHORT).show();;
                wxPay();
                break;

            case -1:
                finish();
                Toast.makeText(WXPayEntryActivity.this,
                        "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、您的微信账号异常等",Toast.LENGTH_SHORT).show();;
                break;

            case -2:
                finish();

//                Intent intent = new Intent();
//                intent.setAction(Constants.MAIL_STATUS_CHANGE);
//                sendBroadcast(intent);

                Toast.makeText(WXPayEntryActivity.this, "用户取消支付",
                        Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
