/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import dao.productDAO;
import models.mProducts;
import models.mStocks;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class productStockAdjustmentFrame extends javax.swing.JFrame {
    
    productDAO qProduct = new productDAO();
    mProducts producto = new mProducts();
    mStocks stock = new mStocks();
    
    private int stockProd = 0;
    private int id_product = 0;
    
    boolean valido = false;
    
    public productStockAdjustmentFrame() {
        initComponents();
        
        cboTipoAjuste.addItem("Seleccione una opción");
        cboTipoAjuste.addItem("Aumento");
        cboTipoAjuste.addItem("Disminución");
        
        cboMotivo.addItem("Seleccione una opción.");
        inicializar();

        btnBuscar.addActionListener(e -> {
            id_product = qProduct.selectIdProduct(txtProductCode.getText());
            buscar();  
        });
        
        btnBuscarProduct.addActionListener(e->{
            
            productSearchDialog fSearchProduct = new productSearchDialog(null, true);
            fSearchProduct.setLocationRelativeTo(null);
            fSearchProduct.setVisible(true);  
            
            id_product = fSearchProduct.getProduct();
            
            if(id_product > 0){
                buscar();
            }
            
        });
        
        btnRegistrar.addActionListener(e -> {
            registrar();        
        });
        
        cboTipoAjuste.addActionListener(e -> {
            String seleccion = (String) cboTipoAjuste.getSelectedItem();
            if(seleccion.equals("Aumento")){
                cboMotivo.removeAllItems();
                qProduct.llenarCombosAjusteStock(cboMotivo, 0);
            }else{
                cboMotivo.removeAllItems();
                qProduct.llenarCombosAjusteStock(cboMotivo, 1);
            }
        });
    }
    
    void inicializar(){
        txtProductCode.setText("");
        txtCant.setText("");
        txtAreaObserv.setText("");
        labelDescripcion.setText("-");
        labelStock.setText("-");
        cboTipoAjuste.setSelectedIndex(0);
        cboMotivo.setSelectedIndex(0);
    }
    
    private void buscar(){
        
        qProduct.selectProduct(id_product, labelDescripcion,txtProductCode);
        stockProd  = qProduct.selectStockActual(id_product);
        labelStock.setText(String.valueOf(stockProd)+ " Unidades");
    }   
    
    private void registrar(){
        
        boolean valido = true;
        int cant = Integer.parseInt(txtCant.getText().trim());
        String motivo = "";
        String observacion = "";
      
        if(!txtProductCode.getText().isEmpty()){         
            producto.productCode = txtProductCode.getText();        
        }else{
            JOptionPane.showMessageDialog(null, "¡Ingrese un producto!..");
            valido = false;
        }
        
        if (cant <= 0) {
            JOptionPane.showMessageDialog(null, "¡Ingrese una cantidad válida mayor a 0!..");
            valido = false;
        }
        
        if (cboTipoAjuste.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "¡Seleccioná un tipo de ajuste!");
            valido = false;
        }
            
        if(cboMotivo.getSelectedIndex() != 0){
            motivo = cboMotivo.getSelectedItem().toString();
        }else{
            JOptionPane.showMessageDialog(null, "¡Seleccione un motivo!..");
            valido = false;
        }
        
        if(!txtAreaObserv.getText().isEmpty()){
            observacion = txtAreaObserv.getText();
        }else{
            JOptionPane.showMessageDialog(null, "¡Ingrese una observación!..");
            valido = false;
        } 

        
        if(!valido){
            return;    
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Desea hacer el ajuste?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            
            if(cboTipoAjuste.getSelectedItem().toString().equals("Aumento")){
                valido = qProduct.updateStockProduct(id_product, (stockProd + cant));
            }else{
                valido = qProduct.updateStockProduct(id_product, (stockProd - cant));
            }       
        }
        
        if(valido){
            JOptionPane.showMessageDialog(null, "Proceso cargado correctamente.");
            inicializar();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaObserv = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        labelDescripcion = new javax.swing.JLabel();
        jlabel = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnBuscarProduct = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtProductCode = new javax.swing.JTextField();
        cboTipoAjuste = new javax.swing.JComboBox<>();
        cboMotivo = new javax.swing.JComboBox<>();
        txtCant = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Ajuste de Stock");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Descripción:");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Observaciones:");

        txtAreaObserv.setColumns(20);
        txtAreaObserv.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtAreaObserv.setForeground(new java.awt.Color(35, 35, 38));
        txtAreaObserv.setRows(5);
        jScrollPane1.setViewportView(txtAreaObserv);

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Codigo Producto:");

        labelDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion.setForeground(new java.awt.Color(35, 35, 38));
        labelDescripcion.setText("-");

        jlabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jlabel.setForeground(new java.awt.Color(35, 35, 38));
        jlabel.setText("Stock");

        labelStock.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelStock.setForeground(new java.awt.Color(35, 35, 38));
        labelStock.setText("-");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Tipo de ajueste:");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Motivo:");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Cantidad:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        cboTipoAjuste.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        cboMotivo.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtCant.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCant.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6)
                            .addComponent(jlabel)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelStock, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 78, Short.MAX_VALUE)))
                                .addGap(46, 46, 46))
                            .addComponent(cboMotivo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboTipoAjuste, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelStock, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTipoAjuste, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(245, 248, 255));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(12, 83, 151));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registrarse64.png"))); // NOI18N
        jLabel7.setText("Ajuste de stock.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            id_product = qProduct.selectIdProduct(txtProductCode.getText().trim());
            buscar();
        }
    }//GEN-LAST:event_txtProductCodeKeyPressed


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new productStockAdjustmentFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProduct;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboMotivo;
    private javax.swing.JComboBox<String> cboTipoAjuste;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlabel;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelStock;
    private javax.swing.JTextArea txtAreaObserv;
    private javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
