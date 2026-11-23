import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExpenseService {
    private User user;

    public ExpenseService(User user) {
        this.user = user;
    }

    // INSERT: add one new row to the transactions table
    public void add(String description, double amount, Category category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        String sql = "INSERT INTO transactions (username, description, amount, category, date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, description);
            ps.setDouble(3, amount);
            ps.setString(4, category.name());
            ps.setString(5, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return;
        }
        checkLimit();
    }

    // SELECT: read all rows that belong to this user
    public void showAll() {
        String sql = "SELECT id, description, amount, category, date FROM transactions "
                + "WHERE username = ? ORDER BY id";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {            // goes through the rows one by one
                found = true;
                Transaction t = new Transaction(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        Category.valueOf(rs.getString("category")),
                        LocalDate.parse(rs.getString("date")));
                System.out.println(t);
            }
            if (!found) {
                System.out.println("No transactions yet.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // SUM: the database adds up the amounts for us
    public double getTotalSpent() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);   // the first (and only) column
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return 0;
    }

    // DELETE: remove one row, but only if it belongs to this user
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ? AND username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, user.getUsername());
            int rowsDeleted = ps.executeUpdate();   // how many rows were removed
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // GROUP BY: the database makes one total per category
    public void categoryReport() {
        String sql = "SELECT category, SUM(amount) AS total FROM transactions "
                + "WHERE username = ? GROUP BY category";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getString("category") + ": Rs." + rs.getDouble("total"));
            }
            if (!found) {
                System.out.println("Nothing to report yet.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Warn when spending reaches 80% of the credit limit
    public void checkLimit() {
        double limit = user.getCreditLimit();
        double spent = getTotalSpent();
        if (spent >= limit) {
            System.out.println("!! You have crossed your credit limit of Rs." + limit);
        } else if (spent >= 0.8 * limit) {
            System.out.println("!! Warning: you used 80% or more of your limit (Rs." + spent + " of Rs." + limit + ")");
        }
    }
}