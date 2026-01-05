/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forms;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class frmEditProduct extends javax.swing.JFrame {
    ProductDAO queries = new ProductDAO();
    modelProducts product = new modelProducts();
  
    fondoPanel background = new fondoPanel();

    private int idProducto;
    
    public void dialogoEdit(int idProducto){
        this.idProducto = idProducto;
        
        if(idProducto != -1){
            inicializar();
        }else{
            
        }    
    }
   
    public frmEditProduct() {
        
        this.setContentPane(background);
        initComponents();
        this.setExtendedState(6);
        
        llenarCombos();
        llenarSubcategorias();
        urlButtons();
        actionCombobox();
        
        txtBrand.setEditable(false);
        txtSupplier.setEditable(false);
        txtCategory.setEditable(false);
        txtSubcategory.setEditable(false);
       
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertProduct();   
                inicializar();
            }
        });
        
        btnBuscar.addActionListener(e -> {
            buscar();         
        });
    }
    
    private void actionCombobox(){
        
        cboBrands.addActionListener(e -> {
            txtBrand.setText(cboBrands.getSelectedItem().toString());
        });
        
        
        cboSuppliers.addActionListener(e -> {
            txtSupplier.setText(cboSuppliers.getSelectedItem().toString());
        });
        
        cboCategories.addActionListener(e -> {
            txtCategory.setText(cboCategories.getSelectedItem().toString());
        });
        
        cboSubcategories.addActionListener(e -> {          
            if (cboSubcategories.getItemCount() == 0 || cboSubcategories.getSelectedItem() == null) {               
                txtSubcategory.setText("");
            }else{
                txtSubcategory.setText(cboSubcategories.getSelectedItem().toString());
            }
        });
        
    }
    
    private void urlButtons(){
        
//        btnBrand.addActionListener(e -> {
//            frmBrandManagement dialogo = new frmBrandManagement(this, true);
//            dialogo.setVisible(true);
//
//            cboBrands.removeAllItems();
//            queries.llenarCombos(cboBrands,"brands");
//            
//            String nuevaMarca = dialogo.getMarcaCreada();
//            if (nuevaMarca != null) {
//                cboBrands.setSelectedItem(nuevaMarca);
//            }          
//        });
        
//        btnCategory.addActionListener(e -> {
//            frmCategoriesAndSubcategoriesMgmt dialogo = new frmCategoriesAndSubcategoriesMgmt(this, true);
//            dialogo.setVisible(true);
//
//            cboCategories.removeAllItems();
//            queries.llenarCombos(cboCategories,"categories");
//            
//            String nuevaCategoria = dialogo.getCategoriaCreada();
//            if (nuevaCategoria != null) {
//                cboCategories.setSelectedItem(nuevaCategoria);
//            }          
//        });
        
//        btnSubcategory.addActionListener(e -> {
//            frmCategoriesAndSubcategoriesMgmt dialogo = new frmCategoriesAndSubcategoriesMgmt(this, true);
//            dialogo.setVisible(true);
//            
//            cboSubcategories.removeAllItems();
//            
//            String categoria = (String) cboCategories.getSelectedItem();
//
//            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
//                int idCat = queries.idCategoria(categoria);
//                cboSubcategories.removeAllItems();
//                queries.llenarCombosSubcategories(cboSubcategories, idCat);          
//            }
//            
//            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();
//            if (nuevaSubcategoria != null) {
//                cboSubcategories.setSelectedItem(nuevaSubcategoria);
//            }          
//        });
        
//        btnSupplier.addActionListener(e -> {
//            frmSupplierManagement dialogo = new frmSupplierManagement(this, true);
//            dialogo.setVisible(true);
//
//            cboSuppliers.removeAllItems();
//            queries.llenarCombos(cboSuppliers,"suppliers");
//            
//            String nuevaSupplier = dialogo.getProveedorCreado();
//            if (nuevaSupplier != null) {
//                cboSuppliers.setSelectedItem(nuevaSupplier);
//            }          
//        });
        
        btnCancel.addActionListener(e -> {
            frmProducts products = new frmProducts();
            products.setVisible(true);
            this.dispose();
        });
        
        btnMenu.addActionListener(e -> {
            frmMenu menu = new frmMenu();
            menu.setVisible(true);
            this.dispose();
        });
        
        btnSales.addActionListener(e -> {
            frmSalesManagement sales = new frmSalesManagement();
            sales.setVisible(true);
            this.dispose();
        });
        
        btnProduct.addActionListener(e -> {
            frmProducts products = new frmProducts();
            products.setVisible(true);
            this.dispose();
        });
    }
    
    private void llenarSubcategorias(){
        cboCategories.addActionListener(e -> {
            String categoria = (String) cboCategories.getSelectedItem();

            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                int idCat = queries.idCategoria(categoria);
                cboSubcategories.removeAllItems();
                queries.llenarCombosSubcategories(cboSubcategories, idCat);          
            }
        });
    }
    
    private void llenarCombos(){
        queries.llenarCombos(cboBrands,"brands");
        queries.llenarCombosActivos(cboCategories,"categories");
        queries.llenarCombos(cboSuppliers,"suppliers");
    }
    
    private void inicializar(){      
        queries.selectProductEdit(idProducto, txtSubcategory, txtBrand, txtSupplier, txtCategory, txtModel, txtColor, txtProductCode);
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
    
    private void insertProduct(){
        int id = idProducto;
        product.state = 1;   
        product.id_brand = queries.obtenerId("id_brand","brands",txtBrand.getText());
        product.id_supplier = queries.obtenerId("id_supplier","suppliers",txtSupplier.getText());
        product.id_category = queries.obtenerId("id_category","categories",txtCategory.getText());
        product.id_subcat = queries.obtenerId("id_subcategory","subcategories",txtSubcategory.getText());       
        product.model = txtModel.getText().toUpperCase();
        product.color = txtColor.getText().toUpperCase();
        product.productCode = txtProductCode.getText().toUpperCase();

        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Confirma el registro del producto?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );                   
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        queries.editProduct(
            id,
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
    
    private void buscar(){
        clear();
        int id = queries.obtenerIdProduct(txtCodProducto.getText());
        queries.selectProductEdit(id, txtSubcategory, txtBrand, txtSupplier, txtCategory, txtModel, txtColor, txtProductCode);      
    }
    
    private void clear(){   
        txtBrand.setText("");
        txtModel.setText("");
        txtColor.setText("");
        txtProductCode.setText("");
        txtSupplier.setText("");
        txtCategory.setText("");
        txtSubcategory.setText("");
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnServiceTec = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtBrand = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtSubcategory = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        cboBrands = new javax.swing.JComboBox<>();
        btnBrand = new javax.swing.JButton();
        cboSuppliers = new javax.swing.JComboBox<>();
        btnSupplier = new javax.swing.JButton();
        btnCategory = new javax.swing.JButton();
        cboCategories = new javax.swing.JComboBox<>();
        cboSubcategories = new javax.swing.JComboBox<>();
        btnSubcategory = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtCodProducto = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
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

        jPanel2.setBackground(new java.awt.Color(0, 0, 0, 10));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0, 0));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Wallpapers/logo.jpg"))); // NOI18N
        jLabel12.setText("jLabel5");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 155, 219)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnServiceTec.setBackground(new java.awt.Color(255, 255, 255));
        btnServiceTec.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnServiceTec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/STEC.png"))); // NOI18N
        btnServiceTec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnServiceTec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServiceTec.setHideActionText(true);
        btnServiceTec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTecActionPerformed(evt);
            }
        });

        btnMenu.setBackground(new java.awt.Color(255, 255, 255));
        btnMenu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/home96.png"))); // NOI18N
        btnMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenu.setFocusPainted(false);
        btnMenu.setHideActionText(true);
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
        btnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductActionPerformed(evt);
            }
        });

        btnSales.setBackground(new java.awt.Color(255, 255, 255));
        btnSales.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cart96.png"))); // NOI18N
        btnSales.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSales.setHideActionText(true);
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServiceTec, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnServiceTec, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 18))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));

        labelTitle.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(13, 155, 219));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("Datos de producto a editar.");

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32_1.png"))); // NOI18N
        btnRegistrar.setText("Editar");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setMaximumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setMinimumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 52));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Cancel32.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel5.setText("Marca:");

        txtBrand.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtBrand.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel6.setText("Modelo:");

        txtModel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtModel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel7.setText("Color:");

        txtColor.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtColor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel11.setText("Codigo prod.");

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel13.setText("Proveedor:");

        txtSupplier.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSupplier.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel10.setText("Categoria");

        txtCategory.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCategory.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel14.setText("Subcategoría:");

        txtSubcategory.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSubcategory.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtProductCode, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                .addComponent(txtColor, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                .addComponent(txtModel, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                .addComponent(txtBrand))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(147, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        cboBrands.setBackground(new java.awt.Color(255, 255, 255));
        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboBrands.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboBrands.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboBrands.setFocusable(false);
        cboBrands.setMinimumSize(new java.awt.Dimension(70, 22));
        cboBrands.setPreferredSize(new java.awt.Dimension(70, 22));
        cboBrands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBrandsActionPerformed(evt);
            }
        });

        btnBrand.setBackground(new java.awt.Color(242, 242, 242));
        btnBrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add48.png"))); // NOI18N
        btnBrand.setBorder(null);
        btnBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrandActionPerformed(evt);
            }
        });

        cboSuppliers.setBackground(new java.awt.Color(255, 255, 255));
        cboSuppliers.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSuppliers.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboSuppliers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSuppliers.setFocusable(false);

        btnSupplier.setBackground(new java.awt.Color(242, 242, 242));
        btnSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add48.png"))); // NOI18N
        btnSupplier.setBorder(null);
        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierActionPerformed(evt);
            }
        });

        btnCategory.setBackground(new java.awt.Color(242, 242, 242));
        btnCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add48.png"))); // NOI18N
        btnCategory.setBorder(null);
        btnCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoryActionPerformed(evt);
            }
        });

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCategories.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboCategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboCategories.setFocusable(false);

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSubcategories.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboSubcategories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSubcategories.setFocusable(false);
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        btnSubcategory.setBackground(new java.awt.Color(242, 242, 242));
        btnSubcategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add48.png"))); // NOI18N
        btnSubcategory.setBorder(null);
        btnSubcategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubcategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubcategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(141, 141, 141)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setText("Codigo de producto:");

        txtCodProducto.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        txtCodProducto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32N.png"))); // NOI18N
        btnBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setDefaultCapable(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFocusable(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(352, 352, 352)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
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
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(339, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrandActionPerformed
    //    frmNewEditBrand brand = new frmNewEditBrand();
    //   brand.setVisible(true);       
    }//GEN-LAST:event_btnBrandActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed
   
    }//GEN-LAST:event_txtColorActionPerformed

    private void btnServiceTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTecActionPerformed
       
    }//GEN-LAST:event_btnServiceTecActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductActionPerformed
        
    }//GEN-LAST:event_btnProductActionPerformed

    private void btnSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierActionPerformed

    }//GEN-LAST:event_btnSupplierActionPerformed

    private void btnCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryActionPerformed

    }//GEN-LAST:event_btnCategoryActionPerformed

    private void btnSubcategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubcategoryActionPerformed

    }//GEN-LAST:event_btnSubcategoryActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
 
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed
        
    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void cboBrandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBrandsActionPerformed
       
    }//GEN-LAST:event_cboBrandsActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

    }//GEN-LAST:event_btnBuscarActionPerformed
 
    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmEditProduct().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnServiceTec;
    private javax.swing.JButton btnSubcategory;
    private javax.swing.JButton btnSupplier;
    private javax.swing.JComboBox<String> cboBrands;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JComboBox<String> cboSuppliers;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtSubcategory;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
