package com.likun.mongo.mongotest.service;

import com.likun.mongo.mongotest.domain.ParamsBean;
import com.likun.mongo.mongotest.utils.response.CommonCode;
import com.likun.mongo.mongotest.utils.response.QueryResponseResult;
import com.likun.mongo.mongotest.utils.response.QueryResult;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExceptionService {
    @Autowired
    private MongoTemplate template;

    public QueryResponseResult getList(ParamsBean param) {
        if (ObjectUtils.isEmpty(param)
                || ObjectUtils.isEmpty(param.getGteDate())
                || ObjectUtils.isEmpty(param.getLetDate())
                || ObjectUtils.isEmpty(param.getWorkshop())
        ) {
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, null);
            return  queryResponseResult ;
        }else {
            List<BasicDBObject> lists = query(param);
            QueryResult queryResult = new QueryResult();
            queryResult.setList(lists);//数据列表
            queryResult.setTotal(1);//数据总记录数
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,   queryResult) ;
            return queryResponseResult ;
        }
    }

    public QueryResponseResult getBorad(String workshop ,String line ) {
        if (ObjectUtils.isEmpty(workshop)
                || ObjectUtils.isEmpty(line)

        ) {
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, null);
            return  queryResponseResult ;
        }else {
            List<BasicDBObject> lists = queryBigScreen(workshop,line);
            QueryResult queryResult = new QueryResult();
            queryResult.setList(lists);//数据列表
            queryResult.setTotal(1);//数据总记录数
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,   queryResult) ;
            return queryResponseResult ;
        }
    }

    public List<BasicDBObject>    query ( ParamsBean paramsBean ){
        Criteria c2 = Criteria.where("cdt").gte(strToDateLong(paramsBean.getGteDate())).lte(strToDateLong(paramsBean.getLetDate()));
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

        Criteria c1 = Criteria.where("T_LineMachineT_ExceptionRecord_TLine_Tworkshop.code").is(paramsBean.getWorkshop());//只查询名字中带有文的人
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
        SortOperation productName = Aggregation.sort(Sort.Direction.ASC, "_id.productName");
        Aggregation aggregation = Aggregation.newAggregation(match2, lookupOperation, unwindOperation, lookupOperation2, unwindOperation2, lookupOperation3, unwindOperation3, lookupOperation4
                , unwindOperation4, lookupOperation5, unwindOperation5, lookupOperation6,
                unwindOperation6, lookupOperation7, unwindOperation7, match1, groupOperation1
                , groupOperation2 ,productName);
        List<BasicDBObject> results = template.aggregate(aggregation, "T_ExceptionRecord", BasicDBObject.class).getMappedResults();
        //查询出的结果集为BasicDBObject类型
        System.out.println(results);
        return  results ;
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


    public List<BasicDBObject>    queryBigScreen ( String workshop ,String line ){
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

        Criteria c1 = Criteria.where("T_LineMachineT_ExceptionRecord_TLine_Tworkshop.code").is(workshop).and("T_LineMachineT_ExceptionRecord_TLine.name").is(line);//只查询名字中带有文的人
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



}
