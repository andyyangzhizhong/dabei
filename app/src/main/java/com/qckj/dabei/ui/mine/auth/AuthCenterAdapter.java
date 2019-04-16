package com.qckj.dabei.ui.mine.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.AuthInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 认证中心适配器
 * <p>
 * Created by yangzhizhong on 2019/4/16.
 */
public class AuthCenterAdapter extends SimpleBaseAdapter<AuthInfo, AuthCenterAdapter.ViewHolder> {

    public AuthCenterAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.auth_center_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, AuthInfo authInfo, int position) {
        GlideUtil.displayImage(context, authInfo.getImgUrl(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.title.setText(authInfo.getName());
        viewHolder.content.setText(authInfo.getContent());
        int authType = authInfo.getAuthType();
        switch (authType) {
            case AuthInfo.AUTH_STATE_ING:
                viewHolder.authStateBtn.setText("认证中");
                break;
            case AuthInfo.AUTH_STATE_UN:
                viewHolder.authStateBtn.setText("未认证");
                break;
            case AuthInfo.AUTH_STATE:
                viewHolder.authStateBtn.setText("已认证");
                break;
        }
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private ImageView icon;

        @FindViewById(R.id.title)
        private TextView title;

        @FindViewById(R.id.content)
        private TextView content;

        @FindViewById(R.id.auth_state_btn)
        private Button authStateBtn;
    }
}
