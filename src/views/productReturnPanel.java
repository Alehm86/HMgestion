/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.productDAO;
import dao.customerDAO;
import dao.purchaseInvoiceDAO;
import dao.supplierDAO;
import javax.swing.JOptionPane;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class productReturnPanel extends javax.swing.JPanel {

    productDAO qProduct = new productDAO();
    customerDAO qCustomer = new customerDAO();
    supplierDAO qSuppliers = new supplierDAO();
    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO();     

    private int idPurchase = -1;
    private int id_product = -1; 
    private String client;
    private String numfact = "";
    
    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
    
    public productReturnPanel() {
        initComponents();
        
        cboEntidad.removeAllItems();
        cboEntidad.addItem("Seleccione una opción");
        cboEntidad.addItem("Proveedor");
        cboEntidad.addItem("Cliente");
        
        panelTableItems.setVisible(false);
        
        cargarComboCompensacion();
        inicializar();
        actions();

    }

    private void objetosCompensacionDes(){
        lblDescripcion.setEnabled(false);
        labelDescripcion1.setEnabled(false);
        labelDescripcion1.setText("");       
    }
    
    private void inicializar(){
        
        objetosCompensacionDes();
        cboCompensacion.setEnabled(false);
        btnRegistrarDevolucion.setEnabled(false);
        txtPrice.setEditable(false);
        cboCompensacion.setEnabled(false);      
        
        jPanelClientes.setVisible(false);
        jPanelFactura.setVisible(false);
        jPanelProducto.setVisible(false);
        jPanelMotivo.setVisible(false);
                
//        jPanelSeparador2.setVisible(false);      
        
        
        
    }
       
    private void cargarComboCompensacion() {
        cboCompensacion.removeAllItems();
        cboCompensacion.addItem("Seleccione una opción");
        cboCompensacion.addItem("Nota de Crédito");
        cboCompensacion.addItem("Reintegro de dinero");
        cboCompensacion.addItem("Cambio por otro producto igual");
    }
  
    void actions(){
        
        cboEntidad.addActionListener(e->{
            inicializar();          
            
            if(cboEntidad.getSelectedIndex()==1){
                
//                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(true);                 
                jPanelClientes.setVisible(false); 
                panelTableItems.setVisible(true);
                panelSearchProduct.setVisible(false);
                
            }else if(cboEntidad.getSelectedIndex()==2){
                
                jPanelClientes.setVisible(true);   
//                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false); 
                panelTableItems.setVisible(false);
                panelSearchProduct.setVisible(true);
                
            }else if(cboEntidad.getSelectedIndex()==0){
                
                jPanelClientes.setVisible(false);   
//                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false);
                panelTableItems.setVisible(false);
                panelSearchProduct.setVisible(false);
            }
            
        });
        
        btnBuscarProduct.addActionListener(e->{
            
            productSearchDialog fSearchProduct = new productSearchDialog(null, true);
            fSearchProduct.setLocationRelativeTo(null);
            fSearchProduct.setVisible(true);  
            
            id_product = fSearchProduct.getProduct();
            
            if(id_product > 0){
                buscarProducto(id_product);
            }           
        });
        
        btnProduct.addActionListener(e->{
            id_product = qProduct.selectIdProduct(txtProductCode.getText().trim());
            buscarProducto(id_product);
        });
        
        btnCancel.addActionListener(e->{
            limpiarFormulario();
        });  
        
        btnSerchClient.addActionListener(e->{           
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            customerSearchDialog pClient = new customerSearchDialog(parent, true);
            
            pClient.setVisible(true);
            client = pClient.getCustomerSerch();            
            buscarCliente();            
        }); 
        
        btnSerchCuitClient.addActionListener(e->{          
            client = txtCuitClient.getText().trim();
            buscarCliente();
        }); 
        
        btnBuscarFactura.addActionListener(e -> {   
            purchaseInvoiceListDialog fPurchaseInv = new purchaseInvoiceListDialog(parent, true, 2);
            fPurchaseInv.setVisible(true);
            idPurchase = fPurchaseInv.getIdPurchase();
            
            if(idPurchase > 0){
                
                DefaultTableModel model = (DefaultTableModel) tableItems.getModel();
                model.setRowCount(0);

                buscarFactura(idPurchase);
                qPurchase.listItemsPurchase(idPurchase, tableItems);
            }
        }); 
        
        btnBuscar.addActionListener(e -> {
            numfact = txtSerie.getText().trim()+"-"+txtFactura.getText().trim();
            idPurchase = qPurchase.selectIdPurchase(numfact);
            buscarFactura(idPurchase);         
        });
                  
   
        cboCompensacion.addActionListener(e -> {
            String Compensacion = cboCompensacion.getSelectedItem().toString();
            
            if(Compensacion.equals("Cambio por otro producto igual")){
                lblDescripcion.setVisible(true);
                lblDescripcion.setEnabled(true);
                labelDescripcion1.setEnabled(true);
                labelDescripcion1.setText(labelDescripcion.getText());
            }
            if(Compensacion.equals("Nota de Crédito")){
                objetosCompensacionDes();
            }
            if(Compensacion.equals("Reintegro de dinero")){
                objetosCompensacionDes();
            }
            
            btnRegistrarDevolucion.setEnabled(true);
        });
        
        btnRegistrarDevolucion.addActionListener(e -> {

            int cantidad = 0;

            try {
                cantidad = Integer.parseInt(txtCantidad.getText().trim());

                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "Ingresá una cantidad mayor a 0.");
                    txtCantidad.requestFocusInWindow();
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingresá un número válido en Cantidad.");
                txtCantidad.requestFocusInWindow();
                return;
            }

            int compensacion = cboCompensacion.getSelectedIndex();
            if (compensacion == 0) {
                JOptionPane.showMessageDialog(null, "Seleccioná una opción válida en Compensación.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseás realizar la devolución?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) return;

            int stock = qProduct.selectStockActual(id_product);
            int entidad = cboEntidad.getSelectedIndex();

            if (entidad == 2) {  
                stock = stock + cantidad;
            } 
            else if (entidad == 1) {
                stock = stock - cantidad;
            }

            if (compensacion == 1 || compensacion == 2) {
                qProduct.updateStockProduct(id_product, stock);
            }

            JOptionPane.showMessageDialog(null, "Devolución registrada con éxito.");

            limpiarFormulario();
        });
       
      
    }
    
    private void limpiarFormulario() {

        inicializar();
        
        cboEntidad.setSelectedIndex(0);

        GrupTipoDev.clearSelection();

        txtSerie.setText("");
        txtFactura.setText("");
        labelFecha.setText("");
        labelSupplier.setText("");
        labelType.setText("");
        labelNumber.setText("");

        txtProductCode.setText("");
        txtCantidad.setText("");
        txtPrice.setText("");

        lblIva.setText("");
        labelDescripcion.setText("");

        textObservaciones.setText("");

        cboCompensacion.setSelectedIndex(0);
    }
    
    private void buscarFactura(int idPurchase){
        qPurchase.selectPurchaseInvoice2(idPurchase, labelFecha, labelSupplier, labelType, labelNumber);   
        
        if(!labelSupplier.getText().isEmpty()){
            jPanelProducto.setVisible(true);
            jPanelMotivo.setVisible(true);            
        }else{
            jPanelProducto.setVisible(false);
            jPanelMotivo.setVisible(false);
        }        
    } 

    private void buscarCliente(){
        qCustomer.selectCustomer(client, lbl_IdClient, txtNameClient, txtCuitClient, txtTelClient, lbl_IVA_Client);   
        
        if(!txtNameClient.getText().isEmpty()){
            jPanelProducto.setVisible(true);
            jPanelMotivo.setVisible(true);            
        }else{
            jPanelProducto.setVisible(false);
            jPanelMotivo.setVisible(false);
        }
    }
    
    private void buscarProducto(int idProduct){

        if(id_product == 0){
            JOptionPane.showMessageDialog(null,"Producto no encontrado");
            return;
        }

        qProduct.selectProduct(id_product, labelDescripcion,txtProductCode);
        qProduct.selectPriceAndIva(id_product, txtPrice, lblIva);

        cboCompensacion.setEnabled(true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupTipoDev = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboEntidad = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnRegistrarDevolucion = new javax.swing.JButton();
        jPanelProducto = new javax.swing.JPanel();
        jPanelMotivo = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        textObservaciones = new javax.swing.JTextArea();
        lblDescripcion = new javax.swing.JLabel();
        labelDescripcion1 = new javax.swing.JLabel();
        cboCompensacion = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        panelSearchProduct = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        labelDescripcion = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        labeliva2 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JFormattedTextField();
        jLabel32 = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        btnProduct = new javax.swing.JButton();
        btnBuscarProduct = new javax.swing.JButton();
        panelTableItems = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanelFactura = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        labelSupplier = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        labelNumber = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        labelFecha = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        lbl_product = new javax.swing.JLabel();
        txtFactura = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnBuscarFactura = new javax.swing.JButton();
        jPanelClientes = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        lbl_IVA_Client = new javax.swing.JLabel();
        txtCuitClient = new javax.swing.JTextField();
        btnSerchCuitClient = new javax.swing.JButton();
        btnSerchClient = new javax.swing.JButton();
        txtTelClient = new javax.swing.JTextField();
        txtNameClient = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        lbl_IdClient = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Gestión de Devoluciones");

        jLabel43.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(12, 83, 151));
        jLabel43.setText("Seleccione entidad:");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product-return128.png"))); // NOI18N

        cboEntidad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboEntidad.setForeground(new java.awt.Color(35, 35, 38));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrarDevolucion.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrarDevolucion.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrarDevolucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrarDevolucion.setText("Confirmar devolución");
        btnRegistrarDevolucion.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14))
        );

        jPanelProducto.setBackground(new java.awt.Color(255, 255, 255));

        jPanelMotivo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(35, 35, 38));
        jLabel30.setText("Tipo de compensación:");

        textObservaciones.setColumns(20);
        textObservaciones.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        textObservaciones.setForeground(new java.awt.Color(35, 35, 38));
        textObservaciones.setRows(5);
        textObservaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jScrollPane4.setViewportView(textObservaciones);

        lblDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(35, 35, 38));
        lblDescripcion.setText("Descripción:");

        labelDescripcion1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion1.setForeground(new java.awt.Color(35, 35, 38));
        labelDescripcion1.setText("-");

        cboCompensacion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCompensacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción" }));

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(35, 35, 38));
        jLabel28.setText("Cantidad: ");
        jLabel28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel28KeyTyped(evt);
            }
        });

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(35, 35, 38));
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanelMotivoLayout = new javax.swing.GroupLayout(jPanelMotivo);
        jPanelMotivo.setLayout(jPanelMotivoLayout);
        jPanelMotivoLayout.setHorizontalGroup(
            jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMotivoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanelMotivoLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 489, Short.MAX_VALUE))
                    .addGroup(jPanelMotivoLayout.createSequentialGroup()
                        .addComponent(lblDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelDescripcion1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelMotivoLayout.setVerticalGroup(
            jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMotivoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        panelSearchProduct.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(35, 35, 38));
        jLabel25.setText("Descripción:");

        labelDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion.setForeground(new java.awt.Color(12, 83, 151));
        labelDescripcion.setText("-");

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(35, 35, 38));
        jLabel29.setText("Precio de venta:");
        jLabel29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel29KeyTyped(evt);
            }
        });

        labeliva2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/$32.png"))); // NOI18N

        txtPrice.setBackground(new java.awt.Color(255, 255, 255));
        txtPrice.setBorder(null);
        txtPrice.setForeground(new java.awt.Color(12, 83, 151));
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        txtPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(35, 35, 38));
        jLabel32.setText("IVA:");
        jLabel32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel32KeyTyped(evt);
            }
        });

        lblIva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblIva.setForeground(new java.awt.Color(12, 83, 151));
        lblIva.setText("-");
        lblIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lblIvaKeyTyped(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(35, 35, 38));
        jLabel24.setText("Codigo de producto: ");

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setForeground(new java.awt.Color(35, 35, 38));
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        javax.swing.GroupLayout panelSearchProductLayout = new javax.swing.GroupLayout(panelSearchProduct);
        panelSearchProduct.setLayout(panelSearchProductLayout);
        panelSearchProductLayout.setHorizontalGroup(
            panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchProductLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labeliva2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSearchProductLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSearchProductLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSearchProductLayout.setVerticalGroup(
            panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchProductLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSearchProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        panelTableItems.setBackground(new java.awt.Color(255, 255, 255));

        tableItems.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableItems.setForeground(new java.awt.Color(35, 35, 38));
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
        jScrollPane1.setViewportView(tableItems);

        javax.swing.GroupLayout panelTableItemsLayout = new javax.swing.GroupLayout(panelTableItems);
        panelTableItems.setLayout(panelTableItemsLayout);
        panelTableItemsLayout.setHorizontalGroup(
            panelTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTableItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelTableItemsLayout.setVerticalGroup(
            panelTableItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelProductoLayout = new javax.swing.GroupLayout(jPanelProducto);
        jPanelProducto.setLayout(jPanelProductoLayout);
        jPanelProductoLayout.setHorizontalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addComponent(panelTableItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanelProductoLayout.setVerticalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductoLayout.createSequentialGroup()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelTableItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jPanelMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanelFactura.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(35, 35, 38));
        jLabel46.setText("Factura nº:");

        jLabel39.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(35, 35, 38));
        jLabel39.setText("Proveedor:");

        labelSupplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelSupplier.setForeground(new java.awt.Color(35, 35, 38));
        labelSupplier.setText("-");

        jLabel45.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(35, 35, 38));
        jLabel45.setText("Tipo:");

        labelType.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelType.setForeground(new java.awt.Color(35, 35, 38));
        labelType.setText("-");

        jLabel47.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(35, 35, 38));
        jLabel47.setText("Número:");

        labelNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelNumber.setForeground(new java.awt.Color(35, 35, 38));
        labelNumber.setText("-");

        jLabel38.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(35, 35, 38));
        jLabel38.setText("Fecha:");

        labelFecha.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelFecha.setForeground(new java.awt.Color(35, 35, 38));
        labelFecha.setText("-");

        txtSerie.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSerie.setForeground(new java.awt.Color(35, 35, 38));

        lbl_product.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_product.setForeground(new java.awt.Color(35, 35, 38));
        lbl_product.setText("-");

        txtFactura.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtFactura.setForeground(new java.awt.Color(35, 35, 38));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        btnBuscarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanelFacturaLayout = new javax.swing.GroupLayout(jPanelFactura);
        jPanelFactura.setLayout(jPanelFacturaLayout);
        jPanelFacturaLayout.setHorizontalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFacturaLayout.createSequentialGroup()
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelType, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel46)
                        .addGap(3, 3, 3)
                        .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(lbl_product)
                        .addGap(3, 3, 3)
                        .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelFacturaLayout.setVerticalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel46)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_product, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelClientes.setBackground(new java.awt.Color(255, 255, 255));

        jLabel62.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(35, 35, 38));
        jLabel62.setText("C.U.I.T./D.N.I.");

        jLabel63.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(35, 35, 38));
        jLabel63.setText("Nombre:");

        jLabel64.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(35, 35, 38));
        jLabel64.setText("I.V.A.:");

        jLabel65.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel65.setText("Teléfono:");

        lbl_IVA_Client.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_IVA_Client.setForeground(new java.awt.Color(35, 35, 38));
        lbl_IVA_Client.setText("-");

        txtCuitClient.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCuitClient.setForeground(new java.awt.Color(35, 35, 38));
        txtCuitClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCuitClientKeyTyped(evt);
            }
        });

        btnSerchCuitClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        btnSerchClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N

        txtTelClient.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTelClient.setForeground(new java.awt.Color(35, 35, 38));
        txtTelClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelClientKeyTyped(evt);
            }
        });

        txtNameClient.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtNameClient.setForeground(new java.awt.Color(35, 35, 38));

        jLabel66.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel66.setText("Id cliente:");

        lbl_IdClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_IdClient.setText("-");

        javax.swing.GroupLayout jPanelClientesLayout = new javax.swing.GroupLayout(jPanelClientes);
        jPanelClientes.setLayout(jPanelClientesLayout);
        jPanelClientesLayout.setHorizontalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCuitClient, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSerchCuitClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_IVA_Client, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel66)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_IdClient, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(433, 433, 433))))
        );
        jPanelClientesLayout.setVerticalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IVA_Client, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IdClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 1090, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanelClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jPanelProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel28KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel28KeyTyped
        int key = evt.getKeyChar();
        boolean numero = key >= 48 && key <= 57;
        if(!numero){
            evt.consume();
        }
        if (txtCantidad.getText().trim().length()==3){
            evt.consume();
        }
    }//GEN-LAST:event_jLabel28KeyTyped

    private void jLabel29KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel29KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel29KeyTyped

    private void jLabel32KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel32KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel32KeyTyped

    private void lblIvaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblIvaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_lblIvaKeyTyped

    private void txtCuitClientKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCuitClientKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtCuitClient.getText().length() >= 11) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCuitClientKeyTyped

    private void txtTelClientKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelClientKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtTelClient.getText().length() >= 13) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelClientKeyTyped

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtCantidad.getText().length() >= 3) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {     
            id_product = qProduct.selectIdProduct(txtProductCode.getText().trim());
            buscarProducto(id_product);
        }  
    }//GEN-LAST:event_txtProductCodeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupTipoDev;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarFactura;
    private javax.swing.JButton btnBuscarProduct;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnSerchClient;
    private javax.swing.JButton btnSerchCuitClient;
    private javax.swing.JComboBox<String> cboCompensacion;
    private javax.swing.JComboBox<String> cboEntidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelClientes;
    private javax.swing.JPanel jPanelFactura;
    private javax.swing.JPanel jPanelMotivo;
    private javax.swing.JPanel jPanelProducto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelDescripcion1;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelNumber;
    private javax.swing.JLabel labelSupplier;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel labeliva2;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lbl_IVA_Client;
    private javax.swing.JLabel lbl_IdClient;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JPanel panelSearchProduct;
    private javax.swing.JPanel panelTableItems;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextArea textObservaciones;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCuitClient;
    private javax.swing.JTextField txtFactura;
    private javax.swing.JTextField txtNameClient;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtTelClient;
    // End of variables declaration//GEN-END:variables
}
