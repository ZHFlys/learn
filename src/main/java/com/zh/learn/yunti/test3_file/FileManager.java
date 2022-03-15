package com.zh.learn.yunti.test3_file;

/**
 * 考察点：面向对象思维
 * 需求：用面向对象的思路：索引本地电脑中指定目录下的所有文件（包含子文件夹），做一个文件管理器,需要记录:文件路径，大小,名称。
 * 要求：需要启动的时候一次性索引好，生成索引文件，下次启动可以读取索引文件。支持重新索引
 * 控制台模式：
 * 使用控制台运行，输入数字可以进入指定的目录，并展示目录下的文件。文件类型，文件名，文件创建时间 大小。
 * 使用控制台运行，支持文件名关键词搜索和大小搜索（单位MB）和类型搜索。
 *
 * BS运行：
 * 有开发界面能力的同学可以做成BS模式
 * ps:有能力的同学可以把生成索引改为多线程
 */

import com.zh.learn.yunti.common.entity.FileInfo;
import com.zh.learn.yunti.common.utils.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：郑小浩
 * @description：练习三
 * @date ：2022/3/14 下午 16:23
 */

@Controller
@Slf4j
public class FileManager {

    private List<FileInfo> allFileData = null;
    private String prjPath = "";

    private String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public void getAllFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
//                log.info("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    FileInfo fileInfo = new FileInfo();
                    String filePath = file2.getAbsolutePath().replace(prjPath, "");
                    fileInfo.setFilePath(filePath);
                    String fileParent = path.replace(prjPath, "");
                    fileParent = "".equals(fileParent) ? "/" : fileParent;
                    fileInfo.setFileParent(EncryptUtils.getMD5(fileParent));
                    if (file2.isDirectory()) {
//                        log.info("文件夹:" + file2.getAbsolutePath());
                        fileInfo.setFileSize("0");
                        fileInfo.setFileName("");
                        fileInfo.setIsDir(true);
                        fileInfo.setFileSub(EncryptUtils.getMD5(filePath));
                        getAllFile(file2.getAbsolutePath());
                    } else {
                        fileInfo.setFileName(file2.getName());
                        fileInfo.setFileSize(getPrintSize(file2.length()));
                        fileInfo.setIsDir(false);
                    }
                    allFileData.add(fileInfo);
                }
            }
        } else {
            log.info("文件不存在!");
        }
    }

    private void objToFile() throws IOException {
        log.info("重新建立文件系统索引中。");
//        File file = ResourceUtils.getFile("classpath:allFileData.data");
        String path = "src/main/resources/allFileData.data";
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        FileInfo[] fileObjS = new FileInfo[allFileData.size()];
        allFileData.toArray(fileObjS);
        out.writeObject(fileObjS);
    }

    private void fileToObj() throws Exception {
        log.info("读取文件系统索引中。");
        File file = ResourceUtils.getFile("classpath:allFileData.data");
        ObjectInputStream out = new ObjectInputStream(new FileInputStream(file));
        FileInfo[] obj = (FileInfo[]) out.readObject();
        allFileData = Arrays.asList(obj);
    }

    @GetMapping(value = "/test3/getFileDir")
    public String getFileDir(String fileParent, String oldPath, ModelMap map) throws IOException {
        allFileData = new ArrayList<>();
        this.prjPath = "D:\\AllDaiMa\\yunti\\learn";
        if(!StringUtils.hasText(fileParent)){
            fileParent = EncryptUtils.getMD5("/");
        }
        String fileParentFinal = fileParent;
        //从文件获取文件索引，获取失败则建立索引
        try {
            fileToObj();
        } catch (Exception e) {
            e.printStackTrace();
            getAllFile(prjPath);
            objToFile();
        }
        //过滤出当前目录，可优化为层级(TODO)
        List<FileInfo> fileInfos = allFileData.stream().filter(fileInfo -> fileInfo.getFileParent().equals(fileParentFinal)).collect(Collectors.toList());

        map.put("oldPath", oldPath);
        map.put("allFile", fileInfos);
        return "showFile";
    }
}
