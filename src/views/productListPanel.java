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
            
            int id = qProduct.selectIdProduct(filaSeleccionada);
            
            productEditFrame editP = new productEditFrame();
            editP.dialogoEdit(id);
            editP.setVisible(true);
            
            filtrarPorCombos();
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
            
            tableProducts.setRowCount(0);
            String productCode = txtCodProduct.getText().trim();

            if (!productCode.isEmpty() && !productCode.equals("código de producto.")) {
                qProduct.listTableProducts(jtablePrducts, productCode);
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        cboSubcategories = new javax.swing.JComboBox<>();
        cboBrand = new javax.swing.JComboBox<>();
        txtCodProduct = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        btnEditPrice = new javax.swing.JButton();
        btnSerchCode2 = new javax.swing.JButton();
        btnSerchCode = new javax.swing.JButton();
        CheckBoxInactivos = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtablePrducts = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1750, 800));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/checklist128.png"))); // NOI18N
        jLabel5.setText("Buscar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Categoría");

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Subcategoría");

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Marca");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("|");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("|");

        txtCodProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCodProduct.setForeground(new java.awt.Color(35, 35, 38));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32.png"))); // NOI18N

        btnEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/priceTag32.png"))); // NOI18N

        btnSerchCode2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSerchCode2.setForeground(new java.awt.Color(12, 83, 151));
        btnSerchCode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/filtrar32.png"))); // NOI18N
        btnSerchCode2.setText("Buscar filtro");

        btnSerchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        CheckBoxInactivos.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        CheckBoxInactivos.setForeground(new java.awt.Color(12, 83, 151));
        CheckBoxInactivos.setText("Mostrar inactivos");
        CheckBoxInactivos.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
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
                        .addComponent(CheckBoxInactivos))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckBoxInactivos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jScrollPane1)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtablePrducts;
    private javax.swing.JTextField txtCodProduct;
    // End of variables declaration//GEN-END:variables
}
