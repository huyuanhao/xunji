package com.jimetec.basin.event;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.Utils;
import com.ta.utdid2.device.UTDevice;

/**
 * 作者:zh
 * 时间:12/1/18 10:18 AM
 * 描述:
 */
public class LoanEventDataUtil {

    public static String utid = "";
//    public static String productName = "productName";
    public static String networktype = "";
    public static String imei = "";
    public static String mac = "";
    public static String reallyChannel = ""; //
    public static String eventChannel = ""; //
    public static String umDeviceId = "";
    public static String umMac = "";
    public static String umDeviceToken = "";
    public static String gyro = "(0,0,0)";
    public static String androidId = "";
    public static String ip = "";
    public static String versionName ="";
    public static String mUMChannel = "";
    public static String applicationId = "";
    public static int versionCode;

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        LoanEventDataUtil.ip = ip;
    }

    public static String getmUMChannel() {
        return mUMChannel;
    }

    public static void setmUMChannel(String mUMChannel) {
        LoanEventDataUtil.mUMChannel = mUMChannel;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static void setVersionName(String versionName) {
        LoanEventDataUtil.versionName = versionName;
    }

    public static int getVersionCode() {
        return versionCode;
    }

    public static void setVersionCode(int versionCode) {
        LoanEventDataUtil.versionCode = versionCode;
    }

    public static String getEfrom() {
        return efrom;
    }

    public static void setEfrom(String efrom) {
        LoanEventDataUtil.efrom = efrom;
    }

    public static String efrom = "";
    public static int power = 0; //用户当前电量，例：18


    public static String getAndroidId() {
        if (TextUtils.isEmpty(androidId)) {
            androidId = Settings.System.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
//            androidId = Settings.System.getString(Utils.getApp().getContentResolver(), Settings.System.ANDROID_ID);
        }
        return androidId;
    }

//    public static String getDeviceGson() {
//        return GsonUtil.toGsonString(new EventHeadData());
//    }

    public static String getUtid() {
        return "android_"+ UTDevice.getUtdid(Utils.getApp());
//        return "1111";
//        return getAndroidId();
//        return UTDevice.getUtdidForUpdate(Utils.getApp());
    }


    public static String getNetworktype() {
        if (TextUtils.isEmpty(networktype) || System.currentTimeMillis() % (60 * 1000) > 30000) {
            networktype = NetworkUtils.getNetworkType().getState();
        }
        return networktype;
    }

    public static String getImei() {
        if (TextUtils.isEmpty(imei)) {
            if (!TextUtils.isEmpty(helpIMEI())) {
                imei = helpIMEI();
            }
        }
        return imei;
    }


    public static String getMac() {
        return mac;
    }

    public static String getReallyChannel() {
        return reallyChannel;
    }

    public static String getEventChannel(){
        if (TextUtils.isEmpty(eventChannel)&&!TextUtils.isEmpty(reallyChannel)){
            if (reallyChannel.contains("c_")){
                eventChannel = reallyChannel.replace("c_", "");
            }else {
                eventChannel=reallyChannel;
            }
        }

        return eventChannel;
    }

    public static String getUmDeviceToken() {

        return umDeviceToken;
    }

    public static String getUmDeviceId() {
        return umDeviceId;
    }

    public static String getUmMac() {
        return umMac;
    }

    public static void setUtid(String utid) {
        LoanEventDataUtil.utid = utid;
    }



    public static void setImei(String imei) {

        LoanEventDataUtil.imei = imei;
    }

    public static void setMac(String mac) {
        LoanEventDataUtil.mac = mac;
    }

    public static void setReallyChannel(String reallyChannel) {
        LoanEventDataUtil.reallyChannel = reallyChannel;
    }

    public static void setUmDeviceId(String umDeviceId) {
        LoanEventDataUtil.umDeviceId = umDeviceId;
    }


    public static void setUmMac(String umMac) {
        LoanEventDataUtil.umMac = umMac;
        setMac(umMac);


    }

    public static void setUmDeviceToken(String umDeviceToken) {
        LoanEventDataUtil.umDeviceToken = umDeviceToken;
    }

    static double[] angle = new double[3];
    static boolean isFirst = true;

    public static void setGyro(double x, double y, double z) {
        if (Math.abs(x - angle[0]) > 0.05 || Math.abs(y - angle[1]) > 0.05 || Math.abs(z - angle[2]) > 0.05) {
            angle[0] = x;
            angle[1] = y;
            angle[2] = z;
//            LogUtil.d("Orientation: " + angle[0] + ", " + angle[1] + ", " + angle[2]);
            if (!isFirst) return;
            gyro = "(" + angle[0] + "," + angle[1] + "," + angle[2] + ")";
            isFirst = false;
        }
    }

    public static String getGyro() {
        return gyro;
    }

    /**
     * 获取手机IMEI
     *
     * @return
     */
    public static final String helpIMEI() {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }




//    public static int getPower() {
//        if (power==0 || (System.currentTimeMillis() /1000%300)<150){
//            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//            Intent batteryStatus = Utils.getApp().registerReceiver(null, ifilter);
//            //你可以读到充电状态,如果在充电，可以读到是usb还是交流电
//            // 是否在充电
//            // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
//             power = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//        }
//
//        return power;
//    }


//
    public static int[] getPower() {
        int[] ints =new int[3];
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = Utils.getApp().registerReceiver(null, ifilter);
        //你可以读到充电状态,如果在充电，可以读到是usb还是交流电
        // 是否在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        // 电池当前的电量, 它介于0和 EXTRA_SCALE之间
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        // 怎么充
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//        LogUtils.e("status"+status+ "isCharging" + isCharging + "==level" + level + "  usbCharge " + usbCharge + "acCharge" + acCharge);
//        return "status"+status+ "isCharging" + isCharging + "==level" + level + "  usbCharge " + usbCharge + "acCharge" + acCharge;
        ints[0]= (status==2?1:0);
        ints[1]=level;
        ints[2]=chargePlug;
        return ints;
//        return "status"+status+ "isCharging" + isCharging + "==level" + level + "  usbCharge " + usbCharge + "acCharge" + acCharge;
    }

    public static String getApplicationId() {
        return applicationId;
    }

    public static void setApplicationId(String applicationId) {
        LoanEventDataUtil.applicationId = applicationId;
    }
}
