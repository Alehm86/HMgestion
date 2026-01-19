/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import Class.ProductDAO;
import Class.modelPrice;
import Class.modelStocks;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class PageProductStock extends javax.swing.JPanel {

    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

    ProductDAO queries = new ProductDAO();
    modelPrice price = new modelPrice();
    modelStocks stock = new modelStocks();
    DefaultTableModel dtm = new DefaultTableModel();
    
    
    public PageProductStock() {
        initComponents();
        
        JTextField txtCant = new JTextField();
        txtCant.setHorizontalAlignment(JTextField.CENTER);
        
        String[] titulo = new String[]{"Codigo", "Descripción", "Cantidad", "Precio","I.V.A."};
        dtm.setColumnIdentifiers(titulo);
        tableStock1.setModel(dtm);

        formatearTxtPrice();
        formatearCantidad();
        buttonsActions();
        
    }

    private void buttonsActions(){
        btnBuscar.addActionListener(e -> {
            buscar();         
        });
        
        btnQuitar.addActionListener(e -> {
            eliminarDeLista();         
        });
        
        btnEliminarTodo.addActionListener(e -> {
            limpiarTabla();         
        });
        
        btnFrmNewProduct.addActionListener(e -> {
            // llama al formulario para crear un producto nuevo
            frmProductNewDialog newProduct = new frmProductNewDialog(parent, true);
            newProduct.setLocationRelativeTo(parent);
            newProduct.setVisible(true);
        });
        
        btnAdd.addActionListener(e -> {
            addToList();
            clearTxt();                     
        });
        
        btnRegister.addActionListener(e -> {
           registrarCambios(); 
        });
        
        CheckBoxEdit.addActionListener(e -> {
            boolean activo = CheckBoxEdit.isSelected();           
            txtPrice.setEnabled(activo);            
        });
    }
    
    void eliminarDeLista(){
        int fila=tableStock1.getSelectedRow();
        if(fila == -1){   
           JOptionPane.showMessageDialog(null, "SELECCIONE UNA OPCION"); 
        }
        else{
           dtm.removeRow(fila); 
        }
    }
    
    void limpiarTabla(){
        int filas=dtm.getRowCount();
        for(int i=0;i<filas;i++){
            dtm.removeRow(0);
        }
    }
    
    
    void clearTxt(){
        txtPrice.setValue(0);
        txtCant.setText("");
        labelDescripcion.setText("-");
        lblIva.setText("0");
        txtProductCode.setText("");
    }
    
    void clearProveedor(){
        txtFactura.setText("");
        labelSupplier.setText("-");
        labelDate.setText("-");
        labelTotal.setText("-");
        CheckBoxFactura.setSelected(false);
    }
   
    void formatearTxtPrice(){
        NumberFormat formatoDecimal = NumberFormat.getNumberInstance();
        formatoDecimal.setMinimumFractionDigits(2);
        formatoDecimal.setMaximumFractionDigits(2);
        formatoDecimal.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(formatoDecimal);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);

        txtPrice.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }
   
    void formatearCantidad(){
        NumberFormat formatoEntero = NumberFormat.getIntegerInstance();
        formatoEntero.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(formatoEntero);
        formatter.setAllowsInvalid(false); // 🔒 Bloquea letras y caracteres no numéricos
        formatter.setMinimum(0); // (opcional) solo números positivos

        JFormattedTextField txtCant = new JFormattedTextField(formatter);
        txtCant.setValue(0);
   }
   
    void buscar(){
        //clear();
        int id = queries.obtenerIdProduct(txtProductCode.getText());
        queries.selectProductPriceEdit(id, txtPrice, lblIva);
        queries.selectProduct(id, labelDescripcion,txtProductCode);
        
    }
   
    void agregarFila(String codigo, String descripcion, int cantidad, double precio, double iva){
        dtm.addRow(new Object[]{codigo,descripcion,cantidad,precio,iva});
    }
   
    void addToList(){
        String codigo = "";
        String descripcion = "";
        int cantidad = 0;
        double precio = 0;
        double iva = 0;
 
        
        if (!txtProductCode.getText().trim().isEmpty()) {    
            if (!txtCant.getText().trim().isEmpty()) {
                int cant = Integer.parseInt(txtCant.getText().trim());

                if (cant > 0) {     
                        codigo = txtProductCode.getText().toUpperCase();
                        descripcion = labelDescripcion.getText().toUpperCase();
                        cantidad = Integer.parseInt(txtCant.getText());
                        precio = ((Number) txtPrice.getValue()).doubleValue();
                        iva = Double.parseDouble(lblIva.getText().toUpperCase());

                        agregarFila(codigo, descripcion, cantidad, precio, iva); 
                }else{
                    JOptionPane.showMessageDialog(null, "Ingresar una cantidad mayor a cero");
                }
            }else{
                JOptionPane.showMessageDialog(null, "El campo cantidad está vacío");
            }
        }else{
            JOptionPane.showMessageDialog(null, "No hay producto seleccionado");
        }
    }
    
    void registrarCambios(){
      
        if(tableStock1.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "No hay productos");
        }else{
            for(int i = 0; i < tableStock1.getRowCount(); i++){
                
                stock.idProduct = queries.obtenerIdProduct(tableStock1.getValueAt(i, 0).toString());
                stock.stock = Integer.parseInt(tableStock1.getValueAt(i, 2).toString());
                price.price = Double.parseDouble(tableStock1.getValueAt(i, 3).toString());
                
                queries.editPriceInStocks(stock.getIdProduct(),price.getPrice());
                queries.editStockProduct(stock.getIdProduct(),stock.getStock());         
            }
        }
        clearTxt();
        dtm.setRowCount(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        CheckBoxFactura = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtFactura = new javax.swing.JTextField();
        btnBuscarFactura = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        labelSupplier = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        labelDate = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        CheckBoxEdit = new javax.swing.JCheckBox();
        txtPrice = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        txtCant = new javax.swing.JTextField();
        btnFrmNewProduct = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        labelDescripcion = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableStock1 = new javax.swing.JTable();
        jPanel36 = new javax.swing.JPanel();
        btnRegister = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        btnEliminarTodo = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1600, 800));

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));

        CheckBoxFactura.setBackground(new java.awt.Color(255, 255, 255));
        CheckBoxFactura.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        CheckBoxFactura.setText("Sin factura");
        CheckBoxFactura.setBorder(null);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CheckBoxFactura)
                .addGap(20, 20, 20))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CheckBoxFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel36.setText("Nº de factura: ");

        txtFactura.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtFactura.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBuscarFactura.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnBuscarFactura.setBorder(null);
        btnBuscarFactura.setBorderPainted(false);
        btnBuscarFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarFactura.setFocusPainted(false);
        btnBuscarFactura.setFocusable(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel39.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel39.setText("Proveedor:");

        labelSupplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelSupplier.setText("-");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(538, 538, 538))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel41.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel41.setText("Fecha: ");

        labelDate.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDate.setText("-");

        jLabel37.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel37.setText("Total");

        labelTotal.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelTotal.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDate)
                .addGap(171, 171, 171)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTotal)
                .addContainerGap(373, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelTotal))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelDate)))
                .addContainerGap())
        );

        jLabel18.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 0, 51));
        jLabel18.setText("Falta programar");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel12.setText("Codigo de producto: ");

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/package_box_add_32.png"))); // NOI18N
        btnAdd.setText("Agregar");
        btnAdd.setBorder(null);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setMaximumSize(new java.awt.Dimension(120, 52));
        btnAdd.setMinimumSize(new java.awt.Dimension(120, 52));
        btnAdd.setPreferredSize(new java.awt.Dimension(120, 52));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnBuscar.setBorder(null);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFocusable(false);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel26.setText("Precio costo:");

        CheckBoxEdit.setBackground(new java.awt.Color(255, 255, 255));
        CheckBoxEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        CheckBoxEdit.setText("Editar");
        CheckBoxEdit.setBorder(null);

        txtPrice.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        txtPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPrice.setEnabled(false);

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel17.setText("Cantidad: ");

        label.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        label.setText("I.V.A.:");

        lblIva.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lblIva.setText("-");

        txtCant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantActionPerformed(evt);
            }
        });
        txtCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(CheckBoxEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(CheckBoxEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(txtPrice)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCant)))
        );

        btnFrmNewProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnFrmNewProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnFrmNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/more32.png"))); // NOI18N
        btnFrmNewProduct.setBorder(null);
        btnFrmNewProduct.setBorderPainted(false);
        btnFrmNewProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFrmNewProduct.setFocusPainted(false);
        btnFrmNewProduct.setFocusable(false);
        btnFrmNewProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrmNewProductActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel13.setText("Descripción:");

        labelDescripcion.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelDescripcion.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDescripcion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(btnFrmNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFrmNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGap(10, 10, 10))
        );

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos a ingresar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        tableStock1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tableStock1);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));

        btnRegister.setBackground(new java.awt.Color(255, 255, 255));
        btnRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/circleok_121876.png"))); // NOI18N
        btnRegister.setBorder(null);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new java.awt.Dimension(52, 52));

        btnQuitar.setBackground(new java.awt.Color(255, 255, 255));
        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin_121907.png"))); // NOI18N
        btnQuitar.setBorder(null);
        btnQuitar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(101, 129, 171));
        jLabel27.setText("  Registrar");

        btnEliminarTodo.setBackground(new java.awt.Color(255, 255, 255));
        btnEliminarTodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancel48.png"))); // NOI18N
        btnEliminarTodo.setToolTipText("");
        btnEliminarTodo.setBorder(null);
        btnEliminarTodo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

    }//GEN-LAST:event_btnAddActionPerformed

    private void txtCantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantActionPerformed

    private void txtCantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantKeyTyped
        int key = evt.getKeyChar();
        boolean numero = key >= 48 && key <= 57;
        if(!numero){
            evt.consume();
        }
        if (txtCant.getText().trim().length()==2){
            evt.consume();
        }
    }//GEN-LAST:event_txtCantKeyTyped

    private void btnFrmNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrmNewProductActionPerformed

    }//GEN-LAST:event_btnFrmNewProductActionPerformed

    private void btnEliminarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarTodoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckBoxEdit;
    private javax.swing.JCheckBox CheckBoxFactura;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarFactura;
    private javax.swing.JButton btnEliminarTodo;
    private javax.swing.JButton btnFrmNewProduct;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel label;
    private javax.swing.JLabel labelDate;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelSupplier;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel lblIva;
    private javax.swing.JTable tableStock1;
    private javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtFactura;
    private javax.swing.JFormattedTextField txtPrice;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
