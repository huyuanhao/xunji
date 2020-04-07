package com.jimetec.basin.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.common.lib.utils.FilePathUtil;
import com.jimetec.basin.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadapkService extends Service {
    public static final String TAG = "DownloadapkService";

    private boolean isDownload;


    /**
     * 下载进度（百分百）
     */
    private int progress = 1;
    private int handmsg = 0;//下载进度
    /**
     * apk总大小
     */
    private int length = 1;

    /**
     * 当前下载大小
     */
    private int currentLength = 0;

    /**
     * 本地通知管理类
     */
    private NotificationManager mNotifyManager;

    /**
     * 引入通知
     */
    private Notification notification;

    /**
     * 通知绑定
     */
    private NotificationCompat.Builder mBuilder;

    /**
     * 下载应用的地址
     */
    private String apkDownloadPath;

    /**
     * APP的名称
     */
    private String apkName;

    /**
     * 本地APP文件
     */
    private File appFile;
    /**
     * 下载中消息
     */
    private static final int DOWN_UPDATE = 0;

    /**
     * 下载完成消息
     */
    private static final int DOWN_OVER = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        initBuilder();
        Log.e("DownloadapkService:", "DownloadapkService--onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String downloadUrl = intent.getStringExtra(TAG);
            if (!isDownload) {
                goToDownload(downloadUrl);
            }
        }
        Log.e("DownloadapkService:", "DownloadapkService--onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDownload = false;
        Log.e("DownloadapkService:", "DownloadapkService--onDestroy");
    }

    private void initBuilder() {

        String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, appName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(appName, appName, NotificationManager.IMPORTANCE_LOW);
            mNotifyManager.createNotificationChannel(mChannel);
        }

        mBuilder.setContentTitle(appName).setSmallIcon(icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel reallyChannel = new NotificationChannel("PUSH_NOTIFY_ID", "PUSH_NOTIFY_NAME", NotificationManager.IMPORTANCE_HIGH);
//            if (mNotifyManager != null) {
//                mNotifyManager.createNotificationChannel(reallyChannel);
//            }
//        }
//
//
//        mBuilder = new NotificationCompat.Builder(this);
//        String appName = getString(getApplicationInfo().labelRes);
//        int icon = getApplicationInfo().icon;
//        mBuilder.setContentTitle(appName).setSmallIcon(icon);
//        mBuilder.setChannelId("PUSH_CHANNEL_ID");
    }


    /**
     * 开启线程下载app
     *
     * @param url
     */
    private void goToDownload(String url) {
        if (!TextUtils.isEmpty(url)) {
            apkDownloadPath = url;
            String name = getResources().getString(R.string.app_name);
            apkName =name+ ".apk";
//            appFile = new File(getApplicationContext().getExternalFilesDir("apk"), apkName);
            appFile = new File(FilePathUtil.getDiskCacheDir(this), apkName);
            isDownload = true;
            new Thread(mdownApkRunnable).start();
        }
    }

    /**
     * 下载APK的线程匿名类START
     */
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkDownloadPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                length = conn.getContentLength();
                if (length == 0) {
                    length = 1;
                }
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(appFile);
                byte buf[] = new byte[1024 * 1024];
                do {
                    int numread = is.read(buf);
                    currentLength += numread;
                    progress = (int) (((float) currentLength / length) * 100);
                    if (handmsg < progress) {
                        handmsg++;
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                    }
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);
                fos.close();
                is.close();
            } catch (Exception e) {

                e.printStackTrace();
                isDownload = false;
                stopSelf();
            }
        }
    };
    /**
     * 处理下载进度的Handler Start
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    updateProgress(handmsg);
                    super.handleMessage(msg);
                    break;
                case DOWN_OVER:
                    //下载完成之后将通知取消
                    mNotifyManager.cancel(getApplicationInfo().icon);
                    isDownload = false;
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 更新
     * setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
     *
     * @param progress
     */
    private void updateProgress(int progress) {
        mBuilder.setContentText("正在下载:" + progress + "%").setProgress(100, progress, false).setOngoing(true).setDefaults(NotificationCompat.DEFAULT_ALL);
        notification = mBuilder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        mNotifyManager.notify(getApplicationInfo().icon, notification);
    }

    /**
     * 安装apk
     * 安装apk之后停止该服务
     */
    private void installApk() {
        if (!appFile.exists()) {
            Toast.makeText(this,"要安装的文件不存在，请检查路径",Toast.LENGTH_LONG);
            return;
        }
        String fileName = appFile.getAbsolutePath();

        installPackage(fileName);
//        if (!TextUtils.isEmpty(fileName)) {
//            if (fileName.endsWith(".apk")) {
//                if(Build.VERSION.SDK_INT>=24) {
//                    File file= new File(fileName);
//                    Uri apkUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file);
//                    Intent install = new Intent(Intent.ACTION_VIEW);
//                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                    startActivity(install);
//                    stopSelf();
//                } else{
//                    Intent install = new Intent(Intent.ACTION_VIEW);
//                    install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
//                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(install);
//                    stopSelf();
//                }
//            }
//        }
    }


    /**
     * 安装文件
     *
     * @param filePath
     */
    private void installPackage(String filePath) {
        AndPermission.with(this)
                .install()
                .file(new File(filePath))
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        stopSelf();
                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        stopSelf();
                    }
                })
                .start();
    }
}
