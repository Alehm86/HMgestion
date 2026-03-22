/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import dao.productDAO;
import dao.customerDAO;
import dao.genericDAO;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class productReturnPanel extends javax.swing.JPanel {

    productDAO queriesProduct = new productDAO();
    customerDAO queriesCustomer = new customerDAO();
    genericDAO queriesGeneric = new genericDAO();

    private int id_product = 0; 
    private String client;
    
    public productReturnPanel() {
        initComponents();
        
        cboEntidad.removeAllItems();
        cboEntidad.addItem("Seleccione una opción");
        cboEntidad.addItem("Proveedor");
        cboEntidad.addItem("Cliente");
        
        queriesGeneric.llenarCombos(cboSupplier, "suppliers");
        cboSupplier.addItem("Otro");
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
        jPanelProveedor.setVisible(false);
        jPanelFactura.setVisible(false);
        jPanelProducto.setVisible(false);
        jPanelMotivo.setVisible(false);
                
        jPanelSeparador2.setVisible(false);
        
        txt_name_supplier.setVisible(false);
        
    }
    
    private void buscarProducto(){

        id_product = queriesProduct.selectIdProduct(txtProductCode.getText().trim());

        if(id_product == 0){
            JOptionPane.showMessageDialog(null,"Producto no encontrado");
            return;
        }

        queriesProduct.selectProduct(id_product, labelDescripcion,txtProductCode);
        queriesProduct.selectPriceAndIva(id_product, txtPrice, lblIva);

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
                
                jPanelProveedor.setVisible(true);
                jPanelSeparador2.setVisible(true);
                jPanelFactura.setVisible(true);                 
                jPanelClientes.setVisible(false); 
                
            }else if(cboEntidad.getSelectedIndex()==2){
                
                jPanelClientes.setVisible(true);   
                jPanelProveedor.setVisible(false);
                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false); 
                
            }else if(cboEntidad.getSelectedIndex()==0){
                
                jPanelClientes.setVisible(false);   
                jPanelProveedor.setVisible(false);
                jPanelSeparador2.setVisible(false);
                jPanelFactura.setVisible(false);
            }
            
        });
        
        btnProduct.addActionListener(e->{
            buscarProducto();
            cboCompensacion.setEnabled(true);
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
        
        cboSupplier.addActionListener(e->{
            
            String nameSupplier = cboSupplier.getSelectedItem().toString();

            if(!nameSupplier.equals("Otro")){
                txt_name_supplier.setVisible(false);
                queriesProduct.select_supplier(nameSupplier, lbl_id_supplier, lbl_Iva_supplier, lbl_cuit_supplier, lbl_phone_supplier);
            }else{
                txt_name_supplier.setVisible(true);
                txt_name_supplier.requestFocus();
                lbl_cuit_supplier.setText("");
                lbl_id_supplier.setText("");
                lbl_Iva_supplier.setText("");
                lbl_phone_supplier.setText("");
            }
            
            jPanelProducto.setVisible(true);
            jPanelMotivo.setVisible(true);
       
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

            int stock = queriesProduct.selectStock(id_product);
            int entidad = cboEntidad.getSelectedIndex();

            if (entidad == 2) {        // Cliente
                stock = stock + cantidad;
            } 
            else if (entidad == 1) {   // Proveedor
                stock = stock - cantidad;
            }

            // Actualizar stock solo si corresponde
            if (compensacion == 1 || compensacion == 2) {
                queriesProduct.updateStockProduct(id_product, stock);
            }

// ACTUALIZAR CUANDO ESTE EL MODULO CAJA
//            switch (compensacion) {
//
//                case 1: // Nota de crédito / débito
//                    // registrar comprobante
//                    break;
//
//                case 2: // Movimiento de dinero
//                    if (entidad == 2) {
//                        // reintegro al cliente (sale dinero de caja)
//                    } else {
//                        // ingreso de dinero (entra dinero a caja)
//                    }
//                    break;
//
//                case 3: // Cambio por producto
//                    // registrar cambio
//                    break;
//            }

            JOptionPane.showMessageDialog(null, "Devolución registrada con éxito.");

            limpiarFormulario();
        });
        
        checkBox.addItemListener(e -> {
            if (checkBox.isSelected()) {
                cboCompensacion.setEnabled(true);

                txtFact.setText("");
                lblFecha.setText("");
                lblComprobante.setText("");
                lblMetPago.setText("");
                lblTotal.setText("");
                txtFact.setEnabled(false);
                btnSerchFact.setEnabled(false);           

            } else {
                cboCompensacion.setEnabled(false);

                txtFact.setEnabled(true);
                btnSerchFact.setEnabled(true);
            }
        }); 
      
    }
    
    private void limpiarFormulario() {

        inicializar();

        GrupTipoDev.clearSelection();

        txtFact.setText("");
        lblFecha.setText("");
        lblComprobante.setText("");
        lblMetPago.setText("");
        lblTotal.setText("");

        txtProductCode.setText("");
        txtCantidad.setText("");
        txtPrice.setText("");

        lblIva.setText("");
        labelDescripcion.setText("");

        textObservaciones.setText("");

        cboCompensacion.setSelectedIndex(0);
    }

    private void buscarCliente(){
        queriesCustomer.selectClient(client, lbl_IdClient, txtNameClient, txtCuitClient, txtTelClient, lbl_IVA_Client);   
        
        if(!txtNameClient.getText().isEmpty()){
            jPanelProducto.setVisible(true);
            jPanelMotivo.setVisible(true);            
        }else{
            jPanelProducto.setVisible(false);
            jPanelMotivo.setVisible(false);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupTipoDev = new javax.swing.ButtonGroup();
        jPanelProveedor = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        lbl_id_supplier = new javax.swing.JLabel();
        lbl_phone_supplier = new javax.swing.JLabel();
        cboSupplier = new javax.swing.JComboBox<>();
        lbl_Iva_supplier = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lbl_cuit_supplier = new javax.swing.JLabel();
        txt_name_supplier = new javax.swing.JTextField();
        jPanelFactura = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        txtFact = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        btnSerchFact = new javax.swing.JButton();
        lblComprobante = new javax.swing.JLabel();
        lblMetPago = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        checkBox = new javax.swing.JCheckBox();
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

        jPanelProveedor.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel42.setText("C.U.I.T.");

        jLabel48.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel48.setText("Teléfono:");

        jLabel49.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel49.setText("Nº de proveedor:");

        lbl_id_supplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_id_supplier.setText("-");

        lbl_phone_supplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_phone_supplier.setText("-");

        cboSupplier.setBackground(new java.awt.Color(255, 255, 255));
        cboSupplier.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        cboSupplier.setForeground(new java.awt.Color(12, 83, 151));
        cboSupplier.setBorder(null);

        lbl_Iva_supplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_Iva_supplier.setText("-");

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel44.setText("I.V.A. :");

        lbl_cuit_supplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_cuit_supplier.setText("-");

        txt_name_supplier.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        txt_name_supplier.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_name_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_name_supplierActionPerformed(evt);
            }
        });
        txt_name_supplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_name_supplierKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelProveedorLayout = new javax.swing.GroupLayout(jPanelProveedor);
        jPanelProveedor.setLayout(jPanelProveedorLayout);
        jPanelProveedorLayout.setHorizontalGroup(
            jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProveedorLayout.createSequentialGroup()
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_name_supplier))
                    .addGroup(jPanelProveedorLayout.createSequentialGroup()
                        .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_Iva_supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_cuit_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_phone_supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_id_supplier, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))))
                .addGap(161, 161, 161))
        );
        jPanelProveedorLayout.setVerticalGroup(
            jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_name_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cuit_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_id_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Iva_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_phone_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFactura.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel46.setText("Factura nº:");

        txtFact.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel50.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel50.setText("Fecha:");

        lblFecha.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblFecha.setText("-");

        jLabel52.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel52.setText("Comprobante");

        jLabel51.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel51.setText("Metodo de pago:");

        jLabel36.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 0, 0));
        jLabel36.setText("FALTA PROGRAMAR");

        btnSerchFact.setBackground(new java.awt.Color(255, 255, 255));
        btnSerchFact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSerchFact.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSerchFact.setBorderPainted(false);
        btnSerchFact.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchFactActionPerformed(evt);
            }
        });

        lblComprobante.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblComprobante.setText("-");

        lblMetPago.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblMetPago.setText("-");

        jLabel53.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel53.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblTotal.setText("-");

        checkBox.setBackground(new java.awt.Color(255, 255, 255));
        checkBox.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        checkBox.setForeground(new java.awt.Color(101, 129, 171));
        checkBox.setText("Sin factura");
        checkBox.setBorder(null);
        checkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFacturaLayout = new javax.swing.GroupLayout(jPanelFactura);
        jPanelFactura.setLayout(jPanelFacturaLayout);
        jPanelFacturaLayout.setHorizontalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFact, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchFact))
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblComprobante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMetPago))
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFecha)))
                .addGap(131, 131, 131)
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFacturaLayout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal))
                    .addComponent(checkBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanelFacturaLayout.setVerticalGroup(
            jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(checkBox)
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchFact)
                    .addComponent(txtFact, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMetPago, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanelMotivo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel30.setText("Tipo de compensación:");

        cboCompensacion.setBackground(new java.awt.Color(255, 255, 255));
        cboCompensacion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCompensacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción" }));
        cboCompensacion.setBorder(null);
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
        lblDescripcion.setText("Descripción:");

        labelDescripcion1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
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
                    .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMotivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 102, 102));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
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

        btnRegistrarDevolucion.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrarDevolucion.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrarDevolucion.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrarDevolucion.setText("Confirmar devolución");
        btnRegistrarDevolucion.setBorder(null);
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

        jPanel2.setBackground(new java.awt.Color(12, 83, 151));

        cboEntidad.setBackground(new java.awt.Color(255, 255, 255));
        cboEntidad.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        cboEntidad.setForeground(new java.awt.Color(12, 83, 151));
        cboEntidad.setBorder(null);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gestión de Devoluciones");

        jLabel43.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Seleccione entidad:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );

        jPanelProducto.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel24.setText("Codigo de producto: ");

        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel25.setText("Descripción:");

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadjLabel28KeyTyped(evt);
            }
        });

        labelDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion.setText("-");

        btnProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnProduct.setBorder(null);
        btnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setText("Cantidad: ");
        jLabel28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel28KeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setText("Precio de venta:");
        jLabel29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel29KeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel32.setText("IVA:");
        jLabel32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabel32KeyTyped(evt);
            }
        });

        lblIva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblIva.setText("-");
        lblIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lblIvaKeyTyped(evt);
            }
        });

        txtPrice.setBackground(new java.awt.Color(255, 255, 255));
        txtPrice.setBorder(null);
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        txtPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        labeliva2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva2.setText("$");

        javax.swing.GroupLayout jPanelProductoLayout = new javax.swing.GroupLayout(jPanelProducto);
        jPanelProducto.setLayout(jPanelProductoLayout);
        jPanelProductoLayout.setHorizontalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelProductoLayout.createSequentialGroup()
                        .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelProductoLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelProductoLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labeliva2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 126, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelProductoLayout.setVerticalGroup(
            jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProductoLayout.createSequentialGroup()
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(169, Short.MAX_VALUE))
        );

        jPanelClientes.setBackground(new java.awt.Color(255, 255, 255));

        btnSerchClient.setBackground(new java.awt.Color(255, 255, 255));
        btnSerchClient.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnSerchClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerchClient.setBorder(null);
        btnSerchClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchClientActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel62.setText("C.U.I.T./D.N.I.");

        jLabel63.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel63.setText("Apellido y nombre/Razón social:");

        txtCuitClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
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
        btnSerchCuitClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCuitClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCuitClientActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel64.setText("Condición frente al I.V.A.");

        txtNameClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
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
        txtTelClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_IdClient.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_IdClient.setText("-");

        lbl_IVA_Client.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
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
                                .addComponent(btnSerchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(lbl_IdClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchCuitClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchClient, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IVA_Client, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanelSeparador2.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanelSeparador2Layout = new javax.swing.GroupLayout(jPanelSeparador2);
        jPanelSeparador2.setLayout(jPanelSeparador2Layout);
        jPanelSeparador2Layout.setHorizontalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2192, Short.MAX_VALUE)
        );
        jPanelSeparador2Layout.setVerticalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSeparador3.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanelSeparador3Layout = new javax.swing.GroupLayout(jPanelSeparador3);
        jPanelSeparador3.setLayout(jPanelSeparador3Layout);
        jPanelSeparador3Layout.setHorizontalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador3Layout.setVerticalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(687, Short.MAX_VALUE))
            .addComponent(jPanelSeparador2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelSeparador3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(jPanelSeparador3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelMotivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void checkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxActionPerformed

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

    private void btnSerchFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchFactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchFactActionPerformed

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

    private void txt_name_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_name_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_name_supplierActionPerformed

    private void txt_name_supplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_name_supplierKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_name_supplierKeyPressed

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            buscarProducto();
            cboCompensacion.setEnabled(true);
        }
    }//GEN-LAST:event_txtProductCodeKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupTipoDev;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnSerchClient;
    private javax.swing.JButton btnSerchCuitClient;
    private javax.swing.JButton btnSerchFact;
    private javax.swing.JComboBox<String> cboCompensacion;
    private javax.swing.JComboBox<String> cboEntidad;
    private javax.swing.JComboBox<String> cboSupplier;
    private javax.swing.JCheckBox checkBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
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
    private javax.swing.JPanel jPanelProveedor;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelDescripcion1;
    private javax.swing.JLabel labeliva2;
    private javax.swing.JLabel lblComprobante;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lblMetPago;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_IVA_Client;
    private javax.swing.JLabel lbl_IdClient;
    private javax.swing.JLabel lbl_Iva_supplier;
    private javax.swing.JLabel lbl_cuit_supplier;
    private javax.swing.JLabel lbl_id_supplier;
    private javax.swing.JLabel lbl_phone_supplier;
    private javax.swing.JTextArea textObservaciones;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCuitClient;
    private javax.swing.JTextField txtFact;
    private javax.swing.JTextField txtNameClient;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtTelClient;
    private javax.swing.JTextField txt_name_supplier;
    // End of variables declaration//GEN-END:variables
}
