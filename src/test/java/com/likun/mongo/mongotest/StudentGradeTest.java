package com.likun.mongo.mongotest;

import com.alibaba.fastjson.JSON;
import com.likun.mongo.mongotest.dao.StudentDao;
import com.likun.mongo.mongotest.domain.Grade;
import com.likun.mongo.mongotest.domain.Item;
import com.likun.mongo.mongotest.domain.Student;
import com.likun.mongo.mongotest.domain.Teacher;
import com.likun.mongo.mongotest.service.PackboxService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.operation.FindAndUpdateOperation;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveUpdateOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@SpringBootTest
class StudentGradeTest {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private PackboxService packboxService;
    @Test
    void testQuery(){
        packboxService.findAllBoxAndPull();
    }
    @Test
    void testStu(){
        packboxService.test();
    }
    @Test
    void aaaa(){
        packboxService.ttt();
    }
    @Test
    void addStu() {
        for (int i = 0; i < 5; i++) {
            Student student = new Student() ;
            student.setStuName("被泡女生"+i);
            student.setId(UUID.randomUUID().toString());
            student.setSex("女人");
            student.setGradeId("cfc3f201-a087-421b-b4f8-210c9d9c03b6");
            studentDao.saveStudent(student);
        }
        Item item = new Item() ;
        item.setItem_name("蹦迪");
        item.setScore(22.3);
        item.setUnit("cm");
        List<Item> items = new ArrayList<>() ;
        items.add(item) ;
        Update update = new Update().setOnInsert("items",items ) ;
        System.out.println();

    }

    /**
     * 模糊查询
     * 模糊查询以 【^】开始 以【$】结束 【.*】相当于Mysql中的%
     *
     * @param username 用户名
     * @return ApiResponse
     */
    @Test
     void findStudentsLikeName() {
        String regex = String.format("%s%s%s", "^.*", "1", ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("stuName").regex(pattern));
        List<Student> students = template.find(query, Student.class);
        System.out.println(students);

    }

    @Test
    void  addOneItem(){

        Item item = new Item() ;
        item.setItem_name("蹦迪");
        item.setScore(22.3);
        item.setUnit("cm");
        List<Item> items = new ArrayList<>() ;
        items.add(item) ;
        Update update = new Update().set("items", JSON.toJSON(items));
        UpdateResult updateResult = template.updateMulti(Query.query(Criteria.where("stuName").type(JsonSchemaObject.Type.stringType())), update, List.class, "student");
        System.out.println(updateResult);
/*
        List<Student> stuName1 = template.find(Query.query(Criteria.where("stuName").type(JsonSchemaObject.Type.stringType())), Student.class);
        System.out.println(stuName1);*/
    }

    @Test
    void addGrade() {
        Grade grade = new Grade() ;

        grade.setId( UUID.randomUUID().toString());

        grade.setGradeName("泡妞班");
        template.save(grade) ;

    }
    @Test
    void  addTeacher(){
        Teacher teacher = new Teacher() ;
//        teacher.setId(UUID.randomUUID().toString());
        teacher.setName("音乐老师");
        List<String> list = new ArrayList<>();
        list.add("d1c22441-4564-4a8e-b802-318156fa3248") ;
        list.add("cfc3f201-a087-421b-b4f8-210c9d9c03b6") ;
//        teacher.setGradeId(list);
        template.save(teacher) ;
    }
    @Test
    void  aggregateGradePersons(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("gradeId").count().as("总人数"));
        AggregationResults<BasicDBObject> student = template.aggregate(aggregation, "student", BasicDBObject.class);
        student.iterator().forEachRemaining(stu -> System.out.println(stu) );
    }
    /*
    *
    * "统计某个年级某一项测试在某个范围的人数" 这个也不难，只需要匹配 年级+测试项目+项目分数
    * between 分数1 and 分数2 然后根据年级分组统计
    * */
    @Test
    void  testGroupProjectItems(){
        //unwind 分解
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("items"),
                Aggregation.match(Criteria.where("items.item_name").is("蹦迪").and("items.score").gte(1).lte(100)),
                Aggregation.group("gradeId").count().as("班级蹦极合格人数")
        );
        AggregationResults<BasicDBObject> res = template.aggregate(aggregation, "student", BasicDBObject.class);
        res.iterator().forEachRemaining(System.out::print);


    }


}
