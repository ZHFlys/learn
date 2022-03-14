package com.zh.learn.yunti.test2_daima;

/**
 *考察点：读写文件和面向对象思想
 * 需求背景：我们写了这么多年代码，不知道有多少了。需要统计指定工程的代码行数。
 * 需求：传入工程文件夹路径和需要统计的文件类型(支持传入多种类型比如java和xml)，递归统计。
 * 1.支持输出代码总量
 * 2.支持查询大于指定行数的文件，输出 代码行数,文件相对路径  并按行数倒排
 */

import com.zh.learn.yunti.common.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;

/**
 * @author ：郑小浩
 * @description：练习二
 * @date ：2022/3/14 下午 13:26
 */
@RestController
@Slf4j
public class GetAllCodeLine {

    private HashMap<String, Integer> allLineData = new HashMap<>();
    String fileTypeS = "";
    String prjPath = "";
    String greaterSome = "";

    public void getAllFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
//                log.info("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        log.info("文件夹:" + file2.getAbsolutePath());
                        getAllFile(file2.getAbsolutePath());
                    } else {
                        String fileName = file2.getName();
                        Integer typeIndex = fileName.lastIndexOf(".");
                        if(typeIndex == -1){
                            continue;
                        }
                        String suffix = fileName.substring(typeIndex);
                        if(fileTypeS.indexOf(suffix) != -1){
                            allLineData.put(file2.getAbsolutePath().replace(prjPath,""), getSingleLine(file2));
                        }
                    }
                }
            }
        } else {
            log.info("文件不存在!");
        }
    }

    private int getSingleLine(File file) throws IOException {
        if(file.exists()){
            long fileLength = file.length();
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
            lineNumberReader.skip(fileLength);
            int lines = lineNumberReader.getLineNumber();
            lineNumberReader.close();
            return lines == 0 ? 0 : lines + 1;
        }else {
            return 0;
        }
    }

    @GetMapping(value = "/test2/getCodeLine")
    public ResultBody getCodeLine(String prjPath, String fileTypeS, String greaterSome) throws IOException {
        this.prjPath = prjPath;
        this.fileTypeS = fileTypeS;
        this.greaterSome = greaterSome;
        getAllFile(prjPath);

        List<Map.Entry<String, Integer>> fileInfoList = new ArrayList<>(allLineData.entrySet());
        Collections.sort(fileInfoList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        long total = 0;
        for (Map.Entry<String, Integer> mapping : fileInfoList){
            total += mapping.getValue();
            if(mapping.getValue() > Integer.valueOf(greaterSome)){
                log.info(String.format("文件：%s，行数%d", mapping.getKey(), mapping.getValue()));
            }
        }
        log.info(String.format("项目包含文件类型【%s】的总文件个数：%s，总行数：%d", fileTypeS, fileInfoList.size(), total));
        return ResultBody.success();
    }
}
