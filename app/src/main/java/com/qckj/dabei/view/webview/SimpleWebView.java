package com.qckj.dabei.view.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by yangzhizhong on 2019/4/9.
 */
public class SimpleWebView extends RelativeLayout {
    /**
     * 浏览器进度条
     */
    @FindViewById(R.id.custom_webView_progress)
    private ProgressBar mProgressBar;

    /**
     * webView
     */
    @FindViewById(R.id.custom_webView)
    public WebView mWebView;

    private WebViewChangeListener webViewChangeListener;
    private String titleText = "";
    private String webUrl = "";

    private List<Map<String, String>> mUrls;
    private String mCurrentUrl;
    private OnTitleChange mOnTitleChange;

    private String originUrl = ""; //最开始打开界面时的url

    private JsInterface mJsInterface;

    public SimpleWebView(Context context) {
        this(context, null);
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_webview, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
        mUrls = new ArrayList<>();
    }

    @SuppressLint({"setJavaScriptEnabled", "addJavascriptInterface"})
    private void initData() {
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mJsInterface = new JsInterface(getContext());
        mWebView.addJavascriptInterface(mJsInterface, "JavaScriptInterface");
        mWebView.addJavascriptInterface(mJsInterface, "android");
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setDownloadListener(new MyWebViewDownloadListener());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mCurrentUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                notifyTitleChange();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (matchRegUrl(url)) {
                    webUrl = url;
                    if (webViewChangeListener != null) {
                        webViewChangeListener.urlAndViewChange(view, url);
                    }
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressBar == null) {
                    return;
                }
                mProgressBar.setProgress(newProgress > 5 ? newProgress : 5);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (webViewChangeListener != null) {
                    webViewChangeListener.titleChange(title);
                }
                titleText = title;
                Map<String, String> urlTitle = new HashMap<>();
                urlTitle.put(mCurrentUrl, title);
                mUrls.add(urlTitle);
                mJsInterface.setTitle(title);
            }
        });
    }

    /**
     * 判断浏览器是否可返回
     */
    public boolean judgeWebViewIsGoBack() {
        return mWebView.canGoBack();
    }

    public void getGoBack() {
        mWebView.goBack();
    }

    public void setReload() {
        mWebView.reload();
    }

    public boolean judgeWebViewCanGoForward() {
        return mWebView.canGoForward();
    }

    public void setGoForward() {
        mWebView.goForward();
    }

    /**
     * 设置是否分享数据给js
     *
     * @param isOpenJs true:是，fasle:否
     */
    @SuppressLint("addJavascriptInterface")
    public void setIsOpenJs(boolean isOpenJs) {
        if (isOpenJs) {
            mWebView.addJavascriptInterface(new JsInterface(getContext()), "JavaScriptInterface");
            mWebView.addJavascriptInterface(new JsInterface(getContext()), "android");
        } else {
            mWebView.removeJavascriptInterface("JavaScriptInterface");
            mWebView.removeJavascriptInterface("android");
        }
    }

    /**
     * 设置webView变化监听
     */
    public void setWebViewChangeListener(WebViewChangeListener webViewChangeListener) {
        this.webViewChangeListener = webViewChangeListener;
    }

    /**
     * 加载网页
     *
     * @param webUrl 网址
     */
    @SuppressLint("addJavascriptInterface")
    public void loadWebUrl(String webUrl) {
        originUrl = webUrl;
        if (webUrl == null) {
            Toast.makeText(getContext(), R.string.url_wrong_check_you_url, Toast.LENGTH_SHORT).show();
            if (webViewChangeListener != null) {
                webViewChangeListener.urlIsEmpty(true);
            }
            return;
        }
        mWebView.loadUrl(webUrl);
    }

    public void setCallHiddenWebViewMethod(String name) {
        try {
            Method method = WebView.class.getMethod(name);
            method.invoke(mWebView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentTitle() {
        for (Map<String, String> map : mUrls) {
            if (map.containsKey(mCurrentUrl)) {
                return map.get(mCurrentUrl);
            }
        }
        return "";
    }

    @SuppressLint({"AddJavascriptInterface", "JavascriptInterface"})
    public void addJavascriptInterface(Object object) {
        mWebView.addJavascriptInterface(object, "JavaScriptInterface");
        mWebView.addJavascriptInterface(object, "android");
    }

    private void notifyTitleChange() {
        if (mOnTitleChange != null) {
            mOnTitleChange.onTitleChange(getCurrentTitle());
        }
    }

    public void setOnTitleChange(OnTitleChange mOnTitleChange) {
        this.mOnTitleChange = mOnTitleChange;
    }

    /**
     * 正则匹配url是否可用
     *
     * @param url url
     */
    private boolean matchRegUrl(String url) {
        return Pattern.compile("^http(s?)://").matcher(url).find();
    }

    public void destroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    /**
     * webView变化监听
     */
    public interface WebViewChangeListener {
        /**
         * 标题变化了
         *
         * @param title 标题
         */
        void titleChange(String title);

        /**
         * 当前网址是否为空
         */
        void urlIsEmpty(boolean isEmpty);

        void urlAndViewChange(WebView webView, String webUrl);
    }

    public interface OnTitleChange {
        void onTitleChange(String title);
    }

    /**
     * 加载网页的监听
     */
    public class MyWebViewDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Context context = getContext();
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }
}
