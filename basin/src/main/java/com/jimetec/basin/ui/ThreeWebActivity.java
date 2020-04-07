package com.jimetec.basin.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.ScreenShotListenManager;
import com.common.lib.utils.ToastUtil;
import com.common.lib.utils.UIUtil;
import com.jimetec.basin.event.LoanDataBean;
import com.jimetec.basin.event.LoanEventBean;
import com.jimetec.basin.utils.LoanConstant;
import com.jimetec.basin.LoanHelp;
import com.jimetec.basin.R;
import com.jimetec.basin.event.JsBean;
import com.jimetec.basin.event.ProdBean;
import com.jimetec.basin.event.ProdStay;
import com.jimetec.basin.event.ScreenBean;
import com.jimetec.basin.utils.AppData;
import com.jimetec.basin.utils.LoanUtil;
import com.jimetec.basin.utils.WebUtil;

import java.util.HashMap;
import java.util.Map;

public class ThreeWebActivity extends AppCompatActivity {

    LoanHelp mLoanHelp = LoanUtil.getLoanHelp();
    TextView mTvTitle;
    ProgressBar mProgressBar;
    WebView webView;
    public static  String TAG = "";
    public long inTime;
    private ScreenShotListenManager mScreenManager;
    private long outTime ;

    private  boolean isFirst= true;
    private  boolean isError = false;
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            mProgressBar.setVisibility(View.GONE);
            if (isFirst && !isError){
                isFirst = false;
                subimtView(mBean);
            }
            isError = false;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            WebUtil.handleReceivedSslError(handler);
        }


        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
            if (WebUtil.isWifiProxy()) {
                return null;
            }
            return super.shouldInterceptRequest(webView, s);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (WebUtil.isWifiProxy()) {
                return null;
            }
            return super.shouldInterceptRequest(view, request);
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            isError =true;
        }
    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {


        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTvTitle.setText(title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }
    };
    private ProdBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        outTime = mLoanHelp.getonfidePopFrameTime();
        TAG =getClass().getSimpleName();
        setContentView(R.layout.activity_three_web);
        initView();
        inTime = System.currentTimeMillis();
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.addJavascriptInterface(new AndroidJs(), "App");//AndroidtoJS类对象映射到js的test对象

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);   // 设置可以使用localStorage// 打开本地缓存提供JS调用,至关重要
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
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
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);//允许使用js

//
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
//        //支持屏幕缩放
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        //不显示webview缩放按钮
//        webSettings.setDisplayZoomControls(false);




        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtils.w("download--- " + url);
//                if (mBean!=null)
//                    subimtClick(Constants.WEB_PRODUCTBEAN+"_download",url);
                Uri uri = Uri.parse(url);
                Intent step = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(step);
            }
        });
        initData();
        initFileListen();
    }

    private void initView() {
        mTvTitle= (TextView) findViewById(R.id.tv_title);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        webView= (WebView) findViewById(R.id.webView);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBean = (ProdBean) getIntent().getSerializableExtra(ProdBean.TAG);
            if (mBean==null|| mBean.prod==null) return;
//            String url =  getIntent().getStringExtra(Constants.THREE_URL);
            if (!TextUtils.isEmpty(mBean.prod.prodUrl)) {
                LogUtils.e("拦截url  url",mBean.prod.prodUrl);
                webView.loadUrl(mBean.prod.prodUrl);

            }
        }
    }


    public class AndroidJs {
        @JavascriptInterface
        public String getAll() {
//            LogUtils.e("getAll");
            String json = GsonUtil.toGsonString(new JsBean());
            LogUtils.e("getAll" + json);
            return json;
        }

        //Js 回调方法，2
        @JavascriptInterface
        public void setPhone(String phone) {
            LogUtils.e("phone" + phone);
            AppData.getInstance().getUser().phone = phone;
            AppData.getInstance().save();
            //已经拿到值，进行相关操作
        }
    }


    private void initFileListen() {
        mScreenManager = ScreenShotListenManager.newInstance(this);
        mScreenManager.setListener(new ScreenShotListenManager.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
//                LogUtils.e(UIUtil.isRunOnUIThread() + imagePath);
                ScreenBean screenBean = new ScreenBean();
                if (mBean==null||mBean.prod==null)return;
                screenBean.pageClassName= TAG;
                screenBean.pageUrl= mBean.prod.prodUrl;
                mLoanHelp.submitScreenshot(GsonUtil.toGsonString(screenBean));
//                RequestClient.getInstance().submitScreenshot(new Gson().toJson(screenBean));
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
        }else {
//            if (System.currentTimeMillis() - inTime > 0) {
            if (System.currentTimeMillis() - inTime > outTime) {
                try {
                    leaveReason();
                } catch (Exception e) {
                    e.printStackTrace();
//                    CrashReport.postCatchedException(new Exception("离开理由失败--"+e.getMessage()));
                }
            } else {
                finish();
            }
        }
    }


    public int  state=0;
    public String feedDesc ="";

    public void leaveReason() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_idea, null);
        final EditText editText = v.findViewById(R.id.et);
        final View card_view = v.findViewById(R.id.card_view);
        final TextView tvLeft = v.findViewById(R.id.tv_left);
        View tvRight = v.findViewById(R.id.tv_right);
        final RadioGroup rg = v.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvLeft.setTextColor(UIUtil.getColor(R.color.color_404040));
                if (checkedId == R.id.rb_3) {
                    state =3;
                    card_view.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    if (checkedId == R.id.rb_1)state = 1;
                    if (checkedId == R.id.rb_2)state=2;
                    editText.clearFocus();
                    hintKeyBoard();
                    card_view.setVisibility(View.GONE);
                }
            }


        });
