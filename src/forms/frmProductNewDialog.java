/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import Class.GenericDAO;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Class.ProductDAO;
import java.text.NumberFormat;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import Class.modelProducts;
import Class.modelPrice;
import Old.frmNewProduct_1;
import forms.frmBrandEdit;
import forms.frmBrandNew;
import forms.frmCategoriesEdit;
import forms.frmCategoriesNew;
import forms.frmSalesManagement;
import forms.frmSubategoriesNew;
import forms.frmSubcategoriesEdit;
import forms.frmSupplierEdit;
import forms.frmSupplierNew;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class frmProductNewDialog extends javax.swing.JDialog {

    ProductDAO queriesProduct = new ProductDAO();
    GenericDAO queriesGeneric = new GenericDAO();
    
    modelProducts product = new modelProducts();
    modelPrice precio = new modelPrice();
    private String action;
    private String productCode;
    
    public frmProductNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        start();
        llenarCombos();
        formatearTxtPrice();
        llenarSubcategorias();
        actionButtons();
        popupBrand();
        popupSupplier();
        popupCategories();
        popupSubcategories();
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
    
    void llenarCombos(){
        queriesGeneric.llenarCombos(cboBrands,"brands");
        queriesGeneric.llenarCombosActivos(cboCategories,"categories");      
        queriesGeneric.llenarCombos(cboSuppliers,"suppliers");
    }
    
    void formatearTxtPrice(){
        NumberFormat formatoDecimal = NumberFormat.getNumberInstance();
        formatoDecimal.setMinimumFractionDigits(2);
        formatoDecimal.setMaximumFractionDigits(2);
        formatoDecimal.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(formatoDecimal);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);

        txtPrice.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }
  
    public void start(){
        txtModel.setText("");
        txtColor.setText("");
        txtProductCode.setText("");
        txtPrice.setValue(0);
        
    }
    
    void insertProduct(){
        product.state = 1;
        precio.benefit = 0;
        precio.salesPrice= 0;
      
        boolean valido = true;
        
        if (cboBrands.getSelectedIndex() > 0) { 
            product.id_brand = queriesGeneric.selectId(
                    "id_brand",
                    "brands",
                    cboBrands.getSelectedItem().toString()
            );
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una marca!");
            valido = false;
        }
        
        if (cboSuppliers.getSelectedIndex() > 0) { 
            product.id_supplier = queriesGeneric.selectId(
                    "id_supplier",
                    "suppliers",
                    cboSuppliers.getSelectedItem().toString()
            );
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un proveedor!");
            valido = false;
        }
        
        if (cboCategories.getSelectedIndex() > 0) { 
            product.id_category = queriesGeneric.selectId(
                    "id_category",
                    "categories",
                    cboCategories.getSelectedItem().toString()
            );

            if (cboSubcategories.getSelectedIndex() > 0) {
                product.id_subcat = queriesGeneric.selectId(
                        "id_subcategory",
                        "subcategories",
                        cboSubcategories.getSelectedItem().toString()
                );
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una subcategoría!");
                valido = false;
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría!");
            valido = false;
        }
        
        if(!txtModel.getText().isEmpty()){
            product.model = txtModel.getText().toUpperCase();
        } else {
            JOptionPane.showMessageDialog(null, "Complete el campo modelo!");
            valido = false;
        }
        
        if(!txtColor.getText().isEmpty()){
            product.color = txtColor.getText().toUpperCase();
        } else {
            JOptionPane.showMessageDialog(null, "Complete el campo color!");
            valido = false;
        }
        
        if(!txtProductCode.getText().isEmpty()){
            product.productCode = txtProductCode.getText().toUpperCase();
            productCode = txtProductCode.getText().toUpperCase();
        } else {
            JOptionPane.showMessageDialog(null, "Complete el campo codigo de producto!");
            valido = false;
        }
                
        if (txtPrice.getValue() != null) {
            precio.price = ((Number) txtPrice.getValue()).doubleValue();
        } else {
            JOptionPane.showMessageDialog(null, "Complete el campo de precio!");
            valido = false;
        }
        
        if (cboIva.getSelectedIndex() != 0) {
            precio.iva = Double.parseDouble(cboIva.getSelectedItem().toString());
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un porcentaje de IVA!");
            valido = false;
        }
  
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Confirma el registro del producto?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );                   
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        queriesProduct.insertProduct(
                product.getId_subcat(),
                product.getId_brand(),
                product.getId_supplier(),
                product.getId_category(),
                product.getModel(),
                product.getColor(),
                product.getProductCode(),
                product.getState()
        );     
    }
    
    public int obtenerId(String codeProduct){
        int id = 0;
        id = queriesProduct.selectIdProduct(codeProduct);
        return id;
    }
    
    void insetPrice(int idProd){ 
        
        queriesProduct.priceProduct(
                idProd, 
                precio.getPrice(), 
                precio.getBenefit(), 
                precio.getIva(),
                precio.getSalesPrice()
        );       
    }
    
    public void popupBrand(){
        JPopupMenu popupBrands = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva marca");
        JMenuItem item2 = new JMenuItem("Editar marca");
        
        item1.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmBrandNew dialogo = new frmBrandNew(parent, true);
            dialogo.setVisible(true);

            String nuevaMarca = dialogo.getMarcaCreada();
            queriesGeneric.llenarCombos(cboBrands, "brands");

            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });

        
        item2.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmBrandEdit dialogo = new frmBrandEdit(parent, true);
            dialogo.setVisible(true);

            String nuevaMarca = dialogo.getMarcaCreada();
            queriesGeneric.llenarCombos(cboBrands, "brands");

            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });
     
        Font fontMenu = new Font("Poppins", Font.BOLD, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(101, 129, 171));
        item2.setForeground(new Color(101, 129, 171));
        
        popupBrands.add(item1);
        popupBrands.add(item2);
        
        btnBrand.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {              
                if (SwingUtilities.isLeftMouseButton(e)) {
                    popupBrands.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void popupSupplier(){
        JPopupMenu popupSupplier = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nuevo proveedor");
        JMenuItem item2 = new JMenuItem("Editar proveedor");
        
        item1.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmSupplierNew dialogo = new frmSupplierNew(parent, true);
            dialogo.setVisible(true);

            String newSupplier = dialogo.getProveedorCreado();
            queriesGeneric.llenarCombos(cboSuppliers, "suppliers");

            if (newSupplier != null && !newSupplier.isEmpty()) {
                cboSuppliers.setSelectedItem(newSupplier);
            }
        });
     
        item2.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmSupplierEdit dialogo = new frmSupplierEdit(parent, true);
            dialogo.setVisible(true);

            String newSupplier = dialogo.getProveedorEditado();
            queriesGeneric.llenarCombos(cboSuppliers, "suppliers");

            if (newSupplier != null && !newSupplier.isEmpty()) {
                cboSuppliers.setSelectedItem(newSupplier);
            }
        });

        Font fontMenu = new Font("Poppins", Font.BOLD, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(101, 129, 171));
        item2.setForeground(new Color(101, 129, 171));
        
        popupSupplier.add(item1);
        popupSupplier.add(item2);
        
        btnSupplier.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {              
                if (SwingUtilities.isLeftMouseButton(e)) {
                    popupSupplier.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void popupCategories(){
        JPopupMenu popupSupplier = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva categoria");
        JMenuItem item2 = new JMenuItem("Editar categoria");
        
        item1.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmCategoriesNew dialogo = new frmCategoriesNew(parent, true);
            dialogo.setVisible(true);

            String nuevaCategoria = dialogo.getCategoriaCreada();
            queriesGeneric.llenarCombosActivos(cboCategories, "categories");

            if (nuevaCategoria != null) {
                cboCategories.setSelectedItem(nuevaCategoria);
            }
        });
     
//        item2.addActionListener(e -> {
//
//            frmCategoriesEdit dialogo = new frmCategoriesEdit(this, true);
//            dialogo.setVisible(true);
//            String nuevaCategoria = dialogo.getCategoriaCreada();
////            queries.llenarCombosActivos(cboCategories,"categories");
//        
//            if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
////                cboCategories.setSelectedItem(nuevaCategoria);
//                cboCategories.removeAllItems();
//                queries.llenarCombosActivos(cboCategories,"categories");
//                cboCategories.setSelectedItem(nuevaCategoria);
//            } 
//        });

        item2.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmCategoriesEdit dialogo = new frmCategoriesEdit(parent, true);
            dialogo.setVisible(true);

            String nuevaCategoria = dialogo.getCategoriaCreada();

            if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {

                cboCategories.removeAllItems();
                queriesGeneric.llenarCombosActivos(cboCategories, "categories");
                cboCategories.setSelectedItem(nuevaCategoria);
            }
        });    
        
        Font fontMenu = new Font("Poppins", Font.BOLD, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(101, 129, 171));
        item2.setForeground(new Color(101, 129, 171));
        
        popupSupplier.add(item1);
        popupSupplier.add(item2);
        
        btnCategory.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {              
                if (SwingUtilities.isLeftMouseButton(e)) {
                    popupSupplier.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void popupSubcategories(){
        JPopupMenu popupSupplier = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva subcategoria");
        JMenuItem item2 = new JMenuItem("Editar subcategoria");
        
        item1.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmSubategoriesNew dialogo = new frmSubategoriesNew(parent, true);
            dialogo.setVisible(true);

            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();

            if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                String categoria = (String) cboCategories.getSelectedItem();

                if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                    int idCat = queriesProduct.selectIdCategoria(categoria);
                    cboSubcategories.removeAllItems();
                    queriesProduct.llenarCombosSubcategories(cboSubcategories, idCat);
                    cboSubcategories.setSelectedItem(nuevaSubcategoria);
                }
            }
        });

        item2.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            frmSubcategoriesEdit dialogo = new frmSubcategoriesEdit(parent, true);
            dialogo.setVisible(true);

            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();

            if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                String categoria = (String) cboCategories.getSelectedItem();

                if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                    int idCat = queriesProduct.selectIdCategoria(categoria);
                    cboSubcategories.removeAllItems();
                    queriesProduct.llenarCombosSubcategories(cboSubcategories, idCat);
                    cboSubcategories.setSelectedItem(nuevaSubcategoria);
                }
            }
        });
        
        Font fontMenu = new Font("Poppins", Font.BOLD, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(101, 129, 171));
        item2.setForeground(new Color(101, 129, 171));
        
        popupSupplier.add(item1);
        popupSupplier.add(item2);
        
        btnSubcategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {              
                if (SwingUtilities.isLeftMouseButton(e)) {
                    popupSupplier.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void insetStock(int idProd){               
        queriesProduct.insertStock(idProd);       
    }
    
    private void actionButtons(){
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertProduct();       
                insetPrice(obtenerId(productCode)); 
                insetStock(obtenerId(productCode));
                JOptionPane.showMessageDialog(null, "Producto registrado con exito!");
                start();
                cboBrands.setSelectedIndex(0);
                cboSuppliers.setSelectedIndex(0);
                cboCategories.setSelectedIndex(0);
                cboIva.setSelectedIndex(0);
            }
        });
       
        btnCancel.addActionListener(e->{
           start();
        });
        
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cboBrands = new javax.swing.JComboBox<>();
        btnBrand = new javax.swing.JButton();
        txtProductCode = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        btnCategory = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        btnSubcategories = new javax.swing.JButton();
        btnSupplier = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        cboIva = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cboSuppliers = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setMaximumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setMinimumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 52));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(383, 383, 383)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setText("Marca:");

        cboBrands.setBackground(new java.awt.Color(255, 255, 255));
        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboBrands.setForeground(new java.awt.Color(102, 102, 102));
        cboBrands.setBorder(null);
        cboBrands.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboBrands.setFocusable(false);
        cboBrands.setLightWeightPopupEnabled(false);
        cboBrands.setMinimumSize(new java.awt.Dimension(70, 22));
        cboBrands.setPreferredSize(new java.awt.Dimension(70, 22));
        cboBrands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBrandsActionPerformed(evt);
            }
        });

        btnBrand.setBackground(new java.awt.Color(255, 255, 255));
        btnBrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear32.png"))); // NOI18N
        btnBrand.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        btnBrand.setBorderPainted(false);
        btnBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBrand.setFocusPainted(false);
        btnBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrandActionPerformed(evt);
            }
        });

        txtProductCode.setBackground(new java.awt.Color(204, 204, 204,25));
        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel17.setText("Codigo prod.");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setText("Modelo:");

        txtModel.setBackground(new java.awt.Color(204, 204, 204,25));
        txtModel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtModel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setText("Color:");

        txtColor.setBackground(new java.awt.Color(204, 204, 204,25));
        txtColor.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel19.setText("Categoria");

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCategories.setBorder(null);
        cboCategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboCategories.setFocusable(false);

        btnCategory.setBackground(new java.awt.Color(255, 255, 255));
        btnCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear32.png"))); // NOI18N
        btnCategory.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        btnCategory.setBorderPainted(false);
        btnCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCategory.setFocusPainted(false);
        btnCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel20.setText("Subcategoría:");

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSubcategories.setBorder(null);
        cboSubcategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSubcategories.setFocusable(false);
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        btnSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        btnSubcategories.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear32.png"))); // NOI18N
        btnSubcategories.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        btnSubcategories.setBorderPainted(false);
        btnSubcategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubcategories.setFocusPainted(false);
        btnSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubcategoriesActionPerformed(evt);
            }
        });

        btnSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear32.png"))); // NOI18N
        btnSupplier.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        btnSupplier.setBorderPainted(false);
        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.setFocusPainted(false);
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setText("Precio de costo:$");

        txtPrice.setBackground(new java.awt.Color(204, 204, 204,25));
        txtPrice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel9.setText("I.V.A.:");

        cboIva.setBackground(new java.awt.Color(255, 255, 255));
        cboIva.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "\"Seleccione un %\"", "10.5", "21" }));
        cboIva.setBorder(null);
        cboIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIvaActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel18.setText("Proveedor:");

        cboSuppliers.setBackground(new java.awt.Color(255, 255, 255));
        cboSuppliers.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSuppliers.setBorder(null);
        cboSuppliers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSuppliers.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCategory, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBrand, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(136, 136, 136)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSupplier)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSubcategories)
                        .addContainerGap(39, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(12, 83, 151));

        labelTitle.setBackground(new java.awt.Color(12, 83, 151));
        labelTitle.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("Datos de producto nuevo.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void cboBrandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBrandsActionPerformed

    }//GEN-LAST:event_cboBrandsActionPerformed

    private void btnBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrandActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed

    }//GEN-LAST:event_txtColorActionPerformed

    private void btnCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCategoryActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed

    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubcategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubcategoriesActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void cboIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIvaActionPerformed

    }//GEN-LAST:event_cboIvaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmProductNewDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProductNewDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProductNewDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProductNewDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmProductNewDialog dialog = new frmProductNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSubcategories;
    private javax.swing.JButton btnSupplier;
    private javax.swing.JComboBox<String> cboBrands;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboIva;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JComboBox<String> cboSuppliers;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModel;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
