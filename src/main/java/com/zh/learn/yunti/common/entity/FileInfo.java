package com.zh.learn.yunti.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ：郑小浩
 * @description：封装文件信息类
 * @date ：2022/3/15 上午 8:53
 */

@Getter
@Setter
public class FileInfo implements Serializable {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件路径
     * */
    private String fileName;
    /**
     * 文件所在目录
     */
    private String fileParent;
    /**
     * 文件子目录
     */
    private String fileSub;
    /**
     * 是否为文件
     */
    private Boolean isDir;
}
