package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.ui.main.homesub.view.HomeBoutiqueRecommendView;

import java.util.List;

/**
 * 首页推荐信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBoutiqueRecommendContent extends HomeBaseContent<List<HomeBoutiqueRecommendInfo>> {

    private HomeBoutiqueRecommendView homeBoutiqueRecommendView;

    public HomeBoutiqueRecommendContent(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        super(homeBoutiqueRecommendInfos);
    }

    @Override
    public View onCreateSubView(Context context) {
        homeBoutiqueRecommendView = new HomeBoutiqueRecommendView(context);
        onRefreshView(getData());
        return homeBoutiqueRecommendView;
    }

    @Override
    public void onDestroySubView() {

    }

    @Override
    public void onRefreshView(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        homeBoutiqueRecommendView.setHomeBoutiqueRecommendInfo(homeBoutiqueRecommendInfos);
    }
}
