package com.calm.diyeverything.heart.seek;

import com.calm.diyeverything.config.DiyEverythingConfig;
import com.calm.diyeverything.heart.common.HandPath;
import com.calm.diyeverything.heart.dao.DataSourceFactory;
import com.calm.diyeverything.heart.dao.FileIndexDao;
import com.calm.diyeverything.heart.dao.iml.FileIndexDaoImpl;
import com.calm.diyeverything.heart.index.FileScan;
import com.calm.diyeverything.heart.index.impl.FileScanImpl;
import com.calm.diyeverything.heart.interceptor.impl.AfterIndexClearInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FileIndexInterceptor;
import com.calm.diyeverything.heart.interceptor.impl.FilePrintInterceptor;
import com.calm.diyeverything.heart.monitor.FileWatch;
import com.calm.diyeverything.heart.monitor.FileWatchImpl;
import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;
import com.calm.diyeverything.heart.seek.impl.FileSeekImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 核心调度器
 */
public final class DiyEverythingManager {
    private static volatile DiyEverythingManager manager;
    private FileSeek fileSeek;//检索
    private FileScan fileScan;//索引
    private ExecutorService executorService;//线程池


    //清理删除的文件
    private AfterIndexClearInterceptor afterIndexClearInterceptor;
    private Thread clearThread;
    private AtomicBoolean clearThreadStatus = new AtomicBoolean(false);
    //文件监听
    private FileWatch fileWatch;

    public DiyEverythingManager(){
        this.initElement();
    }
    private void initElement(){
        //取得数据源对象
        DataSource dataSource = DataSourceFactory.getDataSource();
//        //检查数据库
//        checkDatabase();
        //重置数据库
        resetDatabase();
        //取得业务层对象
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);
        this.fileScan = new FileScanImpl();
        //this.fileScan.interceptor(new FilePrintInterceptor());
        this.fileSeek = new FileSeekImpl(fileIndexDao);
        //拦截器
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        //清理掉删除的文件
        this.afterIndexClearInterceptor = new AfterIndexClearInterceptor(fileIndexDao);
        this.clearThread = new Thread(this.afterIndexClearInterceptor);
        //将用户线程变为守护线程
        this.clearThread.setDaemon(true);
        this.clearThread.setName("AfterIndex-Thread-Clear");
        //监听对象
        fileWatch = new FileWatchImpl(fileIndexDao);
    }

    private void checkDatabase() {
        String workDir = System.getProperty("user.dir");
        String fileName = DiyEverythingConfig.getConfig().getH2IndexPath() + ".mv.db";
        File dbFile = new File(fileName);
        if(dbFile.isFile() && !dbFile.exists()){
            DataSourceFactory.initDatabase();
        }
    }

    //重置数据库
    private void resetDatabase(){
        DataSourceFactory.initDatabase();
    }

    public static DiyEverythingManager getManager(){
        if(manager == null){
            synchronized (DiyEverythingManager.class){
                if(manager == null){
                    manager = new DiyEverythingManager();
                }
            }
        }
        return manager;
    }


    //检索
    public List<AfterIndex> search(Ask ask) {
        return this.fileSeek.seek(ask).stream().filter(afterIndex -> {
            String path = afterIndex.getPath();
            File file = new File(path);
            boolean flag = file.exists();
            if(!flag){
                //删除
                afterIndexClearInterceptor.apply(afterIndex);
            }
            return flag;
        }).collect(Collectors.toList());
    }

    //索引
    public void buildIndex() {
        resetDatabase();
        Set<String> directories = DiyEverythingConfig.getConfig().getIncludePath();
        if (this.executorService == null) {
            this.executorService = Executors.newFixedThreadPool(directories.size(), new ThreadFactory() {
                private final AtomicInteger threadId = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("Thread-Scan-" + threadId.getAndIncrement());
                    return thread;
                }
            });
        }
        final CountDownLatch countDownLatch = new CountDownLatch(directories.size());
        for (String path : directories) {
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    DiyEverythingManager.this.fileScan.index(path);
                    //当前任务完成，值-1
                    countDownLatch.countDown();
                }
            });
        }
        //阻塞，直到任务完成
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //启动清理
    public void startClear(){
        if(this.clearThreadStatus.compareAndSet(false, true)){
            this.clearThread.start();
        }else{
            System.out.println("不能重复启动");
        }
    }

    //启动监听
    public void startFileSystemMonitor() {
        DiyEverythingConfig config = DiyEverythingConfig.getConfig();
        HandPath handPath = new HandPath();
        handPath.setIncludePath(config.getIncludePath());
        handPath.setExcludePath(config.getExcludePath());
        this.fileWatch.monitor(handPath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileWatch.start();
            }
        }).start();
    }
}
