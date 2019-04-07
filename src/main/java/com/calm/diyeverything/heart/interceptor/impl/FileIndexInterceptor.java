package com.calm.diyeverything.heart.interceptor.impl;

import com.calm.diyeverything.heart.common.FileConverAfterIndex;
import com.calm.diyeverything.heart.dao.FileIndexDao;
import com.calm.diyeverything.heart.interceptor.FileInterceptor;
import com.calm.diyeverything.heart.property.AfterIndex;

import java.io.File;

/**
 * 将文件转化为AfterIndex，然后写入数据库
 */
public class FileIndexInterceptor implements FileInterceptor {
    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        AfterIndex afterIndex = FileConverAfterIndex.convert(file);
        fileIndexDao.insert(afterIndex);
    }
}
