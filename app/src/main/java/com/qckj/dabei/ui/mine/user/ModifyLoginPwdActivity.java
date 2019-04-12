package com.qckj.dabei.ui.mine.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * 修改登录密码接口
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyLoginPwdActivity extends BaseActivity {

    @FindViewById(R.id.input_old_password)
    private EditText mOldPwd;

    @FindViewById(R.id.input_new_password)
    private EditText mNewPwd;

    @FindViewById(R.id.input_confirm_new_password)
    private EditText mConfirmNewPwd;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ModifyLoginPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pwd);
        ViewInject.inject(this);
    }

    @OnClick(R.id.confirm_btn)
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn:
                modifyPwd();
                break;
        }
    }

    private void modifyPwd() {
        String oldPwd = mOldPwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();
        String conNewPwd = mConfirmNewPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            showToast("旧密码不能为空");
        } else if (TextUtils.isEmpty(newPwd)) {
            showToast("新密码不能为空");
        } else if (TextUtils.isEmpty(conNewPwd)) {
            showToast("确认新密码不能为空");
        } else if (!newPwd.equals(conNewPwd)) {
            showToast("两次新密码不一致");
        } else {
            modify(oldPwd, newPwd, conNewPwd);
        }
    }

    private void modify(String oldPwd, String newPwd, String conNewPwd) {
        showLoadingProgressDialog();
        userManager.modifyLoginPwd(oldPwd, newPwd, conNewPwd, new OnResultMessageListener() {
            @Override
            public void onResult(boolean isSuccess, String message) {
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    finish();
                } else {
                    showToast(message);
                }
            }
        });
    }
}
