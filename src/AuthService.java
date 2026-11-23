import java.util.HashMap;

public class AuthService {
    private HashMap<String, User> users = new HashMap<>();

    public boolean register(String username, String password, double creditLimit) {
        if (username.isBlank() || password.isBlank() || creditLimit <= 0) {
            return false;
        }
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password, creditLimit));
        return true;
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.checkPassword(password)) {
            return u;
        }
        return null;
    }
}