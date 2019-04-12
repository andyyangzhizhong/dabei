package com.qckj.dabei.ui.mine.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.AlbumUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.dialog.SelectPhotoTypeDialog;
import com.qckj.dabei.view.dialog.SelectSexDialog;
import com.qckj.dabei.view.image.CircleImageView;

import java.io.File;

/**
 * 用户信息界面
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class UserInfoActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;

    @FindViewById(R.id.user_head_portrait)
    private CircleImageView mUserHeadPortrait;

    @FindViewById(R.id.user_name)
    private CommonItemView mUserName;

    @FindViewById(R.id.user_sex)
    private CommonItemView mUserSex;

    @FindViewById(R.id.user_phone)
    private CommonItemView mUserPhone;

    @FindViewById(R.id.modify_password)
    private CommonItemView mModifyPassword;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    private UserManager.OnUserLoginListener onUserLoginListener = new UserManager.OnUserLoginListener() {
        @Override
        public void onLoginSuccess(UserInfo userInfo) {
            GlideUtil.displayImage(getActivity(), userInfo.getImgUrl(), mUserHeadPortrait);
            mUserName.setContent(userInfo.getName());
            mUserPhone.setContent(userInfo.getAccount());
            pwdStateSetting(userInfo);
        }
    };

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
                checkCameraPermission();
            } else {
                AlbumUtils.openAlbum(UserInfoActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

    private SelectSexDialog.OnSelectSexListener selectSexListener = new SelectSexDialog.OnSelectSexListener() {
        @Override
        public void onSelectSex(boolean isMale) {
            showLoadingProgressDialog();
            userManager.modifyUserSex(isMale, new OnResultMessageListener() {
                @Override
                public void onResult(boolean isSuccess, String message) {
                    dismissLoadingProgressDialog();
                    if (isSuccess) {
                        if (isMale) {
                            mUserSex.setContent(getString(R.string.mine_sex_male));
                        } else {
                            mUserSex.setContent(getString(R.string.mine_sex_female));
                        }
                    } else {
                        showToast(message);
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ViewInject.inject(this);
        initData();
        initListener();
    }

    private void initListener() {
        userManager.addOnUserLoginListener(onUserLoginListener);
    }

    private void initData() {
        UserInfo userInfo = userManager.getUserInfo();
        GlideUtil.displayImage(getActivity(), userInfo.getImgUrl(), mUserHeadPortrait);
        String name = userInfo.getName();
        if (TextUtils.isEmpty(name)) {
            mUserName.setContent(getString(R.string.mine_app_user_name));
        } else {
            mUserName.setContent(name);
        }
        int sex = userInfo.getSex();
        switch (sex) {
            case UserInfo.SEX_FEMAIL:
                mUserSex.setContent(getString(R.string.mine_sex_female));
                break;
            case UserInfo.SEX_MAIL:
                mUserSex.setContent(getString(R.string.mine_sex_male));
                break;
            case UserInfo.SEX_NO:
                mUserSex.setContent(getString(R.string.mine_sex_no));
                break;
        }
        String account = userInfo.getAccount();
        if (TextUtils.isEmpty(account)) {
            mUserPhone.setContent(getString(R.string.mine_bind_phone_no));
        } else {
            mUserPhone.setContent(account);
        }
        pwdStateSetting(userInfo);
    }

    private void pwdStateSetting(UserInfo userInfo) {
        int pwdState = userInfo.getPwdState();
        switch (pwdState) {
            case UserInfo.USER_PWD_STATE_NO:
                mModifyPassword.setContent(getString(R.string.setting_no));
                break;
            case UserInfo.USER_PWD_STATE_YES:
                mModifyPassword.setContent(getString(R.string.setting_yes));
                break;
        }
    }


    @OnClick({R.id.user_head_portrait_ll, R.id.user_name, R.id.user_sex, R.id.user_phone, R.id.modify_password, R.id.exit_login})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.user_head_portrait_ll:
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
                break;
            case R.id.user_name:
                ModifyUserNameActivity.startActivity(getActivity());
                break;
            case R.id.user_sex:
                SelectSexDialog selectSexDialog = new SelectSexDialog(getActivity());
                selectSexDialog.show();
                selectSexDialog.setOnSelectSexListener(selectSexListener);
                break;
            case R.id.user_phone:
                AgainBindPhoneActivity.startActivity(getActivity());
                break;
            case R.id.modify_password:
                ModifyLoginPwdActivity.startActivity(getActivity());
                break;
            case R.id.exit_login:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AlbumUtils.OPEN_ALBUM:
                if (data != null && resultCode == RESULT_OK) {
                    uploadPhoto(AlbumUtils.getDirFromAlbumUri(data.getData()));
                    break;
                }
            case AlbumUtils.OPEN_CAMERA:
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file = new File(externalStorageDirectory, "head.png");
                uploadPhoto(file.getPath());
                break;

        }

    }


    private void uploadPhoto(String path) {
        showLoadingProgressDialog();
        userManager.uploadHeadPortrait(path, new OnResultMessageListener() {
            @Override
            public void onResult(boolean isSuccess, String message) {
                dismissLoadingProgressDialog();
                if (!isSuccess) {
                    showToast(message);
                }
            }
        });
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AlbumUtils.openCamera(UserInfoActivity.this, AlbumUtils.NORMAL,"head");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.tip_title);
                builder.setMessage(R.string.permission_tip_camera);
                builder.setPositiveButton(R.string.tip_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AlbumUtils.openCamera(UserInfoActivity.this, AlbumUtils.NORMAL,"head");
            } else {
                showToast(R.string.permission_msg_camera_failed);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userManager.removeOnUserLoginListener(onUserLoginListener);
    }
}
