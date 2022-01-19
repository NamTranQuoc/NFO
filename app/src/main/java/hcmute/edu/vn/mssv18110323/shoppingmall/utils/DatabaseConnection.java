package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    final static String url = "jdbc:mysql://192.168.1.153:3306/heroku_b79b69329c5e45d?useUnicode=true&characterEncoding=utf-8";
    final static String username = "andro";
    final static String password = "androNam@123";

//   final static String url = "jdbc:mysql://192.168.1.153:3306/heroku_b79b69329c5e45d?useUnicode=true&characterEncoding=utf-8";
//   final static String username = "andro";
//   final static String password = "androNam@123";

//    final static String url = "jdbc:mysql://us-cdbr-east-03.cleardb.com:3306/heroku_b79b69329c5e45d?useUnicode=true&characterEncoding=utf-8";
//    final static String username = "b72bbd788843e6";
//    final static String password = "b0acf7e8";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
