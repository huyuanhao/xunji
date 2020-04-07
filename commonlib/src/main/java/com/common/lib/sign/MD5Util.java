package com.common.lib.sign;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SignatureException;


/**
 * 提供的MD5加密码签名类
 *
 * @author admin
 */
public class MD5Util {

    public static final String MD5_SALT = "4u5ji4jfefwef!EWE$%$%$^$%Qwk/TfdftiujjJ";

    public final static String DEFAULT_CHARSET = "UTF-8";

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param appSecret     密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String appSecret, String input_charset) {
        text = text + "&" + appSecret;
        return encrypt32(text);
//        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param appSecret     密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String appSecret, String input_charset) {
        text = text + appSecret;

        String mysign = encrypt32(text);
        ;
//        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 默认返回验签值，字符集编码为：UTF-8
     *
     * @param text
     * @param sign
     * @param appSecret
     * @return
     */
    public static boolean verify(String text, String sign, String appSecret) {
        return verify(text, sign, appSecret, DEFAULT_CHARSET);
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    public static String addSaltMd5(String value) {
        return encrypt32(value + MD5_SALT);
    }

    public static String encrypt32(String encryptStr) {
//        LogUtil.e("aes  before",encryptStr);
        MessageDigest md5;
        try {

            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        LogUtil.e("aes  after",encryptStr);

        return encryptStr;
    }


    /**
     * 加密-16位
     */
    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }

    /**
     * 加密-12位
     */
    public static String encrypt12(String encryptStr) {
        return encrypt32(encryptStr).substring(0, 12);
    }

}