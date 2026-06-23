/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.budgetDAO;
import dao.customerDAO;
import dao.productDAO;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import models.mBudget;
import models.mBudgetDetail;
import models.mProducts;
import utils.tableStyleUtil;
import utils.utility;


public class budgetDialog extends javax.swing.JDialog {
    
    customerDAO qCustomer = new customerDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    
    utility utils = new utility();
    
    mProducts mProduct = new mProducts();
    mBudget mBudget = new mBudget();
    mBudgetDetail mBdetail = new mBudgetDetail();

    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
    
    boolean modoEdicion = false;
    int filaEditable = -1;
    
    DefaultTableModel dtm = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 0 || column == 1 || column == 2 || column == 3);
        }
    };    
        
    String fecha = utils.fecha();
    String cuitCustomer = "";
    int id_product = -1;
    
    String[] opcionesIVA = {"10.5%", "21%"};
    JComboBox<String> comboIVA = new JComboBox<>(opcionesIVA);
    
    int id_budget;
    int id_service;
    String nameCustomer;
    String phoneCustomer;
    
    
    public void dialogoId_service(int id_service){
        this.id_service = id_service;
    }
    
    public void dialogoIdCustomer(String nameCustomer, String phoneCustomer){
        this.nameCustomer = nameCustomer;
        this.phoneCustomer = phoneCustomer;
        
        if(!nameCustomer.isEmpty()){
            txtName.setText(nameCustomer);
            txtPhone.setText(phoneCustomer);
            btnCustomer.setEnabled(false);
        }
    }

    public budgetDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
               
        actions();
        inicializar();      
        formatTable();      
        leyendaBotones();
        
    }
    
    private void leyendaBotones(){
        
        btnCustomer.setToolTipText("Seleccionar cliente");
        btnCancel.setToolTipText("Salir");
        btnBuscarProduct.setToolTipText("Seleccionar producto");
        btnBuscar.setToolTipText("Buscar por número de serie");
        btnDelete.setToolTipText("Borrar de lista");
        btnEdit.setToolTipText("Editar producto de lista");
    }
    
    private void inicializar(){
        
        cboType.addItem("Selecione un tipo");
        cboType.addItem("Producto");
        cboType.addItem("Servicio");
        
        cboIVA.setSelectedIndex(-1);
        lbl_fecha.setText("");
        lbl_iva.setText("");
        lbl_id.setVisible(false);
        lbl_idProduct.setVisible(false);
        lbl_idProduct.setText("0");
        lbl_product.setVisible(false);
        lbl_iva.setVisible(false);
        lbl_fecha.setText(fecha);   
        
        cboIVA.addItem("21.0%");
        cboIVA.addItem("10.5%");
        
        lbl_address.setVisible(false);
        
        lblSubtotal.setText("0.00");
        lblIva105.setText("0.00");
        lblIva21.setText("0.00");
        lblTotal.setText("0.00");
    }
    
    private void formatTable(){

        String[] titulo = new String[]{"Item", "Cant", "P. Unit. IVA Inc.", "Iva","Total","tipo","idProduct"};
        dtm.setColumnIdentifiers(titulo);
        jTablePresupuesto.setModel(dtm);
        
        tableStyleUtil.applyPoppinsHeader(jTablePresupuesto);
        
        jTablePresupuesto.getColumnModel().getColumn(5).setMinWidth(0);
        jTablePresupuesto.getColumnModel().getColumn(5).setMaxWidth(0);
        jTablePresupuesto.getColumnModel().getColumn(5).setPreferredWidth(0);
        
        jTablePresupuesto.getColumnModel().getColumn(6).setMinWidth(0);
        jTablePresupuesto.getColumnModel().getColumn(6).setMaxWidth(0);
        jTablePresupuesto.getColumnModel().getColumn(6).setPreferredWidth(0);

        jTablePresupuesto.getColumnModel().getColumn(0).setPreferredWidth(350);
        jTablePresupuesto.getColumnModel().getColumn(1).setPreferredWidth(40); 
        jTablePresupuesto.getColumnModel().getColumn(2).setPreferredWidth(100); 
        jTablePresupuesto.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboIVA)); 
        jTablePresupuesto.getColumnModel().getColumn(4).setPreferredWidth(100);
        jTablePresupuesto.getColumnModel().getColumn(5).setPreferredWidth(0);
        


        dtm.addTableModelListener(e -> {

            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if (columna == 1 || columna == 2) {

                try {
                    int cantidad = Integer.parseInt(dtm.getValueAt(fila, 1).toString());

                    double precio = Double.parseDouble(
                        dtm.getValueAt(fila, 2).toString()
                    );

                    double total = cantidad * precio;

                    dtm.setValueAt(total, fila, 4);

                } catch (Exception ex) {

                }
            }
        });
    }

    private void actions(){
        
        btnCancel.addActionListener(e -> {
            this.dispose();
        });
        
        txtPrice.addActionListener(e -> {

            String valor = txtPrice.getText().trim();

            if (valor.isEmpty()) return;
            valor = valor.replace("$", "").trim();

            if (!valor.contains(",")) {
                valor = valor + ",00";
            }
            txtPrice.setText("$ " + valor);           
        });
        
        btnCustomer.addActionListener(e->{
            
            customerSearchDialog fSearchCustomer = new customerSearchDialog(null, true);
            fSearchCustomer.setLocationRelativeTo(null);
            fSearchCustomer.setVisible(true);     
 
            cuitCustomer = fSearchCustomer.getCustomerSerch();           
            
            if(!cuitCustomer.isEmpty()){
               qCustomer.selectCustomerSimplified(cuitCustomer, lbl_id, txtName, txtPhone, lbl_address); 
               txtAddress.setText(lbl_address.getText());
            }else{
                
            }
        });
              
        btnBuscarProduct.addActionListener(e->{
            
            id_product = 0;
            
            productSearchDialog fSearchProduct = new productSearchDialog(null, true);
            fSearchProduct.setLocationRelativeTo(null);
            fSearchProduct.setVisible(true);  
            
            id_product = fSearchProduct.getProduct();
            
            if(id_product > 0){
                buscarCode();
            }
            
        });
                
        btnBuscar.addActionListener(e->{
            
            String productCode = txtProductCode.getText().trim();
            id_product = qProduct.selectIdProduct(productCode); 

            if(id_product > 0){
                buscarCode();
            }            
        });
        
        btnAdd.addActionListener(e->{
            
            addToList();
            
            txtProduct.setText("");
            txtProductCode.setText("");
            txtPrice.setText("");
            txtCantidad.setText("");
            txtProductCode.requestFocus();
            cboIVA.setSelectedIndex(-1);  
            lbl_idProduct.setText("0");
            cboType.setSelectedIndex(-1);
        });
        
        btnDelete.addActionListener(e->{  
            eliminarDeLista();
            calcularTotales();
            
        });
        
        btnEdit.addActionListener(e->{
            
            int fila = jTablePresupuesto.getSelectedRow();

            if (fila != -1) {
                
                modoEdicion = true;
                filaEditable = fila;
                dtm.fireTableDataChanged();
                jTablePresupuesto.editCellAt(filaEditable, 1);
                jTablePresupuesto.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            } 
            calcularTotales();
        });
        
        btnRegistrar.addActionListener(e->{
            boolean status = false;
            
            status = insertBudget();
            
            if(status){
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea imprimir el presupuesto?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
                );                   
                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }

                budgetPrintDialog fPrint = new budgetPrintDialog(null, true);
                fPrint.setLocationRelativeTo(null);

                fPrint.dialogoIdBudget(id_budget);

                fPrint.setVisible(true);

                this.dispose();
            } 
        });
        
        cboType.addActionListener(e->{
           
            int tipo = cboType.getSelectedIndex();
            
            if(tipo == 2){
                cboIVA.setSelectedIndex(0);
                cboIVA.setEnabled(false);
            }else{
                cboIVA.setEnabled(true);
            }
        });
    }
    
    private void eliminarDeLista(){
        
        int fila = jTablePresupuesto.getSelectedRow();
        
        if(fila == -1){   
           JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA"); 
        }
        else{
           dtm.removeRow(fila); 
        }
    }
    
    private void infoCombo(){
        
        String iva = lbl_iva.getText().trim();
        
        for (int i = 0; i < cboIVA.getItemCount(); i++) {
            if (cboIVA.getItemAt(i).toString().equalsIgnoreCase(iva)) {
                cboIVA.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void buscarCode(){
        
        qProduct.selectProduct(id_product, lbl_product, txtProductCode);
        qProduct.selectSalePriceAndIva(id_product,txtPrice,lbl_iva);
        txtProduct.setText(lbl_product.getText());
        lbl_idProduct.setText(String.valueOf(id_product));
        cboType.setSelectedIndex(1);
        infoCombo();      
    }   
    
    void addFile(String descripcion, int amount, double precio, String iva, double total, String type, int idProduct){
        dtm.addRow(new Object[]{descripcion,amount,precio,iva,total,type,idProduct});
    }
    
    private void addToList(){

        boolean estado = true;       
        
        int amount = 0;
        double price = 0;        
        double total = 0;
        String iva;
        String type;
        int idProd = -1;
        
        idProd = Integer.parseInt(lbl_idProduct.getText().trim());
  
        if(!txtProduct.getText().isEmpty()){
            
            mBdetail.setDescription(txtProduct.getText().toUpperCase().trim()); 
            price = Double.parseDouble(txtPrice.getText().trim());
            
            if(cboType.getSelectedIndex() == 2){
                price = price * 1.21;
                mBdetail.setPrice(price);
            }else{
                mBdetail.setPrice(price);
            }
            
        }else{           
            JOptionPane.showMessageDialog(null, "Ingresar un producto o servicio.");
            estado = false;
        }  
        
        if(cboIVA.getSelectedIndex()>-1){
            
            iva = cboIVA.getSelectedItem().toString().trim();
            mBdetail.setIva(iva);
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar el tipo de IVA");
            estado = false;
        }
        
        if(!txtCantidad.getText().trim().isEmpty()){
            
            amount = Integer.parseInt(txtCantidad.getText().trim());
            
            if(amount > 0){            
                mBdetail.setQuantity(amount);
                mBudget.setTotal(amount * price);
            }               
        }else{
            JOptionPane.showMessageDialog(null, "El campo cantidad está vacío");
            estado = false;
        }  
        
        if(cboType.getSelectedIndex()>-1){
            
            type = cboType.getSelectedItem().toString().trim();
            
            if("Producto".equals(type)){
                mBdetail.setType("product");
            }else if("Servicio".equals(type)){
                mBdetail.setType("service");
            }
         
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar el tipo Item");
            estado = false;
        }
        
        if(idProd > 0){
            
            mBdetail.setIdProduct(idProd);
        }else{
            mBdetail.setIdProduct(0);
        }
        
        if(!estado){
           return; 
        }else{           
            
            addFile(
                    mBdetail.getDescription(),
                    mBdetail.getQuantity(),
                    mBdetail.getPrice(),
                    mBdetail.getIva(),
                    mBudget.getTotal(),
                    mBdetail.getType(),
                    mBdetail.getIdProduct()
            );
        }
        calcularTotales();
    }
    
    public void calcularTotales(){

        double subtotal = 0;
        double iva105 = 0;
        double iva21 = 0;

        for (int i = 0; i < dtm.getRowCount(); i++) {

            try {
                int cantidad = Integer.parseInt(dtm.getValueAt(i, 1).toString());

                double precioFinal = Double.parseDouble(dtm.getValueAt(i, 2).toString());

                String ivaStr = dtm.getValueAt(i, 3).toString();

                double base = 0;
                double iva = 0;

                if (ivaStr.contains("21")) {
                    base = precioFinal / 1.21;
                    iva = precioFinal - base;
                    iva21 += iva * cantidad;

                } else if (ivaStr.contains("10.5")) {
                    base = precioFinal / 1.105;
                    iva = precioFinal - base;
                    iva105 += iva * cantidad;
                }

                subtotal += base * cantidad;

            } catch (Exception e) {}
        }

        double total = subtotal + iva105 + iva21;

        lblSubtotal.setText("$ " + String.format("%.2f", subtotal));
        lblIva105.setText("$ " + String.format("%.2f", iva105));
        lblIva21.setText("$ " + String.format("%.2f", iva21));
        lblTotal.setText("$ " + String.format("%.2f", total));
    } 
    
    private boolean insertBudget(){
        
        boolean valido = true;
        boolean status = false;
        
        LocalDate fecha = LocalDate.now();
        LocalDate vencimiento = fecha.plusDays(10);
        
        mBudget.setDate(fecha);
        mBudget.setExpiration_date(vencimiento);
        
        mBudget.setState(1);
        
        String name = txtName.getText().trim();
        
        if(!name.isEmpty()){
            mBudget.setCustomer_name(name);
        }else{
            JOptionPane.showMessageDialog(null, "Debe completar el nombre del cliente.");
            valido = false;
        }
        
        String phone = txtPhone.getText().trim();
        
        if(!phone.isEmpty()){
            mBudget.setCustomer_phone(phone);
        }else{
            mBudget.setCustomer_phone(null);
        }
        
        String observations = jTextAreaObservaciones.getText();
        
        if(!observations.isEmpty()){
            mBudget.setObservations(observations);
        }else{
            mBudget.setObservations(null);
        }
        
        if(id_service > 0){
            mBudget.setId_service(id_service);
        }else{
            mBudget.setId_service(null);
        }
        
        if(jTablePresupuesto.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "No hay ítems cargados.");
            valido = false;
        }
        
        if (!valido) {
            return false;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return false;
            }      
        }
        
        id_budget = qBudget.insertBudget(
                mBudget.getId_service(), 
                mBudget.getDate(), 
                mBudget.getExpiration_date(), 
                mBudget.getCustomer_name(), 
                mBudget.getCustomer_phone(), 
                mBudget.getTotal(), 
                mBudget.getState(), 
                mBudget.getObservations()
        );
        
        if(id_budget <= 0){
            JOptionPane.showMessageDialog(null, "Error al registrar el presupuesto.");
            return false;
        }
        
        for(int i = 0; i < jTablePresupuesto.getRowCount(); i++){

            String descripcion = jTablePresupuesto.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(jTablePresupuesto.getValueAt(i, 1).toString());
            double precio = Double.parseDouble(jTablePresupuesto.getValueAt(i, 2).toString());
            String iva = jTablePresupuesto.getValueAt(i, 3).toString();
            double subtotal = Double.parseDouble(jTablePresupuesto.getValueAt(i, 4).toString());
            String type = jTablePresupuesto.getValueAt(i, 5).toString();
            int idProd = Integer.parseInt(jTablePresupuesto.getValueAt(i, 6).toString());
            
            mBudgetDetail item = new mBudgetDetail (
                    descripcion,
                    cantidad,
                    precio,
                    iva,
                    subtotal,
                    type,
                    idProd
            );

            status = qBudget.insertBudgetDetail(
                    id_budget,
                    item.getDescription(),
                    item.getType(),
                    item.getIdProduct(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getIva(),
                    item.getSubtotal()
            );
        }       
        JOptionPane.showMessageDialog(null, "Presupuesto registrado correctamente.");
  
        return status;
    }
    
            
        
                
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        btnCustomer = new javax.swing.JButton();
        btnNewCustomer = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObservaciones = new javax.swing.JTextArea();
        jPanelSeparador3 = new javax.swing.JPanel();
        jPanelSeparador2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        txtProduct = new javax.swing.JTextField();
        btnBuscarProduct = new javax.swing.JButton();
        txtProductCode = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        txtPrice = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        cboIVA = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        lbl_idProduct = new javax.swing.JLabel();
        lbl_id = new javax.swing.JLabel();
        cboType = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePresupuesto = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblIva105 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblIva21 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        lbl_address = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Presupuesto");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(12, 83, 151));
        jLabel2.setText("Cliente:");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Teléfono:");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Observaciones:");

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(35, 35, 38));
        jLabel16.setText("Dirección:");

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        txtPhone.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N

        btnNewCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientNew32.png"))); // NOI18N

        jTextAreaObservaciones.setColumns(20);
        jTextAreaObservaciones.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextAreaObservaciones.setRows(5);
        jScrollPane2.setViewportView(jTextAreaObservaciones);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhone))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddress))
                    .addComponent(jScrollPane2))
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanelSeparador3.setBackground(new java.awt.Color(245, 248, 255));

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

        jPanelSeparador2.setBackground(new java.awt.Color(245, 248, 255));

        javax.swing.GroupLayout jPanelSeparador2Layout = new javax.swing.GroupLayout(jPanelSeparador2);
        jPanelSeparador2.setLayout(jPanelSeparador2Layout);
        jPanelSeparador2Layout.setHorizontalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador2Layout.setVerticalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(245, 248, 255));

        icono.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(12, 83, 151));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/budget64_1.png"))); // NOI18N
        icono.setText("Presupuesto.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icono)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(icono)
                .addGap(5, 5, 5))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(12, 83, 151));
        jLabel6.setText("Items:");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Cantidad:");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setText("Precio:");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Buscar por codigo");

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("IVA:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_iva.setText("xxx");

        txtProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

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

        txtPrice.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
        });

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnAdd.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(12, 83, 151));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add_32.png"))); // NOI18N
        btnAdd.setText("Agregar");
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        lbl_idProduct.setText("idProduct");

        lbl_id.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        cboType.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_iva)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_idProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_product)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(339, 339, 339))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtProductCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lbl_product)
                            .addComponent(lbl_idProduct))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva)
                    .addComponent(btnAdd))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTablePresupuesto.setBackground(new java.awt.Color(255, 255, 255));
        jTablePresupuesto.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTablePresupuesto.setForeground(new java.awt.Color(65, 65, 63));
        jTablePresupuesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTablePresupuesto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTablePresupuesto.setFillsViewportHeight(true);
        jTablePresupuesto.setRowHeight(30);
        jScrollPane3.setViewportView(jTablePresupuesto);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(12, 83, 151));
        jLabel12.setText("Subtotal:");

        lblSubtotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(12, 83, 151));
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("xxx");

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(12, 83, 151));
        jLabel13.setText("IVA 10.5%:");

        lblIva105.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva105.setForeground(new java.awt.Color(12, 83, 151));
        lblIva105.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIva105.setText("xxx");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(12, 83, 151));
        jLabel14.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(12, 83, 151));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("xxx");

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(12, 83, 151));
        jLabel15.setText("IVA 21%:");

        lblIva21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva21.setForeground(new java.awt.Color(12, 83, 151));
        lblIva21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIva21.setText("xxx");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva21, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblIva105)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblIva21)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblTotal)
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnEdit.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(35, 35, 38));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit16.png"))); // NOI18N
        btnEdit.setText("Editar");

        btnDelete.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(35, 35, 38));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin16.png"))); // NOI18N
        btnDelete.setText("Eliminar");

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(247, 247, 247)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(12, 83, 151));
        jLabel4.setText("Fecha:");

        lbl_fecha.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_fecha.setForeground(new java.awt.Color(35, 35, 38));
        lbl_fecha.setText("xxx");

        lbl_address.setText("direccion");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_address, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(lbl_fecha)
                    .addComponent(lbl_address))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelSeparador2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelSeparador3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelSeparador3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

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

        if (txtPrice.getText().length() >= 10
                && Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPriceKeyTyped

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtCantidad.getText().length() >= 3) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void txtProductCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductCodeKeyTyped

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            id_product = qProduct.selectIdProduct(txtProductCode.getText());
            buscarCode();           
        }
    }//GEN-LAST:event_txtProductCodeKeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                budgetDialog dialog = new budgetDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProduct;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNewCustomer;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboType;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablePresupuesto;
    private javax.swing.JTextArea jTextAreaObservaciones;
    private javax.swing.JLabel lblIva105;
    private javax.swing.JLabel lblIva21;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_address;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_idProduct;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
