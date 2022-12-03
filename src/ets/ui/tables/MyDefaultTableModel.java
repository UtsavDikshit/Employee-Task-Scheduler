
package ets.ui.tables;

import javax.swing.table.DefaultTableModel;

public class MyDefaultTableModel extends DefaultTableModel{

        public MyDefaultTableModel(Object[][] os, Object[] os1) {
            super(os, os1);
        }

        @Override
        public boolean isCellEditable(int i, int i1) {
            return false;
        }   
    } 