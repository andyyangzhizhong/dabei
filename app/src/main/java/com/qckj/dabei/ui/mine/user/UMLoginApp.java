package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
import android.util.Log;

import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.util.log.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 第三方平台登录app
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class UMLoginApp {

    public static void loginApp(Activity activity, SHARE_MEDIA shareMedia, OnResult<Map<String, String>> onResult) {
        UMShareAPI.get(activity).getPlatformInfo(activity, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                onResult.onResult(true, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                onResult.onResult(false, null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                onResult.onResult(false, null);
            }
        });


    }

}
