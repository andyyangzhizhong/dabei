package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleOnPageChangeListener;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;

/**
 * 推荐服务界面
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class RecommendServiceActivity extends BaseActivity {

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_ID = "key_id";

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.enterprise_service)
    private TextView enterpriseService;

    @FindViewById(R.id.unity_service)
    private TextView unityService;

    @FindViewById(R.id.service_view_pager)
    private ViewPager serviceViewPager;
    private String id;

    public static void startActivity(Context context, String title, String id) {
        Intent intent = new Intent(context, RecommendServiceActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }


    private SimpleOnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            super.onPageSelected(i);
            if (i == 0) {
                enterpriseService.setSelected(true);
                unityService.setSelected(false);
            } else {
                enterpriseService.setSelected(false);
                unityService.setSelected(true);
            }
        }
    };

    private FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                EnterpriseServiceFragment enterpriseServiceFragment = EnterpriseServiceFragment.newInstance();
                enterpriseServiceFragment.setClassId(id);
                return enterpriseServiceFragment;
            } else {
                UnityServiceFragment unityServiceFragment = UnityServiceFragment.newInstance();
                unityServiceFragment.setClassId(id);
                return unityServiceFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_service);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        String title = getIntent().getStringExtra(KEY_TITLE);
        id = getIntent().getStringExtra(KEY_ID);
        actionBar.setTitleText(title);
        enterpriseService.setSelected(true);
        unityService.setSelected(false);
        serviceViewPager.setAdapter(pagerAdapter);
        serviceViewPager.setCurrentItem(0);
        serviceViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initListener() {
        enterpriseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceViewPager.setCurrentItem(0);
            }
        });

        unityService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceViewPager.setCurrentItem(1);
            }
        });
    }
}
