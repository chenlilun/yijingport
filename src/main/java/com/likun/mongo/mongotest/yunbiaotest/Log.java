package com.likun.mongo.mongotest.yunbiaotest;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class Log {
    @Id
    private String id;
    private Date  crt = new Date() ;
    private String  operation ;
    private int  responseCode ;
    private String  threadName ;
    private String  msg ;
    private Long  threadId ;
    private Long  timeDiff ;
}
