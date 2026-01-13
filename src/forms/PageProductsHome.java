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

public class PageProductsHome extends javax.swing.JPanel {
    
    ProductDAO queries = new ProductDAO();
    private String filaSeleccionada = "";
    //⚠️
    //✅
    public PageProductsHome() {

        initComponents();        
        actionButtons();
        start();
    }
    
    private void start(){
        PageProduct p1 = new PageProduct();
        p1.setSize(1700, 800);
        p1.setLocation(0, 0);
        
        jPanelProductContent.removeAll();
        jPanelProductContent.add(p1, BorderLayout.CENTER);
        jPanelProductContent.revalidate();
        jPanelProductContent.repaint();
    }


    


    private void actionButtons(){
        
        BtnProdHome.addActionListener(e->{
            start();
        });
        
//        btnRerturnProduct.addActionListener(e-> {
//            frmReturnProducts returnProducts = new frmReturnProducts();
//            returnProducts.setVisible(true);
////            this.dispose();
//        });       
//        
//        btnAddStock.addActionListener(e -> {
//            frmStockManagement stockMgmt = new frmStockManagement();
//            stockMgmt.setVisible(true);
////            this.dispose();
//        });          
        
        btnNewProduct.addActionListener(e->{
//            frmNewProduct edit = new frmNewProduct();
//            edit.setVisible(true);
//            this.dispose();
            PageProductNew1 p2 = new PageProductNew1();
            p2.setSize(1700, 800);
            p2.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(p2, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint();            

        });
        
//        btnEdit.addActionListener(e->{
//            int id = queries.obtenerIdProduct(filaSeleccionada);
//
//            frmEditProduct edit = new frmEditProduct();
//            edit.dialogoEdit(id);
//            edit.setVisible(true);
////            this.dispose();  
//        });
//        
//        btnEditPrice.addActionListener(e->{
//            int id = queries.obtenerIdProduct(filaSeleccionada);
//            
//            frmEditPrice editPrice = new frmEditPrice();
//            editPrice.dialogoEdit(id);
//            editPrice.setVisible(true);
//        });
//        
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        btnNewProduct = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnEditPrice = new javax.swing.JButton();
        btnRerturnProduct = new javax.swing.JButton();
        btnAddStock = new javax.swing.JButton();
        BtnProdHome = new javax.swing.JButton();
        jPanelProductContent = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1253, 877));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnNewProduct.setBackground(new java.awt.Color(255,255,255));
        btnNewProduct.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNewProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductAdd.png"))); // NOI18N
        btnNewProduct.setText("Nuevo");
        btnNewProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnNewProduct.setBorderPainted(false);
        btnNewProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewProductMouseExited(evt);
            }
        });
        btnNewProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewProductActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(12, 83, 151));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductEdit.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnEdit.setBorderPainted(false);
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMouseExited(evt);
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnEditPrice.setBackground(new java.awt.Color(255, 255, 255));
        btnEditPrice.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEditPrice.setForeground(new java.awt.Color(12, 83, 151));
        btnEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductPrice.png"))); // NOI18N
        btnEditPrice.setText("Precio");
        btnEditPrice.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnEditPrice.setBorderPainted(false);
        btnEditPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditPrice.setFocusable(false);
        btnEditPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditPriceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditPriceMouseExited(evt);
            }
        });
        btnEditPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPriceActionPerformed(evt);
            }
        });

        btnRerturnProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnRerturnProduct.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRerturnProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnRerturnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductReturn.png"))); // NOI18N
        btnRerturnProduct.setText("Devolución");
        btnRerturnProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnRerturnProduct.setBorderPainted(false);
        btnRerturnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRerturnProduct.setFocusable(false);
        btnRerturnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRerturnProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRerturnProductMouseExited(evt);
            }
        });
        btnRerturnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRerturnProductActionPerformed(evt);
            }
        });

        btnAddStock.setBackground(new java.awt.Color(255, 255, 255));
        btnAddStock.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAddStock.setForeground(new java.awt.Color(12, 83, 151));
        btnAddStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductStock.png"))); // NOI18N
        btnAddStock.setText("Stock");
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

        BtnProdHome.setBackground(new java.awt.Color(255,255,255));
        BtnProdHome.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        BtnProdHome.setForeground(new java.awt.Color(12, 83, 151));
        BtnProdHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductHome.png"))); // NOI18N
        BtnProdHome.setText("Productos");
        BtnProdHome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        BtnProdHome.setBorderPainted(false);
        BtnProdHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnProdHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnProdHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnProdHomeMouseExited(evt);
            }
        });
        BtnProdHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProdHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnProdHome, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(565, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnProdHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanelProductContent.setPreferredSize(new java.awt.Dimension(1600, 800));

        javax.swing.GroupLayout jPanelProductContentLayout = new javax.swing.GroupLayout(jPanelProductContent);
        jPanelProductContent.setLayout(jPanelProductContentLayout);
        jPanelProductContentLayout.setHorizontalGroup(
            jPanelProductContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelProductContentLayout.setVerticalGroup(
            jPanelProductContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 797, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelProductContent, javax.swing.GroupLayout.DEFAULT_SIZE, 1471, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProductContent, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseEntered
        btnNewProduct.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewProductMouseEntered

    private void btnNewProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseExited
        btnNewProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewProductMouseExited

    private void btnNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewProductActionPerformed

    }//GEN-LAST:event_btnNewProductActionPerformed

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        btnEdit.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEditPriceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseEntered
        btnEditPrice.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditPriceMouseEntered

    private void btnEditPriceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseExited
        btnEditPrice.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditPriceMouseExited

    private void btnEditPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPriceActionPerformed

    }//GEN-LAST:event_btnEditPriceActionPerformed

    private void btnRerturnProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRerturnProductMouseEntered
        btnRerturnProduct.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnRerturnProductMouseEntered

    private void btnRerturnProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRerturnProductMouseExited
        btnRerturnProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRerturnProductMouseExited

    private void btnRerturnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRerturnProductActionPerformed

    }//GEN-LAST:event_btnRerturnProductActionPerformed

    private void btnAddStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseEntered
        btnAddStock.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnAddStockMouseEntered

    private void btnAddStockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseExited
        btnAddStock.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddStockMouseExited

    private void btnAddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStockActionPerformed

    }//GEN-LAST:event_btnAddStockActionPerformed

    private void BtnProdHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnProdHomeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnProdHomeMouseEntered

    private void BtnProdHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnProdHomeMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnProdHomeMouseExited

    private void BtnProdHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnProdHomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnProdHomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnProdHome;
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditPrice;
    private javax.swing.JButton btnNewProduct;
    private javax.swing.JButton btnRerturnProduct;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanelProductContent;
    // End of variables declaration//GEN-END:variables
}
