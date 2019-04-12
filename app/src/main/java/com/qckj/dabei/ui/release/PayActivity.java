package com.qckj.dabei.ui.release;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.alipay.AlipayUtils;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.manager.release.PayRequester;
import com.qckj.dabei.model.release.DemandOrderInfo;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.IpManagerUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * 订单支付界面
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class PayActivity extends BaseActivity {

    public static final String KEY_ORDER_INFO = "key_order_info";

    @FindViewById(R.id.order_name)
    private CommonItemView orderName;

    @FindViewById(R.id.order_price)
    private CommonItemView orderPrice;

    @FindViewById(R.id.pay_money)
    private TextView payMoney;

    @FindViewById(R.id.alipay_rel)
    private RelativeLayout alipayRel;

    @FindViewById(R.id.alipay_rb)
    private RadioButton alipayRb;

    @FindViewById(R.id.wx_rel)
    private RelativeLayout wxRel;

    @FindViewById(R.id.wx_rb)
    private RadioButton wxRb;

    @FindViewById(R.id.go_pay_btn)
    private Button goPayBtn;

    @Manager
    private UserManager userManager;

    private DemandOrderInfo orderInfo;

    private int payType = 2;
    private IWXAPI wxapi;

    public static void startActivity(Context context, DemandOrderInfo orderInfo) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(KEY_ORDER_INFO, orderInfo);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        orderInfo = (DemandOrderInfo) getIntent().getSerializableExtra(KEY_ORDER_INFO);
        orderName.setContent(orderInfo.getName());
        orderPrice.setContent(orderInfo.getMoney() + "元");
        payMoney.setText("还需支付：" + orderInfo.getMoney() + "元");
        wxapi = WXAPIFactory.createWXAPI(this, CommonUtils.WX_APP_ID);
    }

    private void initListener() {
        alipayRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 2;
                alipayRb.setChecked(true);
                wxRb.setChecked(false);
            }
        });

        wxRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = 1;
                alipayRb.setChecked(false);
                wxRb.setChecked(true);
            }
        });

        goPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
    }

    private void pay() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);
        IpManagerUtils utils = new IpManagerUtils(wifiManager);
        new PayRequester(orderInfo.getMoney() + "", orderInfo.getName(), orderInfo.getOrderId(),
                payType, userManager.getCurId(), utils.ip(), new OnHttpResponseCodeListener<String>() {

            @Override
            public void onHttpResponse(boolean isSuccess, String data, String message) {
                super.onHttpResponse(isSuccess, data, message);
                if (isSuccess) {
                    if (payType == 1) {
                        wxPay(data);
                    } else {
                        goAlipay(data);
                    }
                } else {
                    showToast(message);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    /**
     * 调用微信支付
     */
    private void wxPay(String result) {
        try {
            JSONObject json = new JSONObject(result);
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = "Sign=WXPay";
            req.sign = json.getString("sign");
            wxapi.registerApp(CommonUtils.WX_APP_ID);
            wxapi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝支付
     */

    private void goAlipay(String orderInfo) {
        AlipayUtils.ALiPayBuilder builder = new AlipayUtils.ALiPayBuilder();
        builder.build().toALiPay(this, orderInfo);
    }

}
