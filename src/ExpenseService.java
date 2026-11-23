import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseService {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private User user;
    private int nextId = 1;

    public ExpenseService(User user) {
        this.user = user;
        load();
    }

    public void add(String description, double amount, Category category) {
        Transaction t = new Transaction(nextId++, description, amount, category, java.time.LocalDate.now());
        transactions.add(t);
        save();
        checkLimit();
    }

    public void showAll() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public double getTotalSpent() {
        double total = 0;
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
        return total;
    }

    public boolean deleteTransaction(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == id) {
                transactions.remove(i);
                save();
                return true;
            }
        }
        return false;
    }

    public void categoryReport() {
        HashMap<Category, Double> totals = new HashMap<>();
        for (Transaction t : transactions) {
            totals.put(t.getCategory(), totals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
        }
        if (totals.isEmpty()) {
            System.out.println("Nothing to report yet.");
            return;
        }
        for (Map.Entry<Category, Double> e : totals.entrySet()) {
            System.out.println(e.getKey() + ": Rs." + e.getValue());
        }
    }

    public void checkLimit() {
        double limit = user.getCreditLimit();
        double spent = getTotalSpent();
        if (spent >= limit) {
            System.out.println("!! You have crossed your credit limit of Rs." + limit);
        } else if (spent >= 0.8 * limit) {
            System.out.println("!! Warning: you used 80% or more of your limit (Rs." + spent + " of Rs." + limit + ")");
        }
    }

    private String fileName() {
        return user.getUsername() + "_expenses.csv";
    }

    private void save() {
        try (PrintWriter w = new PrintWriter(new FileWriter(fileName()))) {
            for (Transaction t : transactions) {
                w.println(t.toCsv());
            }
        } catch (IOException e) {
            System.out.println("Could not save: " + e.getMessage());
        }
    }

    private void load() {
        File f = new File(fileName());
        if (!f.exists()) {
            return;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                Transaction t = Transaction.fromCsv(line);
                transactions.add(t);
                if (t.getId() >= nextId) {
                    nextId = t.getId() + 1;
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }
}