package com.zh.learn.yunti.test1_dingding;

import com.zh.learn.yunti.common.ResultBody;
import com.zh.learn.yunti.common.utils.EncryptUtils;
import com.zh.learn.yunti.common.utils.SendHttps;
import com.zh.learn.yunti.common.utils.ZHConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.zh.learn.yunti.common.utils.EncryptUtils.getDingDingSignature;

/**
 * 考察点：读写文件和http请求
 * 需求：使用自己的钉钉通知群机器人给自己发提前设定好的内容。从文本中读取内容。已经发过的，重启应用也不需要再发了。发布内容为java开发规范中的条例：
 *
 * https://www.tapd.cn/21394591/documents/show/1121394591001002508?file_type=pdf&file_ext=pdf
 *
 *  示例：
 * 1. 【强制】避免通过一个类的对象引用访问此类的静态变量或静态方法，无谓增加编译器解析成 本，直接用类名来访问即可。
 */
/**
 * @author ：郑小浩
 * @description：练习一
 * @date ：2022/3/11 下午 21:24
 */
@RestController
@Slf4j
public class SendDDMessage {
    public static final String DING_DING_TOKEN =
            "https://oapi.dingtalk.com/robot/send?access_token=13dc0703c9157ab9bc8d17281e09ed5bec4290d773e39aa885aa75fa53b77857";

    private String sendMsg(String msg) throws Exception {
        Map<String, Object> msgJson = new HashMap();
        Map<String, Object> text = new HashMap();
        msgJson.put("msgtype", "text");
        text.put("content", msg);
        msgJson.put("text", text);
        ArrayList<String> dingDingSignature = EncryptUtils.getDingDingSignature();
        String dingDingUrl = String.format("%s&timestamp=%s&sign=%s", DING_DING_TOKEN, dingDingSignature.get(0), dingDingSignature.get(1));
        return SendHttps.sendPostByMap(dingDingUrl, msgJson);
    }

    @GetMapping(value = "/test1/sendDDMsg")
    public ResultBody sendDDMsg() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:book.txt")));
        String msgLine;
        String nowLine = ZHConfiguration.get("nowLine", "0");
        int nowLineInt = Integer.parseInt(nowLine);
        for (int line = 1; (msgLine = bufferedReader.readLine()) != null; line++) {
            if("".equals(msgLine) || line <= nowLineInt){
                continue;
            }
            log.info(String.format("第%d行：%s", line, msgLine));

            sendMsg(msgLine);

            ZHConfiguration.set("nowLine", "" + line);
            if(line > nowLineInt + 2){
                return ResultBody.success("模拟中断操作！");
            }
        }
        return ResultBody.success(sendMsg("消息已经发送完成！"));
    }
}
