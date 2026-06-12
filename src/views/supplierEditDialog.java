/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.productDAO;
import dao.supplierDAO;
import models.Supplier;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JOptionPane;


public class supplierEditDialog extends javax.swing.JDialog {

    productDAO qProduct = new productDAO();
    supplierDAO qSuppliers = new supplierDAO();
    
    Supplier mSupplier = new Supplier();
    
    private String SupplierSelected = "";
    private String proveedorEditado;
    
    public String getProveedorEditado() {       
        return proveedorEditado;
    }
    
    public supplierEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
 
        txtCondicion.setVisible(false);
        cboIVA.addItem("Seleccione una opción");
        cboIVA.addItem("Responsable inscripto");
        cboIVA.addItem("Monotributista");
        
        qSuppliers.listTableSupplier(tableSupplier);
        actions();
    }
    
    private void infoComboIva(){
        
        String iva = txtCondicion.getText();

        for (int i = 0; i < cboIVA.getItemCount(); i++) {
            if (cboIVA.getItemAt(i).toString().equalsIgnoreCase(iva)) {
                cboIVA.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void leyendaBotones(){
        
        btnCancel.setToolTipText("Salir");
    }
    
    private void actions(){
        
        tableSupplier.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    SupplierSelected = String.valueOf(tableSupplier.getValueAt(tableSupplier.getSelectedRow(), 0).toString());                  
             
                    txtName.setText("");
                    txtCuit.setText("");
                    txtTel.setText("");
                    txtMail.setText("");
                    txtUrl.setText("");
                    txtUser.setText("");
                    txtPass.setText("");
                    cboIVA.setSelectedIndex(0);
                    
                    qSuppliers.selectSupplier(SupplierSelected, txtName, txtCondicion, txtCuit, txtTel, txtMail, txtUrl, txtUser, txtPass);
                    infoComboIva();
                }
                else{
                }            
            }
        });
        
        btnCancel.addActionListener(e -> {
            this.dispose();
        });
        
        btnConfirm.addActionListener(e -> {
            editarProveedor();
            tableSupplier.setEnabled(true);           
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
  
    private void editarProveedor() {
        
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡Debe ingresar un nombre de proveedor!");
            txtName.requestFocusInWindow();
            return;
        }

        mSupplier.name = txtName.getText().trim().toUpperCase();       
        if(cboIVA.getSelectedIndex() > 0){
            mSupplier.condicion = cboIVA.getSelectedItem().toString();
        }else{
            mSupplier.condicion = null;
        }       
        mSupplier.cuit = txtCuit.getText().trim();
        mSupplier.telefono = txtTel.getText().trim();
        mSupplier.email = txtMail.getText().trim();
        mSupplier.web = txtUrl.getText().trim();
        mSupplier.user = txtUser.getText().trim();
        mSupplier.pass = txtPass.getText().trim();

        qSuppliers.updateSupplier(
                SupplierSelected,
                mSupplier.getName(),
                mSupplier.getCondicion(),        
                mSupplier.getCuit(),
                mSupplier.getTelefono(),
                mSupplier.getEmail(),
                mSupplier.getWeb(),
                mSupplier.getUser(),
                mSupplier.getPass()); 
            
        proveedorEditado = mSupplier.getName();
        qSuppliers.listTableSupplier(tableSupplier);                  
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelDates = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        labelCuit = new javax.swing.JLabel();
        labelTel = new javax.swing.JLabel();
        labelMail = new javax.swing.JLabel();
        labelUrl = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        labelPass = new javax.swing.JLabel();
        labelCuit1 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        txtCondicion = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtCuit = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        txtMail = new javax.swing.JTextField();
        txtUrl = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSupplier = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        labelTitle1 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Gestion de proveedores");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanelDates.setBackground(new java.awt.Color(255, 255, 255));

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

        labelCuit1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelCuit1.setForeground(new java.awt.Color(12, 83, 151));
        labelCuit1.setText("Condición:");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(35, 35, 38));
        cboIVA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtCondicion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCondicion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCondicion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCondicion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCondicionKeyPressed(evt);
            }
        });

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setForeground(new java.awt.Color(35, 35, 38));

        txtCuit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCuit.setForeground(new java.awt.Color(35, 35, 38));
        txtCuit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCuitKeyTyped(evt);
            }
        });

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setForeground(new java.awt.Color(35, 35, 38));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });

        txtMail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtMail.setForeground(new java.awt.Color(35, 35, 38));

        txtUrl.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtUrl.setForeground(new java.awt.Color(35, 35, 38));

        txtUser.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtUser.setForeground(new java.awt.Color(35, 35, 38));

        txtPass.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPass.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanelDatesLayout = new javax.swing.GroupLayout(jPanelDates);
        jPanelDates.setLayout(jPanelDatesLayout);
        jPanelDatesLayout.setHorizontalGroup(
            jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelTel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelCuit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelCuit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelUrl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboIVA, 0, 227, Short.MAX_VALUE)
                            .addComponent(txtName)))
                    .addGroup(jPanelDatesLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPass, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtUser, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtUrl, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMail)
                            .addComponent(txtCuit)))
                    .addGroup(jPanelDatesLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(txtTel)))
                .addContainerGap())
        );
        jPanelDatesLayout.setVerticalGroup(
            jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCuit1)
                    .addComponent(txtCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCuit)
                    .addComponent(txtCuit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTel)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMail)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
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
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tableSupplier.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableSupplier.setForeground(new java.awt.Color(35, 35, 38));
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
        tableSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableSupplier.setFillsViewportHeight(true);
        tableSupplier.setRowHeight(30);
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(245, 248, 255));

        labelTitle1.setBackground(new java.awt.Color(101, 129, 171));
        labelTitle1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        labelTitle1.setForeground(new java.awt.Color(12, 83, 151));
        labelTitle1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelTitle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registrarse64.png"))); // NOI18N
        labelTitle1.setText("Editar datos de proveedor.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnCancel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(12, 83, 151));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Editar");
        btnConfirm.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelDates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelDates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCondicionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCondicionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCondicionKeyPressed

    private void txtCuitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCuitKeyTyped
        txtCuit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCuit.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCuitKeyTyped

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        txtCuit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCuit.getText().length() >= 13) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtTelKeyTyped


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                supplierEditDialog dialog = new supplierEditDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField txtCondicion;
    private javax.swing.JTextField txtCuit;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtUrl;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
