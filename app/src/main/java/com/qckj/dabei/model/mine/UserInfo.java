package com.qckj.dabei.model.mine;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * "F_C_BALANCE": 0,
 * "F_C_ID": "20190327150012Ic8uuMpABGRBVnmYBH",
 * "F_C_NICHENG": "",
 * "F_C_PWD": "",
 * "F_C_TXIMG": "",
 * "F_C_UC": "18394331868",
 * "F_I_ROLE": 1,
 * "F_I_RZPHONE": 1,
 * "F_I_RZSJ_GR": 0,
 * "F_I_RZSJ_QY": 0,
 * "F_I_RZSM": 0,
 * "F_I_SEX": 0,
 * "F_I_STATE": 1,
 * "POINT": 0,
 * "count": 0,
 * "passwordState": 0
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class UserInfo implements Serializable {

    public static final int SEX_NO = 0;
    public static final int SEX_MAIL = 1;
    public static final int SEX_FEMAIL = 2;

    public static final int ROLE_NORMAL = 1;
    public static final int ROLE_MERCHANTS = 2;
    public static final int ROLE_MERCHANTS_AGENT = 3;

    public static final int ACCOUNT_NORMAL = 1;
    public static final int ACCOUNT_ABNORMAL = 2;
    public static final int ACCOUNT_TITLES = 3;

    public static final int USER_AUTH_STATE_UN = 0;
    public static final int USER_AUTH_STATE = 1;
    public static final int USER_AUTH_STATE_ING = 2;

    public static final int USER_PWD_STATE_NO = 0;
    public static final int USER_PWD_STATE_YES = 1;

    public static final int USER_PHONE_STATE_UNBIND = 0;
    public static final int USER_PHONE_STATE_BIND = 1;

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_UC")
    private String account;

    @JsonField("F_C_PWD")
    private String pwd;
    //性别:0=未知；1=男；2=女
    @JsonField("F_I_SEX")
    private int sex;

    @JsonField("F_C_TXIMG")
    private String imgUrl;

    @JsonField("F_C_NICHENG")
    private String name;
    //角色：1=普通用户；2=商家；3=代理商
    @JsonField("F_I_ROLE")
    private int role;
    // 状态：1=正常；2=异常；3=封号
    @JsonField("F_I_STATE")
    private int state;
    //实名认证：0=未认证；1=已认证；2=审核中
    @JsonField("F_I_RZSM")
    private int authState;
    //企业商家认证状态：0=未认证；1=企业商家已认证；2=审核中
    @JsonField("F_I_RZSJ_QY")
    private int entAuthState;
    // 个人商家认证状态：0=未认证；1=个人商家已认证；2=审核中
    @JsonField("F_I_RZSJ_GR")
    private int perAuthState;
    // 密码状态：0未设置，1已设置
    @JsonField("passwordState")
    private int pwdState;
    // 手机号绑定状态：0=未绑定；1=已绑定
    @JsonField("F_I_RZPHONE")
    private int phoneBindState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @UserSex
    public int getSex() {
        return sex;
    }

    public void setSex(@UserSex int sex) {
        this.sex = sex;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @UserRole
    public int getRole() {
        return role;
    }

    public void setRole(@UserRole int role) {
        this.role = role;
    }

    @UserAccountState
    public int getState() {
        return state;
    }

    public void setState(@UserAccountState int state) {
        this.state = state;
    }

    @AuthState
    public int getAuthState() {
        return authState;
    }

    public void setAuthState(@AuthState int authState) {
        this.authState = authState;
    }

    @AuthState
    public int getEntAuthState() {
        return entAuthState;
    }

    @AuthState
    public int getPerAuthState() {
        return perAuthState;
    }

    public void setPerAuthState(@AuthState int perAuthState) {
        this.perAuthState = perAuthState;
    }

    public void setEntAuthState(@AuthState int entAuthState) {
        this.entAuthState = entAuthState;
    }

    @UserPwdState
    public int getPwdState() {
        return pwdState;
    }

    public void setPwdState(@UserPwdState int pwdState) {
        this.pwdState = pwdState;
    }

    @UserPhoneBindState
    public int getPhoneBindState() {
        return phoneBindState;
    }

    public void setPhoneBindState(@UserPhoneBindState int phoneBindState) {
        this.phoneBindState = phoneBindState;
    }

    @IntDef({SEX_NO, SEX_MAIL, SEX_FEMAIL})
    public @interface UserSex {
    }

    @IntDef({ROLE_NORMAL, ROLE_MERCHANTS, ROLE_MERCHANTS_AGENT})
    public @interface UserRole {
    }

    @IntDef({ACCOUNT_NORMAL, ACCOUNT_ABNORMAL, ACCOUNT_TITLES})
    public @interface UserAccountState {
    }

    @IntDef({USER_AUTH_STATE_UN, USER_AUTH_STATE, USER_AUTH_STATE_ING})
    public @interface AuthState {
    }

    @IntDef({USER_PWD_STATE_NO, USER_PWD_STATE_YES})
    public @interface UserPwdState {
    }

    @IntDef({USER_PHONE_STATE_UNBIND, USER_PHONE_STATE_BIND})
    public @interface UserPhoneBindState {
    }
}
