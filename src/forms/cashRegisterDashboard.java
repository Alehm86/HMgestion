/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class cashRegisterDashboard extends javax.swing.JPanel {

    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    public cashRegisterDashboard() {
        initComponents();
        
        btnSales.addActionListener(e->{
            salesPanel pSales = new salesPanel();
            pSales.setSize(1750, 800);
            pSales.setLocation(0, 0);

            panelContent.removeAll();
            panelContent.add(pSales, BorderLayout.CENTER);
            panelContent.revalidate();
            panelContent.repaint();
        });
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        btnSales = new javax.swing.JButton();
        btnBook = new javax.swing.JButton();
        panelContent = new javax.swing.JPanel();

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnSales.setBackground(new java.awt.Color(255,255,255));
        btnSales.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSales.setForeground(new java.awt.Color(12, 83, 151));
        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/sales32.png"))); // NOI18N
        btnSales.setText("Ventas");
        btnSales.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnSales.setBorderPainted(false);
        btnSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalesMouseExited(evt);
            }
        });
        btnSales.addActionListener(this::btnSalesActionPerformed);

        btnBook.setBackground(new java.awt.Color(255, 255, 255));
        btnBook.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBook.setForeground(new java.awt.Color(12, 83, 151));
        btnBook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/libroDiario32.png"))); // NOI18N
        btnBook.setText("Libro diario");
        btnBook.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnBook.setBorderPainted(false);
        btnBook.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBook.setFocusable(false);
        btnBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBookMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBookMouseExited(evt);
            }
        });
        btnBook.addActionListener(this::btnBookActionPerformed);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(890, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBook, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelContent.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelContentLayout = new javax.swing.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 784, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseEntered
        btnSales.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnSalesMouseEntered

    private void btnSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseExited
        btnSales.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSalesMouseExited

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed

    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnBookMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookMouseEntered
        btnBook.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBookMouseEntered

    private void btnBookMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookMouseExited
        btnBook.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBookMouseExited

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed

    }//GEN-LAST:event_btnBookActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnSales;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel panelContent;
    // End of variables declaration//GEN-END:variables
}
