package ets.persistance;

import java.io.FileReader;
import java.util.Properties;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;

public class SettingsPersistance {

    private static final String FILE_NAME = "settings.properties";

    public static final String KEY_COLOR_PROJECT_MANAGER = "colorpm";
    public static final String DEFAULT_VALUE_COLOR_PROJECT_MANAGER = "204,0,204";

    public static final String KEY_COLOR_ADMIN = "coloradmin";
    public static final String DEFAULT_VALUE_COLOR_ADMIN = "0,153,153";

    public static final String KEY_COLOR_EMPLOYEE = "coloremployee";
    public static final String DEFAULT_VALUE_COLOR_EMPLOYEE = "225,25,25";

    public static final String KEY_THEME = "theme";
    public static final String DEFAULT_VALUE_THEME = "Windows";

    public static String getProperty(String KEY) {
        String value = get(KEY);
        if (value == null || value.isEmpty()) {
            switch (KEY) {
                case KEY_COLOR_PROJECT_MANAGER:
                    return DEFAULT_VALUE_COLOR_PROJECT_MANAGER;
                case KEY_COLOR_ADMIN:
                    return DEFAULT_VALUE_COLOR_ADMIN;
                case KEY_COLOR_EMPLOYEE:
                    return DEFAULT_VALUE_COLOR_EMPLOYEE;
                case KEY_THEME:
                    return DEFAULT_VALUE_THEME;
            }
        }
        return value;
    }

    private static String get(String KEY) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader reader = new FileReader(file);
            Properties p = new Properties();
            p.load(reader);
            return p.getProperty(KEY, "");
        } catch (Exception ignore) {
            System.out.println(ignore);
            return "";
        }
    }

    public static void setProperty(String KEY, String VALUE) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader reader = new FileReader(file);
            Properties p = new Properties();
            p.load(reader);
            p.put(KEY, VALUE);

            p.store(new FileWriter(FILE_NAME), null);
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
    }

    public static String ColortoString(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return r + "," + g + "," + b;
    }

    public static Color StringtoColor(String data) {
        int r = 0;
        int g = 0;
        int b = 0;
        try {
            r = Integer.parseInt(data.split(",")[0]);
            g = Integer.parseInt(data.split(",")[1]);
            b = Integer.parseInt(data.split(",")[2]);
        } catch (Exception ignore) {
        }
        return new Color(r, g, b);
    }

}
