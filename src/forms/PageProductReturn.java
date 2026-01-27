/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import Class.ProductDAO;
import OldNotUsed.frmProducts;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;


public class PageProductReturn extends javax.swing.JPanel {

    ProductDAO queries = new ProductDAO();

    private int id = 0; 
    
    public PageProductReturn() {
        initComponents();
        
        cargarComboCompensacion();
        inicializar();
        actions();
        clear();
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
        groupDisabled();      
    }
    
    private void groupDisabled(){
        btnSerchClient.setEnabled(false);
        txtCuit.setEnabled(false);
        btnSerchCuit.setEnabled(false);
        cboIVA.setEnabled(false);
        txtTel.setEnabled(false);
        txtName.setEnabled(false);
        cboCompensacion.setEnabled(false);
    }
    
    private void groupEnabled(){
        btnSerchClient.setEnabled(true);
        txtCuit.setEnabled(true);
        btnSerchCuit.setEnabled(true);
        cboIVA.setEnabled(true);
        txtTel.setEnabled(true);
        txtName.setEnabled(true);
    }
    
    private void buscarProducto(){
        id = queries.selectIdProduct(txtProductCode.getText());       
        queries.selectProduct(id, labelDescripcion,txtProductCode);
        queries.selectProductPriceEdit(id, txtPrice, lblIva);
    }
    
    private void cargarComboCompensacion() {
        cboCompensacion.removeAllItems();
        cboCompensacion.addItem("Seleccione una opción");
        cboCompensacion.addItem("Nota de Crédito");
        cboCompensacion.addItem("Reintegro de dinero");
        cboCompensacion.addItem("Cambio por otro producto igual");
    }

