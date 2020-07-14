package com.likun.mongo.mongotest.dao;

import com.alibaba.fastjson.JSON;
import com.likun.mongo.mongotest.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class StudentDaoImpl implements StudentDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    //新增
    @Override
    public void saveStudent(Student student) {
        mongoTemplate.save(student);
    }

    //删除
    @Override
    public void removeStudent(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Student.class);
    }

    //修改
    @Override
    public void updateStudent(Student student) {
        Query query=new Query(Criteria.where("_id").is(student.getId()));
        Update update=new Update();
        update.set("name",student.getStuName());
        update.set("sex",student.getSex());
        update.set("gradeId",student.getGradeId());
        mongoTemplate.updateFirst(query,update,Student.class);
    }

    //根据编号查询
    @Override
    public Student findById(String id) {
        Query query=new Query(Criteria.where("_id").is(id));//可累加条件
        Student student = mongoTemplate.findOne(query, Student.class);
        return student;
    }

    //查询所有
    @Override
    public List<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    /**
     * 两表联查
     *
     * @return
     */
    @Override
    public Object findStudentAndGrade() {
        LookupOperation lookupOperation= LookupOperation.newLookup().
                from("grade").  //关联从表名
                localField("gradeId").     //主表关联字段
                foreignField("_id").//从表关联的字段
                as("GradeAndStu");   //查询结果名
//带条件查询可以选择添加下面的条件
//       Criteria criteria=Criteria.where("studenAndgrade").not().size(0);   //只查询有结果的学生
//        Criteria qqq=Criteria.where("name").regex("文");//只查询名字中带有文的
        //       AggregationOperation match1= Aggregation.match(qqq);
//        AggregationOperation match = Aggregation.match(criteria);
//        Aggregation counts = Aggregation.newAggregation(match1,lookupOperation,match).;
        Aggregation aggregation=Aggregation.newAggregation(lookupOperation);
        List<Map> results = mongoTemplate.aggregate(aggregation,"student", Map.class).getMappedResults();
        //上面的student必须是查询的主表名
        System.out.println(JSON.toJSONString(results));
        return results;
    }

}
