/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.genericDAO;
import javax.swing.table.DefaultTableModel;
import dao.productDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import utils.utility;

public class productListPanel extends javax.swing.JPanel {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    utility utils = new utility();
    
    DefaultTableModel tableProducts = new DefaultTableModel();
    
    private TableRowSorter<DefaultTableModel> sorter;
    
    private String filaSeleccionada = "";
    private int state = 1;

    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);    
    
    public productListPanel() {
        initComponents();
        
        utils.agregarPlaceholderN(txtCodProduct, "código de producto.");       
        qGeneric.llenarCombosActivos(cboCategories,"product_categories");
        qGeneric.llenarCombos(cboBrand,"product_brands");
        
        llenarSubcategorias();        
        filtrarPorCombos();
        actions();
        
        configTabla();
    }  
    
    private void configTabla(){
        jtablePrducts.getColumnModel().getColumn(5).setCellRenderer(new ColorRenderPrecio());
        jtablePrducts.getColumnModel().getColumn(3).setCellRenderer(new ColorRenderPromo());
        jtablePrducts.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderStock());
        jtablePrducts.getColumnModel().getColumn(4).setCellRenderer(new alignIVA());
    }
    
    private void llenarSubcategorias(){
        
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
        activarBuscadorTabla();
        configTabla();
        
    }
    
    private void activarBuscadorTabla() {
        DefaultTableModel modelo = (DefaultTableModel) jtablePrducts.getModel();
        sorter = new TableRowSorter<>(modelo);
        jtablePrducts.setRowSorter(sorter);

        txtCodProduct.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtCodProduct.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                }
            }
        });
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

        btnEdit.addActionListener(e->{
            
            if(!filaSeleccionada.isEmpty()){
                int id = qProduct.selectIdProduct(filaSeleccionada);
            
                productEditFrame editP = new productEditFrame();
                editP.dialogoEdit(id);
                editP.setVisible(true);

                filtrarPorCombos();
            }else{
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar un producto de la lista!");
            }
            
        });
        
        btnEditPrice.addActionListener(e->{
            
            boolean estado = false;
            
            int id = qProduct.selectIdProduct(filaSeleccionada);
            
            if(!filaSeleccionada.isEmpty()){
                
                productPriceEditDialog editPrice = new productPriceEditDialog(null, true);
                editPrice.dialogoEdit(id);
                editPrice.setVisible(true);
               
                estado = editPrice.respuesta();
                
                if(estado){
                    filtrarPorCombos();
                }    
            }else{
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar un producto de la lista!"); 
            }
        });
                       
        btnSerchCode.addActionListener(e -> { 
            buscarProduct();          
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
    
    private void buscarProduct(){
        tableProducts.setRowCount(0);
            String productCode = txtCodProduct.getText().trim();

            if (!productCode.isEmpty() && !productCode.equals("código de producto.")) {
                qProduct.listTableProducts(jtablePrducts, productCode);
                txtCodProduct.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "¡Debe ingresar un código de producto!");
                txtCodProduct.requestFocus();
            }
    }
    
    public class ColorRenderPrecio extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component precio = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null && "SIN PRECIO".equals(value.toString())) {
                precio.setForeground(Color.RED);
            } else {
                precio.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }

            return precio;
        }
    }
    
    public class ColorRenderPromo extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component promo = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null && "Sin promo".equals(value.toString())) {
                promo.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            } else {
                promo.setForeground(Color.GREEN);
            }

            return promo;
        }
    }
    
    public class ColorRenderStock extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component stock = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                int cantidad = Integer.parseInt(value.toString());

                if (cantidad == 1) {
                    setText("ÚLTIMO EN STOCK");
                    stock.setForeground(Color.ORANGE);

                } else if (cantidad == 0) {
                    setText("SIN STOCK");
                    stock.setForeground(Color.RED);

                } else {
                    setText(String.valueOf(cantidad));
                    stock.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
            }

            return stock;
        }
    }
    
    public class alignIVA extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component promo = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            return promo;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnEditPrice = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cboCategories = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboBrand = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnSerchCode2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtCodProduct = new javax.swing.JTextField();
        btnSerchCode = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        CheckBoxInactivos = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtablePrducts = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1750, 865));

        jPanel9.setBackground(new java.awt.Color(245, 248, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(12, 83, 151));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/productosCatalogo64.png"))); // NOI18N
        jLabel5.setText("Productos");

        jPanel3.setBackground(new java.awt.Color(245, 248, 255));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar-32.png"))); // NOI18N

        btnEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/priceTag32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Categoría");

        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(12, 83, 151));
        jLabel4.setText("Subcategoría");

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(12, 83, 151));
        jLabel2.setText("Marca");

        btnSerchCode2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSerchCode2.setForeground(new java.awt.Color(12, 83, 151));
        btnSerchCode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/filtrar32.png"))); // NOI18N
        btnSerchCode2.setText("Buscar filtro");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(12, 83, 151));
        jLabel3.setText("|");

        txtCodProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCodProduct.setForeground(new java.awt.Color(35, 35, 38));
        txtCodProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodProductKeyPressed(evt);
            }
        });

        btnSerchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(12, 83, 151));
        jLabel6.setText("|");

        CheckBoxInactivos.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        CheckBoxInactivos.setForeground(new java.awt.Color(12, 83, 151));
        CheckBoxInactivos.setText("Mostrar inactivos");
        CheckBoxInactivos.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerchCode2)
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addGap(4, 4, 4)
                .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CheckBoxInactivos)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckBoxInactivos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jtablePrducts.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtablePrducts.setForeground(new java.awt.Color(65, 65, 63));
        jtablePrducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        jtablePrducts.setRowHeight(28);
        jScrollPane1.setViewportView(jtablePrducts);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void txtCodProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProductKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            buscarProduct();          
        }
    }//GEN-LAST:event_txtCodProductKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckBoxInactivos;
    private javax.swing.JButton btnEdit;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtablePrducts;
    private javax.swing.JTextField txtCodProduct;
    // End of variables declaration//GEN-END:variables
}
