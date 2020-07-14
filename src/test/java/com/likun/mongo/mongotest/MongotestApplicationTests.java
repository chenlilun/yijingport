package com.likun.mongo.mongotest;

import com.likun.mongo.mongotest.dao.ExceptionRepository;
import com.likun.mongo.mongotest.domain.*;
import com.likun.mongo.mongotest.domain.Class;
import com.likun.mongo.mongotest.utils.MongoDBPageModel;
import com.likun.mongo.mongotest.utils.SpringbootMongoDBPageable;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@SpringBootTest
class MongotestApplicationTests {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void t1() {
        List<User> userList = template.findAll(User.class);

        if (userList != null && userList.size() > 0) {
            userList.forEach(user -> {
                System.out.println(user.toString());
            });
        }
    }

    @Test
    void add() {
        Class u = new Class();
        u.setClassName("九班");
        template.save(u);

    }

    @Test
    void findById() {
        Teacher u = template.findById("5ef8b4b0cd40580336f9bf85", Teacher.class);
        System.out.println(u);
//        Query query = new Query(Criteria.where("na.me")is("陈老师")


//        );

  /*      List<Teacher> users = template.find(query, Teacher.class);
        users.stream().forEach(System.out::print);*/
    }

    @Test
    void testQueary() {
/*        Query query  = new Query();
        query.addCriteria( new Criteria("_id").is("5d287fa3fc15c200017d2d04"));
        ExceptionBean all = template.findOne(query  ,  ExceptionBean.class,"T_ExceptionRecord");
        System.out.println(all);*/
        ExceptionBean t_exceptionRecord =  template.findById("5d284510fc15c2000149d8fe", ExceptionBean.class);
        System.out.println(t_exceptionRecord);
/*        Optional<ExceptionBean> byId = exceptionRepository.findById("5d2844dcfc15c2000149d115");
        System.out.println(byId.get());*/

    }

    /**
     * 模糊查询
     * 模糊查询以 【^】开始 以【$】结束 【.*】相当于Mysql中的%
     *
     * @return ApiResponse
     */
    @Test
    void findStudentsLikeName() {
        String regex = String.format("%s%s%s", "^.*", "zs", ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> students = template.find(query, User.class);
//        return DefinedBy.Api.ok(students);
        students.stream().forEach(System.out::print);
    }

    @Test
    void delete() {
        Query query = new Query(Criteria.where("name").is("zs1"));
        template.remove(query, "teacher");
    }

    @Test
    void update() {
        Query query = new Query(Criteria.where("name").is("wangwu2"));
        Update update = new Update();
        update.set("name", "lisi");
        UpdateResult updateResult = template.upsert(query, update, User.class, "user");
        System.out.println(updateResult.getModifiedCount());
        System.out.println(updateResult.getUpsertedId());
        System.out.println(updateResult.getMatchedCount());
    }

    //分页查询
    @Test
    void PageQuery() {
        //利用工具类拼装分页信息
        SpringbootMongoDBPageable pageable = new SpringbootMongoDBPageable();
        MongoDBPageModel pm = new MongoDBPageModel();
        pm.setPagesize(3);
        pm.setPagenumber(1);
        List<Sort.Order> orders = new ArrayList<>();  //排序信息
        orders.add(new Sort.Order(Sort.Direction.DESC, "age"));
        pm.setSort(Sort.by(orders));
        pageable.setPage(pm);
      /*  pm.setSort(Sort.by(new Sort.Order(Sort.Direction.DESC,"age")));
        pageable.setPage(pm);*/


        //拼装查询信息
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.where("age").gte(20).lte(100));//检索1-18岁的
        query.addCriteria(criteria.where("name").regex("zs"));//模糊查询名字
        Long count = template.count(query, User.class);//查询总记录数
        List<User> list = template.find(query.with(pageable), User.class, "user");

        System.out.println(count);
        list.stream().forEach(user -> {
            System.out.println(user + "\n");
        });

    }

