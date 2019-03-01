package com.zwxt.shareApp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zwxt.shareApp.AuthResult;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.Information;
import com.zwxt.shareApp.MD5;
import com.zwxt.shareApp.PayResult;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.Util;
import com.zwxt.shareApp.fragment.MyStockFragment;
import com.zwxt.shareApp.util.OrderInfoUtil2_0;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PayActivity extends Activity {

    private RelativeLayout back_re , alipay , wx_pay;

    private TextView pay_car_number , pay_price;

    private Button pay_bt;

    private CheckBox pay_check , wechat_check;

    public static final String APPID = "2018082961169804";

    public static final String PID = "2088231279992543";

    public static final String TARGET_ID = "hengzc888888@163.com";

    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCuXL/Wy5ZTsM4fFFtotAGn0he2dLjbdvgo0/IFL1PFP9dtukOgCwXtpvEs9jrwXg76U/mfGvJW6p9maZ3b7moWjB1UnKgh1xO6bndZ7o/y6354Y0QEFa6PPFI78dNsreUKdI5ChLA1whIRSdYooa/6rotFpdB46F4ei3lD/BOi3uxYP+r5dYXrp8gp8J3xmythuiOIFf3U4QggEo9vM8SfD5VJAxSFrzkyDUdtd62QZlJQfFelJvyC1I/s2E7jZHQCfmBaHsb3/YchYYuBF/3DWOVP680KfjNmzcCfMyyrfGHatqwa6nqq8ih+wAhOqBwdlh/kH2fxgEVHRI4jox2RAgMBAAECggEAZPYXrwxbk0JBWAW1mD8C5mXkjSWxFIMi1qFC9nWdEA4Tc9ny5mz7lcygweQh5KTSD2kolVRJFsLsYaSuX3cBvuSK5z9+q3maVs04Fr7oY0SxCoyQCb10QkQvab5xKfDXOuKarTGfxsEFLTWP6B8XgxQmWMHWoOwL86vXH+KKjDeL2wMyl4Lj+m7i6fCO1UajNCpS8u0Q0FYHAXhos2tCFNFDft9h6HcECZbON62BQVPZaN8eqU6V1cSFwFpDQgNKFeco4BseM43lgP6inXmCLBiYBYpRcaywxNNrVIBurCEKFoBBHo1eI5qYIcpsqZF+rKn/6VGv0lnUKpzDf+jiMQKBgQDahuSf3PqOxwjGHUTQ2s//wrsHDrvXorKi9EeKidUrEhjTp28f8i/g7GDU31zQlWyyYG83NZ+b8CWIHz4DbI6dxhgLO72mbZkZY3JJT9Uex7p5J5oivZ1M8/bXdX8jK25hYM4Q2uL3EACTOiy9UZnU2kMfctNd/r+fTxS397c64wKBgQDMQxMnT9JKPulXNNPJYTu+4ehC8fjKp+UAmp8Z3SqlGXLT+sTDwzn2/V0PnSn+vt+lvAWg0vUgzoTbdkgMnaztkb4e4pv229tzAufQQ5Us7gAGHZA/qI6HE2v4kkvGfpKPjM8ODKJtHus+WU8dMjouIERfUu3NUHU10dzmTbrr+wKBgD+cRo62X4USMN6ihHabSzKy94g9ZLaHWKKQowaTyqZn9SwfL/zHneJGTIWKo1TgOizOX3FdvaFBzVsLZRzQC8+nazZR5Im+m3NFfG4Uu//iucgsm/SC53gQCAl93U1ahh34dON5I9oxIUm6BG3zg+juw90yqo5Q6mywM2K9y+trAoGAH9DSyuzD9IyYC91On69lfvXxF3xZJDfCk7WB0qWAXvb3y8zjmRpvjOkp9aSlRei1LTt8JPC2/cUtLIHf9xcYOGjbFDR31puoMuHg+a+NLNJlbo5sSX4XtTJxPuaKpwBQE8v69FVDd3u5aJzAleuc5FJiU/a206QUna1ymuzAkMECgYAg9GXrVUfdM7fciVy+Fz9yv0jNrjw+LlOyty+CQ5pyElfuwNlLN5ysHDmxX1IFqNttiCluMGvqDgUrf5cqTNTeQV3ZVoj8c/p5enf+2CTxtNHty3djg0PN84F3Md6Jz2NzcImEE7zeed5LiKsQNqXf9JPFRMaEUA8OaTtdzXVeKw==";
    public static final String RSA_PRIVATE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArly/1suWU7DOHxRbaLQBp9IXtnS423b4KNPyBS9TxT/XbbpDoAsF7abxLPY68F4O+lP5nxryVuqfZmmd2+5qFowdVJyoIdcTum53We6P8ut+eGNEBBWujzxSO/HTbK3lCnSOQoSwNcISEUnWKKGv+q6LRaXQeOheHot5Q/wTot7sWD/q+XWF66fIKfCd8ZsrYbojiBX91OEIIBKPbzPEnw+VSQMUha85Mg1HbXetkGZSUHxXpSb8gtSP7NhO42R0An5gWh7G9/2HIWGLgRf9w1jlT+vNCn4zZs3AnzMsq3xh2rasGup6qvIofsAITqgcHZYf5B9n8YBFR0SOI6MdkQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private String oid;

    private float price;

    private MailBroadCastReceiver broadCastReceiver = new MailBroadCastReceiver();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);

        oid = getIntent().getStringExtra("oid");
        price = getIntent().getFloatExtra("price",0);

        sb = new StringBuffer();

        req = new PayReq();

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);

        String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        PrePayIdAsyncTask prePayIdAsyncTask = new PrePayIdAsyncTask();
        prePayIdAsyncTask.execute(urlString);

        init();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MAIL_STATUS_CHANGE);
        registerReceiver(broadCastReceiver, intentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }

    class MailBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.MAIL_STATUS_CHANGE)){
//                logisticsList();
                finish();
            }
        }
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion", "----" + packageSign);
        System.out.println("sign:" + packageSign);
        return packageSign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", "----" + sb.toString());
        return sb.toString();
    }

    private Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", "----" + e.toString());
        }
        return null;

    }

    private String genProductArgs(String body, String fee) {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", oid));
            packageParams.add(new BasicNameValuePair("notify_url", Constants.WX_LINK+"/api/wxpay/pay"));
            //微信订单号
            packageParams.add(new BasicNameValuePair("out_trade_no", oid));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", fee));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));


            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            return null;
        }


    }

    private class PrePayIdAsyncTask extends AsyncTask<String, Void, Map<String, String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(PayActivity.this, "提示", "正在提交订单");

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected Map<String, String> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = String.format(params[0]);

            float price100 = price*100;
            String price = Integer.toString(new Float(price100).intValue());

            System.out.println("微信："+price);

            String entity = genProductArgs("停车费用", price);
            Log.e("Simon", ">>>>" + entity);
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Log.e("orion", "----" + content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
            }
            resultunifiedorder = result;
            String return_code = resultunifiedorder.get("return_code");
            System.out.println("code:"+result.toString());

            String err_code_des = resultunifiedorder.get("err_code_des");
            if (err_code_des!=null){
                Toast.makeText(PayActivity.this,err_code_des,Toast.LENGTH_SHORT).show();
            }

            if (return_code.equals("FAIL")){
                String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                PrePayIdAsyncTask prePayIdAsyncTask = new PrePayIdAsyncTask();
                prePayIdAsyncTask.execute(urlString);
            }
        }
    }

    private void init(){
        back_re = (RelativeLayout) findViewById(R.id.back_re);
        alipay = (RelativeLayout) findViewById(R.id.alipay);
        wx_pay = (RelativeLayout) findViewById(R.id.wx_pay);
        pay_car_number = (TextView) findViewById(R.id.pay_car_number);
        pay_price = (TextView) findViewById(R.id.pay_price);
        pay_check = (CheckBox) findViewById(R.id.pay_check);
        wechat_check = (CheckBox) findViewById(R.id.wechat_check);
        pay_bt = (Button) findViewById(R.id.pay_bt);

        pay_car_number.setText(oid);

        pay_price.setText("¥ "+Float.toString(price));

        back_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pay_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechat_check.setChecked(false);
                pay_check.setChecked(true);
            }
        });

        wechat_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_check.setChecked(false);
                wechat_check.setChecked(true);
            }
        });

        pay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                payV2();

