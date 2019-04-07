package com.calm.diyeverything.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class DiyEverythingConfig {
    private static volatile DiyEverythingConfig config;
    //索引文件的路径
    private Set<String> includePath = new HashSet();
    //排除索引文件的路径
    private Set<String> excludePath = new HashSet<>();
    //检索返回最大的数量
    @Setter
    private Integer maxReturn = 20;
    @Setter
    //默认升序
    private Boolean depthOrderAsc = true;

    //H2数据库文件路径
    private String h2IndexPath = System.getProperty("user.dir")+File.separator+"diy_everything";

    public static DiyEverythingConfig getConfig() {
        if (config == null) {
            synchronized (DiyEverythingConfig.class) {
                if (config == null) {
                    config = new DiyEverythingConfig();
                    config.initDefaultPathsConfig();
                }
            }
        }
        return config;
    }
    private static void initDefaultPathsConfig(){
        //获取文件系统
        FileSystem fileSystem = FileSystems.getDefault();
        //遍历的目录
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        iterable.forEach(path -> config.includePath.add(path.toString()));
        //排除的目录     windows下排除：C:\Windows、C:\Program Files(x86)、C:\Program Files、C:\ProgramData
        //Linux下排除：/tmp /etc
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Windows")){
            config.getExcludePath().add("C:\\Windows");
            config.getExcludePath().add("C:\\Program Files(x86)");
            config.getExcludePath().add("C:\\Program Files");
            config.getExcludePath().add("C:\\ProgramData");
        }else {
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("/etc");
            config.getExcludePath().add("/root");
        }
    }
}
