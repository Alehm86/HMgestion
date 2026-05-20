/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import classDAO.budgetDAO;
import classDAO.customerDAO;
import classDAO.productDAO;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import models.Budget;
import models.BudgetDetail;
import models.Products;
import utils.config;
import utils.utility;


public class budgetDialog extends javax.swing.JDialog {
    
    customerDAO qCustomer = new customerDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    
    utility utils = new utility();
    
    Products mProduct = new Products();
    Budget mBudget = new Budget();
    BudgetDetail mBdetail = new BudgetDetail();

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
        
        cboIVA.setSelectedIndex(-1);
        lbl_fecha.setText("");
        lbl_iva.setText("");
        lbl_id.setVisible(false);
        lbl_product.setVisible(false);
        lbl_iva.setVisible(false);
        lbl_fecha.setText(fecha);     
    }
    
    private void formatTable(){

        String[] titulo = new String[]{"Item", "Cant", "Precio Unit", "Iva","Total"};
        dtm.setColumnIdentifiers(titulo);
        jTablePresupuesto.setModel(dtm);
        
        config.TableStyleUtil.applyPoppinsHeader(jTablePresupuesto);

        jTablePresupuesto.getColumnModel().getColumn(0).setPreferredWidth(350);
        jTablePresupuesto.getColumnModel().getColumn(1).setPreferredWidth(40); 
        jTablePresupuesto.getColumnModel().getColumn(2).setPreferredWidth(100); 
        jTablePresupuesto.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboIVA)); 
        jTablePresupuesto.getColumnModel().getColumn(4).setPreferredWidth(100);
        


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
               qCustomer.selectCustomerSimplified(cuitCustomer, lbl_id, txtName, txtPhone); 
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
            insertBudget();
            
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
        });
    }
    
    private void eliminarDeLista(){
        
        int fila = jTablePresupuesto.getSelectedRow();
        
        if(fila == -1){   
           JOptionPane.showMessageDialog(null, "SELECCIONE UNA OPCION"); 
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
        infoCombo();      
    }   
    
    void addFile(String descripcion, int amount, double precio, String iva, double total){
        dtm.addRow(new Object[]{descripcion,amount,precio,iva,total});
    }
    
    private void addToList(){

        boolean estado = true;       
        
        int amount = 0;
        double price = 0;        
        double total = 0;
        String iva;
  
        if(!txtProduct.getText().isEmpty()){
            
            price = Double.parseDouble(txtPrice.getText().trim());
         
            mBdetail.setDescription(txtProduct.getText().toUpperCase().trim()); 
            mBdetail.setPrice(price);
          
        }else{
            
            JOptionPane.showMessageDialog(null, "Ingresar un producto o servicio.");
            estado = false;
        }  
        
        if(cboIVA.getSelectedIndex()>-1){
            
            iva = cboIVA.getSelectedItem().toString().trim();
            mBdetail.setIva(iva);
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
        
        if(!estado){
           return; 
        }else{           
            
            addFile(
                    mBdetail.getDescription(),
                    mBdetail.getQuantity(),
                    mBdetail.getPrice(),
                    mBdetail.getIva(),
                    mBudget.getTotal()
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

                double precioFinal = Double.parseDouble(
                    dtm.getValueAt(i, 2).toString()
                );

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
    
    private void insertBudget(){
        
        boolean valido = true;
        
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
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
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
            return;
        }
        
        for(int i = 0; i < jTablePresupuesto.getRowCount(); i++){

            String descripcion = jTablePresupuesto.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(jTablePresupuesto.getValueAt(i, 1).toString());
            double precio = Double.parseDouble(jTablePresupuesto.getValueAt(i, 2).toString());
            String iva = jTablePresupuesto.getValueAt(i, 3).toString();
            double subtotal = Double.parseDouble(jTablePresupuesto.getValueAt(i, 4).toString());

            BudgetDetail item = new BudgetDetail (
                    descripcion,
                    cantidad,
                    precio,
                    iva,
                    subtotal
            );

            qBudget.insertBudgetDetail(
                    id_budget,
                    item.getDescription(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getIva(),
                    item.getSubtotal()
            );
        }       
        JOptionPane.showMessageDialog(null, "Presupuesto registrado correctamente.");
  
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnCustomer = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lbl_fecha = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaObservaciones = new javax.swing.JTextArea();
        lbl_id = new javax.swing.JLabel();
        jPanelSeparador3 = new javax.swing.JPanel();
        jPanelSeparador2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtProduct = new javax.swing.JTextField();
        btnBuscarProduct = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePresupuesto = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblIva105 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblIva21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Cliente:");

        txtName.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(65, 65, 63));

        btnCustomer.setBackground(new java.awt.Color(255, 255, 255));
        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/customers32.png"))); // NOI18N
        btnCustomer.setBorder(null);
        btnCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCustomerMouseExited(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Teléfono:");

        txtPhone.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPhone.setForeground(new java.awt.Color(65, 65, 63));
        txtPhone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Fecha:");

        lbl_fecha.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_fecha.setForeground(new java.awt.Color(35, 35, 38));
        lbl_fecha.setText("xxx");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Observaciones:");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextAreaObservaciones.setColumns(20);
        jTextAreaObservaciones.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextAreaObservaciones.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaObservaciones.setLineWrap(true);
        jTextAreaObservaciones.setRows(5);
        jScrollPane1.setViewportView(jTextAreaObservaciones);

        lbl_id.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(53, 53, 53)
                                .addComponent(lbl_id))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(lbl_fecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_id, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSeparador2.setBackground(new java.awt.Color(35, 35, 38));

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

        jPanel5.setBackground(new java.awt.Color(101, 129, 171));

        icono.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(255, 255, 255));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bugdet128.png"))); // NOI18N
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

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Items:");

        txtProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtProduct.setForeground(new java.awt.Color(65, 65, 63));

        btnBuscarProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnBuscarProduct.setBorder(null);
        btnBuscarProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarProductMouseExited(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Cantidad:");

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(65, 65, 63));
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setText("Precio:");

        txtPrice.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(65, 65, 63));
        txtPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(102, 102, 102));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/more32.png"))); // NOI18N
        btnAdd.setText("Agregar");
        btnAdd.setBorder(null);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddMouseExited(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscar.setBorder(null);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/barcode32.png"))); // NOI18N

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtProductCode.setForeground(new java.awt.Color(65, 65, 63));
        txtProductCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Buscar por codigo");

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("IVA:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_iva.setText("xxx");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        cboIVA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "21.0%", "10.5%" }));
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_iva)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel9))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_product)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductCode)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
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
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(lbl_product))
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(lbl_iva)
                        .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTablePresupuesto.setBackground(new java.awt.Color(255, 255, 255));
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

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Presupuest32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir32.png"))); // NOI18N
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin32.png"))); // NOI18N
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteMouseExited(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32.png"))); // NOI18N
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMouseExited(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(35, 35, 38));
        jLabel12.setText("Subtotal:");

        lblSubtotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblSubtotal.setText("xxx");

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(35, 35, 38));
        jLabel13.setText("IVA 10.5%:");

        lblIva105.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva105.setText("xxx");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(35, 35, 38));
        jLabel14.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblTotal.setText("xxx");

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(35, 35, 38));
        jLabel15.setText("IVA 21%:");

        lblIva21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void txtPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyPressed
        txtPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPhone.getText().length() >= 15) {
                    e.consume();
                }
            }
        });       
    }//GEN-LAST:event_txtPhoneKeyPressed

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        txtCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCantidad.getText().length() >= 3) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCantidadKeyPressed

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void txtPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyPressed
        txtPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPrice.getText().length() >= 15) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtPriceKeyPressed

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped
        txtPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPrice.getText().length() >= 10) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtPriceKeyTyped

    private void btnCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerMouseEntered
        btnCustomer.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnCustomerMouseEntered

    private void btnCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerMouseExited
        btnCustomer.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCustomerMouseExited

    private void btnBuscarProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseEntered
        btnBuscarProduct.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarProductMouseEntered

    private void btnBuscarProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseExited
        btnBuscarProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarProductMouseExited

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        btnAdd.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseExited
        btnAdd.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddMouseExited

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        btnBuscar.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        btnBuscar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarMouseExited

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {     
            String productCode = txtProductCode.getText().trim();
            id_product = qProduct.selectIdProduct(productCode); 

            if(id_product > 0){
                buscarCode();
            }
        }    
    }//GEN-LAST:event_txtProductCodeKeyPressed

    private void txtProductCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductCodeKeyTyped

    private void btnDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseEntered
        btnDelete.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnDeleteMouseEntered

    private void btnDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseExited
        btnDelete.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnDeleteMouseExited

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        btnEdit.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited


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
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablePresupuesto;
    private javax.swing.JTextArea jTextAreaObservaciones;
    private javax.swing.JLabel lblIva105;
    private javax.swing.JLabel lblIva21;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_fecha;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
