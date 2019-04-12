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
 * 修改用户性别
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyUserSexRequester extends SimpleBaseRequester<List<UserInfo>> {

    private int sex;
    private String userId;

    public ModifyUserSexRequester(@UserInfo.UserSex int sex, String userId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.sex = sex;
        this.userId = userId;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/editUserSex");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("sex", sex);
        params.put("userid", userId);
    }
}
