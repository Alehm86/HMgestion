/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.customerDAO;
import javax.swing.JOptionPane;

public class customerDevicesDialog extends javax.swing.JDialog {

    customerDAO qCustomer = new customerDAO(); 
    
    String serialNumberDevice = "";

    public void setIdCustomer(int id_customer){
        qCustomer.nameCustomer(id_customer, lbl_titulo);
        qCustomer.listCustomerDevices(tableDevice, id_customer);     
    }
    
    public String getSerialNumberDevice(){
        return serialNumberDevice;
    }
    
    public customerDevicesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
          
        btnSelect.addActionListener(e->{
            
            selectDevice();
            this.dispose();
        });
        
    }
    
    private void selectDevice(){

        int fila = tableDevice.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un dispositivo");
            return;
        }
        serialNumberDevice = tableDevice.getValueAt(fila, 3).toString();
    }  
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDevice = new javax.swing.JTable();
        btnSelect = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Historial ");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tableDevice.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableDevice.setForeground(new java.awt.Color(65, 65, 63));
        tableDevice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableDevice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableDevice.setFillsViewportHeight(true);
        tableDevice.setRowHeight(30);
        tableDevice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDeviceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableDevice);

        btnSelect.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelect.setForeground(new java.awt.Color(12, 83, 151));
        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelect.setText("Seleccionar");
        btnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSelect)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lbl_titulo.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(12, 83, 151));
        lbl_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/historial-de-Dispositivos64.png"))); // NOI18N
        lbl_titulo.setText("Historial de dispositivos ingresados.");

        jPanelSeparador4.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 40, Short.MAX_VALUE))
                    .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tableDeviceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDeviceMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = tableDevice.getSelectedRow();

            if (fila != -1) {
                serialNumberDevice = tableDevice.getValueAt(fila, 3).toString();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_tableDeviceMouseClicked

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSelectActionPerformed


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                customerDevicesDialog dialog = new customerDevicesDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnSelect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JTable tableDevice;
    // End of variables declaration//GEN-END:variables
}
