package ets.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Utils {

    private static int seconds, minutes, hrs, days;
    private static long time;

    public static String convertStamp(long stamp) {

        if (stamp == 0) {
            return "N.A";
        }

        Date date = new Date();
        time = date.getTime() - stamp;

        if (time < 0) {

            time = -1 * time;

            //PAST EVENT
            seconds = (int) (time / 1000);
            minutes = seconds / 60;
            hrs = minutes / 60;
            days = hrs / 24;
            if (minutes == 0 || minutes == 1) {
                return "Almost Over";
            } else if (minutes >= 2 && minutes <= 59) {
                return minutes + " min remaining";
            } else if (hrs >= 1 && hrs <= 23) {
                return hrs + " hrs remaining";
            } else {
                return days + " days remaining";
            }

        } else if (time > 0) {

            //FUTURE EVENT
            seconds = (int) (time / 1000);
            minutes = seconds / 60;
            hrs = minutes / 60;
            days = hrs / 24;
            if (minutes == 0 || minutes == 1) {
                return "Just Now";
            } else if (minutes >= 2 && minutes <= 59) {
                return minutes + " min ago";
            } else if (hrs >= 1 && hrs <= 23) {
                return hrs + " hrs ago";
            } else {
                return days + " days ago";
            }

        } else {
            return "Just Now";
        }

    }

    public static String convertStampLong(long stamp) {
        if (stamp == 0) {
            return "N.A";
        }
        return new Date(stamp).toLocaleString();
    }

    public static String readFile(String path) {
        try {
            File file = new File(path);
            String data;
            try (Scanner reader = new Scanner(file)) {
                data = "";
                while (reader.hasNextLine()) {
                    data += reader.nextLine() + "\n";
                }
            }
            return data;
        } catch (FileNotFoundException ex) {
            Dialogs.error("Error : " + ex.getMessage());
            return "";
        }
    }

    public static TableModel rs_to_tm(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        Vector columnNames = new Vector();
        for (int column = 0; column < numberOfColumns; column++) {
            columnNames.addElement(metaData.getColumnLabel(column + 1));
        }
        Vector rows = new Vector();
        while (rs.next()) {
            Vector newRow = new Vector();
            for (int i = 1; i <= numberOfColumns; i++) {
                newRow.addElement(rs.getObject(i));
            }
            rows.addElement(newRow);
        }
        return new DefaultTableModel(rows, columnNames);
    }

}
