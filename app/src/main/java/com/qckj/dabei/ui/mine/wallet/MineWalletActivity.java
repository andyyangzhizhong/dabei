package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.GetWalletRequester;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.model.mine.WalletInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * 我的收益
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class MineWalletActivity extends BaseActivity {

    @FindViewById(R.id.beibi_num)
    private TextView beibiNum;

    @FindViewById(R.id.balance_num)
    private TextView balanceNum;

    @FindViewById(R.id.bank_num)
    private TextView bankNum;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineWalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        ViewInject.inject(this);
        initData();
    }

    private void initData() {
        getData();
    }

    @OnClick({R.id.beibi_ll, R.id.balance_ll, R.id.bank_ll})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.beibi_ll:
                showToast("此功能暂未开通，敬请期待！");
                break;
            case R.id.balance_ll:
                break;
            case R.id.bank_ll:
                break;
        }
    }

    private void getData() {
        showLoadingProgressDialog();
        new GetWalletRequester(userManager.getCurId(), new OnHttpResponseCodeListener<WalletInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, WalletInfo walletInfo, String message) {
                super.onHttpResponse(isSuccess, walletInfo, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    beibiNum.setText(walletInfo.getPoint());
                    balanceNum.setText(walletInfo.getBalance());
                    bankNum.setText(walletInfo.getCount());
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
