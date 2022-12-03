package ets.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Dialogs {

    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void success(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void desciption(String data) {
        MyDataDialog frame = MyDataDialog.getInstance(data);
        frame.setData(data);
        frame.setVisible(true);
    }

    public static boolean confirm(String message) {
        int response = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }

    public static String input(String message) {
        String response = JOptionPane.showInputDialog(null, message);
        return response;
    }

    public static void listItems(String[] options, String title, onValueInput onInput) {
        String result = (String) JOptionPane.showInputDialog(
                null,
                title,
                "Select",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onInput.onInput(result);
            }
        });
    }

    public interface onValueInput {

        void onInput(String value);
    }
}

class MyDataDialog extends JFrame {

    private static MyDataDialog dataDialog;
    private JLabel label;

    private MyDataDialog() {
        prepare();
    }

    public static MyDataDialog getInstance(String data) {
        if (dataDialog == null) {
            dataDialog = new MyDataDialog();
        }
        dataDialog.refresh();
        return dataDialog;
    }

    private void refresh() {
        setLocationRelativeTo(null);
        setSize(new Dimension(500, 300));
    }

    private void prepare() {
        setResizable(true);
        setTitle("Data");
        refresh();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        label = new JLabel();
        label.setFont(new Font("Monospaced", Font.BOLD, 22));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(Color.BLACK);
        add(label);
    }

    public void setData(String data) {
        data = "<html>" + data + "</html>";
        data = data.replaceAll("\n", "<br>");
        label.setText(data);
    }

}
