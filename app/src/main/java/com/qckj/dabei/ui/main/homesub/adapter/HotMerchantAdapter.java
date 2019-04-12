package com.qckj.dabei.ui.main.homesub.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.util.inject.FindViewById;


/**
 * 热门商家适配器
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HotMerchantAdapter extends SimpleBaseAdapter<HotMerchantInfo, HotMerchantAdapter.ViewHolder> {

    public HotMerchantAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.hot_merchant_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HotMerchantInfo hotMerchantInfo, int position) {
        Glide.with(context).load(hotMerchantInfo.getImgUrl()).into(viewHolder.mHotMerchantIcon);
        viewHolder.mHotMerchantDpName.setText(hotMerchantInfo.getDpName());
        viewHolder.mHotMerchantName.setText(hotMerchantInfo.getName());
        viewHolder.mHotMerchantDistance.setText(hotMerchantInfo.getDistance());

        viewHolder.mHotMerchantRelationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = hotMerchantInfo.getPhone();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "未预留电话", Toast.LENGTH_SHORT).show();
                } else {
                    showPhoneDialog(phone);
                }
            }
        });
    }

    private void showPhoneDialog(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage("联系电话: " + phone);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callPhone(phone);
            }
        });
        builder.show();
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        getContext().startActivity(intent);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.hot_merchant_icon)
        private ImageView mHotMerchantIcon;

        @FindViewById(R.id.hot_merchant_dp_name)
        private TextView mHotMerchantDpName;

        @FindViewById(R.id.hot_merchant_name)
        private TextView mHotMerchantName;

        @FindViewById(R.id.hot_merchant_distance)
        private TextView mHotMerchantDistance;

        @FindViewById(R.id.hot_merchant_relation)
        private TextView mHotMerchantRelationBtn;

    }
}
