import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao {
    public void registerUser (String username, String password, String firstname, String lastname, String email) {
        String sql = "INSERT INTO users (username, password, firstname, lastname, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // Consider hashing the password
            preparedStatement.setString(3, firstname);
            preparedStatement.setString(4, lastname);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();
            System.out.println("User  registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}