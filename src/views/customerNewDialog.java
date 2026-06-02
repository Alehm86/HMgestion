/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.customerDAO;
import dao.genericDAO;
import models.Customer;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import utils.utility;


public class customerNewDialog extends javax.swing.JDialog {

    genericDAO qGeneric = new genericDAO();
    customerDAO qCustomer = new customerDAO();
    
    utility utils = new utility();
    
    Customer mCustomer = new Customer();
  
    String cuitCustomer = "";
    String fecha = utils.fecha();
    
    boolean estado = false;
    
    public boolean dialogoAltaCliente(){
        return estado;
    }
    
    public String getCuitClient(){
       return cuitCustomer;
    }
    
      
    public customerNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        startMsjs();
        llenarCombos();
        buttonActions(); 
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
        lblErrorTipoCustomer.setText("");
        lblErrorName.setText("");
        lblErrorDni.setText("");
        lblErrorTel.setText("");
        lblErrorEmail.setText("");
    }

    private void llenarCombos(){       
        qGeneric.llenarCombos(cboIVA, "customer_iva");
        qGeneric.llenarCombosActivos(cboProvinces, "provinces");
    }
    
    private void buttonActions(){
            
        btnCancel.addActionListener(e-> {
            limpiar();
        });
        
        btnRegistrar.addActionListener(e-> {           
            insertCustomer();
        });
        
        utils.clearMsjErrorTxt(txtName, lblErrorName);
        utils.clearMsjErrorTxt(txtCUIT, lblErrorDni);
        utils.clearMsjErrorTxt(txtTel, lblErrorTel);
        utils.clearMsjErrorCombo(cboIVA,lblErrorTipoCustomer);
        
        
     
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
        
    }
    
    private void limpiar(){
        txtName.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtCUIT.setText("");
        txtStreet.setText("");
        txtHeight.setText("");
        txtCity.setText("");
        cboProvinces.setSelectedIndex(0);
        cboIVA.setSelectedIndex(0);
        startMsjs();
    }
    
    private void insertCustomer(){    
        
        String msjError ="Complete el campo!";
        boolean valido = true;
        int id_customer;
        
        String cuitText = txtCUIT.getText().trim();
        mCustomer.state = 1;
        mCustomer.fechaRegistro = fecha;
        
        if (cboIVA.getSelectedIndex() > 0) { 
            mCustomer.iva = qGeneric.selectId(
                    "id_iva",
                    "iva",
                    cboIVA.getSelectedItem().toString()
            );
        } else {
            lblErrorTipoCustomer.setText("Debe seleccionar una opción.");
            valido = false;
        }

        if(!txtName.getText().isEmpty()){           
            if (txtName.getText().trim().length() < 5) {
                lblErrorName.setText("El nombre debe contener al menos 5 caracteres.");
            } else {
                mCustomer.name = txtName.getText().trim();
            }              
        } else {
            lblErrorName.setText(msjError);
            valido = false;
        }       
        
        if(!txtEmail.getText().isEmpty()){
            mCustomer.email = txtEmail.getText().toLowerCase();
        } else {
            mCustomer.email = null;

        } 

        if(!txtTel.getText().isEmpty()){
            if (txtTel.getText().length() < 10) {
                lblErrorTel.setText("El número de telefono debe tener al menos 10 numeros");
            }else{
                mCustomer.phone = txtTel.getText().trim();
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
                mCustomer.CUIT= cuitText;  
            }
        } else {
            lblErrorDni.setText(msjError);
            valido = false;
        } 
        
        if(!txtStreet.getText().isEmpty()){
            mCustomer.street = txtStreet.getText().trim().toUpperCase();
        } else {
            mCustomer.street = null;
        }          

        if(!txtHeight.getText().isEmpty()){
            mCustomer.height = Integer.parseInt(txtHeight.getText().trim());
        } else {
            mCustomer.height = 0;
        }  

        if(!txtCity.getText().isEmpty()){
            mCustomer.city = txtCity.getText().trim().toUpperCase();
        } else {
            mCustomer.city = null;
        } 

        if (cboProvinces.getSelectedIndex() > 0) { 
            mCustomer.idProvince = qGeneric.selectId("id_province","provinces",cboProvinces.getSelectedItem().toString());
        } else {
            mCustomer.idProvince = 25;
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
                   
        id_customer = qCustomer.insertCustomer(
                mCustomer.getName(),
                mCustomer.getIva(),
                mCustomer.getCUIT(),
                mCustomer.getEmail(),
                mCustomer.getPhone(),
                mCustomer.getStreet(),
                mCustomer.getHeight(),
                mCustomer.getCity(),
                mCustomer.getIdProvince(),
                mCustomer.getFechaRegistro(),
                mCustomer.getState()    
        );
        
        qCustomer.insertCustomerHistory(id_customer, "Alta cliente", "S/D");
        
        estado = true;
        
        cuitCustomer = mCustomer.getCUIT();
        
        this.dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        txtCUIT = new javax.swing.JTextField();
        cboIVA = new javax.swing.JComboBox<>();
        lblErrorTipoCustomer = new javax.swing.JLabel();
        lblErrorName = new javax.swing.JLabel();
        lblErrorDni = new javax.swing.JLabel();
        lblErrorTel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblErrorEmail = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtStreet = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        cboProvinces = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Tipo de cliente");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("DNI / CUIT");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Teléfono");

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setForeground(new java.awt.Color(65, 65, 63));
        txtName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setForeground(new java.awt.Color(65, 65, 63));
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelKeyPressed(evt);
            }
        });

        txtCUIT.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCUIT.setForeground(new java.awt.Color(65, 65, 63));
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

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(65, 65, 63));
        cboIVA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lblErrorTipoCustomer.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorTipoCustomer.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorTipoCustomer.setText("mensaje error!");
        lblErrorTipoCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblErrorName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorName.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorName.setText("mensaje error!");
        lblErrorName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

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
        txtEmail.setForeground(new java.awt.Color(65, 65, 63));
        txtEmail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblErrorEmail.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorEmail.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorEmail.setText("mensaje error!");
        lblErrorEmail.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtName)
                    .addComponent(cboIVA, 0, 272, Short.MAX_VALUE)
                    .addComponent(txtTel)
                    .addComponent(txtCUIT)
                    .addComponent(txtEmail))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblErrorTipoCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(lblErrorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorDni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorTel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorTipoCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(291, 291, 291)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(256, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
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
        cboProvinces.setForeground(new java.awt.Color(65, 65, 63));
        cboProvinces.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cboProvinces.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Provincia");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCity)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cboProvinces, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(101, 129, 171));

        icono.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        icono.setForeground(new java.awt.Color(255, 255, 255));
        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/agregar-usuario64.png"))); // NOI18N
        icono.setText("Alta cliente.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icono, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(icono)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                customerNewDialog dialog = new customerNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboProvinces;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblErrorDni;
    private javax.swing.JLabel lblErrorEmail;
    private javax.swing.JLabel lblErrorName;
    private javax.swing.JLabel lblErrorTel;
    private javax.swing.JLabel lblErrorTipoCustomer;
    private javax.swing.JTextField txtCUIT;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtStreet;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
}
