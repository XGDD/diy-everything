package com.calm.diyeverything.heart.monitor;

import com.calm.diyeverything.heart.common.FileConverAfterIndex;
import com.calm.diyeverything.heart.common.HandPath;
import com.calm.diyeverything.heart.dao.FileIndexDao;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class FileWatchImpl implements FileWatch, FileAlterationListener {

    private FileIndexDao fileIndexDao;
    private FileAlterationMonitor monitor;
    public FileWatchImpl(FileIndexDao fileIndexDao){
        this.fileIndexDao = fileIndexDao;
        //10毫秒检查一次
        this.monitor = new FileAlterationMonitor(10);
    }
    @Override
    //开始监听
    public void onStart(FileAlterationObserver fileAlterationObserver){
        //fileAlterationObserver.addListener(this);
    }
    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate"+directory);
    }
    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("onDirectoryChange"+directory);
    }
    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete"+directory);
    }
    @Override
    //创建文件
    public void onFileCreate(File file){
        System.out.println("onFileCreate" + file);
        this.fileIndexDao.insert(FileConverAfterIndex.convert(file));
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("onFileChange"+file);
    }
    @Override
    //文件删除
    public void onFileDelete(File file){
        System.out.println("onFileDelete" + file);
        this.fileIndexDao.delete(FileConverAfterIndex.convert(file));
    }
    @Override
    //监听停止
    public void onStop(FileAlterationObserver fileAlterationObserver){
        //fileAlterationObserver.removeListener(this);
    }

    @Override
    public void start() {
        try{
            this.monitor.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandPath handPath) {
        for(String path : handPath.getIncludePath()){
            FileAlterationObserver observer = new FileAlterationObserver(path, pathname->{
                //true监控   false过滤不监控
                String currentPath = pathname.getAbsolutePath();
                for(String p : handPath.getExcludePath()){
                    if(p.startsWith(currentPath)){
                        return false;
                    }
                }
                return true;
            });
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void stop() {
        try{
            this.monitor.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
