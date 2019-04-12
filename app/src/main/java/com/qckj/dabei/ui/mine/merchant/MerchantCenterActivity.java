package com.qckj.dabei.ui.mine.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.image.CircleImageView;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * 商家中心
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class MerchantCenterActivity extends BaseActivity {

    @FindViewById(R.id.user_head_portrait)
    private CircleImageView mUserHeadPortrait;

    @FindViewById(R.id.user_name)
    private TextView mUserName;

    @FindViewById(R.id.is_auth_view)
    private ImageView isAuthView;

    @FindViewById(R.id.member_grade_view)
    private ImageView memberGradeView;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MerchantCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_center);
        ViewInject.inject(this);
        initView();
    }

    private void initView() {
        UserInfo userInfo = userManager.getUserInfo();
        isAuthView.setVisibility(View.GONE);
        memberGradeView.setVisibility(View.GONE);
        if (TextUtils.isEmpty(userInfo.getName())) {
            mUserName.setText(R.string.mine_app_user_name);
        } else {
            mUserName.setText(userInfo.getName());
        }
        GlideUtil.displayImage(getContext(), userInfo.getImgUrl(), mUserHeadPortrait, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }
}
