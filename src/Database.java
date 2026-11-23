import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:expense.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY, "
                + "password TEXT NOT NULL, "
                + "credit_limit REAL NOT NULL)";

        String transactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "description TEXT, "
                + "amount REAL NOT NULL, "
                + "category TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "FOREIGN KEY (username) REFERENCES users(username))";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(usersTable);
            st.execute(transactionsTable);
            System.out.println("Database ready.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}