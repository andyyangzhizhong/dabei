package com.qckj.dabei.ui.main.homesub.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.ui.home.RecommendServiceActivity;
import com.qckj.dabei.ui.main.homesub.adapter.HomeBoutiqueRecommendAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ScrollGridLayoutManager;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBoutiqueRecommendView extends LinearLayout {

    @FindViewById(R.id.view_home_boutique_recommend_recycler_view)
    private RecyclerView mRecyclerView;
    private HomeBoutiqueRecommendAdapter homeBoutiqueRecommendAdapter;

    public void setHomeBoutiqueRecommendInfo(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        homeBoutiqueRecommendAdapter.setHomeBoutiqueRecommendInfos(homeBoutiqueRecommendInfos);
    }

    public HomeBoutiqueRecommendView(Context context) {
        this(context, null);
    }

    public HomeBoutiqueRecommendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBoutiqueRecommendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
        initListener();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_boutique_recommend, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
    }

    private void initData() {
        ScrollGridLayoutManager gridLayoutManager = new ScrollGridLayoutManager(getContext(), 2);
        gridLayoutManager.setScrollEnable(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        homeBoutiqueRecommendAdapter = new HomeBoutiqueRecommendAdapter(getContext());
        mRecyclerView.setAdapter(homeBoutiqueRecommendAdapter);
    }

    private void initListener() {
        homeBoutiqueRecommendAdapter.setOnItemClickListener(new HomeBoutiqueRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HomeBoutiqueRecommendInfo info) {
                RecommendServiceActivity.startActivity(getContext(), info.getCategoryName(), info.getId());
            }
        });
    }
}
