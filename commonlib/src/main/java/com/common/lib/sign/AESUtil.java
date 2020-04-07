package com.common.lib.sign;


import android.util.Base64;


import com.common.lib.utils.LogUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {
  

//   private static final String sKey="87654321!@#$%^&*";
   
   // 加密
   public static String Encrypt(String sSrc) {
       byte[] encrypted = new byte[0];
       String encrypt32 = MD5Util.encrypt32(sSrc);
       LogUtils.e("encrypt32",encrypt32);
       String encrypt12 = encrypt32.substring(0,12);
       LogUtils.e("encrypt12",encrypt12);
       String after4 = sSrc.substring(7);
       LogUtils.e("encrypt4",after4);
       String sKey = encrypt12 +after4;
       LogUtils.e("encryptsKey",sKey);

       try {
           if (sKey == null) {
               LogUtils.e("加密密钥为空");
               throw new RuntimeException("加密密钥不能为空!");
           }
           // 判断Key是否为16位
           if (sKey.length() != 16) {
               LogUtils.e("加密密钥长度不符,必须为16位!");
               throw new RuntimeException("加密密钥长度不符,必须为16位!");
           }
           byte[] raw = sKey.getBytes("utf-8");
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
           cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
           encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
       } catch  (Exception e) {
           e.printStackTrace();
           return  "";
       }
       String result = Base64.encodeToString(encrypted, Base64.DEFAULT);
       LogUtils.e("encryptResult",result);

       return result;
   }

   public static String getAesContent(String phone){
       String encrypt32 = MD5Util.encrypt32(phone);
       String src ="phone="+encrypt32+"&timestamp="+System.currentTimeMillis();
       return src;
   }


    public static String getAesKey(String phone){
        String encrypt32 = MD5Util.encrypt32(phone);
        LogUtils.e("encrypt32",encrypt32);
        String encrypt12 = encrypt32.substring(0,12);
        LogUtils.e("encrypt12",encrypt12);
        String after4 = phone.substring(7);
        LogUtils.e("encrypt4",after4);
        String sKey = encrypt12 +after4;
        LogUtils.e("encryptsKey",sKey);
        return sKey;
    }


    public static String encryptAttach(String phone) {
       String result ="";
        try {
            String aesKey = getAesKey(phone);
            String aesContent = getAesContent(phone);
            result = encryptString(aesKey, aesContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  result;
    }



        // 加密
    public static String encryptString(String sKey,String  content) {
        byte[] encrypted = new byte[0];

        try {
            if (sKey == null) {
                LogUtils.e("加密密钥为空");
                throw new RuntimeException("加密密钥不能为空!");
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                LogUtils.e("加密密钥长度不符,必须为16位!");
                throw new RuntimeException("加密密钥长度不符,必须为16位!");
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(content.getBytes("utf-8"));
        } catch  (Exception e) {
            e.printStackTrace();
            return  "";
        }
        String result = Base64.encodeToString(encrypted, Base64.DEFAULT);
        LogUtils.e("encryptResult",result);

        return result;
    }



}