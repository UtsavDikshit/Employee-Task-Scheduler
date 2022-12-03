package ets.utils;

import ets.persistance.SettingsPersistance;

public class Environment {

    public static String department = null;

    public static void loadTheme() {

        try {
            String theme = SettingsPersistance.getProperty(SettingsPersistance.KEY_THEME);
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (theme.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {}
    }

}
