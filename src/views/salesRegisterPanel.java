/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.cashRegisterDAO;
import dao.customerDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.mCashRegDetail;
import models.mCashRegister;
import utils.tableStyleUtil;


public class salesRegisterPanel extends javax.swing.JPanel {

    customerDAO qCustomer = new customerDAO();
    cashRegisterDAO qCashReg = new cashRegisterDAO();
    
    mCashRegister modelCashReg = new mCashRegister();
    mCashRegDetail modelCRDetail = new mCashRegDetail();

    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    String cuitClient = "";
    double totalProducts = 0.00;
    double totalServices = 0.00;
    double saldoPendiente = 0.00;
    double totalFinal = 0.00;
    double totalGeneral = 0.00;
    
    boolean modoEdicion = false;
    int filaEditable = -1;
    
    JComboBox<String> cboPaymentMethod = new JComboBox<>();
    
    DefaultTableModel dtmProduct = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 3);
        }
    };  
    
    DefaultTableModel dtmService = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 2 || column == 3);
        }
    }; 
    
    DefaultTableModel dtmOperation = new DefaultTableModel();
    
    DefaultTableModel dtmPayment = new DefaultTableModel(
        new Object[]{"Método de pago","Monto","Todo","Eliminar"}, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 1;
        }
    };
    
    
    public salesRegisterPanel() {
        initComponents(); 
        
        cboAddOp.addItem("Servicio técnico");
        cboAddOp.addItem("Presupuesto");
        
        panelProduct.setVisible(true);
        panelService.setVisible(true);
        panelBudget.setVisible(true);       
        
        lbl_id_customer.setVisible(false);
        lbl_address.setVisible(false);
                
        inicializar();

        tableProducts();
        tableService();
        tableOperations();
        tablaPagos();
        
        actionCustomer();
        actionProduct();
        actionOperation();    
        actionButtons();
        
    }
    
    private void inicializar(){
        cboAddOp.setEnabled(false);
        btnConfirmOp.setEnabled(false);
        btnAddCancel.setEnabled(false);
        
        lbl_subtProd.setText("0.00");
        lbl_subtServ.setText("0.00");
        lbl_iva_105.setText("0.00");
        lbl_iva_21.setText("0.00");
        lbl_totalFinal.setText("0.00");
        lbl_totalPagos.setText("0.00");
        lbl_Saldo.setText("0.00");
        lbl_totalGeneral.setText("0.00");
        
        lbl_id_customer.setText("-1");
    }
    
    private void tableOperations(){

        String[] titulo = new String[]{"Fecha","Tipo", "Comprobante", "Cliente", "Total"};
        dtmOperation.setColumnIdentifiers(titulo);
        tableOperations.setModel(dtmOperation);
        
        tableStyleUtil.applyPoppinsHeader(tableOperations);

        tableOperations.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableOperations.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableOperations.getColumnModel().getColumn(2).setPreferredWidth(150); 
        tableOperations.getColumnModel().getColumn(3).setPreferredWidth(300); 
        tableOperations.getColumnModel().getColumn(4).setPreferredWidth(100); 

    }

    private void tableProducts(){

        String[] titulo = new String[]{"Id","Comprobante","Producto", "cant.", "Unit. c/IVA","I.V.A", "Total"};
        dtmProduct.setColumnIdentifiers(titulo);
        tableProducts.setModel(dtmProduct);
        
        tableStyleUtil.applyPoppinsHeader(tableProducts);
        
        tableProducts.getColumnModel().getColumn(0).setMinWidth(0);
        tableProducts.getColumnModel().getColumn(0).setMaxWidth(0);
        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(0);

        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableProducts.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(450); 
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(40); 
        tableProducts.getColumnModel().getColumn(4).setPreferredWidth(100); 
        tableProducts.getColumnModel().getColumn(5).setPreferredWidth(50);
        tableProducts.getColumnModel().getColumn(6).setPreferredWidth(100); 

        dtmProduct.addTableModelListener(e -> {

            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if (columna == 3) {

                try {
                    int cantidad = Integer.parseInt(dtmProduct.getValueAt(fila, 3).toString());

                    double precio = Double.parseDouble(dtmProduct.getValueAt(fila, 4).toString());
                    double total = cantidad * precio;
                    dtmProduct.setValueAt(total, fila, 6);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }   
    
    private void tableService(){

        String[] titulo = new String[]{"Comprobante","Item", "cant.", "Unit. c/IVA","I.V.A", "Total"};
        dtmService.setColumnIdentifiers(titulo);
        tableServices.setModel(dtmService);
        
        tableStyleUtil.applyPoppinsHeader(tableServices);

        tableServices.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableServices.getColumnModel().getColumn(1).setPreferredWidth(450);
        tableServices.getColumnModel().getColumn(2).setPreferredWidth(40); 
        tableServices.getColumnModel().getColumn(3).setPreferredWidth(100); 
        tableServices.getColumnModel().getColumn(4).setPreferredWidth(50); 
        tableServices.getColumnModel().getColumn(5).setPreferredWidth(100);

        dtmService.addTableModelListener(e -> {

            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if (columna == 2 || columna == 3) {

                try {
                    int cantidad = Integer.parseInt(dtmService.getValueAt(fila, 2).toString());

                    double precio = Double.parseDouble(dtmService.getValueAt(fila, 3).toString());
                    double total = cantidad * precio;
                    dtmService.setValueAt(total, fila, 5);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }   

    private void tablaPagos(){
        
        String[] titulo = new String[]{"Método de pago","Monto", "", ""};
        dtmPayment.setColumnIdentifiers(titulo);
        tablaPagos.setModel(dtmPayment);
        
        tableStyleUtil.applyPoppinsHeader(tablaPagos);  
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tablaPagos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaPagos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        tablaPagos.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaPagos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaPagos.getColumnModel().getColumn(2).setPreferredWidth(40); 
        tablaPagos.getColumnModel().getColumn(3).setPreferredWidth(40); 
        
        tablaPagos.getColumnModel().getColumn(2).setCellRenderer(new ButtonCellRenderer());
        tablaPagos.getColumnModel().getColumn(3).setCellRenderer(new ButtonCellRenderer());
        
        cboPaymentMethod.addItem("Efectivo");
        cboPaymentMethod.addItem("Transferencia");
        cboPaymentMethod.addItem("Débito");
        cboPaymentMethod.addItem("Crédito");
        cboPaymentMethod.addItem("Cheque");
        cboPaymentMethod.addItem("eCheq");
        cboPaymentMethod.addItem("Cuenta Corriente");
        
        tablaPagos.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cboPaymentMethod));
        
        btnAddMetPago.addActionListener(e->{
             Object[] row = {
                 "Efectivo",
                 0.00,
                 "Todo",
                 "Eliminar"
             };
             dtmPayment.addRow(row);
             calcularPagos();

         }); 
        
        dtmPayment.addTableModelListener(e -> {

            if(e.getColumn() == 1){
                calcularPagos();
            }
        });
        
        tablaPagos.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int fila = tablaPagos.rowAtPoint(e.getPoint());
                int columna = tablaPagos.columnAtPoint(e.getPoint());

                if(fila == -1){
                    return;
                }

                if(columna == 2){
                    calcularSaldo(fila);
                }

                if(columna == 3){
                    dtmPayment.removeRow(fila);
                    calcularPagos();
                }
            }
        });
        
        if(dtmPayment.getRowCount() == 0){
            lbl_totalPagos.setText("0,00");
            lbl_Saldo.setText(String.format("%.2f", totalFinal));
        }
        
    }
       
    public class ButtonCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            if (column == 2){
                label.setBackground(new Color(144, 238, 144));
                label.setForeground(new Color(12,83,151));
            } 
            else if (column == 3){ 
                label.setBackground(new Color(255, 182, 193));
                label.setForeground(new Color(12,83,151));
            } 
            else {
                label.setBackground(Color.WHITE);
            }

            if (isSelected) {
                label.setBackground(new Color(200, 220, 255));
            }
            return label;
        }
    }

    private void actionOperation(){
        
        btnAddOp.addActionListener(e->{
            cboAddOp.setEnabled(true);
            btnConfirmOp.setEnabled(true);
            btnAddCancel.setEnabled(true);
        });
        
        btnAddCancel.addActionListener(e->{
            cboAddOp.setEnabled(false);
            btnConfirmOp.setEnabled(false);
            btnAddCancel.setEnabled(false);
        });
        
        btnConfirmOp.addActionListener(e->{
            
            int id_budget = -1;
            
            String opcion = cboAddOp.getSelectedItem().toString();
            
            if(opcion.equals("Servicio técnico")){
                
                String serviceNumber = "";
                
                serviceSearchListDialog fListServices = new serviceSearchListDialog(null, true);
                
                fListServices.setLocationRelativeTo(null);
                fListServices.setVisible(true);
                
                serviceNumber = fListServices.getService();
                
                if(!serviceNumber.isEmpty()){
                    
                    qCashReg.listServiceOperation(serviceNumber,dtmOperation,dtmProduct,dtmService);
                    
                    totalProducts = totalProductos();
                    lblTotalProducts.setText(String.format("%.2f", totalProducts));
                    
                    totalServices = totalServices();
                    lblTotalServices.setText(String.format("%.2f", totalServices));
                            
                    calcularTotales();
                }
                
            }else if(opcion.equals("Presupuesto")){
                
                budgetListDialog fListBudget = new budgetListDialog(null, true);
                fListBudget.setLocationRelativeTo(null);
                fListBudget.setConfigPage(1);
                fListBudget.setVisible(true);
                
                id_budget = fListBudget.getIdBudget();
            }
            
            if(id_budget > 0){
                
                qCashReg.listBudgetOperation(id_budget, dtmOperation, dtmProduct, dtmService);
                
                totalProducts = totalProductos();
                lblTotalProducts.setText(String.format("%.2f", totalProducts));

                totalServices = totalServices();
                lblTotalServices.setText(String.format("%.2f", totalServices));

                calcularTotales();
            }
            
            cboAddOp.setEnabled(false);
            btnConfirmOp.setEnabled(false);
            btnAddCancel.setEnabled(false);
        });
        
        btnDeleteOp.addActionListener(e->{
            
            int fila = tableOperations.getSelectedRow();

            if(fila == -1){
                JOptionPane.showMessageDialog(null,"Seleccione una operación.");
                return;
            }

            String comprobante = tableOperations.getValueAt(fila, 2).toString().trim();

            eliminarItemsPorComprobante(dtmProduct,1,comprobante);
            eliminarItemsPorComprobante(dtmService,0,comprobante);

            dtmOperation.removeRow(fila);
            
            totalProducts = totalProductos();
            lblTotalProducts.setText(String.format("%.2f", totalProducts));

            totalServices = totalServices();
            lblTotalServices.setText(String.format("%.2f", totalServices));

            calcularTotales();
        });
                
    }
    
    private void actionButtons(){
        
        btnConfirmDiscount.addActionListener(e->{
            calcularDescuento();         
        });
              
        btnConfirm.addActionListener(e->{
            registrarVenta();            
        });
             
        btnCancel.addActionListener(e->{
            limpiar();           
        });
        
        
    }
    
    private void actionCustomer(){
        
        btnSearchCustomer.addActionListener(e->{
            customerSearchDialog pSearch = new customerSearchDialog(parent, true);           
            pSearch.setVisible(true);
            cuitClient = pSearch.getCustomerSerch();
            
            if(!cuitClient.isEmpty()){
                txtDni.setText("");
                txtCustomer.setText("");
                txtPhone.setText("");
                txtDni.setText("");
                txtDireccion.setText("");
                qCustomer.selectCustomerSimplified(cuitClient, lbl_id_customer, txtCustomer, txtPhone, lbl_address);
                txtDni.setText(cuitClient);
            }
        });
    }
    
    private void actionProduct(){
        
        btnAddProduct.addActionListener(e->{
                
            salesProductSelectDialog pSearchProduct = new salesProductSelectDialog(parent, true);
            pSearchProduct.setVisible(true);
            
            dtmProduct.addRow(pSearchProduct.getProduct());
            
            totalProducts = totalProductos();
            lblTotalProducts.setText(String.format("%.2f", totalProducts));
            
            calcularTotales();
        });
        
        btnEditProduct.addActionListener(e->{
            
            int fila = tableProducts.getSelectedRow();

            if (fila != -1) {
                
                modoEdicion = true;
                filaEditable = fila;
                dtmProduct.fireTableDataChanged();
                tableProducts.editCellAt(filaEditable, 3);
                tableProducts.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            } 
            
            totalProducts = totalProductos();
            lblTotalProducts.setText(String.format("%.2f", totalProducts));
            
            calcularTotales();
        });
        
        btnDeleteProduct.addActionListener(e->{  
            
            eliminarDeProductos();
            
            totalProducts = totalProductos();
            lblTotalProducts.setText(String.format("%.2f", totalProducts));
            
            calcularTotales();
        });
    }
    
    private double totalProductos() {

        double total = 0;

        for (int i = 0; i < tableProducts.getRowCount(); i++) {

            Object valor = tableProducts.getValueAt(i, 6);

            if (valor != null) {
                total += Double.parseDouble(valor.toString());
            }
        }

        return total;
    }
    
    private double totalServices() {

        double total = 0;

        for (int i = 0; i < tableServices.getRowCount(); i++) {

            Object valor = tableServices.getValueAt(i, 5);

            if (valor != null) {
                total += Double.parseDouble(valor.toString());
            }
        }

        return total;
    }
    
    private void eliminarDeProductos(){
        
        int fila = tableProducts.getSelectedRow();
        
        if(fila == -1){   
           JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA"); 
        }
        else{
           dtmProduct.removeRow(fila); 
        }
    }
   
    public void calcularTotales(){

        double subtotalProductos = 0;
        double subtotalServicios = 0;

        double iva105 = 0;
        double iva21 = 0;

        for (int i = 0; i < dtmProduct.getRowCount(); i++) {

            try {

                int cantidad = Integer.parseInt(dtmProduct.getValueAt(i, 3).toString());
                double precioFinal = Double.parseDouble(dtmProduct.getValueAt(i, 4).toString());
                String ivaStr = dtmProduct.getValueAt(i, 5).toString();

                double base;
                double iva;

                if (ivaStr.contains("21")) {

                    base = precioFinal / 1.21;
                    iva = precioFinal - base;

                    iva21 += iva * cantidad;

                } else {

                    base = precioFinal / 1.105;
                    iva = precioFinal - base;

                    iva105 += iva * cantidad;
                }

                subtotalProductos += base * cantidad;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < dtmService.getRowCount(); i++) {

            try {

                int cantidad = Integer.parseInt(dtmService.getValueAt(i, 2).toString());
                double precioFinal = Double.parseDouble(dtmService.getValueAt(i, 3).toString());

                double base = precioFinal / 1.21;
                double iva = precioFinal - base;

                iva21 += iva * cantidad;

                subtotalServicios += base * cantidad;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lbl_subtProd.setText("$ " + String.format("%.2f", subtotalProductos));
        lbl_subtServ.setText("$ " + String.format("%.2f", subtotalServicios));

        lbl_iva_105.setText("$ " + String.format("%.2f", iva105));
        lbl_iva_21.setText("$ " + String.format("%.2f", iva21));
        
        Double total = subtotalProductos + subtotalServicios + iva105 + iva21;
        
        lbl_totalFinal.setText("$ " + String.format("%.2f", total));
        lbl_totalGeneral.setText("$ " + String.format("%.2f", total));
        
        totalFinal = total;
        totalGeneral = total;
        saldoPendiente = total;
        lbl_Saldo.setText("$ " + String.format("%.2f", saldoPendiente));
    }    
    
    private void calcularDescuento(){
        
        Double subtotalProductos = Double.parseDouble(lbl_subtProd.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));
        Double subtotalServicios = Double.parseDouble(lbl_subtServ.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));
        Double iva105 = Double.parseDouble(lbl_iva_105.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));
        Double iva21 = Double.parseDouble(lbl_iva_21.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));

        double total = subtotalProductos + subtotalServicios + iva105 + iva21;

        if(txtDescuento.getText().trim().isEmpty()){

            lbl_totalFinal.setText(String.format("%.2f", total));
            return;
        }

        double descuento = Double.parseDouble(txtDescuento.getText().trim());

        if(cboTypeDesc.getSelectedIndex() == 0){

            total -= total * descuento / 100;

        }else{

            total -= descuento;
        }

        lbl_totalFinal.setText("$ " + String.format("%.2f", total));
        
        totalFinal = total;
        saldoPendiente = total;
        lbl_Saldo.setText("$ " + String.format("%.2f", saldoPendiente));
    }
    
    private void eliminarItemsPorComprobante(DefaultTableModel modelo,int columnaComprobante,String comprobante){

        for(int i = modelo.getRowCount() - 1; i >= 0; i--){

            String comp = modelo.getValueAt(i, columnaComprobante).toString().trim();

            if(comp.equals(comprobante)){
                modelo.removeRow(i);
            }
        }
    }
    
    private void calcularPagos(){

        double totalPagado = 0;

        for(int i = 0; i < dtmPayment.getRowCount(); i++){

            try{
                String montoAgregadoSt = dtmPayment.getValueAt(i, 1).toString().trim();
                
                if(montoAgregadoSt.isEmpty()){
                    continue;
                }
                
                double montoAgregadoDb = Double.parseDouble(montoAgregadoSt);
                
                if(montoAgregadoDb < 0){
                    JOptionPane.showMessageDialog(null,"No se permiten importes negativos.");
                    return;
                }
                
                totalPagado += montoAgregadoDb;

            }catch(Exception e){
            }
        }

        lbl_totalPagos.setText(String.format("%.2f", totalPagado));
        double saldo = totalFinal - totalPagado;
        lbl_Saldo.setText(String.format("%.2f", saldo));
    }
    
