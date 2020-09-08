package com.ordercar.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {
    public static void main(String[] args) {
        System.out.println(getSHA256StrJava("linzi6557901"));
//        System.out.println(getSHA256StrJava("130"));

    }

//    $pbkdf2-sha256$100000
//    $9VHAIOPc8zOPK0BU55BTB5CHRZUL0xiVL7EmsUg9TcU=
//    $YbkfGeACpy449klAXv6+XwxCPUequadO/BKK9tGlU4g=

    public static String getSHA256StrJava(String str) {

        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
//1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
