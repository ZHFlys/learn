package com.zh.learn.yunti.test4_ssl;

/**
 * 考察点：面向对象思维
 * 需求：从文件中读取需要探测的域名列表，用https协议访问,如果不支持https协议跳过，支持且过期就打印到控制台
 */

import com.zh.learn.yunti.common.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.ssl.SSLSocketImpl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author ：郑小浩
 * @description：练习四
 * @date ：2022/3/15 下午 11:20
 */

@Controller
@Slf4j
public class SSLCheck {

    @ResponseBody
    @GetMapping(value = "/test4/checkSSl")
    public ResultBody checkSSl() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:https.txt")));
        String msgLine;
        for (int urlLine = 1; (msgLine = bufferedReader.readLine()) != null; urlLine++) {
            if("".equals(msgLine)){
                continue;
            }
            log.info(String.format("第%d行：%s", urlLine, msgLine));
            String sslUrl = "https://" + msgLine;
            long expireTime = 0;

            URL url = new URL(sslUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            try {
                urlConnection.connect();
                //探测SSL是否可用
                urlConnection.getInputStream();
                //获取到期信息
                Certificate[] serverCertificates = urlConnection.getServerCertificates();
                Certificate serverCertificate = serverCertificates[0];
                X509Certificate x509Certificate = (X509Certificate) serverCertificate;
                Date notAfter = x509Certificate.getNotAfter();
                expireTime = notAfter.getTime();
            }catch (SSLException e){
                log.info(String.format("域名：%s不支持SSL！", msgLine));
                continue;
            }catch (Exception e){
                log.info(String.format("域名：%s请求发生其他异常！", msgLine));
            }

            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis > expireTime){
                log.info(String.format("域名：%s SSL已经过期！", msgLine));
            }
        }
        return ResultBody.success();
    }
}
