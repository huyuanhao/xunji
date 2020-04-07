package com.jimetec.basin.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.lib.utils.BitmapUtil;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.ScreenShotListenManager;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.ToastUtil;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.basin.utils.LoanConstant;
import com.jimetec.basin.LoanHelp;
import com.jimetec.basin.R;
import com.jimetec.basin.event.AppList;
import com.jimetec.basin.event.JsBean;
import com.jimetec.basin.event.ProdBean;
import com.jimetec.basin.utils.AppData;
import com.jimetec.basin.utils.LoanAppUtils;
import com.jimetec.basin.utils.LoanUtil;
import com.jimetec.basin.utils.WebUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class AllWebActivity extends AppCompatActivity implements SensorEventListener {
    LoanHelp mLoanHelp = LoanUtil.getLoanHelp();
    private String TAG;
    private ProgressBar mProgressBar;
    private ProgressBar firstBar;
    private static String HOST = "https://h5.jimetec.com";
    private ScreenShotListenManager mScreenManager;
    private SensorManager sensorManager;
    private Sensor sensor;
    boolean isError;
    private FrameLayout mFlError;
    private Button mBtError;
    private boolean isFirst = true;

    private WebView mWebView;

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            LogUtils.e("onPageStarted:" + url);
            isError = false;
            mProgressBar.setVisibility(View.VISIBLE);
            mFlError.setVisibility(View.GONE);
            if (isFirst) firstBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            LogUtils.e("onPageFinished" + url);
            mProgressBar.setVisibility(View.GONE);
            if (!isError) {
                if (mWebView.getVisibility() != View.VISIBLE)
                    mWebView.setVisibility(View.VISIBLE);
            } else {
//                if (mWebView.getVisibility() != View.VISIBLE)
                mWebView.setVisibility(View.INVISIBLE);
            }
            firstBar.setVisibility(View.GONE);
            isFirst = false;
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
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            String curUrl = view.getUrl();
            WebUtil.handleReceivedSslError(curUrl, HOST, handler);
        }


//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();// 接受所有网站的证书
//        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);


//            LogUtils.e("onReceivedError");
            try {
                if (!NetworkUtils.isAvailableByPing()) {
                    isError = true;
                    ToastUtil.showShort("网络中断，请检查您的网络状态！");
                    mWebView.setVisibility(View.GONE);
                    mFlError.setVisibility(View.VISIBLE);
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
            LogUtils.e("onJsAlert");
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
            mProgressBar.setProgress(newProgress);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_web);
        if (!TextUtils.isEmpty(mLoanHelp.geth5HomePageUrl())){
            HOST= mLoanHelp.geth5HomePageUrl();
        }
        initView();
        upDateApp(false);
    }





    private void initView() {

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mFlError = (FrameLayout) findViewById(R.id.view_error);
        mBtError = (Button) findViewById(R.id.bt_error_again);
        firstBar = (ProgressBar) findViewById(R.id.first_bar);
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
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

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtils.w("download--- " + url);
                Uri uri = Uri.parse(url);
                Intent step = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(step);
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mBtError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView == null) return;
                mWebView.reload();
            }
        });
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.addJavascriptInterface(new AndroidJs(), "App");//AndroidtoJS类对象映射到js的test对象
        mWebView.loadUrl(HOST);
        initFileListen();
        commitAppsInfo();

    }


    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScreenManager != null) {
            mScreenManager.startListen();
        }
//        //MobclickAgent.onPageStart(TAG);
//         //统计时长
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mScreenManager != null) {
            mScreenManager.stopListen();
        }
