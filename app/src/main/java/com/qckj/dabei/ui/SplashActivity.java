package com.qckj.dabei.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.WindowManager;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.ui.main.MainActivity;
import com.qckj.dabei.util.inject.Manager;

import static android.content.ContentValues.TAG;

/**
 * 闪屏页面
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class SplashActivity extends BaseActivity {

    public static final int REQUEST_CODE_PERMISSIONS_LOCATION = 15;

    public static final String KEY_SHOW_GUIDE = "key_show_guide";
    private static final int HANDLER_WHAT_START = 2;

    @Manager
    private SettingManager mSettingManager;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HANDLER_WHAT_START) {
                boolean isShowGuide = mSettingManager.getSetting(KEY_SHOW_GUIDE, false);
                if (isShowGuide) {
                    // 进入向导页面
                } else {
                    // 进入应用页面
                    MainActivity.startActivity(getActivity());
                    finish();
                }
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_START, 3000);
        checkLocationPermission();
        UserLocationInfo userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        if (userLocationInfo!=null){
            Log.i(TAG, "updateLocationInfo: %s" + userLocationInfo.getCity());
        }
    }


    private void checkLocationPermission() {

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 有权限, 下一步
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.tip_title);
                builder.setCancelable(false);
                builder.setMessage(R.string.permission_tip_location);
                builder.setPositiveButton(R.string.tip_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS_LOCATION);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS_LOCATION);
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_LOCATION) {
            showToast("3333");
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
