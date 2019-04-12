package com.qckj.dabei.ui.mine.msg;

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

/**
 * 我的消息界面
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineMessageActivity extends BaseActivity {

    @FindViewById(R.id.mine_message)
    private TextView mineMessage;

    @FindViewById(R.id.system_message)
    private TextView systemMessage;

    @FindViewById(R.id.message_list_view_pager)
    private ViewPager mMessageViewPager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineMessageActivity.class);
        context.startActivity(intent);
    }

    private SimpleOnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            super.onPageSelected(i);
            if (i == 0) {
                mineMessage.setSelected(true);
                systemMessage.setSelected(false);
            } else {
                mineMessage.setSelected(false);
                systemMessage.setSelected(true);
            }
        }
    };

    private FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MineMessageFragment.newInstance();
            } else {
                return SystemMessageFragment.newInstance();
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
        setContentView(R.layout.activity_mine_message);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        mineMessage.setSelected(true);
        systemMessage.setSelected(false);
        mMessageViewPager.setAdapter(pagerAdapter);
        mMessageViewPager.setCurrentItem(0);
        mMessageViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initListener() {
        mineMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageViewPager.setCurrentItem(0);
            }
        });

        systemMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageViewPager.setCurrentItem(1);
            }
        });
    }
}