//        //MobclickAgent.onPageEnd(TAG); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
//        //MobclickAgent.onPause(this); //统计时长
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    private void initFileListen() {
        mScreenManager = ScreenShotListenManager.newInstance(this);
        mScreenManager.setListener(new ScreenShotListenManager.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
                screenShotEvent();
            }
        });
    }

    private void screenShotEvent() {
        mWebView.evaluateJavascript("screenShotEvent();", null);
//
    }



    public class AndroidJs {

        @JavascriptInterface
        public String getAll() {
            String json = GsonUtil.toGsonString(new JsBean());
            LogUtils.e("getAll" + json);
            return json;
        }

        //Js 回调方法  设置的电话，
        @JavascriptInterface
        public void setPhone(String phone) {
            AppData.getInstance().getUser().phone = phone;
            AppData.getInstance().save();
            //已经拿到值，进行相关操作
        }

        /**
         * @param appjson 产品信息
         */
        //Js 产品详情
        @JavascriptInterface
        public void goWebProduct(String appjson) {
//            LogUtils.e("goWebProduct" + appjson);
            ProdBean prodBean = GsonUtil.jsonToBean(appjson, ProdBean.class);
            Intent intent = new Intent(AllWebActivity.this, ThreeWebActivity.class);
            intent.putExtra(ProdBean.TAG, prodBean);
            startActivity(intent);
        }

        //Js检查更新，
        @JavascriptInterface
        public void checkUpdate() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upDateApp(true);
                }
            });
        }

        //Js 拍照，
        @JavascriptInterface
        public void takePhoto() {
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    getLocalStorageUserKey();
//                }
//            });
            Intent intent = new Intent(AllWebActivity.this, ChangeIconActivity.class);
            startActivityForResult(intent, 123);
        }


        //Js 展示图片，
        @JavascriptInterface
        public void showIcon() {
            if (!TextUtils.isEmpty(AppData.getInstance().getUser().icon)) {
                if (!new File(AppData.getInstance().getUser().icon).exists()) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPhoto(AppData.getInstance().getUser().icon);
                    }
                });
            }
        }
    }



    private void setPhoto(String imagePath) {
        if (!new File(imagePath).exists()) return;
        Map<String, String> map = new HashMap<>();
        map.put("imgPath", getSrc(imagePath));
        String src = GsonUtil.toGsonString(map);
        mWebView.evaluateJavascript("setPhoto(" + src + ");", null);
    }

    public String getSrc(String path) {
        String src = "data:image/" + getSuffix(path) + ";base64," + BitmapUtil.imageToBase64(path);
        return src;
    }

    public String getSuffix(String path) {
//        = "file:///storage/emulated/0/temp/1540029520966.jpg";
        int dot = path.lastIndexOf('.');
        if ((dot > -1) && (dot < (path.length() - 1))) {
//            System.out.println();
            return path.substring(dot + 1);
        }
        return "jpg";
    }



    public void commitAppsInfo() {
        long uploadTime = SpUtil.getLong(LoanConstant.APPS_UPLOAD_TIME, 0);
        if (System.currentTimeMillis() - uploadTime < mLoanHelp.getApplistReportTime()) {
            return;
        }
        try {

            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    AppList appList = new AppList();
                    appList.userApps = LoanAppUtils.getAppsInfo();
                    Map<String, Object> map = new HashMap<>();
                    map.put("appList", appList);
                    return GsonUtil.toGsonString(map);
                }

                @Override
                protected void onPostExecute(String string) {
                    SpUtil.putLong(LoanConstant.APPS_UPLOAD_TIME, System.currentTimeMillis());
//                    RequestClient.getInstance().submitScreenshot(new Gson().toJson(map));
//                    RequestClient.getInstance().submitAppList(string);
                    mLoanHelp.submitUserAppList(string);
                }

            };



            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 获取网络数据
        }
    }







    /**
     * 检查更新
     *
     * @param isToast
     */
    public void upDateApp(boolean isToast) {
//        if (BuildConfig.appUpdateEnable) {
        if (mLoanHelp.isUpdateApp()) {
            try {
                showUpdateDialog(mLoanHelp.isMustUpdate());
            } catch (Exception e) {
                e.printStackTrace();
//                CrashReport.postCatchedException(new Exception("检查更新--" + e.getMessage()));
                downloadApk();
            }
        } else {
            if (isToast)
                ToastUtil.showShort("已经是最新版本");
        }


    }



//    private NumberProgressBar mProgress;
    private Dialog mDialog;
    /**
     * 展示更新对话框
     *
     * @param isMustUpdate
     */
    public void showUpdateDialog(boolean isMustUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置参数
//        mDialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_update, null);
        View leftbt = v.findViewById(R.id.dialog_left_btn);
//        mProgress = v.findViewById(R.id.progress_bar);
        final TextView title = v.findViewById(R.id.dialog_title);
        View rightbt = v.findViewById(R.id.dialog_right_btn);
        final View content = v.findViewById(R.id.dialog_content);
        final View bottom = v.findViewById(R.id.bottom_button_container);
        final View bottom_button_line = v.findViewById(R.id.bottom_button_line);
        if (isMustUpdate == true) {
            leftbt.setVisibility(View.GONE);
        }
        leftbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subimtClick("app_取消升级", AppData.getInstance().getNewVersion());
                mDialog.cancel();
            }
        });

        rightbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subimtClick("app_确认升级", AppData.getInstance().getNewVersion());
//                title.setText(UIUtil.getString(R.string.app_name)+"下载进度");

//                content.setVisibility(View.GONE);
//                mProgress.setVisibility(View.VISIBLE);
//                bottom_button_line.setVisibility(View.GONE);
//                bottom.setVisibility(View.GONE);
                downloadApk();
                mDialog.cancel();
            }
        });

        builder.setTitle("应用更新")
//                .setIcon(R.mipmap.aic_launcher)
                .setView(v);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(!isMustUpdate);
        mDialog.setCancelable(!isMustUpdate);
        mDialog.show();
//        mDialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
    }

    private void downloadApk() {


        Intent intent = new Intent(this, DownloadapkService.class);
        intent.putExtra(DownloadapkService.TAG, mLoanHelp.getUpgradeUrl());
        startService(intent);
    }


    //重写返回键
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
        } else {
            doubleExit();
        }
    }


    long[] mHits = new long[2];

    public void doubleExit() {
        //        moveTaskToBack(false);
        //每点击一次 实现左移一格数据
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //给数组的最后赋当前时钟值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        long time = mHits[mHits.length - 1] - mHits[0];
        //当0出的值大于当前时间-200时  证明在200秒内点击了2次
        if (time < 2000 && time > 0) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "双击退出应用", Toast.LENGTH_SHORT).show();
        }
    }




    double[] angle = new double[3];
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            angle[0] += event.values[0] * dT;
            angle[1] += event.values[1] * dT;
            angle[2] += event.values[2] * dT;
            LoanEventDataUtil.setGyro(angle[0], angle[1], angle[2]);
        }
        timestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 123 && resultCode == Activity.RESULT_OK) {
            String imagePath = data.getStringExtra(ChangeIconActivity.KEY_FILEPATH);
            if (TextUtils.isEmpty(imagePath)) return;
            LogUtils.e(imagePath);
            AppData.getInstance().getUser().icon = imagePath;
            AppData.getInstance().save();
            setPhoto(imagePath);
        }
    }






}
