import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public boolean register(String username, String password, double creditLimit) {
        if (username.isBlank() || password.isBlank() || creditLimit <= 0) {
            return false;
        }

        // Each ? is an empty slot that we fill in safely below
        String sql = "INSERT INTO users (username, password, credit_limit) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);     // fills the 1st ?
            ps.setString(2, password);     // fills the 2nd ?
            ps.setDouble(3, creditLimit);  // fills the 3rd ?
            ps.executeUpdate();            // runs the INSERT
            return true;
        } catch (SQLException e) {
            // Most likely the username already exists (PRIMARY KEY must be unique)
            return false;
        }
    }

    // Returns the User if login is right, otherwise null
    public User login(String username, String password) {
        String sql = "SELECT username, password, credit_limit FROM users WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();   // runs the SELECT

            if (rs.next()) {                    // found a matching row
                User u = new User(rs.getString("username"),
                        rs.getString("password"),
                        rs.getDouble("credit_limit"));
                if (u.checkPassword(password)) {
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }
}