package com.calm.diyeverything.heart.interceptor;

import com.calm.diyeverything.heart.property.AfterIndex;

@FunctionalInterface
public interface AfterIndexInterceptor {
    void apply(AfterIndex afterIndex);
}
