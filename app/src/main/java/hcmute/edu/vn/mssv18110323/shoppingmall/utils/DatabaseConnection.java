package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    final static String url = "jdbc:mysql://204.2.63.74:15485/NFO_DB?useUnicode=true&characterEncoding=utf-8";
    final static String username = "admin";
    final static String password = "WkJhE06n";

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
