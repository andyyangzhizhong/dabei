package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/3/25.
 */
public class GetHomeBrandPartnerRequester extends SimpleBaseRequester<List<HomeBrandPartnerInfo>> {
    public GetHomeBrandPartnerRequester(OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
    }

    @Override
    protected List<HomeBrandPartnerInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"),HomeBrandPartnerInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/partnerlist");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
