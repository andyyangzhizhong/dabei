package com.qckj.dabei.ui.nearby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.util.inject.FindViewById;


/**
 * 附近商家适配器
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class NearbyMerchantAdapter extends SimpleBaseAdapter<HotMerchantInfo, NearbyMerchantAdapter.ViewHolder> {

    public NearbyMerchantAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.nearby_merchant_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HotMerchantInfo hotMerchantInfo, int position) {
        Glide.with(context).load(hotMerchantInfo.getImgUrl()).into(viewHolder.mNearbyMerchantIcon);
        viewHolder.mNearbyMerchantDpName.setText(hotMerchantInfo.getDpName());
        viewHolder.mNearbyMerchantDistance.setText(hotMerchantInfo.getDistance());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.nearby_merchant_icon)
        private ImageView mNearbyMerchantIcon;

        @FindViewById(R.id.nearby_merchant_dp_name)
        private TextView mNearbyMerchantDpName;

        @FindViewById(R.id.nearby_merchant_distance)
        private TextView mNearbyMerchantDistance;

        @FindViewById(R.id.nearby_merchant_relation)
        private TextView mNearbyMerchantRelationBtn;

    }
}