    //分页聚合查询（多表多条件关联分页查询）
    @Test
    public void PageQuery2() throws Exception {
        //mongodb中有两个表，一个是人物表 一个是宠物表，一个人可以有多个宠物
        //人物表字段为 String id, Integer age,String remark;
        //宠物表字段为 String id, String manId,String age,String remark;
        //拼装分页信息
        SpringbootMongoDBPageable pageable = new SpringbootMongoDBPageable();
        MongoDBPageModel pm = new MongoDBPageModel();
        pm.setPagesize(3);
        pm.setPagenumber(1);
        List<Sort.Order> orders = new ArrayList<>();  //排序信息
        orders.add(new Sort.Order(Sort.Direction.DESC, "age"));
        pm.setSort(Sort.by(orders));
        pageable.setPage(pm);

        //拼装关联信息
        LookupOperation lookupOperation = LookupOperation.newLookup().
                from("dogData").//关联表名
                localField("_id").//关联字段
                foreignField("manId").//主表关联字段对应的次表字段
                as("dogs");//查询结果集合名

        //拼装具体查询信息
        //次表
        Criteria ordercri = Criteria.where("dogs").not().size(0);//只查询有宠物的人
        ordercri.and("age").gte(1).lte(5);//只查询1岁到5岁的宠物
        AggregationOperation match = Aggregation.match(ordercri);
        //主表
        Criteria qqq = Criteria.where("name").regex("文");//只查询名字中带有文的人
        AggregationOperation match1 = Aggregation.match(qqq);

        //分页查询
        Aggregation aggregation = Aggregation.newAggregation(match1, lookupOperation, match, Aggregation.sort(pageable.getSort()),//排序
                Aggregation.skip(pageable.getPageNumber() > 1 ? (pageable.getPageNumber() - 1) * pageable.getPageSize() : 0),//pagenumber
                Aggregation.limit(pageable.getPageSize()));//pagesize
        //总数查询
        Aggregation counts = Aggregation.newAggregation(match1, lookupOperation, match);
        int count = template.aggregate(counts, "manEntry", BasicDBObject.class).getMappedResults().size();
        List<BasicDBObject> results = template.aggregate(aggregation, "manEntry", BasicDBObject.class).getMappedResults();
        //查询出的结果集为BasicDBObject类型
        //解析过程
        for (BasicDBObject b : results
        ) {
            //转化为jsonobject对象
            JSONObject jsonObject = new JSONObject(b);
            String id = jsonObject.get("id").toString();
            Integer age = ((int) jsonObject.get("age"));
            String remark = jsonObject.get("remark").toString();
            //转化为jsonarray
            JSONArray dogs = jsonObject.getJSONArray("dogs");
            if (dogs.length() > 0) {
                for (int i = 0; i < dogs.length(); i++) {
                    JSONObject job = dogs.getJSONObject(i);
                    String dogId = job.get("id").toString();
                    String manId = job.get("manId").toString();
                }
            }

        }
    }