//    private void calcularSaldo(int fila){
//
//        double totalPagado = 0;
//
//        for(int i = 0; i < dtmPayment.getRowCount(); i++){
//
//            if(i == fila){
//                continue;
//            }
//
//            try{
//                totalPagado += Double.parseDouble(dtmPayment.getValueAt(i, 1).toString());
//
//            }catch(Exception e){
//            }
//        }
//
//        double saldoPendiente = totalFinal - totalPagado;
//
//        if(saldoPendiente < 0){
//            saldoPendiente = 0;
//        }
//
//        dtmPayment.setValueAt(saldoPendiente,fila,1);
//
//        calcularPagos();
//    }
    private void calcularSaldo(int fila){

        BigDecimal totalPagado = BigDecimal.ZERO;

        for(int i = 0; i < dtmPayment.getRowCount(); i++){

            if(i == fila){
                continue;
            }

            try {
                BigDecimal valor = new BigDecimal(dtmPayment.getValueAt(i, 1).toString());
                totalPagado = totalPagado.add(valor);
            } catch(Exception e){
            }
        }

        BigDecimal total = new BigDecimal(String.valueOf(totalFinal));
        BigDecimal saldoPendiente = total.subtract(totalPagado);

        if (saldoPendiente.compareTo(BigDecimal.ZERO) < 0) {
            saldoPendiente = BigDecimal.ZERO;
        }

        saldoPendiente = saldoPendiente.setScale(2, RoundingMode.HALF_UP);
        dtmPayment.setValueAt(saldoPendiente.doubleValue(), fila, 1);
        calcularPagos();
    }
    
    private void registrarVenta(){
        
        boolean valido = true;
        boolean status = false;
        
        int id_operation = -1;      
        int id_customer = Integer.parseInt(lbl_id_customer.getText().trim());       
        double discount =  totalGeneral - totalFinal;     
        double saldo = Double.parseDouble(lbl_Saldo.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));       
        String observation = textAreaObservation.getText();
        
        if(!observation.isEmpty()){
            modelCashReg.setObservation(observation);
        }else{
            modelCashReg.setObservation(null);
        }
        
        if(id_customer > 0){
            modelCashReg.setId_customer(id_customer);
        }else{
            modelCashReg.setId_customer(null);
        }
        
        if(totalGeneral != totalFinal){
            modelCashReg.setDiscount(discount);
        }else{
            modelCashReg.setDiscount(0.00);
        }
          
        if(saldo < 0){
            JOptionPane.showMessageDialog(null, "El importe excede el total a pagar");
            return;
        }else if(saldo > 0){
            JOptionPane.showMessageDialog(null, "Falta pagar: $" + saldo);
            return;
        }else{
            
        }
        
        if(tableProducts.getRowCount() < 1 && tableServices.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "No hay ítems cargados.");
            valido = false;
        }
        
        if(tablaPagos.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "No hay metodo de pago cargado.");
            valido = false;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Confirma el registro?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );                   
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }  
        
        id_operation = qCashReg.cashRegister(
                modelCashReg.getId_customer(), 
                totalGeneral, 
                modelCashReg.getDiscount(), 
                totalFinal, 
                modelCashReg.getObservation()
        );
        
        if(id_operation <= 0){
            JOptionPane.showMessageDialog(null, "Error al registrar el presupuesto.");
            return;
        }
        
        if(tableProducts.getRowCount() > 0){
            
           for(int i = 0; i < tableProducts.getRowCount(); i++){

                String operation = tableProducts.getValueAt(i, 1).toString();
                String description = tableProducts.getValueAt(i, 2).toString();           
                String type = "product";
                
                String idProd = "";
                
                Object value = tableProducts.getValueAt(i, 0);
                if (value != null) {
                    idProd = value.toString().trim();
                }
                Integer id_product = null;
                if (!idProd.isEmpty()) {
                    id_product = Integer.parseInt(idProd);
                }
                
                int quantity = Integer.parseInt(tableProducts.getValueAt(i, 3).toString());
                double price = Double.parseDouble(tableProducts.getValueAt(i, 4).toString());
                String iva = tableProducts.getValueAt(i, 5).toString();
                double subtotal = Double.parseDouble(tableProducts.getValueAt(i, 6).toString());                 

                mCashRegDetail item = new mCashRegDetail(
                        operation,
                        description,
                        type,
                        id_product,
                        quantity,
                        price,
                        iva,
                        subtotal
                );

                status = qCashReg.insertCashRegDetail(
                        id_operation,
                        item.getOperation(),
                        item.getDescription(),
                        item.getType(),
                        item.getId_product(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getIva(),
                        item.getSubtotal()
                );
            } 
        }
        
        if(tableServices.getRowCount() > 0){
                       
            for(int i = 0; i < tableServices.getRowCount(); i++){

                String operation = tableServices.getValueAt(i, 0).toString();
                String description = tableServices.getValueAt(i, 1).toString();           
                String type = "service";
                Integer id_product = null;
                int quantity = Integer.parseInt(tableServices.getValueAt(i, 2).toString());
                double price = Double.parseDouble(tableServices.getValueAt(i, 3).toString());
                String iva = "21%";
                double subtotal = Double.parseDouble(tableServices.getValueAt(i, 5).toString());                      

                mCashRegDetail item = new mCashRegDetail(
                        operation,
                        description,
                        type,
                        id_product,
                        quantity,
                        price,
                        iva,
                        subtotal
                );

                status = qCashReg.insertCashRegDetail(
                        id_operation,
                        item.getOperation(),
                        item.getDescription(),
                        item.getType(),
                        item.getId_product(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getIva(),
                        item.getSubtotal()
                );
            } 
            
        }
           
        //ADD METODO DE PAGO
        //cuenta corriente
        //UPDATE STOCK
        //ENTREGAR SERVICIO
        //UPDATE PRESUPUESTO
        
        JOptionPane.showMessageDialog(null, "Presupuesto registrado correctamente.");
        
        limpiar();
        
    }
    
    private void limpiar(){
        
        txtCustomer.setText("");
        txtPhone.setText("");
        txtDni.setText("");
        txtDireccion.setText("");
        
        lbl_id_customer.setText("-1");
        
        lbl_subtProd.setText("0.00");
        lbl_subtServ.setText("0.00");
        lbl_iva_105.setText("0.00");
        lbl_iva_21.setText("0.00");
        lbl_totalGeneral.setText("0.00");
        lbl_totalFinal.setText("0.00");
        lbl_totalPagos.setText("0.00");
        lbl_Saldo.setText("0.00");
        lblTotalProducts.setText("0.00");
        lblTotalServices.setText("0.00");
        
        txtDescuento.setText("");
        cboTypeDesc.setSelectedIndex(0);
        
        textAreaObservation.setText("");
        
        dtmOperation.setRowCount(0);
        dtmProduct.setRowCount(0);
        dtmService.setRowCount(0);
        dtmPayment.setRowCount(0);
        
        totalProducts = 0.00;
        totalServices = 0.00;
        saldoPendiente = 0.00;
        totalFinal = 0.00;
        totalGeneral = 0.00;
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        cboTypeDesc = new javax.swing.JComboBox<>();
        lbl_subtProd = new javax.swing.JLabel();
        lbl_subtServ = new javax.swing.JLabel();
        lbl_iva_105 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        lbl_totalFinal = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        lbl_iva_21 = new javax.swing.JLabel();
        btnConfirmDiscount = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        lbl_totalGeneral = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaPagos = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        lbl_totalPagos = new javax.swing.JLabel();
        btnAddMetPago = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        lbl_Saldo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        textAreaObservation = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        btnSearchCustomer = new javax.swing.JButton();
        btnAddCustomer = new javax.swing.JButton();
        lbl_id_customer = new javax.swing.JLabel();
        lbl_address = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        panelBudget = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        btnAddOp = new javax.swing.JButton();
        btnDeleteOp = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableOperations = new javax.swing.JTable();
        cboAddOp = new javax.swing.JComboBox<>();
        btnConfirmOp = new javax.swing.JButton();
        btnAddCancel = new javax.swing.JButton();
        panelProduct = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnAddProduct = new javax.swing.JButton();
        btnEditProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProducts = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        lblTotalProducts = new javax.swing.JLabel();
        panelService = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        lblTotalServices = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableServices = new javax.swing.JTable();
        jPanelSeparador3 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        jLabel45.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(12, 83, 151));
        jLabel45.setText("Registrar venta");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1172, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(12, 83, 151));
        jLabel34.setText("Resumen");

        jLabel39.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(35, 35, 38));
        jLabel39.setText("Subtotal productos (sin IVA):");

        jLabel40.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(35, 35, 38));
        jLabel40.setText("Subtotal servicios (sin IVA):");

        jPanelSeparador4.setBackground(new java.awt.Color(245, 248, 255));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jLabel42.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(35, 35, 38));
        jLabel42.setText("Descuento:");

        txtDescuento.addActionListener(this::txtDescuentoActionPerformed);

        cboTypeDesc.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboTypeDesc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "$" }));

        lbl_subtProd.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_subtProd.setForeground(new java.awt.Color(35, 35, 38));
        lbl_subtProd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_subtProd.setText("xxx");

        lbl_subtServ.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_subtServ.setForeground(new java.awt.Color(35, 35, 38));
        lbl_subtServ.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_subtServ.setText("xxx");

        lbl_iva_105.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_105.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_105.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_iva_105.setText("xxx");

        jPanel10.setBackground(new java.awt.Color(245, 248, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel46.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(12, 83, 151));
        jLabel46.setText("TOTAL FINAL:");

        lbl_totalFinal.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lbl_totalFinal.setForeground(new java.awt.Color(12, 83, 151));
        lbl_totalFinal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_totalFinal.setText("XXX");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_totalFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_totalFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel41.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(35, 35, 38));
        jLabel41.setText("IVA 10.5%");

        jLabel54.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(35, 35, 38));
        jLabel54.setText("IVA 21%");

        lbl_iva_21.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_iva_21.setForeground(new java.awt.Color(35, 35, 38));
        lbl_iva_21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_iva_21.setText("xxx");

        btnConfirmDiscount.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnConfirmDiscount.setForeground(new java.awt.Color(35, 35, 38));
        btnConfirmDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(35, 35, 38));
        jLabel55.setText("Total:");

        lbl_totalGeneral.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_totalGeneral.setForeground(new java.awt.Color(35, 35, 38));
        lbl_totalGeneral.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_totalGeneral.setText("xxx");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_subtProd, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_subtServ, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lbl_iva_105, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel54))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_iva_21, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lbl_totalGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboTypeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnConfirmDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel55)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_subtProd, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_subtServ, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbl_iva_105, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_iva_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_totalGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboTypeDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel43.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(12, 83, 151));
        jLabel43.setText("Pagos");

        tablaPagos.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tablaPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tablaPagos);

        jPanel12.setBackground(new java.awt.Color(245, 248, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel48.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(12, 83, 151));
        jLabel48.setText("Total pagos:");

        lbl_totalPagos.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_totalPagos.setForeground(new java.awt.Color(12, 83, 151));
        lbl_totalPagos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_totalPagos.setText("XXX");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_totalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_totalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnAddMetPago.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAddMetPago.setForeground(new java.awt.Color(35, 35, 38));
        btnAddMetPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add16.png"))); // NOI18N
        btnAddMetPago.setText("Agregar pago");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel47.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(153, 0, 0));
        jLabel47.setText("Saldo pendiente:");

        lbl_Saldo.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lbl_Saldo.setForeground(new java.awt.Color(153, 0, 0));
        lbl_Saldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Saldo.setText("XXX");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(35, 35, 38));
        jLabel44.setText("Observaciones de pago:");

        textAreaObservation.setColumns(20);
        textAreaObservation.setRows(5);
        jScrollPane6.setViewportView(textAreaObservation);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        btnConfirm.setBackground(new java.awt.Color(0, 153, 51));
        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirm.setText("Registrar operación");

        btnCancel.setBackground(new java.awt.Color(204, 51, 0));
        btnCancel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancelar");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddMetPago))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddMetPago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel49.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(12, 83, 151));
        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/customer16.png"))); // NOI18N
        jLabel49.setText("Cliente");

        jLabel50.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(35, 35, 38));
        jLabel50.setText("Nombre:");

        jLabel51.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(35, 35, 38));
        jLabel51.setText("Teléfono:");

        txtPhone.addActionListener(this::txtPhoneActionPerformed);

        jLabel52.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(35, 35, 38));
        jLabel52.setText("Dni/Cuit");

        jLabel53.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(35, 35, 38));
        jLabel53.setText("Dirección:");

        btnSearchCustomer.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnSearchCustomer.setForeground(new java.awt.Color(35, 35, 38));
        btnSearchCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        btnAddCustomer.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAddCustomer.setForeground(new java.awt.Color(35, 35, 38));
        btnAddCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add_32.png"))); // NOI18N

        lbl_id_customer.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_id_customer.setForeground(new java.awt.Color(35, 35, 38));
        lbl_id_customer.setText("id_cliente");

        lbl_address.setText("address_cliente");

        panelBudget.setBackground(new java.awt.Color(255, 255, 255));

        jLabel37.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(12, 83, 151));
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/budget16.png"))); // NOI18N
        jLabel37.setText("Operación");

        btnAddOp.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAddOp.setForeground(new java.awt.Color(35, 35, 38));
        btnAddOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add16.png"))); // NOI18N

        btnDeleteOp.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnDeleteOp.setForeground(new java.awt.Color(35, 35, 38));
        btnDeleteOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin16.png"))); // NOI18N

        tableOperations.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableOperations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(tableOperations);

        cboAddOp.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cboAddOp.setForeground(new java.awt.Color(35, 35, 38));

        btnConfirmOp.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnConfirmOp.setForeground(new java.awt.Color(35, 35, 38));
        btnConfirmOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok-16.png"))); // NOI18N

        btnAddCancel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAddCancel.setForeground(new java.awt.Color(35, 35, 38));
        btnAddCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancel-16.png"))); // NOI18N

        javax.swing.GroupLayout panelBudgetLayout = new javax.swing.GroupLayout(panelBudget);
        panelBudget.setLayout(panelBudgetLayout);
        panelBudgetLayout.setHorizontalGroup(
            panelBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBudgetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                    .addGroup(panelBudgetLayout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddOp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboAddOp, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirmOp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteOp)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelBudgetLayout.setVerticalGroup(
            panelBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBudgetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboAddOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddOp)
                    .addComponent(btnConfirmOp)
                    .addComponent(btnDeleteOp)
                    .addComponent(btnAddCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelBudgetLayout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel50))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_address)
                            .addComponent(lbl_id_customer)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel49))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_address)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_id_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelProduct.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(12, 83, 151));
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product16.png"))); // NOI18N
        jLabel32.setText("Producto");

        btnAddProduct.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnAddProduct.setForeground(new java.awt.Color(35, 35, 38));
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add16.png"))); // NOI18N

        btnEditProduct.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnEditProduct.setForeground(new java.awt.Color(35, 35, 38));
        btnEditProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit16.png"))); // NOI18N

        btnDeleteProduct.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnDeleteProduct.setForeground(new java.awt.Color(35, 35, 38));
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin16.png"))); // NOI18N

        tableProducts.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableProducts);

        jPanel13.setBackground(new java.awt.Color(245, 248, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel33.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(12, 83, 151));
        jLabel33.setText("Total de productos:");

        lblTotalProducts.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblTotalProducts.setForeground(new java.awt.Color(12, 83, 151));
        lblTotalProducts.setText("$0,00");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelProductLayout = new javax.swing.GroupLayout(panelProduct);
        panelProduct.setLayout(panelProductLayout);
        panelProductLayout.setHorizontalGroup(
            panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProductLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteProduct)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelProductLayout.setVerticalGroup(
            panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductLayout.createSequentialGroup()
                .addGroup(panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(btnDeleteProduct)
                        .addComponent(btnEditProduct)
                        .addComponent(btnAddProduct)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelService.setBackground(new java.awt.Color(255, 255, 255));

        jLabel35.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(12, 83, 151));
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/spanner16.png"))); // NOI18N
        jLabel35.setText("Servicios Técnicos");

        jPanel14.setBackground(new java.awt.Color(245, 248, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel36.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(12, 83, 151));
        jLabel36.setText("Total de servicios técnicos:");

        lblTotalServices.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblTotalServices.setForeground(new java.awt.Color(12, 83, 151));
        lblTotalServices.setText("$0,00");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalServices, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalServices, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        tableServices.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableServices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(tableServices);

        javax.swing.GroupLayout panelServiceLayout = new javax.swing.GroupLayout(panelService);
        panelService.setLayout(panelServiceLayout);
        panelServiceLayout.setHorizontalGroup(
            panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelServiceLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelServiceLayout.setVerticalGroup(
            panelServiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServiceLayout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelSeparador3.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanelSeparador3Layout = new javax.swing.GroupLayout(jPanelSeparador3);
        jPanelSeparador3.setLayout(jPanelSeparador3Layout);
        jPanelSeparador3Layout.setHorizontalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador3Layout.setVerticalGroup(
            jPanelSeparador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addComponent(jPanelSeparador3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelSeparador3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescuentoActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCancel;
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnAddMetPago;
    private javax.swing.JButton btnAddOp;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnConfirmDiscount;
    private javax.swing.JButton btnConfirmOp;
    private javax.swing.JButton btnDeleteOp;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnEditProduct;
    private javax.swing.JButton btnSearchCustomer;
    private javax.swing.JComboBox<String> cboAddOp;
    private javax.swing.JComboBox<String> cboTypeDesc;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelSeparador3;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel lblTotalProducts;
    private javax.swing.JLabel lblTotalServices;
    private javax.swing.JLabel lbl_Saldo;
    private javax.swing.JLabel lbl_address;
    private javax.swing.JLabel lbl_id_customer;
    private javax.swing.JLabel lbl_iva_105;
    private javax.swing.JLabel lbl_iva_21;
    private javax.swing.JLabel lbl_subtProd;
    private javax.swing.JLabel lbl_subtServ;
    private javax.swing.JLabel lbl_totalFinal;
    private javax.swing.JLabel lbl_totalGeneral;
    private javax.swing.JLabel lbl_totalPagos;
    private javax.swing.JPanel panelBudget;
    private javax.swing.JPanel panelProduct;
    private javax.swing.JPanel panelService;
    private javax.swing.JTable tablaPagos;
    private javax.swing.JTable tableOperations;
    private javax.swing.JTable tableProducts;
    private javax.swing.JTable tableServices;
    private javax.swing.JTextArea textAreaObservation;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
