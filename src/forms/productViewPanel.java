/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import dao.genericDAO;
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
import models.modelProducts;
import dao.productDAO;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import models.modelPrice;

public class productViewPanel extends javax.swing.JPanel {

    productDAO queriesProduct = new productDAO();
    genericDAO queriesGeneric = new genericDAO();
    
    modelProducts product = new modelProducts();
    modelPrice price = new modelPrice();

    
    private int idProduct;
    
    public void dialogoEditSearch(int idProducto){
        this.idProduct = idProducto;
        
        if(idProducto != -1){
            buscar();
            jLabel9.setVisible(false);
            txtCodProducto.setVisible(false);
            btnBuscar.setVisible(false);
            btnSearchList.setVisible(false);
        }else{
            
        }    
    }
  
    public productViewPanel() {
        initComponents();
        
        inicializar();
        startMsj();
        OffObjets();
        llenarCombos();
        actionPromotions();
        
        popupBrand();
        popupSubcategories();
        
        txtBrand.setEditable(false);
        txtSubcategory.setEditable(false);
        lbl_iva.setVisible(false);
        
        btnEdit.setEnabled(false);
               
        actions();  
        
    }
    
    private void inicializar(){
        
        txtSubcategory.setVisible(false);
        txtBrand.setVisible(false);
        txtCategory.setVisible(false);
        txtSubcategory.setVisible(false);
        txtBrand.setVisible(false);
        
        btnActivePromo.setVisible(false);
        cboPromotion.setVisible(false);
        btnConfirmPromo.setVisible(false);
        btnConfirmPromo.setEnabled(false);
    }
    
    private void llenarCombos(){
        
        queriesGeneric.llenarCombos(cboBrands,"brands");  
        queriesGeneric.llenarCombosActivos(cboCategories,"categories");
        queriesGeneric.llenarCombosActivos(cboPromotion,"product_promotions");        
    }   
    
    private void actionPromotions(){

        btnActivePromo.addActionListener(e->{
            cboPromotion.setVisible(true);
            btnConfirmPromo.setVisible(true);
        });
        
        cboPromotion.addActionListener(e->{
            btnConfirmPromo.setEnabled(true);
        });        
        
        btnConfirmPromo.addActionListener(e->{
            //ACTUALIZA LA PROMOCIÓN
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma activar promoción?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            if (cboPromotion.getSelectedIndex() > 0) { 
                String namePromo = cboPromotion.getSelectedItem().toString();  
                product.id_promotion = queriesGeneric.selectId("id_promotion","product_promotions",namePromo); 
                queriesProduct.updateProductPromotion(idProduct, product.getId_promotion());
            }
            
            btnActivePromo.setVisible(false);
            cboPromotion.setVisible(false);
            btnConfirmPromo.setVisible(false);
            
            buscar();
        });
        
        btnCancelPromo.addActionListener(e->{
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma terminar promoción?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            queriesProduct.updateProductPromotion(Integer.parseInt(lbl_id.getText().trim()), null);
            
            buscar();
            
            cboPromotion.setSelectedIndex(0);                                  
        });        
    }

    private void llenarSubcategorias(){

        String categoria = (String) cboCategories.getSelectedItem();
        int idCat = queriesProduct.selectIdCategoria(categoria);
        cboSubcategories.removeAllItems();
        queriesProduct.llenarCombosSubcategories(cboSubcategories, idCat);          
    }    
    
