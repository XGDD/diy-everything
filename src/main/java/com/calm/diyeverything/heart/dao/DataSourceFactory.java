package com.calm.diyeverything.heart.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.calm.diyeverything.config.DiyEverythingConfig;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//数据源
public class DataSourceFactory {

    private static volatile DruidDataSource dataSource;
    public static DataSource getDataSource(){
        if(dataSource == null){
            synchronized (DataSourceFactory.class){
                if(dataSource == null){
                    //实例化
                    dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("org.h2.Driver");
                    //采用的是H2的嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口

                    //将数据存到本地文件
                    dataSource.setUrl("jdbc:h2:" + DiyEverythingConfig.getConfig().getH2IndexPath());
//                    //数据库创建完成之后，初始化表
//                    initDatabase();
                    dataSource.setValidationQuery("select now()");
                }
            }
        }
        return dataSource;
    }

    public static void initDatabase(){
        //1.获取数据源
        DataSource dataSource = DataSourceFactory.getDataSource();
        //2.获取SQL语句
        try(InputStream inputStream = DataSourceFactory.class.getClassLoader().
                getResourceAsStream("diy_everything.sql");){
            if(inputStream == null){
                throw new RuntimeException("没有读到初始化数据库，请检查读取语句");
            }
            StringBuilder stringBuilder = new StringBuilder();
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));){
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    if(!line.startsWith("--")){
                        stringBuilder.append(line);
                    }
                }
            }
            //3.获取数据库连接和名称执行SQL
            String sql = stringBuilder.toString();
            //获取连接
            Connection connection = dataSource.getConnection();
            //创建命令
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //执行sql语句
            preparedStatement.execute();

            //关闭连接和命令
            connection.close();
            preparedStatement.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}