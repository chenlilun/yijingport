package com.likun.mongo.mongotest.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Optional;

public class SpringbootMongoDBPageable implements Serializable, Pageable {

    private static final long serialVersionUID = 1L;

    MongoDBPageModel page;

    public MongoDBPageModel getPage() {
        return page;
    }

    public void setPage(MongoDBPageModel page) {
        this.page = page;
    }

    @Override
    public boolean isPaged() {
        return false;
    }

    @Override
    public boolean isUnpaged() {
        return false;
    }

    @Override
    public int getPageNumber() {
        return page.getPagenumber();
    }

    @Override
    public int getPageSize() {
        return page.getPagesize();
    }

    @Override
    public long getOffset() {
        return (page.getPagenumber() - 1) * page.getPagesize();
    }

    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return null;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Optional.empty();
    }
}
