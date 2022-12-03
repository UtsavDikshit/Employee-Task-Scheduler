package ets;

import ets.ui.frames.Login;
import ets.utils.Environment;
import javax.swing.SwingUtilities;

/**
 * MAIN CLASS
 */
public class ETS {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Environment.loadTheme();
                Login login = new Login();
                login.setVisible(true);
            }
        });

    }
}