    @Test
    void queryException() {
        Criteria c2 = Criteria.where("cdt").gte(strToDateLong("2020-06-03 00:00:00")).lte(strToDateLong("2020-06-04 00:00:00"));
        AggregationOperation match2 = Aggregation.match(c2);
        //拼装关联信息
        LookupOperation lookupOperation = LookupOperation.newLookup().
                from("T_LineMachine").//关联表名
                localField("lineMachine").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord");//查询结果集合名
        UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord"));
        LookupOperation lookupOperation2 = LookupOperation.newLookup().
                from("T_Silk").//关联表名
                localField("silk").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord");//查询结果集合名
        UnwindOperation unwindOperation2 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord"));
        LookupOperation lookupOperation3 = LookupOperation.newLookup().
                from("T_Batch").//关联表名
                localField("T_SilkT_ExceptionRecord.batch").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord_T_Batch");//查询结果集合名
        UnwindOperation unwindOperation3 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord_T_Batch"));
        LookupOperation lookupOperation4 = LookupOperation.newLookup().
                from("T_SilkException").//关联表名
                localField("exception").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkException_T_ExceptionRecord");//查询结果集合名
        UnwindOperation unwindOperation4 = new UnwindOperation(Fields.field("$T_SilkException_T_ExceptionRecord"));
        LookupOperation lookupOperation5 = LookupOperation.newLookup().
                from("T_Line").//关联表名
                localField("T_LineMachineT_ExceptionRecord.line").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord_TLine");//查询结果集合名
        UnwindOperation unwindOperation5 = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord_TLine"));
        LookupOperation lookupOperation6 = LookupOperation.newLookup().
                from("T_Workshop").//关联表名
                localField("T_LineMachineT_ExceptionRecord_TLine.workshop").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord_TLine_Tworkshop");//查询结果集合名
        UnwindOperation unwindOperation6 = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord_TLine_Tworkshop"));
        LookupOperation lookupOperation7 = LookupOperation.newLookup().
                from("T_Product").//关联表名
                localField("T_SilkT_ExceptionRecord_T_Batch.product").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord_T_Batch_T_Product");//查询结果集合名
        UnwindOperation unwindOperation7 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord_T_Batch_T_Product"));

        Criteria c1 = Criteria.where("T_LineMachineT_ExceptionRecord_TLine_Tworkshop.code").is("C");//只查询名字中带有文的人
        AggregationOperation match1 = Aggregation.match(c1);
        GroupOperation groupOperation1 = Aggregation.group(
                Fields.fields( "$T_SilkT_ExceptionRecord_T_Batch.batchNo",
                        "$T_SilkException_T_ExceptionRecord.name",
                        "$T_SilkT_ExceptionRecord_T_Batch.spec"
                        , "$T_LineMachineT_ExceptionRecord_TLine._id"
                )

        ).count().as("exceptionSum")
                .first("$T_SilkT_ExceptionRecord_T_Batch.batchNo").as("batchNo")
                .first("$T_SilkException_T_ExceptionRecord.name").as("exceptionname")
                .first("$T_SilkT_ExceptionRecord_T_Batch.spec").as("spec")
                .first("$T_SilkT_ExceptionRecord_T_Batch_T_Product.name").as("productName")
                .first("$T_LineMachineT_ExceptionRecord_TLine.name").as("lineName")
                .first("$T_LineMachineT_ExceptionRecord_TLine._id").as("lineId");
//                .first("$T_LineMachineT_ExceptionRecord_TLine._id").as("lineId");
        GroupOperation groupOperation2 = Aggregation.group("_id.batchNo" , "_id.spec" , "_id._id" , "$lineName" ,"$productName"
                /*,
                "$T_SilkT_ExceptionRecord_T_Batch.spec"

                , "$T_SilkT_ExceptionRecord_T_Batch.spec"*/
        ).push(new BasicDBObject("exceptionname", "$_id.name").append("exceptionSum", "$exceptionSum").append("lineName", "$lineName")).as("exceptions");
        SortOperation productName = Aggregation.sort(Sort.Direction.DESC, "_id.productName");
        Aggregation aggregation = Aggregation.newAggregation(match2, lookupOperation, unwindOperation, lookupOperation2, unwindOperation2, lookupOperation3, unwindOperation3, lookupOperation4
                , unwindOperation4, lookupOperation5, unwindOperation5, lookupOperation6,
                unwindOperation6, lookupOperation7, unwindOperation7, match1, groupOperation1
                     , groupOperation2 ,productName);

        List<BasicDBObject> results = template.aggregate(aggregation, "T_ExceptionRecord", BasicDBObject.class).getMappedResults();
        //查询出的结果集为BasicDBObject类型
        System.out.println(results);
        //解析过程
/*        for (BasicDBObject b :results
        ) {
            //转化为jsonobject对象
            JSONObject jsonObject = new JSONObject(b);
            String id = jsonObject.get("id").toString();
            Integer age = ((int) jsonObject.get("age"));
            String remark = jsonObject.get("remark").toString();
            //转化为jsonarray
            JSONArray dogs = jsonObject.getJSONArray("dogs");
            if (dogs.length() > 0) {
                for (int i = 0; i < dogs.length(); i++) {
                    JSONObject job = dogs.getJSONObject(i);
                    String dogId = job.get("id").toString();
                    String manId = job.get("manId").toString();
                }
            }

        }*/
    }

