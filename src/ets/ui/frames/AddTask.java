package ets.ui.frames;

import ets.ui.base.BaseProjectManagerFrame;
import ets.entity.Task;
import ets.error.ExceptionHandler;
import ets.values.Constants;
import ets.tools.DateTimePicker;
import ets.utils.Dialogs;
import ets.utils.Environment;
import ets.values.Settings;
import ets.utils.Utils;
import ets.listeners.onSelected;
import java.sql.SQLException;
import java.util.Date;

public class AddTask extends BaseProjectManagerFrame implements Settings, Constants {

    private Date date = null;

    public AddTask() {
        initComponents();
        add_btn.addMouseListener(adapterButtons);
        reset_btn.addMouseListener(adapterButtons);
        choose.addMouseListener(adapterButtons);

        //LOAD DEPARTMENT NAME
        department.setText(Environment.department);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        add_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        head = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        score = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        deadline = new javax.swing.JTextField();
        choose = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        reset_btn = new javax.swing.JButton();
        department = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Task");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("ID will be generated Automatically");

        add_btn.setBackground(new java.awt.Color(255, 255, 255));
        add_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        add_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/add.png"))); // NOI18N
        add_btn.setText("Add");
        add_btn.setIconTextGap(10);
        add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_btnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/header.png"))); // NOI18N
        jLabel1.setText("Header : ");
        jLabel1.setIconTextGap(10);
        jLabel1.setMinimumSize(new java.awt.Dimension(20, 20));

        desc.setColumns(20);
        desc.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        desc.setRows(5);
        jScrollPane3.setViewportView(desc);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/description.png"))); // NOI18N
        jLabel2.setText("Description : ");
        jLabel2.setIconTextGap(10);
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 20));

        head.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/score.png"))); // NOI18N
        jLabel4.setText("Score : ");
        jLabel4.setIconTextGap(10);
        jLabel4.setMinimumSize(new java.awt.Dimension(20, 20));

        score.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar.png"))); // NOI18N
        jLabel6.setText("Deadline : ");
        jLabel6.setIconTextGap(10);
        jLabel6.setMinimumSize(new java.awt.Dimension(20, 20));

        deadline.setEditable(false);
        deadline.setBackground(new java.awt.Color(204, 204, 255));
        deadline.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N

        choose.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        choose.setForeground(new java.awt.Color(153, 0, 0));
        choose.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        choose.setText("Click here to choose");
        choose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id.png"))); // NOI18N
        jLabel8.setText("ID : ");
        jLabel8.setMinimumSize(new java.awt.Dimension(20, 20));

        id.setEditable(false);
        id.setBackground(new java.awt.Color(204, 204, 255));
        id.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N

        reset_btn.setBackground(new java.awt.Color(255, 255, 255));
        reset_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        reset_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/reset.png"))); // NOI18N
        reset_btn.setText("Reset");
        reset_btn.setIconTextGap(10);
        reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_btnActionPerformed(evt);
            }
        });

        department.setEditable(false);
        department.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/department.png"))); // NOI18N
        jLabel7.setText("Department : ");
        jLabel7.setIconTextGap(10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reset_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(add_btn)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(id)
                            .addComponent(score, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(deadline)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(choose))
                            .addComponent(department))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(score, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choose))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add_btn)
                    .addComponent(reset_btn)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_btnActionPerformed

        String _head = head.getText();
        String _desc = desc.getText();
        long _score = -1;

        try {
            _score = Long.parseLong(score.getText());
        } catch (NumberFormatException ex) {
            Dialogs.error("Invalid Score");
            return;
        }

        long _entry = System.currentTimeMillis();

        if (date == null || _score == -1 || _head.isEmpty() || _desc.isEmpty()) {
            Dialogs.error("Some fields are missing");
            return;
        }

        if (!(date.getTime() - _entry >= MINIMUM_TASK_TIME_TO_DEADLINE)) {
            Dialogs.error("Deadline is beyond/before the specified limits");
            return;
        }

        String ID = TASK_IDENTIFIER + Task.getNextTaskNumber() + TASK_IDENTIFIER;
        id.setText(ID);

        Task task = new Task();

        task.setId(ID);
        task.setHead(_head);
        task.setDesc(_desc);
        task.setWork(null);
        task.setDepartment_id(Environment.department);
        task.setStatus(Task.STATUS_UNASSIGNED);
        task.setEmpid(null);
        task.setEntry(_entry);
        task.setExit(date.getTime());
        task.setAssigned(0);
        task.setSubmit(0);
        task.setScore(_score);

        try {
            Task.saveTask(task);
            Dialogs.success("Added Successfully");
        } catch (SQLException ex) {
            ExceptionHandler.exception(ex);
            Dialogs.error("Task not added. See log for details.");
        }

    }//GEN-LAST:event_add_btnActionPerformed

    private void chooseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chooseMouseClicked

        DateTimePicker dateTimePicker = new DateTimePicker(new onSelected() {
            @Override
            public void onSelected(Date date) {
                AddTask.this.date = date;
                deadline.setText(Utils.convertStamp(date.getTime()));
            }
        });
        dateTimePicker.setVisible(true);
    }//GEN-LAST:event_chooseMouseClicked

    private void reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_btnActionPerformed
        head.setText(null);
        desc.setText(null);
        score.setText(null);
        deadline.setText(null);
        date = null;
        id.setText(null);
    }//GEN-LAST:event_reset_btnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_btn;
    private javax.swing.JLabel choose;
    private javax.swing.JTextField deadline;
    private javax.swing.JTextField department;
    private javax.swing.JTextArea desc;
    private javax.swing.JTextField head;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton reset_btn;
    private javax.swing.JTextField score;
    // End of variables declaration//GEN-END:variables
}
