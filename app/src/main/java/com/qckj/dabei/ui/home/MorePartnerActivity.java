package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.PullRefreshView;

/**
 * 更多合作商
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class MorePartnerActivity extends BaseActivity {

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView mPullListView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MorePartnerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_partner);
        ViewInject.inject(this);
    }
}
