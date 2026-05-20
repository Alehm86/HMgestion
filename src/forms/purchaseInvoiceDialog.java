/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import classDAO.budgetDAO;
import classDAO.customerDAO;
import classDAO.genericDAO;
import classDAO.productDAO;
import classDAO.purchaseInvoiceDAO;
import classDAO.supplierDAO;
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
import models.Budget;
import models.BudgetDetail;
import models.Products;
import models.PurchaseInvoice;
import models.PurchaseInvoiceDetail;
import utils.config;
import utils.utility;

public class purchaseInvoiceDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(purchaseInvoiceDialog.class.getName());
    
    customerDAO qCustomer = new customerDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    supplierDAO qSupplier = new supplierDAO();
    purchaseInvoiceDAO qPurchase = new purchaseInvoiceDAO();
    
    utility utils = new utility();
    
    PurchaseInvoice mPinvoice = new PurchaseInvoice();
    PurchaseInvoiceDetail mPdetail = new PurchaseInvoiceDetail();
    Products mProduct = new Products();
    Budget mBudget = new Budget();
    BudgetDetail mBdetail = new BudgetDetail();

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
        
        txtProduct.setEditable(false);
        
        lbl_date.setText("");
        lbl_iva.setVisible(false);
        
        qGeneric.llenarCombosActivos(cboSupplier, "suppliers");
        
        cboTipe.addItem("Seleccione una opción");
        cboTipe.addItem("Factura A");
        cboTipe.addItem("Factura C");   
        cboTipe.addItem("Sin Factura"); 
        
        lblErrorType.setText("");
        lblErrorNumber.setText("");
        lblErrorSupplier.setText("");
        lblErrorDate.setText("");
        
        lblSubtotal.setText("");
        lblIva105.setText("");
        lblIva21.setText("");
        lblTotal.setText("");
        
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
        
        config.TableStyleUtil.applyPoppinsHeader(tableItems);

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
        qProduct.selectSalePriceAndIva(id_product,txtPrice,lbl_iva);
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

            PurchaseInvoiceDetail  item = new PurchaseInvoiceDetail (
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
        
        txt_comprobante.setText("");
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
        txt_comprobante = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnBuscarComprobante = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cboTipe = new javax.swing.JComboBox<>();
        lblErrorType = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPuntoVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNumber = new javax.swing.JTextField();
        lblErrorNumber = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cboSupplier = new javax.swing.JComboBox<>();
        lblErrorSupplier = new javax.swing.JLabel();
        btnCalendar = new javax.swing.JButton();
        lbl_date = new javax.swing.JLabel();
        lblErrorDate = new javax.swing.JLabel();
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
        jLabel19 = new javax.swing.JLabel();
        txtPercepciones = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtImpInterno = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtDecuento = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaNotes = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
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
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txt_comprobante.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_comprobante.setHorizontalAlignment(javax.swing.JTextField.CENTER);
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
        btnBuscarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
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

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(35, 35, 38));
        jLabel12.setText("Tipo:");

        cboTipe.setBackground(new java.awt.Color(255, 255, 255));
        cboTipe.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboTipe.setForeground(new java.awt.Color(35, 35, 38));
        cboTipe.setBorder(null);

        lblErrorType.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorType.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorType.setText("Mensaje error!");
        lblErrorType.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("Factura nº:");

        txtPuntoVenta.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPuntoVenta.setForeground(new java.awt.Color(35, 35, 38));
        txtPuntoVenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPuntoVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));
        txtPuntoVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPuntoVentaMouseClicked(evt);
            }
        });
        txtPuntoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPuntoVentaKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("-");

        txtNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtNumber.setForeground(new java.awt.Color(35, 35, 38));
        txtNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));
        txtNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNumberMouseClicked(evt);
            }
        });
        txtNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumberKeyPressed(evt);
            }
        });

        lblErrorNumber.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorNumber.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorNumber.setText("Mensaje error!");
        lblErrorNumber.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorType)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorNumber)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPuntoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(cboTipe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPuntoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorType)
                    .addComponent(lblErrorNumber))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setText("Proveedor:");

        cboSupplier.setBackground(new java.awt.Color(255, 255, 255));
        cboSupplier.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboSupplier.setForeground(new java.awt.Color(35, 35, 38));
        cboSupplier.setBorder(null);

        lblErrorSupplier.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorSupplier.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorSupplier.setText("Mensaje error!");
        lblErrorSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnCalendar.setBackground(new java.awt.Color(255, 204, 0));
        btnCalendar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCalendar.setForeground(new java.awt.Color(65, 65, 63));
        btnCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/day32.png"))); // NOI18N
        btnCalendar.setText("Fecha de compra");
        btnCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCalendarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCalendarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCalendarMouseExited(evt);
            }
        });

        lbl_date.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(0, 102, 0));
        lbl_date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_date.setText("xxx");
        lbl_date.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblErrorDate.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorDate.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorDate.setText("Mensaje error!");
        lblErrorDate.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorSupplier)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(120, 120, 120)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorDate)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnCalendar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrorSupplier)
                    .addComponent(lblErrorDate))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(35, 35, 38));
        jLabel15.setText("Subtotal:");

        lblSubtotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(65, 65, 63));
        lblSubtotal.setText("xxx");

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(35, 35, 38));
        jLabel16.setText("IVA 10.5%:");

        lblIva105.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva105.setForeground(new java.awt.Color(65, 65, 63));
        lblIva105.setText("xxx");

        jLabel17.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(65, 65, 63));
        jLabel17.setText("Total:");

        lblTotal.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(65, 65, 63));
        lblTotal.setText("xxx");

        jLabel18.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(35, 35, 38));
        jLabel18.setText("IVA 21%:");

        lblIva21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblIva21.setForeground(new java.awt.Color(65, 65, 63));
        lblIva21.setText("xxx");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIva105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIva21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                        .addGap(17, 17, 17))))
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
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(35, 35, 38));
        jLabel19.setText("Percepciones:");

        txtPercepciones.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPercepciones.setForeground(new java.awt.Color(65, 65, 63));
        txtPercepciones.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPercepciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPercepcionesKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPercepcionesKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(35, 35, 38));
        jLabel20.setText("Imp. interno:");

        txtImpInterno.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtImpInterno.setForeground(new java.awt.Color(65, 65, 63));
        txtImpInterno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImpInterno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImpInternoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtImpInternoKeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(35, 35, 38));
        jLabel21.setText("Descuento:");

        txtDecuento.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtDecuento.setForeground(new java.awt.Color(65, 65, 63));
        txtDecuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDecuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDecuentoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDecuentoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPercepciones, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImpInterno, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDecuento, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19)
                    .addComponent(txtPercepciones, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel20)
                    .addComponent(txtImpInterno, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21)
                    .addComponent(txtDecuento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)));

        jScrollPane1.setForeground(new java.awt.Color(65, 65, 63));

        jTextAreaNotes.setColumns(20);
        jTextAreaNotes.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextAreaNotes.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaNotes.setLineWrap(true);
        jTextAreaNotes.setRows(5);
        jScrollPane1.setViewportView(jTextAreaNotes);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Notas");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setBorderPainted(false);
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

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(101, 129, 171));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir32.png"))); // NOI18N
        btnCancel.setText("Salir");
        btnCancel.setToolTipText("");
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
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
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
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
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Items:");

        txtProduct.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtProduct.setForeground(new java.awt.Color(35, 35, 38));
        txtProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Cantidad:");

        txtCantidad.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(35, 35, 38));
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
        txtPrice.setForeground(new java.awt.Color(35, 35, 38));
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

        btnBuscarPorCodigo.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarPorCodigo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
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
        txtProductCode.setForeground(new java.awt.Color(35, 35, 38));
        txtProductCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
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
        jLabel14.setForeground(new java.awt.Color(35, 35, 38));
        jLabel14.setText("IVA:");

        lbl_iva.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_iva.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva.setText("xxx");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(35, 35, 38));
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

        jPanel8.setBackground(new java.awt.Color(101, 129, 171));

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
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

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

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

    }//GEN-LAST:event_txtPriceKeyPressed

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped

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

    private void txtPuntoVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPuntoVentaMouseClicked
        lblErrorNumber.setText("");
    }//GEN-LAST:event_txtPuntoVentaMouseClicked

    private void btnCalendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCalendarMouseClicked
        lblErrorDate.setText("");
    }//GEN-LAST:event_btnCalendarMouseClicked

    private void txtNumberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNumberMouseClicked
        lblErrorNumber.setText("");
    }//GEN-LAST:event_txtNumberMouseClicked

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

    private void txtPercepcionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPercepcionesKeyPressed

    }//GEN-LAST:event_txtPercepcionesKeyPressed

    private void txtPercepcionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPercepcionesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPercepcionesKeyTyped

    private void txtImpInternoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImpInternoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImpInternoKeyPressed

    private void txtImpInternoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImpInternoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImpInternoKeyTyped

    private void txtDecuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecuentoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDecuentoKeyPressed

    private void txtDecuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDecuentoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDecuentoKeyTyped

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
    private javax.swing.JLabel jLabel9;
    private com.toedter.components.JLocaleChooser jLocaleChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
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
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_iva;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTable tableItems;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDecuento;
    private javax.swing.JTextField txtImpInterno;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtPercepciones;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtPuntoVenta;
    private javax.swing.JTextField txt_comprobante;
    // End of variables declaration//GEN-END:variables
}
