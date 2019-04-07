package com.calm.diyeverything.heart.common;


import lombok.Data;

import java.util.Set;

//所有路径
@Data
public class HandPath {
    //包含的目录
    private Set<String> includePath;
    //排除的目录
    private Set<String> excludePath;
}
