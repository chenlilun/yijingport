package com.likun.mongo.mongotest.yunbiaotest;

import com.google.gson.Gson;
import com.likun.mongo.mongotest.domain.*;
import com.likun.mongo.mongotest.interf.IGlobalCache;
import com.likun.mongo.mongotest.okhttp.OkHttpUtils;
import com.likun.mongo.mongotest.utils.response.CommonCode;
import com.likun.mongo.mongotest.utils.response.QueryResponseResult;
import com.likun.mongo.mongotest.utils.response.QueryResult;
import com.mongodb.client.result.DeleteResult;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Service
public class YunbiaoService {
    private static String token = "";
    private Gson gson = null;
    @Autowired
            private  MongoTemplate template ;

    {
        try {
            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Date dateStr ;
    Date dateEnd ;
    public void login() throws Exception {
         dateStr = new Date() ;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=-------Embt-Boundary--0757C1E41BD8DD79; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "---------Embt-Boundary--0757C1E41BD8DD79\r\nContent-Disposition: form-data; name=\"json\"\r\n\r\n\r\n{\"dynPassword\":\"\",\"deviceId\":\"B6723FCDB6518843F90DA15DB5A165D4\",\"password\":\"YTiyxMEgUIbipe5R173lWNkjCjjnAWWka72A+FnEAyDWEuD3UsriyQfyLWvWrhCI\",\"account\":\"dKOXEhJDJ9ETTtd9p2jyvg==\"}\r\n---------Embt-Boundary--0757C1E41BD8DD79--");
        Request request = new Request.Builder()
                .url("http://wms.hengyi.com:8400/10002/login")
                .method("POST", body)
                .addHeader("Content-Type", "multipart/form-data; boundary=-------Embt-Boundary--4017BD1C4EE1A590; charset=UTF-8")
                .addHeader("Accept", "application/x-lato")
                .addHeader("X-Eversheet-Agent", "Eversheet/3.1.21.79")
                .build();
        Call call = client.newCall(request);   //建立一个会话
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                System.out.println("======error======");
                e.printStackTrace();
                Log log = new Log() ;
                log.setTimeDiff(dateEnd.getTime()-dateStr.getTime());
                log.setOperation("登录");
                log.setMsg(e.getMessage()+"登录错误");
                log.setThreadName(Thread.currentThread().getName());
                log.setThreadId(Thread.currentThread().getId());
                log.setResponseCode(400);
                template.save(log) ;
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (gson == null) gson = new Gson();
                Token t = gson.fromJson(response.body().string(), Token.class);
                System.out.println("((((("+t.toString());
                token = t.token;
                 dateEnd = new Date() ;
                System.out.println("时间差***"+(dateEnd.getTime()-dateStr.getTime()));
                Log log = new Log() ;
                log.setTimeDiff(dateEnd.getTime()-dateStr.getTime());
                log.setOperation("登录");
                log.setMsg(t.toString());
                log.setThreadName(Thread.currentThread().getName());
                log.setThreadId(Thread.currentThread().getId());
                log.setResponseCode(response.code());
                template.save(log) ;
            }
        });
    }


    public void jianPei(String number,String packageBox) throws  Exception{
        dateStr = new Date() ;
        System.out.println("token********"+token);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=-------Embt-Boundary--033210EC4C18469E");
        RequestBody body = RequestBody.create(mediaType, "---------Embt-Boundary--033210EC4C18469E\r\nContent-Disposition:form-data;name=\"formJson\"\r\n\r\n{\"classTemplateName\":\"x\\u51fa\\u5e93\\u62e3\\u914d\\u63d0\\u4ea4\",\"dataTableList\":[{\"deletedRecList\":[],\"updatedRecList\":[],\"insertRecList\":[[\""+packageBox+"\",\""+packageBox+"\",\""+number+"\",\"000010\",1,\"\\u62e3\\u914d\",\"9110\",\"PP\",\"PPL1\",null,\"\\u6d45\\u68d5\",1,\"2022-08-22\",null,null,\"9A57\",\"9110220822\",\"000000001000700133\",\"PD014013\",\"AA\",\"72dtex\\/72f\",\"POY\",\"\\u8fd4\\u4fee\\u5165\\u5e93\",1.1,\"9110\",\"P1\",\"\\u666e\\u901a\\u6728\\u6258\",\"2022-08-23\",\"\\u9648\\u7acb\\u5764\"]],\"tableCaption\":\"x\\u51fa\\u5e93\\u62e3\\u914d\\u63d0\\u4ea4\",\"fieldList\":[\"f1\",\"f2\",\"f24\",\"f25\",\"f26\",\"f27\",\"f29\",\"f30\",\"f31\",\"f32\",\"f33\",\"f34\",\"f35\",\"f36\",\"f37\",\"f38\",\"f39\",\"f40\",\"f41\",\"f42\",\"f43\",\"f44\",\"f45\",\"f46\",\"f47\",\"f48\",\"f49\",\"f50\",\"f51\"]}],\"latoVersion\":0,\"templateUrl\":\"\",\"formId\":\"0\",\"url\":\"\"}\r\n---------Embt-Boundary--033210EC4C18469E—-");
        Request request = new Request.Builder()
                .url("http://wms.hengyi.com:8400/10002/x出库拣配提交/new")
                .method("POST", body)
                .addHeader("Content-Type", "multipart/form-data; boundary=-------Embt-Boundary--033210EC4C18469E")
                .addHeader("Accept", "application/x-lato")
                .addHeader("token", token)
                .addHeader("Cookie", "latoAccount=18011514; latoDepartCode=\"\"; latoDepartFullName=\"\"; latoDepartName=\"\"; latoDomainPath=wms.hengyi.com%3A8400%2F10002; latoOrgFullName=\"\"; latoOrgName=\"\"; latoToken=12776605-b70e-46be-a9dc-68f9dd6a3eeb; latoUser=%E9%99%88%E7%AB%8B%E5%9D%A4; PLAY_FLASH=%00httpUrl%3Ahttp%3A%2F%2Fwms.hengyi.com%3A8400%2F10002%2Fx%25E5%2587%25BA%25E5%25BA%2593%25E6%258B%25A3%25E9%2585%258D%25E6%258F%2590%25E4%25BA%25A4%2Fnew%00%00interface%3A10002%00")
                .build();
        Response response = client.newCall(request).execute();

        if (gson == null) gson = new Gson();
        Token t = gson.fromJson(response.body().string(), Token.class);
        dateEnd = new Date() ;
        System.out.println("时间差***"+(dateEnd.getTime()-dateStr.getTime()));
        Log log = new Log() ;
        log.setTimeDiff(dateEnd.getTime()-dateStr.getTime());
        log.setOperation("拣配");
        log.setThreadName(Thread.currentThread().getName());
        log.setThreadId(Thread.currentThread().getId());
        log.setResponseCode(response.code());
        if(response.code()!=200){
            log.setMsg(t.getException().toString());
            login();
        }else {
            log.setMsg(t.toString());
        }
        template.save(log) ;
    }
    public void chongxiao(String number,String packageBox) throws  Exception{
        dateStr = new Date() ;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=-------Embt-Boundary--033210EC4C18469E");
        RequestBody body = RequestBody.create(mediaType, "---------Embt-Boundary--033210EC4C18469E\r\nContent-Disposition: form-data; name=\"formJson\"\r\n\r\n{\"classTemplateName\":\"x\\u51fa\\u5e93\\u62e3\\u914d\\u63d0\\u4ea4\",\"dataTableList\":[{\"deletedRecList\":[],\"updatedRecList\":[],\"insertRecList\":[[\""+packageBox+"\",\""+packageBox+"\",\""+number+"\",\"000010\",1,\"\\u51b2\\u9500\",\"9110\",\"PPX\",\"PPL1\",null,\"\\u6d45\\u68d5\",1,\"2022-08-22\",null,null,\"9A57\",\"9110220822\",\"000000001000700133\",\"PD014013\",\"AA\",\"72dtex\\/72f\",\"POY\",\"\\u8fd4\\u4fee\\u5165\\u5e93\",1.1,\"9110\",\"P1\",\"\\u666e\\u901a\\u6728\\u6258\",\"2022-08-23\",\"\\u5f90\\u5fe0\"]],\"tableCaption\":\"x\\u51fa\\u5e93\\u62e3\\u914d\\u63d0\\u4ea4\",\"fieldList\":[\"f1\",\"f2\",\"f24\",\"f25\",\"f26\",\"f27\",\"f29\",\"f30\",\"f31\",\"f32\",\"f33\",\"f34\",\"f35\",\"f36\",\"f37\",\"f38\",\"f39\",\"f40\",\"f41\",\"f42\",\"f43\",\"f44\",\"f45\",\"f46\",\"f47\",\"f48\",\"f49\",\"f50\",\"f51\"]}],\"latoVersion\":0,\"templateUrl\":\"\",\"formId\":\"0\",\"url\":\"\"}\r\n---------Embt-Boundary--033210EC4C18469E--\r\n");
        Request request = new Request.Builder()
                .url("http://wms.hengyi.com:8400/10002/x出库拣配提交/new")
                .method("POST", body)
                .addHeader("Content-Type", "multipart/form-data; boundary=-------Embt-Boundary--033210EC4C18469E")
                .addHeader("Accept", "application/x-lato")
                .addHeader("token", token)
                .addHeader("Cookie", "latoAccount=18011514; latoDepartCode=\"\"; latoDepartFullName=\"\"; latoDepartName=\"\"; latoDomainPath=wms.hengyi.com%3A8400%2F10002; latoOrgFullName=\"\"; latoOrgName=\"\"; latoToken=12776605-b70e-46be-a9dc-68f9dd6a3eeb; latoUser=%E9%99%88%E7%AB%8B%E5%9D%A4; PLAY_FLASH=%00httpUrl%3Ahttp%3A%2F%2Fwms.hengyi.com%3A8400%2F10002%2Fx%25E5%2587%25BA%25E5%25BA%2593%25E6%258B%25A3%25E9%2585%258D%25E6%258F%2590%25E4%25BA%25A4%2Fnew%00%00interface%3A10002%00")
                .build();
        Response response = client.newCall(request).execute();
        if (gson == null) gson = new Gson();
        Token t = gson.fromJson(response.body().string(), Token.class);
        dateEnd = new Date() ;
        System.out.println("时间差***"+(dateEnd.getTime()-dateStr.getTime()));
        Log log = new Log() ;
        log.setTimeDiff(dateEnd.getTime()-dateStr.getTime());
        log.setOperation("取消拣配");
        log.setThreadName(Thread.currentThread().getName());
        log.setThreadId(Thread.currentThread().getId());
        log.setResponseCode(response.code());
        if(response.code()!=200){
            log.setMsg(t.getException().toString());
            login();
        }else {
            log.setMsg(t.toString());
        }
        template.save(log) ;
    }

   public void  doSomething(String number,List<String> packages ){
       for (int i = 0; i < packages.size(); i++) {
           CompletableFuture<Void> jian = jian(number, packages.get(i));
           CompletableFuture<Void> chongxiao = chong(number, packages.get(i)) ;
           jian.thenCombineAsync(chongxiao ,(__, tf) -> {
               return "上茶:" + tf;
           }) ;
       }

   }

   public  CompletableFuture<Void> jian (String number,String packageBox){
       CompletableFuture<Void> jianpei = CompletableFuture
               .runAsync(() -> {
                   try {
                       jianPei(number,packageBox) ;
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
       return jianpei ;
   }
   public  CompletableFuture<Void> chong (String number,String packageBox){
       CompletableFuture<Void> chong = CompletableFuture
               .runAsync(() -> {
                   try {
                       chongxiao(number,packageBox); ;
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
       return chong ;
   }

}
