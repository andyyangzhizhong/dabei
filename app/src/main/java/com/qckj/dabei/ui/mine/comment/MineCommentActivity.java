package com.qckj.dabei.ui.mine.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.GetCommentRequester;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.manager.mine.msg.GetMessageRequester;
import com.qckj.dabei.model.comment.CommentInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.List;

/**
 * 我的评论
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class MineCommentActivity extends BaseActivity {

    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullListView;

    @Manager
    private UserManager userManager;

    private int curPage = 1;
    private CommentAdapter commentAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_comment);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        commentAdapter = new CommentAdapter(getActivity());
        pullListView.setAdapter(commentAdapter);
        pullListView.setPullRefreshEnable(true);
        pullListView.setLoadMoreEnable(false);
        pullListView.startPullRefresh();
        loadData(true);
    }

    private void initListener() {
        pullListView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                loadData(true);
            }
        });

        pullListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }


    private void loadData(boolean isRefresh) {
        new GetCommentRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<CommentInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<CommentInfo> commentInfos, String message) {
                super.onHttpResponse(isSuccess, commentInfos, message);
                if (isSuccess) {
                    if (isRefresh) {
                        commentAdapter.setData(commentInfos);
                    } else {
                        commentAdapter.addData(commentInfos);
                    }
                    if (commentInfos.size() == 10) {
                        pullListView.setLoadMoreEnable(true);
                        curPage++;
                    } else if (commentInfos.size() == 0) {
                        pullListView.setLoadMoreEnable(false);
                    } else {
                        pullListView.setLoadMoreEnable(false);
                    }
                } else {
                    showToast(message);
                    pullListView.setLoadMoreEnable(false);
                }
                if (isRefresh) {
                    pullListView.stopPullRefresh();
                } else {
                    pullListView.stopLoadMore();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                if (isRefresh) {
                    pullListView.stopPullRefresh();
                } else {
                    pullListView.stopLoadMore();
                }
            }
        }).doPost();
    }

}
