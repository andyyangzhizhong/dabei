package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.GetStoreDetailRequester;
import com.qckj.dabei.model.home.StoreDetailInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * 店铺详情
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class StoreDetailActivity extends BaseActivity {

    public static final String KEY_ID = "key_id";

    @FindViewById(R.id.store_icon)
    private ImageView storeIcon;

    @FindViewById(R.id.store_name)
    private TextView storeName;

    @FindViewById(R.id.address)
    private TextView address;

    @FindViewById(R.id.distance)
    private TextView distance;

    @FindViewById(R.id.go_other)
    private TextView goOther;

    @FindViewById(R.id.store_service_btn)
    private TextView storeServiceBtn;

    @FindViewById(R.id.store_detail_btn)
    private TextView storeDetailBtn;

    @FindViewById(R.id.service_list)
    private ListView serviceList;

    @FindViewById(R.id.store_detail)
    private TextView storeDetail;

    @FindViewById(R.id.call_store)
    private LinearLayout callStore;

    private ServiceListAdapter serviceListAdapter;
    private StoreDetailInfo mStoreDetailInfo;

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, StoreDetailActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ViewInject.inject(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        storeServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeServiceBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
                storeServiceBtn.setTextSize(16);
                storeDetailBtn.setTextColor(getActivity().getResources().getColor(R.color.gray));
                storeDetailBtn.setTextSize(14);
                if (mStoreDetailInfo != null) {
                    if (mStoreDetailInfo.getServiceInfos().size() > 0) {
                        serviceList.setVisibility(View.VISIBLE);
                        storeDetail.setVisibility(View.GONE);
                    } else {
                        serviceList.setVisibility(View.GONE);
                        storeDetail.setVisibility(View.VISIBLE);
                        storeDetail.setText("暂无服务");
                    }
                }
            }
        });

        storeDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeServiceBtn.setTextColor(getActivity().getResources().getColor(R.color.gray));
                storeServiceBtn.setTextSize(14);
                storeDetailBtn.setTextColor(getActivity().getResources().getColor(R.color.black));
                storeDetailBtn.setTextSize(16);
                serviceList.setVisibility(View.GONE);
                storeDetail.setVisibility(View.VISIBLE);
                storeDetail.setText("暂无详情");
            }
        });
    }

    private void initView() {
        serviceListAdapter = new ServiceListAdapter(getActivity());
        serviceList.setAdapter(serviceListAdapter);
    }

    private void initData() {
        String id = getIntent().getStringExtra(KEY_ID);
        showLoadingProgressDialog();
        new GetStoreDetailRequester(id, "", "", new OnHttpResponseCodeListener<StoreDetailInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, StoreDetailInfo storeDetailInfo, String message) {
                super.onHttpResponse(isSuccess, storeDetailInfo, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    showData(storeDetailInfo);
                    mStoreDetailInfo = storeDetailInfo;
                } else {
                    showToast(message);
                    finish();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dismissLoadingProgressDialog();
                finish();
            }
        }).doPost();

    }

    private void showData(StoreDetailInfo storeDetailInfo) {
        GlideUtil.displayImage(getActivity(), storeDetailInfo.getImgUrl(), storeIcon, R.mipmap.ic_empty_data);
        storeName.setText(storeDetailInfo.getName());
        address.setText(storeDetailInfo.getAddress());
        distance.setText(storeDetailInfo.getDistance());
        if (storeDetailInfo.getServiceInfos().size() > 0) {
            serviceListAdapter.setData(storeDetailInfo.getServiceInfos());
            serviceList.setVisibility(View.VISIBLE);
            storeDetail.setVisibility(View.GONE);
        } else {
            serviceList.setVisibility(View.generateViewId());
            storeDetail.setVisibility(View.VISIBLE);
            storeDetail.setText("暂无服务");
        }
    }
}
