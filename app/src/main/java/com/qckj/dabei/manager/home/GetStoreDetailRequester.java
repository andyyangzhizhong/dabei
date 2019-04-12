package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.StoreDetailInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * 获取店铺详情
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class GetStoreDetailRequester extends SimpleBaseRequester<StoreDetailInfo> {

    private String id;
    private String longitude;
    private String latitude;

    public GetStoreDetailRequester(String id, String longitude, String latitude, OnHttpResponseCodeListener<StoreDetailInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected StoreDetailInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), StoreDetailInfo.class).get(0);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/businessInformation");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("businessId", id);
        params.put("rows", "");
        params.put("page", "");
        params.put("user_x", longitude);
        params.put("user_y", latitude);
    }
}
