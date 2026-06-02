/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.productDAO;
import javax.swing.SwingUtilities;
import java.awt.Frame;
import dao.purchaseInvoiceDAO;
import models.Price;
import models.Stocks;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

public class productStockPanel extends javax.swing.JPanel {

    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO(); 
    productDAO qProduct = new productDAO();
    
    Price price = new Price();
    Stocks mStock = new Stocks();
       
    int idPurchase = -1;
    String numfact = "";
          
    public productStockPanel() {
        initComponents();
        
        JTextField txtCant = new JTextField();
        txtCant.setHorizontalAlignment(JTextField.CENTER);
       
        btnRegistrar.setEnabled(false);
        btnCancel.setEnabled(false);
        
        buttonsActions();
        titlesButtons();
        inicializar();
    }
    
    private void titlesButtons(){

        btnBuscar.setToolTipText("Cargar factura.");
        btnBuscarLista.setToolTipText("Buscar de la lista.");
        btnCancel.setToolTipText("Cancelar");
    }

    private void buttonsActions(){
        
        btnBuscar.addActionListener(e -> {
            numfact = txtSerie.getText().trim()+"-"+txtFactura.getText().trim();
            idPurchase = qPurchase.selectIdPurchase(numfact);
            buscar();         
        });
        
        btnCancel.addActionListener(e -> {
            inicializar();         
        });      
                
        btnRegistrar.addActionListener(e -> {
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            registrarCambios(); 
        });     

        btnBuscarLista.addActionListener(e -> {
            
            purchaseInvoiceListDialog fPurchaseInv = new purchaseInvoiceListDialog(parent, true, 1);
            fPurchaseInv.setVisible(true);
            idPurchase = fPurchaseInv.getIdPurchase();
            
            if(idPurchase > 0){
                buscar();
            }
        });    
       
    }

    void inicializar(){
        labelSupplier.setText("");
        labelType.setText("-");
        labelNumber.setText("");
        labelFecha.setText("");
        labelSubtotal.setText("-");
        lbl_iva_105.setText("");
        lbl_iva_21.setText("");
        labelOtherTaxes.setText("-");
        labelPersepciones.setText("");
        lblDiscount.setText("");
        lblTotal.setText("");
        
        btnRegistrar.setEnabled(false);
        btnCancel.setEnabled(false);
        
        DefaultTableModel model = (DefaultTableModel) tableItems.getModel();
        model.setRowCount(0);       
    }   
   
    void buscar(){  
        
        boolean state = false;
        
        state = qPurchase.selectPuchaseInvoice(
                idPurchase, 
                labelType, 
                labelNumber, 
                labelSupplier, 
                labelFecha, 
                labelSubtotal, 
                labelPersepciones, 
                labelOtherTaxes, 
                lblDiscount, 
                lbl_iva_105, 
                lbl_iva_21, 
                lblTotal,
                tableItems
        );        
        
        if(state){
            btnRegistrar.setEnabled(true);
            btnCancel.setEnabled(true);
        }
        
    }
    
