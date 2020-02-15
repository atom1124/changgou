package com.changgou.goods.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
@RefreshScope
public class LoadConfigController {

    @Value("${test.message}")
    private String msg;

    /***
     * 响应配置文件中的数据
     * @return
     */
    @RequestMapping(value = "/load")
    public String load(){
        System.out.println(msg);
        return msg;
    }
}