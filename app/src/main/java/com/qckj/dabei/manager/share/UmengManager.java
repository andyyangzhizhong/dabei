package com.qckj.dabei.manager.share;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 友盟管理器
 */

public class UmengManager extends BaseManager {
    // 微信
    private final String wxAppID = "wxf8cc09cfbaf41350";
    private String wxAppSecret = "c4eb1d1acb40f8fbfc55b64e00ec883c";
    // QQ
    private final String qqAppID = "101507083";
    private final String qqAppSecret = "2da9daeb773bdb04590db94c67d53976";

    @Override
    public void onManagerCreate(App application) {
        PlatformConfig.setWeixin(wxAppID, wxAppSecret);
        PlatformConfig.setQQZone(qqAppID, qqAppSecret);
        UMShareAPI.get(application);

    }

}
