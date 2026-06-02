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
        lblErrorDni.setText("");
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
        utils.clearMsjErrorTxt(txtCUIT, lblErrorDni);
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
        utils.clearMsjErrorTxt(txtCUIT, lblErrorDni);
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
            lblErrorDni.setText(msjError);
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
        txtName = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        txtCUIT = new javax.swing.JTextField();
        lblErrorDni = new javax.swing.JLabel();
        lblErrorTel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblErrorEmail = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        lblErrorName = new javax.swing.JLabel();
        txtIVA = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtStreet = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        cboProvinces = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtProvince = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        btnEditClient = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();
        btnSuspender = new javax.swing.JButton();
        btnReactivar = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        btnServices = new javax.swing.JButton();
        btnDevices = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setForeground(new java.awt.Color(35, 35, 38));
        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setForeground(new java.awt.Color(35, 35, 38));
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelKeyPressed(evt);
            }
        });

        txtCUIT.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCUIT.setForeground(new java.awt.Color(35, 35, 38));
        txtCUIT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCUIT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtCUIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCUITActionPerformed(evt);
            }
        });
        txtCUIT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCUITKeyPressed(evt);
            }
        });

        lblErrorDni.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorDni.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorDni.setText("mensaje error!");
        lblErrorDni.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblErrorTel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorTel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorTel.setText("mensaje error!");
        lblErrorTel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Email");

        txtEmail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(35, 35, 38));
        txtEmail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblErrorEmail.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorEmail.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorEmail.setText("mensaje error!");
        lblErrorEmail.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Tipo de cliente");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(35, 35, 38));
        cboIVA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lblErrorName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorName.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorName.setText("mensaje error!");
        lblErrorName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        txtIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtIVA.setForeground(new java.awt.Color(35, 35, 38));
        txtIVA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("ID:");

        txtState.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txtState.setForeground(new java.awt.Color(35, 35, 38));
        txtState.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setText("Estado: ");

        txtID.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txtID.setForeground(new java.awt.Color(35, 35, 38));
        txtID.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtID, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCUIT, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblErrorDni, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 41, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCUIT, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorDni, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32.png"))); // NOI18N
        btnRegistrar.setText("Editar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(291, 291, 291)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Dirección", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        txtStreet.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtStreet.setForeground(new java.awt.Color(65, 65, 63));
        txtStreet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStreet.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Calle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14), new java.awt.Color(35, 35, 38))); // NOI18N

        txtCity.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCity.setForeground(new java.awt.Color(65, 65, 63));
        txtCity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Ciudad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14), new java.awt.Color(35, 35, 38))); // NOI18N

        txtHeight.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtHeight.setForeground(new java.awt.Color(65, 65, 63));
        txtHeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHeight.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Altura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14), new java.awt.Color(35, 35, 38))); // NOI18N
        txtHeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHeightKeyPressed(evt);
            }
        });

        cboProvinces.setBackground(new java.awt.Color(255, 255, 255));
        cboProvinces.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboProvinces.setForeground(new java.awt.Color(35, 35, 38));
        cboProvinces.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboProvinces.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Provincia");

        txtProvince.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProvince.setForeground(new java.awt.Color(35, 35, 38));
        txtProvince.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCity))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnEditClient.setBackground(new java.awt.Color(255, 255, 255));
        btnEditClient.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEditClient.setForeground(new java.awt.Color(12, 83, 151));
        btnEditClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32.png"))); // NOI18N
        btnEditClient.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnEditClient.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEditClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditClientMouseExited(evt);
            }
        });
        btnEditClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditClientActionPerformed(evt);
            }
        });

        btnBaja.setBackground(new java.awt.Color(255, 255, 255));
        btnBaja.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBaja.setForeground(new java.awt.Color(12, 83, 151));
        btnBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bin32.png"))); // NOI18N
        btnBaja.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBaja.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBaja.setFocusable(false);
        btnBaja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBajaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBajaMouseExited(evt);
            }
        });
        btnBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBajaActionPerformed(evt);
            }
        });

        btnSuspender.setBackground(new java.awt.Color(255, 255, 255));
        btnSuspender.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSuspender.setForeground(new java.awt.Color(12, 83, 151));
        btnSuspender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pause32.png"))); // NOI18N
        btnSuspender.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSuspender.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSuspender.setFocusable(false);
        btnSuspender.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSuspenderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSuspenderMouseExited(evt);
            }
        });
        btnSuspender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenderActionPerformed(evt);
            }
        });

        btnReactivar.setBackground(new java.awt.Color(255, 255, 255));
        btnReactivar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnReactivar.setForeground(new java.awt.Color(12, 83, 151));
        btnReactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/switch32.png"))); // NOI18N
        btnReactivar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnReactivar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnReactivar.setFocusable(false);
        btnReactivar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReactivarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReactivarMouseExited(evt);
            }
        });
        btnReactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReactivarActionPerformed(evt);
            }
        });

        btnHistory.setBackground(new java.awt.Color(255, 255, 255));
        btnHistory.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnHistory.setForeground(new java.awt.Color(12, 83, 151));
        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N
        btnHistory.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnHistory.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnHistory.setFocusable(false);
        btnHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHistoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHistoryMouseExited(evt);
            }
        });
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });

        btnServices.setBackground(new java.awt.Color(255, 255, 255));
        btnServices.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnServices.setForeground(new java.awt.Color(12, 83, 151));
        btnServices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeService32.png"))); // NOI18N
        btnServices.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnServices.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnServices.setFocusable(false);
        btnServices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnServicesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnServicesMouseExited(evt);
            }
        });
        btnServices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServicesActionPerformed(evt);
            }
        });

        btnDevices.setBackground(new java.awt.Color(255, 255, 255));
        btnDevices.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnDevices.setForeground(new java.awt.Color(12, 83, 151));
        btnDevices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/device32.png"))); // NOI18N
        btnDevices.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnDevices.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnDevices.setFocusable(false);
        btnDevices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDevicesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDevicesMouseExited(evt);
            }
        });
        btnDevices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevicesActionPerformed(evt);
            }
        });

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
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnReactivar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDevices, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuspender, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(101, 129, 171));

        icono.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(255, 255, 255));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cliente128.png"))); // NOI18N
        icono.setText("Datos del cliente.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icono, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyPressed
        txtTel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtTel.getText().length() >= 15) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtTelKeyPressed

    private void txtCUITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCUITActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCUITActionPerformed

    private void txtCUITKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCUITKeyPressed
        txtCUIT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCUIT.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCUITKeyPressed

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

    private void txtHeightKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHeightKeyPressed
        txtHeight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtHeight.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtHeightKeyPressed

    private void btnEditClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditClientMouseEntered
        btnEditClient.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnEditClientMouseEntered

    private void btnEditClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditClientMouseExited
        btnEditClient.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditClientMouseExited

    private void btnEditClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditClientActionPerformed

    }//GEN-LAST:event_btnEditClientActionPerformed

    private void btnBajaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBajaMouseEntered
        btnBaja.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnBajaMouseEntered

    private void btnBajaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBajaMouseExited
        btnBaja.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBajaMouseExited

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed

    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnSuspenderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuspenderMouseEntered
        btnSuspender.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnSuspenderMouseEntered

    private void btnSuspenderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuspenderMouseExited
        btnSuspender.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSuspenderMouseExited

    private void btnSuspenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuspenderActionPerformed

    private void btnReactivarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReactivarMouseEntered
        btnReactivar.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnReactivarMouseEntered

    private void btnReactivarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReactivarMouseExited
        btnReactivar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnReactivarMouseExited

    private void btnReactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReactivarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReactivarActionPerformed

    private void btnHistoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistoryMouseEntered
        btnHistory.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnHistoryMouseEntered

    private void btnHistoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistoryMouseExited
        btnHistory.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnHistoryMouseExited

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnServicesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServicesMouseEntered
        btnServices.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnServicesMouseEntered

    private void btnServicesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServicesMouseExited
        btnServices.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnServicesMouseExited

    private void btnServicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServicesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnServicesActionPerformed

    private void btnDevicesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDevicesMouseEntered
        btnDevices.setBackground(new Color(245,248,255));
    }//GEN-LAST:event_btnDevicesMouseEntered

    private void btnDevicesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDevicesMouseExited
        btnDevices.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnDevicesMouseExited

    private void btnDevicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevicesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDevicesActionPerformed

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
    private javax.swing.JLabel lblErrorDni;
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
