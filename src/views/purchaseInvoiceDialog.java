/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.budgetDAO;
import dao.customerDAO;
import dao.genericDAO;
import dao.productDAO;
import dao.purchaseInvoiceDAO;
import dao.supplierDAO;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import models.mBudget;
import models.mBudgetDetail;
import models.mProducts;
import models.mPurchaseInvoice;
import models.mPurchaseInvoiceDetail;
import utils.tableStyleUtil;
import utils.utility;

public class purchaseInvoiceDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(purchaseInvoiceDialog.class.getName());
    
    customerDAO qCustomer = new customerDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    supplierDAO qSupplier = new supplierDAO();
    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO();
    
    utility utils = new utility();
    
    mPurchaseInvoice mPinvoice = new mPurchaseInvoice();
    mPurchaseInvoiceDetail mPdetail = new mPurchaseInvoiceDetail();
    mProducts mProduct = new mProducts();
    mBudget mBudget = new mBudget();
    mBudgetDetail mBdetail = new mBudgetDetail();

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
        
        inicializar();
        actions();
              
        formatTable();      
        leyendaBotones();
        
        aplicarFiltroNumericoConDecimal(txtPercepciones, 8);
        aplicarFiltroNumericoConDecimal(txtPrice, 15);
        aplicarFiltroNumericoConDecimal(txtImpInterno, 15);
        aplicarFiltroNumericoConDecimal(txtDecuento, 15);
        
    }
    
    private void leyendaBotones(){
        
        btnBuscar.setToolTipText("Buscar Pedido de compra");
        btnBuscarComprobante.setToolTipText("Buscar pedido de compra en lista");
        btnBuscarProduct.setToolTipText("Seleccionar producto");
        btnBuscar.setToolTipText("Buscar por número de serie");
        btnNewProduct.setToolTipText("Alta producto nuevo");
    }
    
    public void inicializar(){

        cboIVA.addItem("21.0%");
        cboIVA.addItem("10.5%");
        
        cboTipe.addItem("Seleccione una opción");
        cboTipe.addItem("Factura A");
        cboTipe.addItem("Factura C");   
        cboTipe.addItem("Sin Factura"); 
        
        utils.agregarPlaceholderN(txtComprobante, "Ingrese comprobante de compra."); 
        
        txtProduct.setEditable(false);
        
        lbl_date.setText("");
        lbl_iva.setVisible(false);
        lblStock.setVisible(false);
        
        qGeneric.llenarCombosActivos(cboSupplier, "suppliers");
        
        lblErrorType.setText("");
        lblErrorNumber.setText("");
        lblErrorSupplier.setText("");
        lblErrorDate.setText("");
        
        lblSubtotal.setText("");
        lblIva105.setText("");
        lblIva21.setText("");
        lblTotal.setText("");
        
        lbl_product.setVisible(false);
    }

    private void actions(){
        
        utils.clearMsjErrorCombo(cboTipe,lblErrorType);
        utils.clearMsjErrorCombo(cboSupplier,lblErrorSupplier);
                      
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
        
        btnRegistrar.addActionListener(e->{
            insertPurchaseInvoice();
        });
        
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { calcularTotales(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calcularTotales(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calcularTotales(); }
        };

        txtPercepciones.getDocument().addDocumentListener(dl);
        txtImpInterno.getDocument().addDocumentListener(dl);
        txtDecuento.getDocument().addDocumentListener(dl);
        
        cboTipe.addActionListener(e->{
            String opcion = cboTipe.getSelectedItem().toString();
            
            if(opcion.equals("Sin Factura")){
                txtPuntoVenta.setEditable(false);
                txtNumber.setEditable(false);
                txtPuntoVenta.setText("");
                txtNumber.setText("");
            }else{
                txtPuntoVenta.setEditable(true);
                txtNumber.setEditable(true); 
            }
            
        });
        
    }
    
    private void formatTable(){

        String[] titulo = new String[]{"Code","Item", "Cant", "Precio Unit", "Iva","Total"};
        dtm.setColumnIdentifiers(titulo);
        tableItems.setModel(dtm);
        
        tableStyleUtil.applyPoppinsHeader(tableItems);

        tableItems.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableItems.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableItems.getColumnModel().getColumn(2).setPreferredWidth(40); 
        tableItems.getColumnModel().getColumn(3).setPreferredWidth(100); 
        tableItems.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cboIVA)); 
        tableItems.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        dtm.addTableModelListener(e -> {

            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if (columna == 2 || columna == 3) {

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
        qProduct.selectSalePriceAndIva(id_product,txtPrice,lbl_iva,lblStock);
        txtProduct.setText(lbl_product.getText());
        infoCombo();
        txtPrice.setText("");
    }   
    
    void addFile(String code, String descripcion, int amount, double precio, String iva, double total){
        dtm.addRow(new Object[]{code,descripcion,amount,precio,iva,total});
    }
    
    private void addToList(){

        boolean estado = true;       
        
        int amount = 0;
        double price = 0;        
        double total = 0;
        String iva;
        
        String codeProduct = "";
        
  
        if(!txtProduct.getText().isEmpty()){
            
            price = Double.parseDouble(txtPrice.getText().trim());
         
            codeProduct = txtProductCode.getText().trim();
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
                    codeProduct,
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
        double percepciones = 0;
        double impInterno = 0;
        double descuento = 0;
        
        if(!txtPercepciones.getText().isEmpty()){
            percepciones = Double.parseDouble(txtPercepciones.getText().trim());
        }
        
        if(!txtImpInterno.getText().isEmpty()){
            impInterno = Double.parseDouble(txtImpInterno.getText().trim());
        }
        
        if(!txtDecuento.getText().isEmpty()){
            descuento = Double.parseDouble(txtDecuento.getText().trim());
        }
        

        for (int i = 0; i < dtm.getRowCount(); i++) {

            try {
                int cantidad = Integer.parseInt(dtm.getValueAt(i, 2).toString());

                double precioFinal = Double.parseDouble(
                    dtm.getValueAt(i, 3).toString()
                );

                String ivaStr = dtm.getValueAt(i, 4).toString();

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

        double total = subtotal + iva105 + iva21 + percepciones + impInterno - descuento;

        lblSubtotal.setText("$ " + String.format("%.2f", subtotal));
        lblIva105.setText("$ " + String.format("%.2f", iva105));
        lblIva21.setText("$ " + String.format("%.2f", iva21));
        lblTotal.setText("$ " + String.format("%.2f", total));
    }
    
    private void insertPurchaseInvoice(){
        
        boolean valido = true;
        boolean status = false;
        
        int id_purchase_invoice = -1;
        
        String msj = "Seleccione una opcion!";
        
        String tipoFactura = cboTipe.getSelectedItem().toString();       
        if(cboTipe.getSelectedIndex() > 0){
            mPinvoice.setType(tipoFactura);
        }else{
            valido = false;
            lblErrorType.setText(msj);           
        }
        
        String Supplier =  cboSupplier.getSelectedItem().toString().trim();
        int idSupplier = qSupplier.getIdSupplier(Supplier);
        if(cboSupplier.getSelectedIndex() > 0){
            mPinvoice.setSupplier_id(idSupplier);
        }else{
            valido = false;
            lblErrorSupplier.setText(msj);
        }
            
        String puntoVenta = txtPuntoVenta.getText().trim();
        String numero = txtNumber.getText().trim();

        String numeroFactura = puntoVenta.toUpperCase() + "-" + numero.toUpperCase();

        if (!puntoVenta.isEmpty() && !numero.isEmpty()) {
            mPinvoice.setNumber(numeroFactura);
        } else {
            valido = false;
            lblErrorNumber.setText(msj);
        }
        
        String fecha = lbl_date.getText();

        if (fecha.isEmpty()) {
            valido = false;
            lblErrorDate.setText("Seleccione la fecha de compra!");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date fechaCompra = sdf.parse(fecha);
                mPinvoice.setFechaCompra(fechaCompra);
            } catch (Exception e) {
                valido = false;
            }
        }
        
        if(tableItems.getRowCount() > 0){
            
            String textoSubtotal = lblSubtotal.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double subtotal = Double.parseDouble(textoSubtotal);
            mPinvoice.setSubtotal(subtotal);
            
            String textoIva10_5 = lblIva105.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double iva10_5 = Double.parseDouble(textoIva10_5);
            mPinvoice.setIva_10_5(iva10_5);
            
            String textoIva21 = lblIva21.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double iva21 = Double.parseDouble(textoIva21);
            mPinvoice.setIva_21(iva21);
            
            String textoTotal = lblTotal.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double total = Double.parseDouble(textoTotal);
            mPinvoice.setTotal(total);          
            
        }else{
            valido = false;
            JOptionPane.showMessageDialog(null, "Debe ingresar items!");
        }
        
        if(!txtDecuento.getText().isEmpty()){           
            String textoDescuento = txtDecuento.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double descuento = Double.parseDouble(textoDescuento);
            mPinvoice.setDiscount(descuento);
        }else{
            mPinvoice.setDiscount(null);
        }
        
        if(!txtPercepciones.getText().isEmpty()){
            String textoPersepciones = txtPercepciones.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double persepciones = Double.parseDouble(textoPersepciones);
            mPinvoice.setPercepciones(persepciones);
        }else{
            mPinvoice.setPercepciones(null);
        }
 
        if(!txtImpInterno.getText().isEmpty()){
            String textoImpInterno = txtImpInterno.getText().replace("$", "").replace(".", "").replace(",", ".").trim();
            double impInterno = Double.parseDouble(textoImpInterno);
            mPinvoice.setImpInterno(impInterno);
        }else{
            mPinvoice.setImpInterno(null);
        }
        
        String notas = jTextAreaNotes.getText().trim();
        if(!notas.isEmpty()){
            mPinvoice.setNotes(notas);
        }else{
            mPinvoice.setNotes(null);
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
        
        id_purchase_invoice = qPurchase.insertPurchaseInvoice(
                mPinvoice.getType(), 
                mPinvoice.getNumber(), 
                mPinvoice.getSupplier_id(), 
                mPinvoice.getFechaCompra(), 
                mPinvoice.getSubtotal(), 
                mPinvoice.getDiscount(), 
                mPinvoice.getIva_10_5(), 
                mPinvoice.getIva_21(), 
                mPinvoice.getPercepciones(), 
                mPinvoice.getImpInterno(), 
                mPinvoice.getTotal(), 
                mPinvoice.getNotes()
        );
        
        if(id_purchase_invoice <= 0){
            JOptionPane.showMessageDialog(null, "Error al registrar factura.");
            return;
        }

        for(int i = 0; i < tableItems.getRowCount(); i++){

            int idProduct = qProduct.selectIdProduct(tableItems.getValueAt(i, 0).toString());
            int cantidad = Integer.parseInt(tableItems.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(tableItems.getValueAt(i, 3).toString());
            String iva = tableItems.getValueAt(i, 4).toString();
            double total = Double.parseDouble(tableItems.getValueAt(i, 5).toString());

            mPurchaseInvoiceDetail  item = new mPurchaseInvoiceDetail (
                    idProduct,
                    cantidad,
                    precio,
                    iva,
                    total
            );

            status = qPurchase.insertPurchaceInvoiceDetail(
                    id_purchase_invoice,
                    item.getId_product(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getIva(),
                    item.getTotal()
            );
            
        }
        
        if(status){
            JOptionPane.showMessageDialog(null, "Factura registrada correctamente!."); 
            limpiar(); 
        }else{
            JOptionPane.showMessageDialog(null, "Error al registrar detalle de factura.");
        }
        
    }
    
    private void aplicarFiltroNumericoConDecimal(JTextField campo, int maxLength) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = campo.getText();

                if (Character.isDigit(c)) return;

                if (c == '.' && !text.contains(".")) return;

                if (c == '\b') return;

                if (text.length() >= maxLength) {
                    e.consume();
                    return;
                }

                e.consume();
            }
        });
    }
    
    private void limpiar(){
        
        txtComprobante.setText("");
        cboTipe.setSelectedIndex(0);
        txtPuntoVenta.setText("");
        txtNumber.setText("");
        cboSupplier.setSelectedIndex(0);
        lbl_date.setText("");
        
        DefaultTableModel model = (DefaultTableModel) tableItems.getModel();
        model.setRowCount(0);
        
        lblSubtotal.setText("");
        lblIva105.setText("");
        lblIva21.setText("");
        lblTotal.setText("");
        
        txtPercepciones.setText("");
        txtImpInterno.setText("");
        txtDecuento.setText("");
        jTextAreaNotes.setText("");      
    }
    
    
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLocaleChooser1 = new com.toedter.components.JLocaleChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnBuscarComprobante = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtComprobante = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblErrorType = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblErrorNumber = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblErrorSupplier = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        lblErrorDate = new javax.swing.JLabel();
        btnCalendar = new javax.swing.JButton();
        cboSupplier = new javax.swing.JComboBox<>();
        cboTipe = new javax.swing.JComboBox<>();
        txtNumber = new javax.swing.JTextField();
        txtPuntoVenta = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableItems = new javax.swing.JTable();
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
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtPercepciones = new javax.swing.JTextField();
        txtImpInterno = new javax.swing.JTextField();
        txtDecuento = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaNotes = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lbl_iva = new javax.swing.JLabel();
        btnBuscarProduct = new javax.swing.JButton();
        btnNewProduct = new javax.swing.JButton();
        btnBuscarPorCodigo = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        cboIVA = new javax.swing.JComboBox<>();
        txtProduct = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        txtProductCode = new javax.swing.JTextField();
        lblStock = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Cargar facturas de compra");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("FALTA PROGRAMAR");

        btnBuscarComprobante.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnBuscarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        btnBuscar.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        txtComprobante.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jPanel13.setBackground(new java.awt.Color(12, 83, 151));
        jPanel13.setForeground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(btnBuscarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(35, 35, 38));
        jLabel12.setText("Tipo:");

        lblErrorType.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorType.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorType.setText("Mensaje error!");
        lblErrorType.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("Factura nº:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("-");

        lblErrorNumber.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorNumber.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorNumber.setText("Mensaje error!");
        lblErrorNumber.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setText("Proveedor:");

        lblErrorSupplier.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorSupplier.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorSupplier.setText("Mensaje error!");
        lblErrorSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lbl_date.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(12, 83, 151));
        lbl_date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_date.setText("xxx");
        lbl_date.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblErrorDate.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorDate.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorDate.setText("Mensaje error!");
        lblErrorDate.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnCalendar.setBackground(new java.awt.Color(255, 255, 204));
        btnCalendar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCalendar.setForeground(new java.awt.Color(12, 83, 151));
        btnCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N
        btnCalendar.setText("Fecha de compra");
        btnCalendar.setFocusable(false);
        btnCalendar.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnCalendar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnCalendar.addActionListener(this::btnCalendarActionPerformed);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorSupplier)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnCalendar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblErrorDate))
                .addGap(60, 60, 60))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrorSupplier)
                    .addComponent(lblErrorDate))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        txtNumber.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNumberMousePressed(evt);
            }
        });
        txtNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumberKeyPressed(evt);
            }
        });

        txtPuntoVenta.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPuntoVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtPuntoVentaMousePressed(evt);
            }
        });
        txtPuntoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPuntoVentaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorType)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorNumber)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPuntoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(71, 71, 71))
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPuntoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorType)
                    .addComponent(lblErrorNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));

        tableItems.setBackground(new java.awt.Color(255, 255, 255));
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
        jScrollPane3.setViewportView(tableItems);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 35, 38)));

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(35, 35, 38));
        jLabel15.setText("Subtotal:");

        lblSubtotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(65, 65, 63));
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("xxx");

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(35, 35, 38));
        jLabel16.setText("IVA 10.5%:");

        lblIva105.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva105.setForeground(new java.awt.Color(65, 65, 63));
        lblIva105.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIva105.setText("xxx");

        jLabel17.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(12, 83, 151));
        jLabel17.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(12, 83, 151));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("xxx");

        jLabel18.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(35, 35, 38));
        jLabel18.setText("IVA 21%:");

        lblIva21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva21.setForeground(new java.awt.Color(65, 65, 63));
        lblIva21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .addComponent(lblIva21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIva105, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(lblIva105, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(lblIva21, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTotal))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 35, 38)));

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(12, 83, 151));
        jLabel19.setText("Percepciones:");

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(12, 83, 151));
        jLabel20.setText("Imp. interno:");

        jLabel21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(12, 83, 151));
        jLabel21.setText("Descuento:");

        txtPercepciones.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPercepciones.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtImpInterno.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtImpInterno.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtDecuento.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtDecuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPercepciones, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(txtImpInterno)
                    .addComponent(txtDecuento))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPercepciones, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtImpInterno, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtDecuento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(35, 35, 38)));

        jScrollPane1.setForeground(new java.awt.Color(65, 65, 63));

        jTextAreaNotes.setColumns(20);
        jTextAreaNotes.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextAreaNotes.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaNotes.setLineWrap(true);
        jTextAreaNotes.setRows(5);
        jScrollPane1.setViewportView(jTextAreaNotes);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Notas");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnDelete.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin32.png"))); // NOI18N

        btnEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar-32.png"))); // NOI18N

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setFocusable(false);
        btnRegistrar.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        btnCancel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(12, 83, 151));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N
        btnCancel.setText("Salir");
        btnCancel.setFocusable(false);
        btnCancel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Items:");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Cantidad:");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/$32.png"))); // NOI18N
        jLabel8.setText("Precio:");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(65, 65, 63));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Buscar por codigo");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(35, 35, 38));
        jLabel14.setText("IVA:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_iva.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva.setText("xxx");

        btnBuscarProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnBuscarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        btnNewProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/addProduct32.png"))); // NOI18N

        btnBuscarPorCodigo.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnBuscarPorCodigo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        btnAdd.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(12, 83, 151));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/more32.png"))); // NOI18N
        btnAdd.setText("Agregar");
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        txtProduct.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

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

        jPanel14.setBackground(new java.awt.Color(12, 83, 151));
        jPanel14.setForeground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel15.setBackground(new java.awt.Color(12, 83, 151));
        jPanel15.setForeground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        txtProductCode.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProductCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductCodeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_iva)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_product))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(98, 98, 98)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                    .addComponent(txtProductCode))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarPorCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStock)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lbl_product)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarPorCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStock))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel7)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_iva)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        jPanel8.setBackground(new java.awt.Color(245, 248, 255));

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(12, 83, 151));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/purchaseInvoice128.png"))); // NOI18N
        jLabel4.setText("Cargar factura de compra");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
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

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtCantidad.getText().length() >= 3) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadKeyTyped

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

        if (txtPrice.getText().length() >= 15
            && Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPriceKeyTyped

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtPuntoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuntoVentaKeyPressed
        txtPuntoVenta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPuntoVenta.getText().length() >= 4) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtPuntoVentaKeyPressed

    private void txtPuntoVentaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPuntoVentaMousePressed
        lblErrorNumber.setText("");
    }//GEN-LAST:event_txtPuntoVentaMousePressed

    private void txtNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumberKeyPressed
        txtNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtNumber.getText().length() >= 8) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtNumberKeyPressed

    private void txtNumberMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNumberMousePressed
        lblErrorNumber.setText("");
    }//GEN-LAST:event_txtNumberMousePressed

    private void btnCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalendarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalendarActionPerformed

    private void txtProductCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String productCode = txtProductCode.getText().trim();
            id_product = qProduct.selectIdProduct(productCode);

            if(id_product > 0){
                buscarCode();
            }
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
    private javax.swing.JButton btnRegistrar;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private com.toedter.components.JLocaleChooser jLocaleChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaNotes;
    private javax.swing.JLabel lblErrorDate;
    private javax.swing.JLabel lblErrorNumber;
    private javax.swing.JLabel lblErrorSupplier;
    private javax.swing.JLabel lblErrorType;
    private javax.swing.JLabel lblIva105;
    private javax.swing.JLabel lblIva21;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtComprobante;
    private javax.swing.JTextField txtDecuento;
    private javax.swing.JTextField txtImpInterno;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtPercepciones;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtPuntoVenta;
    // End of variables declaration//GEN-END:variables
}
