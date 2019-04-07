package com.calm.diyeverything.heart.dao.iml;

import com.calm.diyeverything.heart.dao.FileIndexDao;
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

public class FileIndexDaoImpl implements FileIndexDao {
    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
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
            releaseResource(null, preparedStatement, connection);
        }
    }

    //查询
    public List<AfterIndex> seek(Ask ask) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<AfterIndex> list = new ArrayList<>();
        try {
            //获取数据库连接
            connection = dataSource.getConnection();
            StringBuilder sb = new StringBuilder();
            sb.append(" select name, path, depth, file_type from file_index ");
            //name匹配：前后都模糊的匹配
            sb.append(" where ").append(" name like '").append(ask.getName()).append("%' ");
            if(ask.getFileType() != null){
                sb.append(" and file_type = '").append(FileType.searchByName(ask.getFileType().toUpperCase())).append("' ");
            }
            //limit,order
            sb.append(" order by depth ").append(ask.getOrderByAsc() ? "asc" : "desc");
            sb.append(" limit ")
                    .append(ask.getLimit());
            //System.out.println(sb.toString());
            //准备命令
            preparedStatement = connection.prepareStatement(sb.toString());
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
            e.printStackTrace();
        }finally {
            releaseResource(resultSet, preparedStatement, connection);
        }
        return list;
    }

    @Override
    public void delete(AfterIndex afterIndex) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            //连接数据库
            connection = dataSource.getConnection();
            //SQL语句
            String sql = "delete from file_index where path like '"+afterIndex.getPath()+"%'";
            //准备命令
            preparedStatement = connection.prepareStatement(sql);
            //执行命令
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null, preparedStatement,connection);
        }
    }
    private void releaseResource(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection){
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
                e.printStackTrace();
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
