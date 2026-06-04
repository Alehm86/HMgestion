/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import java.awt.Color;
import javax.swing.JOptionPane;

public class productCategoriesNewDialog extends javax.swing.JDialog {
    
    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    public String categoriaCreada;
    public int state = 1;


    public String getCategoriaCreada() {
        return categoriaCreada;
    }
    
    public productCategoriesNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();  
        
        checkState.setSelected(true);
        actionButtons();        
    }
  
    void newCategory(){
        
        if(!qGeneric.nameExists(txtName.getText(), "categories")){
            
            categoriaCreada = txtName.getText().toUpperCase();
            qProduct.insertCategory(txtName.getText().toUpperCase(), state);
            
            JOptionPane.showMessageDialog(null, "Categoria creada");
            
            int msjNuevo = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas crear otra categoria?",
                "Confirmar registro",
                JOptionPane.YES_NO_OPTION
            );

            if (msjNuevo == JOptionPane.YES_OPTION) {
                txtName.setText("");
                checkState.setSelected(false);
            }else{
                this.dispose();
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "El nombre ya existe!");
        }  
    }
 
    void actionButtons(){
        
        checkState.addActionListener(e -> {
            
            boolean activo = checkState.isSelected();

            if (activo) {
                state = 1;
            } else {
                state = 0;
            }
        });
       
        btnRegistrar.addActionListener(e -> {
            
            if(!txtName.getText().isEmpty()){       
                newCategory();     
            }else{
                JOptionPane.showMessageDialog(null, "¡Ingrese un nombre!..");
                txtName.requestFocusInWindow();
            }       
        });
        
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labelTitleNameNew = new javax.swing.JLabel();
        labelState = new javax.swing.JLabel();
        checkState = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Crear categoria");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registro64.png"))); // NOI18N
        jLabel1.setText("Crear categorías.");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labelTitleNameNew.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelTitleNameNew.setForeground(new java.awt.Color(12, 83, 151));
        labelTitleNameNew.setText("Nombre:");

        labelState.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelState.setForeground(new java.awt.Color(12, 83, 151));
        labelState.setText("Estado:");

        checkState.setBackground(new java.awt.Color(255, 255, 255));
        checkState.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        checkState.setForeground(new java.awt.Color(0, 153, 51));
        checkState.setText("Activa");
        checkState.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        checkState.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkState.setFocusPainted(false);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelState)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkState, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelTitleNameNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelTitleNameNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(checkState, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelState, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                productCategoriesNewDialog dialog = new productCategoriesNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JCheckBox checkState;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel labelState;
    private javax.swing.JLabel labelTitleNameNew;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
