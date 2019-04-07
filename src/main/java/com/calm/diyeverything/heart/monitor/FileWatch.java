package com.calm.diyeverything.heart.monitor;

import com.calm.diyeverything.heart.common.HandPath;

//文件监控
public interface FileWatch {
    void start();
    void monitor(HandPath handPath);
    void stop();
}
