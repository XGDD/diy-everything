package com.calm.diyeverything.heart.property;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件类型
 */
public enum FileType {
    IMG("bmp","jpeg","gif","psd","pcx","png","dxf","cdr"),  //图片类型
    DOC("pdf","docx","ppt"),  //文档类型
    BIN("exe","jar","sh","msi"),  //二进制类型
    ARCHIVE("zip","rar"), //存档类型
    OTHER("*");  //其他类型

    /**
     * 文件类型的扩展名集合
     */
    private Set<String> strings = new HashSet<>();
    FileType(String... strings){
        this.strings.addAll(Arrays.asList(strings));
    }


    /**
     * 根据文件扩展名获取文件类型对象
     */
    public static FileType searchByName(String name){
        for(FileType fileType : FileType.values()){
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
}
