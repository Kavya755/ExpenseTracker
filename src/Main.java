import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("\n=== Credit Card Expense Tracker ===");
            System.out.println("1. Register\n2. Login\n3. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Username: ");
                String u = sc.nextLine().trim();
                System.out.print("Password: ");
                String p = sc.nextLine().trim();
                double limit = readDouble("Credit limit: ");
                System.out.println(auth.register(u, p, limit) ? "Registered!" : "Registration failed (blank or username taken).");
            } else if (choice.equals("2")) {
                System.out.print("Username: ");
                String u = sc.nextLine().trim();
                System.out.print("Password: ");
                String p = sc.nextLine().trim();
                User user = auth.login(u, p);
                if (user == null) {
                    System.out.println("Wrong username or password.");
                } else {
                    expenseMenu(user);
                }
            } else if (choice.equals("3")) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Please choose 1, 2 or 3.");
            }
        }
    }

    static void expenseMenu(User user) {
        ExpenseService service = new ExpenseService(user);
        while (true) {
            System.out.println("\n--- Welcome " + user.getUsername() + " ---");
            System.out.println("1. Add expense\n2. Show all\n3. Delete expense\n4. Total spent\n5. Category report\n6. Logout");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                if (choice.equals("1")) {
                    System.out.print("Description (no commas): ");
                    String d = sc.nextLine().trim().replace(",", " ");
                    double amount = readDouble("Amount: ");
                    System.out.println("Categories: " + java.util.Arrays.toString(Category.values()));
                    System.out.print("Category: ");
                    Category c = Category.valueOf(sc.nextLine().trim().toUpperCase());
                    service.add(d, amount, c);
                    System.out.println("Added!");
                } else if (choice.equals("2")) {
                    service.showAll();
                } else if (choice.equals("3")) {
                    int id = (int) readDouble("Id to delete: ");
                    System.out.println(service.deleteTransaction(id) ? "Deleted." : "No transaction with that id.");
                } else if (choice.equals("4")) {
                    System.out.println("Total spent: Rs." + service.getTotalSpent());
                } else if (choice.equals("5")) {
                    service.categoryReport();
                } else if (choice.equals("6")) {
                    return;
                } else {
                    System.out.println("Please choose 1 to 6.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please type a number.");
            }
        }
    }
}