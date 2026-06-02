/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.purchaseInvoiceDAO;
import java.awt.Color;
import javax.swing.JOptionPane;

public class purchaseInvoiceListDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(purchaseInvoiceListDialog.class.getName());

    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO();
    
    int idPurchaseInvoice = -1;
    int action = 0;
    
    public static final int MODE_PENDING = 1;
    public static final int MODE_ALL = 2;
    
    public int getIdPurchase(){
        return idPurchaseInvoice;
    }


    public purchaseInvoiceListDialog(java.awt.Frame parent, boolean modal, int action) {
        super(parent, modal);
        
        this.action = action;
        
        initComponents();
        
        if(action == 1){
            qPurchase.listPurchaseInvoice(tablePurchaseInvoice);
        }else if(action == 2){
            qPurchase.listAllPurchase(tablePurchaseInvoice);
        }
        
        
        btnSelectPurchase.addActionListener(e->{
            selectPurchase();
            this.dispose();
        });
        
    }
    
    private void selectPurchase(){

        int filaVista = tablePurchaseInvoice.getSelectedRow();

        if (filaVista == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una factura");
            return;
        }

        int filaModelo = tablePurchaseInvoice.convertRowIndexToModel(filaVista);

        idPurchaseInvoice = Integer.parseInt(
            tablePurchaseInvoice.getModel().getValueAt(filaModelo, 0).toString()
        );
    } 


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePurchaseInvoice = new javax.swing.JTable();
        btnCancel = new javax.swing.JButton();
        btnSelectPurchase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/factura32.png"))); // NOI18N
        jLabel1.setText("Facturas cargadas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(557, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(6, 6, 6))
        );

        tablePurchaseInvoice.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tablePurchaseInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablePurchaseInvoice.setFillsViewportHeight(true);
        tablePurchaseInvoice.setRowHeight(30);
        jScrollPane1.setViewportView(tablePurchaseInvoice);

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.setFocusPainted(false);
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnSelectPurchase.setBackground(new java.awt.Color(255, 255, 255));
        btnSelectPurchase.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelectPurchase.setForeground(new java.awt.Color(101, 129, 171));
        btnSelectPurchase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelectPurchase.setText("Seleccionar");
        btnSelectPurchase.setBorder(null);
        btnSelectPurchase.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSelectPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSelectPurchaseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSelectPurchaseMouseExited(evt);
            }
        });
        btnSelectPurchase.addActionListener(this::btnSelectPurchaseActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSelectPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSelectPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectPurchaseMouseEntered
        btnSelectPurchase.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnSelectPurchaseMouseEntered

    private void btnSelectPurchaseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectPurchaseMouseExited
        btnSelectPurchase.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSelectPurchaseMouseExited

    private void btnSelectPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectPurchaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSelectPurchaseActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                purchaseInvoiceListDialog dialog = new purchaseInvoiceListDialog(new javax.swing.JFrame(), true, MODE_PENDING);
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
    private javax.swing.JButton btnSelectPurchase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePurchaseInvoice;
    // End of variables declaration//GEN-END:variables
}
