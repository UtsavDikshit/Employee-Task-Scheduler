package ets.ui.frames;

import ets.core.QRClass;
import ets.entity.Admin;
import ets.entity.Employee;
import ets.entity.ProjectManager;
import ets.ui.base.BaseFrame;
import ets.utils.Dialogs;
import ets.utils.Environment;
import ets.values.Permissions;
import ets.values.Settings;
import java.awt.CardLayout;

public class Login extends BaseFrame implements Settings {

    static {
        Environment.loadTheme();
    }
                         
    public Login() {
        initComponents();
        login_btn.addMouseListener(adapterButtons);
        ///startCamera();
    }

    private QRClass qrclass = null;
    
    private void startCamera() {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (QRClass qr = new QRClass(cameraPanel)) {
                    qrclass = qr;
                    parseQRText(qr.getResult());
                } catch (InterruptedException ignored) {
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void parseQRText(String text) {
        try {
            String username = text.split("====")[0];
            String pass = text.split("====")[1];
            
            userid.setText(username);
            password.setText(pass);
            role.setSelectedItem("Employee");
            
            login_btnActionPerformed(null);
        } catch (Exception ex) {
            Dialogs.error("QR Code is invalid");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        mainContainer = new javax.swing.JPanel();
        cameraPanel = new javax.swing.JPanel();
        userid = new javax.swing.JTextField();
        login_btn = new javax.swing.JButton();
        role = new javax.swing.JComboBox<>();
        password = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        progressContainer = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        main.setLayout(new java.awt.CardLayout());

        mainContainer.setBackground(new java.awt.Color(255, 255, 255));

        cameraPanel.setLayout(new java.awt.BorderLayout());

        userid.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N

        login_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        login_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/add.png"))); // NOI18N
        login_btn.setText("Login");
        login_btn.setIconTextGap(10);
        login_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_btnActionPerformed(evt);
            }
        });

        role.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N
        role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee", "Project Manager", "Admin" }));

        password.setFont(new java.awt.Font("Monospaced", 1, 16)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/password.png"))); // NOI18N
        jLabel2.setText("Password  : ");
        jLabel2.setIconTextGap(10);
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/role.png"))); // NOI18N
        jLabel3.setText("Role : ");
        jLabel3.setIconTextGap(10);
        jLabel3.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/images/id.png"))); // NOI18N
        jLabel1.setText("User ID : ");
        jLabel1.setIconTextGap(10);
        jLabel1.setMinimumSize(new java.awt.Dimension(20, 20));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("$Employee$");
        jLabel4.setOpaque(true);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setText("@ProjectManager@");
        jLabel5.setOpaque(true);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setText("#Task#");
        jLabel6.setOpaque(true);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("*Department*");
        jLabel7.setOpaque(true);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setText("Show Your QR Code on Camera");
        jLabel9.setOpaque(true);

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(login_btn)
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13)
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(password, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(userid, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(role, 0, 366, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .addComponent(cameraPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(userid, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(role, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(login_btn)
                        .addGap(20, 20, 20)
                        .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(mainContainerLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel6)))
                        .addGap(0, 46, Short.MAX_VALUE))
                    .addGroup(mainContainerLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cameraPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        main.add(mainContainer, "mainContainer");

        progressContainer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ets/icons/progress.gif"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Roboto Black", 0, 30)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("LOADING PLEASE WAIT...");

        javax.swing.GroupLayout progressContainerLayout = new javax.swing.GroupLayout(progressContainer);
        progressContainer.setLayout(progressContainerLayout);
        progressContainerLayout.setHorizontalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressContainerLayout.createSequentialGroup()
                .addGroup(progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(458, 458, 458)
                        .addComponent(jLabel8))
                    .addGroup(progressContainerLayout.createSequentialGroup()
                        .addGap(238, 238, 238)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(240, Short.MAX_VALUE))
        );
        progressContainerLayout.setVerticalGroup(
            progressContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressContainerLayout.createSequentialGroup()
                .addContainerGap(132, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );

        main.add(progressContainer, "progressContainer");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_btnActionPerformed

        //SHOW PROGRESS PANEL
        CardLayout card = (CardLayout) main.getLayout();
        card.show(main, "progressContainer");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                doLogin();

                //SHOW PROGRESS PANEL
                CardLayout card = (CardLayout) main.getLayout();
                card.show(main, "mainContainer");
            }
        });
        t.start();
    }//GEN-LAST:event_login_btnActionPerformed

    @Override
    public void dispose() {
        super.dispose();
        //qrclass.close();
    }
    
    private void doLogin() {
        String _id = userid.getText();
        String _pass = password.getText();
        String _role = role.getSelectedItem().toString();

        if (_id.isEmpty() || _pass.isEmpty()) {
            Dialogs.error("Some fields are missing");
            return;
        }

        switch (_role) {
            case "Employee":
                Employee e = new Employee(_id);
                if (e.getPassword() != null && !e.getPassword().isEmpty() && e.getPassword().equals(_pass)) {
                    Environment.department = e.getDepartment_id();
                    DashboardEmployee dashboard = new DashboardEmployee(_id, Permissions.MODE_FULL);
                    dashboard.setVisible(true);
                    dispose();
                } else {
                    Dialogs.error("Invalid credentials");
                }
                break;
            case "Project Manager":
                ProjectManager pm = new ProjectManager(_id);
                if (pm.getPassword() != null && !pm.getPassword().isEmpty() && pm.getPassword().equals(_pass)) {
                    Environment.department = pm.getDepartment_id();
                    DashboardProjectManager dashboard = new DashboardProjectManager(_id, Permissions.MODE_FULL);
                    dashboard.setVisible(true);
                    dispose();
                } else {
                    Dialogs.error("Invalid credentials");
                }
                break;
            case "Admin":
                Admin a = new Admin(_id);
                if (a.getPassword() != null && !a.getPassword().isEmpty() && a.getPassword().equals(_pass)) {
                    DashboardAdmin dashboard = new DashboardAdmin(_id, Permissions.MODE_FULL);
                    dashboard.setVisible(true);
                    dispose();
                } else {
                    Dialogs.error("Invalid credentials");
                }
                break;
        }
    }

    /*
    private vologin_btnionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glActionPerformed
        
    }//GEN-LAST:event_glActionPerformed
*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cameraPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton login_btn;
    private javax.swing.JPanel main;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPasswordField password;
    private javax.swing.JPanel progressContainer;
    private javax.swing.JComboBox<String> role;
    private javax.swing.JTextField userid;
    // End of variables declaration//GEN-END:variables
}
