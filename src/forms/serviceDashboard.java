/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import java.awt.BorderLayout;
import java.awt.Color;

public class serviceDashboard extends javax.swing.JPanel {

    public serviceDashboard() {
        initComponents();
        actions();
        listar();
    }

    private void actions(){
        
        btnListar.addActionListener(e->{
            listar();
        });
        
        btnNewService.addActionListener(e->{

            serviceNewPanel pNewService = new serviceNewPanel();
            pNewService.setSize(1700, 800);
            pNewService.setLocation(0, 0);

            jPanelDashboard.removeAll();
            jPanelDashboard.add(pNewService, BorderLayout.CENTER);
            jPanelDashboard.revalidate();
            jPanelDashboard.repaint();
        });
        
        btnPresupuesto.addActionListener(e->{
            budgetDialog fPresupuesto = new budgetDialog(null, true);
            fPresupuesto.setLocationRelativeTo(null);
            fPresupuesto.setVisible(true);
        });
        
    }
    
    private void listar(){
        
        serviceListPanel pList = new serviceListPanel();
        pList.setSize(1700, 800);
        pList.setLocation(0, 0);
        
        jPanelDashboard.removeAll();
        jPanelDashboard.add(pList, BorderLayout.CENTER);
        jPanelDashboard.revalidate();
        jPanelDashboard.repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        btnNewService = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        btnPresupuesto = new javax.swing.JButton();
        jPanelDashboard = new javax.swing.JPanel();

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnNewService.setBackground(new java.awt.Color(255,255,255));
        btnNewService.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNewService.setForeground(new java.awt.Color(12, 83, 151));
        btnNewService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/serviceOrder32.png"))); // NOI18N
        btnNewService.setText(" Nuevo servicio");
        btnNewService.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnNewService.setBorderPainted(false);
        btnNewService.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewService.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewServiceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewServiceMouseExited(evt);
            }
        });
        btnNewService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewServiceActionPerformed(evt);
            }
        });

        btnListar.setBackground(new java.awt.Color(255,255,255));
        btnListar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnListar.setForeground(new java.awt.Color(12, 83, 151));
        btnListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Service32.png"))); // NOI18N
        btnListar.setText("Servicios técnicos");
        btnListar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnListar.setBorderPainted(false);
        btnListar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnListar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnListarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnListarMouseExited(evt);
            }
        });
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });

        btnPresupuesto.setBackground(new java.awt.Color(255, 255, 255));
        btnPresupuesto.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnPresupuesto.setForeground(new java.awt.Color(12, 83, 151));
        btnPresupuesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/BudgetNew32.png"))); // NOI18N
        btnPresupuesto.setText("Presupuesto");
        btnPresupuesto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnPresupuesto.setBorderPainted(false);
        btnPresupuesto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPresupuesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPresupuestoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPresupuestoMouseExited(evt);
            }
        });
        btnPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPresupuestoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewService, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnListar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewService, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanelDashboard.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanelDashboardLayout = new javax.swing.GroupLayout(jPanelDashboard);
        jPanelDashboard.setLayout(jPanelDashboardLayout);
        jPanelDashboardLayout.setHorizontalGroup(
            jPanelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelDashboardLayout.setVerticalGroup(
            jPanelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 764, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewServiceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewServiceMouseEntered
        btnNewService.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewServiceMouseEntered

    private void btnNewServiceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewServiceMouseExited
        btnNewService.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewServiceMouseExited

    private void btnNewServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewServiceActionPerformed

    }//GEN-LAST:event_btnNewServiceActionPerformed

    private void btnListarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnListarMouseEntered
        btnListar.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnListarMouseEntered

    private void btnListarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnListarMouseExited
        btnListar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnListarMouseExited

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnListarActionPerformed

    private void btnPresupuestoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPresupuestoMouseEntered
        btnPresupuesto.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnPresupuestoMouseEntered

    private void btnPresupuestoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPresupuestoMouseExited
        btnPresupuesto.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnPresupuestoMouseExited

    private void btnPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPresupuestoActionPerformed

    }//GEN-LAST:event_btnPresupuestoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnListar;
    private javax.swing.JButton btnNewService;
    private javax.swing.JButton btnPresupuesto;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanelDashboard;
    // End of variables declaration//GEN-END:variables
}
