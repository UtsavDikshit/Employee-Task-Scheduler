package ets.ui.tables;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

public final class AdapterTables extends MouseAdapter {

    private final ClickListener clickListener;

    public AdapterTables(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getComponent();
        if (mouseEvent.getClickCount() == 2
                && table.getSelectedRow() != -1
                && table.getSelectedColumn() != -1) {

            Object data = table.getModel().getValueAt(
                    table.getSelectedRow(),
                    table.getSelectedColumn());

            if (data != null) {
                clickListener.onDoubleClick(table, data);
            }
        }
    }

    public interface ClickListener {

        void onDoubleClick(JTable table, Object data);
    }
}
