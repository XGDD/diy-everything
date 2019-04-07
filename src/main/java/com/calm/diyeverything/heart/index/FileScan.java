package com.calm.diyeverything.heart.index;

import com.calm.diyeverything.heart.dao.DataSourceFactory;
import com.calm.diyeverything.heart.dao.iml.FileIndexDaoImpl;
import com.calm.diyeverything.heart.index.impl.FileScanImpl;
import com.calm.diyeverything.heart.interceptor.FileInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FileIndexInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FilePrintInterceptor;
import com.calm.diyeverything.heart.property.AfterIndex;

public interface FileScan {
    /**
     * 遍历path，将指定path路径下的所有目录和文件以及子目录和文件递归扫描索引到数据库
     */
    void index(String path);
    /**
     * 遍历的拦截器
     */
    void interceptor(FileInterceptor interceptor);
}
