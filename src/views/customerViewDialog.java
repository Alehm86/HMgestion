/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import models.Customer;
import dao.genericDAO;
import dao.customerDAO;
import dao.serviceDAO;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import utils.utility;

public class customerViewDialog extends javax.swing.JDialog {

    genericDAO qGeneric = new genericDAO();
    customerDAO qCustomer = new customerDAO();
    serviceDAO qService = new serviceDAO();
    
    utility utils = new utility();
    
    Customer mClient = new Customer();
    
    String fecha = utils.fecha();
    
    private String cuitFound;
    private boolean msjEstado = false;
    private int id_customer;
    
    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    public void dialogoEdit(String cuit){    
        this.cuitFound = cuit;               
        if(cuit != null && !cuitFound.isEmpty()){
            cargarCliente();
        }   
    }
    
    public boolean dialogoClienteActualizado(){
        return msjEstado;
    } 
    
    public customerViewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        txtIVA.setEnabled(false);
        txtIVA.setVisible(false);
        
        txtProvince.setEnabled(false);
        txtProvince.setVisible(false);

        btnRegistrar.setEnabled(false);
        btnCancel.setEnabled(false);          
        
        inicializar();
        startMsjs();
        llenarCombos();
        actions();  
        leyendaBotones();
        
    }
    
    private void leyendaBotones(){
        
        btnEditClient.setToolTipText("Editar");
        btnHistory.setToolTipText("Historial");
        btnServices.setToolTipText("Servicios");
        btnDevices.setToolTipText("Dispositivos");
        btnBaja.setToolTipText("baja");
        btnSuspender.setToolTipText("Suspender");
        btnReactivar.setToolTipText("Activar");
    }

    public void cargarCliente(){
        
        qCustomer.selectCustomerEdit(cuitFound, txtID, txtName, txtCUIT, txtTel, txtEmail, txtStreet, txtHeight, txtCity, txtIVA, txtProvince,txtState);
        infoCombo(); 
        verificarEstado();

        String idText = txtID.getText().trim();
        id_customer = Integer.parseInt(idText);

    }
    
    public void inicializar(){
        btnSuspender.setEnabled(false);
        btnBaja.setEnabled(false);
        btnReactivar.setEnabled(false);
        
        verificarEstado();
    }   
    
    public void verificarEstado(){
        String opcion = txtState.getText(); 
       
        switch(opcion){
            case "Activo":
                txtState.setForeground(new Color(0, 153, 51));
                btnSuspender.setEnabled(true);
                btnBaja.setEnabled(true);                
                break;
               
            case "Dado de Baja":
                txtState.setForeground(new Color(255, 0, 0));
                btnReactivar.setEnabled(true);            
                break;   
                
            case "Suspendido":
                txtState.setForeground(new Color(255, 102, 0));
                btnReactivar.setEnabled(true);            
                break;                 
       }
    }    
    
    public void infoCombo(){
        String iva = txtIVA.getText().trim();
        String province = txtProvince.getText().trim();

        for (int i = 0; i < cboIVA.getItemCount(); i++) {
            if (cboIVA.getItemAt(i).toString().equalsIgnoreCase(iva)) {
                cboIVA.setSelectedIndex(i);
                break;
            }
        } 
        for (int i = 0; i < cboProvinces.getItemCount(); i++) {
            if (cboProvinces.getItemAt(i).toString().equalsIgnoreCase(province)) {
                cboProvinces.setSelectedIndex(i);
                break;
            }
        }         
    }   
    
    private void clear(){
        cboIVA.setSelectedIndex(0);
        cboProvinces.setSelectedIndex(0);
        txtName.setText("");
        txtCUIT.setText("");
        txtTel.setText("");
        txtEmail.setText("");
        txtStreet.setText("");
        txtHeight.setText("");
        txtCity.setText("");
    }
    
    private void startMsjs(){
        lblErrorName.setText("");
        lblErrorTel.setText("");
        lblErrorEmail.setText("");
    }

    private void llenarCombos(){       
        qGeneric.llenarCombos(cboIVA, "customer_iva");
        qGeneric.llenarCombosActivos(cboProvinces, "provinces");
    }
    
    private void offObjets(){
        cboIVA.setEnabled(false);
        txtName.setEnabled(false);
        txtCUIT.setEnabled(false);
        txtTel.setEnabled(false);
        txtEmail.setEnabled(false);
        txtStreet.setEnabled(false);
        txtHeight.setEnabled(false);
        txtCity.setEnabled(false);
        cboProvinces.setEnabled(false);
    }    
    
    private void onObjets(){
        cboIVA.setEnabled(true);
        txtName.setEnabled(true);
        txtCUIT.setEnabled(true);
        txtTel.setEnabled(true);
        txtEmail.setEnabled(true);
        txtStreet.setEnabled(true);
        txtHeight.setEnabled(true);
        txtCity.setEnabled(true);
        cboProvinces.setEnabled(true);
    }     
    
    private void actions(){       
        
        btnEditClient.addActionListener(e->{
            btnRegistrar.setEnabled(true);
            btnCancel.setEnabled(true);          
        });
        
        btnBaja.addActionListener(e->{
                 
            String title = "Dar de baja";        
            
            customerCambioEstadoDialog dCambioEstado = new customerCambioEstadoDialog(parent, true);
            
            dCambioEstado.dialogoId_client(id_customer);
            dCambioEstado.dialogoGetTitle(title);
            
            dCambioEstado.setVisible(true);            
            
            msjEstado = dCambioEstado.dialogoEstadoActualizado();
            
            if (msjEstado){
                clear();
                cargarCliente();
                inicializar();
            }
     
        }); 
        
        btnSuspender.addActionListener(e->{                     
            
            String title = "Suspender";
            
            customerCambioEstadoDialog dCambioEstado = new customerCambioEstadoDialog(parent, true);           
            
            dCambioEstado.dialogoId_client(id_customer);
            dCambioEstado.dialogoGetTitle(title);
            
            dCambioEstado.setVisible(true);
            
            msjEstado = dCambioEstado.dialogoEstadoActualizado();
            
            if (msjEstado){
                clear();
                cargarCliente();
                inicializar();
            }
 
        }); 
        
        btnReactivar.addActionListener(e->{

            int state = 1;
            
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Confirma la acción sobre el cliente?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            qCustomer.updateState(id_customer, state);
            qCustomer.insertCustomerHistory(id_customer, "Activacion de cliente", "S/D");
            clear();
            cargarCliente();
            inicializar();
            msjEstado = true;
            
        });
        
        btnHistory.addActionListener(e-> {
                   
            viewHistoryDialog fHistory = new viewHistoryDialog(parent, true);
                        
            fHistory.dialogoId_client(id_customer);
            fHistory.setVisible(true);
            
        });
        
        btnServices.addActionListener(e->{
            
            customerServiceHistoryDialog fServicesHistory = new customerServiceHistoryDialog(parent, true);
            boolean status = fServicesHistory.getServices(Integer.parseInt(txtID.getText().trim()));
            fServicesHistory.setVisible(true);
            
            if(!status){
                JOptionPane.showMessageDialog(null, "No se encontraron servicios técnicos.");
            }
            
        });
        
        utils.clearMsjErrorTxt(txtName, lblErrorName);
        utils.clearMsjErrorTxt(txtTel, lblErrorTel);         
        
        btnCancel.addActionListener(e-> {
            btnRegistrar.setEnabled(false);
            btnCancel.setEnabled(false);             
        });
        
        btnRegistrar.addActionListener(e-> {           
            updateCustomer();
            msjEstado=true;
        });
        
        utils.clearMsjErrorTxt(txtName, lblErrorName);
        utils.clearMsjErrorTxt(txtTel, lblErrorTel);

        //OBLIGA A VALIDAR MAIL CON '@'       
        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String email = txtEmail.getText();

                if (!email.contains("@")) {
                    lblErrorEmail.setText("El correo debe contener '@'");
                }else
                    lblErrorEmail.setText("");
            }
        });  
        
        //VALIDA QUE TENGA AL MENOS 8 CARACTERES
        txtCUIT.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = txtCUIT.getText();

                if (!texto.isEmpty() && texto.length() < 8) {
                    JOptionPane.showMessageDialog(
                        null,
                        "El DNI / CUIT debe tener al menos 8 dígitos",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });               
        
        txtTel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = txtTel.getText();

                if (texto.length() >= 10) {
                    lblErrorTel.setText("");   
                }
            }
        }); 
        
        txtName.addActionListener(e->{
            lblErrorName.setText("");
        });
        
        btnDevices.addActionListener(e->{
            JTable tabla = new JTable();
            boolean dato = false;
                     
            customerDevicesDialog pCustDev = new customerDevicesDialog(parent, true);         
                          
            int idClient = Integer.parseInt(txtID.getText().trim());
            dato = dato = qCustomer.listCustomerDevices(tabla, idClient);

            if(dato){
                pCustDev.setIdCustomer(idClient);
                pCustDev.setVisible(true); 
            }else{
                JOptionPane.showMessageDialog(null, "No hay dispositivos vinculados");
            }
                          
            
        });
        
    }   
    
    private void updateCustomer(){
        String msjError ="Complete el campo!";
        
        boolean valido = true;
        
        String cuitText = txtCUIT.getText().trim();
        mClient.state=1;
        mClient.fechaRegistro=fecha;
        mClient.ID = Integer.parseInt(txtID.getText().trim());
        
        if (cboIVA.getSelectedIndex() > 0) { 
            mClient.iva = qGeneric.selectId(
                    "id_iva",
                    "iva",
                    cboIVA.getSelectedItem().toString()
            );
        } else {
            valido = false;
        }

        if(!txtName.getText().isEmpty()){           
            if (txtName.getText().trim().length() < 5) {
                lblErrorName.setText("El nombre debe contener al menos 5 caracteres.");
            } else {
                mClient.name = txtName.getText().trim().toUpperCase();
            }              
        } else {
            lblErrorName.setText(msjError);
            valido = false;
        }       
        
        if(!txtEmail.getText().isEmpty()){
            mClient.email = txtEmail.getText();
        } else {
            mClient.email="S/D";
        } 

        if(!txtTel.getText().isEmpty()){
            if (txtTel.getText().length() < 10) {
                lblErrorTel.setText("El número de telefono debe tener al menos 10 numeros");
            }else{
                mClient.phone = txtTel.getText().trim();
            }        
        } else {
            lblErrorTel.setText(msjError);
            valido = false;
        }  

        if(!cuitText.isEmpty()){
//      VALIDA QUE EL DNI/CUIT NO SE REPITA          
            if (qCustomer.existsByCuit(cuitText)) {
                JOptionPane.showMessageDialog(null,
                    "El DNI/CUIT corresponde a otro cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }else{
                mClient.CUIT= cuitText;  
            }
        } else {
            valido = false;
        } 
        
        if(!txtStreet.getText().isEmpty()){
            mClient.street = txtStreet.getText().trim().toUpperCase();
        } else {
            mClient.street = "S/D";
        }          

        if(!txtHeight.getText().isEmpty()){
            mClient.height = Integer.parseInt(txtHeight.getText());
        } else {
            mClient.height = 0;
        }  

        if(!txtCity.getText().isEmpty()){
            mClient.city = txtCity.getText().trim().toUpperCase();
        } else {
            mClient.city = "S/D";
        } 

        if (cboProvinces.getSelectedIndex() > 0) { 
            mClient.idProvince = qGeneric.selectId(
                    "id_province",
                    "provinces",
                    cboProvinces.getSelectedItem().toString()
            );
        } else {
            mClient.idProvince=25;
        }                 
        
        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro del Cliente?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }
                   
        qCustomer.updateCustomer(
                mClient.getID(),        
                mClient.getName(),
                mClient.getIva(),
                mClient.getCUIT(),
                mClient.getEmail(),
                mClient.getPhone(),
                mClient.getStreet(),
                mClient.getHeight(),
                mClient.getCity(),
                mClient.getIdProvince()  
        );        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblErrorTel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblErrorEmail = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblErrorName = new javax.swing.JLabel();
        txtIVA = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        cboIVA = new javax.swing.JComboBox<>();
        txtCUIT = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtProvince = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtStreet = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        cboProvinces = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        btnEditClient = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        btnServices = new javax.swing.JButton();
        btnDevices = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();
        btnSuspender = new javax.swing.JButton();
        btnReactivar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Editar cliente");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("DNI / CUIT");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Teléfono");

        lblErrorTel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorTel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorTel.setText("mensaje error!");
        lblErrorTel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Email");

        lblErrorEmail.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorEmail.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorEmail.setText("mensaje error!");
        lblErrorEmail.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Tipo de cliente");

        lblErrorName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorName.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorName.setText("mensaje error!");
        lblErrorName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        txtIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtIVA.setForeground(new java.awt.Color(35, 35, 38));
        txtIVA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        txtCUIT.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCUIT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCUITKeyTyped(evt);
            }
        });

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });

        txtEmail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(12, 83, 151));
        jLabel7.setText("Id:");

        txtID.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txtID.setForeground(new java.awt.Color(35, 35, 38));
        txtID.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setText("Estado: ");

        txtState.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txtState.setForeground(new java.awt.Color(35, 35, 38));
        txtState.setBorder(null);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtState)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtTel)
                            .addComponent(txtCUIT, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboIVA, javax.swing.GroupLayout.Alignment.LEADING, 0, 285, Short.MAX_VALUE)
                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblErrorName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCUIT, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblErrorName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Editar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(12, 83, 151)), "Dirección", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(12, 83, 151))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Provincia");

        txtProvince.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProvince.setForeground(new java.awt.Color(35, 35, 38));
        txtProvince.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        txtStreet.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtStreet.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtStreet.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 12))); // NOI18N

        txtHeight.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtHeight.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtHeight.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Altura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 12))); // NOI18N
        txtHeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHeightKeyTyped(evt);
            }
        });

        txtCity.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCity.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCity.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ciudad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 12))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cboProvinces.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnEditClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar-32.png"))); // NOI18N

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N

        btnServices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeService32.png"))); // NOI18N

        btnDevices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/device32_1.png"))); // NOI18N

        btnBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/delClient32.png"))); // NOI18N

        btnSuspender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pause32.png"))); // NOI18N

        btnReactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/switch32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEditClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDevices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuspender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuspender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDevices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(245, 248, 255));

        icono.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(12, 83, 151));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cliente128.png"))); // NOI18N
        icono.setText("Datos del cliente.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icono, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(icono)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCUITKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCUITKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtCUIT.getText().length() >= 11) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCUITKeyTyped

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtTel.getText().length() >= 13) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelKeyTyped

    private void txtHeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHeightKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) || txtHeight.getText().length() >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtHeightKeyTyped

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                customerViewDialog dialog = new customerViewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDevices;
    private javax.swing.JButton btnEditClient;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnReactivar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnServices;
    private javax.swing.JButton btnSuspender;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboProvinces;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblErrorEmail;
    private javax.swing.JLabel lblErrorName;
    private javax.swing.JLabel lblErrorTel;
    private javax.swing.JTextField txtCUIT;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtIVA;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtProvince;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtStreet;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
}
