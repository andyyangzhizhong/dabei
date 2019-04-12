package com.qckj.dabei.ui.lib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.lib.GetDemandLibRequester;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求库内部fragment
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class DemandLibTableFragment extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    private View rootView;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView mListView;

    private String type;
    private String id;
    private UserLocationInfo userLocationInfo;
    private int curPage = 1;
    private DemandLibTableAdapter demandLibTableAdapter;

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
        if (mListView != null) {
            mListView.setPullRefreshEnable(true);
            mListView.setLoadMoreEnable(false);
            mListView.startPullRefresh();
            loadData(true);
        }
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
        demandLibTableAdapter = new DemandLibTableAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_demand_lib_table, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    private void initView() {
        mListView.setAdapter(demandLibTableAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setLoadMoreEnable(false);
        mListView.startPullRefresh();
        loadData(true);
    }

    private void initListener() {
        mListView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                loadData(true);
            }
        });

        mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        new GetDemandLibRequester(id, curPage, PAGE_SIZE, type, userLocationInfo.getLongitude() + "", userLocationInfo.getLatitude() + "",
                userLocationInfo.getCity(), userLocationInfo.getDistrict(), new OnHttpResponseCodeListener<List<DemandLibInfo>>() {

            @Override
            public void onHttpResponse(boolean isSuccess, List<DemandLibInfo> demandLibInfos, String message) {
                super.onHttpResponse(isSuccess, demandLibInfos, message);
                if (isSuccess) {
                    if (isRefresh) {
                        demandLibTableAdapter.setData(demandLibInfos);
                        mListView.stopPullRefresh();
                    } else {
                        demandLibTableAdapter.addData(demandLibInfos);
                        mListView.stopLoadMore();
                    }
                    if (demandLibInfos.size() == PAGE_SIZE) {
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
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
