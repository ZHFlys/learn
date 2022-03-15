package com.zh.learn.yunti.test3_file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
public class FileManagerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getFileDir() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/test3/getFileDir")
                .param("prjPath","D:\\AllDaiMa\\yunti\\learn")
                .param("fileTypeS",".txt.java.xml.json")
                .param("greaterSome","20")
                .contentType(MediaType.APPLICATION_JSON);
        String res = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("测试结果：{}", res);
    }
}