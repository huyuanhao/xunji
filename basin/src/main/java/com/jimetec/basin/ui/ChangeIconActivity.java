package com.jimetec.basin.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.lib.utils.FilePathUtil;
import com.jimetec.basin.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ChangeIconActivity  extends TakePhotoActivity  implements View.OnClickListener {


    TextView mTvTake;
    TextView mTvAbuml;
    TextView mTvCancel;
    private TakePhoto mTakePhoto;
    private CropOptions mCropOptions;
    public static final String KEY_FILEPATH = "keyfilepath";
    String[] takePermissions ={Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE,Permission.CAMERA};
    String[] albumPermissions ={Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_icon);
        mTvTake = findViewById(R.id.tvTake);
        mTvAbuml = findViewById(R.id.tvAbuml);
        mTvCancel = findViewById(R.id.tvCancel);
        mTvTake.setOnClickListener(this);
        mTvAbuml.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
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

    private Uri imageUri;  //图片保存路径

    public void takePhotoPermission(){
        if (AndPermission.hasPermissions( this,takePermissions)){
            imageUri = getImageCropUri();
            mTakePhoto.onPickFromCaptureWithCrop(imageUri, mCropOptions);
            return;
        }
        if (AndPermission.hasAlwaysDeniedPermission(ChangeIconActivity.this, takePermissions)) {
            showSettingDialog(ChangeIconActivity.this, Arrays.asList(takePermissions));
            return;
        }
        imageUri = getImageCropUri();
        mTakePhoto.onPickFromCaptureWithCrop(imageUri, mCropOptions);

    }


    public void abumlpermission(){
        if (AndPermission.hasPermissions( this,albumPermissions)){
            imageUri = getImageCropUri();
            mTakePhoto.onPickFromGalleryWithCrop(imageUri, mCropOptions);
            return;
        }
        if (AndPermission.hasAlwaysDeniedPermission(ChangeIconActivity.this, takePermissions)) {
            showSettingDialog(ChangeIconActivity.this, Arrays.asList(albumPermissions));
            return;
        }

//        imageUri = getImageCropUri();
//        mTakePhoto.onPickFromGalleryWithCrop(imageUri, mCropOptions);

    }

    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
//        setPermission();
        List<String> permissionNames = Permission.transformText(context, permissions);
        String regex ="[\\[\\],]";
        String message = context.getString(R.string.message_permission_always_failed,permissionNames.toString().replaceAll(regex," "));
//        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));
        new AlertDialog.Builder(context,R.style.MyPermissionDialog)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .start();
    }




    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        final String iconPath = result.getImage().getOriginalPath();
        Log.e("http",iconPath);
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
        File file=new File(FilePathUtil.getDiskCacheDir(this), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    public void finish() {
        super.finish();

        this.overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id  == R.id.tvTake){
            takePhotoPermission();
        }else if (id  == R.id.tvTake){
            abumlpermission();
        }else if (id  == R.id.tvCancel){
            finish();
        }

    }
}
