package com.zwxt.shareApp.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zwxt.shareApp.Constants;
import com.zwxt.shareApp.MD5;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private Button gotoBtn, regBtn, launchBtn, checkBtn, scanBtn , prePayBtn , payTrueBtn;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private PayReq req;

    private Map<String, String> resultunifiedorder;

    private StringBuffer sb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        req = new PayReq();

        sb = new StringBuffer();

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);


        regBtn = (Button) findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 将该app注册到微信
                if (api.registerApp(Constants.APP_ID)){
                    System.out.println("000");
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_yyyy2";

                    if (api.sendReq(req)){
                        System.out.println(111);
                    }
                }

            }
        });

        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
//                finish();
            }
        });

        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(WXEntryActivity.this, "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show();
            }
        });

        checkBtn = (Button) findViewById(R.id.check_timeline_supported_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int wxSdkVersion = api.getWXAppSupportAPI();
                if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
                    Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
                }
            }
        });

        scanBtn = (Button) findViewById(R.id.scan_qrcode_login_btn);
        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WXEntryActivity.this, ScanQRCodeLoginActivity.class));
//                finish();
            }
        });

        prePayBtn = (Button) findViewById(R.id.prePay);
        prePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                PrePayIdAsyncTask prePayIdAsyncTask = new PrePayIdAsyncTask();
                prePayIdAsyncTask.execute(urlString);
            }
        });

        payTrueBtn = (Button) findViewById(R.id.payTrue);
        payTrueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genPayReq();
            }
        });



        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class PrePayIdAsyncTask extends AsyncTask<String, Void, Map<String, String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(WXEntryActivity.this, "提示", "正在提交订单");

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected Map<String, String> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = String.format(params[0]);
            String entity = genProductArgs("测试", "1");
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
            String result_code = resultunifiedorder.get("result_code");
            System.out.println("code:"+result.toString());
            if (result_code.equals("FAIL")){
                String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                PrePayIdAsyncTask prePayIdAsyncTask = new PrePayIdAsyncTask();
                prePayIdAsyncTask.execute(urlString);
            }
        }
    }


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

    private void sendPayReq() {
        api.registerApp(Constants.APP_ID);
        if (api.sendReq(req)){
            Toast.makeText(WXEntryActivity.this,"111",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(WXEntryActivity.this,"222",Toast.LENGTH_SHORT).show();
        }
//        api.sendReq(req);
    }

    public Map<String, String> decodeXml(String content) {

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
        String oid = "20150801253463";
        try {
            String nonceStr = genNonceStr();

            String wx_dingdan = genOutTradNo();

            SharedPreferences sp2 = getSharedPreferences("chongzhi",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp2.edit();
            editor.putString("wx_dingdan", wx_dingdan);
            editor.commit();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", Constants.WX_LINK+"/app_recharge/wechat_xin_notify.php"));
//            packageParams.add(new BasicNameValuePair("notify_url", Constants.MAIN_LINK+"/app_recharge/wechat_notify.php"));
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
    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;

                String code = ((SendAuth.Resp) resp).code;

                System.out.println("code:"+code);

                final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                        ""+ Constants.APP_ID +"&secret="+ Constants.API_KEY+"&code="+code+"&grant_type=authorization_code",new AsyncHttpResponseHandler(){

                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);

                        try {
                            final JSONObject object = new JSONObject(s);
                            int errcode = object.optInt("errcode",6899);
                            if (errcode == 6899){
                                final String access_token = object.optString("access_token");
                                final String refresh_token = object.optString("refresh_token");
                                final String openid = object.optString("openid");
                                asyncHttpClient.get("https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openid,new AsyncHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(String s) {
                                        super.onSuccess(s);

                                        try {
                                            JSONObject object1 = new JSONObject(s);
                                            int errCode = object1.optInt("errcode",6899);
                                            String errmsg = object1.optString("errmsg");
                                            if (errCode == 0 && errmsg.equals("ok")){
                                                asyncHttpClient.get("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid,new AsyncHttpResponseHandler(){

                                                    @Override
                                                    public void onSuccess(String s) {
                                                        super.onSuccess(s);
                                                        System.out.println("获取用户信息成功："+s);
                                                    }


                                                    @Override
                                                    public void onFailure(Throwable throwable, String s) {
                                                        super.onFailure(throwable, s);
                                                        System.out.println("获取用户信息失败");
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(WXEntryActivity.this,"验证access_token不通过",Toast.LENGTH_SHORT).show();

                                                asyncHttpClient.get("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                                                        + Constants.APP_ID+"&grant_type=refresh_token&refresh_token="+refresh_token,new AsyncHttpResponseHandler(){
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        super.onSuccess(s);
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable throwable, String s) {
                                                        super.onFailure(throwable, s);
                                                    }
                                                });

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onFailure(Throwable throwable, String s) {
                                        super.onFailure(throwable,s);
                                        System.out.println("验证access_token失败");
                                    }
                                });

                            } else {
                                Toast.makeText(WXEntryActivity.this,"access_token获取失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        super.onFailure(throwable, s);
                        System.out.println("code请求失败");
                    }
                });

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;

                System.out.println(-2);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                System.out.println(-4);
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;

                System.out.println(-5);
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }


        int code = resp.errCode;

//        switch (code){
//            case 0:
//                Toast.makeText(WXEntryActivity.this,"支付成功",Toast.LENGTH_SHORT).show();;
//                break;
//
//            case -1:
//                Toast.makeText(WXEntryActivity.this,
//                        "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、您的微信账号异常等",Toast.LENGTH_SHORT).show();;
//                break;
//
//            case -2:
//                Toast.makeText(WXEntryActivity.this, "用户取消支付",
//                        Toast.LENGTH_SHORT).show();
//                break;
//
//        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void goToGetMsg() {
//        Intent intent = new Intent(this, GetFromWXActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
//        finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//        WXMediaMessage wxMsg = showReq.message;
//        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//
//        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
//        msg.append("description: ");
//        msg.append(wxMsg.description);
//        msg.append("\n");
//        msg.append("extInfo: ");
//        msg.append(obj.extInfo);
//        msg.append("\n");
//        msg.append("filePath: ");
//        msg.append(obj.filePath);
//
//        Intent intent = new Intent(this, ShowFromWXActivity.class);
//        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//        startActivity(intent);
//        finish();
    }
}