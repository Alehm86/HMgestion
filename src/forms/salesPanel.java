/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import classDAO.productDAO;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import models.SaleItem;
import utils.config;
import utils.utility;


public class salesPanel extends javax.swing.JPanel {

    productDAO qProduct = new productDAO();
    
    utility utils = new utility();
    
    SaleItem salesItems = new SaleItem();
    
    private int id_product = -1;
    private String serviceNumber = "";
    
    private List<SaleItem> items = new ArrayList<>();
    private SaleItemTableModel model;
    
    public salesPanel() {
        initComponents();
        
        utils.agregarPlaceholderN(txtProductCode, "código de producto.");  
        utils.agregarPlaceholderN(txt_service2, "Ingrese nº servicio."); 
        
        inicializar();
        actions();
        tableItems();
        
    }
    
    private void inicializar(){
        
        txtPrice.setVisible(false);
        txtPrice.setEditable(false);
        
        radioProduct.setSelected(true);
        panelService.setVisible(false);
        
        panelService(false);

    }
    
    private void panelProduct(boolean state){
        
        jLabel19.setEnabled(state);
        lbl_description.setEnabled(state);
        jLabel22.setEnabled(state);
        lbl_price.setEnabled(state);
        jLabel24.setEnabled(state);
        lbl_iva.setEnabled(state);
        jLabel23.setEnabled(state);
        txt_quantity.setEnabled(state);
        jLabel25.setEnabled(state);
        lbl_total.setEnabled(state);
        btnAddProduct.setEnabled(state);
        
        if(state){
            panelProductItem.setBackground(new Color(255, 255, 255));
        }else{
            panelProductItem.setBackground(new Color(204, 204, 204));
        }
        
        
    }
    
    private void panelService(boolean state){
        jLabel26.setEnabled(state);
        lbl_serviceNumber.setEnabled(state);
        jLabel27.setEnabled(state);
        lbl_customer.setEnabled(state);
        jLabel28.setEnabled(state);
        lbl_date.setEnabled(state);
        jLabel30.setEnabled(state);
        lbl_device.setEnabled(state);
        jLabel29.setEnabled(state);
        tableItemsService.setEnabled(state);
        btnAddItemService.setEnabled(state);
        
        if(state){
            panelServiceItems.setBackground(new Color(255, 255, 255));
        }else{
            panelServiceItems.setBackground(new Color(204, 204, 204));
        }
    }        
    
