package com.calm.diyeverything.heart.seek.impl;

import com.calm.diyeverything.heart.dao.FileIndexDao;
import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;
import com.calm.diyeverything.heart.seek.FileSeek;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class FileSeekImpl implements FileSeek {
    private final FileIndexDao fileIndexDao;

    public FileSeekImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public List<AfterIndex> seek(Ask ask) {
        if(ask == null){
            return new ArrayList<>();
        }
        return this.fileIndexDao.seek(ask);
    }
}
