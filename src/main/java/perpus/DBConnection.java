package perpus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static String user = "root";
    static String pass = "7788";
    static String url = "jdbc:mysql://localhost/dbperpustakaan";
    static String driver = "com.mysql.cj.jdbc.Driver";

    @SuppressWarnings("exports")
    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException();
        }
        return conn;
    }
}