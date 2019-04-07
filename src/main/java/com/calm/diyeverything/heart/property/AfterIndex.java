package com.calm.diyeverything.heart.property;

import lombok.Data;

/**
 * 文件属性信息索引之后的记录用AfterIndex表示
 */
@Data  //加了这个插件后，getter setter toString不用覆写，直接生成完成
public class AfterIndex {
    private String name;//文件名，不含路径
    private Integer depth;//文件深度
    private String path;//文件路径
    private FileType fileType;//文件类型
}
