public class User {
    private String username;
    private String password;
    private double creditLimit;

    public User(String username, String password, double creditLimit) {
        this.username = username;
        this.password = password;
        this.creditLimit = creditLimit;
    }

    public String getUsername() { return username; }
    public double getCreditLimit() { return creditLimit; }

    public boolean checkPassword(String attempt) {
        return password.equals(attempt);
    }
}