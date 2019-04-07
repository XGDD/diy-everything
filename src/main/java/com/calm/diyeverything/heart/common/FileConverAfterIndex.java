package com.calm.diyeverything.heart.common;

import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.FileType;

import java.io.File;

/**
 * 辅助工具类，将File对象转换为AfterIndex对象
 */
public final class FileConverAfterIndex {
    private FileConverAfterIndex(){}
    public static AfterIndex convert(File file){
        AfterIndex afterIndex = new AfterIndex();
        afterIndex.setName(file.getName());
        afterIndex.setPath(file.getAbsolutePath());
        afterIndex.setDepth(computeFileDepth(file));
        afterIndex.setFileType(computeFileType(file));
        return afterIndex;
    }
    //计算深度
    private static int computeFileDepth(File file){
        String[] routes = file.getAbsolutePath().split("\\\\");
        return routes.length;
    }
    //计算类型
    private static FileType computeFileType(File file){
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        int index = file.getName().lastIndexOf(".");
        if(index != -1 && index < (file.getName().length()-1)){//防止abc.这种情况
            String extend = file.getName().substring(index+1);
            return FileType.searchByName(extend);
        }else{
            return FileType.OTHER;
        }
    }
}
