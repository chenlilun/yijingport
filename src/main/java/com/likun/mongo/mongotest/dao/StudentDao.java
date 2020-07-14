package com.likun.mongo.mongotest.dao;

import com.likun.mongo.mongotest.domain.Student;

import java.util.List;

public interface StudentDao {
    //新增
    void saveStudent(Student student);

    //删除
    void removeStudent(String id);

    //修改
    void updateStudent(Student student);

    //根据编号查询
    Student findById(String id);

    //查询所有
    List<Student> findAll();

    /**
     * 两表联查
     * @return
     */
    Object findStudentAndGrade();
}