//                if (parking_price != 0){
                    if (pay_check.isChecked()){
                        payV2();
                    } else if (wechat_check.isChecked()){
                        genPayReq();
                    }
//                } else {
//                    Toast.makeText(PayActivity.this,"订单结算完成！",Toast.LENGTH_SHORT);
//                }


            }
        });

    }


    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,oid,Float.toString(price));
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = RSA2_PRIVATE ;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

//        sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion", "----" + sb.toString());
        return appSign;
    }

    private IWXAPI api;

    private void sendPayReq() {
        api.registerApp(Constants.APP_ID);
        if (api.sendReq(req)){
//            Toast.makeText(PayActivity.this,"111",Toast.LENGTH_SHORT).show();
            HashMap<String, Object> WXMap = new HashMap<>();
            WXMap.put(Information.WX_PAY_OID,oid);
            WXMap.put(Information.WX_PAY_PRICE,price+"");
            Information.saveInformation(PayActivity.this,Information.SHARE_LOGIN,WXMap);
        } else {
//            Toast.makeText(PayActivity.this,"222",Toast.LENGTH_SHORT).show();
        }
//        api.sendReq(req);
    }

    private PayReq req;

    private Map<String, String> resultunifiedorder;

    private StringBuffer sb;

    private void genPayReq() {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams).toUpperCase();

        sb.append("sign\n" + req.sign.toUpperCase() + "\n\n");

        // show.setText(sb.toString());
        System.out.println("signParams:" + req.sign);
        sendPayReq();
    }

    private void aliPay(){
        String phone = Information.getStringConfig(PayActivity.this,Information.SHARE_LOGIN,Information.SHARE_LOGIN_ACCOUNT);
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


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        aliPay();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户

//                        System.out.println("授权：" + authResult.toString());

                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.toString()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

}
