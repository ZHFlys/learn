package com.zh.learn.yunti.common.utils;

/**
 * @author ：郑小浩
 * @description：配置类
 * @date ：2022/3/14 上午 9:45
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ZHConfiguration {
    private static File BASEFILE;
    static {
        try {
            BASEFILE = ResourceUtils.getFile("classpath:config.ini");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //根据Key读取Value
    public static String get(String key, String defaultValue) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(BASEFILE));
            pps.load(in);
            String value = pps.getProperty(key)+"";
            if(("null").equals(value)) {
                return defaultValue;
            }
            in.close();
            return value;
        }catch (IOException e) {
            return null;
        }
    }
    //读取Properties的全部信息
    public static Map<String, String> getAll() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Properties pps = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream(BASEFILE));
            pps.load(in);
            Enumeration en = pps.propertyNames();
            while(en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                String strValue = pps.getProperty(strKey);
                map.put(strKey, strValue);
            }
        } catch (IOException e) {
        }
        return  map;
    }

    public static void set(String pKey, String pValue) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(BASEFILE);
        //从输入流中读取属性列表（键和元素对）
        pps.load(in);
        //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream out = new FileOutputStream(BASEFILE);
        pps.setProperty(pKey, pValue);
        //以适合使用 load 方法加载到 Properties 表中的格式，
        //将此 Properties 表中的属性列表（键和元素对）写入输出流
        pps.store(out, "Update " + pKey + " name");
        out.flush();
        in.close();
        out.close();
    }

    //批量写入Properties信息
    public static void setBatch(List<String> list, Map<String,String> map) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(BASEFILE);
        InputStreamReader inputStreamReader = new InputStreamReader(in,"UTF-8");
        //从输入流中读取属性列表（键和元素对）
        pps.load(inputStreamReader);
        OutputStream out = new FileOutputStream(BASEFILE);
        for(String str : list) {
            pps.setProperty(str, map.get(str));
        }
        pps.store(out, "Update");
        out.flush();
        in.close();
        out.close();
    }
}