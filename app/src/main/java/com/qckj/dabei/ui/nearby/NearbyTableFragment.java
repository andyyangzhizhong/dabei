package com.qckj.dabei.ui.nearby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.nearby.GetNearbyNerchantListRequester;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.ui.main.adapter.NearbyGridViewAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangzhizhong on 2019/3/26.
 */
public class NearbyTableFragment extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.type_grid_view)
    private GridView mGridView;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView mListView;

    private UserLocationInfo userLocationInfo;
    private int curPage = 1;

    private String curId;

    // 原始数据
    List<HomeFunctionInfo.Category> categoryList = new ArrayList<>();
    // 收缩数据
    List<HomeFunctionInfo.Category> categoryShrinkList = new ArrayList<>();
    // 扩展数据
    List<HomeFunctionInfo.Category> categoryExtendList = new ArrayList<>();

    private List<HotMerchantInfo> nearbyMerchantInfoList = new ArrayList<>();

    private NearbyGridViewAdapter nearbyGridViewAdapter;
    private NearbyMerchantAdapter nearbyMerchantAdapter;
    private View rootView;

    public void setCategoryList(List<HomeFunctionInfo.Category> categoryList) {
        this.categoryList.clear();
        this.categoryList = categoryList;
    }

    public void setUserLocationInfo(UserLocationInfo userLocationInfo) {
        this.userLocationInfo = userLocationInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        nearbyGridViewAdapter = new NearbyGridViewAdapter(getContext());
        nearbyMerchantAdapter = new NearbyMerchantAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_nearby_table, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initListener() {
        nearbyGridViewAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeFunctionInfo.Category>() {
            @Override
            public void onAdapterItemClick(int position, HomeFunctionInfo.Category category) {
                if ("更多".equals(category.getClassName())) {
                    extend();
                } else if ("收起".equals(category.getClassName())) {
                    shrink();
                } else {
                    mListView.startPullRefresh();
                    loadData(true, category.getClassId());
                    curId = category.getClassId();
                }
            }
        });

        mListView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                loadData(true, curId);
            }
        });

        mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false, curId);
            }
        });
    }

    // 展开
    private void extend() {
        categoryExtendList.clear();
        categoryExtendList.addAll(categoryList);
        HomeFunctionInfo.Category cat = new HomeFunctionInfo.Category();
        cat.setClassName("收起");
        categoryExtendList.add(cat);
        nearbyGridViewAdapter.setData(categoryExtendList);
    }

    private void shrink() {
        categoryShrinkList.clear();
        categoryShrinkList.addAll(categoryList);
        HomeFunctionInfo.Category category = new HomeFunctionInfo.Category();
        category.setClassName("更多");
        categoryShrinkList.add(7, category);
        nearbyGridViewAdapter.setData(categoryShrinkList.subList(0, 8));
    }

    private void initData() {
        if (categoryList.size() > 8) {
            shrink();
        } else {
            nearbyGridViewAdapter.setData(categoryList);
        }
        loadData(true, curId);
    }

    private void initView() {
        curId = categoryList.get(0).getClassId();
        mGridView.setAdapter(nearbyGridViewAdapter);
        mListView.setAdapter(nearbyMerchantAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setLoadMoreEnable(false);
        mListView.startPullRefresh();
    }

    private void loadData(boolean isRefresh, String id) {
        new GetNearbyNerchantListRequester(PAGE_SIZE, curPage, id, userLocationInfo.getCity(), userLocationInfo.getDistrict(), "", "", "", new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if (isSuccess) {
                    if (isRefresh) {
                        nearbyMerchantAdapter.setData(hotMerchantInfos);
                        mListView.stopPullRefresh();
                    } else {
                        nearbyMerchantAdapter.addData(hotMerchantInfos);
                        mListView.stopLoadMore();
                    }
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        curPage++;
                        mListView.setLoadMoreEnable(true);
                    } else {
                        mListView.setLoadMoreEnable(false);
                    }
                } else {
                    if (isRefresh) {
                        mListView.stopPullRefresh();
                    } else {
                        mListView.stopLoadMore();
                    }
                    mListView.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                if (isRefresh) {
                    mListView.stopPullRefresh();
                } else {
                    mListView.stopLoadMore();
                }
            }
        }).doPost();
    }

}
