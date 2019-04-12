package com.qckj.dabei.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.GetRecommendServiceRequester;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.manager.mine.msg.GetMessageRequester;
import com.qckj.dabei.model.home.RecommendServiceInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.ui.mine.msg.adapter.MessageAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.List;

/**
 * 个体服务
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class UnityServiceFragment extends BaseFragment {

    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    private boolean isFirst = true;
    private View rootView;

    @FindViewById(R.id.service_list_view)
    private PullRefreshView serviceList;

    @FindViewById(R.id.no_data)
    private TextView noRecord;

    @Manager
    private UserManager userManager;

    private ServiceAdapter serviceAdapter;

    private String classId;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public static UnityServiceFragment newInstance() {
        return new UnityServiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.service_list, container, false);
        ViewInject.inject(this, rootView);
        initView();
        loadData(true);
        initListener();
        return rootView;
    }

    private void initListener() {
        serviceList.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                loadData(true);
            }
        });
        serviceList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        new GetRecommendServiceRequester(classId, curPage, PAGE_SIZE, "2", new OnHttpResponseCodeListener<List<RecommendServiceInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<RecommendServiceInfo> recommendServiceInfos, String message) {
                super.onHttpResponse(isSuccess, recommendServiceInfos, message);
                if (isSuccess) {
                    if (isRefresh) {
                        serviceAdapter.setData(recommendServiceInfos);
                    } else {
                        serviceAdapter.addData(recommendServiceInfos);
                    }
                    if (recommendServiceInfos.size() == 10) {
                        serviceList.setLoadMoreEnable(true);
                        noRecord.setVisibility(View.GONE);
                        curPage++;
                    } else if (recommendServiceInfos.size() == 0) {
                        noRecord.setVisibility(View.VISIBLE);
                        serviceList.setLoadMoreEnable(false);
                    } else {
                        noRecord.setVisibility(View.GONE);
                        serviceList.setLoadMoreEnable(false);
                    }
                } else {
                    if (getActivity() != null) {
                        ((BaseActivity) getActivity()).showToast(message);
                    }
                    serviceList.setLoadMoreEnable(false);
                }
                if (isRefresh) {
                    serviceList.stopPullRefresh();
                } else {
                    serviceList.stopLoadMore();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                if (isRefresh) {
                    serviceList.stopPullRefresh();
                } else {
                    serviceList.stopLoadMore();
                }
            }
        }).doPost();
    }

    private void initView() {
        serviceAdapter = new ServiceAdapter(getContext());
        serviceList.setAdapter(serviceAdapter);
        serviceList.setPullRefreshEnable(true);
        serviceList.setLoadMoreEnable(false);
        if (isFirst) {
            serviceList.startPullRefresh();
            isFirst = false;
        }
    }

}
