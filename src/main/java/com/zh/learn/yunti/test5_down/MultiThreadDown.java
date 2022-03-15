package com.zh.learn.yunti.test5_down;

/**
 * 考察点：面向对象思维
 * 需求：使用Java多线程对url进行分片下载。链接:https://staticres.bookln.cn/1815649_093029b0-9e01-11ec-bf51-01edb7c2ac0d.zip
 */

import com.zh.learn.yunti.common.ResultBody;
import com.zh.learn.yunti.common.utils.DownUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * @author ：郑小浩
 * @description：练习五
 * @date ：2022/3/15 下午 13:12
 */

@Controller
@Slf4j
public class MultiThreadDown {

    @ResponseBody
    @GetMapping(value = "/test5/multiThreadDown")
    public ResultBody multiThreadDown() throws Exception {

        File file = new File("src/main/resources/1815649_093029b0-9e01-11ec-bf51-01edb7c2ac0d.zip");
        //初始化DownUtil对象
        final DownUtil downUtil = new DownUtil("https://staticres.bookln.cn/1815649_093029b0-9e01-11ec-bf51-01edb7c2ac0d.zip", file.getAbsolutePath(), 5);
        //开始下载
        downUtil.download();
        while (downUtil.getCompleteRate() < 1) {
            //每隔0.1秒查询一次任务的完成进度
            log.info("已完成：" + downUtil.getCompleteRate());
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
        }
        return ResultBody.success("多线程下载完成！");
    }
}