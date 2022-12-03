package ets.tools;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import ets.listeners.onSelected;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;

public class DateTimePicker extends JFrame {

    private onSelected onSelected = null;

    public DateTimePicker(onSelected onSelected) {
        init();
        this.onSelected = onSelected;
    }

    private void init() {
        setTitle("Choose");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(new Dimension(310, 110));
        setLocationRelativeTo(null);
        setResizable(false);

        DatePicker datePicker = new DatePicker();
        add(datePicker);

        TimePicker timePicker = new TimePicker();
        add(timePicker);

        JButton button = new JButton("Select");
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (datePicker.getDate() == null || timePicker.getTime() == null) {
                    return;
                }
                Date d = toDate(datePicker.getDate().toString(), timePicker.getTime().toString());
                onSelected.onSelected(d);
                setVisible(false);
            }

            private Date toDate(String d, String t) {
                Date date = new Date();
                date.setDate(Integer.parseInt(d.split("-")[2]));
                date.setMonth(Integer.parseInt(d.split("-")[1]) - 1);
                date.setYear(Integer.parseInt(d.split("-")[0]) - 1900);
                date.setHours(Integer.parseInt(t.split(":")[0]));
                date.setMinutes(Integer.parseInt(t.split(":")[1]));
                date.setSeconds(0);
                return date;
            }
        });

    }
}
