package com.qckj.dabei.ui.mine.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.model.mine.AuthInfo;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证中心界面
 * <p>
 * Created by yangzhizhong on 2019/4/16.
 */
public class AuthCenterActivity extends BaseActivity {

    @FindViewById(R.id.auth_list_view)
    private ListView authListView;
    private AuthCenterAdapter authCenterAdapter;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,AuthCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_center);
        ViewInject.inject(this);
        initView();
        initData();
    }

    private void initView() {
        authCenterAdapter = new AuthCenterAdapter(getActivity());
        authListView.setAdapter(authCenterAdapter);
    }

    private void initData() {
        List<AuthInfo> authInfos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AuthInfo authInfo = new AuthInfo();
            authInfo.setImgUrl("");
            authInfo.setName("认证中心" + i);
            authInfo.setContent("认证中心内容认证中心内容认证中心内容认证中心内容");
            authInfo.setAuthType(i);
            authInfos.add(authInfo);
        }
        authCenterAdapter.setData(authInfos);
    }
}
