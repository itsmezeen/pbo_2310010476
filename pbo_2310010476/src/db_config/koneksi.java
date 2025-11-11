package db_config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import javax.swing.JOptionPane;

public class koneksi {
    
    private static final String NAMA_DB = "pbo_2310010476";
    private static final String URL = "jdbc:mysql://localhost/" + NAMA_DB;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getKoneksi() throws SQLException {
        try {
            Driver mysqldriver;
            mysqldriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi ke Database GAGAL: " + e.getMessage());
            throw e; 
        }
    }
}