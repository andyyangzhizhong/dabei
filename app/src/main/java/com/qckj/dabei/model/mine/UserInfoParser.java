package com.qckj.dabei.model.mine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/3/27.
 */
public class UserInfoParser {

    public JSONObject toJsonObject(UserInfo userInfo) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("F_C_ID", userInfo.getId());
        jsonObject.put("F_C_UC", userInfo.getAccount());
        jsonObject.put("F_C_PWD", userInfo.getPwd());
        jsonObject.put("F_I_SEX", userInfo.getSex());
        jsonObject.put("F_C_TXIMG", userInfo.getImgUrl());
        jsonObject.put("F_C_NICHENG",userInfo.getName());
        return jsonObject;
    }

}
