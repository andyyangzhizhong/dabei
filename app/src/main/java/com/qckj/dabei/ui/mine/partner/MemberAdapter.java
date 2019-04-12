package com.qckj.dabei.ui.mine.partner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 介入合伙人条目
 * <p>
 * Created by yangzhizhong on 2019/3/26.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;
    private List<MemberInfo> infos;
    private int curPosition = 0;
    private OnItemClickListener onItemClickListener;
    private boolean isReload = true;

    public MemberAdapter(Context context) {
        this.context = context;
        infos = new ArrayList<>();
    }

    public void setInfos(List<MemberInfo> infos) {
        this.infos.clear();
        this.infos = infos;
        notifyDataSetChanged();
    }

    public void setCurPosition(int position) {
        if (position == curPosition) {
            return;
        }
        isReload = false;
        this.curPosition = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.member_item_view, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberInfo memberInfo = infos.get(position);
        if (position == curPosition) {
            holder.memberItemView.setSelected(true);
        } else {
            holder.memberItemView.setSelected(false);
        }
        holder.memberItemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, memberInfo);
            }
        });
        if (isReload) {
            GlideUtil.displayImage(context, memberInfo.getMemberLogo(), holder.icon, R.mipmap.ic_empty_data);
            holder.title.setText(memberInfo.getMemberName() + " ￥" + memberInfo.getMemberPrice());
            holder.content.setText(memberInfo.getMemberIntroduce());
        }
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @FindViewById(R.id.member_item_view)
        private RelativeLayout memberItemView;

        @FindViewById(R.id.icon)
        private ImageView icon;

        @FindViewById(R.id.title)
        private TextView title;

        @FindViewById(R.id.content)
        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewInject.inject(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, MemberInfo info);
    }

}
