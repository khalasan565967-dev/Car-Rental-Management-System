
package Controller;

import Model.UserManager;
import java.sql.SQLException;

public class LoginController {

    private UserManager userManager;

    public LoginController() {
        userManager = new UserManager();
    }

    // Login account
    public boolean login(String username, String password)
            throws SQLException {

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return false;
        }

        return userManager.loginUser(username.trim(), password);
    }

    // Register account
    public boolean register(String fullName, String username,
                            String password) throws SQLException {

        if (fullName == null || fullName.trim().isEmpty()
                || username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return false;
        }

        return userManager.registerUser(
                fullName.trim(),
                username.trim(),
                password
        );
    }

    // Check existing username
    public boolean usernameExists(String username)
            throws SQLException {

        return userManager.usernameExists(username.trim());
    }
}
