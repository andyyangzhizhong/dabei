package com.qckj.dabei.view.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;

/**
 * 内置浏览器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class BrowserActivity extends BaseActivity implements SimpleWebView.WebViewChangeListener, SimpleWebView.OnTitleChange {
    public static String URL_NAME = "url_name";
    public static String TITLE = "title";
    public static String SHOULD_OVERRIDE_TITLE = "should_override_title";

    @FindViewById(R.id.activity_browser_actionbar)
    private ActionBar mActionBar;

    @FindViewById(R.id.activity_browser_browser_wv)
    private SimpleWebView mBrowserWv;

    @FindViewById(R.id.rootView)
    private ViewGroup rootView;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private String mTitle;
    private String mCurrentTitle = "";
    private String mUrl;
    private boolean mShouldOverrideTitle = false;

    public static void startActivity(Context context, String url, String title, boolean shouldOverrideTitle) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra(TITLE, title);
        lIntent.putExtra(SHOULD_OVERRIDE_TITLE, shouldOverrideTitle);
        context.startActivity(lIntent);
    }

    public static void startActivity(Context context, String url, String title) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra(TITLE, title);
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ViewInject.inject(this);
        initData();
        initView();
        addListener();
        dealWebViewBrowser();
    }

    private void initData() {
        mUrl = getIntent().getStringExtra(URL_NAME);
        mTitle = getIntent().getStringExtra(TITLE);
        mCurrentTitle = mTitle;
        mShouldOverrideTitle = getIntent().getBooleanExtra(SHOULD_OVERRIDE_TITLE, false);
    }

    private void initView() {
        mActionBar.setTitleText(mTitle);
    }

    private void addListener() {
        mBrowserWv.setWebViewChangeListener(this);
        mBrowserWv.setOnTitleChange(this);
    }

    private void dealWebViewBrowser() {
        mBrowserWv.loadWebUrl(mUrl);
        mBrowserWv.setIsOpenJs(true);
    }

    @Override
    protected void onDestroy() {
        rootView.removeAllViews();
        super.onDestroy();
        mBrowserWv = null;
    }

    @Override
    protected void onPause() {
        mBrowserWv.setCallHiddenWebViewMethod("onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBrowserWv.setCallHiddenWebViewMethod("onResume");
        showActionBar(mCurrentTitle);
        super.onResume();
    }

    public int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void onTitleChange(String title) {
        if (mShouldOverrideTitle) {
            showActionBar(title);
        }
    }

    @Override
    public void titleChange(String title) {
        if (!TextUtils.isEmpty(title) && !Patterns.WEB_URL.matcher(title).matches()) {
            if (mShouldOverrideTitle) {
                showActionBar(title);
            }
        }
    }

    @Override
    public void urlIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            finish();
        }
    }

    @Override
    public void urlAndViewChange(WebView view, String webUrl) {
        view.loadUrl(webUrl);
    }

    private void showActionBar(String title) {
        mActionBar.setTitleText(title);
        mCurrentTitle = title;
    }

}