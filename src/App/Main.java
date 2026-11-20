
package App;

import javax.swing.SwingUtilities;
import View.LoginView;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginView();
        });

    }
}
