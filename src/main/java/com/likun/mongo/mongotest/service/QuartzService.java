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
//    @Scheduled(fixedDelay = 1000*60*2)
    public void two(){
        packboxService.findAllBoxAndPull();
    }

//    @Scheduled(cron = "0/1 * * * * *")
    public void goTest(){
        System.out.println("定时器来了"+list);
        for (int i = 0; i < list.size(); i++) {
            if(hashMap.containsKey(list.get(i))){
                System.out.println("来了，上个任务加上去的");
                continue;
            }
            hashMap.put(list.get(i) ,  UUID.randomUUID().toString().replaceAll("-","")) ;
            if(list.get(i).equals("3")){
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            hashMap.remove(list.get(i)) ;

            list.remove(list.get(i)) ;
            System.out.println("删除成功");
            hashMap.forEach((k,v)->{
                System.out.println(k);
            });
            System.out.println("list"+list);

        }
    }
}
