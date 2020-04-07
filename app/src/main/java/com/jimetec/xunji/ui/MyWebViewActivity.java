package com.jimetec.xunji.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.common.baseview.base.BaseActivity;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.util.AndroidXunJS;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWebViewActivity extends BaseActivity {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLayout)
    RelativeLayout mRlTitleLayout;
    @BindView(R.id.vStatus)
    View vStatus;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.webview)
    WebView mWebview;
//    boolean showTitle =true;
    public static final String SHOW_TITLE = "showTitle";

    public static void  startToPreVip(Context context){
        MyWebViewActivity.startTo(context,"https://xunjiapi.jimetec.com/dist/experience.html","前置付费","前置付费",false);
 
    }


    public static void  startToAfterVip(Context context){
        startTo(context, "https://xunjiapi.jimetec.com/dist/unlock.html", "解锁功能", "解锁功能"); 
    }
//    String referer,
    public  static  void startTo(Context context, String url, String toMode , String toTitle){
        Intent intent = new Intent(context,MyWebViewActivity.class);
        intent.putExtra(EVENT_URL,url);
//        intent.putExtra(EVENT_REFERER,referer);
        intent.putExtra(EVENT_MODE,toMode);
        intent.putExtra(EVENT_TITLE,toTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public  static  void startTo(Context context, String url, String toMode , String toTitle,boolean showTitle){
        Intent intent = new Intent(context,MyWebViewActivity.class);
        intent.putExtra(EVENT_URL,url);
//        intent.putExtra(EVENT_REFERER,referer);
        intent.putExtra(EVENT_MODE,toMode);
        intent.putExtra(EVENT_TITLE,toTitle);
        intent.putExtra(SHOW_TITLE,showTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    AMapLocationClient locationClientSingle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        ButterKnife.bind(this);

        if(null == locationClientSingle){
            locationClientSingle = new AMapLocationClient(this.getApplicationContext());
        }

        boolean showTitle = getIntent().getBooleanExtra(SHOW_TITLE, true);
        if (showTitle){
            vStatus.setVisibility(View.VISIBLE);
            mRlTitleLayout.setVisibility(View.VISIBLE);
        }else {
            vStatus.setVisibility(View.GONE);
            mRlTitleLayout.setVisibility(View.GONE);
        }
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mTvTitle.setText(getEventTitle());
        WebSettings webSettings = mWebview.getSettings();
        mWebview.addJavascriptInterface(new AndroidXunJS(this), "App");//AndroidtoJS类对象映射到js的test对象

        //设置是否允许定位，这里为了使用H5辅助定位，设置为false。
        //设置为true不一定会进行H5辅助定位，设置为true时只有H5定位失败后才会进行辅助定位
        webSettings.setGeolocationEnabled(false);//设置启用定位
        webSettings.setJavaScriptEnabled(true);//允许使用js
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setDomStorageEnabled(true);   // 设置可以使用localStorage// 打开本地缓存提供JS调用,至关重要
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
//        try {
//            String appCachePath = MyApplication.BASEPATH;
//            LogUtils.e("appCachePath"+appCachePath);
//            webSettings.setAppCachePath(appCachePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(false);

        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtils.w("download--- " + url);
                Uri uri = Uri.parse(url);
                Intent step = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(step);
            }
        });

        mWebview.setWebChromeClient(webChromeClient);
        mWebview.setWebViewClient(webViewClient);
        locationClientSingle.startAssistantLocation(mWebview); 
        mWebview.loadUrl(mEventUrl); 
    }

    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }

//    @Override
//    public String getEventMode() {
//        return "服务协议及隐私条款";
//    }


    @Override
    protected void onDestroy() {
        if (null != locationClientSingle) {
            locationClientSingle.stopAssistantLocation();
            locationClientSingle.onDestroy();
        }
        super.onDestroy();

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载

        }

        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成

        }


        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {

            return super.shouldInterceptRequest(webView, s);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            return super.shouldInterceptRequest(view, request);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }


//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();// 接受所有网站的证书
//        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            try {
                if (!NetworkUtils.isAvailableByPing()) {
                    ToastUtil.showShort("网络中断，请检查您的网络状态！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("拦截url:" + url);

            if (!url.startsWith("http")) {
                // 前往第三方app
                try {
                    // 以下固定写法,表示跳转到第三方应用
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            return false;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            //重写此方法，配置权限
            callback.invoke(origin,true,false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };

}
