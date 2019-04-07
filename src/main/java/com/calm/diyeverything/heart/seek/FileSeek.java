package com.calm.diyeverything.heart.seek;

import com.calm.diyeverything.heart.dao.DataSourceFactory;
import com.calm.diyeverything.heart.dao.iml.FileIndexDaoImpl;
import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;
import com.calm.diyeverything.heart.seek.impl.FileSeekImpl;

import java.util.List;

/**
 *根据条件检索文件
 */
public interface FileSeek {
    List<AfterIndex> seek(Ask ask);

}
