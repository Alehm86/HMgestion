/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.genericDAO;
import java.awt.Color;
import dao.productDAO;
import models.Products;
import models.Price;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import utils.utility;

public class productNewPanel extends javax.swing.JPanel {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    utility utils = new utility();
    
    Products product = new Products();
    Price precio = new Price();
    
    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    private String action;
    private String productCode;
    private String catPadre;
    
    private int idProduct = -1;
    
    public int getIdProduct(){
        return idProduct;
    }
    
    public productNewPanel() {
        initComponents();
        
        start();
        startMsj();
        llenarCombos();
        llenarSubcategorias();
        actions();
        popupBrand();
        popupCategories();
        popupSubcategories();
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
    
    void llenarCombos(){
        
        qGeneric.llenarCombos(cboBrands,"product_brands");
        qGeneric.llenarCombosActivos(cboCategories,"product_categories");      
    }
  
    public void start(){
        
        txtModel.setText("");
        txtProductCode.setText("");
        txtColor.setText("");       
    }
    
    private void startMsj(){
        
        lblErrorBrand.setText("");
        lblErrorModel.setText("");
        lblErrorCodeP.setText("");
        lblErrorCategory.setText("");
        lblErrorSubcategory.setText("");
    }
    
    public void clearCombos(){
        
        cboBrands.setSelectedIndex(0);
        cboSubcategories.removeAllItems();
        cboCategories.setSelectedIndex(0);     
        cboBrands.setSelectedIndex(0);
    }

    private void actions(){

        btnRegistrar.addActionListener(e-> {           
            insertProduct();
        });
       
        btnCancel.addActionListener(e->{
           start();
           clearCombos();
        });
        
        utils.clearMsjErrorCombo(cboBrands,lblErrorBrand);
        utils.clearMsjErrorCombo(cboCategories,lblErrorCategory);        
        utils.clearMsjErrorCombo(cboSubcategories,lblErrorSubcategory);       
        utils.clearMsjErrorTxt(txtModel, lblErrorModel);        
        utils.clearMsjErrorTxt(txtProductCode, lblErrorCodeP);  
        
        cboCategories.addActionListener(e -> {
            Object selected = cboCategories.getSelectedItem();

            if (selected != null) {
                catPadre = selected.toString();
            }
        });
  
    } 
    
    public int obtenerId(String codeProduct){
        
        int id = 0;
        
        id = qProduct.selectIdProduct(codeProduct);
        return id;
    }      
    
    void insertProduct(){
               
        String msjErrorTxt ="Complete el campo!";   
        String msjErrorCombo="Debe seleccionar una opción.";
        
        boolean valido = true;
        
        product.state = 1;
        precio.benefit = 0;
        precio.salesPrice= 0;
        
        if (cboBrands.getSelectedIndex() > 0) { 
            product.id_brand = qGeneric.selectId("id_brand", "product_brands", cboBrands.getSelectedItem().toString());
        } else {
            lblErrorBrand.setText(msjErrorCombo);            
            valido = false;
        }        
        
        if (cboSubcategories.getSelectedIndex() > 0) {
            product.id_subcat = qGeneric.selectId("id_subcategory","product_subcategories",
                    cboSubcategories.getSelectedItem().toString());
            } else {
                lblErrorSubcategory.setText(msjErrorCombo);                  
                valido = false;
            }
        
        if(!txtModel.getText().isEmpty()){
            product.model = txtModel.getText().toUpperCase();
        } else {
            lblErrorModel.setText(msjErrorTxt);
            valido = false;
        }
        
        if(!txtColor.getText().isEmpty()){
            product.color = txtColor.getText().toUpperCase();
        } else {
            product.color = null;
        }
        
        if(!txtProductCode.getText().isEmpty()){
            product.productCode = txtProductCode.getText().toUpperCase();
            productCode = txtProductCode.getText().toUpperCase();
        } else {
            lblErrorCodeP.setText(msjErrorTxt);
            valido = false;
        }              
        
        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }
      
        idProduct = qProduct.insertProduct(
                product.getId_subcat(),
                product.getId_brand(),                
                product.getModel(),
                product.getColor(),
                product.getProductCode(),
                product.getState()
        );        
        
        if(idProduct>0){
            qProduct.insertInitialPrice(idProduct);
            qProduct.insertInitialStock(idProduct);
            qProduct.insertProductHistory(idProduct, "Producto creado", "Sin observaciones");
        }else{
            return;
        }

        start();
        clearCombos();
    }  
    
    
    public void popupBrand(){
        JPopupMenu popupBrands = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva marca");
        JMenuItem item2 = new JMenuItem("Editar marca");
        
        item1.addActionListener(e -> {

            productBrandNewDialog dialogo = new productBrandNewDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaMarca = dialogo.getMarcaCreada();
            qGeneric.llenarCombos(cboBrands, "product_brands");

            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });

        
        item2.addActionListener(e -> {

            productBrandEditDialog dialogo = new productBrandEditDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaMarca = dialogo.getMarcaCreada();
            qGeneric.llenarCombos(cboBrands, "product_brands");

            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });
     
        Font fontMenu = new Font("Poppins", Font.PLAIN, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(12,83,151));
        item2.setForeground(new Color(12,83,151));
        
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
    
    public void popupCategories(){
        JPopupMenu popupSupplier = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva categoria");
        JMenuItem item2 = new JMenuItem("Editar categoria");
        
        item1.addActionListener(e -> {

            productCategoriesNewDialog dialogo = new productCategoriesNewDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaCategoria = dialogo.getCategoriaCreada();
            qGeneric.llenarCombosActivos(cboCategories, "product_categories");

            if (nuevaCategoria != null) {
                cboCategories.setSelectedItem(nuevaCategoria);
            }
        });

        item2.addActionListener(e -> {

            productCategoriesEditDialog dialogo = new productCategoriesEditDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaCategoria = dialogo.getCategoriaCreada();

            if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {

                cboCategories.removeAllItems();
                qGeneric.llenarCombosActivos(cboCategories, "product_categories");
                cboCategories.setSelectedItem(nuevaCategoria);
            }
        });    
        
        Font fontMenu = new Font("Poppins", Font.PLAIN, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(12,83,151));
        item2.setForeground(new Color(12,83,151));
        
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

            productSubategoriesNewDialog dialogo = new productSubategoriesNewDialog(parent, true);         
            dialogo.setCategoriaPadre(catPadre);
            dialogo.setVisible(true);

            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();

            if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                String categoria = (String) cboCategories.getSelectedItem();

                if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                    int idCat = qProduct.selectIdCategoria(categoria);
                    cboSubcategories.removeAllItems();
                    qProduct.llenarCombosSubcategories(cboSubcategories, idCat);
                    cboSubcategories.setSelectedItem(nuevaSubcategoria);
                }
            }
        });

        item2.addActionListener(e -> {

            productSubcategoriesEditDialog dialogo = new productSubcategoriesEditDialog(parent, true);
            dialogo.setCategoriaPadre(catPadre);
            dialogo.setVisible(true);

            String nuevaSubcategoria = dialogo.getSubcategoriaCreada();

            if (nuevaSubcategoria != null && !nuevaSubcategoria.isEmpty()) {
                String categoria = (String) cboCategories.getSelectedItem();

                if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                    int idCat = qProduct.selectIdCategoria(categoria);
                    cboSubcategories.removeAllItems();
                    qProduct.llenarCombosSubcategories(cboSubcategories, idCat);
                    cboSubcategories.setSelectedItem(nuevaSubcategoria);
                }
            }
        });
        
        Font fontMenu = new Font("Poppins", Font.PLAIN, 18);
        item1.setFont(fontMenu);
        item2.setFont(fontMenu);
        
        item1.setForeground(new Color(12,83,151));
        item2.setForeground(new Color(12,83,151));
        
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblErrorBrand = new javax.swing.JLabel();
        lblErrorModel = new javax.swing.JLabel();
        lblErrorCodeP = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblErrorCategory = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblErrorSubcategory = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        cboSubcategories = new javax.swing.JComboBox<>();
        cboBrands = new javax.swing.JComboBox<>();
        txtModel = new javax.swing.JTextField();
        txtProductCode = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        btnCategory = new javax.swing.JButton();
        btnBrand = new javax.swing.JButton();
        btnSubcategories = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(351, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(337, 337, 337))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Marca:");

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(35, 35, 38));
        jLabel17.setText("Codigo prod.:");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(35, 35, 38));
        jLabel15.setText("Modelo:");

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(35, 35, 38));
        jLabel16.setText("Color:");

        lblErrorBrand.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorBrand.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorBrand.setText("Mensaje error!");
        lblErrorBrand.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblErrorModel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorModel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorModel.setText("Mensaje error!");
        lblErrorModel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblErrorCodeP.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorCodeP.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorCodeP.setText("Mensaje error!");
        lblErrorCodeP.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(35, 35, 38));
        jLabel19.setText("Categoria:");

        lblErrorCategory.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorCategory.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorCategory.setText("Mensaje error!");
        lblErrorCategory.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(35, 35, 38));
        jLabel20.setText("Subcategoría:");

        lblErrorSubcategory.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorSubcategory.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorSubcategory.setText("Mensaje error!");
        lblErrorSubcategory.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cboCategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtModel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtColor.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnCategory.setBorderPainted(false);

        btnBrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnBrand.setBorderPainted(false);

        btnSubcategories.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnSubcategories.setBorderPainted(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblErrorBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblErrorModel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lblErrorCodeP, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrorCategory)
                    .addComponent(lblErrorSubcategory))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrorBrand)
                    .addComponent(lblErrorModel))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrorCodeP)
                .addGap(30, 30, 30))
        );

        jPanel3.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product64.png"))); // NOI18N
        jLabel1.setText("Datos de producto nuevo.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSubcategories;
    private javax.swing.JComboBox<String> cboBrands;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblErrorBrand;
    private javax.swing.JLabel lblErrorCategory;
    private javax.swing.JLabel lblErrorCodeP;
    private javax.swing.JLabel lblErrorModel;
    private javax.swing.JLabel lblErrorSubcategory;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
