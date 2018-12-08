package db;

import java.sql.*;

public class DB {
    
    static public Connection connection;
    
    static private String serverName = "localhost";
    static private String instanceName = ".";
    static private String databaseName = "work_db";
    static private String userName = "sa";
    static private String password = "sa";
    static private String port = "1433";
    
    static private String connectoinString = "jdbc:sqlserver://" + serverName + 
            "\\" + instanceName + 
            ":" + port +
            ";databaseName=" + databaseName +
            ";username=" + userName +
            ";password=" + password;
   
    
    static {
        try {
            connection = DriverManager.getConnection(connectoinString);
        } catch (SQLException ex) {
            System.err.println("Nije moguce uspostaviti konekciju sa bazom!");
            System.err.println(ex.getMessage());
        }
    }
    
}