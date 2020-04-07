package com.fanjun.keeplive;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者:capTain
 * 时间:2019-08-08 11:01
 * 描述:
 */
public class LogUtil {



    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();
    private static SimpleDateFormat getSdf() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }



    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static void input2File(Context context, String string) {


        Date now = new Date(System.currentTimeMillis());
        String format = getSdf().format(now);
        String date = format.substring(0, 10);
        final String fullPath =
                context.getExternalCacheDir() + "/log" + "-" + date + ".txt";

        final String  input =format+"---"+string;

        File file = new File(fullPath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;

                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("LogUtilss", "log to " + fullPath + " failed!");
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