    private void actions(){
        
        radioProduct.addActionListener(e -> {
            panelProduct.setVisible(true);
            panelProduct(true);
            panelService.setVisible(false);
            panelService(false);
        });

        radioService.addActionListener(e -> {
            panelProduct.setVisible(false);
            panelProduct(false);
            panelService.setVisible(true);
            panelService(true);
        });
        
        
        txt_quantity.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
            }
        });
        
        btnSearchProductList.addActionListener(e->{
            
            id_product = 0;
            
            productSearchDialog fSearchProduct = new productSearchDialog(null, true);
            fSearchProduct.setLocationRelativeTo(null);
            fSearchProduct.setVisible(true);  
            
            id_product = fSearchProduct.getProduct();
            
            if(id_product > 0){
                buscarCode();
            }
            
        });
                
        btnBuscarProduct.addActionListener(e->{
            
            String productCode = txtProductCode.getText().trim();
            id_product = qProduct.selectIdProduct(productCode); 

            if(id_product > 0){
                buscarCode();
            }            
        });
        
        btnSearchServiceList.addActionListener(e->{
            
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            serviceSearchListDialog pServiceView = new serviceSearchListDialog(parent, true);
            
            pServiceView.setVisible(true);
            serviceNumber = pServiceView.getService();
            JOptionPane.showMessageDialog(null, serviceNumber);
            if(!serviceNumber.isEmpty()){
                
            }
        });
        
        btnBuscarService.addActionListener(e->{
            //falta programar
        });
        
        btnAddProduct.addActionListener(e -> {
            addItem();
        });
    }
    
    private void calculateTotal(){
        
        if(txt_quantity.getText().trim().isEmpty()){
            lbl_total.setText("");
            return;
        }

        try{
            int cantidad = Integer.parseInt(txt_quantity.getText().trim());
            double precio = Double.parseDouble(lbl_price.getText().trim());
            double total = cantidad * precio;

            lbl_total.setText(String.valueOf(total));

        } catch(NumberFormatException e){
            lbl_total.setText("");
        }
    }
    
    private void addItem(){

        if(txt_quantity.getText().trim().isEmpty()){

            JOptionPane.showMessageDialog(null, "Ingrese cantidad.");
            return;
        }

        SaleItem item = new SaleItem(
                id_product,
                txtProductCode.getText(),
                lbl_description.getText(),
                Integer.parseInt(txt_quantity.getText()),
                Double.parseDouble(lbl_price.getText()),
                Double.parseDouble(lbl_iva.getText().replace("%", "")),
                Double.parseDouble(lbl_total.getText())
        );

        items.add(item);

        model.fireTableDataChanged();
              
        limpiarProduct();
    }
    
    
    public class SaleItemTableModel extends AbstractTableModel {

        private final String[] columns = {            
            "Código",
            "Descripción",
            "Cantidad",
            "Precio",
            "IVA",
            "Total"
        };

        private List<SaleItem> items;

        public SaleItemTableModel(List<SaleItem> items) {
            this.items = items;
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            SaleItem item = items.get(rowIndex);

            switch(columnIndex){

                case 0:
                    return item.getProductCode();

                case 1:
                    return item.getDescription();

                case 2:
                    return item.getQuantity();

                case 3:
                    return item.getPrice();

                case 4:
                    return item.getIva();

                case 5:
                    return item.getTotal();

                default:
                    return null;
            }
        }
    }
    
    private void tableItems(){

        model = new SaleItemTableModel(items);

        tableItems.setModel(model);

        tableItems.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(70);
        tableItems.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableItems.getColumnModel().getColumn(4).setPreferredWidth(60);
        tableItems.getColumnModel().getColumn(5).setPreferredWidth(120);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();

        center.setHorizontalAlignment(SwingConstants.CENTER);

        tableItems.getColumnModel().getColumn(0).setCellRenderer(center);
        tableItems.getColumnModel().getColumn(1).setCellRenderer(center);
        tableItems.getColumnModel().getColumn(2).setCellRenderer(center);
        tableItems.getColumnModel().getColumn(3).setCellRenderer(center);
        tableItems.getColumnModel().getColumn(4).setCellRenderer(center);
        tableItems.getColumnModel().getColumn(5).setCellRenderer(center);

        config.TableStyleUtil.applyPoppinsHeader(tableItems);
        
    }
    
    
    private void buscarCode(){
        
        qProduct.selectProduct(id_product, lbl_description, txtProductCode);
        qProduct.selectSalePriceAndIva(id_product,txtPrice,lbl_iva);
        
        lbl_price.setText(txtPrice.getText().trim());
    }
    
    private void limpiarProduct(){
        
        txtProductCode.setText("");
        txt_service.setText("");
        lbl_description.setText("");
        lbl_price.setText("-");
        lbl_iva.setText("-");
        txt_quantity.setText("");
        lbl_total.setText("-");
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtPrice = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        radioProduct = new javax.swing.JRadioButton();
        radioService = new javax.swing.JRadioButton();
        panelProductItem = new javax.swing.JPanel();
        btnAddProduct = new javax.swing.JButton();
        lbl_total = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt_quantity = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lbl_price = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        panelProduct = new javax.swing.JPanel();
        txtProductCode = new javax.swing.JTextField();
        btnBuscarProduct = new javax.swing.JButton();
        btnSearchProductList = new javax.swing.JButton();
        lbl_description = new javax.swing.JLabel();
        panelServiceItems = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableItemsService = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lbl_serviceNumber = new javax.swing.JLabel();
        lbl_customer = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnAddItemService = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        lbl_device = new javax.swing.JLabel();
        panelService = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txt_service = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_service2 = new javax.swing.JTextField();
        btnBuscarService = new javax.swing.JButton();
        btnSearchServiceList = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
        btnBuscarProduct3 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(35, 35, 38));

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cajaReg64.png"))); // NOI18N
        jLabel4.setText("Ventas");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(65, 65, 63));
        txtPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceKeyPressed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        buttonGroup.add(radioProduct);
        radioProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        radioProduct.setForeground(new java.awt.Color(35, 35, 38));
        radioProduct.setText("Producto");

        buttonGroup.add(radioService);
        radioService.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        radioService.setForeground(new java.awt.Color(35, 35, 38));
        radioService.setText("Servicio");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioService)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(radioProduct)
                    .addComponent(radioService))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        panelProductItem.setBackground(new java.awt.Color(255, 255, 255));
        panelProductItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAddProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProduct.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAddProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add32.png"))); // NOI18N
        btnAddProduct.setText("Agregar");
        btnAddProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnAddProduct.setBorderPainted(false);
        btnAddProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddProduct.setDefaultCapable(false);
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setFocusable(false);
        btnAddProduct.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnAddProduct.addActionListener(this::btnAddProductActionPerformed);

        lbl_total.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_total.setForeground(new java.awt.Color(35, 35, 38));
        lbl_total.setText("-");

        jLabel25.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(35, 35, 38));
        jLabel25.setText("Total:");

        txt_quantity.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_quantity.setForeground(new java.awt.Color(65, 65, 63));
        txt_quantity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_quantity.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txt_quantity.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_quantityKeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(35, 35, 38));
        jLabel23.setText("Cantidad:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva.setText("-");

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(35, 35, 38));
        jLabel24.setText("I.V.A.:");

        lbl_price.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_price.setForeground(new java.awt.Color(35, 35, 38));
        lbl_price.setText("-");

        jLabel22.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(35, 35, 38));
        jLabel22.setText("Precio:");

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(35, 35, 38));
        jLabel19.setText("Item:");

        panelProduct.setBackground(new java.awt.Color(255, 255, 255));

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setForeground(new java.awt.Color(65, 65, 63));
        txtProductCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProductCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtProductCode.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btnBuscarProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscarProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnBuscarProduct.setBorderPainted(false);
        btnBuscarProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProduct.setDefaultCapable(false);
        btnBuscarProduct.setFocusPainted(false);
        btnBuscarProduct.setFocusable(false);
        btnBuscarProduct.addActionListener(this::btnBuscarProductActionPerformed);

        btnSearchProductList.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchProductList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnSearchProductList.setBorder(null);
        btnSearchProductList.setBorderPainted(false);
        btnSearchProductList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchProductList.setFocusPainted(false);
        btnSearchProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchProductListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchProductListMouseExited(evt);
            }
        });
        btnSearchProductList.addActionListener(this::btnSearchProductListActionPerformed);

        javax.swing.GroupLayout panelProductLayout = new javax.swing.GroupLayout(panelProduct);
        panelProduct.setLayout(panelProductLayout);
        panelProductLayout.setHorizontalGroup(
            panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchProductList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panelProductLayout.setVerticalGroup(
            panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSearchProductList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_description.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_description.setForeground(new java.awt.Color(35, 35, 38));
        lbl_description.setText("-");

        javax.swing.GroupLayout panelProductItemLayout = new javax.swing.GroupLayout(panelProductItem);
        panelProductItem.setLayout(panelProductItemLayout);
        panelProductItemLayout.setHorizontalGroup(
            panelProductItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductItemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelProductItemLayout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_description, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_price, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addGap(0, 0, 0)
                        .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(160, Short.MAX_VALUE))
        );
        panelProductItemLayout.setVerticalGroup(
            panelProductItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProductItemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_price, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_description, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelServiceItems.setBackground(new java.awt.Color(255, 255, 255));
        panelServiceItems.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableItemsService.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableItemsService.setForeground(new java.awt.Color(65, 65, 63));
        tableItemsService.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableItemsService.setFillsViewportHeight(true);
        tableItemsService.setRowHeight(30);
        jScrollPane4.setViewportView(tableItemsService);

        jLabel26.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(35, 35, 38));
        jLabel26.setText("Servicio:");

        jLabel27.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(35, 35, 38));
        jLabel27.setText("Cliente:");

        lbl_serviceNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_serviceNumber.setForeground(new java.awt.Color(35, 35, 38));
        lbl_serviceNumber.setText("-");

        lbl_customer.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_customer.setForeground(new java.awt.Color(35, 35, 38));
        lbl_customer.setText("-");

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(35, 35, 38));
        jLabel28.setText("Fecha:");

        lbl_date.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(35, 35, 38));
        lbl_date.setText("-");

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(35, 35, 38));
        jLabel29.setText("Items");

        btnAddItemService.setBackground(new java.awt.Color(255, 255, 255));
        btnAddItemService.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAddItemService.setForeground(new java.awt.Color(12, 83, 151));
        btnAddItemService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add32.png"))); // NOI18N
        btnAddItemService.setText("Agregar");
        btnAddItemService.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnAddItemService.setBorderPainted(false);
        btnAddItemService.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddItemService.setDefaultCapable(false);
        btnAddItemService.setFocusPainted(false);
        btnAddItemService.setFocusable(false);
        btnAddItemService.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnAddItemService.addActionListener(this::btnAddItemServiceActionPerformed);

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(35, 35, 38));
        jLabel30.setText("Dispositivo:");

        lbl_device.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_device.setForeground(new java.awt.Color(35, 35, 38));
        lbl_device.setText("-");

        panelService.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(35, 35, 38));
        jLabel20.setText("ST-");

        txt_service.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_service.setForeground(new java.awt.Color(65, 65, 63));
        txt_service.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_service.setText("2026");
        txt_service.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txt_service.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(35, 35, 38));
        jLabel21.setText("-");

        txt_service2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_service2.setForeground(new java.awt.Color(65, 65, 63));
        txt_service2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_service2.setText("2026");
        txt_service2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txt_service2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_service2.addActionListener(this::txt_service2ActionPerformed);

        btnBuscarService.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscarService.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnBuscarService.setBorderPainted(false);
        btnBuscarService.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarService.setDefaultCapable(false);
        btnBuscarService.setFocusPainted(false);
        btnBuscarService.setFocusable(false);
        btnBuscarService.addActionListener(this::btnBuscarServiceActionPerformed);

        btnSearchServiceList.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchServiceList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnSearchServiceList.setBorder(null);
        btnSearchServiceList.setBorderPainted(false);
        btnSearchServiceList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchServiceList.setFocusPainted(false);
        btnSearchServiceList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchServiceListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchServiceListMouseExited(evt);
            }
        });
        btnSearchServiceList.addActionListener(this::btnSearchServiceListActionPerformed);

        javax.swing.GroupLayout panelServiceLayout = new javax.swing.GroupLayout(panelService);
        panelService.setLayout(panelServiceLayout);
        panelServiceLayout.setHorizontalGroup(
            panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(0, 0, 0)
                .addComponent(txt_service, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(0, 0, 0)
                .addComponent(txt_service2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarService, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchServiceList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        panelServiceLayout.setVerticalGroup(
            panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSearchServiceList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarService, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_service, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_service2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelServiceItemsLayout = new javax.swing.GroupLayout(panelServiceItems);
        panelServiceItems.setLayout(panelServiceItemsLayout);
        panelServiceItemsLayout.setHorizontalGroup(
            panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServiceItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(panelServiceItemsLayout.createSequentialGroup()
                        .addComponent(panelService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelServiceItemsLayout.createSequentialGroup()
                        .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelServiceItemsLayout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_serviceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelServiceItemsLayout.createSequentialGroup()
                                .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel29))
                                .addGap(201, 201, 201)
                                .addComponent(lbl_device, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddItemService, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelServiceItemsLayout.setVerticalGroup(
            panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelServiceItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_serviceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelServiceItemsLayout.createSequentialGroup()
                        .addGroup(panelServiceItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_device, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelServiceItemsLayout.createSequentialGroup()
                        .addComponent(btnAddItemService, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        tableItems.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableItems.setFillsViewportHeight(true);
        tableItems.setRowHeight(30);
        jScrollPane1.setViewportView(tableItems);

        btnBuscarProduct3.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarProduct3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBuscarProduct3.setForeground(new java.awt.Color(12, 83, 151));
        btnBuscarProduct3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add32.png"))); // NOI18N
        btnBuscarProduct3.setText("Confirmar");
        btnBuscarProduct3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(12, 83, 151), 1, true));
        btnBuscarProduct3.setBorderPainted(false);
        btnBuscarProduct3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProduct3.setDefaultCapable(false);
        btnBuscarProduct3.setFocusPainted(false);
        btnBuscarProduct3.setFocusable(false);
        btnBuscarProduct3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnBuscarProduct3.addActionListener(this::btnBuscarProduct3ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBuscarProduct3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProductItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelServiceItems, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(panelProductItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelServiceItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarProduct3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductActionPerformed

    }//GEN-LAST:event_btnBuscarProductActionPerformed

    private void btnSearchProductListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchProductListMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchProductListMouseEntered

    private void btnSearchProductListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchProductListMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchProductListMouseExited

    private void btnSearchProductListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchProductListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchProductListActionPerformed

    private void btnBuscarServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarServiceActionPerformed

    private void btnSearchServiceListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchServiceListMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchServiceListMouseEntered

    private void btnSearchServiceListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchServiceListMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchServiceListMouseExited

    private void btnSearchServiceListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchServiceListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchServiceListActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void txt_quantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_quantityKeyPressed
        txt_quantity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txt_quantity.getText().length() >= 3) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txt_quantityKeyPressed

    private void txtPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceKeyPressed

    private void btnAddItemServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddItemServiceActionPerformed

    private void btnBuscarProduct3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProduct3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarProduct3ActionPerformed

    private void txt_service2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_service2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_service2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItemService;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnBuscarProduct;
    private javax.swing.JButton btnBuscarProduct3;
    private javax.swing.JButton btnBuscarService;
    private javax.swing.JButton btnSearchProductList;
    private javax.swing.JButton btnSearchServiceList;
    private javax.swing.ButtonGroup buttonGroup;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbl_customer;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_description;
    private javax.swing.JLabel lbl_device;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_price;
    private javax.swing.JLabel lbl_serviceNumber;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel panelProduct;
    private javax.swing.JPanel panelProductItem;
    private javax.swing.JPanel panelService;
    private javax.swing.JPanel panelServiceItems;
    private javax.swing.JRadioButton radioProduct;
    private javax.swing.JRadioButton radioService;
    private javax.swing.JTable tableItems;
    private javax.swing.JTable tableItemsService;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txt_quantity;
    private javax.swing.JTextField txt_service;
    private javax.swing.JTextField txt_service2;
    // End of variables declaration//GEN-END:variables
}
