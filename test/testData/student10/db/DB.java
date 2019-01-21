package db;

import java.sql.*;

public class DB {
    
    public static Connection getConnection()
    {
    	return org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
    }
    
}