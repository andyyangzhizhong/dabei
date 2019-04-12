package com.qckj.dabei.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.qckj.dabei.app.App;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.UserManager;

/**
 * 与js交互管理类
 */
public class JsInterface {
    private Context context;
    private String title = "";

    public JsInterface(Context context) {
        this.context = context;
    }

    /**
     * 获取用户id
     *
     * @return int
     */
    @JavascriptInterface
    public String getUid() {
        return App.getInstance().getManager(UserManager.class).getCurId();
    }

    /**
     * 获取客户端版本号
     *
     * @return 版本号
     */
    @JavascriptInterface
    public int getVersionCode() {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @JavascriptInterface
    public void finishWebView() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    @JavascriptInterface
    public String back() {
        return App.getInstance().getManager(SettingManager.class).getSetting(SettingManager.KEY_USER_INFO, "");
    }

    @JavascriptInterface
    public void backToAndroid() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    @JavascriptInterface
    public String getLocation() {
        // TODO: 2019/4/9 待修改
        //UserLocationInfo userLocationInfo = App.getInstance().getManager(GaoDeLocationManager.class).getUserLocationInfo();
        // return "[" + userLocationInfo.getLongitude() + "," + userLocationInfo.getLatitude() + "]";
        return "[" + 106.71685319874189 + "," + 26.56750550752618 + "]";
    }

    @JavascriptInterface
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
