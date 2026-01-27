/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Old;


import Class.GenericDAO;
import Old.frmProducts;
import Old.frmMenu;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public class frmNewProduct_1 extends javax.swing.JFrame {
    ProductDAO queries = new ProductDAO();
        GenericDAO queriesGeneric = new GenericDAO();
    modelProducts product = new modelProducts();
    modelPrice precio = new modelPrice();
    
    fondoPanel background = new fondoPanel();
    private String action;
    private String productCode;
    
    public frmNewProduct_1() {
        
        this.setContentPane(background);
        initComponents();
        this.setExtendedState(6);
        
        inicializar();
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
                int idCat = queries.selectIdCategoria(categoria);
                cboSubcategories.removeAllItems();
                queries.llenarCombosSubcategories(cboSubcategories, idCat);          
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
  
    public void inicializar(){
        txtModel.setText("");
        txtColor.setText("");
        txtProductCode.setText("");
        txtPrice.setValue(0);
        
    }
    
    class fondoPanel extends JPanel{
        private Image imagen;      
        @Override
        public void paint(Graphics g)
        {
            imagen = new ImageIcon(getClass().getResource("/Wallpapers/fondoMenu.jpg")).getImage();          
            g.drawImage(imagen,0, 0, getWidth(), getHeight(),this);         
            setOpaque(false);      
            super.paint(g);
        }
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
        
        queries.insertProduct(
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
        id = queries.selectIdProduct(codeProduct);
        return id;
    }
    
    void insetPrice(int idProd){ 
        
        queries.priceProduct(
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
            frmBrandNew dialogo = new frmBrandNew(this, true);
            dialogo.setVisible(true);
            
            String nuevaMarca = dialogo.getMarcaCreada();
            queriesGeneric.llenarCombos(cboBrands,"brands");
           
            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });

        
        item2.addActionListener(e -> {
            frmBrandEdit dialogo = new frmBrandEdit(this, true);
            dialogo.setVisible(true);
            String nuevaMarca = dialogo.getMarcaCreada();
            queriesGeneric.llenarCombos(cboBrands,"brands");
           
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
            
            frmSupplierNew dialogo = new frmSupplierNew(this, true);
            dialogo.setVisible(true);
            
            String newSupplier = dialogo.getProveedorCreado();
            queriesGeneric.llenarCombos(cboSuppliers, "suppliers");
           
            if (newSupplier != null && !newSupplier.isEmpty()) {
                cboSuppliers.setSelectedItem(newSupplier);
            }
        });
     
        item2.addActionListener(e -> {

            frmSupplierEdit dialogo = new frmSupplierEdit(this, true); 
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
            
            frmCategoriesNew dialogo = new frmCategoriesNew(this, true);
            dialogo.setVisible(true);
            String nuevaCategoria = dialogo.getCategoriaCreada();
            queriesGeneric.llenarCombosActivos(cboCategories,"categories");
        
            if (nuevaCategoria != null) {
                cboCategories.setSelectedItem(nuevaCategoria);
            } 
        });
     
        item2.addActionListener(e -> {

            frmCategoriesEdit dialogo = new frmCategoriesEdit(this, true);
            dialogo.setVisible(true);
            String nuevaCategoria = dialogo.getCategoriaCreada();
//            queries.llenarCombosActivos(cboCategories,"categories");
        
            if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
//                cboCategories.setSelectedItem(nuevaCategoria);
                cboCategories.removeAllItems();
                queriesGeneric.llenarCombosActivos(cboCategories,"categories");
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
            frmSubategoriesNew dialogo = new frmSubategoriesNew(this, true);
            dialogo.setVisible(true);
            
            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();
            
            if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                String categoria = (String) cboCategories.getSelectedItem();
            
                if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                    int idCat = queries.selectIdCategoria(categoria);
                    cboSubcategories.removeAllItems();
                    queries.llenarCombosSubcategories(cboSubcategories, idCat);
                    cboSubcategories.setSelectedItem(nuevaSubcategoria);
                }
            }
        });
     
        item2.addActionListener(e -> {
            frmSubcategoriesEdit dialogo = new frmSubcategoriesEdit(this, true);
                dialogo.setVisible(true);
            
                String nuevaSubcategoria = dialogo.getSubcategoriaCreada();
            
                if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                    String categoria = (String) cboCategories.getSelectedItem();
            
                    if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                        int idCat = queries.selectIdCategoria(categoria);
                        cboSubcategories.removeAllItems();
                        queries.llenarCombosSubcategories(cboSubcategories, idCat);
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
        queries.insertStock(idProd);       
    }
    
    private void actionButtons(){
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertProduct();       
                insetPrice(obtenerId(productCode)); 
                insetStock(obtenerId(productCode));
                JOptionPane.showMessageDialog(null, "Producto registrado con exito!");
                inicializar();
                cboBrands.setSelectedIndex(0);
                cboSuppliers.setSelectedIndex(0);
                cboCategories.setSelectedIndex(0);
                cboIva.setSelectedIndex(0);
            }
        });
        
        btnMenu.addActionListener(e->{
            frmMenu menu = new frmMenu();
            menu.setVisible(true);
            this.dispose();
        });
        
        btnSales.addActionListener(e->{
            frmSalesManagement sales = new frmSalesManagement();
            sales.setVisible(true);
            this.dispose();
        });
        
        btnProduct.addActionListener(e->{
            frmProducts products = new frmProducts();
            products.setVisible(true);
            this.dispose();
        });
        
        btnCancel.addActionListener(e->{
            frmProducts products = new frmProducts();
            products.setVisible(true);
            this.dispose();
        });
        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        labelTitle1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JFormattedTextField();
        cboBrands = new javax.swing.JComboBox<>();
        cboSuppliers = new javax.swing.JComboBox<>();
        cboCategories = new javax.swing.JComboBox<>();
        cboSubcategories = new javax.swing.JComboBox<>();
        cboIva = new javax.swing.JComboBox<>();
        btnCategory = new javax.swing.JButton();
        btnSupplier = new javax.swing.JButton();
        btnBrand = new javax.swing.JButton();
        btnSubcategories = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        btnServiceTec = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HM Gestión");
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 18))); // NOI18N
        jPanel9.setForeground(new java.awt.Color(204, 204, 204));

        labelTitle1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        labelTitle1.setForeground(new java.awt.Color(101, 129, 171));
        labelTitle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle1.setText("Datos de producto nuevo");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel5.setText("Marca:");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel15.setText("Modelo:");

        txtModel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtModel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel16.setText("Color:");

        txtColor.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtColor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel17.setText("Codigo prod.");

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel18.setText("Proveedor:");

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel19.setText("Categoria");

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel20.setText("Subcategoría:");

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel9.setText("I.V.A.:");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel8.setText("Precio de costo:$");

        txtPrice.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));

        cboBrands.setBackground(new java.awt.Color(255, 255, 255));
        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboBrands.setForeground(new java.awt.Color(102, 102, 102));
        cboBrands.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
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

        cboSuppliers.setBackground(new java.awt.Color(255, 255, 255));
        cboSuppliers.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSuppliers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboSuppliers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSuppliers.setFocusable(false);

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCategories.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboCategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboCategories.setFocusable(false);

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSubcategories.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboSubcategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSubcategories.setFocusable(false);
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        cboIva.setBackground(new java.awt.Color(255, 255, 255));
        cboIva.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "\"Seleccione un %\"", "10.5", "21" }));
        cboIva.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIvaActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboBrands, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtModel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtColor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCategories, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBrand)
                    .addComponent(btnSupplier)
                    .addComponent(btnCategory)
                    .addComponent(btnSubcategories)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
        btnRegistrar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle1)
                .addGap(59, 59, 59)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(0, 0, 0, 10));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel8.setBackground(new java.awt.Color(0, 0, 0, 0));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Wallpapers/logo.jpg"))); // NOI18N
        jLabel7.setText("jLabel5");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 155, 219)));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnMenu.setBackground(new java.awt.Color(255, 255, 255));
        btnMenu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/home96.png"))); // NOI18N
        btnMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenu.setFocusPainted(false);
        btnMenu.setHideActionText(true);
        btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenuMouseExited(evt);
            }
        });
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        btnProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnProduct.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/box96.png"))); // NOI18N
        btnProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProduct.setHideActionText(true);
        btnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProductMouseExited(evt);
            }
        });

        btnSales.setBackground(new java.awt.Color(255, 255, 255));
        btnSales.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cart96.png"))); // NOI18N
        btnSales.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSales.setHideActionText(true);
        btnSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalesMouseExited(evt);
            }
        });
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });

        btnServiceTec.setBackground(new java.awt.Color(255, 255, 255));
        btnServiceTec.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnServiceTec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/STEC.png"))); // NOI18N
        btnServiceTec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnServiceTec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServiceTec.setHideActionText(true);
        btnServiceTec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnServiceTecMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnServiceTecMouseExited(evt);
            }
        });
        btnServiceTec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServiceTec, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1193, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnServiceTec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenuBar1.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N

        jMenu2.setText("Productos");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Clientes");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Servicio técnico");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Proveedores");
        jMenuBar1.add(jMenu5);

        jMenu6.setText("Administracion");
        jMenuBar1.add(jMenu6);

        jMenu7.setText("Seguridad");
        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(205, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIvaActionPerformed
        
    }//GEN-LAST:event_cboIvaActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed

    }//GEN-LAST:event_txtColorActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed

    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseEntered
        btnMenu.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnMenuMouseEntered

    private void btnMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseExited
        btnMenu.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnMenuMouseExited

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed

    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseEntered
        btnSales.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnSalesMouseEntered

    private void btnSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseExited
        btnSales.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSalesMouseExited

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed

    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnServiceTecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseEntered
        btnServiceTec.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnServiceTecMouseEntered

    private void btnServiceTecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseExited
        btnServiceTec.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnServiceTecMouseExited

    private void btnServiceTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTecActionPerformed

    }//GEN-LAST:event_btnServiceTecActionPerformed

    private void btnProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseEntered
        btnProduct.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnProductMouseEntered

    private void btnProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseExited
        btnProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnProductMouseExited

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(238,238,238));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrandActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSupplierActionPerformed

    private void btnCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCategoryActionPerformed

    private void btnSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubcategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubcategoriesActionPerformed

    private void cboBrandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBrandsActionPerformed

    }//GEN-LAST:event_cboBrandsActionPerformed
 
    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNewProduct_1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnServiceTec;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModel;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
