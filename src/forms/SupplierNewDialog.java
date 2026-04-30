/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import classDAO.productDAO;
import classDAO.genericDAO;
import classDAO.supplierDAO;
import models.modelSupplier;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;


public class SupplierNewDialog extends javax.swing.JDialog {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    supplierDAO qSuppliers = new supplierDAO();
    
    modelSupplier mSupplier = new modelSupplier();
    
    private String SupplierSelected = "";
    private String proveedorCreado;
    
    public String getProveedorCreado() {       
        return proveedorCreado;
    }
    
    public SupplierNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        cboIVA.addItem("Seleccione una opción");
        cboIVA.addItem("Responsable inscripto");
        cboIVA.addItem("Monotributista");

        qSuppliers.listTableSupplier(tableSupplier);
        actions();
        leyendaBotones();
    }
    
    private void leyendaBotones(){
        
        btnOut.setToolTipText("Salir");
        btnCancel.setToolTipText("Borrar");
    }
    
    void actions(){
        
        btnCancel.addActionListener(e -> {
            this.dispose();
        });
        
        btnCancel.addActionListener(e -> {
            clearFields();
            tableSupplier.setEnabled(true);
        });     
        
        btnConfirm.addActionListener(e -> {
            nuevoProveedor();
        });
        
        txtCuit.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = txtCuit.getText();

                if (!texto.isEmpty() && texto.length() < 8) {
                    JOptionPane.showMessageDialog(
                        null,
                        "El DNI / CUIT debe tener al menos 8 dígitos",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
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
        cboIVA.setSelectedIndex(0);
    }
       
    void nuevoProveedor(){
        
        Boolean valido = true;
        String cuit = cboIVA.getSelectedItem().toString();
   
        if(!txtName.getText().isEmpty()){         
            mSupplier.name=txtName.getText().toUpperCase();        
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe ingresar un nombre de proveedor!");           
            txtName.requestFocusInWindow();
            valido = false;       
        }
        
        if(cboIVA.getSelectedIndex() > 0){
            mSupplier.condicion = cuit;
        }else{
            mSupplier.condicion=null;
        }        
        
        if(!txtCuit.getText().isEmpty()){
            mSupplier.cuit=txtCuit.getText();
        }else{
            mSupplier.cuit=null;
        }
        
        if(!txtTel.getText().isEmpty()){
            mSupplier.telefono = txtTel.getText();
        }else{
            mSupplier.telefono=null;
        }
        
        if(!txtMail.getText().isEmpty()){
            mSupplier.email=txtMail.getText();
        }else{
            mSupplier.email=null;
        }
        
        if(!txtUrl.getText().isEmpty()){
            mSupplier.web=txtUrl.getText();
        }else{
            mSupplier.web=null;
        }
        
        if(!txtUser.getText().isEmpty()){
            mSupplier.user=txtUser.getText();
        }else{
            mSupplier.user=null;
        }
        
        if(!txtPass.getText().isEmpty()){
            mSupplier.pass=txtPass.getText();
        }else{
            mSupplier.pass="Sin datos";
        }
        
        if(!valido){
            return;
        } 
        
        if (!qGeneric.nameExists(txtName.getText(),"suppliers")) {
            
            qSuppliers.insertSupplier(
                mSupplier.getName(),
                mSupplier.getCondicion(),        
                mSupplier.getCuit(),
                mSupplier.getTelefono(),
                mSupplier.getEmail(),
                mSupplier.getWeb(),
                mSupplier.getUser(),
                mSupplier.getPass());   
            
            proveedorCreado = mSupplier.getName();
            
            qSuppliers.listTableSupplier(tableSupplier);  
                                                      
        }else{
            JOptionPane.showMessageDialog(null, "El nombre del proveedor ya existe!..");
        }
            
        proveedorCreado = mSupplier.getName();
        qSuppliers.listTableSupplier(tableSupplier);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelDates = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
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
        txtCuit = new javax.swing.JTextField();
        labelCuit1 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSupplier = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        labelTitle1 = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnOut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Gestion de proveedores");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanelDates.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDates.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setForeground(new java.awt.Color(65, 65, 63));
        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtName.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelName.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelName.setForeground(new java.awt.Color(12, 83, 151));
        labelName.setText("Nombre:");

        labelCuit.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelCuit.setForeground(new java.awt.Color(12, 83, 151));
        labelCuit.setText("C.U.I.T.");

        labelTel.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelTel.setForeground(new java.awt.Color(12, 83, 151));
        labelTel.setText("Teléfono:");

        labelMail.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelMail.setForeground(new java.awt.Color(12, 83, 151));
        labelMail.setText("Correo electrónico:");

        labelUrl.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelUrl.setForeground(new java.awt.Color(12, 83, 151));
        labelUrl.setText("Sitio web:");

        labelUser.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelUser.setForeground(new java.awt.Color(12, 83, 151));
        labelUser.setText("Usuario:");

        labelPass.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelPass.setForeground(new java.awt.Color(12, 83, 151));
        labelPass.setText("Contraseña:");

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setForeground(new java.awt.Color(65, 65, 63));
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtMail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtMail.setForeground(new java.awt.Color(65, 65, 63));
        txtMail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtUrl.setForeground(new java.awt.Color(65, 65, 63));
        txtUrl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUrl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtUser.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtUser.setForeground(new java.awt.Color(65, 65, 63));
        txtUser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtPass.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPass.setForeground(new java.awt.Color(65, 65, 63));
        txtPass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPass.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCuit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCuit.setForeground(new java.awt.Color(65, 65, 63));
        txtCuit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCuit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCuit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCuitKeyPressed(evt);
            }
        });

        labelCuit1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelCuit1.setForeground(new java.awt.Color(12, 83, 151));
        labelCuit1.setText("Condición:");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(65, 65, 63));
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
                    .addComponent(labelPass)
                    .addComponent(labelCuit1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .addComponent(txtUser)
                    .addComponent(txtUrl)
                    .addComponent(txtName)
                    .addComponent(txtTel)
                    .addComponent(txtMail)
                    .addComponent(txtCuit, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboIVA, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelDatesLayout.setVerticalGroup(
            jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName))
                .addGap(15, 15, 15)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboIVA, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(labelCuit1))
                .addGap(18, 18, 18)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCuit)
                    .addComponent(txtCuit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tableSupplier.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        tableSupplier.setForeground(new java.awt.Color(65, 65, 63));
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
        tableSupplier.setColumnSelectionAllowed(true);
        tableSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tableSupplier.setFillsViewportHeight(true);
        tableSupplier.setRowHeight(25);
        jScrollPane1.setViewportView(tableSupplier);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(101, 129, 171));

        labelTitle1.setBackground(new java.awt.Color(255, 255, 255));
        labelTitle1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        labelTitle1.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelTitle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registro64.png"))); // NOI18N
        labelTitle1.setText("Registrar nuevo proveedor.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 82, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnConfirm.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(101, 129, 171));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Registrar");
        btnConfirm.setBorder(null);
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

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
        btnCancel.setBorder(null);
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

        btnOut.setBackground(new java.awt.Color(255, 255, 255));
        btnOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir32.png"))); // NOI18N
        btnOut.setBorder(null);
        btnOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOutMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jPanelDates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnOut, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelDates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnOut, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
   
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        if (btnCancel.isEnabled()) {
            btnCancel.setBackground(new Color(255,127,39));
        } else {
            btnCancel.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnConfirmMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmMouseEntered
        if (btnConfirm.isEnabled()) {
            btnConfirm.setBackground(new Color(255,215,0));
        } else {
            btnConfirm.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnConfirmMouseEntered

    private void btnConfirmMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmMouseExited
        btnConfirm.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnConfirmMouseExited

    private void txtCuitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCuitKeyPressed
        txtCuit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCuit.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCuitKeyPressed

    private void btnOutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutMouseEntered
        btnOut.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnOutMouseEntered

    private void btnOutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutMouseExited
        btnOut.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnOutMouseExited


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SupplierNewDialog dialog = new SupplierNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnOut;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelDates;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCuit;
    private javax.swing.JLabel labelCuit1;
    private javax.swing.JLabel labelMail;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPass;
    private javax.swing.JLabel labelTel;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JLabel labelUrl;
    private javax.swing.JLabel labelUser;
    private javax.swing.JTable tableSupplier;
    private javax.swing.JTextField txtCuit;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtUrl;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