    void registrarCambios(){
      
        int stockAct = 0;
        int stockUp = 0;
        int stockNew = 0;
        int idProduct = -1;
      
        if(tableItems.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "No hay productos");
        }else{
            for(int i = 0; i < tableItems.getRowCount(); i++){                
                idProduct = Integer.parseInt(tableItems.getValueAt(i, 0).toString());
                stockNew = Integer.parseInt(tableItems.getValueAt(i, 1).toString());
                
                stockAct = qProduct.selectStockActual(idProduct);
                stockUp = stockAct + stockNew;
                qProduct.updateStockProduct(idProduct,stockUp);         
            }
        }
        qPurchase.updateStockCargadoInPurchase(idPurchase);
        inicializar();
       
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel35 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnBuscarLista = new javax.swing.JButton();
        txtFactura = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        labelNumber = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        labelFecha = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        labelSupplier = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        labelSubtotal = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lbl_iva_105 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl_iva_21 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        labelOtherTaxes = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        labelPersepciones = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();
        lblDiscount = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(680, 744));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        tableItems.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableItems.setForeground(new java.awt.Color(65, 65, 63));
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
        tableItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableItems.setFillsViewportHeight(true);
        tableItems.setRowHeight(30);
        jScrollPane5.setViewportView(tableItems);

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setToolTipText("");
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
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnRegistrar.setPreferredSize(new java.awt.Dimension(52, 52));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(35, 35, 38));
        jLabel36.setText("Nº de factura: ");

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

        btnBuscarLista.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarLista.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBuscarLista.setForeground(new java.awt.Color(35, 35, 38));
        btnBuscarLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnBuscarLista.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscarLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarListaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarListaMouseExited(evt);
            }
        });

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarLista, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnBuscarLista, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ingresoStock64.png"))); // NOI18N
        jLabel1.setText("Ingresar stock");

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

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(35, 35, 38));
        jLabel42.setText("Tipo:");

        labelType.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelType.setForeground(new java.awt.Color(35, 35, 38));
        labelType.setText("-");

        jLabel43.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(35, 35, 38));
        jLabel43.setText("Número:");

        labelNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelNumber.setForeground(new java.awt.Color(35, 35, 38));
        labelNumber.setText("-");

        jLabel38.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(35, 35, 38));
        jLabel38.setText("Fecha:");

        labelFecha.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelFecha.setForeground(new java.awt.Color(35, 35, 38));
        labelFecha.setText("-");

        jLabel39.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(35, 35, 38));
        jLabel39.setText("Proveedor:");

        labelSupplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelSupplier.setForeground(new java.awt.Color(35, 35, 38));
        labelSupplier.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel39)
                    .addComponent(jLabel43)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelType, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(labelNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 35, 38)));

        jLabel37.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(35, 35, 38));
        jLabel37.setText("Subtotal:");

        labelSubtotal.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelSubtotal.setForeground(new java.awt.Color(35, 35, 38));
        labelSubtotal.setText("-");

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(35, 35, 38));
        jLabel44.setText("Iva 10,5%:");

        lbl_iva_105.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_105.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_105.setText("-");

        lbl2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl2.setForeground(new java.awt.Color(35, 35, 38));
        lbl2.setText("Iva 21%:");

        lbl_iva_21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_21.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_21.setText("-");

        jLabel41.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(35, 35, 38));
        jLabel41.setText("Otros impuestos:");

        labelOtherTaxes.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelOtherTaxes.setForeground(new java.awt.Color(35, 35, 38));
        labelOtherTaxes.setText("-");

        jLabel40.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(35, 35, 38));
        jLabel40.setText("Persepciones:");

        labelPersepciones.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelPersepciones.setForeground(new java.awt.Color(35, 35, 38));
        labelPersepciones.setText("-");

        lbl.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl.setForeground(new java.awt.Color(35, 35, 38));
        lbl.setText("Descuento:");

        lblDiscount.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblDiscount.setForeground(new java.awt.Color(35, 35, 38));
        lblDiscount.setText("-");

        jLabel45.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(35, 35, 38));
        jLabel45.setText("Total");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(35, 35, 38));
        lblTotal.setText("-");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel44)
                    .addComponent(lbl2)
                    .addComponent(jLabel41)
                    .addComponent(jLabel40)
                    .addComponent(lbl)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(lbl_iva_105, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(lbl_iva_21, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(labelOtherTaxes, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(labelPersepciones, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(lblDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbl_iva_105, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbl_iva_21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelOtherTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPersepciones, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelSeparador4.setBackground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,102,102));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void txtSerieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerieKeyPressed

    }//GEN-LAST:event_txtSerieKeyPressed

    private void txtFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            numfact = txtSerie.getText().trim()+"-"+txtFactura.getText().trim();
            idPurchase = qPurchase.selectIdPurchase(numfact);
            buscar();
        } 
    }//GEN-LAST:event_txtFacturaKeyPressed

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

    private void btnBuscarListaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarListaMouseEntered
        btnBuscarLista.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarListaMouseEntered

    private void btnBuscarListaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarListaMouseExited
        btnBuscarLista.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarListaMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarLista;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelNumber;
    private javax.swing.JLabel labelOtherTaxes;
    private javax.swing.JLabel labelPersepciones;
    private javax.swing.JLabel labelSubtotal;
    private javax.swing.JLabel labelSupplier;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_iva_105;
    private javax.swing.JLabel lbl_iva_21;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextField txtFactura;
    private javax.swing.JTextField txtSerie;
    // End of variables declaration//GEN-END:variables
}
