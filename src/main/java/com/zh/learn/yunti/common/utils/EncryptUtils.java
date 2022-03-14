package com.zh.learn.yunti.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author ：郑小浩
 * @description：TODO
 * @date ：2022/3/11 下午 21:31
 */

public class EncryptUtils {

    public static ArrayList<String> getDingDingSignature() throws Exception {
        ArrayList<String> signAll = new ArrayList<>();

        Long timestamp = System.currentTimeMillis();
        signAll.add(timestamp.toString());

        String secret = "SEC9e02bd81613782cef8dc933147bead36903851167fc04aa543301448bd7f845c";
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        signAll.add(sign);

        return signAll;
    }
}
