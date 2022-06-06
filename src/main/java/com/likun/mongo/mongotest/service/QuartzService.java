package com.likun.mongo.mongotest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class QuartzService {
    @Autowired
    ExceptionService exceptionService ;
    @Autowired
    PackboxService packboxService ;
    public  static HashMap hashMap = new HashMap() ;
    public List<String> list = Stream.of("1","2","3").collect(Collectors.toList());
    //    每分钟启动
//    @Scheduled(fixedDelay = 1000*60*60*24*2)
    public void timerToNow(){
        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        exceptionService.updateHandler();
    }
//    @Scheduled(fixedDelay = 1*60*2)
    @Scheduled(fixedDelay = 1000*60*2)
    public void two(){
        packboxService.findAllBoxAndPull();
    }

}