    private void actions(){
        
        queriesGeneric.clearMsjErrorTxt(txtModel, lblErrorModel);        
        queriesGeneric.clearMsjErrorTxt(txtProductCode, lblErrorCodeP);  
        queriesGeneric.clearMsjErrorTxt(txtColor, lblErrorColor);
        
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();  
            }
            
        });
        
        btnBuscar.addActionListener(e -> {
            
            idProduct = queriesProduct.selectIdProduct(txtCodProducto.getText());
            buscar();           
        });         
        
        btnEdit.addActionListener(e->{           
            OnObjets();
        });
        
        btnCancel.addActionListener(e->{
            
            buscar();
            OffObjets(); 
        });   
        
        btnSearchList.addActionListener(e -> {
            
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            productSearchDialog dialogo = new productSearchDialog(parent, true);            
            
            dialogo.setVisible(true); 
            
            idProduct = dialogo.getProduct();
            buscar();        
        });              
        
        txtBenefit.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calcularPrecioSugerido();
            }
        });
        
        cboIva.addActionListener(e->{
            calcularPrecioSugerido();
        });               
        
        cboBrands.addActionListener(e -> {
            txtBrand.setText(cboBrands.getSelectedItem().toString());
        });                      
        
        cboSubcategories.addActionListener(e -> {          
            if (cboSubcategories.getItemCount() == 0 || cboSubcategories.getSelectedItem() == null) {               
                txtSubcategory.setText("");
            }else{
                txtSubcategory.setText(cboSubcategories.getSelectedItem().toString());
            }
        }); 
        
    }
    
    private void startMsj(){
        
        lblErrorModel.setText("");
        lblErrorColor.setText("");
        lblErrorCodeP.setText("");
        lblErrorStock.setText("");
        lblStock.setText("");
        lblPrecioSujerido.setText("");
        lblEnPromo.setText("");
    }
    
    private void OffObjets(){
        
        //DATOS DE PRODUCTO
        
        cboBrands.setEnabled(false);
        cboSubcategories.setEnabled(false);
        
        txtModel.setEditable(false);
        txtColor.setEditable(false);
        txtProductCode.setEditable(false);
        
        btnBrand.setEnabled(false);
        btnSubcategories.setEnabled(false);
        
        cboCategories.setEnabled(false);
        btnCategory.setEnabled(false);
        
        btnCancel.setVisible(false);
        btnRegistrar.setVisible(false);
        
        //DATOS DE STOCK
        
        txtMin.setEditable(false);
        
        //DATOS DE PRECIO
        
        txtPrecioCosto.setEditable(false);
        txtBenefit.setEditable(false);
        txtSalePrice.setEditable(false);
        cboIva.setEnabled(false);
        jLabel20.setVisible(false);
        lblPrecioSujerido.setVisible(false);
        
        //promocion
        btnCancelPromo.setVisible(false); 
    }
    
    private void OnObjets(){
        
        cboBrands.setEnabled(true);
        cboSubcategories.setEnabled(true);
        
        txtModel.setEditable(true);
        txtColor.setEditable(true);
        txtProductCode.setEditable(true);
        
        btnBrand.setEnabled(true);
        btnSubcategories.setEnabled(true);
        
        cboCategories.setEnabled(true);
        btnCategory.setEnabled(true);
        
        btnCancel.setVisible(true);
        btnRegistrar.setVisible(true);
        
        //datos de stock
        txtMin.setEditable(true);
        cboIva.setEnabled(true);
        
        //datos de precio
        txtPrecioCosto.setEditable(true);
        txtBenefit.setEditable(true);
        txtSalePrice.setEditable(true);
        cboIva.setEnabled(true);
        
    }
        
    private void limpiar(){
        
        cboBrands.setSelectedIndex(0);
        cboSubcategories.removeAllItems();   
        cboBrands.setSelectedIndex(0);
        txtCodProducto.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtProductCode.setText("");
        txtSubcategory.setText("");
        txtColor.setText("");       
    } 
    
    private void buscar(){
        
        clear();
        queriesProduct.selectProductEdit(idProduct, lbl_id, txtCategory, txtSubcategory, txtBrand, txtModel, txtColor, txtProductCode, lblEnPromo);
        queriesProduct.selectAllTableStock(idProduct, lblStock, txtMin);
        queriesProduct.selectProductPriceEdit(idProduct, txtPrecioCosto, txtBenefit, lbl_iva, txtSalePrice);
        
        infoComboSelected();
        
        colorLabelStock();
        
        if(!txtProductCode.getText().isEmpty()){
            btnEdit.setEnabled(true);         
        }
        
        if(!lblEnPromo.getText().equals("Sin promoción")){
            btnCancelPromo.setVisible(true);
            infoComboPromo();          
        }else{
            btnActivePromo.setVisible(true);
        }
  
    }    

    private void updateProduct(){
        
        String msjErrorTxt ="Complete el campo!";           
        Boolean valido = true;
        product.state = 1;  
        int stockMin = -1;
        
        if(!txtModel.getText().isEmpty()){
            product.model = txtModel.getText().toUpperCase();
        } else {
            lblErrorModel.setText(msjErrorTxt);
            valido = false;
        }

        if(!txtColor.getText().isEmpty()){
            product.color = txtColor.getText().toUpperCase();
        } else {
            lblErrorColor.setText(msjErrorTxt);
            valido = false;
        }        
        
        if(!txtProductCode.getText().isEmpty()){
            product.productCode = txtProductCode.getText().toUpperCase();
        } else {
            lblErrorCodeP.setText(msjErrorTxt);
            valido = false;
        }  
        
        if(!txtMin.getText().isEmpty()){
            stockMin = Integer.parseInt(txtMin.getText());
        } else {
            lblErrorStock.setText(msjErrorTxt);
            valido = false;
        }         
                 
        product.id_brand = queriesGeneric.selectId("id_brand","brands",txtBrand.getText());
        product.id_subcat = queriesGeneric.selectId("id_subcategory","subcategories",txtSubcategory.getText());  
               
        price.price =  Double.parseDouble(txtPrecioCosto.getText());           
        price.benefit = Double.parseDouble(txtBenefit.getText());
        price.salesPrice = Double.parseDouble(txtSalePrice.getText());
        price.iva = Double.parseDouble(cboIva.getSelectedItem().toString());      
        
        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma la edición?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }        
        
        //ACTUALIZA DATOS DE PRODUCTO
        queriesProduct.updateProduct(
            idProduct,
            product.getId_subcat(),
            product.getId_brand(),
            product.getModel(),
            product.getColor(),
            product.getProductCode(), 
            product.getState()
        );
        
        //ACTUALIZA ALARMA DE STOCK MINIMO
        queriesProduct.updateAlarmStockMin(idProduct, stockMin);
        
        //ACTUALIZA DATOS DEL PRECIO
        queriesProduct.updatePriceProduct(idProduct, price.getPrice(), price.getBenefit(), price.getSalesPrice(), price.getIva());       
               
        OffObjets();
        
    }
    
    private void colorLabelStock(){
        
        int stock = Integer.parseInt(lblStock.getText());
        int minimo = Integer.parseInt(txtMin.getText());


        if (stock < minimo) {
            lblStock.setForeground(Color.RED);
        } else if (stock == minimo) {
            lblStock.setForeground(Color.ORANGE);
        } else {
            lblStock.setForeground(Color.GREEN);
        }
        
    }
    
    private void infoComboSelected() {

        double ivaDB = Double.parseDouble(lbl_iva.getText().trim());

        for (int i = 0; i < cboIva.getItemCount(); i++) {

            String item = cboIva.getItemAt(i).toString()
                            .replace("%", "")
                            .replace(",", ".")
                            .trim();

            double ivaCombo = Double.parseDouble(item);

            if (ivaCombo == ivaDB) {
                cboIva.setSelectedIndex(i);
                break;
            }
        }
        
        String brand = txtBrand.getText();
        
        for (int i = 0; i < cboBrands.getItemCount(); i++) {
            if (cboBrands.getItemAt(i).toString().equalsIgnoreCase(brand)) {
                cboBrands.setSelectedIndex(i);
                break;
            }
        }
        
        String cat = txtCategory.getText();
        String subcat = txtSubcategory.getText();

        for (int i = 0; i < cboCategories.getItemCount(); i++) {
            if (cboCategories.getItemAt(i).toString().equalsIgnoreCase(cat)) {
                cboCategories.setSelectedIndex(i);

                llenarSubcategorias();

                for (int j = 0; j < cboSubcategories.getItemCount(); j++) {
                    if (cboSubcategories.getItemAt(j).toString().equalsIgnoreCase(subcat)) {
                        cboSubcategories.setSelectedIndex(j);
                        break;
                    }
                }

                break;
            }
        }
    }
    
    public void infoComboPromo(){
        String promo = lblEnPromo.getText().trim();

        for (int i = 0; i < cboPromotion.getItemCount(); i++) {
            if (cboPromotion.getItemAt(i).toString().equalsIgnoreCase(promo)) {
                cboPromotion.setSelectedIndex(i);
                break;
            }
        } 
       
    }       
    
    private void clear(){  
        
        txtCodProducto.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtColor.setText("");
        txtProductCode.setText("");
        txtSubcategory.setText(""); 
        
        btnActivePromo.setVisible(false);
        btnCancelPromo.setVisible(false);
        cboPromotion.setVisible(false);
        btnConfirmPromo.setVisible(false);
        btnConfirmPromo.setEnabled(false);
    }  
    
    private void calcularPrecioSugerido(){    
        
        if(txtPrecioCosto.getText().trim().isEmpty() || txtBenefit.getText().trim().isEmpty()){
            lblPrecioSujerido.setText("$ 0");
            return;
        }
        
        lblPrecioSujerido.setText(queriesProduct.calcularPrecioSugerido(
                txtPrecioCosto.getText().trim(), 
                cboIva.getSelectedItem().toString(), 
                txtBenefit.getText().trim())
        );
        jLabel20.setVisible(true);
        lblPrecioSujerido.setVisible(true);
    }
    
    public void popupBrand(){
        JPopupMenu popupBrands = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva marca");
        JMenuItem item2 = new JMenuItem("Editar marca");
        
        item1.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            productBrandNewDialog dialogo = new productBrandNewDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaMarca = dialogo.getMarcaCreada();
            queriesGeneric.llenarCombos(cboBrands, "brands");

            if (nuevaMarca != null) {
                cboBrands.setSelectedItem(nuevaMarca);
            }
        });

        
        item2.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            productBrandEditDialog dialogo = new productBrandEditDialog(parent, true);
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
    
    public void popupCategories(){
        JPopupMenu popupSupplier = new JPopupMenu();
        
        JMenuItem item1 = new JMenuItem("Nueva categoria");
        JMenuItem item2 = new JMenuItem("Editar categoria");
        
        item1.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            productCategoriesNewDialog dialogo = new productCategoriesNewDialog(parent, true);
            dialogo.setVisible(true);

            String nuevaCategoria = dialogo.getCategoriaCreada();
            queriesGeneric.llenarCombosActivos(cboCategories, "categories");

            if (nuevaCategoria != null) {
                cboCategories.setSelectedItem(nuevaCategoria);
            }
        });    

        item2.addActionListener(e -> {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            productCategoriesEditDialog dialogo = new productCategoriesEditDialog(parent, true);
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

            productSubategoriesNewDialog dialogo = new productSubategoriesNewDialog(parent, true);
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

            productSubcategoriesEditDialog dialogo = new productSubcategoriesEditDialog(parent, true);
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
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        txtCodProducto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnSearchList = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnActivePromo = new javax.swing.JButton();
        cboPromotion = new javax.swing.JComboBox<>();
        btnCancelPromo = new javax.swing.JButton();
        btnConfirmPromo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        cboBrands = new javax.swing.JComboBox<>();
        btnBrand = new javax.swing.JButton();
        txtColor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblErrorColor = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        btnSubcategories = new javax.swing.JButton();
        txtBrand = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblErrorModel = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        lblErrorCodeP = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSubcategory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtMin = new javax.swing.JTextField();
        lblErrorStock = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtPrecioCosto = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cboIva = new javax.swing.JComboBox<>();
        lbl_iva = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtBenefit = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        lblPrecioSujerido = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSalePrice = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        lblEnPromo = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        cboCategories = new javax.swing.JComboBox<>();
        btnCategory = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(12, 83, 151));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnBuscar.setBorderPainted(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setDefaultCapable(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFocusable(false);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        txtCodProducto.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        txtCodProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));
        txtCodProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodProductoKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/barCode48.png"))); // NOI18N
        jLabel9.setText("Codigo de producto");

        btnSearchList.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchlist32.png"))); // NOI18N
        btnSearchList.setBorder(null);
        btnSearchList.setBorderPainted(false);
        btnSearchList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchList.setFocusPainted(false);
        btnSearchList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchListMouseExited(evt);
            }
        });
        btnSearchList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchListActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(12, 83, 151));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit_121852.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnEdit.setBorderPainted(false);
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.setDefaultCapable(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setFocusable(false);
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

        btnActivePromo.setBackground(new java.awt.Color(255, 204, 0));
        btnActivePromo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnActivePromo.setForeground(new java.awt.Color(255, 255, 255));
        btnActivePromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/promo.png"))); // NOI18N
        btnActivePromo.setText("Activar promo!");
        btnActivePromo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnActivePromo.setBorderPainted(false);
        btnActivePromo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActivePromo.setDefaultCapable(false);
        btnActivePromo.setFocusPainted(false);
        btnActivePromo.setFocusable(false);
        btnActivePromo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnActivePromoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnActivePromoMouseExited(evt);
            }
        });
        btnActivePromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivePromoActionPerformed(evt);
            }
        });

        cboPromotion.setBackground(new java.awt.Color(255, 255, 255));

        btnCancelPromo.setBackground(new java.awt.Color(204, 102, 0));
        btnCancelPromo.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCancelPromo.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelPromo.setText("Terminar promoción.");

        btnConfirmPromo.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirmPromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirmPromo.setBorder(null);
        btnConfirmPromo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmPromo.setFocusPainted(false);
        btnConfirmPromo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmPromoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmPromoMouseExited(evt);
            }
        });
        btnConfirmPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmPromoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(6, 6, 6)
                .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActivePromo, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirmPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActivePromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

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
        btnRegistrar.setText("Editar");
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
                .addGap(539, 539, 539)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Datos de producto.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 24), new java.awt.Color(101, 129, 171))); // NOI18N

        cboBrands.setBackground(new java.awt.Color(255, 255, 255));
        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboBrands.setBorder(null);
        cboBrands.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboBrands.setFocusable(false);
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

        txtColor.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtColor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setText("Color:");

        lblErrorColor.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorColor.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorColor.setText("Mensaje error!");
        lblErrorColor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

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

        txtBrand.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtBrand.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBrand.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbl_id.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_id.setForeground(new java.awt.Color(101, 129, 171));
        lbl_id.setText("0");

        txtModel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtModel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtModel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setText("Modelo:");

        lblErrorModel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorModel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorModel.setText("Mensaje error!");
        lblErrorModel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        txtProductCode.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtProductCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProductCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setText("Codigo producto:");

        lblErrorCodeP.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorCodeP.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorCodeP.setText("Mensaje error!");
        lblErrorCodeP.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel14.setText("Subcategoría:");

        txtSubcategory.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtSubcategory.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSubcategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(101, 129, 171));
        jLabel12.setText("Id producto:");

        jLabel21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel21.setText("Marca:");

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(101, 129, 171));
        jLabel19.setText("Cantidad en stock:");

        lblStock.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblStock.setText("xxx");

        jPanel5.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setText("Alarma stock minímo:");

        txtMin.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtMin.setForeground(new java.awt.Color(255, 102, 0));
        txtMin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblErrorStock.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorStock.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorStock.setText("Mensaje error!");
        lblErrorStock.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setText("Precio de costo:");

        txtPrecioCosto.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtPrecioCosto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtPrecioCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioCostoKeyPressed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/%.png"))); // NOI18N
        jLabel15.setText("IVA:");
        jLabel15.setToolTipText("");
        jLabel15.setFocusable(false);
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cboIva.setBackground(new java.awt.Color(255, 255, 255));
        cboIva.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "21", "10.5" }));
        cboIva.setBorder(null);
        cboIva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboIva.setFocusable(false);
        cboIva.setMinimumSize(new java.awt.Dimension(70, 22));
        cboIva.setPreferredSize(new java.awt.Dimension(70, 22));
        cboIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIvaActionPerformed(evt);
            }
        });

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva.setText("xxx");

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/%.png"))); // NOI18N
        jLabel17.setText("Beneficio:");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        txtBenefit.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtBenefit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBenefit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtBenefit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBenefitKeyPressed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(12, 83, 151));
        jLabel20.setText("Precio Sujerido:");

        lblPrecioSujerido.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblPrecioSujerido.setForeground(new java.awt.Color(0, 204, 0));
        lblPrecioSujerido.setText("xxx");

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ProductPrice.png"))); // NOI18N
        jLabel18.setText("Precio de venta:");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        txtSalePrice.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtSalePrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSalePrice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtSalePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalePriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrecioSujerido)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrecioSujerido, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel23.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(101, 129, 171));
        jLabel23.setText("En promoción:");

        lblEnPromo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblEnPromo.setForeground(new java.awt.Color(255, 153, 0));
        lblEnPromo.setText("xxx");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel10.setText("Categoria:");

        txtCategory.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtCategory.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStock, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEnPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 291, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboBrands, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrand))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCategory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblErrorCodeP, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblErrorModel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSubcategories)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(lblErrorColor, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStock, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEnPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorCodeP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrorColor, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 254, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

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

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed

    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrandActionPerformed

    private void btnSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubcategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubcategoriesActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed

    }//GEN-LAST:event_txtColorActionPerformed

    private void txtCodProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            idProduct = queriesProduct.selectIdProduct(txtCodProducto.getText());
            buscar(); 
        }     
    }//GEN-LAST:event_txtCodProductoKeyPressed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        btnEdit.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited

    private void cboIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIvaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIvaActionPerformed

    private void btnSearchListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchListMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchListMouseEntered

    private void btnSearchListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchListMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchListMouseExited

    private void btnSearchListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchListActionPerformed

    private void txtSalePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalePriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalePriceActionPerformed

    private void txtPrecioCostoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCostoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calcularPrecioSugerido();
        }
    }//GEN-LAST:event_txtPrecioCostoKeyPressed

    private void txtBenefitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calcularPrecioSugerido();
        }
    }//GEN-LAST:event_txtBenefitKeyPressed

    private void btnCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCategoryActionPerformed

    private void btnActivePromoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActivePromoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActivePromoMouseEntered

    private void btnActivePromoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActivePromoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActivePromoMouseExited

    private void btnActivePromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivePromoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActivePromoActionPerformed

    private void btnConfirmPromoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmPromoMouseEntered

    }//GEN-LAST:event_btnConfirmPromoMouseEntered

    private void btnConfirmPromoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmPromoMouseExited

    }//GEN-LAST:event_btnConfirmPromoMouseExited

    private void btnConfirmPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmPromoActionPerformed

    }//GEN-LAST:event_btnConfirmPromoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivePromo;
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelPromo;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnConfirmEdit;
    private javax.swing.JButton btnConfirmPromo;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSearchList;
    private javax.swing.JButton btnSubcategories;
    private javax.swing.JComboBox<String> cboBrands;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboIva;
    private javax.swing.JComboBox<String> cboPromotion;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblEnPromo;
    private javax.swing.JLabel lblErrorCodeP;
    private javax.swing.JLabel lblErrorColor;
    private javax.swing.JLabel lblErrorModel;
    private javax.swing.JLabel lblErrorStock;
    private javax.swing.JLabel lblPrecioSujerido;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JTextField txtBenefit;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtMin;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPrecioCosto;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtSalePrice;
    private javax.swing.JTextField txtSubcategory;
    // End of variables declaration//GEN-END:variables
}
