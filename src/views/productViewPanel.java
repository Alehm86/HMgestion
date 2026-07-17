/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

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
import models.mProducts;
import dao.productDAO;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import models.mPrice;
import utils.utility;

public class productViewPanel extends javax.swing.JPanel {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    utility utils = new utility();
    
    mProducts product = new mProducts();
    mPrice price = new mPrice();

    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    private int idProduct;
    private String catPadre;
    
    public void dialogoEditSearch(int idProducto){
        this.idProduct = idProducto;
        
        if(idProducto != -1){
            buscar();
            panelSearchCode.setVisible(false);
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
        leyendaBotones();
        
        popupBrand();
        popupSubcategories();
             
        actions();   
        
    } 
    
    private void inicializar(){
        
        txtBrand.setEditable(false);
        txtSubcategory.setEditable(false);
        lbl_iva.setVisible(false);
        
        lbl_discount_percentage.setVisible(false);
        lbl_discount_percentage.setText("0");
        
        btnEdit.setEnabled(false);
        btnHistory.setEnabled(false);
        
        cboIva.addItem("21");
        cboIva.addItem("10.5");
        
        txtImpCred.setFocusable(false);
        txtImpDeb.setFocusable(false);
        txtRecPlat.setFocusable(false);
        txt3C.setFocusable(false);
        txt6C.setFocusable(false);
        
    }
    
    private void leyendaBotones(){
        
        btnEdit.setToolTipText("Editar.");
        btnActivePromo.setToolTipText("Activar promo!");
        btnHistory.setToolTipText("Ver historial.");
        btnCancelPromo.setToolTipText("Terminar promoción.");
        btnBuscar.setToolTipText("Buscar producto por codigo.");
        btnSearchList.setToolTipText("Buscar producto en lista.");
        btnBrand.setToolTipText("Gestionar marca.");
        btnCategory.setToolTipText("Gestionar categorias.");
        btnSubcategories.setToolTipText("Gestionar subcategorias.");
        btnConfirmBajaAlta.setToolTipText("Confirmar.");
    }
    
    private void llenarCombos(){
        
        qGeneric.llenarCombos(cboBrands,"product_brands");  
        qGeneric.llenarCombosActivos(cboCategories,"product_categories");
        qGeneric.llenarCombosActivos(cboPromotion,"product_promotions");   
        
        cboMotivoBaja.addItem("Seleccione una opción");
        cboMotivoBaja.addItem("Sin stock permanente");
        cboMotivoBaja.addItem("Producto discontinuado por proveedor");
        cboMotivoBaja.addItem("Cambio de modelo (reemplazado por otro)");
        cboMotivoBaja.addItem("No se vende");
        cboMotivoBaja.addItem("Baja rotación");
        cboMotivoBaja.addItem("No es rentable");
        cboMotivoBaja.addItem("Error en carga (producto duplicado o mal creado");
        cboMotivoBaja.addItem("Datos incorrectos");
        cboMotivoBaja.addItem("Producto obsoleto");
        cboMotivoBaja.addItem("Decisión manual del administrador");
        cboMotivoBaja.addItem("Reorganización del catálogo");
        cboMotivoBaja.addItem("Unificación de productos");
        
        cboMotivoAlta.addItem("Seleccione una opción");
        cboMotivoAlta.addItem("Ingreso de nuevo stock");
        cboMotivoAlta.addItem("Vuelve a estar disponible el proveedor");
        cboMotivoAlta.addItem("Se repone producto discontinuado temporalmente");
        cboMotivoAlta.addItem("Vuelve la demanda");
        cboMotivoAlta.addItem("Se decide volver a venderlo");
        cboMotivoAlta.addItem("Nueva estrategia de ventas");
        cboMotivoAlta.addItem("Corrección de error de carga");
        cboMotivoAlta.addItem("Producto dado de baja por equivocación");
        cboMotivoAlta.addItem("Datos corregidos");
        cboMotivoAlta.addItem("Se cancela el reemplazo por otro modelo");
        cboMotivoAlta.addItem("El modelo anterior vuelve a comercializarse");
        
    }   
    
    private void llenarSubcategorias(){

        String categoria = (String) cboCategories.getSelectedItem();
        int idCat = qProduct.selectIdCategoria(categoria);
        cboSubcategories.removeAllItems();
        qProduct.llenarCombosSubcategories(cboSubcategories, idCat);          
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
                product.id_promotion = qGeneric.selectId("id_promotion","product_promotions",namePromo); 
                qProduct.updateProductPromotion(idProduct, product.getId_promotion());
                qProduct.insertProductHistory(idProduct, "Promo activada", "Se activo la siguiente promo: "+namePromo);
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
            qProduct.updateProductPromotion(Integer.parseInt(lbl_id.getText().trim()), null);
            qProduct.insertProductHistory(idProduct, "Promo terminada", "Se dio de baja promo");
            
            buscar();
            
            cboPromotion.setSelectedIndex(0);                                  
        });        
    }
    
    private void startMsj(){
        
        lblErrorModel.setText("");
        lblErrorCodeP.setText("");
        lblErrorStock.setText("");
        lblStock.setText("");
        lblPrecioSujerido.setText("");
        lblEnPromo.setText("");
        lbl_state.setText("");
    }
    
    private void OffObjets(){
        
        txtSubcategory.setVisible(false);
        txtBrand.setVisible(false);
        txtCategory.setVisible(false);
        txtSubcategory.setVisible(false);
        txtBrand.setVisible(false);    
        
        txtModel.setEditable(false);
        txtColor.setEditable(false);
        txtProductCode.setEditable(false);
        
        btnBrand.setEnabled(false);
        btnSubcategories.setEnabled(false);
        
        btnCategory.setEnabled(false);
        
        btnCancel.setVisible(false);
        btnRegistrar.setVisible(false);
        
        txtMin.setEditable(false);
        txtMinHidden.setVisible(false);
        
        txtPrecioCosto.setEditable(false);
        txtBenefit.setEditable(false);
        txtSalePriceHidden.setVisible(false);
        txtSalePrice.setEditable(false);
        cboIva.setEnabled(false);
        jLabel20.setVisible(false);
        lblPrecioSujerido.setVisible(false);
       
        btnCancelPromo.setVisible(false); 
        btnActivePromo.setVisible(false);
        cboPromotion.setVisible(false);
        btnConfirmPromo.setVisible(false);
        btnConfirmPromo.setEnabled(false);
        
        btnBajaAlta.setEnabled(false);
        btnBajaAlta.setVisible(false);
        
        cboMotivoBaja.setVisible(false);
        cboMotivoAlta.setVisible(false);
        
        btnConfirmBajaAlta.setVisible(false);
        btnConfirmBajaAlta.setEnabled(false);
        
        cboBrands.setEnabled(false);
        cboCategories.setEnabled(false);
        cboSubcategories.setEnabled(false);
    }
    
    private void OnObjets(){
        
        btnBajaAlta.setVisible(true);
        btnBajaAlta.setEnabled(true);
        
        txtModel.setEditable(true);
        txtColor.setEditable(true);
        txtProductCode.setEditable(true);
        
        btnBrand.setEnabled(true);
        btnSubcategories.setEnabled(true);
        
        btnCategory.setEnabled(true);
        
        btnCancel.setVisible(true);
        btnRegistrar.setVisible(true);
        
        txtMin.setEditable(true);
        cboIva.setEnabled(true);
        
        txtPrecioCosto.setEditable(true);
        txtBenefit.setEditable(true);
        txtSalePrice.setEditable(true);
        cboIva.setEnabled(true);     
        
        cboBrands.setEnabled(true);
        cboCategories.setEnabled(true);
        cboSubcategories.setEnabled(true);
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
        lbl_state.setText("");
    } 
    
    private void buscar(){
        
        OffObjets(); 
        clear();
        
        qProduct.selectProductEdit(idProduct, lbl_id, txtCategory, txtSubcategory, txtBrand, txtModel, txtColor, txtProductCode, lbl_state, lblEnPromo,lbl_discount_percentage);
        qProduct.selectAllTableStock(idProduct, lblStock, txtMinHidden);
        qProduct.selectProductPriceEdit(idProduct, txtPrecioCosto, txtBenefit, lbl_iva, txtSalePriceHidden);
        
        txtMin.setText(txtMinHidden.getText().trim());
        txtSalePrice.setText(txtSalePriceHidden.getText().trim());
        
        infoComboSelected();
        
        colorLabelStock();
        calcularPrecioConPromo();

        
        if(!txtProductCode.getText().isEmpty()){
            btnEdit.setEnabled(true); 
            btnHistory.setEnabled(true);
        }else{
            btnEdit.setEnabled(false); 
            btnHistory.setEnabled(false);
        }
        
        if(!lblEnPromo.getText().equals("Sin promoción")){
            btnCancelPromo.setVisible(true);
            infoComboPromo();          
        }else{
            btnActivePromo.setVisible(true);
        }
        
        catPadre = cboCategories.getSelectedItem().toString();
        
        String status = lbl_state.getText().trim();
        
        if(status.equals("Activo")){
            btnBajaAlta.setText("Dar de baja");
        }else if(status.equals("Inactivo")){
            btnBajaAlta.setText("Alta producto");
        };
        
    }  
    
    private void actions(){
        
        utils.clearMsjErrorTxt(txtModel, lblErrorModel);        
        utils.clearMsjErrorTxt(txtProductCode, lblErrorCodeP);  
        
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();  
            }     
        });
        
        btnHistory.addActionListener(e->{
            
            viewHistoryDialog fHistory = new viewHistoryDialog(parent, true);
                        
            fHistory.dialogoId_Product(idProduct);
            fHistory.setVisible(true);
            
        });
        
        btnBuscar.addActionListener(e -> {
            idProduct = qProduct.selectIdProduct(txtCodProducto.getText());
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
        
        btnBajaAlta.addActionListener(e->{
            
            String ValStatus;
            ValStatus = lbl_state.getText().trim();
            
            if(ValStatus.equals("Activo")){
                cboMotivoBaja.setVisible(true);
            }else if(ValStatus.equals("Inactivo")){
                cboMotivoAlta.setVisible(true);
            }
            
            btnConfirmBajaAlta.setVisible(true);
  
        });
        
        btnConfirmBajaAlta.addActionListener(e->{
        
            boolean estado = false;
            String ValStatus;
            String action = "";
            String information = "";
            
            idProduct = Integer.parseInt(lbl_id.getText().trim());
            ValStatus = lbl_state.getText().trim();
            
            int status = -1;
            
            if(ValStatus.equals("Activo")){
                
                status = 0;
                action = "Producto dado de baja";
                information = cboMotivoBaja.getSelectedItem().toString();
                
            }else if(ValStatus.equals("Inactivo")){
                
                status = 1;
                action = "Producto reactivado";
                information = cboMotivoAlta.getSelectedItem().toString();
                
            }
            
            estado = qProduct.updateStateProduct(idProduct,status);
            qProduct.insertProductHistory(idProduct, action, information);
            
            if(estado){
                buscar(); 
            } 
            
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
        
        cboCategories.addActionListener(e -> {
            Object selected = cboCategories.getSelectedItem();

            if (selected != null) {
                catPadre = selected.toString();
            }
        });    
        
        cboMotivoAlta.addActionListener(e -> {
            String valor = cboMotivoAlta.getSelectedItem().toString();
            if(!valor.equals("Seleccione una opción")){
                btnConfirmBajaAlta.setEnabled(true);
            }else{
                btnConfirmBajaAlta.setEnabled(false);
            }        
        });  
        
        cboMotivoBaja.addActionListener(e -> {
            String valor = cboMotivoBaja.getSelectedItem().toString();
            if(!valor.equals("Seleccione una opción")){
                btnConfirmBajaAlta.setEnabled(true);
            }else{
                btnConfirmBajaAlta.setEnabled(false);
            }   
        });  
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
            product.color = null;
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
                 
        product.id_brand = qGeneric.selectId("id_brand","product_brands",txtBrand.getText());
        product.id_subcat = qGeneric.selectId("id_subcategory","product_subcategories",txtSubcategory.getText());  
               
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
        qProduct.updateProduct(
            idProduct,
            product.getId_subcat(),
            product.getId_brand(),
            product.getModel(),
            product.getColor(),
            product.getProductCode(), 
            product.getState()
        );
        
        qProduct.insertProductHistory(idProduct, "Producto editado",null);
        
        //ACTUALIZA ALARMA DE STOCK MINIMO
        if(!txtMin.getText().equals(txtMinHidden.getText())){
            
            qProduct.updateAlarmStockMin(idProduct, stockMin);
            qProduct.insertProductHistory(idProduct, "Producto editado", "Stock minimo modificado");
        }
        
        //ACTUALIZA DATOS DEL PRECIO
        if(!txtSalePrice.getText().equals(txtSalePriceHidden.getText())){
            qProduct.updatePriceProduct(idProduct, price.getPrice(), price.getBenefit(), price.getSalesPrice(), price.getIva());                 
            qProduct.insertProductHistory(idProduct, "Producto editado", "Precio modificado");
        }
  
        OffObjets();
        buscar();
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
        
        lblPrecioSujerido.setText(utils.calcularPrecioSugerido(
                txtPrecioCosto.getText().trim(), 
                cboIva.getSelectedItem().toString(), 
                txtBenefit.getText().trim())
        );
        jLabel20.setVisible(true);
        lblPrecioSujerido.setVisible(true);
    }
    
    private void calcularPrecioConPromo(){
        
        Double promo = Double.parseDouble(lbl_discount_percentage.getText().trim());
        Double precioVenta = Double.parseDouble(txtSalePriceHidden.getText().trim());
        
        if(promo > 0){
            
            double precioPromo = precioVenta - (precioVenta * promo / 100); 
            txtSalePrice.setText(String.format("%.2f", precioPromo));            
            txtSalePrice.setForeground(new Color(0,128,0));
        }
                  
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

        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtBrand = new javax.swing.JTextField();
        lbl_id = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblErrorModel = new javax.swing.JLabel();
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
        lblErrorStock = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblPrecioSujerido = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtMinHidden = new javax.swing.JTextField();
        txtSalePriceHidden = new javax.swing.JTextField();
        txtMin = new javax.swing.JTextField();
        txtPrecioCosto = new javax.swing.JTextField();
        txtBenefit = new javax.swing.JTextField();
        txtSalePrice = new javax.swing.JTextField();
        cboIva = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        txtSalePrice1 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        lblEnPromo = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        Estado = new javax.swing.JLabel();
        lbl_state = new javax.swing.JLabel();
        cboBrands = new javax.swing.JComboBox<>();
        btnBrand = new javax.swing.JButton();
        cboCategories = new javax.swing.JComboBox<>();
        btnCategory = new javax.swing.JButton();
        txtColor = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        txtProductCode = new javax.swing.JTextField();
        cboSubcategories = new javax.swing.JComboBox<>();
        btnSubcategories = new javax.swing.JButton();
        lbl_discount_percentage = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtImpCred = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtImpDeb = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtRecPlat = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txt3C = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txt6C = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnHistory = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnActivePromo = new javax.swing.JButton();
        cboPromotion = new javax.swing.JComboBox<>();
        btnConfirmPromo = new javax.swing.JButton();
        btnCancelPromo = new javax.swing.JButton();
        btnBajaAlta = new javax.swing.JButton();
        cboMotivoAlta = new javax.swing.JComboBox<>();
        cboMotivoBaja = new javax.swing.JComboBox<>();
        btnConfirmBajaAlta = new javax.swing.JButton();
        panelSearchCode = new javax.swing.JPanel();
        txtCodProducto = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnSearchList = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/productEdit64.png"))); // NOI18N
        jLabel1.setText("Administrar producto");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(1201, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(9, 9, 9))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(407, 407, 407)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Color:");

        txtBrand.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtBrand.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBrand.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbl_id.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_id.setForeground(new java.awt.Color(12, 83, 151));
        lbl_id.setText("0");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Modelo:");

        lblErrorModel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorModel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorModel.setText("Mensaje error!");
        lblErrorModel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("Codigo producto:");

        lblErrorCodeP.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorCodeP.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorCodeP.setText("Mensaje error!");
        lblErrorCodeP.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(35, 35, 38));
        jLabel14.setText("Subcategoría:");

        txtSubcategory.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtSubcategory.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSubcategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(12, 83, 151));
        jLabel12.setText("Id producto:");

        jLabel21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(35, 35, 38));
        jLabel21.setText("Marca:");

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(12, 83, 151));
        jLabel19.setText("Cantidad en stock:");

        lblStock.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblStock.setForeground(new java.awt.Color(12, 83, 151));
        lblStock.setText("xxx");

        jPanel5.setBackground(new java.awt.Color(245, 248, 255));
        jPanel5.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(245, 248, 255));
        jPanel6.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setText("Alarma stock minímo:");

        lblErrorStock.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorStock.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorStock.setText("Mensaje error!");
        lblErrorStock.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(12, 83, 151));
        jLabel16.setText("Precio de costo:");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(12, 83, 151));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/porcentaje.png"))); // NOI18N
        jLabel15.setText("IVA:");
        jLabel15.setToolTipText("");
        jLabel15.setFocusable(false);
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva.setText("xxx");

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(12, 83, 151));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/%.png"))); // NOI18N
        jLabel17.setText("Beneficio:");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(12, 83, 151));
        jLabel20.setText("Precio Sujerido:");

        lblPrecioSujerido.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblPrecioSujerido.setForeground(new java.awt.Color(0, 128, 0));
        lblPrecioSujerido.setText("xxx");

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(12, 83, 151));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/priceTag32.png"))); // NOI18N
        jLabel18.setText("Precio de venta:");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        txtMinHidden.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtMinHidden.setForeground(new java.awt.Color(35, 35, 38));
        txtMinHidden.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMinHidden.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtSalePriceHidden.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtSalePriceHidden.setForeground(new java.awt.Color(35, 35, 38));
        txtSalePriceHidden.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSalePriceHidden.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtSalePriceHidden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalePriceHiddenActionPerformed(evt);
            }
        });

        txtMin.setForeground(new java.awt.Color(35, 35, 38));

        txtPrecioCosto.setForeground(new java.awt.Color(35, 35, 38));

        txtBenefit.setForeground(new java.awt.Color(35, 35, 38));

        txtSalePrice.setForeground(new java.awt.Color(35, 35, 38));

        cboIva.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIva.setForeground(new java.awt.Color(35, 35, 38));

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(12, 83, 151));
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/creditCard32.png"))); // NOI18N
        jLabel29.setText("Precio de lista:");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        txtSalePrice1.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txtSalePriceHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMinHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrecioSujerido))
                            .addComponent(lblErrorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtSalePrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrecioSujerido, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtMinHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalePriceHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalePrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel23.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(12, 83, 151));
        jLabel23.setText("En promoción:");

        lblEnPromo.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lblEnPromo.setForeground(new java.awt.Color(12, 83, 151));
        lblEnPromo.setText("xxx");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setText("Categoria:");

        txtCategory.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtCategory.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        Estado.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        Estado.setForeground(new java.awt.Color(12, 83, 151));
        Estado.setText("Estado:");

        lbl_state.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_state.setForeground(new java.awt.Color(12, 83, 151));
        lbl_state.setText("xxx");

        cboBrands.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboBrands.setForeground(new java.awt.Color(35, 35, 38));

        btnBrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnBrand.setBorderPainted(false);

        cboCategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCategories.setForeground(new java.awt.Color(35, 35, 38));
        cboCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriesActionPerformed(evt);
            }
        });

        btnCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnCategory.setBorderPainted(false);

        txtColor.setForeground(new java.awt.Color(35, 35, 38));

        txtModel.setForeground(new java.awt.Color(35, 35, 38));

        txtProductCode.setForeground(new java.awt.Color(35, 35, 38));

        cboSubcategories.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSubcategories.setForeground(new java.awt.Color(35, 35, 38));
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        btnSubcategories.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu32.png"))); // NOI18N
        btnSubcategories.setBorderPainted(false);

        lbl_discount_percentage.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_discount_percentage.setForeground(new java.awt.Color(12, 83, 151));
        lbl_discount_percentage.setText("xxx");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Otros conceptos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14), new java.awt.Color(12, 83, 151))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Imp. credito:");

        txtImpCred.setForeground(new java.awt.Color(35, 35, 38));

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Imp. debito:");

        txtImpDeb.setForeground(new java.awt.Color(35, 35, 38));

        jLabel22.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Recargo plataforma");

        txtRecPlat.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtImpCred, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImpDeb, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRecPlat, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtImpCred, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txtImpDeb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txtRecPlat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 34, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Financiación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14), new java.awt.Color(12, 83, 151))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Recargo 3 cuotas:");

        txt3C.setForeground(new java.awt.Color(35, 35, 38));

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("Recargo 6 cuotas:");

        txt6C.setForeground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt3C, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt6C, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txt3C, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(txt6C, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtColor, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(cboBrands, 0, 300, Short.MAX_VALUE)
                            .addComponent(cboCategories, 0, 300, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSubcategories, 0, 300, Short.MAX_VALUE)
                            .addComponent(txtModel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblErrorCodeP, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblErrorModel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(251, 251, 251))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Estado, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_state, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStock, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEnPromo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_discount_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Estado, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_state, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblStock, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEnPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_discount_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBrands, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorModel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorCodeP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubcategories, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubcategory, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar-32.png"))); // NOI18N

        btnActivePromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/descuento-32.png"))); // NOI18N

        cboPromotion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboPromotion.setForeground(new java.awt.Color(35, 35, 38));

        btnConfirmPromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        btnCancelPromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/endPromotion32.png"))); // NOI18N

        btnBajaAlta.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnBajaAlta.setForeground(new java.awt.Color(35, 35, 38));

        cboMotivoAlta.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboMotivoAlta.setForeground(new java.awt.Color(35, 35, 38));

        cboMotivoBaja.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboMotivoBaja.setForeground(new java.awt.Color(35, 35, 38));

        btnConfirmBajaAlta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActivePromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirmPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelPromo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBajaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMotivoAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMotivoBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirmBajaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActivePromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelPromo)
                    .addComponent(btnBajaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMotivoAlta)
                    .addComponent(cboMotivoBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmBajaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelSearchCode.setBackground(new java.awt.Color(255, 255, 255));

        txtCodProducto.setForeground(new java.awt.Color(35, 35, 38));
        txtCodProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodProductoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodProductoKeyTyped(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        btnSearchList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        javax.swing.GroupLayout panelSearchCodeLayout = new javax.swing.GroupLayout(panelSearchCode);
        panelSearchCode.setLayout(panelSearchCodeLayout);
        panelSearchCodeLayout.setHorizontalGroup(
            panelSearchCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchCodeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSearchCodeLayout.setVerticalGroup(
            panelSearchCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchCodeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtCodProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 310, Short.MAX_VALUE))
                    .addComponent(panelSearchCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSearchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSalePriceHiddenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalePriceHiddenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalePriceHiddenActionPerformed

    private void cboCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategoriesActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void txtCodProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            idProduct = qProduct.selectIdProduct(txtCodProducto.getText());
            buscar();           
        }
    }//GEN-LAST:event_txtCodProductoKeyPressed

    private void txtCodProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodProductoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Estado;
    private javax.swing.JButton btnActivePromo;
    private javax.swing.JButton btnBajaAlta;
    private javax.swing.JButton btnBrand;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelPromo;
    private javax.swing.JButton btnCategory;
    private javax.swing.JButton btnConfirmBajaAlta;
    private javax.swing.JButton btnConfirmPromo;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSearchList;
    private javax.swing.JButton btnSubcategories;
    private javax.swing.JComboBox<String> cboBrands;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboIva;
    private javax.swing.JComboBox<String> cboMotivoAlta;
    private javax.swing.JComboBox<String> cboMotivoBaja;
    private javax.swing.JComboBox<String> cboPromotion;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblEnPromo;
    private javax.swing.JLabel lblErrorCodeP;
    private javax.swing.JLabel lblErrorModel;
    private javax.swing.JLabel lblErrorStock;
    private javax.swing.JLabel lblPrecioSujerido;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lbl_discount_percentage;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_state;
    private javax.swing.JPanel panelSearchCode;
    private javax.swing.JTextField txt3C;
    private javax.swing.JTextField txt6C;
    private javax.swing.JTextField txtBenefit;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtCodProducto;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtImpCred;
    private javax.swing.JTextField txtImpDeb;
    private javax.swing.JTextField txtMin;
    private javax.swing.JTextField txtMin4;
    private javax.swing.JTextField txtMin5;
    private javax.swing.JTextField txtMin6;
    private javax.swing.JTextField txtMinHidden;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPrecioCosto;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtRecPlat;
    private javax.swing.JTextField txtSalePrice;
    private javax.swing.JTextField txtSalePrice1;
    private javax.swing.JTextField txtSalePriceHidden;
    private javax.swing.JTextField txtSubcategory;
    // End of variables declaration//GEN-END:variables
}
