package com.qckj.dabei.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.StoreDetailInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 店铺详情服务列表
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class ServiceListAdapter extends SimpleBaseAdapter<StoreDetailInfo.ServiceInfo, ServiceListAdapter.ViewHolder> {

    public ServiceListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.service_list_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, StoreDetailInfo.ServiceInfo serviceInfo, int position) {
        GlideUtil.displayImage(context, serviceInfo.getImgUrl(), viewHolder.icon, R.mipmap.ic_empty_data);
        viewHolder.serviceName.setText(serviceInfo.getName());
        viewHolder.servicePrice.setText("￥" + serviceInfo.getMoney());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private ImageView icon;

        @FindViewById(R.id.service_name)
        private TextView serviceName;

        @FindViewById(R.id.service_price)
        private TextView servicePrice;
    }
}
