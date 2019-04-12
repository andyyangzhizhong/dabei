package com.qckj.dabei.manager.mine;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 修改用户姓名请求
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyUserNameRequester extends SimpleBaseRequester<List<UserInfo>> {

    private String name;
    private String userId;

    public ModifyUserNameRequester(String name, String userId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.name = name;
        this.userId = userId;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/editUserNicName");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("nicheng", name);
        params.put("userid", userId);
    }
}
