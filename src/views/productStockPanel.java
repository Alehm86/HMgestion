/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.productDAO;
import javax.swing.SwingUtilities;
import java.awt.Frame;
import dao.purchaseInvoiceDAO;
import models.mPrice;
import models.mStocks;
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
    
    mPrice price = new mPrice();
    mStocks mStock = new mStocks();
       
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
        labelType.setText("");
        labelNumber.setText("");
        labelFecha.setText("");
        labelSubtotal.setText("");
        lbl_iva_105.setText("");
        lbl_iva_21.setText("");
        labelOtherTaxes.setText("");
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
        jLabel2 = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        txtFactura = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnBuscarLista = new javax.swing.JButton();
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

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(803, 754));

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

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(35, 35, 38));
        jLabel36.setText("Nº de factura: ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("-");

        txtSerie.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtFactura.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        btnBuscarLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarLista, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
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
                .addContainerGap(15, Short.MAX_VALUE))
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelType, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(labelFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jLabel37.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(35, 35, 38));
        jLabel37.setText("Subtotal:");

        labelSubtotal.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelSubtotal.setForeground(new java.awt.Color(35, 35, 38));
        labelSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelSubtotal.setText("-");
        labelSubtotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(35, 35, 38));
        jLabel44.setText("Iva 10,5%:");

        lbl_iva_105.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_105.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_105.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_iva_105.setText("-");
        lbl_iva_105.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbl2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl2.setForeground(new java.awt.Color(35, 35, 38));
        lbl2.setText("Iva 21%:");

        lbl_iva_21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_21.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_iva_21.setText("-");
        lbl_iva_21.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel41.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(35, 35, 38));
        jLabel41.setText("Otros impuestos:");

        labelOtherTaxes.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelOtherTaxes.setForeground(new java.awt.Color(35, 35, 38));
        labelOtherTaxes.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelOtherTaxes.setText("-");
        labelOtherTaxes.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel40.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(35, 35, 38));
        jLabel40.setText("Persepciones:");

        labelPersepciones.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelPersepciones.setForeground(new java.awt.Color(35, 35, 38));
        labelPersepciones.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPersepciones.setText("-");
        labelPersepciones.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbl.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl.setForeground(new java.awt.Color(35, 35, 38));
        lbl.setText("Descuento:");

        lblDiscount.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblDiscount.setForeground(new java.awt.Color(35, 35, 38));
        lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscount.setText("-");
        lblDiscount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel45.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(12, 83, 151));
        jLabel45.setText("Total");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(12, 83, 151));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("-");
        lblTotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labelPersepciones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                        .addComponent(labelOtherTaxes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_iva_21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_iva_105, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelSubtotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
