package ets.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ColorPicker extends javax.swing.JFrame {

    private static ColorPicker colorPicker = null;

    public static ColorPicker getInstance(ColorListener listener) {
        if (colorPicker == null) {
            colorPicker = new ColorPicker();
        }
        colorPicker.setListener(listener);
        return colorPicker;
    }

    public interface ColorListener {

        void onSelected(java.awt.Color color);
    }

    private ColorListener listener;
    private final List<Color> showableColors;
    private Component previous;

    //CONSTANTS
    private static final int MIN_COLOR_RANGE = 0;
    private static final int MAX_COLOR_RANGE = 255;
    private static final int INCREMENT = 50;
    private static final int COLUMNS_CHOOSER = 12;
    private static final int BORDER_SIZE_SELECTED = 10;
    private static final int TILE_GAP = 6;

    private static final Dimension TILE_SIZE = new Dimension(60, 60);
    private static final Dimension WINDOW_SIZE = new Dimension(900, 600);

    public void setListener(ColorListener listener) {
        this.listener = listener;
    }

    private ColorPicker() {

        initComponents();

        //SET SIZE
        setSize(WINDOW_SIZE);

        //GET ALL SHOWABLE COLORS
        showableColors = new RGBGenerator().generate();

        //LOAD COLORS
        load();

    }

    private void load() {
        GridLayout gl = (GridLayout) colors.getLayout();
        gl.setColumns(COLUMNS_CHOOSER);
        gl.setRows((int) showableColors.size() / COLUMNS_CHOOSER);
        gl.setHgap(TILE_GAP);
        gl.setVgap(TILE_GAP);
        for (Color c : showableColors) {
            JPanel l = new JPanel();
            l.setName(c.getString());
            l.setPreferredSize(TILE_SIZE);
            l.setBackground(new java.awt.Color(c.r, c.g, c.b));
            l.addMouseListener(adapter);
            colors.add(l);
        }
    }

    private final MouseAdapter adapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent me) {
            if (previous != null) {
                ((JPanel) previous).setBorder(null);
            }
            previous = me.getComponent();

            LineBorder lb = new LineBorder(java.awt.Color.WHITE, BORDER_SIZE_SELECTED);
            ((JPanel) me.getComponent()).setBorder(lb);

            if (listener != null) {
                Color c = new Color();
                c.load(me.getComponent().getName());
                listener.onSelected(new java.awt.Color(c.r, c.g, c.b));
            }
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorsContainer = new javax.swing.JScrollPane();
        colors = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Color Picker");

        colors.setBackground(new java.awt.Color(255, 255, 255));
        colors.setLayout(new java.awt.GridLayout(1, 0));
        colorsContainer.setViewportView(colors);

        jMenu1.setText("Custom");

        jMenuItem1.setText("RGB");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JOptionPane.showOptionDialog(
                null,
                new PanelRGBCustom(listener),
                "Custom RGB",
                JOptionPane.CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    class RGBGenerator {

        public ArrayList<Color> generate() {
            ArrayList<Color> colors = new ArrayList<>();
            for (int r = MIN_COLOR_RANGE; r <= MAX_COLOR_RANGE; r += INCREMENT) {
                for (int g = MIN_COLOR_RANGE; g <= MAX_COLOR_RANGE; g += INCREMENT) {
                    for (int b = MIN_COLOR_RANGE; b <= MAX_COLOR_RANGE; b += INCREMENT) {
                        colors.add(new Color(r, g, b));
                    }
                }
            }
            return colors;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel colors;
    private javax.swing.JScrollPane colorsContainer;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables

}

class Color {

    public int r, g, b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color() {
    }

    public String getString() {
        return r + "," + g + "," + b;
    }

    public void load(String data) {
        try {
            r = Integer.parseInt(data.split(",")[0]);
            g = Integer.parseInt(data.split(",")[1]);
            b = Integer.parseInt(data.split(",")[2]);
        } catch (Exception ignore) {

        }
    }
}
