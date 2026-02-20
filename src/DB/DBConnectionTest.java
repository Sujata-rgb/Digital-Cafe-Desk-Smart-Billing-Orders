package DB;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionTest {

    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/cafe_db";
        String username = "root";
        String password = "1234";  
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection con = DriverManager.getConnection(url, username, password);
            
            System.out.println("Connected Successfully!");
            
            con.close();
        } 
        catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}

