/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.productDAO;
import models.mPrice;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import utils.utility;


public class productPriceEditDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(productPriceEditDialog.class.getName());

    productDAO qProduct = new productDAO();
    
    utility utils = new utility();
    
    mPrice price = new mPrice();  
    
    private int idProducto = -1;
    private double finalPrice = 0;
    
    public boolean estado = false;
    
    public void dialogoEdit(int idProducto){
        this.idProducto = idProducto;
        
        if(idProducto != 0){
            BuscarProducto();
            btnSearchList.setVisible(false);
            btnBuscar.setVisible(false);
            txtProductCode.setVisible(false);
        }
    }
    
    public boolean respuesta(){
        return estado;
    }
    
    public productPriceEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        leyendaBotones();
        
        utils.agregarPlaceholderN(txtProductCode, "Ingrese código de producto...");         
        
        llenarCombo();
        
        lbl_iva.setVisible(false);
        
        txtBenefit.setText("0");
        txtPrice.setText("0");
       
        actions();
        leyendaBotones();
    }
    
    private void leyendaBotones(){
        
        btnRefresh.setToolTipText("Calcular");
        btnCancel.setToolTipText("Borrar");
        btnBuscar.setToolTipText("Buscar por número de serie");
        btnSearchList.setToolTipText("Seleccioná un dispositivo de la lista");

    }

    public void llenarCombo(){
        
        cbo_iva.removeAllItems();
        
        cbo_iva.addItem(" - ");
        cbo_iva.addItem("10.5");
        cbo_iva.addItem("21");
        
        cbo_iva.setSelectedIndex(0);      
    }
    
    
    public void infoCombo(){
        
        String iva = lbl_iva.getText().trim();
        
        for (int i = 0; i < cbo_iva.getItemCount(); i++) {
            if (cbo_iva.getItemAt(i).toString().equalsIgnoreCase(iva)) {
                cbo_iva.setSelectedIndex(i);
                break;
            }
        }      
    } 
  
    private void BuscarProducto(){
        
        qProduct.selectProductPriceEdit(idProducto, txtPrice, txtBenefit, lbl_iva, txtFinalPrice);
        qProduct.selectProduct(idProducto, labelDescripcion, txtProductCode);
        infoCombo();
        calcularPrecioSugerido();
    }
    
    private void buscarPorCodigoDeProducto(){
        
            String codigo = txtProductCode.getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un código de producto.");
                txtProductCode.requestFocus();
                return;
            }

            idProducto = qProduct.selectIdProduct(codigo);

            if (idProducto > 0) {
                clear();
                BuscarProducto();
                calcularPrecioSugerido();
            }else{
                JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                txtProductCode.requestFocus();
                return;
            }
    }
 
    private void actions(){
           
        txtPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPrice.setText("0");
            }
        });
        
        txtBenefit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBenefit.setText("0");
            }
        });
        
        txtFinalPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFinalPrice.setText("0");
            }
        });
        
        btnRefresh.addActionListener(e -> {
            calcularPrecioSugerido();         
        });
        
        btnRegistrar.addActionListener(e -> {
            editar();         
        });
        
        btnSearchList.addActionListener(e -> {
            
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            productSearchDialog dialogo = new productSearchDialog(parent, true);            
            
            dialogo.setVisible(true); 
            
            idProducto = dialogo.getProduct();
            BuscarProducto();
            
        });
        
        btnBuscar.addActionListener(e -> {
                      
            buscarPorCodigoDeProducto();
                    
        });
        
        txtBenefit.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calcularPrecioSugerido();
            }
        });
        
        txtPrice.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizar();
            }

            public void actualizar() {
                calcularPrecioSugerido();
            }
        });
        
        btnCancel.addActionListener(e->{
            this.dispose();
        });
    }
    
    private void calcularPrecioSugerido(){        

        if(txtPrice.getText().trim().isEmpty() || txtBenefit.getText().trim().isEmpty()){
            labelPrecioSujerido.setText("$ 0");
            return;
        }
        labelPrecioSujerido.setText(utils.calcularPrecioSugerido(txtPrice.getText().trim(), lbl_iva.getText().trim(), txtBenefit.getText().trim()));
    }
    
    private void clear(){
        txtPrice.setText("0");
        txtBenefit.setText("");
        labelPrecioSujerido.setText("0");
        lbl_iva.setText("0");
    }
    
    private void editar(){
        int id = qProduct.selectIdProduct(txtProductCode.getText());
        price.price =  Double.parseDouble(txtPrice.getText());           
        price.benefit = Double.parseDouble(txtBenefit.getText());
        price.salesPrice = Double.parseDouble(txtFinalPrice.getText());
        price.iva = Double.parseDouble(cbo_iva.getSelectedItem().toString());
        
        estado = qProduct.updatePriceProduct(
                id, 
                price.getPrice(), 
                price.getBenefit(), 
                price.getSalesPrice(),
                price.getIva()
        );          
        
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas modificar el precio de otro producto?",
            "Confirmar registro",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            txtProductCode.setText("");
            labelDescripcion.setText("");
            txtPrice.setText("0");
            lbl_iva.setText("0");
            txtBenefit.setText("0");
            txtFinalPrice.setText("0");
            labelPrecioSujerido.setText("0");
            btnRegistrar.setEnabled(false);
        }else{
            this.dispose();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labelDescripcion = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labeliva2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labeliva3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labeliva1 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        txtBenefit = new javax.swing.JTextField();
        labelPrecioSujerido = new javax.swing.JLabel();
        txtFinalPrice = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        cbo_iva = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        txtProductCode = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnSearchList = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Editar precio");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labelDescripcion.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        labelDescripcion.setForeground(new java.awt.Color(12, 83, 151));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Precio de costo:");

        labeliva2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva2.setForeground(new java.awt.Color(35, 35, 38));
        labeliva2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/$32.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Beneficio:");

        labeliva3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva3.setForeground(new java.awt.Color(35, 35, 38));
        labeliva3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/porcentaje.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 153, 0));
        jLabel7.setText("Precio sujerido:");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Precio de venta:");

        labeliva1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labeliva1.setForeground(new java.awt.Color(35, 35, 38));
        labeliva1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/$32.png"))); // NOI18N

        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
        });

        txtBenefit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtBenefit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBenefit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBenefitKeyTyped(evt);
            }
        });

        labelPrecioSujerido.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelPrecioSujerido.setForeground(new java.awt.Color(255, 153, 0));
        labelPrecioSujerido.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrecioSujerido.setText("0");

        txtFinalPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtFinalPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtFinalPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFinalPriceKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labeliva2)
                    .addComponent(labeliva3)
                    .addComponent(labeliva1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBenefit)
                    .addComponent(txtFinalPrice)
                    .addComponent(labelPrecioSujerido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelPrecioSujerido, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtFinalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeliva1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/porcentaje.png"))); // NOI18N
        jLabel6.setText("I.V.A.: ");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva.setText("0");

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calc32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_iva))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(245, 248, 255));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/priceTag64.png"))); // NOI18N
        jLabel8.setText("Editar precio.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyTyped(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        btnSearchList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)
                && c != '.'
                && c != ','
                && c != KeyEvent.VK_BACK_SPACE
                && c != KeyEvent.VK_DELETE) {
            evt.consume();
        }

        if ((c == '.' || c == ',')
                && (txtPrice.getText().contains(".")
                || txtPrice.getText().contains(","))) {
            evt.consume();
        }

        if (txtPrice.getText().length() >= 8
                && Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPriceKeyTyped

    private void txtBenefitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtBenefit.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBenefitKeyTyped

    private void txtFinalPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFinalPriceKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)
                && c != '.'
                && c != ','
                && c != KeyEvent.VK_BACK_SPACE
                && c != KeyEvent.VK_DELETE) {
            evt.consume();
        }

        if ((c == '.' || c == ',')
                && (txtFinalPrice.getText().contains(".")
                || txtFinalPrice.getText().contains(","))) {
            evt.consume();
        }

        if (txtFinalPrice.getText().length() >= 8
                && Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtFinalPriceKeyTyped

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtProductCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyTyped
        
    }//GEN-LAST:event_txtProductCodeKeyTyped

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            buscarPorCodigoDeProducto();           
        }
    }//GEN-LAST:event_txtProductCodeKeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                customerCambioEstadoDialog dialog = new customerCambioEstadoDialog(new javax.swing.JFrame(),true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSearchList;
    private javax.swing.JComboBox<String> cbo_iva;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelPrecioSujerido;
    private javax.swing.JLabel labeliva1;
    private javax.swing.JLabel labeliva2;
    private javax.swing.JLabel labeliva3;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JTextField txtBenefit;
    private javax.swing.JTextField txtFinalPrice;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
