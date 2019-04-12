package com.qckj.dabei.ui.main.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResultListener;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.HomeBannerInfo;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.ui.home.ErCodeScanActivity;
import com.qckj.dabei.ui.home.StoreDetailActivity;
import com.qckj.dabei.ui.main.adapter.HomeBannerAdapter;
import com.qckj.dabei.ui.main.homesub.HomeBaseContent;
import com.qckj.dabei.ui.main.homesub.HomeBoutiqueRecommendContent;
import com.qckj.dabei.ui.main.homesub.HomeBrandPartnerContent;
import com.qckj.dabei.ui.main.homesub.HomeFunctionContent;
import com.qckj.dabei.ui.main.homesub.HomeTransactionContent;
import com.qckj.dabei.ui.main.homesub.adapter.HotMerchantAdapter;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.banner.AutoBannerView;
import com.qckj.dabei.view.listview.EmptyAdapter;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 主页
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class HomeFragment extends BaseFragment {

    public static final int KEY_HOME_FUNCTION = 1;
    public static final int KEY_HOME_TRANSACTION = 2;
    public static final int KEY_HOME_BRAND_PARTNER = 3;
    public static final int KEY_HOME_BOUTIQUE_RECOMMEND = 4;

    public static final int PAGE_SIZE = 10;

    private ViewGroup rootView;
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.sub_view_contain)
    private LinearLayout mSubViewContain;

    @FindViewById(R.id.banner_view)
    private AutoBannerView mBannerView;

    @Manager
    private HomeDataManager mHomeDataManager;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private HomeBannerAdapter homeBannerAdapter;
    private SparseArray<HomeBaseContent> baseContentSparseArray = new SparseArray<>();
    private ArrayList<HomeBaseContent> baseContentList = new ArrayList<>();
    private HotMerchantAdapter hotMerchantAdapter;

    private UserLocationInfo mUserLocationInfo;

    private int curPage = 1;
    private List<HotMerchantInfo> hotMerchantInfoList = new ArrayList<>();
    private ImageView mErCodeScan;
    private TextView mCurLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        if (gaoDeLocationManager.checkLocationOpen(getContext())) {
            updateLocationInfo(null);
        }
        UserLocationInfo userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        if (userLocationInfo != null) {
            if (TextUtils.isEmpty(userLocationInfo.getCity())) {
                userLocationInfo.setCity("贵阳市");
                userLocationInfo.setDistrict("南明区");
            }
            mUserLocationInfo = userLocationInfo;
        } else {
            UserLocationInfo info = new UserLocationInfo();
            info.setCity("贵阳市");
            info.setDistrict("南明区");
            mUserLocationInfo = info;
        }

    }

    private void updateLocationInfo(UserLocationInfo mUserLocationInfo) {
        if (mUserLocationInfo == null) {
            gaoDeLocationManager.requireLocation(userLocationInfo -> {
                if (userLocationInfo != null) {
                    updateLocationInfo(userLocationInfo);
                }
            });
        } else {
            Log.i(TAG, "updateLocationInfo: %s" + mUserLocationInfo.getCity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        initPullRefreshView(inflater, rootView);
        init();
        ViewInject.inject(this, rootView);
        initContentView();
        initListener();
        return rootView;
    }

    private void init() {
        mCurLocation.setText(mUserLocationInfo.getDistrict());
    }

    private void initListener() {
        mErCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErCodeScanActivity.startActivity(getContext());
            }
        });
        hotMerchantAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HotMerchantInfo>() {
            @Override
            public void onAdapterItemClick(int position, HotMerchantInfo hotMerchantInfo) {
                StoreDetailActivity.startActivity(getContext(), hotMerchantInfo.getId());
            }
        });
    }

    private void initData() {
        homeBannerAdapter = new HomeBannerAdapter(getContext());
        hotMerchantAdapter = new HotMerchantAdapter(getContext());
    }


    private void initPullRefreshView(LayoutInflater inflater, ViewGroup rootView) {
        mErCodeScan = rootView.findViewById(R.id.er_code_scan);
        mCurLocation = rootView.findViewById(R.id.cur_location);
        pullRefreshView = rootView.findViewById(R.id.home_list_view);
        pullRefreshView.setAdapter(hotMerchantAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        View headView = inflater.inflate(R.layout.fragment_home_head_view, pullRefreshView, false);
        pullRefreshView.addHeaderView(headView);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage = 1;
            // 加载数据
            loadData(isSuccess -> {
                pullRefreshView.stopPullRefresh();
                if (isSuccess) {
                    baseContentList.clear();
                    addSubView();
                    hotMerchantAdapter.setData(hotMerchantInfoList);
                }
            });
        });

        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadMoreHotMerchant());
    }

    private void loadMoreHotMerchant() {
        mHomeDataManager.GetHomeHotMerchantListInfo(PAGE_SIZE, curPage, mUserLocationInfo.getCity(), mUserLocationInfo.getDistrict(), "", "", new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if (isSuccess && hotMerchantInfos.size() > 0) {
                    hotMerchantAdapter.addData(hotMerchantInfos);
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                    pullRefreshView.stopLoadMore();
                }
            }
        });
    }

    private void loadData(OnResultListener onResultListener) {
        boolean[] result = new boolean[]{false, false, false, false, false, false};
        getHomeBanner(onResultListener, result);
        getHomeFunction(onResultListener, result);
        getHomeTransaction(onResultListener, result);
        getHomeBrandPartner(onResultListener, result);
        getBoutiqueRecommendInfo(onResultListener, result);
        getHotMerchantInfo(onResultListener, result);
    }

    private void getHotMerchantInfo(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.GetHomeHotMerchantListInfo(PAGE_SIZE, curPage, mUserLocationInfo.getCity(), mUserLocationInfo.getDistrict(), "", "", new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {

            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if (isSuccess) {
                    hotMerchantInfoList.clear();
                    hotMerchantInfoList = hotMerchantInfos;
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                }
                result[5] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[5] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }


    private void getBoutiqueRecommendInfo(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.GetHomeBoutiqueRecommendInfo(new OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos, String message) {
                super.onHttpResponse(isSuccess, homeBoutiqueRecommendInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_BOUTIQUE_RECOMMEND, new HomeBoutiqueRecommendContent(homeBoutiqueRecommendInfos));
                }
                result[4] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[4] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    private void getHomeBrandPartner(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.GetHomeBrandPartnerInfo(new OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBrandPartnerInfo> homeBrandPartnerInfos, String message) {
                super.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_BRAND_PARTNER, new HomeBrandPartnerContent(homeBrandPartnerInfos));
                }
                result[3] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[3] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    private void getHomeTransaction(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.getTransactionInfo(new OnHttpResponseCodeListener<List<HomeTransactionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeTransactionInfo> homeTransactionInfos, String message) {
                super.onHttpResponse(isSuccess, homeTransactionInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_TRANSACTION, new HomeTransactionContent(homeTransactionInfos));
                }
                result[2] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[2] = true;
                notifyAllFinish(onResultListener, result);
            }
        });
    }

    private void getHomeFunction(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.getHomeFunctionInfo(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_FUNCTION, new HomeFunctionContent(homeFunctionInfos));
                }
                result[1] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[1] = true;
                notifyAllFinish(onResultListener, result);
            }
        });
    }

    private void getHomeBanner(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.getHomeBannerInfo(mUserLocationInfo.getCity(), mUserLocationInfo.getDistrict(), new OnHttpResponseCodeListener<List<HomeBannerInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBannerInfo> homeBannerInfoList, String message) {
                super.onHttpResponse(isSuccess, homeBannerInfoList, message);
                if (isSuccess) {
                    homeBannerAdapter.changeItems(homeBannerInfoList);
                }
                result[0] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[0] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    private void notifyAllFinish(OnResultListener onResultListener, boolean[] result) {
        boolean state = true;
        for (boolean b : result) {
            if (!b) {
                state = false;
                break;
            }
        }
        if (state) {
            onResultListener.onResult(true);
        }
    }

    private void addSubView() {
        HomeBaseContent homeFunctionBaseContent = baseContentSparseArray.get(KEY_HOME_FUNCTION);
        if (homeFunctionBaseContent != null) {
            baseContentList.add(homeFunctionBaseContent);
        }
        HomeBaseContent homeTransactionBaseContent = baseContentSparseArray.get(KEY_HOME_TRANSACTION);
        if (homeTransactionBaseContent != null) {
            baseContentList.add(homeTransactionBaseContent);
        }
        HomeBaseContent homeBrandPartnerBaseContent = baseContentSparseArray.get(KEY_HOME_BRAND_PARTNER);
        if (homeBrandPartnerBaseContent != null) {
            baseContentList.add(homeBrandPartnerBaseContent);
        }
        HomeBaseContent homeBoutiqueRecommendBaseContent = baseContentSparseArray.get(KEY_HOME_BOUTIQUE_RECOMMEND);
        if (homeBoutiqueRecommendBaseContent != null) {
            baseContentList.add(homeBoutiqueRecommendBaseContent);
        }

        mSubViewContain.removeAllViews();
        for (HomeBaseContent baseContent : baseContentList) {
            View topView = new View(getContext());
            topView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dipToPx(getActivity(), 11)));
            View childView = baseContent.onCreateSubView(getContext());
            topView.setBackgroundColor(Color.parseColor("#f7f7f7"));
            mSubViewContain.addView(childView);
        }
    }

    private void initContentView() {
        pullRefreshView.startPullRefresh();
        mBannerView.setWaitMilliSecond(3000);
        mBannerView.setAdapter(homeBannerAdapter);
    }

}
