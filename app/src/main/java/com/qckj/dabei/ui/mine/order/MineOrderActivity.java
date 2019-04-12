package com.qckj.dabei.ui.mine.order;

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
 * 我的订单界面
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineOrderActivity extends BaseActivity {

    @FindViewById(R.id.mine_release)
    private TextView mMineRelease;

    @FindViewById(R.id.mine_order)
    private TextView mMineOrder;

    @FindViewById(R.id.mine_trip)
    private TextView mMineTrip;

    @FindViewById(R.id.order_list_view_pager)
    private ViewPager mOrderListViewPager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineOrderActivity.class);
        context.startActivity(intent);
    }

    private SimpleOnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            super.onPageSelected(i);
            if (i == 0) {
                mMineRelease.setSelected(true);
                mMineOrder.setSelected(false);
                mMineTrip.setSelected(false);
            } else if (i == 1) {
                mMineRelease.setSelected(false);
                mMineOrder.setSelected(true);
                mMineTrip.setSelected(false);
            } else {
                mMineRelease.setSelected(false);
                mMineOrder.setSelected(false);
                mMineTrip.setSelected(true);
            }
        }
    };

    private FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MineReleaseFragment.newInstance();
            } else if (position == 1) {
                return MineOrderFragment.newInstance();
            } else {
                return MineReleaseFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        mMineRelease.setSelected(true);
        mMineOrder.setSelected(false);
        mMineTrip.setSelected(false);
        mOrderListViewPager.setAdapter(pagerAdapter);
        mOrderListViewPager.setCurrentItem(0);
        mOrderListViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initListener() {
        mMineRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderListViewPager.setCurrentItem(0);
            }
        });

        mMineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderListViewPager.setCurrentItem(1);
            }
        });
        mMineTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderListViewPager.setCurrentItem(2);
            }
        });

    }
}
