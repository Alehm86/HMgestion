/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Class.ProductDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class PageClientHome extends javax.swing.JPanel {
    
    ProductDAO queries = new ProductDAO();
    private String filaSeleccionada = "";
    //⚠️
    //✅
    public PageClientHome() {

        initComponents();        
        actionButtons();
        start();
    }
    
    private void start(){
//AQUI VA LA PAGINA PRINCIPAL DE EL MODULO
    }


    


    private void actionButtons(){
        
        BtnClientHome.addActionListener(e->{
            start();
        });
        
        btnNewClient.addActionListener(e->{
            PageAltaCliente pNewClient = new PageAltaCliente();
            pNewClient.setSize(1700, 877);
            pNewClient.setLocation(0, 0);

            jPanelClientContent.removeAll();
            jPanelClientContent.add(pNewClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelClientContent.revalidate();
            jPanelClientContent.repaint();
        });
            
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        btnNewClient = new javax.swing.JButton();
        btnAddStock = new javax.swing.JButton();
        BtnClientHome = new javax.swing.JButton();
        btnEditClient = new javax.swing.JButton();
        jPanelClientContent = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1253, 877));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnNewClient.setBackground(new java.awt.Color(255,255,255));
        btnNewClient.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNewClient.setForeground(new java.awt.Color(12, 83, 151));
        btnNewClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/altaCliente.png"))); // NOI18N
        btnNewClient.setText("Alta cliente");
        btnNewClient.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnNewClient.setBorderPainted(false);
        btnNewClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewClientMouseExited(evt);
            }
        });
        btnNewClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewClientActionPerformed(evt);
            }
        });

        btnAddStock.setBackground(new java.awt.Color(255, 255, 255));
        btnAddStock.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAddStock.setForeground(new java.awt.Color(12, 83, 151));
        btnAddStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eliminarCliente.png"))); // NOI18N
        btnAddStock.setText("Baja");
        btnAddStock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnAddStock.setBorderPainted(false);
        btnAddStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddStock.setFocusable(false);
        btnAddStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddStockMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddStockMouseExited(evt);
            }
        });
        btnAddStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStockActionPerformed(evt);
            }
        });

        BtnClientHome.setBackground(new java.awt.Color(255,255,255));
        BtnClientHome.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        BtnClientHome.setForeground(new java.awt.Color(12, 83, 151));
        BtnClientHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeCliente (2).png"))); // NOI18N
        BtnClientHome.setText("Home");
        BtnClientHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        BtnClientHome.setBorderPainted(false);
        BtnClientHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnClientHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnClientHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnClientHomeMouseExited(evt);
            }
        });
        BtnClientHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnClientHomeActionPerformed(evt);
            }
        });

        btnEditClient.setBackground(new java.awt.Color(255, 255, 255));
        btnEditClient.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEditClient.setForeground(new java.awt.Color(12, 83, 151));
        btnEditClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editClient.png"))); // NOI18N
        btnEditClient.setText("Editar");
        btnEditClient.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnEditClient.setBorderPainted(false);
        btnEditClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditClientMouseExited(evt);
            }
        });
        btnEditClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnClientHome, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditClient, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(BtnClientHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanelClientContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelClientContent.setPreferredSize(new java.awt.Dimension(1600, 800));

        javax.swing.GroupLayout jPanelClientContentLayout = new javax.swing.GroupLayout(jPanelClientContent);
        jPanelClientContent.setLayout(jPanelClientContentLayout);
        jPanelClientContentLayout.setHorizontalGroup(
            jPanelClientContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1247, Short.MAX_VALUE)
        );
        jPanelClientContentLayout.setVerticalGroup(
            jPanelClientContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 797, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelClientContent, javax.swing.GroupLayout.DEFAULT_SIZE, 1247, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelClientContent, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewClientMouseEntered
        btnNewClient.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewClientMouseEntered

    private void btnNewClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewClientMouseExited
        btnNewClient.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewClientMouseExited

    private void btnNewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewClientActionPerformed

    }//GEN-LAST:event_btnNewClientActionPerformed

    private void BtnClientHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnClientHomeMouseEntered
        BtnClientHome.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_BtnClientHomeMouseEntered

    private void BtnClientHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnClientHomeMouseExited
        BtnClientHome.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_BtnClientHomeMouseExited

    private void BtnClientHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnClientHomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnClientHomeActionPerformed

    private void btnAddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStockActionPerformed

    }//GEN-LAST:event_btnAddStockActionPerformed

    private void btnAddStockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseExited
        btnAddStock.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddStockMouseExited

    private void btnAddStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseEntered
        btnAddStock.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnAddStockMouseEntered

    private void btnEditClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditClientActionPerformed

    }//GEN-LAST:event_btnEditClientActionPerformed

    private void btnEditClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditClientMouseExited
        btnEditClient.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditClientMouseExited

    private void btnEditClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditClientMouseEntered
        btnEditClient.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditClientMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnClientHome;
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnEditClient;
    private javax.swing.JButton btnNewClient;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanelClientContent;
    // End of variables declaration//GEN-END:variables
}