    public static Date dateToISODate(Date dateStr) {
        Date parse = null;
        try {
            // 解析字符串时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            parse = format.parse(format.format(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static Date strToDateLong(String strDate) {
        Date strtodate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strtodate;
    }
    public List<BasicDBObject>    queryBigScreen ( ParamsBean paramsBean ){
        Criteria c2 = Criteria.where("cdt").gte(getBeforeTwoDay()).lte(new Date());
        AggregationOperation match2 = Aggregation.match(c2);
        //拼装关联信息
        LookupOperation lookupOperation = LookupOperation.newLookup().
                from("T_LineMachine").//关联表名
                localField("lineMachine").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord");//查询结果集合名
        UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord"));
        LookupOperation lookupOperation2 = LookupOperation.newLookup().
                from("T_Silk").//关联表名
                localField("silk").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord");//查询结果集合名
        //todo   Sort.by(Sort.Order.by("sortBy"))
        SortOperation sortCdt = new SortOperation(Sort.by(Sort.Order.by("cdt").with(Sort.Direction.DESC)));
        UnwindOperation unwindOperation2 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord"));
        LookupOperation lookupOperation3 = LookupOperation.newLookup().
                from("T_Batch").//关联表名
                localField("T_SilkT_ExceptionRecord.batch").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord_T_Batch");//查询结果集合名
        UnwindOperation unwindOperation3 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord_T_Batch"));
        LookupOperation lookupOperation4 = LookupOperation.newLookup().
                from("T_SilkException").//关联表名
                localField("exception").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkException_T_ExceptionRecord");//查询结果集合名
        UnwindOperation unwindOperation4 = new UnwindOperation(Fields.field("$T_SilkException_T_ExceptionRecord"));
        LookupOperation lookupOperation5 = LookupOperation.newLookup().
                from("T_Line").//关联表名
                localField("T_LineMachineT_ExceptionRecord.line").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord_TLine");//查询结果集合名
        UnwindOperation unwindOperation5 = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord_TLine"));
        LookupOperation lookupOperation6 = LookupOperation.newLookup().
                from("T_Workshop").//关联表名
                localField("T_LineMachineT_ExceptionRecord_TLine.workshop").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_LineMachineT_ExceptionRecord_TLine_Tworkshop");//查询结果集合名
        UnwindOperation unwindOperation6 = new UnwindOperation(Fields.field("$T_LineMachineT_ExceptionRecord_TLine_Tworkshop"));
        LookupOperation lookupOperation7 = LookupOperation.newLookup().
                from("T_Product").//关联表名
                localField("T_SilkT_ExceptionRecord_T_Batch.product").//关联字段
                foreignField("_id").//主表关联字段对应的次表字段
                as("T_SilkT_ExceptionRecord_T_Batch_T_Product");//查询结果集合名
        UnwindOperation unwindOperation7 = new UnwindOperation(Fields.field("$T_SilkT_ExceptionRecord_T_Batch_T_Product"));

        Criteria c1 = Criteria.where("T_LineMachineT_ExceptionRecord_TLine_Tworkshop.code").is(paramsBean.getWorkshop()).and("T_LineMachineT_ExceptionRecord_TLine.name").is(paramsBean.getLine());//只查询名字中带有文的人
        AggregationOperation match1 = Aggregation.match(c1);
        GroupOperation groupOperation1 = Aggregation.group(
                Fields.fields( "$T_SilkT_ExceptionRecord.spindle",
                        "$T_LineMachineT_ExceptionRecord.item",
                        "$T_SilkException_T_ExceptionRecord.name"
                        , "$T_LineMachineT_ExceptionRecord_TLine._id"
                )


        ).count().as("exCepSum")
                .first("$T_LineMachineT_ExceptionRecord_TLine.name").as("lineName")
                .push(
                        new BasicDBObject("doffingNum", "$doffingNum"))
                .as("doffs")
                .push(
                        new BasicDBObject("time", "$cdt"))
                .as("times")

                ;

//                .first("$T_SilkT_ExceptionRecord.spindle").as("spindle")
//                .first("$T_LineMachineT_ExceptionRecord.item").as("item")
//                .first("$T_SilkException_T_ExceptionRecord.name").as("exCepName")
//                .first("$T_LineMachineT_ExceptionRecord_TLine.name").as("lineName")
//                .first("$T_LineMachineT_ExceptionRecord_TLine._id").as("lineId")

        ;
//                .first("$T_LineMachineT_ExceptionRecord_TLine._id").as("lineId");
        SortOperation $excepSum = Aggregation.sort(Sort.Direction.DESC, "exCepSum");


        Aggregation aggregation = Aggregation.newAggregation(match2, lookupOperation, sortCdt, unwindOperation, lookupOperation2, unwindOperation2, lookupOperation3, unwindOperation3, lookupOperation4
                , unwindOperation4, lookupOperation5, unwindOperation5, lookupOperation6,
                unwindOperation6, lookupOperation7, unwindOperation7, match1, groupOperation1
                ,$excepSum
        );
        List<BasicDBObject> results = template.aggregate(aggregation, "T_ExceptionRecord", BasicDBObject.class).getMappedResults();
        //查询出的结果集为BasicDBObject类型
        System.out.println(results);
        return  results ;
    }


    public  Date getBeforeTwoDay(){
        Date date = new Date() ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -2);
        Date time = calendar.getTime();
        System.out.println("天： " + time.getDay());
        return  time  ;


    }
    @Test
    void T2 (){
        ParamsBean paramsBean =  new ParamsBean() ;
        paramsBean.setWorkshop("C");
        paramsBean.setLine("C1");
        queryBigScreen(paramsBean) ;
    }



}
