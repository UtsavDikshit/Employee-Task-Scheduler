
package ets.ui.base;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class BaseProjectManagerFrame extends JFrame{
    
    //BASE COLORS
    public static Color background_unclicked = new Color(0,0,0);
    public static Color background_clicked = new Color(45,10,45);
    public static Color foreground_unclicked = Color.WHITE;
    public static Color foreground_clicked = Color.WHITE;
    public static Color background_clicked_container = Color.WHITE;
    
    //GENERAL BUTTON COLORS
    public static Color general_background_unclicked = Color.WHITE;
    public static Color general_background_clicked = new Color(45, 10, 45);
    public static Color general_foreground_unclicked = Color.GRAY;
    public static Color general_foreground_clicked = Color.WHITE;
    
    //GENERAL COLORS
    public static Color green = new Color(0,255,0);
    public static Color red = new Color(255,0,0);
    
    public final MouseAdapter adapterAll = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent me) {
            super.mouseEntered(me);
            ((JComponent)me.getComponent()).setBackground(background_clicked);
            ((JComponent)me.getComponent()).setForeground(foreground_clicked);   
        }

        @Override
        public void mouseExited(MouseEvent me) {
            super.mouseExited(me);
            ((JComponent)me.getComponent()).setBackground(background_unclicked);
            ((JComponent)me.getComponent()).setForeground(foreground_unclicked);
        } 
    };
    
    public final MouseAdapter adapterButtons = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent me) {
            super.mouseEntered(me);
            ((JComponent)me.getComponent()).setBackground(general_background_clicked);
            ((JComponent)me.getComponent()).setForeground(general_foreground_clicked);  
        }

        @Override
        public void mouseExited(MouseEvent me) {
            super.mouseExited(me);
            ((JComponent)me.getComponent()).setBackground(general_background_unclicked);
            ((JComponent)me.getComponent()).setForeground(general_foreground_unclicked);  
        } 
    };
     
}
