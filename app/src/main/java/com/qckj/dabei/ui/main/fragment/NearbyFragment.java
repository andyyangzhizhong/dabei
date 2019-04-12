package com.qckj.dabei.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleOnPageChangeListener;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.main.adapter.NearbyTabListAdapter;
import com.qckj.dabei.ui.nearby.NearbyTableFragment;
import com.qckj.dabei.ui.nearby.TableViewPagerAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.NestScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class NearbyFragment extends BaseFragment {

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private UserLocationInfo mUserLocationInfo;

    @FindViewById(R.id.cur_location)
    private TextView mCurLocation;

    @FindViewById(R.id.nearby_search)
    private RelativeLayout mNearbySearch;

    @FindViewById(R.id.tab_list)
    private RecyclerView mTabList;

    @FindViewById(R.id.view_pager)
    private NestScrollViewPager mViewPager;

    @Manager
    private HomeDataManager homeDataManager;

    @Manager
    private CacheManager cacheManager;

    private int lastCur = 0;

    private NearbyTabListAdapter nearbyTabListAdapter;
    private List<BaseFragment> tabList = new ArrayList<>();
    private List<HomeFunctionInfo> mHomeFunctionInfos;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mHomeFunctionInfos = cacheManager.getList(CacheManager.KEY_HOME_FUNCTION_INFO, HomeFunctionInfo.class);
        UserLocationInfo userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        if (userLocationInfo != null) {
            if (TextUtils.isEmpty(userLocationInfo.getCity())) {
                userLocationInfo.setCity("贵阳市");
                userLocationInfo.setDistrict("南明区");
            }
            mUserLocationInfo = userLocationInfo;
        } else {
            UserLocationInfo info = new UserLocationInfo();
            info.setCity("贵阳市");
            info.setDistrict("南明区");
            mUserLocationInfo = info;
        }
        nearbyTabListAdapter = new NearbyTabListAdapter(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void refreshData() {
        if (mHomeFunctionInfos != null && mHomeFunctionInfos.size() > 0) {
            nearbyTabListAdapter.setInfos(mHomeFunctionInfos);
            initTab(mHomeFunctionInfos);
        }
    }

    private void initListener() {
        nearbyTabListAdapter.setOnTabListClickListener((position, info) -> {
            nearbyTabListAdapter.setCurPosition(position);
            mViewPager.setCurrentItem(position);
        });
        mViewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                nearbyTabListAdapter.setCurPosition(i);
                scrollTab(i);
            }
        });
    }

    // 滑动tab
    private void scrollTab(int position) {
        if (position > lastCur) {
            mTabList.scrollToPosition(position + 1);
        } else {
            if (position >= 1) {
                mTabList.scrollToPosition(position - 1);
            } else {
                mTabList.scrollToPosition(position);
            }
        }
        lastCur = position;
    }

    private void initData() {
        homeDataManager.getHomeFunctionInfo(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                mHomeFunctionInfos = homeFunctionInfos;
                if (isSuccess) {
                    refreshData();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        });
    }

    private void initTab(List<HomeFunctionInfo> homeFunctionInfos) {
        for (HomeFunctionInfo info : homeFunctionInfos) {
            NearbyTableFragment nearbyTableFragment = new NearbyTableFragment();
            nearbyTableFragment.setCategoryList(info.getCategoryList());
            nearbyTableFragment.setUserLocationInfo(mUserLocationInfo);
            tabList.add(nearbyTableFragment);
        }
        TableViewPagerAdapter tableViewPagerAdapter = new TableViewPagerAdapter(getFragmentManager(), tabList);
        mViewPager.setAdapter(tableViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTabList.setLayoutManager(linearLayoutManager);
        mCurLocation.setText(mUserLocationInfo.getDistrict());
        mTabList.setAdapter(nearbyTabListAdapter);
        refreshData();
    }
}
