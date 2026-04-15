/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import dao.budgetDAO;
import dao.genericDAO;
import dao.serviceDAO;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import models.modelService;

public class serviceViewPanel extends javax.swing.JDialog {

    genericDAO queriesGeneric = new genericDAO();
    serviceDAO queriesServices = new serviceDAO();
    budgetDAO queriesBudget = new budgetDAO();
    
    modelService mService = new modelService();
    
    private boolean msjEstado = false;
    
    String serviceNumber ="";
    int id_status = -1;
    int id_service = -1;
    int id_budget = -1;
    
    Map<String, Integer> estadosMap = new HashMap<>();
    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);   
    
    public void setService(String serviceNumber){
        
        this.serviceNumber = serviceNumber;

        if(serviceNumber != null && !serviceNumber.isEmpty()){            
            cargarServicio();      
        }
    }
    
    public boolean dialogoServiceActualizado(){
        return msjEstado;
    }    
    
    public serviceViewPanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        llenarCombo();
        actions();
        leyendaBotones();
    }
    
    private void leyendaBotones(){
        btnPresupuesto.setToolTipText("Presupuestar");
        btnViewBudget.setToolTipText("Ver presupuesto");
        btnCancelBudget.setToolTipText("Cancelar presupuesto");
        btnServiceOrder.setToolTipText("Imprimir orden de servicio técnico");
    }
    
    private void inicializar(){
        
        lbl_id_service.setText("");
        lbl_id_service.setVisible(false);
        
        lbl_serviceNumber.setText("");
        lbl_date.setText("");
        lbl_name.setText("");
        lbl_phone.setText("");
        lbl_deviceType.setText("");
        lbl_brand.setText("");
        lbl_Model.setText("");
        lbl_sn.setText("");
        lbl_state.setText("");
        jTextAreaDescription.setText("");
        jTextAreaDescription.setEditable(false);
        
        txtCost.setText("");
        
        jTextAreaFalla.setText("");
        jTextAreaFalla.setEditable(false);
        
        btnChangeState.setEnabled(false);    
        
        lbl_CostoDeRep.setEnabled(false);
        txtCost.setEditable(false);   
        
        btnPresupuesto.setVisible(false);
        btnViewBudget.setVisible(false);
        btnCancelBudget.setVisible(false);
        
    }
       
    private void llenarCombo(){

        cboStates.removeAllItems();
        estadosMap.clear();

        cboStates.addItem("Seleccionar estado");

        cboStates.addItem("Diagnosticado");
        estadosMap.put("Diagnosticado", 2);

        cboStates.addItem("Esperando aprobación");
        estadosMap.put("Esperando aprobación", 3);
        
        cboStates.addItem("Presupuesto aprobado");
        estadosMap.put("Presupuesto aprobado", 4);

        cboStates.addItem("No reparado");
        estadosMap.put("No reparado", 5);

        cboStates.addItem("Reparado");
        estadosMap.put("Reparado", 6);
    }
    
    private void existsBudget(){
        
        if(queriesBudget.existsBudget(id_service)){
            
            btnPresupuesto.setVisible(false);
            btnViewBudget.setVisible(true);
            btnCancelBudget.setVisible(true);
            llenarTabla();
        }else{
            btnPresupuesto.setVisible(true);
            btnViewBudget.setVisible(false);
            btnCancelBudget.setVisible(false);
        }
    }
    
    private void actions(){
        
        btnChangeState.addActionListener(e->{
            updateServiceState();
            cargarServicio();
        });
        
        cboStates.addActionListener(e -> {

            String selected = (String) cboStates.getSelectedItem();

            if (selected != null && estadosMap.containsKey(selected)) {

                id_status = estadosMap.get(selected);

                btnChangeState.setEnabled(true);
                lbl_diagnostico.setEnabled(true);
                jTextAreaDiagnostico.setEnabled(true);
                lbl_CostoDeRep.setEnabled(true);             

            } else {

                btnChangeState.setEnabled(false);
                lbl_diagnostico.setEnabled(false);
                jTextAreaDiagnostico.setEnabled(false);
                lbl_CostoDeRep.setEnabled(false);
            }
        });
        
        btnCancel.addActionListener(e->{
            this.dispose();
        });
        
        txtCost.addActionListener(e -> {

            String valor = txtCost.getText().trim();

            if (valor.isEmpty()) return;
            valor = valor.replace("$", "").trim();

            if (!valor.contains(",")) {
                valor = valor + ",00";
            }
            txtCost.setText("$ " + valor);
        });
        
        btnPresupuesto.addActionListener(e -> {

            String name = lbl_name.getText().trim();
            String phone = lbl_phone.getText().trim();
            
            this.setVisible(false);

            budgetDialog fNewBudget = new budgetDialog(null, true);
            
            fNewBudget.dialogoId_service(id_service);
            fNewBudget.dialogoIdCustomer(name, phone);
            
            fNewBudget.setLocationRelativeTo(null);
            fNewBudget.setVisible(true);
            
            cargarServicio();
            btnPresupuesto.setVisible(false);
            btnViewBudget.setVisible(true);
            llenarTabla();
            
            this.setVisible(true);   

        });
        
        btnViewBudget.addActionListener(e->{
            
            this.setVisible(false);
            
            budgetPrintDialog fViewBudget = new budgetPrintDialog(null, true);
            
            fViewBudget.dialogoIdBudget(id_budget);
            
            fViewBudget.setLocationRelativeTo(null);
            fViewBudget.setVisible(true);
            
            this.setVisible(true);
            
        });         
        
        btnCancelBudget.addActionListener(e->{
            
            boolean cancelado = false;
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de anular el presupuesto?\n\n" +
                "Esta acción no se puede deshacer.",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            cancelado = queriesBudget.cancelBudget(id_service);
            
            if(cancelado){
               cargarServicio();
                btnPresupuesto.setVisible(true);
                btnViewBudget.setVisible(false);
                btnCancelBudget.setVisible(false);
                
                DefaultTableModel dtm = (DefaultTableModel) jTableItems.getModel();
                dtm.setRowCount(0);
            }            
        });     
        
        btnServiceOrder.addActionListener(e->{
        
            servicePrintDialog pPrintServiceOrder = new servicePrintDialog(parent, true);
            pPrintServiceOrder.dialogoId_service(id_service);
            pPrintServiceOrder.setVisible(true);
        });
    }
    
    public void llenarTabla(){
        
        queriesBudget.listBudgetInService(id_budget, jTableItems);
        calcularTotales(); 
        
    }
    
    public double obtenerValorNumerico(String texto){

        if (texto == null || texto.trim().isEmpty()) {
            return 0;
        }

        texto = texto.replace("$", "")
                     .replace(" ", "")
                     .replace(".", "")  
                     .replace(",", "."); 

        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private void cargarServicio(){
        inicializar();
        
        queriesServices.selectService(
                serviceNumber, 
                lbl_state, 
                lbl_id_service,
                lbl_serviceNumber, 
                lbl_date, 
                lbl_name, 
                lbl_phone, 
                lbl_deviceType, 
                lbl_brand, 
                lbl_Model, 
                lbl_sn, 
                jTextAreaDescription, 
                jTextAreaFalla,
                jTextAreaDiagnostico,
                txtCost
        );

        id_service = Integer.parseInt(lbl_id_service.getText().trim());
        id_budget = queriesBudget.selectIdBudget(id_service);
        
        if(!lbl_state.getText().equals("Ingresado")){
            btnPresupuesto.setVisible(true);
        }
        
        existsBudget();
        
        String estado = lbl_state.getText().trim();
        removeItemByText(cboStates,estado);
        
    }
    
    private void updateServiceState(){
        
        boolean valido = true;
               
        Double cost = null;
        String tCosto = txtCost.getText().trim();
        cost = obtenerValorNumerico(tCosto);
        
        if(!txtCost.getText().isEmpty()){
            mService.setFinal_cost(cost);           
        }else{
            mService.setFinal_cost(null);
        }  
        
        mService.setService_number(lbl_serviceNumber.getText());
        mService.setId_status(id_status);
        
        String tDiagnosis = jTextAreaDiagnostico.getText().toUpperCase();
        if(!jTextAreaDiagnostico.getText().isEmpty()){
            mService.setDiagnosis(tDiagnosis);           
        }else{
            JOptionPane.showMessageDialog(null, "Complete el diagnositco.");
            valido = false;          
        }

        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma la edición?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }  

        msjEstado = queriesServices.updateService(
                serviceNumber, 
                id_status, 
                mService.getDiagnosis(), 
                mService.getFinal_cost()
        );
        
        if(msjEstado){
            
            int id_service = Integer.parseInt(lbl_id_service.getText().trim());
            queriesServices.insertService_order_status_history(id_service, id_status);
        }         
        
    }
    
    public void removeItemByText(JComboBox combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).toString().equals(texto)) {
                combo.removeItemAt(i);
                break;
            }
        }
    }

    public void calcularTotales(){

        double total = 0;

        for (int i = 0; i < jTableItems.getRowCount(); i++) {
            try {
                double precio = Double.parseDouble(jTableItems.getValueAt(i, 4).toString());
            
                total = precio + total;
            } catch (Exception e) {
                System.out.println("Error en fila " + i + ": " + e.getMessage());
            }
        }
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        txtCost.setText(formato.format(total));

    }
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cboStates = new javax.swing.JComboBox<>();
        btnPresupuesto = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        btnViewBudget = new javax.swing.JButton();
        btnCancelBudget = new javax.swing.JButton();
        btnServiceOrder = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl_name = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_phone = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lbl_deviceType = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbl_brand = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbl_Model = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_sn = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbl_serviceNumber = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_state = new javax.swing.JLabel();
        lbl_id_service = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDiagnostico = new javax.swing.JTextArea();
        lbl_diagnostico = new javax.swing.JLabel();
        btnChangeState = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaFalla = new javax.swing.JTextArea();
        lbl_diagnostico1 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        lbl_CostoDeRep = new javax.swing.JLabel();
        txtCost = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableItems = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        cboStates.setBackground(new java.awt.Color(255, 255, 255));
        cboStates.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboStates.setBorder(null);
        cboStates.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnPresupuesto.setBackground(new java.awt.Color(255, 255, 255));
        btnPresupuesto.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnPresupuesto.setForeground(new java.awt.Color(12, 83, 151));
        btnPresupuesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/BudgetNew32.png"))); // NOI18N
        btnPresupuesto.setBorder(null);
        btnPresupuesto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPresupuesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPresupuestoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPresupuestoMouseExited(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(12, 83, 151));
        jLabel9.setText("Cambiar estado a:");

        btnViewBudget.setBackground(new java.awt.Color(255, 255, 255));
        btnViewBudget.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnViewBudget.setForeground(new java.awt.Color(12, 83, 151));
        btnViewBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/budget32.png"))); // NOI18N
        btnViewBudget.setBorder(null);
        btnViewBudget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewBudgetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewBudgetMouseExited(evt);
            }
        });

        btnCancelBudget.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelBudget.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCancelBudget.setForeground(new java.awt.Color(12, 83, 151));
        btnCancelBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelBudget32.png"))); // NOI18N
        btnCancelBudget.setBorder(null);
        btnCancelBudget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelBudgetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelBudgetMouseExited(evt);
            }
        });

        btnServiceOrder.setBackground(new java.awt.Color(255, 255, 255));
        btnServiceOrder.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnServiceOrder.setForeground(new java.awt.Color(12, 83, 151));
        btnServiceOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/serviceOrder32.png"))); // NOI18N
        btnServiceOrder.setBorder(null);
        btnServiceOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServiceOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnServiceOrderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnServiceOrderMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(btnPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Datos del cliente:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 18), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(65, 65, 63));
        jLabel1.setText("Nombre:");

        lbl_name.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_name.setForeground(new java.awt.Color(65, 65, 63));
        lbl_name.setText("xxx");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(65, 65, 63));
        jLabel2.setText("Teléfono:");

        lbl_phone.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_phone.setForeground(new java.awt.Color(65, 65, 63));
        lbl_phone.setText("xxx");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addComponent(lbl_name, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(lbl_phone))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lbl_name)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Datos del dispositivo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 18), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(65, 65, 63));
        jLabel3.setText("Tipo de dispositivo:");

        lbl_deviceType.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_deviceType.setForeground(new java.awt.Color(65, 65, 63));
        lbl_deviceType.setText("xxx");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(65, 65, 63));
        jLabel4.setText("Marca:");

        lbl_brand.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_brand.setForeground(new java.awt.Color(65, 65, 63));
        lbl_brand.setText("xxx");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(65, 65, 63));
        jLabel5.setText("Modelo:");

        lbl_Model.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_Model.setForeground(new java.awt.Color(65, 65, 63));
        lbl_Model.setText("xxx");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(65, 65, 63));
        jLabel6.setText("Nº de serie:");

        lbl_sn.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_sn.setForeground(new java.awt.Color(65, 65, 63));
        lbl_sn.setText("xxx");

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(65, 65, 63));
        jLabel11.setText("Descripción:");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextAreaDescription.setEditable(false);
        jTextAreaDescription.setBackground(new java.awt.Color(255, 255, 255));
        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextAreaDescription.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(11, 11, 11)
                                        .addComponent(lbl_deviceType, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(11, 11, 11)
                                        .addComponent(lbl_sn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(11, 11, 11)
                                .addComponent(lbl_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addGap(11, 11, 11)
                                .addComponent(lbl_Model, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbl_deviceType)
                    .addComponent(jLabel4)
                    .addComponent(lbl_brand)
                    .addComponent(jLabel5)
                    .addComponent(lbl_Model))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lbl_sn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Datos del servicio:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 18), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(65, 65, 63));
        jLabel7.setText("Fecha:");

        lbl_date.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(65, 65, 63));
        lbl_date.setText("xxx");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(65, 65, 63));
        jLabel8.setText("Nº de servicio:");

        lbl_serviceNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_serviceNumber.setForeground(new java.awt.Color(65, 65, 63));
        lbl_serviceNumber.setText("xxx");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(65, 65, 63));
        jLabel10.setText("Estado:");

        lbl_state.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_state.setForeground(new java.awt.Color(65, 65, 63));
        lbl_state.setText("xxx");

        lbl_id_service.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_id_service.setForeground(new java.awt.Color(65, 65, 63));
        lbl_id_service.setText("id");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_state, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_serviceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jLabel7)
                .addGap(11, 11, 11)
                .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lbl_id_service, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(lbl_serviceNumber))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(lbl_date)
                        .addComponent(lbl_id_service))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(lbl_state)))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextAreaDiagnostico.setColumns(20);
        jTextAreaDiagnostico.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextAreaDiagnostico.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaDiagnostico.setRows(5);
        jTextAreaDiagnostico.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jScrollPane2.setViewportView(jTextAreaDiagnostico);

        lbl_diagnostico.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_diagnostico.setForeground(new java.awt.Color(101, 129, 171));
        lbl_diagnostico.setText("Diagnostico:");

        btnChangeState.setBackground(new java.awt.Color(255, 255, 255));
        btnChangeState.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnChangeState.setForeground(new java.awt.Color(12, 83, 151));
        btnChangeState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/state32.png"))); // NOI18N
        btnChangeState.setText("Confirmar");
        btnChangeState.setBorder(null);
        btnChangeState.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnChangeStateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnChangeStateMouseExited(evt);
            }
        });

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextAreaFalla.setColumns(20);
        jTextAreaFalla.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextAreaFalla.setForeground(new java.awt.Color(65, 65, 63));
        jTextAreaFalla.setRows(5);
        jTextAreaFalla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jScrollPane3.setViewportView(jTextAreaFalla);

        lbl_diagnostico1.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_diagnostico1.setForeground(new java.awt.Color(101, 129, 171));
        lbl_diagnostico1.setText("Falla:");

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
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

        lbl_CostoDeRep.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_CostoDeRep.setForeground(new java.awt.Color(101, 129, 171));
        lbl_CostoDeRep.setText("Costo de reparación:");

        txtCost.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        txtCost.setForeground(new java.awt.Color(65, 65, 63));
        txtCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostKeyTyped(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(0, 204, 204));

        icono.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(102, 102, 102));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/soporte-tecnico128.png"))); // NOI18N
        icono.setText("Servicio técnico.");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icono)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(icono)
                .addGap(5, 5, 5))
        );

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTableItems.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTableItems.setForeground(new java.awt.Color(65, 65, 63));
        jTableItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableItems.setFillsViewportHeight(true);
        jTableItems.setRowHeight(25);
        jScrollPane4.setViewportView(jTableItems);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_diagnostico1)
                            .addComponent(lbl_diagnostico))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_CostoDeRep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChangeState, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_diagnostico1)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_diagnostico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_CostoDeRep)
                        .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnChangeState, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(299, 299, 299))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangeStateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateMouseEntered
        btnChangeState.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnChangeStateMouseEntered

    private void btnChangeStateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateMouseExited
        btnChangeState.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnChangeStateMouseExited

    private void btnPresupuestoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPresupuestoMouseEntered
        btnPresupuesto.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnPresupuestoMouseEntered

    private void btnPresupuestoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPresupuestoMouseExited
        btnPresupuesto.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnPresupuestoMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void txtCostKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyTyped
        txtCost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCost.getText().length() >= 10) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCostKeyTyped

    private void txtCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyPressed

    }//GEN-LAST:event_txtCostKeyPressed

    private void btnViewBudgetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewBudgetMouseEntered
        btnViewBudget.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnViewBudgetMouseEntered

    private void btnViewBudgetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewBudgetMouseExited
        btnViewBudget.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnViewBudgetMouseExited

    private void btnCancelBudgetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelBudgetMouseEntered
        btnCancelBudget.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelBudgetMouseEntered

    private void btnCancelBudgetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelBudgetMouseExited
        btnCancelBudget.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelBudgetMouseExited

    private void btnServiceOrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceOrderMouseEntered
        btnServiceOrder.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnServiceOrderMouseEntered

    private void btnServiceOrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceOrderMouseExited
        btnServiceOrder.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnServiceOrderMouseExited


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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(serviceViewPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(serviceViewPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(serviceViewPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(serviceViewPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                serviceViewPanel dialog = new serviceViewPanel(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelBudget;
    private javax.swing.JButton btnChangeState;
    private javax.swing.JButton btnPresupuesto;
    private javax.swing.JButton btnServiceOrder;
    private javax.swing.JButton btnViewBudget;
    private javax.swing.JComboBox<String> cboStates;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableItems;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextArea jTextAreaDiagnostico;
    private javax.swing.JTextArea jTextAreaFalla;
    private javax.swing.JLabel lbl_CostoDeRep;
    private javax.swing.JLabel lbl_Model;
    private javax.swing.JLabel lbl_brand;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_deviceType;
    private javax.swing.JLabel lbl_diagnostico;
    private javax.swing.JLabel lbl_diagnostico1;
    private javax.swing.JLabel lbl_id_service;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_phone;
    private javax.swing.JLabel lbl_serviceNumber;
    private javax.swing.JLabel lbl_sn;
    private javax.swing.JLabel lbl_state;
    private javax.swing.JTextField txtCost;
    // End of variables declaration//GEN-END:variables
}
