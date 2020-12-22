package com.likun.mongo.mongotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class QuartzService {
    @Autowired
    ExceptionService exceptionService ;
    @Autowired
    PackboxService packboxService ;
    //    每分钟启动
//    @Scheduled(fixedDelay = 1000*60*60*24*2)
    public void timerToNow(){
        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        exceptionService.updateHandler();
    }
    @Scheduled(fixedDelay = 1000*60*2)
    public void two(){
        packboxService.findAllBoxAndPull();
    }
}
