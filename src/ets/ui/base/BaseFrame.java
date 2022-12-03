package ets.ui.base;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class BaseFrame extends JFrame {

    //GENERAL BUTTON COLORS
    public static Color general_background_unclicked = Color.WHITE;
    public static Color general_background_clicked = new Color(10, 10, 10);
    public static Color general_foreground_unclicked = Color.GRAY;
    public static Color general_foreground_clicked = Color.WHITE;

    public final MouseAdapter adapterButtons = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent me) {
            super.mouseEntered(me);
            ((JComponent) me.getComponent()).setBackground(general_background_clicked);
            ((JComponent) me.getComponent()).setForeground(general_foreground_clicked);
        }

        @Override
        public void mouseExited(MouseEvent me) {
            super.mouseExited(me);
            ((JComponent) me.getComponent()).setBackground(general_background_unclicked);
            ((JComponent) me.getComponent()).setForeground(general_foreground_unclicked);
        }
    };

}