//        int checkedRadioButtonId = ;
//        LogUtils.e(checkedRadioButtonId+"iddid");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1) {
                    ToastUtil.showShort("请选择一项");
                } else {
                    if (rg.getCheckedRadioButtonId() == R.id.rb_3) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            ToastUtil.showShort("请输入内容");
                            return;
                        }
                    }
                    feedDesc = editText.getText().toString();

                    //成功离开
                    leaveSuccess();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }

                }

            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    state = 0;
                    dialog.dismiss();

                }
            }
        });
        dialog.setView(v);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//                p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的0.3
//        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
//        dialog.getWindow().setAttributes(p);     //设置生效
//                mDialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
    }


    public void leaveSuccess(){
        Map<String, String> map = new HashMap<>();
        map.put("state",state+"");
        if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone)) map.put("phone", AppData.getInstance().getUser().phone);
        if (mBean!=null&&mBean.prod!=null)map.put("prodId",mBean.prod.prodId+"");
        if (state ==3)map.put("feedDesc",feedDesc);
        feed(map);
    }

    public void hintKeyBoard() {
        if(isSoftShowing()){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 软键盘是否显示
     * @return
     */
    private boolean isSoftShowing() {
        //获取当屏幕内容的高度
        int screenHeight = this.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight*2/3 > rect.bottom;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScreenManager!=null){
            mScreenManager.startListen();
        }
        //MobclickAgent.onPageStart(TAG);
         //统计时长
//        int  i= 1/0;
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mScreenManager!=null){
            mScreenManager.stopListen();
        }
        //MobclickAgent.onPageEnd(TAG); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        //MobclickAgent.onPause(this); //统计时长
    }

    @Override
    protected void onDestroy() {
        submitProdStayTime();
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
        webView.destroy();
        //释放资源
        webView = null;
        super.onDestroy();
    }

    //弹窗
    public void feed(Map<String ,String> map){
//        RequestClient.getInstance().feed(map)
//                .compose(Rxutil.<LoanHttpResult<Object>>rxSchedulerHelper())
//                .map(new LoanHttpResultFuc<Object>())
//                .subscribeWith(new NoResponseSubscriber() {
//                });
        mLoanHelp.feed(map);
    }

    //停留时间
    public void  submitProdStayTime(){
        if (mBean==null||mBean.prod==null)return;
        ProdStay prodStay = new ProdStay();
        prodStay.prodId = mBean.prod.prodId;
        prodStay.stayTime = System.currentTimeMillis() - inTime;
        mLoanHelp.submitProdStayTime(GsonUtil.toGsonString(prodStay));
//        RequestClient.getInstance().submitProdStayTime(new Gson().toJson(prodStay));
    }


    public void subimtView(ProdBean bean){
        if (bean==null||bean.prod==null)return;
        LoanEventBean eventBean =new LoanEventBean().setREFERRER(bean.referer).setRefererUrl(bean.refererUrl).setUrl(bean.prod.prodUrl).setEtype(LoanEventBean.etypeView).setMode(LoanConstant.WEB_PRODUCTBEAN)
                .setTitle(bean.prod.prodName).setProductId(bean.prod.prodId);
//        RequestClient.getInstance().submitEvent(new DataBean().setEvents(eventBean).toJson());
        mLoanHelp.submitGson(new LoanDataBean().setEvents(eventBean).toJson());
    }
}
