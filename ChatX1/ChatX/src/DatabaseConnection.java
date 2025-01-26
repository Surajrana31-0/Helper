import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chat_app"; // Update with your database name
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "Sur@j9109rana_"; // replace with your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}