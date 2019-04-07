package com.calm.diyeverything.heart.index.impl;

import com.calm.diyeverything.heart.dao.FileIndexDao;
import com.calm.diyeverything.heart.index.FileIndex;
import com.calm.diyeverything.heart.property.AfterIndex;

public class FileIndexImpl implements FileIndex {
    private final FileIndexDao fileIndexDao;

    public FileIndexImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void insert(AfterIndex afterIndex) {
        this.fileIndexDao.insert(afterIndex);
    }
}
