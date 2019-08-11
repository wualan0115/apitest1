package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDataUtil {
    //通过dburl,用户名密码获取当前数据库的连接对象
    public static Connection getConn(String url, String username, String password){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,username,password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据库连接
    public static void closeConn(Connection conn){
        try {
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据sql返回二维数组
    public static Object[][] getData(Connection conn,String query){
        Object[][] data = null;

        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int column = meta.getColumnCount();
            List<Object[]> list = new ArrayList<>();
            while (rs.next()){
                Object[] oa = new Object[column];
                for (int i = 1; i <= column; i++) {
                    oa[i-1] = rs.getObject(i);
                }
                list.add(oa);
            }
            if(list.size() > 0){
                data = new Object[list.size()][column];
                for (int i = 0; i < list.size(); i++) {
                    data[i] = list.get(i);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }

    //返回sql查询结果的二维数组中的指定行列
    public static String getData(Connection conn,String query,int row,int column){
        Object[][] result = getData(conn,query);
        if(result!= null && result.length>row && result[0].length>column){
            return result[row][column].toString();
        }
        return "No matched Value";
    }

    //executeUpdate方法可以执行新增、更新、删除三种sql语句
    public static int executeUpdate(Connection conn,String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            int updateCount = stmt.getUpdateCount();
            return updateCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (stmt !=null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