    public void clear(){
        GrupTipoDev.clearSelection();
        cboIVA.setSelectedIndex(0);
        lblFecha.setText("");
        lblComprobante.setText("");
        txtFact.setText("");
        lblMetPago.setText("");
        lblTotal.setText("");
        txtCuit.setText("");
        lblClient.setText("");
        txtTel.setText("");
        txtName.setText("");
        txtProductCode.setText("");
        txtCantidad.setText("");
        labelDescripcion.setText("");
        txtPrice.setText("");
        lblIva.setText("");
        cboCompensacion.setSelectedIndex(0);
        labelDescripcion1.setText("");
        textObservaciones.setText("");       
        groupDisabled();
        txtFact.setEnabled(true);
        btnSerchFact.setEnabled(true);
        checkBox.setSelected(false);
        lblDescripcion.setVisible(false);
    }
    
    
    void actions(){
        
        btnProduct.addActionListener(e->{
            buscarProducto();
            cboCompensacion.setEnabled(true);
        });
        
        btnCancel.addActionListener(e->{
            clear();
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
            double cantidad;
            try {
                cantidad = Double.parseDouble(txtCantidad.getText().trim());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresá un número mayor a cero en Cantidad."); 
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresá un número válido en Cantidad."); 
                return;
            }

            double Stock = queries.selectStock(id);

            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Deseás realizar la devolución?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            if (!rbtnClient.isSelected() && !rbtnProv.isSelected()) {
                JOptionPane.showMessageDialog(null, "Seleccione una opción en: Tipo devolución!...");
                return;
            }

            int seleccion = cboCompensacion.getSelectedIndex();
            if (seleccion <= 0) { 
                JOptionPane.showMessageDialog(null, "Por favor, seleccioná una opción válida en Compensación.");
                return;
            }

            if (rbtnClient.isSelected()) {
                switch (seleccion) {
                    case 1: // Nota de Crédito
                        Stock = Stock + cantidad;
                        queries.updateStockProduct(id, Stock);
                        break;
                    case 2: // Reintegro de dinero
                        Stock = Stock + cantidad;
                        queries.updateStockProduct(id, Stock);
                        // Aplicar reintegro y descontar dinero de caja
                        break;
                    case 3: // Cambio por otro producto igual
                        // Agregar a registro el cambio por otro equipo igual
                        break;
                }
            } else if (rbtnProv.isSelected()) {
                switch (seleccion) {
                    case 1: // Nota de Dévito
                        Stock = Stock - cantidad;
                        queries.updateStockProduct(id, Stock);
                        break;
                    case 2: // ingreso de dinero
                        Stock = Stock - cantidad;
                        queries.updateStockProduct(id, Stock);
                        // Aplicar ingreso y aumentar dinero en caja
                        break;
                    case 3: // Cambio por otro producto igual
                        // Agregar a registro el cambio por otro equipo igual
                        break;
                }
            }
            JOptionPane.showMessageDialog(null, "Devolución registrada con exito!...");
            inicializar();
//            txtCantidad.setText("");
//            cboCompensacion.setSelectedIndex(0);
//            labelDescripcion.setText("-");
//            txtPrice.setText("");
//            lblIva.setText("-");
//            txtProductCode.setText("");
//            GrupTipoDev.clearSelection();
            clear();
        });
        
    checkBox.addItemListener(e -> {
        if (checkBox.isSelected()) {
            groupEnabled();
            
            txtFact.setText("");
            lblFecha.setText("");
            lblComprobante.setText("");
            lblMetPago.setText("");
            lblTotal.setText("");
            txtFact.setEnabled(false);
            btnSerchFact.setEnabled(false);           
            
        } else {
            groupDisabled();
            
            txtFact.setEnabled(true);
            btnSerchFact.setEnabled(true);
        }
    }); 
        
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupTipoDev = new javax.swing.ButtonGroup();
        jPanel22 = new javax.swing.JPanel();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        btnSerchClient = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtCuit = new javax.swing.JTextField();
        btnSerchCuit = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        txtName = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTel = new javax.swing.JTextField();
        lblClient = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        rbtnProv = new javax.swing.JRadioButton();
        rbtnClient = new javax.swing.JRadioButton();
        jPanel26 = new javax.swing.JPanel();
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
        jPanel29 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        cboCompensacion = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        textObservaciones = new javax.swing.JTextArea();
        lblDescripcion = new javax.swing.JLabel();
        labelDescripcion1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrarDevolucion = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel24.setText("Codigo de producto: ");

        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jTable1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addComponent(jLabel24)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(labeliva2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel32)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

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

        jLabel42.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel42.setText("C.U.I.T./D.N.I.");

        jLabel43.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel43.setText("Apellido y nombre/Razón social:");

        txtCuit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCuitActionPerformed(evt);
            }
        });

        btnSerchCuit.setBackground(new java.awt.Color(255, 255, 255));
        btnSerchCuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSerchCuit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSerchCuit.setBorderPainted(false);
        btnSerchCuit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCuitActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel44.setText("Condición frente al I.V.A.");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CONSUMIDOR FINAL", "RESPONSABLE INSCRIPTO", "EXCENTO", "MONOTRIBUTISTA" }));
        cboIVA.setBorder(null);
        cboIVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIVAActionPerformed(evt);
            }
        });

        txtName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel48.setText("Teléfono:");

        jLabel49.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel49.setText("Nº de cliente:");

        txtTel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblClient.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblClient.setText("-");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addGap(128, 128, 128)
                                        .addComponent(btnSerchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addComponent(jLabel49)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblClient)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addComponent(jLabel48)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTel, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))))))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCuit, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerchCuit)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(btnSerchCuit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSerchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCuit, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));

        rbtnProv.setBackground(new java.awt.Color(255, 255, 255));
        GrupTipoDev.add(rbtnProv);
        rbtnProv.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        rbtnProv.setForeground(new java.awt.Color(101, 129, 171));
        rbtnProv.setText("Proveedor");
        rbtnProv.setBorder(null);

        rbtnClient.setBackground(new java.awt.Color(255, 255, 255));
        GrupTipoDev.add(rbtnClient);
        rbtnClient.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        rbtnClient.setForeground(new java.awt.Color(101, 129, 171));
        rbtnClient.setText("Cliente");
        rbtnClient.setBorder(null);
        rbtnClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(608, 608, 608)
                .addComponent(rbtnClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(rbtnProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(587, 587, 587))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(rbtnProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtnClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Facturación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

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

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFact, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblComprobante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMetPago))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(btnSerchFact)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFecha)))
                .addGap(131, 131, 131)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal))
                    .addComponent(checkBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(checkBox)
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchFact)
                    .addComponent(txtFact, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMetPago, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Motivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

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

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(lblDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelDescripcion1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboCompensacion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(703, 703, 703)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegistrarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnSerchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchClientActionPerformed

    private void btnSerchCuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCuitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchCuitActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void rbtnClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnClientActionPerformed

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

    private void cboIVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIVAActionPerformed

    private void btnSerchFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchFactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchFactActionPerformed

    private void txtCuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCuitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCuitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupTipoDev;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnSerchClient;
    private javax.swing.JButton btnSerchCuit;
    private javax.swing.JButton btnSerchFact;
    private javax.swing.JComboBox<String> cboCompensacion;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JCheckBox checkBox;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelDescripcion1;
    private javax.swing.JLabel labeliva2;
    private javax.swing.JLabel lblClient;
    private javax.swing.JLabel lblComprobante;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lblMetPago;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JRadioButton rbtnClient;
    private javax.swing.JRadioButton rbtnProv;
    private javax.swing.JTextArea textObservaciones;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCuit;
    private javax.swing.JTextField txtFact;
    private javax.swing.JTextField txtName;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
}
