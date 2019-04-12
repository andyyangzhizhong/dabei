package com.qckj.dabei.manager.mine;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.WalletInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * 我的收益
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class GetWalletRequester extends SimpleBaseRequester<WalletInfo> {

    private String userId;

    public GetWalletRequester(String userId, OnHttpResponseCodeListener<WalletInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected WalletInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), WalletInfo.class).get(0);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/myWallet");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);

    }
}
