package com.likun.mongo.mongotest.service;

import com.likun.mongo.mongotest.yunbiaotest.YunbiaoService;
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
    ExceptionService exceptionService;
    @Autowired
    YunbiaoService yunbiaoService;
    @Autowired
    PackboxService packboxService;

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void two() {
        packboxService.findAllBoxAndPull();
    }


}
