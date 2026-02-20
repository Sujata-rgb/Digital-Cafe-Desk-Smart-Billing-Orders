package DB;

//public class InsertTest {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
import java.sql.*;

public class InsertTest {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/cafe_db";
        String username = "root";
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, username, password);

            String query = "INSERT INTO items (item_name, price, category) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, "Cold Coffee");
            ps.setDouble(2, 120.0);
            ps.setString(3, "Beverage");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Item Inserted Successfully!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}