package com.qckj.dabei.ui.mine.msg;

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
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.manager.mine.msg.GetMessageRequester;
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
 * 系统消息
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class SystemMessageFragment extends BaseFragment {

    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    private boolean isFirst = true;
    private View rootView;

    @FindViewById(R.id.system_message_list)
    private PullRefreshView systemMessageList;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;

    private MessageAdapter messageAdapter;

    public static SystemMessageFragment newInstance() {
        return new SystemMessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.system_message_list, container, false);
        ViewInject.inject(this, rootView);
        initView();
        loadData(true);
        initListener();
        return rootView;
    }

    private void initListener() {
        systemMessageList.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                loadData(true);
            }
        });
        systemMessageList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        new GetMessageRequester(curPage, PAGE_SIZE, userManager.getCurId(), 2, new OnHttpResponseCodeListener<List<MessageInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MessageInfo> messageInfos, String message) {
                super.onHttpResponse(isSuccess, messageInfos, message);
                if (!isSuccess) {
                    ((BaseActivity) getActivity()).showToast(message);
                    return;
                }
                if (isRefresh) {
                    messageAdapter.setData(messageInfos);
                } else {
                    messageAdapter.addData(messageInfos);
                }
                if (messageInfos.size() == 10) {
                    systemMessageList.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (messageInfos.size() == 0) {
                    noRecord.setVisibility(View.VISIBLE);
                    systemMessageList.setLoadMoreEnable(false);
                } else {
                    noRecord.setVisibility(View.GONE);
                    systemMessageList.setLoadMoreEnable(false);
                }
                if (isRefresh) {
                    systemMessageList.stopPullRefresh();
                } else {
                    systemMessageList.stopLoadMore();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                if (isRefresh) {
                    systemMessageList.stopPullRefresh();
                } else {
                    systemMessageList.stopLoadMore();
                }
            }
        }).doPost();
    }

    private void initView() {
        messageAdapter = new MessageAdapter(getContext());
        systemMessageList.setAdapter(messageAdapter);
        systemMessageList.setPullRefreshEnable(true);
        systemMessageList.setLoadMoreEnable(false);
        if (isFirst) {
            systemMessageList.startPullRefresh();
            isFirst = false;
        }
    }


}
