package com.qckj.dabei.manager.mine;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * 上传图片文件
 * <p>
 * Created by yangzhizhong on 2019/3/29.
 */
public class UploadPhotoFileRequester extends SimpleBaseRequester<String> {

    private File file;

    public UploadPhotoFileRequester(File file, OnHttpResponseCodeListener<String> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.file = file;
    }

    @Override
    protected String onDumpData(JSONObject jsonObject) {
        return jsonObject.optString("url");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/alyFile");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }

    @Override
    protected File getFile() {
        return file;
    }
}
