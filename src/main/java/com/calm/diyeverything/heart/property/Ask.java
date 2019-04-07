package com.calm.diyeverything.heart.property;

import lombok.Data;
import lombok.Getter;

/**
 * 检索条件
 */
@Data
public class Ask {
    private String name;
    private String fileType;
    private Integer limit;
    //如果是true则是升序，如果是false，则是降序
    private Boolean orderByAsc = true;
}
