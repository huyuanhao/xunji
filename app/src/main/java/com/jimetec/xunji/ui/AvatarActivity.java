package com.jimetec.xunji.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.baseview.base.BaseActivity;
import com.common.lib.utils.FilePathUtil;
import com.common.lib.utils.Utils;
import com.jimetec.xunji.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvatarActivity extends TakePhotoActivity {




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_icon);
//    }

    public static final String TAG = "ChangeIconActivity";
    @BindView(R.id.tvTake)
    TextView mTvTake;
    @BindView(R.id.tvAbuml)
    TextView mTvAbuml;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    private TakePhoto mTakePhoto;
    private CropOptions mCropOptions;
    public static final String KEY_FILEPATH = "keyfilepath";
//    String[] takePermissions ={Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE,Permission.CAMERA};
//    String[] albumPermissions ={Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            String referer = getIntent().getStringExtra(BaseActivity.EVENT_REFERER);
//            EventHelp.submitViewEvent("头像",referer);
        }
        mTakePhoto = getTakePhoto();
        mCropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
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
    }

    private Uri imageUri= getImageCropUri();;  //图片保存路径
    @OnClick({R.id.tvTake, R.id.tvAbuml, R.id.tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTake:
                takePhotoPermission();
                break;
            case R.id.tvAbuml:
                abumlpermission();
                break;
            case R.id.tvCancel:
                finish();
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart(TAG);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd(TAG); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        //MobclickAgent.onPause(this);
    }

    public void takePhotoPermission(){
        mTakePhoto.onPickFromCaptureWithCrop(imageUri, mCropOptions);

    }

    public void abumlpermission(){

        mTakePhoto.onPickFromGalleryWithCrop(imageUri, mCropOptions);
//        if (AndPermission.hasPermissions( this,albumPermissions)){
//            return;
//        }
//        if (AndPermission.hasAlwaysDeniedPermission(ChangeIconActivity.this, takePermissions)) {
//            showSettingDialog(ChangeIconActivity.this, Arrays.asList(albumPermissions));
//            return;
//        }

//        imageUri = getImageCropUri();
//        mTakePhoto.onPickFromGalleryWithCrop(imageUri, mCropOptions);

    }



    /**
     * Set permissions.
     */
    private void setPermission() {
//        AndPermission.with(this)
//                .runtime()
//                .setting()
//                .start();
    }




    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        final String iconPath = result.getImage().getOriginalPath();
        //Toast显示图片路径
//        Toast.makeText(this, "imagePath:" + iconPath, Toast.LENGTH_SHORT).show();
//        //Google Glide库 用于加载图片资源
//        Glide.with(this).load(iconPath).into(imageView);
//        tvAlbum.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        Intent intent = new Intent();
        intent.putExtra(KEY_FILEPATH,iconPath);
        setResult(RESULT_OK,intent);
        finish();
//            }
//        },100);

    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file=new File(FilePathUtil.getDiskCacheDir(Utils.getApp()), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,0);
    }




}
