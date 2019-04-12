package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.ThirdLoginRequester;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * 登录界面
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class LoginActivity extends BaseActivity {

    @FindViewById(R.id.other_login_view)
    private LinearLayout otherLoginView;

    @Manager
    private UserManager userManager;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.overridePendingTransition(R.anim.move_bottom_in_avtivity, R.anim.fade_out_activity);
    }

    private UserManager.OnUserLoginListener onUserLoginListener = new UserManager.OnUserLoginListener() {
        @Override
        public void onLoginSuccess(UserInfo userInfo) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewInject.inject(this);
        userManager.addOnUserLoginListener(onUserLoginListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userManager.removeOnUserLoginListener(onUserLoginListener);
    }

    @OnClick({R.id.login_rl, R.id.mine_go_go, R.id.login_account_password, R.id.login_verification_code,
            R.id.other_login_ll, R.id.qq_login_ll, R.id.wx_login_ll, R.id.cancel})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.login_rl:
                isDealOtherLoginView();
                break;
            case R.id.mine_go_go:
                if (!isDealOtherLoginView()) {
                    finish();
                }
                break;
            case R.id.login_account_password:
                if (!isDealOtherLoginView()) {
                    // 跳转账号登录界面
                    AccountLoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.login_verification_code:
                if (!isDealOtherLoginView()) {
                    // 跳转验证码登录界面
                    VerificationCodeLoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.other_login_ll:
                if (!isDealOtherLoginView()) {
                    otherLoginView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.qq_login_ll:
                loginThirdPlatform(SHARE_MEDIA.QQ);
                break;
            case R.id.wx_login_ll:
                loginThirdPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.cancel:
                otherLoginView.setVisibility(View.GONE);
                break;
        }
    }

    private void loginThirdPlatform(SHARE_MEDIA shareMedia) {
        UMLoginApp.loginApp(getActivity(), shareMedia, new OnResult<Map<String, String>>() {
            @Override
            public void onResult(boolean isSuccess, Map<String, String> map) {
                otherLoginView.setVisibility(View.GONE);
                if (isSuccess) {
                    String openid = map.get("openid");
                    showLoadingProgressDialog();
                    userManager.loginThirdPlatform(shareMedia, openid, new OnResultMessageListener() {
                        @Override
                        public void onResult(boolean isSuccess, String message) {
                            dismissLoadingProgressDialog();
                            if (!isSuccess) {
                                if (UserManager.UN_BIND_PHONE.equals(message)) {
                                    BindPhoneActivity.startActivity(getActivity(), openid);
                                } else {
                                    showToast(message);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void login(SHARE_MEDIA shareMedia, String openid) {
        showLoadingProgressDialog();
        new ThirdLoginRequester(shareMedia == SHARE_MEDIA.QQ ? ThirdLoginRequester.QQ_PLATFORM : ThirdLoginRequester.WEXIN_PLATFORM, openid, new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
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


    private boolean isDealOtherLoginView() {
        if (otherLoginView.getVisibility() == View.VISIBLE) {
            otherLoginView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
