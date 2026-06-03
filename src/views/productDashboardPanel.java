/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.productDAO;
import java.awt.BorderLayout;
import java.awt.Color;

public class productDashboardPanel extends javax.swing.JPanel {
    
    productDAO queries = new productDAO();
    private String filaSeleccionada = "";

    public productDashboardPanel() {

        initComponents();        
        actionButtons();
        start();
    }
    
    private void start(){
        
        productListPanel p1 = new productListPanel();
        p1.setSize(1700, 800);
        p1.setLocation(0, 0);
        
        jPanelProductContent.removeAll();
        jPanelProductContent.add(p1, BorderLayout.CENTER);
        jPanelProductContent.revalidate();
        jPanelProductContent.repaint();
    }

    private void actionButtons(){
        
        btnProdHome.addActionListener(e->{
            start();
        });
        
        btnRerturnProduct.addActionListener(e-> {
            
            productReturnPanel pReturn = new productReturnPanel();
            pReturn.setSize(1750, 800);
            pReturn.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(pReturn, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint(); 
        });       
        
        btnAddStock.addActionListener(e -> {
            
            productStockPanel pStock = new productStockPanel();
            pStock.setSize(1750, 800);
            pStock.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(pStock, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint(); 
        });          
        
        btnNewProduct.addActionListener(e->{

            productNewPanel pNew = new productNewPanel();
            pNew.setSize(1750, 800);
            pNew.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(pNew, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint();            

        });
        
        btnView.addActionListener(e->{
            
            productViewPanel pEdit = new productViewPanel();
            pEdit.setSize(1750, 800);
            pEdit.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(pEdit, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint(); 
        });
        
        btnAddSN.addActionListener(e->{
            
            productAddSerialNumberPanel pAddSN = new productAddSerialNumberPanel();
            pAddSN.setSize(1750, 800);
            pAddSN.setLocation(0, 0);

            jPanelProductContent.removeAll();
            jPanelProductContent.add(pAddSN, BorderLayout.CENTER);
            jPanelProductContent.revalidate();
            jPanelProductContent.repaint(); 
        });
      
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        btnAddSN = new javax.swing.JButton();
        btnRerturnProduct = new javax.swing.JButton();
        jPanelProductContent = new javax.swing.JPanel();
        btnAddStock = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnNewProduct = new javax.swing.JButton();
        btnProdHome = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1253, 877));

        jPanel10.setBackground(new java.awt.Color(245, 248, 255));

        btnAddSN.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnAddSN.setForeground(new java.awt.Color(12, 83, 151));
        btnAddSN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/barcode32.png"))); // NOI18N
        btnAddSN.setText("Registrar S/N");

        btnRerturnProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnRerturnProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnRerturnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pReturn32.png"))); // NOI18N
        btnRerturnProduct.setText("Devolución");

        jPanelProductContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelProductContent.setPreferredSize(new java.awt.Dimension(1600, 800));

        javax.swing.GroupLayout jPanelProductContentLayout = new javax.swing.GroupLayout(jPanelProductContent);
        jPanelProductContent.setLayout(jPanelProductContentLayout);
        jPanelProductContentLayout.setHorizontalGroup(
            jPanelProductContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelProductContentLayout.setVerticalGroup(
            jPanelProductContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
        );

        btnAddStock.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnAddStock.setForeground(new java.awt.Color(12, 83, 151));
        btnAddStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pStock32.png"))); // NOI18N
        btnAddStock.setText("Stock");

        btnView.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnView.setForeground(new java.awt.Color(12, 83, 151));
        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pView32.png"))); // NOI18N
        btnView.setText("Ver producto");

        btnNewProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnNewProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pNew32.png"))); // NOI18N
        btnNewProduct.setText("Nuevo");

        btnProdHome.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnProdHome.setForeground(new java.awt.Color(12, 83, 151));
        btnProdHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pList32.png"))); // NOI18N
        btnProdHome.setText("Listar productos");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnProdHome, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddSN, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(197, Short.MAX_VALUE))
            .addComponent(jPanelProductContent, javax.swing.GroupLayout.DEFAULT_SIZE, 1253, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnAddSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProdHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProductContent, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSN;
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnNewProduct;
    private javax.swing.JButton btnProdHome;
    private javax.swing.JButton btnRerturnProduct;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanelProductContent;
    // End of variables declaration//GEN-END:variables
}
