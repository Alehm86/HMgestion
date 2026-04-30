/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import classDAO.budgetDAO;
import classDAO.customerDAO;
import classDAO.genericDAO;
import classDAO.productDAO;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import models.modelBudget;
import models.modelBudgetDetail;
import models.modelProducts;

public class purchaseInvoiceDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(purchaseInvoiceDialog.class.getName());
    
    customerDAO qCustomer = new customerDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    
    modelProducts mProduct = new modelProducts();
    modelBudget mBudget = new modelBudget();
    modelBudgetDetail mBdetail = new modelBudgetDetail();

    genericDAO qGeneric = new genericDAO();
    
    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
    
    String fecha;
    
    boolean modoEdicion = false;
    int filaEditable = -1;
    
    DefaultTableModel dtm = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 0 || column == 1 || column == 2 || column == 3);
        }
    }; 
    
    int id_product = -1;
    
    String[] opcionesIVA = {"10.5%", "21%"};
    JComboBox<String> comboIVA = new JComboBox<>(opcionesIVA);
    
    public purchaseInvoiceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        actions();
        inicializar();      
        formatTable();      
        leyendaBotones();
    }
    
    private void leyendaBotones(){
        
        btnBuscar.setToolTipText("Buscar Pedido de compra");
        btnBuscarComprobante.setToolTipText("Buscar pedido de compra en lista");
        btnBuscarProduct.setToolTipText("Seleccionar producto");
        btnBuscar.setToolTipText("Buscar por número de serie");
        btnNewProduct.setToolTipText("Alta producto nuevo");
    }
    
    public void inicializar(){
        lbl_date.setText("");
        lbl_iva.setVisible(false);
        
        qGeneric.llenarCombosActivos(cboSupplier, "suppliers");
        cboSupplier.addItem("Generico");
        
        cboTipe.addItem("Factura A");
        cboTipe.addItem("Factura C");   
        cboTipe.addItem("Sin Factura"); 
        
        
    }

    private void actions(){
               
        btnCalendar.addActionListener(e -> {
            calendarDialog fCalendar = new calendarDialog(parent, true);
            fCalendar.setLocationRelativeTo(null);
            fCalendar.setVisible(true);
            
            String selectedDate = fCalendar.getFecha();
            lbl_date.setText(selectedDate);
        
        });
        
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
        
        btnBuscarProduct.addActionListener(e->{
            
            productSearchDialog fSearchProduct = new productSearchDialog(null, true);
            fSearchProduct.setLocationRelativeTo(null);
            fSearchProduct.setVisible(true);  
            
            id_product = fSearchProduct.getProduct();
            
            if(id_product > 0){
                buscarCode();
            }
            
        });
                
        btnBuscarPorCodigo.addActionListener(e->{
            
            String productCode = txtProductCode.getText().trim();
            id_product = qProduct.selectIdProduct(productCode);        
            buscarCode();            
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
            
            int fila = tableItems.getSelectedRow();

            if (fila != -1) {
                
                modoEdicion = true;
                filaEditable = fila;
                dtm.fireTableDataChanged();
                tableItems.editCellAt(filaEditable, 1);
                tableItems.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            } 
            calcularTotales();
        });
        
        btnNewProduct.addActionListener(e->{
            
            productNewDialog fProductNew = new productNewDialog(null, true);
            fProductNew.setVisible(true);
            id_product = fProductNew.getIdProduct();
            
            if(id_product > 0){
                buscarCode();
            }
            
        });
    }
    
    private void formatTable(){

        String[] titulo = new String[]{"Item", "Cant", "Precio Unit", "Iva","Total"};
        dtm.setColumnIdentifiers(titulo);
        tableItems.setModel(dtm);

        tableItems.getColumnModel().getColumn(0).setPreferredWidth(350);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(40); 
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(100); 
        tableItems.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cboIVA)); 
        tableItems.getColumnModel().getColumn(4).setPreferredWidth(100);
        


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
    
        private void eliminarDeLista(){
        
        int fila = tableItems.getSelectedRow();
        
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
                mBdetail.setAmount(amount);
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
                    mBdetail.getAmount(),
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
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLocaleChooser1 = new com.toedter.components.JLocaleChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txt_comprobante = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnBuscarComprobante = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnCalendar = new javax.swing.JButton();
        lbl_date = new javax.swing.JLabel();
        cboSupplier = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txt_comprobante1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cboTipe = new javax.swing.JComboBox<>();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblIva105 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblIva21 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnRegistrar2 = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtProduct = new javax.swing.JTextField();
        btnBuscarProduct = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnBuscarPorCodigo = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        btnNewProduct = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txt_comprobante.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_comprobante.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)), "Ingresar comprobantes de compra", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 12), new java.awt.Color(12, 83, 151))); // NOI18N

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });

        btnBuscarComprobante.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnBuscarComprobante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarComprobanteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarComprobanteMouseExited(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("FALTA PROGRAMAR");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_comprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(btnBuscarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(txt_comprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(12, 83, 151));
        jLabel10.setText("Proveedor:");

        btnCalendar.setBackground(new java.awt.Color(255, 204, 0));
        btnCalendar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCalendar.setForeground(new java.awt.Color(65, 65, 63));
        btnCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/day32.png"))); // NOI18N
        btnCalendar.setText("Fecha");
        btnCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalendarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCalendarMouseExited(evt);
            }
        });

        lbl_date.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(65, 65, 63));
        lbl_date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_date.setText("xxx");
        lbl_date.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        cboSupplier.setBackground(new java.awt.Color(255, 255, 255));
        cboSupplier.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSupplier.setBorder(null);

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(12, 83, 151));
        jLabel11.setText("Factura nº:");

        txt_comprobante1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_comprobante1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(12, 83, 151));
        jLabel12.setText("Tipo:");

        cboTipe.setBackground(new java.awt.Color(255, 255, 255));
        cboTipe.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboTipe.setBorder(null);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(200, 200, 200)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnCalendar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_comprobante1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel11)
                        .addComponent(txt_comprobante1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        tableItems.setBackground(new java.awt.Color(255, 255, 255));
        tableItems.setForeground(new java.awt.Color(65, 65, 63));
        tableItems.setModel(new javax.swing.table.DefaultTableModel(
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
        tableItems.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableItems.setFillsViewportHeight(true);
        tableItems.setRowHeight(25);
        jScrollPane3.setViewportView(tableItems);

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit_121852.png"))); // NOI18N
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMouseExited(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eliminarCliente.png"))); // NOI18N
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteMouseExited(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(12, 83, 151));
        jLabel15.setText("Subtotal:");

        lblSubtotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblSubtotal.setText("xxx");

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(12, 83, 151));
        jLabel16.setText("IVA 10.5%:");

        lblIva105.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva105.setText("xxx");

        jLabel17.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(12, 83, 151));
        jLabel17.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblTotal.setText("xxx");

        jLabel18.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(12, 83, 151));
        jLabel18.setText("IVA 21%:");

        lblIva21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva21.setText("xxx");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva21, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblIva105)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblIva21)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblTotal)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("otros conceptos");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel1)
                .addContainerGap(149, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar2.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar2.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar2.setText("Registrar");
        btnRegistrar2.setBorder(null);
        btnRegistrar2.setBorderPainted(false);
        btnRegistrar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar2.setFocusPainted(false);
        btnRegistrar2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnRegistrar2.setPreferredSize(new java.awt.Dimension(52, 52));
        btnRegistrar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrar2MouseExited(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir32.png"))); // NOI18N
        btnCancel.setToolTipText("");
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
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(12, 83, 151));
        jLabel6.setText("Items:");

        txtProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtProduct.setForeground(new java.awt.Color(65, 65, 63));

        btnBuscarProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32_1.png"))); // NOI18N
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
        jLabel7.setForeground(new java.awt.Color(12, 83, 151));
        jLabel7.setText("Cantidad:");

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(65, 65, 63));
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setText("Precio:");

        txtPrice.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(65, 65, 63));
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
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddMouseExited(evt);
            }
        });

        btnBuscarPorCodigo.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarPorCodigo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnBuscarPorCodigo.setBorder(null);
        btnBuscarPorCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarPorCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarPorCodigoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarPorCodigoMouseExited(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/barcode32.png"))); // NOI18N

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtProductCode.setForeground(new java.awt.Color(65, 65, 63));
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(65, 65, 63));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Buscar por codigo");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(12, 83, 151));
        jLabel14.setText("IVA:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_iva.setText("xxx");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(65, 65, 63));
        cboIVA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "21.0%", "10.5%" }));
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnNewProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/addProduct32.png"))); // NOI18N
        btnNewProduct.setBorder(null);
        btnNewProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewProductMouseExited(evt);
            }
        });

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
                        .addComponent(jLabel14)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_product)))
                        .addGap(60, 60, 60)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductCode)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarPorCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lbl_product))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBuscarPorCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)
                                .addComponent(lbl_iva)
                                .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,102,102));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRegistrar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrar2MouseEntered
        btnRegistrar2.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrar2MouseEntered

    private void btnRegistrar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrar2MouseExited
        btnRegistrar2.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrar2MouseExited

    private void btnBuscarProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseEntered
        btnBuscarProduct.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarProductMouseEntered

    private void btnBuscarProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarProductMouseExited
        btnBuscarProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarProductMouseExited

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

    private void btnAddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseEntered
        btnAdd.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnAddMouseEntered

    private void btnAddMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseExited
        btnAdd.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddMouseExited

    private void btnBuscarPorCodigoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarPorCodigoMouseEntered
        btnBuscarPorCodigo.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarPorCodigoMouseEntered

    private void btnBuscarPorCodigoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarPorCodigoMouseExited
        btnBuscarPorCodigo.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarPorCodigoMouseExited

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

    private void btnNewProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseEntered
        btnNewProduct.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewProductMouseEntered

    private void btnNewProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseExited
        btnNewProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewProductMouseExited

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        btnBuscar.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        btnBuscar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnBuscarComprobanteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarComprobanteMouseEntered
        btnBuscarComprobante.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarComprobanteMouseEntered

    private void btnBuscarComprobanteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarComprobanteMouseExited
        btnBuscarComprobante.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarComprobanteMouseExited

    private void btnCalendarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalendarMouseEntered
        btnCalendar.setBackground(new Color(255,180,0));
    }//GEN-LAST:event_btnCalendarMouseEntered

    private void btnCalendarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalendarMouseExited
        btnCalendar.setBackground(new Color(255,204,0));
    }//GEN-LAST:event_btnCalendarMouseExited

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        btnEdit.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited

    private void btnDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseEntered
        btnDelete.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnDeleteMouseEntered

    private void btnDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseExited
        btnDelete.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnDeleteMouseExited

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                purchaseInvoiceDialog dialog = new purchaseInvoiceDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBuscarComprobante;
    private javax.swing.JButton btnBuscarPorCodigo;
    private javax.swing.JButton btnBuscarProduct;
    private javax.swing.JButton btnCalendar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNewProduct;
    private javax.swing.JButton btnRegistrar2;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboSupplier;
    private javax.swing.JComboBox<String> cboTipe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.toedter.components.JLocaleChooser jLocaleChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblIva105;
    private javax.swing.JLabel lblIva21;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txt_comprobante;
    private javax.swing.JTextField txt_comprobante1;
    // End of variables declaration//GEN-END:variables
}
