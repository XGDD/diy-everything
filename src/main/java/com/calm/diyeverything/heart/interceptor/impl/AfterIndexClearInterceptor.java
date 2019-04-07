package com.calm.diyeverything.heart.interceptor.impl;

import com.calm.diyeverything.heart.dao.FileIndexDao;
import com.calm.diyeverything.heart.interceptor.AfterIndexInterceptor;
import com.calm.diyeverything.heart.property.AfterIndex;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class AfterIndexClearInterceptor implements AfterIndexInterceptor,Runnable {
    private Queue<AfterIndex> queue = new ArrayBlockingQueue<>(1024);
    private final FileIndexDao fileIndexDao;
    public AfterIndexClearInterceptor(FileIndexDao fileIndexDao){
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public void apply(AfterIndex afterIndex) {
        this.queue.add(afterIndex);
    }

    @Override
    public void run() {
        while(true){
            AfterIndex afterIndex = this.queue.poll();
            if(afterIndex != null){
                fileIndexDao.delete(afterIndex);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
