package com.qckj.dabei.ui.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.model.HomeBannerInfo;
import com.qckj.dabei.view.banner.AutoBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeBannerAdapter extends AutoBannerAdapter {

    private Context context;
    private List<HomeBannerInfo> homeBannerInfoList;
    private OnBannerClickListener onBannerClickListener;

    public HomeBannerAdapter(Context context) {
        this.context = context;
        homeBannerInfoList = new ArrayList<>();
    }

    public void changeItems(List<HomeBannerInfo> bannerInfoList) {
        this.homeBannerInfoList.clear();
        this.homeBannerInfoList.addAll(bannerInfoList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return homeBannerInfoList.size();
    }

    @Override
    protected HomeBannerInfo getItem(int position) {
        return homeBannerInfoList.get(position);
    }

    @Override
    public View getView(View convertView, final int position) {
        ImageView imageView;
        if (convertView != null) {
            imageView = (ImageView) convertView;
        } else {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
        Glide.with(context).load(homeBannerInfoList.get(position).getImgUrl()).into(imageView);
        imageView.setOnClickListener(view -> {
            if (onBannerClickListener != null) {
                onBannerClickListener.onClick(homeBannerInfoList.get(position));
            }
        });
        return imageView;
    }

    /**
     * 设置banner点击事件
     */
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }


    public interface OnBannerClickListener {
        void onClick(HomeBannerInfo info);
    }
}