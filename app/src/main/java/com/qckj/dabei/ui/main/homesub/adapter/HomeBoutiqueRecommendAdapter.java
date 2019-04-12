package com.qckj.dabei.ui.main.homesub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 精品推荐适配器
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBoutiqueRecommendAdapter extends RecyclerView.Adapter<HomeBoutiqueRecommendAdapter.HomeBoutiqueRecommendViewHolder> {

    private Context context;
    private List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos;
    private OnItemClickListener onItemClickListener;

    public HomeBoutiqueRecommendAdapter(Context context) {
        this.context = context;
        homeBoutiqueRecommendInfos = new ArrayList<>();
    }

    public void setHomeBoutiqueRecommendInfos(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        this.homeBoutiqueRecommendInfos.clear();
        this.homeBoutiqueRecommendInfos = homeBoutiqueRecommendInfos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HomeBoutiqueRecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_boutique_recommend_item_view, parent, false);
        return new HomeBoutiqueRecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBoutiqueRecommendViewHolder holder, int position) {
        HomeBoutiqueRecommendInfo homeBoutiqueRecommendInfo = homeBoutiqueRecommendInfos.get(position);
        holder.functionName.setText(homeBoutiqueRecommendInfo.getCategoryName());
        switch (position) {
            case 0:
                holder.functionIcon.setBackgroundResource(R.mipmap.ic_car);
                break;
            case 1:
                holder.functionIcon.setBackgroundResource(R.mipmap.ic_eat);
                break;
            case 2:
                holder.functionIcon.setBackgroundResource(R.mipmap.ic_appliance);
                break;
            case 3:
                holder.functionIcon.setBackgroundResource(R.mipmap.ic_daily);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(homeBoutiqueRecommendInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeBoutiqueRecommendInfos.size();
    }

    static class HomeBoutiqueRecommendViewHolder extends RecyclerView.ViewHolder {

        ImageView functionIcon;
        TextView functionName;

        HomeBoutiqueRecommendViewHolder(@NonNull View itemView) {
            super(itemView);
            functionIcon = itemView.findViewById(R.id.function_icon);
            functionName = itemView.findViewById(R.id.function_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(HomeBoutiqueRecommendInfo info);
    }


}
