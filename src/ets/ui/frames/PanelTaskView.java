package ets.ui.frames;

import ets.entity.Task;
import ets.listeners.Refreshable;
import ets.tools.Editor;
import ets.utils.Dialogs;
import ets.viewcontroller.ViewControllerTask;
import java.awt.CardLayout;

public final class PanelTaskView extends javax.swing.JFrame implements Refreshable.Database {

    private int mode = -1;
    private String taskID = null;

    public PanelTaskView(String taskID, int mode) {

        //INIT COMPONENTS
        initComponents();

        //SET PARAMETERS
        setTitle("Task : " + taskID);
        setSize(1060, 630);
        setLocationRelativeTo(null);

        //INIT PARAMETERS
        this.mode = mode;
        this.taskID = taskID;

        updateDatabase();

        //REGSITER RECEIVER
        Refreshable.register(this);
    }

    private void loadTask() {
        ViewControllerTask viewControllerTask = new ViewControllerTask(ctpanel);
        viewControllerTask.loadTask(new Task(taskID), mode);
    }

    @Override
    public void dispose() {
        Refreshable.unregister(this);
    }

    @Override
    public void updateDatabase() {

        //SHOW PROGRESS PANEL
        CardLayout card = (CardLayout) main.getLayout();
        card.show(main, "progressContainer");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                loadTask();

                //SHOW TASK PANEL
                CardLayout card = (CardLayout) main.getLayout();
                card.show(main, "mainContainer");
            }
        });

        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        progressContainer = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ctpanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        deadline = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        submitted = new javax.swing.JTextField();
        entry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        deadline1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        score1 = new javax.swing.JTextField();
        tid = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        employee = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        id2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        head = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        desc = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        response = new javax.swing.JLabel();
        refreshIcon = new javax.swing.JLabel();

        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        main.setLayout(new java.awt.CardLayout());

        progressContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Roboto Black", 0, 30)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("LOADING PLEASE WAIT...");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/progress.gif"))); // NOI18N

        javax.swing.GroupLayout progressContainerLayout = new javax.swing.GroupLayout(progressContainer);
        progressContainer.setLayout(progressContainerLayout);
        progressContainerLayout.setHorizontalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressContainerLayout.createSequentialGroup()
                .addGroup(progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(354, 354, 354)
                        .addComponent(jLabel11))
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(497, 497, 497)
                        .addComponent(jLabel4)))
                .addContainerGap(355, Short.MAX_VALUE))
        );
        progressContainerLayout.setVerticalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressContainerLayout.createSequentialGroup()
                .addContainerGap(262, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(225, 225, 225))
        );

        main.add(progressContainer, "progressContainer");

        ctpanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));

        jLabel19.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel19.setText("Entry : ");
        jLabel19.setIconTextGap(15);
        jLabel19.setMinimumSize(new java.awt.Dimension(20, 20));

        deadline.setEditable(false);
        deadline.setBackground(new java.awt.Color(245, 245, 245));
        deadline.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        deadline.setName("tentry"); // NOI18N

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel20.setText("Assigned : ");
        jLabel20.setIconTextGap(15);
        jLabel20.setMinimumSize(new java.awt.Dimension(20, 20));

        submitted.setEditable(false);
        submitted.setBackground(new java.awt.Color(245, 245, 245));
        submitted.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        submitted.setName("tsubmitted"); // NOI18N

        entry.setEditable(false);
        entry.setBackground(new java.awt.Color(245, 245, 245));
        entry.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        entry.setName("tassigned"); // NOI18N

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel21.setText("Submitted : ");
        jLabel21.setIconTextGap(15);
        jLabel21.setMinimumSize(new java.awt.Dimension(20, 20));

        deadline1.setEditable(false);
        deadline1.setBackground(new java.awt.Color(245, 245, 245));
        deadline1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        deadline1.setName("tdeadline"); // NOI18N

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/calendar_small.png"))); // NOI18N
        jLabel22.setText("Deadline : ");
        jLabel22.setIconTextGap(15);
        jLabel22.setMinimumSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deadline, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                            .addComponent(submitted)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entry))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deadline1)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entry, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitted, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deadline1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(250, 250, 250));

        score1.setEditable(false);
        score1.setBackground(new java.awt.Color(245, 245, 245));
        score1.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        score1.setName("tscore"); // NOI18N

        tid.setEditable(false);
        tid.setBackground(new java.awt.Color(245, 245, 245));
        tid.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        tid.setName("tid"); // NOI18N

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/status_small.png"))); // NOI18N
        jLabel23.setText("Status : ");
        jLabel23.setIconTextGap(15);
        jLabel23.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel24.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id_small.png"))); // NOI18N
        jLabel24.setText("Task ID : ");
        jLabel24.setIconTextGap(15);
        jLabel24.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel25.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/score_small.png"))); // NOI18N
        jLabel25.setText("Score : ");
        jLabel25.setIconTextGap(15);
        jLabel25.setMinimumSize(new java.awt.Dimension(20, 20));

        employee.setEditable(false);
        employee.setBackground(new java.awt.Color(245, 245, 245));
        employee.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        employee.setName("tstatus"); // NOI18N

        jLabel26.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/department_small.png"))); // NOI18N
        jLabel26.setText("Employee : ");
        jLabel26.setIconTextGap(15);
        jLabel26.setMinimumSize(new java.awt.Dimension(20, 20));

        id2.setEditable(false);
        id2.setBackground(new java.awt.Color(245, 245, 245));
        id2.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        id2.setName("temployee"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tid, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(employee))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(score1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(id2)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(score1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/header.png"))); // NOI18N
        jLabel27.setText("Header : ");
        jLabel27.setIconTextGap(10);
        jLabel27.setMinimumSize(new java.awt.Dimension(20, 20));

        head.setEditable(false);
        head.setBackground(new java.awt.Color(245, 245, 245));
        head.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        head.setName("theader"); // NOI18N
        head.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                headMouseClicked(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/description.png"))); // NOI18N
        jLabel28.setText("Description : ");
        jLabel28.setIconTextGap(10);
        jLabel28.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/response.png"))); // NOI18N
        jLabel29.setText("Response : ");
        jLabel29.setIconTextGap(10);
        jLabel29.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel30.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 102, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Double Click on Reponse Box for EdItor/IDE");

        desc.setEditable(false);
        desc.setBackground(new java.awt.Color(245, 245, 245));
        desc.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        desc.setName("tdesc"); // NOI18N
        desc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descMouseClicked(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 102, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Double Click on Other Box for Details");

        response.setBackground(new java.awt.Color(245, 245, 245));
        response.setFont(new java.awt.Font("Monospaced", 1, 20)); // NOI18N
        response.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        response.setName("tresponse"); // NOI18N
        response.setOpaque(true);
        response.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                responseMouseClicked(evt);
            }
        });

        refreshIcon.setBackground(new java.awt.Color(0, 0, 0));
        refreshIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/refresh.png"))); // NOI18N
        refreshIcon.setOpaque(true);
        refreshIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshIconMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ctpanelLayout = new javax.swing.GroupLayout(ctpanel);
        ctpanel.setLayout(ctpanelLayout);
        ctpanelLayout.setHorizontalGroup(
            ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ctpanelLayout.createSequentialGroup()
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ctpanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(response, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(desc))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ctpanelLayout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(head))
                            .addGroup(ctpanelLayout.createSequentialGroup()
                                .addComponent(refreshIcon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(ctpanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ctpanelLayout.setVerticalGroup(
            ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ctpanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(desc, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(response, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addGroup(ctpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ctpanelLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31))
                    .addComponent(refreshIcon))
                .addContainerGap())
        );

        main.add(ctpanel, "mainContainer");
        ctpanel.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void headMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headMouseClicked
        if (evt.getClickCount() == 2) {
            Dialogs.desciption(head.getText());
        }
    }//GEN-LAST:event_headMouseClicked

    private void descMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descMouseClicked
        if (evt.getClickCount() == 2) {
            Dialogs.desciption(desc.getText());
        }
    }//GEN-LAST:event_descMouseClicked

    private void responseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_responseMouseClicked
        if (evt.getClickCount() == 2 && !tid.getText().equals("null")) {
            if (tid.getText() != null && !tid.getText().isEmpty()) {
                Editor editor = new Editor(tid.getText(), mode);
                editor.setVisible(true);
            }
        }
    }//GEN-LAST:event_responseMouseClicked

    private void refreshIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshIconMouseClicked
        updateDatabase();
    }//GEN-LAST:event_refreshIconMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel ctpanel;
    private javax.swing.JTextField deadline;
    private javax.swing.JTextField deadline1;
    private javax.swing.JTextField desc;
    private javax.swing.JTextField employee;
    private javax.swing.JTextField entry;
    private javax.swing.JTextField head;
    private javax.swing.JTextField id2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel main;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JLabel refreshIcon;
    private javax.swing.JLabel response;
    private javax.swing.JTextField score1;
    private javax.swing.JTextField submitted;
    private javax.swing.JTextField tid;
    // End of variables declaration//GEN-END:variables
}
