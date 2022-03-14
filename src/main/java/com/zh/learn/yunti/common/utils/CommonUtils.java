package com.zh.learn.yunti.common.utils;

import java.io.*;

/**
 * @author ：郑小浩
 * @description：通用工具类
 * @date ：2022/3/14 上午 11:13
 */

public class CommonUtils {

    public static String readTxt(File file) throws IOException {
        String s = "";
        InputStreamReader in = new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader br = new BufferedReader(in);
        StringBuffer content = new StringBuffer();
        while ((s=br.readLine())!=null){
            content = content.append(s);
        }
        return content.toString();
    }
}
