package com.likun.mongo.mongotest.dao;

import com.likun.mongo.mongotest.domain.ExceptionBean;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionRepository extends MongoRepository<ExceptionBean,String> {
}
