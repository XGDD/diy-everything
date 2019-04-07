package com.calm.diyeverything.heart.dao;

import com.calm.diyeverything.heart.property.AfterIndex;
import com.calm.diyeverything.heart.property.Ask;
import com.calm.diyeverything.heart.property.FileType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

class FileIndexDaoImplTest implements FileIndexDao{
    private final DataSource dataSource;

    public FileIndexDaoImplTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(AfterIndex afterIndex) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //获取数据库连接
            connection = dataSource.getConnection();
            //准备SQL语句
            String string = "insert into file_index(name,path,depth,file_type) values (?,?,?,?)";
            //准备命令
            preparedStatement = connection.prepareStatement(string);
            //设置参数
            preparedStatement.setString(1,afterIndex.getName());
            preparedStatement.setString(2,afterIndex.getPath());
            preparedStatement.setInt(3,afterIndex.getDepth());
            preparedStatement.setString(4,afterIndex.getFileType().name());
            //执行命令
            preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {

                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<AfterIndex> seek(Ask ask) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<AfterIndex> list = new ArrayList<>();
        try {
            //获取数据库连接
            connection = dataSource.getConnection();
            //准备SQL语句
            String string = "select name,path,depth,file_type from file_index";
            //准备命令
            preparedStatement = connection.prepareStatement(string);
            //执行命令
            resultSet = preparedStatement.executeQuery();
            //处理结果
            while (resultSet.next()){
                //将数据库中每行对象包装成java对象
                AfterIndex afterIndex = new AfterIndex();
                afterIndex.setName(resultSet.getString("name"));
                afterIndex.setPath(resultSet.getString("path"));
                afterIndex.setDepth(resultSet.getInt("depth"));
                String fileType = resultSet.getString("file_type");
                afterIndex.setFileType(FileType.searchByName(fileType));
                list.add(afterIndex);
            }
        }catch (SQLException e){

        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {

                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void delete(AfterIndex afterIndex) {

    }

    public static void main(String[] args) {
        FileIndexDao fileIndexDao = new FileIndexDaoImplTest(DataSourceFactory.getDataSource());
        AfterIndex afterIndex = new AfterIndex();
        afterIndex.setName("简历.ppt");
        afterIndex.setPath("D:\\a\\test\\简历.ppt");
        afterIndex.setDepth(3);
        afterIndex.setFileType(FileType.DOC);
        fileIndexDao.insert(afterIndex);
        List<AfterIndex> list = fileIndexDao.seek(new Ask());
        for(AfterIndex afterIndex1 : list){
            System.out.println(afterIndex);
        }
    }
}