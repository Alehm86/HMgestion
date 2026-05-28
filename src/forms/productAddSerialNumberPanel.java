/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import classDAO.productDAO;
import classDAO.purchaseInvoiceDAO;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import classDAO.productDAO.ComboProducto;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.ProductSerialNumber;
import utils.config;


public class productAddSerialNumberPanel extends javax.swing.JPanel {

    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO();
    productDAO qProduct = new productDAO();
    
    private boolean actualizandoTabla = false;
    
    private int idPurchase = -1;
    int idProduct = -1;
    String detalle = "";
    
    boolean modoEdicion = false;
    int filaEditable = -1;
    
    DefaultTableModel dtm = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 2);
        }
    };  
     
    public productAddSerialNumberPanel() {
        initComponents();
        
        inicializar();
        actions();
        formatTable();
    }
    
    private void inicializar(){
        lbl_number.setVisible(false);
        cboProduct.removeAllItems();
    }

    private void actions(){
        
        btnBuscar.addActionListener(e->{
            factura();
            buscar();
        });
        
        btnBuscarFacturaLista.addActionListener(e -> {

            Window parent = SwingUtilities.getWindowAncestor(this);

            purchaseInvoiceListDialog fPurchaseInv = new purchaseInvoiceListDialog((Frame) null, true, 2);

            fPurchaseInv.setLocationRelativeTo(parent);
            fPurchaseInv.setVisible(true);

            idPurchase = fPurchaseInv.getIdPurchase();

            if (idPurchase > 0) {
                buscar();
            }
        });
        
        btnAddSN.addActionListener(e -> {

            DefaultTableModel model = (DefaultTableModel) tableItems.getModel();

            model.setRowCount(0);

            int cantidad = qProduct.obtenerStockDeCompra(idPurchase, idProduct);

            for (int i = 0; i < cantidad; i++) {

                model.addRow(new Object[]{idProduct,detalle,""});
            }

        });
        
        btnEditSN.addActionListener(e->{
            
            int fila = tableItems.getSelectedRow();

            if (fila != -1) {
                
                modoEdicion = true;
                filaEditable = fila;
                dtm.fireTableDataChanged();
                tableItems.editCellAt(filaEditable, 2);
                tableItems.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            } 

        });
        
        btnRegistrar.addActionListener(e->{
                      
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            insertSN();
//            qProduct.listProductSN(idProduct, idPurchase, tableItems); 
            
        });
        
        
        cboProduct.addActionListener(e->{
            
            ComboProducto productoSeleccionado =(ComboProducto) cboProduct.getSelectedItem();

            if(productoSeleccionado != null){

                idProduct = productoSeleccionado.getIdProduct();
                detalle = productoSeleccionado.toString();
                qProduct.listProductSN(idProduct, idPurchase,dtm);

            }
        });
        
        tableItems.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getClickCount() == 2){

                    filaEditable = tableItems.getSelectedRow();
                    modoEdicion = true;

                    dtm.fireTableDataChanged();
                }
            }
        });
        
    }
    
    private void factura(){
        String serie = txtSerie.getText().trim();
        String numero = txtNumber.getText().trim();
        String purchase = serie+"-"+numero;
        idPurchase = qPurchase.selectIdPurchase(purchase);
    }
    
    private void buscar(){
        
        qPurchase.selectPurchaseInvoice2(idPurchase, lbl_Fecha, lbl_supplier, lbl_purchase_type, lbl_number);
        cboProduct.removeAllItems();
        qProduct.llenarComboProducto(cboProduct,idPurchase);
        
    }
    
    private void formatTable() {

        String[] titulo = new String[]{"id","Producto","Numero de serie"};
        dtm.setColumnIdentifiers(titulo);
        tableItems.setModel(dtm);

        config.TableStyleUtil.applyPoppinsHeader(tableItems);

        tableItems.getColumnModel().getColumn(0).setMinWidth(0);
        tableItems.getColumnModel().getColumn(0).setMaxWidth(0);
        tableItems.getColumnModel().getColumn(0).setWidth(0);

        tableItems.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(200);


        dtm.addTableModelListener(e -> {

            if(actualizandoTabla){
                return;
            }

            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if(columna == 2 && fila >= 0){

                try{

                    Object value = dtm.getValueAt(fila, 2);

                    String serialNumber =
                            value == null
                            ? ""
                            : value.toString().trim();

                    if(serialNumber.isEmpty()){
                        return;
                    }

                    if(serialDuplicado(serialNumber, fila)){

                        actualizandoTabla = true;

                        JOptionPane.showMessageDialog(null,"El número de serie ya existe");

                        dtm.setValueAt("", fila, 2);

                        actualizandoTabla = false;

                        return;
                    }

                    String serialUpper = serialNumber.toUpperCase();

                    if(!serialNumber.equals(serialUpper)){

                        actualizandoTabla = true;
                        dtm.setValueAt(serialUpper,fila,2);
                        actualizandoTabla = false;
                    }

                }catch(Exception ex){
                    actualizandoTabla = false;
                    JOptionPane.showMessageDialog(null,"Error: " + ex.getMessage());
                }
            }
        });
    }
    
    private boolean serialDuplicado(String serial, int filaActual) {

        for (int i = 0; i < dtm.getRowCount(); i++) {

            if (i != filaActual) {

                Object value = dtm.getValueAt(i, 2);

                if (value != null &&
                    value.toString().trim()
                            .equalsIgnoreCase(serial)) {

                    return true;
                }
            }
        }

        return false;
    }
    
    private void insertSN(){

        boolean valido;

        for(int i = 0; i < tableItems.getRowCount(); i++){

            int idProduct = Integer.parseInt(
                    tableItems.getValueAt(i, 0).toString()
            );

            Object value = tableItems.getValueAt(i, 2);

            String serialNumber =
                    value == null ? "" : value.toString().trim();

            if(serialNumber.isEmpty()){

                JOptionPane.showMessageDialog(
                        null,
                        "Complete todos los números de serie"
                );

                return;
            }

            LocalDate fecha = LocalDate.now();

            String status = "STOCK";

            String observations = "Sin observaciones";

            ProductSerialNumber item =
                    new ProductSerialNumber(
                            idProduct,
                            idPurchase,
                            serialNumber,
                            fecha,
                            status,
                            observations
                    );

            valido = qProduct.insertProductSerialNumber(
                    item.getId_product(),
                    item.getId_purchase(),
                    item.getSerialNumber(),
                    item.getFechaRegistro(),
                    item.getStatus(),
                    item.getObservations()
            );

            if(!valido){
                JOptionPane.showMessageDialog(null,"Error al guardar serial: "+ serialNumber);
                return;
            }
        }
        JOptionPane.showMessageDialog(null,"Números de serie registrados correctamente");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Proveedor = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        txtNumber = new javax.swing.JTextField();
        btnBuscarFacturaLista = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        cboProduct = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        Proveedor1 = new javax.swing.JLabel();
        lbl_supplier = new javax.swing.JLabel();
        lbl_Fecha = new javax.swing.JLabel();
        Proveedor2 = new javax.swing.JLabel();
        lbl_purchase_type = new javax.swing.JLabel();
        lbl_number = new javax.swing.JLabel();
        btnAddSN = new javax.swing.JButton();
        btnEditSN = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Proveedor.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        Proveedor.setForeground(new java.awt.Color(35, 35, 38));
        Proveedor.setText("Proveedor:");

        lbl_product.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_product.setForeground(new java.awt.Color(35, 35, 38));
        lbl_product.setText("-");

        txtSerie.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSerie.setForeground(new java.awt.Color(65, 65, 63));
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtSerie.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });

        txtNumber.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtNumber.setForeground(new java.awt.Color(65, 65, 63));
        txtNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtNumber.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumberKeyPressed(evt);
            }
        });

        btnBuscarFacturaLista.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarFacturaLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnBuscarFacturaLista.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBuscarFacturaLista.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarFacturaLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarFacturaListaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarFacturaListaMouseExited(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(35, 35, 38));
        jLabel27.setText("Producto:");

        cboProduct.setBackground(new java.awt.Color(255, 255, 255));
        cboProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboProduct.setForeground(new java.awt.Color(35, 35, 38));
        cboProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
        tableItems.setFillsViewportHeight(true);
        tableItems.setRowHeight(30);
        tableItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItemsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableItems);

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnRegistrar.setMaximumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setMinimumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 52));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
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
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(35, 35, 38));
        jLabel28.setText("Factura:");

        Proveedor1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        Proveedor1.setForeground(new java.awt.Color(35, 35, 38));
        Proveedor1.setText("Fecha:");

        lbl_supplier.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_supplier.setForeground(new java.awt.Color(35, 35, 38));
        lbl_supplier.setText("-");

        lbl_Fecha.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_Fecha.setForeground(new java.awt.Color(35, 35, 38));
        lbl_Fecha.setText("-");

        Proveedor2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        Proveedor2.setForeground(new java.awt.Color(35, 35, 38));
        Proveedor2.setText("Tipo:");

        lbl_purchase_type.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_purchase_type.setForeground(new java.awt.Color(35, 35, 38));
        lbl_purchase_type.setText("-");

        lbl_number.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_number.setForeground(new java.awt.Color(35, 35, 38));
        lbl_number.setText("-");

        btnAddSN.setBackground(new java.awt.Color(255, 255, 255));
        btnAddSN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add32.png"))); // NOI18N
        btnAddSN.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnAddSN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddSN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddSNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddSNMouseExited(evt);
            }
        });

        btnEditSN.setBackground(new java.awt.Color(255, 255, 255));
        btnEditSN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32.png"))); // NOI18N
        btnEditSN.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnEditSN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditSN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditSNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditSNMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lbl_product)
                        .addGap(0, 0, 0)
                        .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarFacturaLista, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Proveedor2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_purchase_type, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboProduct, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Proveedor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Proveedor1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_number))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAddSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 29, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_product, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarFacturaLista, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Proveedor2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_purchase_type, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Proveedor1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_number, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAddSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/barcode64.png"))); // NOI18N
        jLabel1.setText("Ingresar numero de serie");

        jPanelSeparador4.setBackground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        btnBuscar.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        btnBuscar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnBuscarFacturaListaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarFacturaListaMouseEntered
        btnBuscarFacturaLista.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBuscarFacturaListaMouseEntered

    private void btnBuscarFacturaListaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarFacturaListaMouseExited
        btnBuscarFacturaLista.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarFacturaListaMouseExited

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddSNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSNMouseEntered
        btnAddSN.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnAddSNMouseEntered

    private void btnAddSNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSNMouseExited
        btnAddSN.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddSNMouseExited

    private void txtNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumberKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            factura();
            buscar();
        }
    }//GEN-LAST:event_txtNumberKeyPressed

    private void btnEditSNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditSNMouseEntered
        btnEditSN.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnEditSNMouseEntered

    private void btnEditSNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditSNMouseExited
        btnEditSN.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditSNMouseExited

    private void tableItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableItemsMouseClicked

        if (evt.getClickCount() == 2) {

            int fila = tableItems.getSelectedRow();

            if (fila != -1) {

                modoEdicion = true;
                filaEditable = fila;

                dtm.fireTableDataChanged();

                tableItems.editCellAt(filaEditable, 2);

                Component editor = tableItems.getEditorComponent();

                if (editor != null) {
                    editor.requestFocusInWindow();
                }

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione una fila"
                );
            }
        }
    }//GEN-LAST:event_tableItemsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Proveedor;
    private javax.swing.JLabel Proveedor1;
    private javax.swing.JLabel Proveedor2;
    private javax.swing.JButton btnAddSN;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarFacturaLista;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEditSN;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador3;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_Fecha;
    private javax.swing.JLabel lbl_number;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JLabel lbl_purchase_type;
    private javax.swing.JLabel lbl_supplier;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtSerie;
    // End of variables declaration//GEN-END:variables
}
