package com.jimetec.xunji.share;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.ShareBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.lang.ref.WeakReference;
import java.util.List;

public class ShareActivity extends AppCompatActivity {



    public static final int FINSH = -100;
    public static final int OPEN = 100;
    public static final int DELAY = 100;//延迟
    public static final String TAG = "ShareActivity";
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    boolean isSuccess ;

    public boolean isBoardOpen;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINSH:
                    if (isSuccess) setResult(Activity.RESULT_OK);
                    finish();
                    break;
                case OPEN:
                    mShareAction.open(mConfig);
                    isBoardOpen = true;
                    break;
            }
        }
    };
    private ShareBean mShareBean;
    private ShareBoardConfig mConfig;
    private String mShowWord;
    private String mShareUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        try {
            getWindow().setGravity(Gravity.BOTTOM);
            //设置布局在底部
            //设置布局填充满宽度
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getIntent() == null) {
            finish();
            return;
        }

        try {
//            mShareBean = (ShareBean) getIntent().getSerializableExtra(TAG);
            Uri data = getIntent().getData();
            if (data!=null){
                mShareBean = new ShareBean();
                String title = data.getQueryParameter("title");
                if(!TextUtils.isEmpty(title)){
                    mShareBean.title = title;
                }
                String content = data.getQueryParameter("content");
                if(!TextUtils.isEmpty(content)){
                    mShareBean.content = content;
                }
                String url = data.getQueryParameter("url");
                if(!TextUtils.isEmpty(url)){
                    mShareBean.url = url;
                }
            }else {
                mShareBean = (ShareBean) getIntent().getSerializableExtra(TAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mShareBean == null) mShareBean = new ShareBean();
        if (TextUtils.isEmpty(mShareBean.url)){
            finish();
            return;
        }
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isBoardOpen) {
            handler.sendEmptyMessageDelayed(FINSH, 400);
        }

    }


    public void init() {
        //新建ShareBoardConfig
        mConfig = new ShareBoardConfig().setIndicatorVisibility(false).setTitleVisibility(false);

        mConfig.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
//                ToastUtil.showShort("手动取消取消");
            }
        });

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(ShareActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE
//                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                SHARE_MEDIA.ALIPAY, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
//                SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.YNOTE,
//                SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.LAIWANG, SHARE_MEDIA.LAIWANG_DYNAMIC,
//                SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.YIXIN, SHARE_MEDIA.YIXIN_CIRCLE,
//                SHARE_MEDIA.TENCENT, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.TWITTER,
//                SHARE_MEDIA.WHATSAPP, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.LINE,
//                SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.KAKAO, SHARE_MEDIA.PINTEREST,
//                SHARE_MEDIA.POCKET, SHARE_MEDIA.TUMBLR, SHARE_MEDIA.FLICKR,
//                SHARE_MEDIA.FOURSQUARE,
//                SHARE_MEDIA.MORE
        )
//                .addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
//                .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//                        if (snsPlatform.mShowWord.equals("复制文本")) {
//                            Toast.makeText(ShareActivity.this, "复制文本按钮", Toast.LENGTH_LONG).show();
//                        } else
                        mShowWord = snsPlatform.mShowWord;
                        mShareUrl = mShareBean.url;
//                                +
//                                "&invitorChannel="+2;
//                        if (snsPlatform.mShowWord.equals("复制链接")) {
//                            ClipboardManager clipboardManager =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                            clipboardManager.setText(mShareUrl);
//                            Toast.makeText(ShareActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();
//                            if (handler!=null)   handler.sendEmptyMessageDelayed(FINSH, DELAY);
//                        } else {
//                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
////                                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                            if (share_media == SHARE_MEDIA.WEIXIN){
//                                mShareUrl = mShareBean.url+"&invitorChannel="+4;
//                            }else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE){
//                                mShareUrl = mShareBean.url+"&invitorChannel="+5;
//
//                            }else if (share_media == SHARE_MEDIA.WEIXIN_FAVORITE){
//                                mShareUrl = mShareBean.url+"&invitorChannel="+4;
//
//                            }else if (share_media == SHARE_MEDIA.QQ){
//                                mShareUrl = mShareBean.url+"&invitorChannel="+6;
//
//                            }else if (share_media == SHARE_MEDIA.QZONE){
//                                mShareUrl = mShareBean.url+"&invitorChannel="+6;
////                            }

                            Bitmap bitmap =drawableToBitamp(getResources().getDrawable(R.drawable.share_img_bg));

                            UMWeb web = new UMWeb(mShareUrl);
                            web.setTitle(mShareBean.title);
                            web.setDescription(mShareBean.content);
                            web.setThumb(new UMImage(ShareActivity.this,bitmap));
                            new ShareAction(ShareActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
//                        }
//                        submitClickEvent(mShareUrl,mShowWord);

//                        ToastUtil.showShort("点击了");
                    }
                });


//        handler.sendEmptyMessageDelayed(OPEN, DELAY);

//        findViewById(R.id.shareboard_bottom_one).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mShareAction.open(mConfig);
//
//          }
//        });
        if (!AndPermission.hasPermissions(this, Permission.WRITE_EXTERNAL_STORAGE)){
            requestPermission(Permission.WRITE_EXTERNAL_STORAGE);
        }else {
            handler.sendEmptyMessageDelayed(OPEN, DELAY);
        }
//
//        requestPermission(Permission.READ_PHONE_STATE,Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_COARSE_LOCATION);


    }

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        toast(R.string.sss);
                        handler.sendEmptyMessageDelayed(OPEN, DELAY);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        handler.sendEmptyMessageDelayed(OPEN, DELAY);
//                        toast(R.string.failure);
//                        ToastUtil.show("授权失败即将退出应用");
//                        handler.sendEmptyMessageDelayed(-100, 2000);
//                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
//                            showSettingDialog(mContext, permissions);
//                        }
                    }
                })
                .start();
    }



    public  class CustomShareListener implements UMShareListener {

        private WeakReference<ShareActivity> mActivity;

        private CustomShareListener(ShareActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            isSuccess =true;
            if (platform.name().equals("WEIXIN_FAVORITE")) {
//                Toast.makeText(mActivity.get(),   " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST
                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
//                    Toast.makeText(mActivity.get(),   " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }


            if (handler!=null)            handler.sendEmptyMessageDelayed(FINSH, DELAY);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST
                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(),  " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (handler!=null)   handler.sendEmptyMessageDelayed(FINSH, DELAY);


            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

//            Toast.makeText(mActivity.get(),  " 分享取消了", Toast.LENGTH_SHORT).show();
            if (handler!=null)            handler.sendEmptyMessageDelayed(FINSH, DELAY);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null)handler.removeCallbacksAndMessages(null);
        if (mShareListener!=null)mShareListener=null;
        UMShareAPI.get(this).release();
    }




    /**
     * Drawable转换为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
