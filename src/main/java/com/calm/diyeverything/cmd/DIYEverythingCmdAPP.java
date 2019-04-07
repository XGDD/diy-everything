package com.calm.diyeverything.cmd;

import com.calm.diyeverything.config.DiyEverythingConfig;
import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;
import com.calm.diyeverything.heart.seek.DiyEverythingManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class DIYEverythingCmdAPP {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //解析参数
        parseParams(args);

        welcome();
        //统一调度器
        DiyEverythingManager manager = DiyEverythingManager.getManager();
        //启动后清理线程
        manager.startClear();
        //启动文件监控
        manager.startFileSystemMonitor();
        //交互式
        interactive(manager);
    }
    //解析参数
    private static void parseParams(String[] args){
        if(args == null){
            return;
        }
        DiyEverythingConfig config = DiyEverythingConfig.getConfig();
        for(String param : args){
            //如果用户输入参数错误，直接忽略用户输入的值，采用默认值
            String maxReturnParam = "--maxReturn=";
            if(param.startsWith(maxReturnParam)){
                int index = param.lastIndexOf("=");
                String maxReturnStr = param.substring(index+1);
                try{
                    int maxReturn = Integer.parseInt(maxReturnStr);
                    config.setMaxReturn(maxReturn);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
            String deptOrderByAscParam = "--deptOrderByAsc=";
            if(param.startsWith(deptOrderByAscParam)){
                int index = param.lastIndexOf("=");
                String deptOrderByAscStr = param.substring(index+1);
                DiyEverythingConfig.getConfig().setDepthOrderAsc(Boolean.parseBoolean(deptOrderByAscStr));
            }
            String includePathParam = "--includePath=";
            if(param.startsWith(includePathParam)){
                int index = param.lastIndexOf("=");
                String includePathStr = param.substring(index+1);
                String[] paths = includePathStr.split(";");
                if(paths.length > 0){
                    config.getIncludePath().clear();
                }
                for(String path : paths){
                    config.getIncludePath().add(path);
                }
            }
            String excludePathStr = "--excludePath=";
            if(param.startsWith(excludePathStr)){
                int index = param.lastIndexOf("=");
                String excludePath = param.substring(index+1);
                String[] paths = excludePath.split(";");
                config.getExcludePath().clear();
                for(String path : paths){
                    config.getExcludePath().add(path);
                }
            }
        }
    }

    public static void interactive(DiyEverythingManager manager){
        while(true){
            System.out.println("diyeverything >>");
            String input = scanner.nextLine();
            //优先处理search
            if(input.startsWith("search")){
                String[] values = input.split(" ");
                if(values.length >= 2){
                    if(!values[0].equals("search")){
                        help();
                        continue;
                    }
                    Ask ask = new Ask();
                    String name = values[1];
                    ask.setName(name);
                    if(values.length >= 3){
                        String fileType = values[2];
                        ask.setFileType(fileType.toUpperCase());
                    }
                    search(manager, ask);
                    continue;
                }else{
                    help();
                    continue;
                }
            }
            switch (input){
                case "help":
                    help();
                    break;
                case "quit":
                    quit();
                    return;
                case "index":
                    index(manager);
                    break;
//                case "search":
//                    search(manager, ask);
//                    break;
                default:
                    help();
            }
        }
    }
    private static void search(DiyEverythingManager manager, Ask ask){
        ask.setLimit(DiyEverythingConfig.getConfig().getMaxReturn());
        ask.setOrderByAsc(DiyEverythingConfig.getConfig().getDepthOrderAsc());
        List<AfterIndex> thingList = manager.search(ask);
        for(AfterIndex afterIndex : thingList){
            System.out.println(afterIndex.getPath());
        }
    }
    private static void index(DiyEverythingManager manager){
        new Thread(new Runnable() {
            @Override
            public void run() {
                manager.buildIndex();
            }
        }).start();
    }
    private static void quit(){
        System.out.println("退出");
        System.exit(0);
    }
    private static void welcome(){
        System.out.println("欢迎使用Diy_Everything");
    }
    private static void help(){
        System.out.println("命令：");
        System.out.println("退出：quit");
        System.out.println("帮助：help");
        System.out.println("索引：index");
        System.out.println("查询：search <name> [<file-Type> img | doc | bin | archive | other");
    }
}
