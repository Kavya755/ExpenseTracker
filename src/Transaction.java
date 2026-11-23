import java.time.LocalDate;

public class Transaction {
    private int id;
    private String description;
    private double amount;
    private Category category;
    private LocalDate date;

    public Transaction(int id, String description, double amount, Category category, LocalDate date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }

    public String toCsv() {
        return id + "," + description + "," + amount + "," + category + "," + date;
    }

    public static Transaction fromCsv(String line) {
        String[] p = line.split(",");
        return new Transaction(Integer.parseInt(p[0]), p[1], Double.parseDouble(p[2]),
                Category.valueOf(p[3]), LocalDate.parse(p[4]));
    }

    @Override
    public String toString() {
        return "#" + id + " | " + date + " | " + category + " | " + description + " | Rs." + amount;
    }
}