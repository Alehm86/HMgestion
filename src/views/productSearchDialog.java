/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class productSearchDialog extends javax.swing.JDialog {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    DefaultTableModel tableProducts = new DefaultTableModel();
    
    private String filaSeleccionada = "";
    private int idProduct;
    private int state = 1;
    
    public int getProduct(){        
        return idProduct;
    }
       
    public productSearchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        qGeneric.llenarCombosActivos(cboCategories,"product_categories");
        qGeneric.llenarCombos(cboBrand,"product_brands");
        
        llenarSubcategorias();        
        filtrarPorCombos();
        actions();
        ocultarColumnas();
    }   

    void llenarSubcategorias(){
        cboCategories.addActionListener(e -> {
            String categoria = (String) cboCategories.getSelectedItem();

            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                int idCat = qProduct.selectIdCategoria(categoria);
                cboSubcategories.removeAllItems();
                qProduct.llenarCombosSubcategories(cboSubcategories, idCat);          
            }
        });
    }
    
    private void filtrarPorCombos(){
        
        String categoria = (String) cboCategories.getSelectedItem();
        String subcategoria = (String) cboSubcategories.getSelectedItem();
        String brand = (String) cboBrand.getSelectedItem();
        
        int idCat = qProduct.selectIdCategoria(categoria); 
        int idSubcat = qGeneric.selectId("id_subcategory","product_subcategories",subcategoria);
        int idBrand = qGeneric.selectId("id_brand","product_brands",brand);
        
        if(cboBrand.getSelectedIndex() != 0){
            if(cboCategories.getSelectedIndex() != 0){               
                if(cboSubcategories.getSelectedIndex() != 0){
                    qProduct.listProdForBrandAndSubCat(jtablePrducts, idBrand, idSubcat, state);
                }else{
                    qProduct.listProdForBrandAndCat(jtablePrducts, idBrand, idCat, state);
                }
            }else{
                qProduct.listAllProdForBrand(jtablePrducts, idBrand, state);
            }                     
        }else{
            if(cboCategories.getSelectedIndex() != 0){
                if(cboSubcategories.getSelectedIndex() != 0){
                    qProduct.listProdFSubcategory(jtablePrducts,idSubcat,state);
                }else{
                    qProduct.listProdFCategory(jtablePrducts,idCat,state);
                }               
            }else{
                qProduct.listAllProduct(jtablePrducts,state);
            }
        }
        ocultarColumnas();
    }
    
    private void actions(){
            
        CheckBoxInactivos.addActionListener(e->{

            if(CheckBoxInactivos.isSelected()){
                state = 0;           
            }else{
                state = 1;
            } 
            filtrarPorCombos();
        });    
        
        btnSerchCode2.addActionListener(e -> {
            tableProducts.setRowCount(0);
            filtrarPorCombos(); 
        });
        
        btnSelectProduct.addActionListener (e->{
            
            if(!filaSeleccionada.isEmpty()){
                idProduct = qProduct.selectIdProduct(filaSeleccionada);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar un producto de la lista!"); 
            }

        });
        
        btnCancel.addActionListener (e->{
            this.dispose();
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
    
    private void ocultarColumnas(){
        
        int totalColumnas = jtablePrducts.getColumnModel().getColumnCount();

        for (int i = 2; i <= 5 && i < totalColumnas; i++) {
            jtablePrducts.getColumnModel().getColumn(i).setMinWidth(0);
            jtablePrducts.getColumnModel().getColumn(i).setMaxWidth(0);
            jtablePrducts.getColumnModel().getColumn(i).setPreferredWidth(0);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboBrand = new javax.swing.JComboBox<>();
        btnSerchCode2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        CheckBoxInactivos = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtablePrducts = new javax.swing.JTable();
        btnSelectProduct = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(101, 129, 171));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Categoría:");

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboCategories.setForeground(new java.awt.Color(35, 35, 38));
        cboCategories.setToolTipText("");
        cboCategories.setBorder(null);
        cboCategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriesActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Subcategoría:");

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboSubcategories.setForeground(new java.awt.Color(35, 35, 38));
        cboSubcategories.setBorder(null);
        cboSubcategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Marca:");

        cboBrand.setBackground(new java.awt.Color(255, 255, 255));
        cboBrand.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboBrand.setForeground(new java.awt.Color(35, 35, 38));
        cboBrand.setBorder(null);
        cboBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnSerchCode2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnSerchCode2.setForeground(new java.awt.Color(12, 83, 151));
        btnSerchCode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnSerchCode2.setText("Buscar");
        btnSerchCode2.setBorder(null);
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

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product128.png"))); // NOI18N

        CheckBoxInactivos.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        CheckBoxInactivos.setForeground(new java.awt.Color(255, 255, 255));
        CheckBoxInactivos.setText("Mostrar inactivos");
        CheckBoxInactivos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(CheckBoxInactivos))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CheckBoxInactivos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );

        jtablePrducts.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtablePrducts.setForeground(new java.awt.Color(65, 65, 63));
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
        jtablePrducts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtablePrducts.setFillsViewportHeight(true);
        jtablePrducts.setGridColor(new java.awt.Color(255, 255, 255));
        jtablePrducts.setRowHeight(30);
        jtablePrducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtablePrductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtablePrducts);

        btnSelectProduct.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnSelectProduct.setForeground(new java.awt.Color(101, 129, 171));
        btnSelectProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelectProduct.setText("Seleccionar");
        btnSelectProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSelectProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSelectProduct.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSelectProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSelectProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSelectProductMouseExited(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(101, 129, 171));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSelectProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cboCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriesActionPerformed

    }//GEN-LAST:event_cboCategoriesActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed

    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnSerchCode2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseEntered
        btnSerchCode2.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnSerchCode2MouseEntered

    private void btnSerchCode2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseExited
        btnSerchCode2.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchCode2MouseExited

    private void btnSerchCode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCode2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchCode2ActionPerformed

    private void btnSelectProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectProductMouseEntered
        btnSelectProduct.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnSelectProductMouseEntered

    private void btnSelectProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectProductMouseExited
        btnSelectProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSelectProductMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void jtablePrductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtablePrductsMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = jtablePrducts.getSelectedRow();

            if (fila != -1) {
                idProduct = qProduct.selectIdProduct(filaSeleccionada);
                this.dispose();     
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_jtablePrductsMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                customerCambioEstadoDialog dialog = new customerCambioEstadoDialog(new javax.swing.JFrame(),true);
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
    private javax.swing.JCheckBox CheckBoxInactivos;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectProduct;
    private javax.swing.JButton btnSerchCode2;
    private javax.swing.JComboBox<String> cboBrand;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtablePrducts;
    // End of variables declaration//GEN-END:variables
}
