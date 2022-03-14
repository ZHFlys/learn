package com.zh.learn.yunti.test1_dingding;

import com.zh.learn.yunti.common.utils.CommonUtils;
import com.zh.learn.yunti.common.utils.ZHConfiguration;
import jdk.management.resource.ResourceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileReader;

@Slf4j
@SpringBootTest
public class SendDDMessageTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void sendDDMsg() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/test1/sendDDMsg")
                .contentType(MediaType.APPLICATION_JSON);
        String res = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        log.info("注册测试结果：{}", res);
    }
}