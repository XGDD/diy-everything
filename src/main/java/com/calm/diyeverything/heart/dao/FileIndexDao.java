package com.calm.diyeverything.heart.dao;

import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;

import java.util.List;
import java.util.concurrent.locks.Condition;

//用户访问数据库的操作
public interface FileIndexDao {
    //插入数据AfterIndex
    void insert(AfterIndex afterIndex);

    //检索数据AfterIndex

    List<AfterIndex> seek(Ask ask);

    //删除afterIndex
    void delete(AfterIndex afterIndex);
}
