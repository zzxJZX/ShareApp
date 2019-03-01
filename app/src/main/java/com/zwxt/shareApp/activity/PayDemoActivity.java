package com.zwxt.shareApp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.zwxt.shareApp.AuthResult;
import com.zwxt.shareApp.PayResult;
import com.zwxt.shareApp.R;
import com.zwxt.shareApp.util.OrderInfoUtil2_0;

import java.util.Map;

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class PayDemoActivity extends FragmentActivity {

    /**
     * 支付宝支付业务：入参app_id
     */
//    public static final String APPID = "2017020705547578";
//    public static final String APPID = "2017020705547578";

    public static final String APPID = "2018082961169804";
    /**
     * 支付宝账户登录授权业务：入参pid值
     */
//    public static final String PID = "2088521588357167";

    public static final String PID = "2088231279992543";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
//    public static final String TARGET_ID = "11113050@qq.com";

    public static final String TARGET_ID = "hengzc888888@163.com";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
//    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmCoaNL/4kCAryMQ2A68RRhih/QaVqmnk+cfcvdDdvt3s03vcpCi/039RfifC0vrISKNOQrDMxVXYGzufyQ4332kzwr5sHfCiPC3Ox3fcOqg11K2f+zyj3lX+mgms5uimbODM9OAB3Pop93szCYKpgi+1iDcmeW0tyMHYaHq/+GEVr4UCsgQwlVVbsCjHU/k6PPfxcyoR0eI6mDAVeWPSu7mUAjKVcexEMR0gUw3IZmC6GIVRYdct0CZNsMA46JhUFiAVbd9TSx5iTFJSLBeaE67zJH2DRcYztFc4nEffsT8miBAKG15w9JdSFHZ//yPUE+6jr8SKmYypkX+g5cmYlAgMBAAECggEAMjJ6bRK2TWRVEtU7lN8yMcdSVO6euLTMtNE1fpYu8EpTKC/EHxciWnCUvvFv4OTJ5u2K0HEO5PpGPVauDrLXp7fKjPU59DBX/q7iokJNn5RPjz25KfKtGvSKa+d/zSr3yyJZc0eL9IDm05jY5Gbe+2MyN/OdIY/OJk56VfkzEaj+HZltMK2rNXZFjG5RMvNeTGnDMhH+Od5wIIOcoHzpKadSRAd/0ZBmq5Nhg7jL5Axa7thY0KAzQSjWjrlPKziyGEjgVLAgSRP2g7OJhgUmJDMEOIuvyvRe2d+LU32iMPSrTitSldOZVlrmyGV1Z0FBeTq37S4mN1uxqJQb4ASOIQKBgQDR7m2LfvK4YPu3m3oBDnKgjkTnYnjzbss+EcmBbKs5jOl4ZA2vFkRPzFkb36WsVSFY9m7r5YNy+Rro3aNgLKti2IMp2ViNGXx2/Cy/XLpDsPaTa6YwqHQeQikRVFL5WfY0aPvprHes7TdpSWaMmKEeBDFvFY1OyS9WHoL6QQkN+QKBgQDKemvoAc7UUH7zTcYqYFWuMAEB6KqWSGDqAF8iF+4iAEbY5w1TUPK/6ADra9T4AWvgq64E8jq3dNhrliLBqNqQdzk6/6r0V9q9BKrOCpYpQ7UsHCjuvydMWMuD/z7wvSHt+lg8MIPxZ+9i21Khy0HuM90JX3Cj0WTdCuiKHeBUjQKBgF4VJ9z7kNXXCRiZQk1U50RTXkgAlsoqXkW7/P3W8MnDACMnXAaayRSmNU1Lptkb8e0HzGBGJOoS/99a7Uw92LVc6TOJJ6lcwa4lh/xfF08c8PTSqtFlvVyONA+DJgiAK9oDLtrjo2LJkSrLbX0XLiXgpTLuirzDZ0aGOBblrKQpAoGBAJVdviCGxOdHAn/FzfwQ2OQIsZWQ59a5ShUVdRiXgqo+fgUgXWoq+wiqw5LoF1Fk6wnwXA12C+6uGcE0Hiuyvpl3+hYV6S0baleqC763fJKwSgRiOaf2B62Ai5GyR+IcA92kola0i4EYZTYOCWUlpltskxCJAXU30SpnE6hf5c85AoGBAIymE/xHhm2SZ3VruXbRpKDJq5dwANNJMLYzcwvlWIDV6A6wRbqjMflDrXVqj4LAyOGsnJwkdIyc7tlnqHzBLxpR1usfFBFzjViS0y+Lcee1qRe25eOGOHb0caDyT+1Ebxak0USA90R6/GYO+8hBdXMjE2zso0NQ7yQgDrldI3qg";

    public static final String RSA2_PRIVATE = "MMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCuXL/Wy5ZTsM4fFFtotAGn0he2dLjbdvgo0/IFL1PFP9dtukOgCwXtpvEs9jrwXg76U/mfGvJW6p9maZ3b7moWjB1UnKgh1xO6bndZ7o/y6354Y0QEFa6PPFI78dNsreUKdI5ChLA1whIRSdYooa/6rotFpdB46F4ei3lD/BOi3uxYP+r5dYXrp8gp8J3xmythuiOIFf3U4QggEo9vM8SfD5VJAxSFrzkyDUdtd62QZlJQfFelJvyC1I/s2E7jZHQCfmBaHsb3/YchYYuBF/3DWOVP680KfjNmzcCfMyyrfGHatqwa6nqq8ih+wAhOqBwdlh/kH2fxgEVHRI4jox2RAgMBAAECggEAZPYXrwxbk0JBWAW1mD8C5mXkjSWxFIMi1qFC9nWdEA4Tc9ny5mz7lcygweQh5KTSD2kolVRJFsLsYaSuX3cBvuSK5z9+q3maVs04Fr7oY0SxCoyQCb10QkQvab5xKfDXOuKarTGfxsEFLTWP6B8XgxQmWMHWoOwL86vXH+KKjDeL2wMyl4Lj+m7i6fCO1UajNCpS8u0Q0FYHAXhos2tCFNFDft9h6HcECZbON62BQVPZaN8eqU6V1cSFwFpDQgNKFeco4BseM43lgP6inXmCLBiYBYpRcaywxNNrVIBurCEKFoBBHo1eI5qYIcpsqZF+rKn/6VGv0lnUKpzDf+jiMQKBgQDahuSf3PqOxwjGHUTQ2s//wrsHDrvXorKi9EeKidUrEhjTp28f8i/g7GDU31zQlWyyYG83NZ+b8CWIHz4DbI6dxhgLO72mbZkZY3JJT9Uex7p5J5oivZ1M8/bXdX8jK25hYM4Q2uL3EACTOiy9UZnU2kMfctNd/r+fTxS397c64wKBgQDMQxMnT9JKPulXNNPJYTu+4ehC8fjKp+UAmp8Z3SqlGXLT+sTDwzn2/V0PnSn+vt+lvAWg0vUgzoTbdkgMnaztkb4e4pv229tzAufQQ5Us7gAGHZA/qI6HE2v4kkvGfpKPjM8ODKJtHus+WU8dMjouIERfUu3NUHU10dzmTbrr+wKBgD+cRo62X4USMN6ihHabSzKy94g9ZLaHWKKQowaTyqZn9SwfL/zHneJGTIWKo1TgOizOX3FdvaFBzVsLZRzQC8+nazZR5Im+m3NFfG4Uu//iucgsm/SC53gQCAl93U1ahh34dON5I9oxIUm6BG3zg+juw90yqo5Q6mywM2K9y+trAoGAH9DSyuzD9IyYC91On69lfvXxF3xZJDfCk7WB0qWAXvb3y8zjmRpvjOkp9aSlRei1LTt8JPC2/cUtLIHf9xcYOGjbFDR31puoMuHg+a+NLNJlbo5sSX4XtTJxPuaKpwBQE8v69FVDd3u5aJzAleuc5FJiU/a206QUna1ymuzAkMECgYAg9GXrVUfdM7fciVy+Fz9yv0jNrjw+LlOyty+CQ5pyElfuwNlLN5ysHDmxX1IFqNttiCluMGvqDgUrf5cqTNTeQV3ZVoj8c/p5enf+2CTxtNHty3djg0PN84F3Md6Jz2NzcImEE7zeed5LiKsQNqXf9JPFRMaEUA8OaTtdzXVeKw==";


//    public static final String RSA_PRIVATE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApgqGjS/+JAgK8jENgOvEUYYof0Glapp5PnH3L3Q3b7d7NN73KQov9N/UX4nwtL6yEijTkKwzMVV2Bs7n8kON99pM8K+bB3wojwtzsd33DqoNdStn/s8o95V/poJrObopmzgzPTgAdz6Kfd7MwmCqYIvtYg3JnltLcjB2Gh6v/hhFa+FArIEMJVVW7Aox1P5Ojz38XMqEdHiOpgwFXlj0ru5lAIylXHsRDEdIFMNyGZguhiFUWHXLdAmTbDAOOiYVBYgFW3fU0seYkxSUiwXmhOu8yR9g0XGM7RXOJxH37E/JogQChtecPSXUhR2f/8j1BPuo6/EipmMqZF/oOXJmJQIDAQAB";

    public static final String RSA_PRIVATE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArly/1suWU7DOHxRbaLQBp9IXtnS423b4KNPyBS9TxT/XbbpDoAsF7abxLPY68F4O+lP5nxryVuqfZmmd2+5qFowdVJyoIdcTum53We6P8ut+eGNEBBWujzxSO/HTbK3lCnSOQoSwNcISEUnWKKGv+q6LRaXQeOheHot5Q/wTot7sWD/q+XWF66fIKfCd8ZsrYbojiBX91OEIIBKPbzPEnw+VSQMUha85Mg1HbXetkGZSUHxXpSb8gtSP7NhO42R0An5gWh7G9/2HIWGLgRf9w1jlT+vNCn4zZs3AnzMsq3xh2rasGup6qvIofsAITqgcHZYf5B9n8YBFR0SOI6MdkQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private TextView sq;

    @SuppressLint("HandlerLeak")
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
                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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

                        sq.setText(authResult.toString());

                        Toast.makeText(PayDemoActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.toString()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayDemoActivity.this,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);

        sq = (TextView) findViewById(R.id.sq);
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,"","");
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayDemoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayDemoActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
//        Intent intent = new Intent(this, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
//         *
//         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
//         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
//         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
//         * 进行测试。
//         *
//         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
//         * 可以参考它实现自定义的 URL 拦截逻辑。
//         */
//        String url = "http://m.taobao.com";
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
    }

}
