package com.likun.mongo.mongotest.controllor;

import com.likun.mongo.mongotest.dao.StudentDao;
import com.likun.mongo.mongotest.domain.ParamsBean;
import com.likun.mongo.mongotest.domain.Student;
import com.likun.mongo.mongotest.service.ExceptionService;
import com.likun.mongo.mongotest.utils.response.QueryResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exception")
public class StudentController {
    @Autowired
    private ExceptionService  exceptionService ;

    @PostMapping("/getException")
    public QueryResponseResult getException(@RequestBody ParamsBean paramsBean) {
        return exceptionService.getList(paramsBean);
    }

    @GetMapping("/getBorad/{workshop}/{line}")
    public QueryResponseResult getBorad(@PathVariable("workshop") String workshop ,@PathVariable("line") String line) {
        return exceptionService.getBorad(workshop ,line);
    }
    @GetMapping("/sb")
    public  String getString () {
        return  "aaa" ;
    }

/*    @Autowired
    private StudentDao studentDao;*/

   /* //查询所有
    @GetMapping("/students")
    public Object findAll(){
        return studentDao.findAll();
    }

    //根据编号查询
    @GetMapping("/findById")
    public Object findById(String id){
        return studentDao.findById(id);
    }

    //根据编号删除
    @DeleteMapping("/students/{id}")
    public  Object removeStudent(@PathVariable("id") String id){
        try {
            studentDao.removeStudent(id);
            return 200;
        }catch (Exception e){
            e.printStackTrace();
            return  500;
        }
    }

    //修改
    @PutMapping("/students")
    public Object updateStudent(Student student){
        studentDao.updateStudent(student);
        return student;
    }


    //新增
    @PostMapping("/students")
    public  Object saveStudent(Student student){
        studentDao.saveStudent(student);
        return  student;
    }

    //多表联查
    @GetMapping("/findStudentAndGrade")
    public Object findStudentAndGrade(){
        return studentDao.findStudentAndGrade();
    }*/

}
