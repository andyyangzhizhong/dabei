package com.qckj.dabei.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;

/**
 * 关于我们
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class AboutUsActivity extends BaseActivity {

    @FindViewById(R.id.version_name)
    private TextView versionName;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ViewInject.inject(this);
        versionName.setText(BuildConfig.VERSION_NAME);
    }

    @OnClick({R.id.app_website, R.id.app_service_phone})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.app_website:
                BrowserActivity.startActivity(getActivity(), "http://www.dabei.com/", "大贝官网");
                break;
            case R.id.app_service_phone:
                showPhoneDialog("0851-85555809");
                break;
        }
    }

    private void showPhoneDialog(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("联系电话: " + phone);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callPhone(phone);
            }
        });
        builder.show();
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        getActivity().startActivity(intent);
    }
}
