/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import Class.GenericDAO;
import Class.ProductDAO;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Class.ProductDAO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;



public class PageProduct extends javax.swing.JPanel {

    ProductDAO queriesProduct = new ProductDAO();
    GenericDAO queriesGeneric = new GenericDAO();
    
    DefaultTableModel tableProducts = new DefaultTableModel();
    DefaultTableModel tableFinancy = new DefaultTableModel();
    private String filaSeleccionada = "";
    //⚠️
    //✅
    
    
    public PageProduct() {
        initComponents();
        actionButtons();
        agregarPlaceholder(txtCodProduct, "Ingrese código de producto...");       
        queriesGeneric.llenarCombosActivos(cboCategories,"categories");
        queriesGeneric.llenarCombos(cboBrand,"brands");
        llenarSubcategorias(); 
        
        filtrarPorCombos();
        
    }

    private void addTableFinancy(String entidad, String financy, double ctf, double cuota){
         tableFinancy.addRow(new Object[]{entidad,financy,ctf,cuota});
    }

    void agregarPlaceholder(JTextField campo, String placeholder) {
        campo.setForeground(Color.GRAY);
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }
    
    void llenarSubcategorias(){
        cboCategories.addActionListener(e -> {
            String categoria = (String) cboCategories.getSelectedItem();

            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                int idCat = queriesProduct.selectIdCategoria(categoria);
                cboSubcategories.removeAllItems();
                queriesProduct.llenarCombosSubcategories(cboSubcategories, idCat);          
            }
        });
    }
    
    void filtrarPorCombos(){
        
        String categoria = (String) cboCategories.getSelectedItem();
        String subcategoria = (String) cboSubcategories.getSelectedItem();
        String brand = (String) cboBrand.getSelectedItem();
        
        int idCat = queriesProduct.selectIdCategoria(categoria); 
        int idSubcat = queriesGeneric.selectId("id_subcategory","subcategories",subcategoria);
        int idBrand = queriesGeneric.selectId("id_brand","brands",brand);
        
        if(cboBrand.getSelectedIndex() != 0){
            if(cboCategories.getSelectedIndex() != 0){               
                if(cboSubcategories.getSelectedIndex() != 0){
                    queriesProduct.listProdForBrandAndSubCat(jtablePrducts, idBrand, idSubcat);
                }else{
                    queriesProduct.listProdForBrandAndCat(jtablePrducts, idBrand, idCat);
                }
            }else{
                queriesProduct.listAllProdForBrand(jtablePrducts, idBrand);
            }          
            
        }else{
            if(cboCategories.getSelectedIndex() != 0){
                if(cboSubcategories.getSelectedIndex() != 0){
                    queriesProduct.listAllProdForSubcategory(jtablePrducts,idSubcat);
                }else{
                    queriesProduct.listAllProdForIDCategory(jtablePrducts,idCat);
                }               
            }else{
                queriesProduct.listAllProdForCategory(jtablePrducts);
            }
        }
        
    }    
 
    private void actionButtons(){

        
        btnEditPrice.addActionListener(e->{
            int id = queriesProduct.selectIdProduct(filaSeleccionada);
            
            frmEditPrice editPrice = new frmEditPrice();
            editPrice.dialogoEdit(id);
            editPrice.setVisible(true);
        });
          
                
        btnSerchCode.addActionListener(e -> { 
            tableProducts.setRowCount(0);
            String productCode = txtCodProduct.getText().trim();

            if (!productCode.isEmpty() && !productCode.equals("Ingrese código de producto...")) {
                queriesProduct.listTableProducts(jtablePrducts, productCode);
                txtCodProduct.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "¡Debe ingresar un código de producto!");
                txtCodProduct.requestFocus();
            }
        });
        
        btnSerchCode2.addActionListener(e -> {
            tableProducts.setRowCount(0);
            filtrarPorCombos(); 
        });
                
        jtablePrducts.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    filaSeleccionada = String.valueOf(jtablePrducts.getValueAt(jtablePrducts.getSelectedRow(), 0).toString()); 
                }
                else{
                }            
            }
        });
    }    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCodProduct = new javax.swing.JTextField();
        btnSerchCode = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboBrand = new javax.swing.JComboBox<>();
        btnSerchCode2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtablePrducts = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtableFinancy = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnEditPrice = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1600, 800));

        jPanel9.setBackground(new java.awt.Color(12, 83, 151));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Buscar");

        txtCodProduct.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        txtCodProduct.setText("Ingrese codigo de producto...");
        txtCodProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        btnSerchCode.setBackground(new java.awt.Color(242, 242, 242));
        btnSerchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerchCode.setBorder(null);
        btnSerchCode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSerchCodeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSerchCodeMouseExited(evt);
            }
        });
        btnSerchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCodeActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Categoría:");

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboCategories.setForeground(new java.awt.Color(102, 102, 102));
        cboCategories.setToolTipText("");
        cboCategories.setBorder(null);
        cboCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriesActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Subcategoría:");

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboSubcategories.setForeground(new java.awt.Color(102, 102, 102));
        cboSubcategories.setBorder(null);
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Marca:");

        cboBrand.setBackground(new java.awt.Color(255, 255, 255));
        cboBrand.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboBrand.setForeground(new java.awt.Color(102, 102, 102));
        cboBrand.setBorder(null);

        btnSerchCode2.setBackground(new java.awt.Color(242, 242, 242));
        btnSerchCode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerchCode2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 2, true));
        btnSerchCode2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCode2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSerchCode2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSerchCode2MouseExited(evt);
            }
        });
        btnSerchCode2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCode2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(501, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboSubcategories)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboBrand)
                    .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        jtablePrducts.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtablePrducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtablePrducts.setGridColor(new java.awt.Color(255, 255, 255));
        jtablePrducts.setRowHeight(25);
        jtablePrducts.setSelectionBackground(new java.awt.Color(255, 204, 153));
        jScrollPane1.setViewportView(jtablePrducts);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jtableFinancy.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jtableFinancy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jtableFinancy);

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(12, 83, 151));
        jLabel6.setText("Financiación.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnEditPrice.setBackground(new java.awt.Color(255, 255, 255));
        btnEditPrice.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEditPrice.setForeground(new java.awt.Color(12, 83, 151));
        btnEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductPrice.png"))); // NOI18N
        btnEditPrice.setText("Editar Precio");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1194, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSerchCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCodeMouseEntered
        btnSerchCode.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnSerchCodeMouseEntered

    private void btnSerchCodeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCodeMouseExited
        btnSerchCode.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchCodeMouseExited

    private void btnSerchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCodeActionPerformed

    }//GEN-LAST:event_btnSerchCodeActionPerformed

    private void cboCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriesActionPerformed

    }//GEN-LAST:event_cboCategoriesActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed

    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnSerchCode2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseEntered
        btnSerchCode2.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnSerchCode2MouseEntered

    private void btnSerchCode2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseExited
        btnSerchCode2.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchCode2MouseExited

    private void btnSerchCode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCode2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchCode2ActionPerformed

    private void btnEditPriceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseEntered
        btnEditPrice.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditPriceMouseEntered

    private void btnEditPriceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseExited
        btnEditPrice.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditPriceMouseExited

    private void btnEditPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPriceActionPerformed

    }//GEN-LAST:event_btnEditPriceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditPrice;
    private javax.swing.JButton btnSerchCode;
    private javax.swing.JButton btnSerchCode2;
    private javax.swing.JComboBox<String> cboBrand;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jtableFinancy;
    private javax.swing.JTable jtablePrducts;
    private javax.swing.JTextField txtCodProduct;
    // End of variables declaration//GEN-END:variables
}
