package com.qckj.dabei.ui.release;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.manager.release.SubmitDemandRequester;
import com.qckj.dabei.model.release.DemandOrderInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;

/**
 * 需求描述
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class DemandDescribeActivity extends BaseActivity {

    public static final String KEY_DEMAND_TYPE = "demand_type";
    public static final String KEY_CLASS_ID = "key_class_id";

    @FindViewById(R.id.input_describe)
    private EditText inputDescribe;

    @FindViewById(R.id.input_address)
    private EditText inputAddress;

    @FindViewById(R.id.input_money)
    private EditText inputMoney;

    @FindViewById(R.id.pay_rg)
    private RadioGroup payRg;

    @FindViewById(R.id.no_pay_rb)
    private RadioButton noPayRb;

    @FindViewById(R.id.pay_rb)
    private RadioButton payRb;

    @FindViewById(R.id.demand_type)
    private CommonItemView mDemandTypeView;

    @FindViewById(R.id.commit_demand)
    private Button commitDemand;

    @Manager
    private UserManager userManager;

    private int isPay = 2;
    private String classId;

    public static void startActivity(Context context, String demandType, String classId) {
        Intent intent = new Intent(context, DemandDescribeActivity.class);
        intent.putExtra(KEY_DEMAND_TYPE, demandType);
        intent.putExtra(KEY_CLASS_ID, classId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_describe);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void initListener() {
        payRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (noPayRb.getId() == checkedId) {
                    isPay = 2;
                    noPayRb.setTextColor(getActivity().getResources().getColor(R.color.bg_yellow_end));
                    payRb.setTextColor(getActivity().getResources().getColor(R.color.black));
                } else {
                    isPay = 1;
                    payRb.setTextColor(getActivity().getResources().getColor(R.color.bg_yellow_end));
                    noPayRb.setTextColor(getActivity().getResources().getColor(R.color.black));
                }
            }
        });
        commitDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitDemandInfo();
            }
        });
    }

    private void commitDemandInfo() {
        String des = inputDescribe.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String money = inputMoney.getText().toString().trim();

        if (TextUtils.isEmpty(des)) {
            showToast("需求描述不能为空");
        } else if (TextUtils.isEmpty(address)) {
            showToast("地址不能为空");
        } else if (TextUtils.isEmpty(money)) {
            showToast("赏金不能为空");
        } else {
            showLoadingProgressDialog();
            new SubmitDemandRequester(userManager.getCurId(), isPay, classId, money, address, des, "",
                    userManager.getUserInfo().getAccount(), "", "",
                    new OnHttpResponseCodeListener<DemandOrderInfo>() {
                        @Override
                        public void onHttpResponse(boolean isSuccess, DemandOrderInfo demandOrderInfo, String message) {
                            super.onHttpResponse(isSuccess, demandOrderInfo, message);
                            dismissLoadingProgressDialog();
                            if (isSuccess) {
                                    if (isPay == 2){

                                    } else {
                                        PayActivity.startActivity(getActivity(),demandOrderInfo);
                                    }
                            } else {
                                showToast(message);
                            }
                        }

                        @Override
                        public void onLocalErrorResponse(int code) {
                            super.onLocalErrorResponse(code);
                            dismissLoadingProgressDialog();
                        }
                    }).doPost();
        }


    }

    private void init() {
        String demandType = getIntent().getStringExtra(KEY_DEMAND_TYPE);
        classId = getIntent().getStringExtra(KEY_CLASS_ID);
        mDemandTypeView.setContent(demandType);
    }
}
