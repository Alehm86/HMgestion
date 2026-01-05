/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import Class.ProductDAO;
import Class.modelSupplier;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

public class frmSupplierNew extends javax.swing.JDialog {

    ProductDAO queries = new ProductDAO();
    modelSupplier classSupplier = new modelSupplier();
    
    private String SupplierSelected = "";
    private String proveedorCreado;
    
    public String getProveedorCreado() {       
        return proveedorCreado;
    }
    
    public frmSupplierNew(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        queries.listTableSupplier(tableSupplier);
        actions();
    }
    
    void actions(){
        
     
        btnCancel.addActionListener(e -> {
            clearFields();
            tableSupplier.setEnabled(true);
        });
        
        btnExit.addActionListener(e -> {
            this.dispose();
        });
        
        btnConfirm.addActionListener(e -> {
            nuevoProveedor();
        });
        
    }
    
    void clearFields(){

        txtName.setText("");
        txtCuit.setText("");
        txtTel.setText("");
        txtMail.setText("");
        txtUrl.setText("");
        txtUser.setText("");
        txtPass.setText("");
        
    }
    
   
    void nuevoProveedor(){
   
        if(!txtName.getText().isEmpty()){         
            classSupplier.name=txtName.getText().toUpperCase();        
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe ingresar un nombre de proveedor!");           
            txtName.requestFocusInWindow();
            return;       
        }
        
        if(!txtCuit.getText().isEmpty()){
            classSupplier.cuit=txtCuit.getText();
        }else{
            classSupplier.cuit="00-00000000-0";
        }
        
        if(!txtTel.getText().isEmpty()){
            classSupplier.telefono = txtTel.getText();
        }else{
            classSupplier.telefono="0";
        }
        
        if(!txtMail.getText().isEmpty()){
            classSupplier.email=txtMail.getText();
        }else{
            classSupplier.email="Sin datos";
        }
        
        if(!txtUrl.getText().isEmpty()){
            classSupplier.web=txtUrl.getText();
        }else{
            classSupplier.web="Sin datos";
        }
        
        if(!txtUser.getText().isEmpty()){
            classSupplier.user=txtUser.getText();
        }else{
            classSupplier.user="Sin datos";
        }
        
        if(!txtPass.getText().isEmpty()){
            classSupplier.pass=txtPass.getText();
        }else{
            classSupplier.pass="Sin datos";
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Registrar el proveedor " + classSupplier.getName()+"?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
            
        if (!queries.nameExists(txtName.getText(),"suppliers")) {
            queries.newSupplier(
            classSupplier.getName(),
            classSupplier.getCuit(),
            classSupplier.getTelephone(),
            classSupplier.getEmail(),
            classSupplier.getWeb(),
            classSupplier.getUser(),
            classSupplier.getPass()); 
            
            proveedorCreado = classSupplier.getName();
            queries.listTableSupplier(tableSupplier);
            
            
            JOptionPane.showMessageDialog(null, "Proveedor creado correctamente. ✅");
            
            int msjNuevo = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas crear otro proveedor?",
                "Confirmar registro",
                JOptionPane.YES_NO_OPTION
            );

            if (msjNuevo == JOptionPane.YES_OPTION) {
                clearFields();  
            }else{
                this.dispose();
            }            
            
            
        }else{
            JOptionPane.showMessageDialog(null, "El nombre del proveedor ya existe!..");
        }
            
        proveedorCreado = classSupplier.getName();
        queries.listTableSupplier(tableSupplier);
        clearFields();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelDates = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btnConfirm = new javax.swing.JButton();
        labelName = new javax.swing.JLabel();
        labelCuit = new javax.swing.JLabel();
        labelTel = new javax.swing.JLabel();
        labelMail = new javax.swing.JLabel();
        labelUrl = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        labelPass = new javax.swing.JLabel();
        txtTel = new javax.swing.JTextField();
        txtMail = new javax.swing.JTextField();
        txtUrl = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        txtCuit = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSupplier = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        labelTitle1 = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Gestion de proveedores");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanelDates.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDates.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtName.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnConfirm.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(102, 102, 102));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Registrar");
        btnConfirm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmMouseExited(evt);
            }
        });
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        labelName.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelName.setForeground(new java.awt.Color(13, 155, 219));
        labelName.setText("Nombre:");

        labelCuit.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelCuit.setForeground(new java.awt.Color(13, 155, 219));
        labelCuit.setText("C.U.I.T.");

        labelTel.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelTel.setForeground(new java.awt.Color(13, 155, 219));
        labelTel.setText("Teléfono:");

        labelMail.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelMail.setForeground(new java.awt.Color(13, 155, 219));
        labelMail.setText("Correo electrónico:");

        labelUrl.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelUrl.setForeground(new java.awt.Color(13, 155, 219));
        labelUrl.setText("Sitio web:");

        labelUser.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelUser.setForeground(new java.awt.Color(13, 155, 219));
        labelUser.setText("Usuario:");

        labelPass.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelPass.setForeground(new java.awt.Color(13, 155, 219));
        labelPass.setText("Contraseña:");

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtMail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtMail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtUrl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtUser.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtUser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtPass.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPass.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Cancel32.png"))); // NOI18N
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtCuit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            txtCuit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCuit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCuit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanelDatesLayout = new javax.swing.GroupLayout(jPanelDates);
        jPanelDates.setLayout(jPanelDatesLayout);
        jPanelDatesLayout.setHorizontalGroup(
            jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelMail)
                    .addComponent(labelTel)
                    .addComponent(labelCuit)
                    .addComponent(labelName)
                    .addComponent(labelUrl)
                    .addComponent(labelUser)
                    .addComponent(labelPass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatesLayout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                    .addComponent(txtPass)
                    .addComponent(txtUser)
                    .addComponent(txtUrl)
                    .addComponent(txtName)
                    .addComponent(txtTel)
                    .addComponent(txtMail)
                    .addComponent(txtCuit))
                .addContainerGap())
        );
        jPanelDatesLayout.setVerticalGroup(
            jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCuit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCuit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTel)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelMail))
                .addGap(12, 12, 12)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUrl)
                    .addComponent(txtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUser)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPass)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnConfirm)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        tableSupplier.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        tableSupplier.setForeground(new java.awt.Color(102, 102, 102));
        tableSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableSupplier.setRowHeight(25);
        tableSupplier.setSelectionBackground(new java.awt.Color(224, 242, 255));
        jScrollPane1.setViewportView(tableSupplier);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        labelTitle1.setBackground(new java.awt.Color(255, 255, 255));
        labelTitle1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        labelTitle1.setForeground(new java.awt.Color(101, 129, 171));
        labelTitle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle1.setText("Registrar nuevo proveedor.");
        labelTitle1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(101, 129, 171));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit2_64.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jPanelDates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelDates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
   
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setBackground(new Color(238,238,238));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnExitMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        if (btnCancel.isEnabled()) {
            btnCancel.setBackground(new Color(238,238,238));
        } else {
            btnCancel.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnConfirmMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmMouseEntered
        if (btnConfirm.isEnabled()) {
            btnConfirm.setBackground(new Color(238,238,238));
        } else {
            btnConfirm.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnConfirmMouseEntered

    private void btnConfirmMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmMouseExited
        btnConfirm.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnConfirmMouseExited


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmSupplierNew dialog = new frmSupplierNew(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnExit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelDates;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCuit;
    private javax.swing.JLabel labelMail;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPass;
    private javax.swing.JLabel labelTel;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JLabel labelUrl;
    private javax.swing.JLabel labelUser;
    private javax.swing.JTable tableSupplier;
    private javax.swing.JFormattedTextField txtCuit;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtUrl;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
