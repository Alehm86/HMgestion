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
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


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
                
        jPanelSeparador2.setVisible(false);      
        
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
                
                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(true);                 
                jPanelClientes.setVisible(false); 
                
            }else if(cboEntidad.getSelectedIndex()==2){
                
                jPanelClientes.setVisible(true);   
                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false); 
                
            }else if(cboEntidad.getSelectedIndex()==0){
                
                jPanelClientes.setVisible(false);   
                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false);
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
                buscarFactura(idPurchase);
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
        jPanelFactura = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        btnBuscarFactura = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        labelSupplier = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        labelNumber = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        labelFecha = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFactura = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanelMotivo = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        cboCompensacion = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        textObservaciones = new javax.swing.JTextArea();
        lblDescripcion = new javax.swing.JLabel();
        labelDescripcion1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrarDevolucion = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cboEntidad = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelProducto = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        labelDescripcion = new javax.swing.JLabel();
        btnProduct = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        txtPrice = new javax.swing.JFormattedTextField();
        labeliva2 = new javax.swing.JLabel();
        btnBuscarProduct = new javax.swing.JButton();
        jPanelClientes = new javax.swing.JPanel();
        btnSerchClient = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        txtCuitClient = new javax.swing.JTextField();
        btnSerchCuitClient = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        txtNameClient = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtTelClient = new javax.swing.JTextField();
        lbl_IdClient = new javax.swing.JLabel();
        lbl_IVA_Client = new javax.swing.JLabel();
        jPanelSeparador2 = new javax.swing.JPanel();
        jPanelSeparador3 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanelFactura.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(35, 35, 38));
        jLabel46.setText("Factura nº:");

        btnBuscarFactura.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarFactura.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBuscarFactura.setForeground(new java.awt.Color(35, 35, 38));
        btnBuscarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnBuscarFactura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscarFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarFacturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarFacturaMouseExited(evt);
            }
        });

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
        txtSerie.setForeground(new java.awt.Color(65, 65, 63));
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtSerie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSerieKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSerieKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("-");

        txtFactura.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtFactura.setForeground(new java.awt.Color(65, 65, 63));
        txtFactura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFactura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFacturaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFacturaKeyTyped(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(35, 35, 38));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelFacturaLayout = new javax.swing.GroupLayout(jPanelFactura);
        jPanelFactura.setLayout(jPanelFacturaLayout);
        jPanelFacturaLayout.setHorizontalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelFacturaLayout.setVerticalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel46)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelMotivo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(35, 35, 38));
        jLabel30.setText("Tipo de compensación:");

        cboCompensacion.setBackground(new java.awt.Color(255, 255, 255));
        cboCompensacion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCompensacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción" }));
        cboCompensacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboCompensacion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboCompensacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCompensacionActionPerformed(evt);
            }
        });

        textObservaciones.setColumns(20);
        textObservaciones.setRows(5);
        textObservaciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jScrollPane4.setViewportView(textObservaciones);

        lblDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(35, 35, 38));
        lblDescripcion.setText("Descripción:");

        labelDescripcion1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion1.setForeground(new java.awt.Color(35, 35, 38));
        labelDescripcion1.setText("-");

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
                        .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 251, Short.MAX_VALUE))
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
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 102, 102));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
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

        btnRegistrarDevolucion.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrarDevolucion.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrarDevolucion.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrarDevolucion.setText("Confirmar devolución");
        btnRegistrarDevolucion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrarDevolucion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrarDevolucion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarDevolucionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarDevolucionMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(617, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(617, 617, 617))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        cboEntidad.setBackground(new java.awt.Color(255, 255, 255));
        cboEntidad.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        cboEntidad.setForeground(new java.awt.Color(65, 65, 63));
        cboEntidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Gestión de Devoluciones");

        jLabel43.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(35, 35, 38));
        jLabel43.setText("Seleccione entidad:");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product-return128.png"))); // NOI18N

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
                        .addComponent(cboEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelProducto.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(35, 35, 38));
        jLabel24.setText("Codigo de producto: ");

        txtProductCode.setForeground(new java.awt.Color(35, 35, 38));
        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(35, 35, 38));
        jLabel25.setText("Descripción:");

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(35, 35, 38));
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadjLabel28KeyTyped(evt);
            }
        });

        labelDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion.setForeground(new java.awt.Color(35, 35, 38));
        labelDescripcion.setText("-");

        btnProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProductMouseExited(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(35, 35, 38));
        jLabel28.setText("Cantidad: ");
        jLabel28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel28KeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(35, 35, 38));
        jLabel29.setText("Precio de venta:");
        jLabel29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel29KeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(35, 35, 38));
        jLabel32.setText("IVA:");
        jLabel32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel32KeyTyped(evt);
            }
        });

        lblIva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblIva.setForeground(new java.awt.Color(35, 35, 38));
        lblIva.setText("-");
        lblIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lblIvaKeyTyped(evt);
            }
        });

        txtPrice.setBackground(new java.awt.Color(255, 255, 255));
        txtPrice.setBorder(null);
        txtPrice.setForeground(new java.awt.Color(35, 35, 38));
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        txtPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        labeliva2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva2.setText("$");

        btnBuscarProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32_1.png"))); // NOI18N
        btnBuscarProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscarProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarProductMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelProductoLayout = new javax.swing.GroupLayout(jPanelProducto);
        jPanelProducto.setLayout(jPanelProductoLayout);
        jPanelProductoLayout.setHorizontalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(245, Short.MAX_VALUE))
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelProductoLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelProductoLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labeliva2)
                                .addGap(0, 0, 0)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelProductoLayout.setVerticalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProductoLayout.createSequentialGroup()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jPanelClientes.setBackground(new java.awt.Color(255, 255, 255));

        btnSerchClient.setBackground(new java.awt.Color(255, 255, 255));
        btnSerchClient.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnSerchClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N
        btnSerchClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSerchClient.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSerchClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSerchClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSerchClientMouseExited(evt);
            }
        });
        btnSerchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchClientActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(35, 35, 38));
        jLabel62.setText("C.U.I.T./D.N.I.");

        jLabel63.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(35, 35, 38));
        jLabel63.setText("Apellido y nombre/Razón social:");

        txtCuitClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtCuitClient.setForeground(new java.awt.Color(35, 35, 38));
        txtCuitClient.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCuitClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCuitClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCuitClientActionPerformed(evt);
            }
        });
        txtCuitClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCuitClientKeyPressed(evt);
            }
        });

        btnSerchCuitClient.setBackground(new java.awt.Color(255, 255, 255));
        btnSerchCuitClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSerchCuitClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSerchCuitClient.setBorderPainted(false);
        btnSerchCuitClient.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSerchCuitClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCuitClientActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(35, 35, 38));
        jLabel64.setText("Condición frente al I.V.A.");

        txtNameClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtNameClient.setForeground(new java.awt.Color(35, 35, 38));
        txtNameClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNameClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameClientActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel65.setText("Teléfono:");

        jLabel66.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel66.setText("Id cliente:");

        txtTelClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txtTelClient.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_IdClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_IdClient.setText("-");

        lbl_IVA_Client.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_IVA_Client.setForeground(new java.awt.Color(35, 35, 38));
        lbl_IVA_Client.setText("-");

        javax.swing.GroupLayout jPanelClientesLayout = new javax.swing.GroupLayout(jPanelClientes);
        jPanelClientes.setLayout(jPanelClientesLayout);
        jPanelClientesLayout.setHorizontalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelClientesLayout.createSequentialGroup()
                                .addComponent(jLabel64)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_IVA_Client, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelClientesLayout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(txtCuitClient, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSerchCuitClient)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSerchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelClientesLayout.createSequentialGroup()
                                .addComponent(jLabel66)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_IdClient)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanelClientesLayout.createSequentialGroup()
                                .addComponent(jLabel65)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelClient, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))))
                    .addGroup(jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNameClient)))
                .addContainerGap())
        );
        jPanelClientesLayout.setVerticalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IdClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IVA_Client, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanelSeparador2.setBackground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanelSeparador2Layout = new javax.swing.GroupLayout(jPanelSeparador2);
        jPanelSeparador2.setLayout(jPanelSeparador2Layout);
        jPanelSeparador2Layout.setHorizontalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2192, Short.MAX_VALUE)
        );
        jPanelSeparador2Layout.setVerticalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanelSeparador3.setBackground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanelSeparador3Layout = new javax.swing.GroupLayout(jPanelSeparador3);
        jPanelSeparador3.setLayout(jPanelSeparador3Layout);
        jPanelSeparador3Layout.setHorizontalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador3Layout.setVerticalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSeparador2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelSeparador3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanelSeparador3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelMotivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCantidadjLabel28KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadjLabel28KeyTyped
        int key = evt.getKeyChar();
        boolean numero = key >= 48 && key <= 57;
        if(!numero){
            evt.consume();
        }
        if (txtCantidad.getText().trim().length()==3){
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadjLabel28KeyTyped

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

    private void btnRegistrarDevolucionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarDevolucionMouseEntered
        btnRegistrarDevolucion.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarDevolucionMouseEntered

    private void btnRegistrarDevolucionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarDevolucionMouseExited
        btnRegistrarDevolucion.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarDevolucionMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void cboCompensacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCompensacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCompensacionActionPerformed

    private void btnSerchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchClientActionPerformed

    private void txtCuitClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCuitClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCuitClientActionPerformed

    private void btnSerchCuitClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCuitClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchCuitClientActionPerformed

    private void txtNameClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameClientActionPerformed

    private void txtCuitClientKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCuitClientKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            client = txtCuitClient.getText().trim();
            buscarCliente();
        }   
        
        txtCuitClient.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCuitClient.getText().length() >= 11) {
                    e.consume();
                }
            }
        });        
    }//GEN-LAST:event_txtCuitClientKeyPressed

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            id_product = qProduct.selectIdProduct(txtProductCode.getText().trim());
            buscarProducto(id_product);
            
        }
    }//GEN-LAST:event_txtProductCodeKeyPressed

    private void btnBuscarProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseEntered
        btnBuscarProduct.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarProductMouseEntered

    private void btnBuscarProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseExited
        btnBuscarProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarProductMouseExited

    private void txtSerieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerieKeyPressed

    }//GEN-LAST:event_txtSerieKeyPressed

    private void txtSerieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerieKeyTyped
        txtSerie.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtSerie.getText().length() >= 4) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtSerieKeyTyped

    private void txtFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            numfact = txtSerie.getText().trim()+"-"+txtFactura.getText().trim();
            idPurchase = qPurchase.selectIdPurchase(numfact);
            buscarFactura(idPurchase);
        }
    }//GEN-LAST:event_txtFacturaKeyPressed

    private void txtFacturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyTyped
        txtFactura.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtFactura.getText().length() >= 8) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtFacturaKeyTyped

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        btnBuscar.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        btnBuscar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnBuscarFacturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarFacturaMouseEntered
        btnBuscarFactura.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarFacturaMouseEntered

    private void btnBuscarFacturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarFacturaMouseExited
        btnBuscarFactura.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarFacturaMouseExited

    private void btnSerchClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchClientMouseEntered
        btnSerchClient.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnSerchClientMouseEntered

    private void btnSerchClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchClientMouseExited
        btnSerchClient.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchClientMouseExited

    private void btnProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseEntered
        btnProduct.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnProductMouseEntered

    private void btnProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseExited
        btnProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnProductMouseExited


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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelClientes;
    private javax.swing.JPanel jPanelFactura;
    private javax.swing.JPanel jPanelMotivo;
    private javax.swing.JPanel jPanelProducto;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador3;
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
