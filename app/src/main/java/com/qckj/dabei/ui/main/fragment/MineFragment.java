package com.qckj.dabei.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.mine.AboutUsActivity;
import com.qckj.dabei.ui.mine.comment.MineCommentActivity;
import com.qckj.dabei.ui.mine.complain.ComplainActivity;
import com.qckj.dabei.ui.mine.contact.EmergencyContactActivity;
import com.qckj.dabei.ui.mine.contact.EmergencyContactAddActivity;
import com.qckj.dabei.ui.mine.merchant.MerchantCenterActivity;
import com.qckj.dabei.ui.mine.msg.MineMessageActivity;
import com.qckj.dabei.ui.mine.order.MineOrderActivity;
import com.qckj.dabei.ui.mine.partner.JoinPartnerActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.ui.mine.user.UserInfoActivity;
import com.qckj.dabei.ui.mine.wallet.MineWalletActivity;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.dialog.AppShareDialog;
import com.qckj.dabei.view.image.CircleImageView;
import com.qckj.dabei.view.webview.BrowserActivity;

/**
 * 我的
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class MineFragment extends BaseFragment {

    @FindViewById(R.id.user_head_portrait)
    private CircleImageView mUserHeadPortrait;

    @FindViewById(R.id.user_name)
    private TextView mUserName;

    @FindViewById(R.id.is_auth_view)
    private ImageView isAuthView;

    @FindViewById(R.id.member_grade_view)
    private ImageView memberGradeView;

    @FindViewById(R.id.merchant_center_btn)
    private TextView mMerchantCenterBtn;

    @Manager
    private UserManager userManager;

    private UserManager.OnUserLoginListener onUserLoginListener = new UserManager.OnUserLoginListener() {
        @Override
        public void onLoginSuccess(UserInfo userInfo) {
            showUserInfo(userInfo);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ViewInject.inject(this, rootView);
        initData();
        initListener();
        return rootView;
    }

    private void initData() {
        showUserInfo(userManager.getUserInfo());
    }

    private void showUserInfo(UserInfo userInfo) {
        if (TextUtils.isEmpty(userInfo.getId())) {
            mUserName.setText(R.string.mine_login_reg);
            isAuthView.setVisibility(View.GONE);
            memberGradeView.setVisibility(View.GONE);
        } else {
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

    private void initListener() {
        userManager.addOnUserLoginListener(onUserLoginListener);
    }

    @OnClick({R.id.user_info_rl, R.id.merchant_center_btn, R.id.mine_order_view, R.id.mine_message_view,
            R.id.mine_earning_view, R.id.mine_join_partner_view,
            R.id.mine_evaluate_view, R.id.mine_emergency_contact_view, R.id.mine_complaint_feedback_view, R.id.mine_about_us_view})
    private void onViewClick(View view) {
        if (getActivity() == null) return;
        switch (view.getId()) {
            case R.id.user_info_rl:
                if (userManager.isLogin()) {
                    UserInfoActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.merchant_center_btn:
                // 商家中心
                MerchantCenterActivity.startActivity(getActivity());
                break;
            case R.id.mine_order_view:
                if (userManager.isLogin()) {
                    MineOrderActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_message_view:
                if (userManager.isLogin()) {
                    MineMessageActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_earning_view:
                if (userManager.isLogin()) {
                    MineWalletActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_evaluate_view:
                if (userManager.isLogin()) {
                    MineCommentActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_join_partner_view:
                if (userManager.isLogin()) {
                    JoinPartnerActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_emergency_contact_view:
                if (userManager.isLogin()) {
                    EmergencyContactActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_complaint_feedback_view:
                ComplainActivity.startActivity(getActivity());
                break;
            case R.id.mine_about_us_view:
                //AboutUsActivity.startActivity(getActivity());
                BrowserActivity.startActivity(getActivity(), "http://192.168.1.123:8091/merchant?shopId=20180806095026HqSkT8gK9kv96Fw7YQ&type=gold&userId=12312&poi=122,33", false);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userManager.removeOnUserLoginListener(onUserLoginListener);
    }
}
