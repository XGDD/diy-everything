package com.calm.diyeverything.heart.index.impl;

import com.calm.diyeverything.config.DiyEverythingConfig;
import com.calm.diyeverything.heart.dao.DataSourceFactory;
import com.calm.diyeverything.heart.dao.iml.FileIndexDaoImpl;
import com.calm.diyeverything.heart.index.FileScan;
import com.calm.diyeverything.heart.interceptor.FileInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FileIndexInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FilePrintInterceptor;
import com.calm.diyeverything.heart.property.AfterIndex;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileScanImpl implements FileScan {
    private DiyEverythingConfig config = DiyEverythingConfig.getConfig();
    //拦截器的实例化对象
    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();
    @Override
    public void index(String path) {
        File file = new File(path);
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getParent())){
               return;
            }
        }else{
            if(config.getExcludePath().contains(path)){
                return;
            }else{
                File[] files = file.listFiles();
                if(files != null){
                    for(File f : files){
                        index(f.getAbsolutePath());
                    }
                }
            }
        }
        for(FileInterceptor interceptor : this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}
